package com.example.demo700.Services.TurfServices;

import java.util.List;
import java.util.Optional;

import com.example.demo700.Models.Turf.Discount;

public interface DiscountService {
	Discount createDiscount(Discount discount, String userId);

	Optional<Discount> findByCode(String code);

	boolean isValidDiscount(String code);

	Discount findDiscount(String id);

	List<Discount> findUsersDiscount(String ownerId);

	List<Discount> seeAllDiscount(String userId);
	
	boolean deleteDiscount(String discountId, String userId);
	
	Discount updateDiscount(String discountId, Discount updatedDiscount, String userId);

}
