package com.example.demo700.Services.TurfServices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.demo700.DTOFiles.BookingRequestDto;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Turf.Booking;
import com.example.demo700.Models.Turf.Discount;
import com.example.demo700.Models.Turf.GroupBooking;
import com.example.demo700.Models.Turf.Venue;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Turf.BookingRepository;
import com.example.demo700.Repositories.Turf.DiscountRepository;
import com.example.demo700.Repositories.Turf.GroupBookingRepository;
import com.example.demo700.Repositories.Turf.VenueRepository;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private VenueRepository venueRepository;
	@Autowired
	private GroupBookingRepository groupBookingRepository;

	@Autowired
	private DiscountRepository discountRespository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Booking createBooking(BookingRequestDto request) {
		
		if(request == null) {
			
			return null;
			
		}
		
		User user = userRepository.findById(request.getUserId()).get();
		
		if(user == null) {
			
			return null;
			
		}
		
		
		
		Venue venue = venueRepository.findById(request.getVenueId())
				.orElseThrow(() -> new IllegalArgumentException("Venue not found"));

		System.out.println("request group :- " + request.isGroup());
		
		if(request.getStartTime().isAfter(request.getEndTime())) {
			
			return null;
			
		}

		List<Booking> overlapped = bookingRepository.findByVenueIdAndEndTimeAfterAndStartTimeBefore(
				request.getVenueId(), request.getStartTime(), request.getEndTime());

		if (!overlapped.isEmpty()) {
			return null;
		}

		long seconds = Duration.between(request.getStartTime(), request.getEndTime()).getSeconds();
		double hours = Math.max(0.5, seconds / 3600.0);

		double multiplier = 1.0;
		if (venue.getPricingPolicy() != null && venue.getPricingPolicy().containsKey("peak_multiplier")) {
			multiplier = venue.getPricingPolicy().get("peak_multiplier");
		}

		double amount = hours * venue.getBasePricePerHour() * multiplier;

		List<Discount> discount = discountRespository.findByOwnerIdAndVenueId(request.getUserId(), venue.getId());

		double offer = 0.0;
		int usages = 0;

		Discount _discount = null;

		if (discount != null) {

			for (Discount i : discount) {

				if (i.getExpiry() != null && !i.getExpiry().isBefore(Instant.now()) && i.getUsageLimit() > 0
						&& i.getPercentage() >= offer) {

					offer = Math.max(i.getPercentage(), offer);
					usages = i.getUsageLimit() - 1;

					_discount = i;

				}

			}

		}

		if (_discount != null) {

			_discount.setUsageLimit(usages);

			discountRespository.save(_discount);

		}

		amount -= (offer / 100) * amount;

		Booking b = new Booking();
		b.setVenueId(venue.getId());
		b.setUserId(request.getUserId());
		b.setStartTime(request.getStartTime());
		b.setEndTime(request.getEndTime());
		b.setAmount(amount);
		b.setStatus(com.example.demo700.ENUMS.BookingStatus.PENDING);

		if (request.isGroup()) {

			GroupBooking gb = new GroupBooking();
			gb.setBookingId(null);
			gb.setMemberIds(List.of(request.getUserId()));
			gb.setPerMemberAmount(amount);
			gb.setStatus(com.example.demo700.ENUMS.GroupBookingStatus.PENDING);
			groupBookingRepository.save(gb);
			b.setGroupBookingId(gb.getId());
			b.setGroupBooking(true);
		}

		Booking saved = bookingRepository.save(b);

		if (saved.isGroupBooking()) {
			GroupBooking gb = groupBookingRepository.findById(saved.getGroupBookingId()).orElse(null);
			if (gb != null) {
				gb.setBookingId(saved.getId());
				groupBookingRepository.save(gb);
			}
		}

		return saved;
	}

	@Override
	public List<Booking> seeAll() {

		return bookingRepository.findAll();
	}

	@Override
	public Booking updateBooking(String id, BookingRequestDto request) {

		Booking booking = bookingRepository.findById(id).get();
		GroupBooking groupBooking = null;

		if (booking == null) {

			groupBooking = groupBookingRepository.findById(id).get();

		}

		if (booking == null && groupBooking == null) {

			return null;

		}

		request.setUserId(id);

		Venue venue = venueRepository.findById(request.getVenueId())
				.orElseThrow(() -> new IllegalArgumentException("Venue not found"));

		List<Booking> overlapped = bookingRepository.findByVenueIdAndEndTimeAfterAndStartTimeBefore(
				request.getVenueId(), request.getStartTime(), request.getEndTime());

		int index = 0, removedIndex = -1;

		for (Booking i : overlapped) {

			if (i.getId().equals(id)) {

				removedIndex = index;

			}

			++index;

		}

		if (removedIndex != -1) {

			overlapped.remove(removedIndex);

		}

		if (!overlapped.isEmpty()) {
			return null;
		}

		long seconds = Duration.between(request.getStartTime(), request.getEndTime()).getSeconds();
		double hours = Math.max(0.5, seconds / 3600.0);

		double multiplier = 1.0;
		if (venue.getPricingPolicy() != null && venue.getPricingPolicy().containsKey("peak_multiplier")) {
			multiplier = venue.getPricingPolicy().get("peak_multiplier");
		}

		double amount = hours * venue.getBasePricePerHour() * multiplier;

		List<Discount> discount = discountRespository.findByOwnerIdAndVenueId(request.getUserId(), venue.getId());

		double offer = 0.0;
		int usages = 0;

		Discount _discount = null;

		if (discount != null) {

			for (Discount i : discount) {

				if (i.getExpiry() != null && !i.getExpiry().isBefore(Instant.now()) && i.getUsageLimit() > 0
						&& i.getPercentage() >= offer && i.getVenueId().equals(venue.getId())) {

					offer = Math.max(i.getPercentage(), offer);
					usages = i.getUsageLimit() - 1;

					_discount = i;

				}

			}

		}

		if (_discount != null) {

			_discount.setUsageLimit(usages);

			discountRespository.save(_discount);

		}

		amount -= (offer / 100) * amount;

		Booking b = new Booking();
		b.setVenueId(venue.getId());
		b.setUserId(request.getUserId());
		b.setStartTime(request.getStartTime());
		b.setEndTime(request.getEndTime());
		b.setAmount(amount);
		b.setStatus(com.example.demo700.ENUMS.BookingStatus.PENDING);

		if (request.isGroup()) {

			GroupBooking gb = new GroupBooking();
			gb.setBookingId(null);
			gb.setMemberIds(List.of(request.getUserId()));
			gb.setPerMemberAmount(amount);
			gb.setStatus(com.example.demo700.ENUMS.GroupBookingStatus.PENDING);
			groupBookingRepository.save(gb);
			b.setGroupBookingId(gb.getId());
			b.setGroupBooking(true);
		}

		Booking saved = bookingRepository.save(b);

		if (saved.isGroupBooking()) {
			GroupBooking gb = groupBookingRepository.findById(saved.getGroupBookingId()).orElse(null);
			if (gb != null) {
				gb.setBookingId(saved.getId());
				groupBookingRepository.save(gb);
			}
		}

		return saved;

	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean deleteBooking(String id, boolean isGroup, String givenUserId) {

		if (id == null) {

			return false;

		}

		if (isGroup) {

			GroupBooking groupBooking = groupBookingRepository.findById(id).get();

			if (groupBooking == null) {

				return false;

			} else {

				User _user = userRepository.findById(givenUserId).get();

				if (_user == null) {

					return false;

				}

				if (_user.getRoles().contains(Role.valueOf("ROLE_ADMIN"))) {

					long count = groupBookingRepository.count();

					groupBookingRepository.deleteById(id);

					if (count != groupBookingRepository.count()) {

						return true;

					} else {

						return false;

					}

				}

				List<String> list = groupBooking.getMemberIds();

				if (!list.contains(givenUserId)) {

					return false;

				}

				long count = groupBookingRepository.count();

				groupBookingRepository.deleteById(id);

				if (count != groupBookingRepository.count()) {

					return true;

				} else {

					return false;

				}

			}

		} else {

			Booking groupBooking = bookingRepository.findById(id).get();

			if (groupBooking == null) {

				return false;

			} else {

				String userId = groupBooking.getUserId();

				User user = userRepository.findById(userId).get();

				User _user = userRepository.findById(givenUserId).get();

				if (_user == null) {

					return false;

				}

				if (_user.getRoles().contains(Role.valueOf("ROLE_ADMIN"))) {

					long count = bookingRepository.count();

					bookingRepository.deleteById(id);

					if (count != groupBookingRepository.count()) {

						return true;

					} else {

						return false;

					}

				} else if (user.getId().equals(givenUserId)) {

					long count = bookingRepository.count();

					bookingRepository.deleteById(id);

					if (count != groupBookingRepository.count()) {

						return true;

					} else {

						return false;

					}

				} else {

					return false;

				}

			}

		}

	}

	@Override
	public Booking updateBookingStatus(String bookingId, String ownerId, String status) {

		if (bookingId == null | ownerId == null || status == null) {

			return null;

		}

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new IllegalArgumentException("Booking not found"));

		Venue venue = venueRepository.findById(booking.getVenueId())
				.orElseThrow(() -> new IllegalArgumentException("Venue not found"));

		if (!venue.getOwnerId().equals(ownerId)) {
			throw new SecurityException("Only the venue owner can change booking status!");
		}

		try {
			com.example.demo700.ENUMS.BookingStatus newStatus = com.example.demo700.ENUMS.BookingStatus
					.valueOf(status.toUpperCase());
			booking.setStatus(newStatus);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid booking status value!");
		}

		booking = bookingRepository.save(booking);

		if (booking == null) {

			return null;

		}

		return booking;

	}

}
