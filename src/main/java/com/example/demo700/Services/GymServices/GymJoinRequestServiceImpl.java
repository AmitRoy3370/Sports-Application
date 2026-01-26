package com.example.demo700.Services.GymServices;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.GymModels.GymJoinRequest;
import com.example.demo700.Models.GymModels.GymMember;
import com.example.demo700.Models.GymModels.Gyms;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.GymRepositories.GymJoinRequestRepository;
import com.example.demo700.Repositories.GymRepositories.GymMemberRepository;
import com.example.demo700.Repositories.GymRepositories.GymsRepository;

@Service
public class GymJoinRequestServiceImpl implements GymJoinRequestService {

	@Autowired
	private GymJoinRequestRepository gymJoinRequestRepository;

	@Autowired
	private GymsRepository gymsRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GymMemberRepository gymMemberRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public GymJoinRequest addGymJoinRequest(GymJoinRequest gymJoinRequest) {

		if (gymJoinRequest == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(gymJoinRequest.getUserId()).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here....");

		}

		try {

			Gyms gyms = gymsRepository.findById(gymJoinRequest.getGymId()).get();

			if (gyms == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such gym find at here...");

		}

		try {

			GymJoinRequest request = gymJoinRequestRepository.findByGymIdAndUserId(gymJoinRequest.getGymId(),
					gymJoinRequest.getUserId());

			if (request != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException(
					"One user can send join request for join a gym only once until come's some response....");

		} catch (Exception e) {

		}

		try {

			List<GymMember> gymMembers = gymMemberRepository
					.findByGymMembersContaingingIgnoreCase(gymJoinRequest.getUserId());

			if (!gymMembers.isEmpty()) {

				List<String> gyms = new ArrayList<>();

				for (GymMember i : gymMembers) {

					gyms.add(i.getGymId());

				}

				if (gyms.contains(gymJoinRequest.getGymId())) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("You are the member of that gym already....");

		} catch (Exception e) {

		}

		gymJoinRequest = gymJoinRequestRepository.save(gymJoinRequest);

		return gymJoinRequest;
	}

	@Override
	public GymJoinRequest updayeGymJoinRequest(GymJoinRequest gymJoinRequest, String userId, String gymId) {

		if (gymJoinRequest == null || userId == null || gymId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			GymJoinRequest request = gymJoinRequestRepository.findById(gymId).get();

			if (request == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}

		try {

			User user = userRepository.findById(gymJoinRequest.getUserId()).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here....");

		}

		try {

			Gyms gyms = gymsRepository.findById(gymJoinRequest.getGymId()).get();

			if (gyms == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such gym find at here...");

		}

		try {

			GymJoinRequest request = gymJoinRequestRepository.findByGymIdAndUserId(gymJoinRequest.getGymId(),
					gymJoinRequest.getUserId());

			if (request != null) {

				if (!request.getId().equals(gymId)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException(
					"One user can send join request for join a gym only once until come's some response....");

		} catch (Exception e) {

		}

		try {

			List<GymMember> gymMembers = gymMemberRepository
					.findByGymMembersContaingingIgnoreCase(gymJoinRequest.getUserId());

			if (!gymMembers.isEmpty()) {

				List<String> gyms = new ArrayList<>();

				for (GymMember i : gymMembers) {

					gyms.add(i.getGymId());

				}

				if (gyms.contains(gymJoinRequest.getGymId())) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("You are the member of that gym already....");

		} catch (Exception e) {

		}

		gymJoinRequest.setId(gymId);

		gymJoinRequest = gymJoinRequestRepository.save(gymJoinRequest);

		return gymJoinRequest;
	}

	@Override
	public GymJoinRequest findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request....");

		}

		try {

			GymJoinRequest request = gymJoinRequestRepository.findById(id).get();

			if (request == null) {

				throw new Exception();

			}

			return request;

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}

	}

	@Override
	public List<GymJoinRequest> seeAll() {

		try {

			List<GymJoinRequest> list = gymJoinRequestRepository.findAll();

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}

	}

	@Override
	public List<GymJoinRequest> findByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<GymJoinRequest> list = gymJoinRequestRepository.findByUserId(userId);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}

	}

	@Override
	public List<GymJoinRequest> findByGymId(String gymId) {

		if (gymId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<GymJoinRequest> list = gymJoinRequestRepository.findByGymId(gymId);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}

	}

	@Override
	public List<GymJoinRequest> findByRequestSendingTimeBefore(Instant requestSendingTime) {

		if (requestSendingTime == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<GymJoinRequest> list = gymJoinRequestRepository.findByRequestSendingTimeBefore(requestSendingTime);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}

	}

	@Override
	public List<GymJoinRequest> findByRequestSendingTimeAfter(Instant requestSendingTime) {

		if (requestSendingTime == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<GymJoinRequest> list = gymJoinRequestRepository.findByRequestSendingTimeAfter(requestSendingTime);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}

	}

	@Override
	public boolean removeGymJoinRequest(String id, String userId) {

		if (id == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			GymJoinRequest request = gymJoinRequestRepository.findById(id).get();

			if (request == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such gym join request find at here....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			GymJoinRequest request = gymJoinRequestRepository.findById(id).get();

			if (request == null) {

				throw new Exception();

			}

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = gymJoinRequestRepository.count();

				cleaner.removeGymJoinRequest(id);

				return count != gymJoinRequestRepository.count();

			}

			if (!request.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("You can remove only your gyms join request....");

		}

		long count = gymJoinRequestRepository.count();

		cleaner.removeGymJoinRequest(id);

		return count != gymJoinRequestRepository.count();
	}

	@Override
	public GymMember handleGymJoinRequest(String id, String userId, boolean response) {

		if (id == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			GymJoinRequest request = gymJoinRequestRepository.findById(id).get();

			if (request == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}

		try {

			GymJoinRequest request = gymJoinRequestRepository.findById(id).get();

			if (request == null) {

				throw new Exception();

			}

			Gyms gyms = gymsRepository.findById(request.getGymId()).get();

			if (gyms == null) {

				throw new Exception();

			}

			if (!gyms.getGymOwner().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("Only the gym owner can accept the request....");

		}

		if (response) {

			GymJoinRequest request = gymJoinRequestRepository.findById(id).get();

			try {

				GymMember gymMember = gymMemberRepository.findByGymId(request.getGymId());

				if (gymMember == null) {

					throw new Exception();

				}

				gymMember.getGymMembers().add(request.getUserId());

				gymMember = gymMemberRepository.save(gymMember);

				return gymMember;

			} catch (Exception e) {

				GymMember gymMember = new GymMember();

				gymMember.setGymId(request.getGymId());

				Set<String> set = new HashSet<>();

				set.add(request.getUserId());

				gymMember.setGymMembers(set);

				gymMember = gymMemberRepository.save(gymMember);

				return gymMember;

			}

		} else {

			cleaner.removeGymJoinRequest(id);

			return null;

		}

	}

	@Override
	public GymJoinRequest findByGymIdAndUserId(String gymId, String userId) {

		if (gymId == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here....");

		}

		try {

			Gyms gyms = gymsRepository.findById(gymId).get();

			if (gyms == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such gym find at here...");

		}

		try {

			GymJoinRequest request = gymJoinRequestRepository.findByGymIdAndUserId(gymId, userId);

			if (request == null) {

				throw new Exception();

			}

			return request;

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}

	}

}
