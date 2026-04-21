package com.example.demo700.Services.TurfServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.MatchResponse;
import com.example.demo700.DTOFiles.VenueResponse;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Models.EventOrganaizer.MatchVenue;
import com.example.demo700.Models.Turf.Booking;
import com.example.demo700.Models.Turf.Discount;
import com.example.demo700.Models.Turf.Owner;
import com.example.demo700.Models.Turf.Venue;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchVenueRepository;
import com.example.demo700.Repositories.Turf.BookingRepository;
import com.example.demo700.Repositories.Turf.DiscountRepository;
import com.example.demo700.Repositories.Turf.OwnerRepository;
import com.example.demo700.Repositories.Turf.VenueLocationServiceRepository;
import com.example.demo700.Repositories.Turf.VenueRepository;
import com.example.demo700.Services.EventOrganaizer.MatchService;
import com.example.demo700.Validator.AddressValidator;
import com.example.demo700.Validator.URLValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

	@Autowired
	private VenueRepository venueRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@Autowired
	private OwnerRepository ownerRepository;

	URLValidator urlValidator = new URLValidator();
	AddressValidator adressValidator = new AddressValidator();

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private DiscountRepository discountRepository;

	@Autowired
	private MatchService matchService;

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public Venue createVenue(Venue v) {

		System.out.println("I am now working to add a venue");

		try {

			if (v == null || v.getAddress().isEmpty() || v.getName().isEmpty() || v.getOwnerId().isEmpty()
					|| v.getBasePricePerHour() <= 0.00 || v.getAmenities().isEmpty() || v.getPhotos().isEmpty()
					|| v.getSportsSupported().isEmpty()) {

				// System.out.println("not enough data...");

				throw new Exception("Have to put all the required arguments at here");

			} else if (!urlValidator.isValid(v.getPhotos())) {

				// System.out.println("url is not valid...");

				throw new Exception("Have to put all the urls perfectly at here");

			} else if (!adressValidator.isValidAddress(v.getAddress())) {

				// System.out.println("adress is not valid...");

				throw new Exception("Have to put all the perfect address at here");

			} else {

				User user = userRepository.findById(v.getOwnerId()).get();

				if (user == null) {

					// System.out.println("user not find...");

					throw new Exception("Have to put all the perfect userId at here");

				} else if (!user.getRoles().contains(Role.valueOf("ROLE_OWNER"))) {

					// System.out.println("Only role owner can add it...");

					throw new Exception("Only turf owner can add venue");

				}

			}

		} catch (Exception e) {

			System.out.println(e.getMessage());

			return null;

		}

		try {

			Owner owner = ownerRepository.searchByUserId(v.getOwnerId());

			if (owner == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			System.out.println(e);

			return null;

		}

		try {

			Venue venue = venueRepository.findByNameIgnoreCase(v.getName());

			if (venue != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Already added venue with this name...");

		} catch (Exception e) {

		}

		Venue venue = venueRepository.save(v);

		if (venue == null) {

			// System.out.println("venue is not added at here...");

			return null;

		}

		System.out.println(venue.toString());

		return venue;
	}

	@Override
	public VenueResponse getVenueById(String id) {
		return getVenueResponseFromVenue(venueRepository.findById(id).get());
	}

	@Override
	public List<VenueResponse> getAllVenue() {

		List<Venue> list = venueRepository.findAll();

		if (list.isEmpty()) {

			return null;

		}

		return getVenueResponseFromVenueList(list);

	}

	@Override
	public VenueResponse findBySpecificUser(String venueId, String userId) {

		if (venueId == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		User user;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such valid user find at here...");

		}

		try {

			VenueResponse gettedResponse = getVenueResponseFromVenue(venueRepository.findById(venueId).get());

			List<Booking> bookings = gettedResponse.getBookings();

			bookings = bookings.stream().filter(Objects::nonNull).filter(booking -> booking.getUserId().equals(userId))
					.collect(Collectors.toList());

			List<MatchResponse> matches = gettedResponse.getMatches();

			matches = matches.stream().filter(Objects::nonNull).filter(
					match -> match.getOrganaizerName() != null && match.getOrganaizerName().equals(user.getName()))
					.collect(Collectors.toList());

			gettedResponse.setBookings(bookings);
			gettedResponse.setMatches(matches);

			return gettedResponse;

		} catch (Exception e) {

			throw new NoSuchElementException("No such venue find at here...");

		}

	}

	@Override
	public List<VenueResponse> searchByAddress(String q) {
		return getVenueResponseFromVenueList(venueRepository.findByAddressContainingIgnoreCase(q));
	}

	@Override
	public Venue updateVeue(String id, Venue v) {

		if (id == null) {

			return null;

		}

		try {

			if (venueRepository.findById(id).get() == null) {

				return null;

			}

		} catch (Exception e) {

			throw new ArithmeticException("Your venue id is not valid...");

		}

		try {

			if (v == null || v.getAddress().isEmpty() || v.getName().isEmpty() || v.getOwnerId().isEmpty()
					|| v.getBasePricePerHour() <= 0.00 || v.getAmenities().isEmpty() || v.getPhotos().isEmpty()
					|| v.getSportsSupported().isEmpty()) {

				throw new Exception("Have to put all the required arguments at here");

			} else if (!urlValidator.isValid(v.getPhotos())) {

				throw new Exception("Have to put all the urls perfectly at here");

			} else if (!adressValidator.isValidAddress(v.getAddress())) {

				throw new Exception("Have to put all the perfect address at here");

			} else {

				User user = userRepository.findById(v.getOwnerId()).get();

				if (user == null) {

					throw new Exception("Have to put all the perfect userId at here");

				} else if (!user.getRoles().contains(Role.valueOf("ROLE_OWNER"))) {

					throw new Exception("Only turf owner can add venue");

				}

			}

		} catch (Exception e) {

			System.out.println(e);

			return null;

		}

		try {

			Venue venue = venueRepository.findByNameIgnoreCase(v.getName());

			if (venue != null) {

				if (!venue.getId().equals(id)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Already added venue with this name...");

		} catch (Exception e) {

		}

		v.setId(id);

		Venue venue = venueRepository.save(v);

		return venue;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean removeVenue(String id, String userId) {

		if (id == null || userId == null) {

			return false;

		}

		User user = userRepository.findById(userId).get();

		if (user == null) {

			return false;

		}

		Venue venue = venueRepository.findById(id).get();

		if (venue == null) {

			return false;

		}

		if (user.getRoles().contains(Role.valueOf("ROLE_ADMIN"))) {

			long count = venueRepository.count();

			cleaner.removeVenue(id);

			if (venueRepository.count() != count) {

				return true;

			}

		} else if (userId.equals(venue.getOwnerId()) && user.getRoles().contains(Role.valueOf("ROLE_OWNER"))) {

			long count = venueRepository.count();

			cleaner.removeVenue(id);

			if (venueRepository.count() != count) {

				return true;

			}

		}

		return false;
	}

	@Override
	public VenueResponse findByName(String name) {

		if (name == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Venue venue = venueRepository.findByNameIgnoreCase(name);

			if (venue == null) {

				throw new Exception("No such venue find at here...");

			}

			return getVenueResponseFromVenue(venue);

		} catch (Exception e) {

			throw new NoSuchElementException(e.getMessage());

		}

	}

	private VenueResponse getVenueResponseFromVenue(Venue venue) {

		List<Venue> venues = new ArrayList<>();

		venues.add(venue);

		return getVenueResponseFromVenueList(venues).get(0);

	}

	private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());;

	private List<VenueResponse> getVenueResponseFromVenueList(List<Venue> venues) {

		List<VenueResponse> responses = new ArrayList<>();

		List<String> allVenueId = venues.stream().map(Venue::getId).collect(Collectors.toList());

		List<String> allOwnersId = venues.stream().map(Venue::getOwnerId).collect(Collectors.toList());

		CompletableFuture<Map<String, User>> userFuture = CompletableFuture.supplyAsync(
				() -> userRepository.findAllById(allOwnersId).isEmpty() ? new HashMap<>()
						: userRepository.findAllById(allOwnersId).stream().filter(Objects::nonNull).collect(Collectors
								.toMap(User::getId, Function.identity(), (existing, replacement) -> existing)),
				executor);

		CompletableFuture<Map<String, List<Booking>>> bookingFuture = CompletableFuture.supplyAsync(() -> {
			List<Booking> bookings = bookingRepository.findByVenueIdIn(allVenueId);

			if (bookings == null || bookings.isEmpty()) {
				return new HashMap<>();
			}

			// ভেন্যু আইডি অনুযায়ী বুকিং গ্রুপ করুন
			return bookings.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(Booking::getVenueId));
		}, executor);

		CompletableFuture<Map<String, List<MatchResponse>>> matchFuture = CompletableFuture.supplyAsync(() -> {
			List<MatchResponse> matches = matchService.seeAllMatch();

			if (matches == null || matches.isEmpty()) {
				return new HashMap<>();
			}

			// ম্যাচ ভেন্যু আইডি অনুযায়ী গ্রুপ করুন
			return matches.stream().filter(Objects::nonNull).filter(match -> match.getMatchVenueId() != null) // নাল
																												// ভেন্যু
																												// আইডি
																												// ফিল্টার
					.collect(Collectors.groupingBy(MatchResponse::getMatchVenueId));
		}, executor);

		CompletableFuture<Map<String, Discount>> discountFuture = CompletableFuture
				.supplyAsync(
						() -> discountRepository.findByVenueIdIn(allVenueId)
								.isEmpty()
										? new HashMap<>()
										: discountRepository.findByVenueIdIn(allVenueId).stream()
												.filter(Objects::nonNull).collect(Collectors.toMap(Discount::getVenueId,
														Function.identity(), (existing, replacement) -> existing)),
						executor);

		CompletableFuture.allOf(userFuture, bookingFuture, matchFuture, discountFuture).join();

		Map<String, User> userMap = userFuture.join();
		Map<String, List<Booking>> bookingMap = bookingFuture.join();

		Map<String, List<MatchResponse>> matchResponseMap = matchFuture.join();
		Map<String, Discount> discountMap = discountFuture.join();

		for (Venue venue : venues) {

			try {

				VenueResponse response = new VenueResponse();

				try {

					response.setId(venue.getId());
					response.setAddress(venue.getAddress());
					response.setName(venue.getName());
					response.setOwnerId(venue.getOwnerId());

					try {

						response.setOwnerName(userMap.get(venue.getOwnerId()).getName());

					} catch (Exception e) {

					}

					try {

						response.setAmenities(venue.getAmenities());

					} catch (Exception e) {

					}

					response.setBasePricePerHour(venue.getBasePricePerHour());

					try {

						response.setPhotos(venue.getPhotos());

					} catch (Exception e) {

					}

					try {

						response.setPricingPolicy(venue.getPricingPolicy());

					} catch (Exception e) {

					}

					try {

						response.setSportsSupported(venue.getSportsSupported());

					} catch (Exception e) {

					}

					try {

						response.setMatches(matchResponseMap.get(venue.getId()));

					} catch (Exception e) {

					}

				} catch (Exception e) {

				}

				try {

					response.setBookings(bookingMap.get(venue.getId()));

				} catch (Exception e) {

				}

				try {

					Discount discount = discountMap.get(venue.getId());

					response.setDiscountId(discount.getId());
					response.setDiscountCode(discount.getCode());
					response.setExpiry(discount.getExpiry());
					response.setUsageLimit(discount.getUsageLimit());
					response.setPercentage(discount.getPercentage());

				} catch (Exception e) {

				}

				responses.add(response);

			} catch (Exception e) {

			}

		}

		return responses;

	}

}
