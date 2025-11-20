package com.example.demo700.Services.TurfServices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Turf.Venue;
import com.example.demo700.Models.Turf.VenueLocation;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Turf.VenueLocationServiceRepository;
import com.example.demo700.Repositories.Turf.VenueRepository;

@Service
public class VenueLocationServiceImpl implements VenueLocationService {

	@Autowired
	VenueLocationServiceRepository venueLocationRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	VenueRepository venueRepository;

	@Autowired
	CyclicCleaner cleaner;

	@Override
	public VenueLocation addVenueLocation(VenueLocation venueLocation, String userId) {

		if (userId == null) {

			throw new NullPointerException("user id can't be null...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new NullPointerException("No such user exist at here...");

			}

		} catch (Exception e) {

			throw new NullPointerException("No such user exist at here...");

		}

		if (venueLocation == null) {

			throw new NullPointerException("False venue location request...");

		}

		try {

			VenueLocation _venueLocation = venueLocationRepository.findByVenueId(venueLocation.getVenueId());

			if (_venueLocation != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Duplicate venue at here...");

		} catch (Exception e) {

		}

		try {

			Venue venue = venueRepository.findById(venueLocation.getVenueId()).get();

			if (venue == null) {

				throw new NullPointerException("No such venue exist at here...");

			}

			if (!venue.getOwnerId().equals(userId)) {

				throw new ArithmeticException("You can fixed the location only at your own venue...");

			}

		} catch (Exception e) {

			throw new ArithmeticException(e.getMessage());

		}

		venueLocation = venueLocationRepository.save(venueLocation);

		if (venueLocation == null) {

			throw new NullPointerException("value is not added at here...");

		}

		return venueLocation;
	}

	@Override
	public List<VenueLocation> seeAllVenueLocation() {

		List<VenueLocation> list = venueLocationRepository.findAll();

		if (list == null) {

			throw new NullPointerException("no venue location added till now...");

		}

		return list;
	}

	@Override
	public VenueLocation searchVenueLocation(String venueLocationId) {

		if (venueLocationId == null) {

			throw new NullPointerException("no venueLocation id at here...");

		}

		VenueLocation venueLocation = venueLocationRepository.findById(venueLocationId).get();

		if (venueLocation == null) {

			throw new ArithmeticException("no such location find...");

		}

		return venueLocation;
	}

	@Override
	public List<VenueLocation> searchVenueLocationByName(String locationName) {

		if (locationName == null) {

			throw new ArithmeticException("This location name is not valid...");

		}

		List<VenueLocation> list = venueLocationRepository.findByLocationName(locationName);

		if (list == null) {

			throw new ArithmeticException("no venue is present at here");

		}

		return list;
	}

	@Override
	public VenueLocation updateVenueLocation(String venueLocationId, VenueLocation updatedVenueLocation,
			String userId) {

		System.out.println("I am now working to update the venue location...");

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new NullPointerException("No such user exist at here...");

			}

		} catch (Exception e) {

			throw new NullPointerException(e.getMessage());

		}

		System.out.println("Your given user user is valid...");

		if (updatedVenueLocation == null) {

			throw new NullPointerException("False venue location request...");

		}

		Venue venue = null;

		try {

			venue = venueRepository.findById(updatedVenueLocation.getVenueId()).get();

			if (venue == null) {

				throw new NullPointerException("No such venue exist at here...");

			}

		} catch (Exception e) {

			throw new NullPointerException(e.getMessage());

		}

		try {

			VenueLocation _venueLocation = venueLocationRepository.findByVenueId(venue.getId());

			if (_venueLocation != null) {

				if (!_venueLocation.getId().equals(venueLocationId)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Venue not exist...");

		} catch (Exception e) {

		}

		System.out.println("Your given venue is valid...");

		if (!venue.getOwnerId().equals(userId)) {

			throw new NullPointerException("You can update only update at your own created venue...");

		}

		updatedVenueLocation.setId(venueLocationId);

		updatedVenueLocation = venueLocationRepository.save(updatedVenueLocation);

		if (updatedVenueLocation == null) {

			throw new ArithmeticException("venue location is not updated..");

		}

		return updatedVenueLocation;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean deleteVenueLocation(String venueLocationId, String userId) {

		User user = userRepository.findById(userId).get();

		if (user == null) {

			throw new NullPointerException("No such user exist at here...");

		}

		VenueLocation venueLocation = venueLocationRepository.findById(venueLocationId).get();

		if (venueLocation == null) {

			throw new ArithmeticException("No valid venue exist at here...");

		}

		Venue venue = venueRepository.findById(venueLocation.getVenueId()).get();

		if (venue == null) {

			throw new ArithmeticException("No valid venue this is...");

		}

		long count = venueLocationRepository.count();

		if (user.getRoles().contains(Role.valueOf("ROLE_ADMIN"))) {

			cleaner.removeVenueLocation(venueLocationId);

		} else if (user.getRoles().contains("ROLE_OWNER") && venue.getOwnerId().equals(userId)) {

			cleaner.removeVenueLocation(venueLocationId);

		}

		return count != venueRepository.count();
	}

}
