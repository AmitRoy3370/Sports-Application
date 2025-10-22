package com.example.demo700.Services.TurfServices;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Turf.Discount;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Turf.DiscountRepository;
import com.example.demo700.Repositories.Turf.VenueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

	@Autowired
	private DiscountRepository discountRepository;

	@Autowired
	private UserRepository userrepository;
	
	@Autowired
	private VenueRepository venueRepository;

	@Override
	public Discount createDiscount(Discount discount, String userId) {

		if (discount.getExpiry() == null || discount.getExpiry().isBefore(Instant.now())) {
			throw new IllegalArgumentException("Expiry date invalid");
		} else if(venueRepository.findById(discount.getVenueId()).get() == null  || !venueRepository.findById(discount.getVenueId()).get().getOwnerId().equals(userId)) {
			throw new IllegalArgumentException("You can add discount only for your created venue..");
		}
		discount =  discountRepository.save(discount);
		
		if(discount == null) {
			
			return null;
			
		}
		
		return discount;
		
	}

	@Override
	public Optional<Discount> findByCode(String code) {
		return discountRepository.findByCode(code);
	}

	@Override
	public boolean isValidDiscount(String code) {
		Optional<Discount> disc = discountRepository.findByCode(code);
		return disc.isPresent() && disc.get().getExpiry().isAfter(Instant.now()) && disc.get().getUsageLimit() > 0;
	}

	@Override
	public Discount findDiscount(String id) {

		if (id == null) {

			return null;

		}

		if (discountRepository.findById(id).get() == null) {

			return null;

		}

		return discountRepository.findById(id).get();

	}

	@Override
	public List<Discount> findUsersDiscount(String ownerId) {

		if (ownerId == null) {

			return null;

		}

		List<Discount> list = discountRepository.findByOwnerId(ownerId);

		if (list.isEmpty()) {

			return null;

		}

		return list;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public List<Discount> seeAllDiscount(String userId) {

		if (userId == null) {

			return null;

		}

		User user = userrepository.findById(userId).get();

		if (user == null) {

			return null;

		}

		if (!user.getRoles().contains(Role.valueOf("ROLE_ADMIN"))) {
			
			System.out.println("not an admin");

			return null;

		}

		return discountRepository.findAll();
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean deleteDiscount(String discountId, String userId) {

		if (discountId == null || userId == null) {

			return false;

		}

		User user = userrepository.findById(userId).get();

		if (user == null) {

			return false;

		}

		Discount discount = discountRepository.findById(discountId).get();

		if (discount == null) {

			return false;

		}

		if (user.getRoles().contains(Role.valueOf("ROLE_ADMIN"))) {

			long count = discountRepository.count();

			discountRepository.deleteById(discountId);

			if (discountRepository.count() != count) {

				return true;

			} else {

				return false;

			}

		} else if (discount.getOwnerId().equals(userId)) {

			long count = discountRepository.count();

			discountRepository.deleteById(discountId);

			if (discountRepository.count() != count) {

				return true;

			} else {

				return false;

			}

		}

		return false;
	}

	@Override
	public Discount updateDiscount(String discountId, Discount updatedDiscount, String userId) {
		
		if(discountId == null || updatedDiscount == null || userId == null) {
			
			return null;
			
		}
		
		Discount discount = discountRepository.findById(discountId).get();
		
		if(discount == null) {
			
			return null;
			
		}
		
		discount.setId(discountId);
		
		if (discount.getExpiry() == null || discount.getExpiry().isBefore(Instant.now())) {
			throw new IllegalArgumentException("Expiry date invalid");
		}
		
		discount = discountRepository.save(updatedDiscount);
		
		if(discount == null) {
			
			return null;
			
		}
		
		return discount;
	}

}
