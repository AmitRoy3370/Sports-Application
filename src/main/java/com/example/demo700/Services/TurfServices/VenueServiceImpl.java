package com.example.demo700.Services.TurfServices;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Turf.Owner;
import com.example.demo700.Models.Turf.Venue;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Turf.OwnerRepository;
import com.example.demo700.Repositories.Turf.VenueRepository;
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

			Venue venue = venueRepository.findByNameCaseSensitive(v.getName());

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
	public Optional<Venue> getVenueById(String id) {
		return venueRepository.findById(id);
	}

	@Override
	public List<Venue> getAllVenue() {

		List<Venue> list = venueRepository.findAll();

		if (list.isEmpty()) {

			return null;

		}

		return list;

	}

	@Override
	public List<Venue> searchByAddress(String q) {
		return venueRepository.findByAddressContainingIgnoreCase(q);
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

			Venue venue = venueRepository.findByNameCaseSensitive(v.getName());

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
	public Venue findByName(String name) {
		
		if(name == null) {
			
			throw new NullPointerException("False request...");
			
		}
		
		try {
			
			Venue venue = venueRepository.findByName(name);
			
			if(venue == null) {
				
				throw new Exception("No such venue find at here...");
				
			}
			
			return venue;
			
		} catch(Exception e) {
			
			throw new NoSuchElementException(e.getMessage());
			
		}
		
	}

}
