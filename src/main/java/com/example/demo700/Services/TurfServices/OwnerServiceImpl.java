package com.example.demo700.Services.TurfServices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
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
	public List<Owner> seeAllOwner() {

		return ownerRepository.findAll();
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
	public Owner findByUserId(String userId) {

		if (userId == null) {

			return null;

		}

		try {

			Owner owner = ownerRepository.searchByUserId(userId);

			return owner;

		} catch (Exception e) {

		}

		return null;
	}

	@Override
	public List<Owner> searchByName(String name) {

		if (name == null) {

			return null;

		}

		List<Owner> list = ownerRepository.searchByName(name);

		if (list.isEmpty()) {

			return null;

		}

		return list;
	}

	@Override
	public List<Owner> searchByPhone(String phone) {

		if (phone == null) {

			return null;

		}

		List<Owner> list = ownerRepository.searchByPhone(phone);

		if (list.isEmpty()) {

			return null;

		}

		return list;
	}

}
