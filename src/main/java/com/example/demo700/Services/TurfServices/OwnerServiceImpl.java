package com.example.demo700.Services.TurfServices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

		User user = userRepository.findById(owner.getUserId()).get();

		if (user == null) {

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

		User user = userRepository.findById(updatedOwner.getUserId()).get();

		if (user == null) {

			return null;

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

			ownerRepository.deleteById(id);

			if (ownerRepository.count() != count) {

				userRepository.deleteById(user.getId());

				return true;

			}

		} else if (owner.getUserId().equals(ownId)) {

			long count = ownerRepository.count();

			ownerRepository.deleteById(id);

			if (ownerRepository.count() != count) {

				userRepository.deleteById(user.getId());

				return true;

			}

		}

		return false;
	}

}
