package com.example.demo700.Services.TurfServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.TurfOwnerResponse;
import com.example.demo700.DTOFiles.VenueResponse;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Turf.Owner;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Turf.OwnerRepository;
import com.example.demo700.Validator.PhoneValidator;

@Service
public class OwnerServiceImpl implements TurfOwnerService {

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VenueService venueService;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public Owner addOwner(Owner owner) {

		if (owner == null) {

			return null;

		}

		if (owner.getName() == null || owner.getPhone() == null || owner.getUserId() == null) {

			return null;

		}

		if (!new PhoneValidator(owner.getPhone()).isValid()) {

			return null;

		}

		try {

			User user = userRepository.findById(owner.getUserId()).get();

			if (user == null) {

				return null;

			}

		} catch (Exception e) {

		}

		try {

			Owner _owner = ownerRepository.searchByUserId(owner.getUserId());

			if (_owner != null) {

				return null;

			}

		} catch (Exception e) {

			return null;

		}

		try {

			if (!ownerRepository.searchByPhone(owner.getPhone()).isEmpty()) {

				throw new ArithmeticException("Phone number exist already...");

			}

		} catch (Exception e) {

			return null;

		}

		owner = ownerRepository.save(owner);

		return owner;
	}

	@Override
	public List<TurfOwnerResponse> seeAllOwner() {

		return getTrufOwnersResponse(ownerRepository.findAll());
	}

	@Override
	public Owner updateOwnerData(String id, Owner updatedOwner) throws Exception {

		Owner owner = ownerRepository.findById(id).orElseThrow(() -> new Exception("Not Exist Owner"));

		if (owner == null) {

			throw new Exception("False input");

		}

		updatedOwner.setId(owner.getId());

		if (updatedOwner.getId() == null || updatedOwner.getName() == null || updatedOwner.getPhone() == null
				|| updatedOwner.getUserId() == null) {

			return null;

		}

		if (!new PhoneValidator(updatedOwner.getPhone()).isValid()) {

			return null;

		}

		try {

			User user = userRepository.findById(updatedOwner.getUserId()).get();

			if (user == null) {

				return null;

			}

		} catch (Exception e) {

		}

		try {

			Owner _owner = ownerRepository.searchByUserId(updatedOwner.getUserId());

			if (_owner != null) {

				if (!_owner.getId().equals(updatedOwner.getId())) {

					throw new Exception();

				}

			}

		} catch (Exception e) {

			throw new ArithmeticException("One turf owner can be form from single user..");

		}

		try {

			List<Owner> list = ownerRepository.searchByPhone(owner.getPhone());

			if (!list.isEmpty()) {

				if (list.size() > 1) {

					throw new ArithmeticException();

				}

				if (!list.get(0).getId().equals(id)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Phone number exist already...");

		} catch (Exception e) {

		}

		updatedOwner = ownerRepository.save(updatedOwner);

		return updatedOwner;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean removeOwner(String id, String ownId) {

		if (id == null) {

			return false;

		}

		Owner owner = ownerRepository.findById(id).get();

		if (owner == null || ownId == null) {

			return false;

		}

		User user = userRepository.findById(owner.getUserId()).get();

		if (user == null) {

			return false;

		}

		User _user = userRepository.findById(ownId).get();

		if (_user == null) {

			return false;

		}

		if (_user.getRoles().contains(Role.valueOf("ROLE_ADMIN"))) {

			long count = ownerRepository.count();

			cleaner.removeVenueOwner(id);

			if (ownerRepository.count() != count) {

				// cleaner.removeUser(user.getId());

				return true;

			}

		} else if (owner.getUserId().equals(ownId)) {

			long count = ownerRepository.count();

			cleaner.removeVenueOwner(id);

			if (ownerRepository.count() != count) {

				// cleaner.removeUser(user.getId());

				return true;

			}

		}

		return false;
	}

	@Override
	public TurfOwnerResponse findByUserId(String userId) {

		if (userId == null) {

			return null;

		}

		try {

			Owner owner = ownerRepository.searchByUserId(userId);

			return getTurfOwnerResponse(owner);

		} catch (Exception e) {

		}

		return null;
	}

	@Override
	public List<TurfOwnerResponse> searchByName(String name) {

		if (name == null) {

			return null;

		}

		List<Owner> list = ownerRepository.searchByNameIgnoreCase(name);

		if (list.isEmpty()) {

			return null;

		}

		return getTrufOwnersResponse(list);
	}

	@Override
	public List<TurfOwnerResponse> searchByPhone(String phone) {

		if (phone == null) {

			return null;

		}

		List<Owner> list = ownerRepository.searchByPhone(phone);

		if (list.isEmpty()) {

			return null;

		}

		return getTrufOwnersResponse(list);
	}

	private ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private TurfOwnerResponse getTurfOwnerResponse(Owner owner) {

		List<Owner> list = new ArrayList<>();

		list.add(owner);

		return getTrufOwnersResponse(list).get(0);

	}

	private List<TurfOwnerResponse> getTrufOwnersResponse(List<Owner> owners) {

		List<TurfOwnerResponse> responses = new ArrayList<>();

		List<String> usersId = owners.stream().map(Owner::getUserId).collect(Collectors.toList());

		List<User> users = userRepository.findAllById(usersId);

		List<VenueResponse> venues = venueService.findByVenueOwners(usersId);

		CompletableFuture<Map<String, User>> userFuture = CompletableFuture
				.supplyAsync(() -> users.isEmpty() ? new HashMap<>()
						: users.stream().filter(Objects::nonNull).filter(user -> user.getName() != null)
								.collect(Collectors.toMap(User::getId, Function.identity())),
						executors);

		CompletableFuture<Map<String, List<VenueResponse>>> venueFuture = CompletableFuture
				.supplyAsync(() -> venues.isEmpty() ? new HashMap<>()
						: venues.stream().filter(Objects::nonNull)
								.collect(Collectors.groupingBy(VenueResponse::getOwnerId)),
						executors);

		CompletableFuture.allOf(userFuture, venueFuture).join();

		Map<String, User> userMap = userFuture.join();
		Map<String, List<VenueResponse>> venueMap = venueFuture.join();

		for (Owner owner : owners) {

			try {

				TurfOwnerResponse response = new TurfOwnerResponse();

				response.setId(owner.getId());
				response.setPhone(owner.getPhone());
				response.setUserId(owner.getUserId());

				try {

					response.setUserName(userMap.get(owner.getUserId()).getName());

				} catch (Exception e) {

					response.setUserName("UnNamed");

				}

				try {

					response.setVenueDetails(venueMap.get(owner.getUserId()));

				} catch (Exception e) {

				}

				responses.add(response);

			} catch (Exception e) {

			}

		}

		return responses;

	}

}
