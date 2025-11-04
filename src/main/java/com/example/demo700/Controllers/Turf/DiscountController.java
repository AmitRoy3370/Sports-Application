package com.example.demo700.Controllers.Turf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo700.Models.Turf.Discount;
import com.example.demo700.Services.TurfServices.DiscountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {

	@Autowired
	private DiscountService discountService;

	@PostMapping("/addDiscount")
	public ResponseEntity<?> create(@Validated @RequestBody Discount discount, @RequestParam String userId) {

		if (discount == null) {

			return ResponseEntity.status(400).body("Data is not added as no data at here...");

		}

		try {

			Discount _discount = discountService.createDiscount(discount, userId);

			if (_discount == null) {

				return ResponseEntity.status(400).body("Data is not added");

			}

			return ResponseEntity.ok(_discount);

		} catch (Exception e) {

			return ResponseEntity.status(400).body(e.getMessage());

		}
	}

	@GetMapping("/{code}")
	public ResponseEntity<?> getByCode(@PathVariable String code) {
		
		try {
			
			Discount discount = discountService.findByCode(code);
			
			if(discount == null) {
				
				throw new Exception();
				
			}
			
			return ResponseEntity.status(200).body(discount);
			
		} catch(Exception e) {
			
			return ResponseEntity.status(404).body("invalid code request...");
			
		}
		
	}

	@GetMapping("/isValid")
	public ResponseEntity<?> isValidDiscount(@RequestParam String code) {

		if (code == null) {

			return ResponseEntity.status(400).body("This is false request...");

		}

		boolean yes = discountService.isValidDiscount(code);

		if (!yes) {

			return ResponseEntity.status(400).body("Not valid...");

		}

		return ResponseEntity.status(200).body("Yes this is a valid discount...");

	}

	@GetMapping("/seeAll")
	public ResponseEntity<?> seeAllDiscount(@RequestParam String userId) {

		List<Discount> list = discountService.seeAllDiscount(userId);

		if (list == null) {

			return ResponseEntity.status(404).body("No, discount find at here...");

		}

		return ResponseEntity.status(200).body(list);

	}

	@GetMapping("/serachDiscount")
	public ResponseEntity<?> findDiscount(@RequestParam String id) {

		if (id == null) {

			return ResponseEntity.status(400).body("False request...");

		}

		Discount discount = discountService.findDiscount(id);

		if (discount == null) {

			return ResponseEntity.status(404).body("Discount not find...");

		}

		return ResponseEntity.status(200).body(discount);

	}

	@GetMapping("/findUsersDiscount")
	public ResponseEntity<?> findUsersDiscount(@RequestParam String userId) {

		if (userId == null) {

			return ResponseEntity.status(400).body("false request...");

		}

		List<Discount> list = discountService.findUsersDiscount(userId);

		if (list.isEmpty()) {

			return ResponseEntity.status(404).body("No discount at here...");

		}

		return ResponseEntity.status(200).body(list);

	}

	@PutMapping("/update")
	public ResponseEntity<?> updateDiscount(@RequestParam String discountId, @RequestBody Discount discount,
			@RequestParam String userId) {

		try {

			Discount _discount = discountService.updateDiscount(discountId, discount, userId);

			if (_discount == null) {

				return ResponseEntity.status(400).body("Not updated..");

			} else {

				return ResponseEntity.status(200).body(_discount);

			}

		} catch (Exception e) {

			return ResponseEntity.status(400).body("session expire");

		}

	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteDiscount(@RequestParam String discountId, @RequestParam String userId) {

		if (discountId == null || userId == null) {

			return ResponseEntity.status(400).body("false request...");

		}

		boolean yes = discountService.deleteDiscount(discountId, userId);

		if (!yes) {

			return ResponseEntity.status(400).body("Not deleted...");

		}

		return ResponseEntity.status(200).body("discount removed...");

	}

}
