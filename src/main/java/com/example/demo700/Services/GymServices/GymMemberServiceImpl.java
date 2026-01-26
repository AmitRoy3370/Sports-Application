package com.example.demo700.Services.GymServices;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.GymModels.GymMember;
import com.example.demo700.Models.GymModels.Gyms;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.GymRepositories.GymMemberRepository;
import com.example.demo700.Repositories.GymRepositories.GymsRepository;

@Service
public class GymMemberServiceImpl implements GymMemberService {

	@Autowired
	private GymMemberRepository gymMemberRepository;

	@Autowired
	private GymsRepository gymsRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public GymMember removeGymMember(String memberId, String gymId, String userId) {

		if (memberId == null || gymId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(memberId).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getRoles().contains(Role.ROLE_GYM_OWNER)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			Gyms gyms = gymsRepository.findById(gymId).get();

			if (gyms == null) {

				throw new Exception();

			}

			if (!gyms.getGymOwner().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}
		try {

			GymMember gymMember = gymMemberRepository.findByGymId(gymId);

			if (gymMember == null) {

				throw new Exception();

			}

			if (!gymMember.getGymMembers().isEmpty()) {

				gymMember.getGymMembers().remove(memberId);

			}

			gymMember = gymMemberRepository.save(gymMember);

			return gymMember;

		} catch (Exception e) {

			throw new NoSuchElementException("This gyms does not have any member...");

		}
	}

	@Override
	public GymMember addGymMember(String memberId, String gymId, String userId) {
		try {

			GymMember gymMember = gymMemberRepository.findByGymId(gymId);

			if (gymMember == null) {

				throw new Exception();

			}

			gymMember.getGymMembers().add(memberId);

			gymMember = gymMemberRepository.save(gymMember);

			return gymMember;

		} catch (Exception e) {

			GymMember gymMember = new GymMember();

			gymMember.setGymId(gymId);

			Set<String> set = new HashSet<>();

			set.add(memberId);

			gymMember.setGymMembers(set);

			gymMember = gymMemberRepository.save(gymMember);

			return gymMember;

		}
	}

	@Override
	public GymMember findByGymId(String gymId) {

		if (gymId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			GymMember gymMember = gymMemberRepository.findByGymId(gymId);

			if (gymMember == null) {

				throw new Exception();

			}

			return gymMember;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gym member present in this gyms...");

		}

	}

	@Override
	public List<GymMember> findByGymMembersContaingingIgnoreCase(String gymMembers) {

		if (gymMembers == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<GymMember> list = gymMemberRepository.findByGymMembersContainingIgnoreCase(gymMembers);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

	}

	@Override
	public GymMember findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request....");

		}

		try {

			GymMember gymMember = gymMemberRepository.findById(id).get();

			if (gymMember == null) {

				throw new Exception();

			}

			return gymMember;

		} catch (Exception e) {

			throw new NoSuchElementException("no such gym member find at here...");

		}

	}

	@Override
	public List<GymMember> seeAll() {

		try {

			List<GymMember> list = gymMemberRepository.findAll();

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gym member find at here...");

		}

	}

	@Override
	public boolean removeGymMember(String id, String userId) {

		if (id == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			GymMember member = gymMemberRepository.findById(id).get();

			if (member == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such gym member find at here...");

		}

		try {

			GymMember member = gymMemberRepository.findById(id).get();

			if (member == null) {

				throw new Exception();

			}

			Gyms gym = gymsRepository.findById(member.getGymId()).get();

			if (gym == null) {

				throw new Exception();

			}

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = gymMemberRepository.count();

				cleaner.removeGymMember(id);

				return count != gymMemberRepository.count();

			}

			if (!gym.getGymOwner().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here to delete the members of the gym...");

		}

		long count = gymMemberRepository.count();

		cleaner.removeGymMember(id);

		return count != gymMemberRepository.count();
	}

}
