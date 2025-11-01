package com.example.demo700.Services.Athlete;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.ENUMS.AtheletesTeamJoiningResponse;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.ENUMS.TeamJoinRequestRole;
import com.example.demo700.ENUMS.TeamJoinRequestStatus;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.Coach;
import com.example.demo700.Models.Athlete.Scouts;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.Athlete.TeamJoinRequest;
import com.example.demo700.Models.Athlete.TeamOwner;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.CoachRepository;
import com.example.demo700.Repositories.Athelete.ScoutsRepository;
import com.example.demo700.Repositories.Athelete.TeamJoinRequestRepository;
import com.example.demo700.Repositories.Athelete.TeamOwnerRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;

@Service
public class TeamJoinRequestServiceImpl implements TeamJoinRequestService {

	@Autowired
	private TeamJoinRequestRepository teamJoinRequestRepository;

	@Autowired
	private TeamOwnerRepository teamOwnerRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AtheleteRepository atheleteRepository;

	@Autowired
	private CoachRepository coachRepository;

	@Autowired
	private ScoutsRepository scoutsRepository;

	@Override
	public TeamJoinRequest sendJoinRequest(TeamJoinRequest teamJoinRequest, String userId) {

		if (teamJoinRequest == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new NoSuchElementException();

			}

			Athelete athelete = atheleteRepository.findByUserId(userId).get();

			if (athelete == null) {

				throw new NoSuchElementException();

			}

			TeamOwner teamOwner = teamOwnerRepository.findByAtheleteId(athelete.getId());

			if (teamOwner == null) {

				throw new NoSuchElementException();

			}

			if (!teamJoinRequest.getSenderId().equals(userId)) {

				throw new NoSuchElementException();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist...");

		}

		try {

			Team team = teamRepository.findById(teamJoinRequest.getTeamId()).get();

			if (team == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("In valid team join request...");

		}

		try {

			if (!teamJoinRequest.getRequestEndTime().isAfter(teamJoinRequest.getRequestStartTime())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Invalid joining date...");

		}

		try {

			if (teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_ATHLETE) {

				Athelete athelete = atheleteRepository.findById(teamJoinRequest.getReceiverId()).get();

				if (athelete == null) {

					throw new Exception();

				}

				Team team = teamRepository.findByAtheletesContainingIgnoreCase(athelete.getId());

				if (team != null) {

					throw new Exception();

				}

			} else if (teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_COACH) {

				Coach coach = coachRepository.findById(teamJoinRequest.getReceiverId()).get();

				if (coach == null) {

					throw new Exception();

				}

				Team team = teamRepository.findByCoachesContainingIgnoreCase(coach.getId());

				if (team != null) {

					throw new Exception();

				}

			} else if (teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_SCOUT) {

				Scouts scout = scoutsRepository.findById(teamJoinRequest.getReceiverId()).get();

				if (scout == null) {

					throw new Exception();

				}

				Team team = teamRepository.findByScoutsContainingIgnoreCase(scout.getId());

				if (team != null) {

					throw new Exception();

				}

			}

			if (teamJoinRequest.getRequestStartTime().isAfter(teamJoinRequest.getRequestEndTime())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("Invalid receiver user request...");

		}

		if (!teamJoinRequest.getStatus().equals(TeamJoinRequestStatus.ROLE_PENDING)) {

			throw new ArithmeticException("The newly added join reuqest must be pending...");

		}

		teamJoinRequest = teamJoinRequestRepository.save(teamJoinRequest);

		if (teamJoinRequest == null) {

			return null;

		}

		return teamJoinRequest;
	}

	@Override
	public TeamJoinRequest updateRequestStatus(TeamJoinRequest teamJoinRequest, String userId,
			String teamJoinRequestId) {

		if (teamJoinRequest == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new NoSuchElementException();

			}

			Athelete athelete = atheleteRepository.findByUserId(userId).get();

			if (athelete == null) {

				throw new NoSuchElementException();

			}

			TeamOwner teamOwner = teamOwnerRepository.findByAtheleteId(athelete.getId());

			if (teamOwner == null) {

				throw new NoSuchElementException();

			}

			if (!teamJoinRequest.getSenderId().equals(userId)) {

				throw new NoSuchElementException();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist...");

		}

		try {

			Team team = teamRepository.findById(teamJoinRequest.getTeamId()).get();

			if (team == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("In valid team join request...");

		}

		try {

			if (!teamJoinRequest.getRequestEndTime().isAfter(teamJoinRequest.getRequestStartTime())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Invalid joining date...");

		}

		try {

			if (teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_ATHLETE) {

				Athelete athelete = atheleteRepository.findById(teamJoinRequest.getReceiverId()).get();

				if (athelete == null) {

					throw new Exception();

				}

				Team team = teamRepository.findByAtheletesContainingIgnoreCase(athelete.getId());

				if (team != null) {

					throw new Exception();

				}

			} else if (teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_COACH) {

				Coach coach = coachRepository.findById(teamJoinRequest.getReceiverId()).get();

				if (coach == null) {

					throw new Exception();

				}

				Team team = teamRepository.findByCoachesContainingIgnoreCase(coach.getId());

				if (team != null) {

					throw new Exception();

				}

			} else if (teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_SCOUT) {

				Scouts scout = scoutsRepository.findById(teamJoinRequest.getReceiverId()).get();

				if (scout == null) {

					throw new Exception();

				}

				Team team = teamRepository.findByScoutsContainingIgnoreCase(scout.getId());

				if (team != null) {

					throw new Exception();

				}

			}

		} catch (Exception e) {

			throw new NoSuchElementException("Invalid receiver user request...");

		}

		if (teamJoinRequest.getStatus().equals(TeamJoinRequestStatus.ROLE_CANCEL)) {

			teamJoinRequestRepository.deleteById(teamJoinRequestId);

			throw new ArithmeticException("This join request is cancelled already...");

		}

		if (teamJoinRequest.getStatus().equals(TeamJoinRequestStatus.ROLE_ACCEPT)) {

			teamJoinRequestRepository.deleteById(teamJoinRequestId);

			throw new ArithmeticException("This join request is approved already...");

		}

		if (teamJoinRequest.getRequestStartTime().isAfter(teamJoinRequest.getRequestEndTime())) {

			throw new ArithmeticException("Request time schedule is not valid...");

		}

		teamJoinRequest.setId(teamJoinRequestId);

		teamJoinRequestRepository.save(teamJoinRequest);

		return teamJoinRequest;
	}

	@Override
	public List<TeamJoinRequest> getRequestsByReceiver(String receiverId) {

		if (receiverId == null) {

			throw new NullPointerException("False request...");

		}

		return teamJoinRequestRepository.findByReceiverId(receiverId);
	}

	@Override
	public List<TeamJoinRequest> getRequestsBySender(String senderId) {
		if (senderId == null) {

			throw new NullPointerException("False request...");

		}

		return teamJoinRequestRepository.findBySenderId(senderId);
	}

	@Override
	public boolean deleteRequest(String requestId, String userId) {

		if (requestId == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new NoSuchElementException();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				TeamJoinRequest teamJoinRequest1 = teamJoinRequestRepository.findById(requestId).get();

				if (teamJoinRequest1 == null) {

					throw new Exception();

				}

				long count = teamJoinRequestRepository.count();

				teamJoinRequestRepository.deleteById(requestId);

				return count != teamJoinRequestRepository.count();

			}

			Athelete athelete = atheleteRepository.findByUserId(userId).get();

			if (athelete == null) {

				throw new NoSuchElementException();

			}

			TeamOwner teamOwner = teamOwnerRepository.findByAtheleteId(athelete.getId());

			if (teamOwner == null) {

				throw new NoSuchElementException();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist...");

		}

		try {

			TeamJoinRequest teamJoinRequest = teamJoinRequestRepository.findById(requestId).get();

			if (teamJoinRequest == null) {

				throw new Exception();

			}

			long count = teamJoinRequestRepository.count();

			teamJoinRequestRepository.deleteById(requestId);

			return count != teamJoinRequestRepository.count();

		} catch (Exception e) {

			throw new NoSuchElementException("No such request find at here...");

		}

	}

	@Override
	public List<TeamJoinRequest> seeAllTeamJoinRequest() {

		return teamJoinRequestRepository.findAll();
	}

	@Override
	public List<TeamJoinRequest> searchByPrice(double price) {

		return teamJoinRequestRepository.findByPriceGreaterThan(price);
	}

	@Override
	public boolean handleJoinResponse(String teamJoinRequestId, String userId,
			AtheletesTeamJoiningResponse atheleteResponse) {

		if (teamJoinRequestId == null || userId == null || atheleteResponse == null) {

			throw new NullPointerException("False request....");

		}

		TeamJoinRequest teamJoinRequest = null;

		long count = 0;

		try {

			System.out.println(teamJoinRequestId);

			try {

				teamJoinRequest = teamJoinRequestRepository.findById(teamJoinRequestId).orElseThrow(
						() -> new NoSuchElementException("There is no team join request based on this id..."));

			} catch (Exception e_) {

				System.out.println(e_.getMessage());

				throw new NoSuchElementException(e_.getMessage());

			}

			if (teamJoinRequest == null) {

				System.out.println("Stopped at here....");

				throw new Exception();

			}

			System.out.println("team join requst find....");

			if (Instant.now().isAfter(teamJoinRequest.getRequestEndTime())) {

				teamJoinRequestRepository.deleteById(teamJoinRequestId);

				throw new Exception();

			}

			if (teamJoinRequest.getStatus().equals(TeamJoinRequestStatus.ROLE_CANCEL)) {

				teamJoinRequestRepository.deleteById(teamJoinRequestId);

				return false;

			}

			if (teamJoinRequest.getStatus().equals(TeamJoinRequestStatus.ROLE_ACCEPT)) {

				teamJoinRequestRepository.deleteById(teamJoinRequestId);

				User user = userRepository.findById(userId)
						.orElseThrow(() -> new NoSuchElementException("There is no user based on this id..."));

				if (user == null) {

					throw new NoSuchElementException();

				}

				Team team = teamRepository.findById(teamJoinRequest.getTeamId()).get();

				if (team == null) {

					throw new Exception();

				}

				if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_ATHLETE)) {

					try {

						Athelete athelete = atheleteRepository.findByUserId(userId).get();

						if (athelete == null) {

							throw new Exception();

						}

						athelete.setPresentTeam(team.getTeamName());

						atheleteRepository.save(athelete);

					} catch (Exception e) {

						throw new NoSuchElementException("No such athelete find at here...");

					}

				} else if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_COACH)) {

					try {

						Athelete athelete = atheleteRepository.findByUserId(userId).get();

						if (athelete == null) {

							throw new Exception();

						}

						Coach coach = coachRepository.findByAtheleteId(athelete.getId());

						if (coach == null) {

							throw new Exception();

						}

						coach.setTeamName(team.getTeamName());

						coachRepository.save(coach);

					} catch (Exception e) {

						throw new NoSuchElementException("No such coach find at here..");

					}

				} else if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_SCOUT)) {

					try {

						Athelete athelete = atheleteRepository.findByUserId(userId).get();

						if (athelete == null) {

							throw new Exception();

						}

						Scouts scouts = scoutsRepository.findByAtheleteId(athelete.getId());

						if (scouts == null) {

							throw new Exception();

						}

						if (athelete.getPresentTeam() != null) {

						} else {

							athelete.setPresentTeam(team.getTeamName());

							atheleteRepository.save(athelete);

						}

					} catch (Exception e) {

						throw new NoSuchElementException("No such scouts find at here...");

					}

				}

				return false;

			}

			if (!teamJoinRequest.getStatus().equals(TeamJoinRequestStatus.ROLE_PENDING)) {

				throw new Exception();

			}

			count = teamJoinRequestRepository.count();

		} catch (NoSuchElementException e) {

			throw new NoSuchElementException(e.getMessage());

		} catch (Exception e) {

			throw new NoSuchElementException("Team Join request is not present...");

		}

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getRoles().contains(Role.ROLE_ATHLETE) && !user.getRoles().contains(Role.ROLE_COACH)
					&& !user.getRoles().contains(Role.ROLE_SCOUT)) {

				throw new Exception();

			}

			System.out.println(teamJoinRequest.getRoleType().toString());

			System.out.println(user.getRoles().contains(Role.valueOf(teamJoinRequest.getRoleType().toString())));

			if (!user.getRoles().contains(Role.valueOf(teamJoinRequest.getRoleType().toString()))) {

				// System.out.println("Stacked at here...");

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_ATHLETE)) {

				System.out.println("Athelete join request sending...");

				Athelete athelete = atheleteRepository.findByUserId(userId).get();

				if (athelete == null) {

					throw new Exception();

				}

				System.out.println("Athelete find...");

				if (!teamJoinRequest.getReceiverId().equals(athelete.getId())) {

					throw new Exception();

				}

			} else if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_COACH)) {

				Athelete athelete = atheleteRepository.findByUserId(userId).get();

				if (athelete == null) {

					throw new Exception();

				}

				Coach coach = coachRepository.findByAtheleteId(athelete.getId());

				if (coach == null) {

					throw new Exception();

				}

				if (!teamJoinRequest.getReceiverId().equals(coach.getId())) {

					throw new Exception();

				}

			} else if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_SCOUT)) {

				Athelete athelete = atheleteRepository.findByUserId(userId).get();

				if (athelete == null) {

					throw new Exception();

				}

				Scouts scout = scoutsRepository.findByAtheleteId(athelete.getId());

				if (scout == null) {

					throw new Exception();

				}

				if (!teamJoinRequest.getReceiverId().equals(scout.getId())) {

					throw new Exception();

				}

			}

		} catch (Exception e) {

			throw new NoSuchElementException("This user is not requested from this team...");

		}

		try {

			Team team = null;

			if (atheleteResponse.name().equals("ROLE_ACCEPT")) {

				if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_ATHLETE)) {

					Athelete athelete = atheleteRepository.findByUserId(userId).get();

					team = teamRepository.findById(teamJoinRequest.getTeamId()).get();

					if (!teamJoinRequest.getReceiverId().equals(athelete.getId())) {

						throw new Exception("User id and reciver id is matched...");

					}

					athelete.setPresentTeam(team.getTeamName());

					TeamOwner teamOwner = teamOwnerRepository.findByTeamsContainingIgnoreCase(team.getId());

					if (teamOwner == null) {

						throw new Exception("Team owner not find...");

					}

					team.getAtheletes().add(athelete.getId());

					team = teamRepository.save(team);

					if (team != null) {

						teamJoinRequestRepository.deleteById(teamJoinRequestId);

					}

				} else if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_COACH)) {

					Athelete athelete = atheleteRepository.findByUserId(userId).get();

					Coach coach = coachRepository.findByAtheleteId(athelete.getId());

					if (coach == null) {

						throw new Exception();

					}

					System.out.println("Coach find...");

					if (!teamJoinRequest.getReceiverId().equals(coach.getId())) {

						throw new Exception();

					}

					System.out.println("Team Owner find...");

					team = teamRepository.findById(teamJoinRequest.getTeamId()).get();

					if (!teamJoinRequest.getReceiverId().equals(coach.getId())) {

						throw new Exception();

					}

					athelete.setPresentTeam(team.getTeamName());

					TeamOwner teamOwner = teamOwnerRepository.findByTeamsContainingIgnoreCase(team.getId());

					if (teamOwner == null) {

						throw new Exception();

					}

					team.getCoaches().add(coach.getId());

					team = teamRepository.save(team);

					if (team != null) {

						teamJoinRequestRepository.deleteById(teamJoinRequestId);

					}

				} else if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_SCOUT)) {

					Athelete athelete = atheleteRepository.findByUserId(userId).get();

					Scouts scout = scoutsRepository.findByAtheleteId(athelete.getId());

					if (scout == null) {

						throw new Exception();

					}

					System.out.println("Scouts find...");

					if (!teamJoinRequest.getReceiverId().equals(scout.getId())) {

						throw new Exception();

					}

					System.out.println("Scouts id match...");

					team = teamRepository.findById(teamJoinRequest.getTeamId()).get();

					if (!teamJoinRequest.getReceiverId().equals(scout.getId())) {

						throw new Exception();

					}

					System.out.println("This scout is requested...");

					athelete.setPresentTeam(team.getTeamName());

					TeamOwner teamOwner = teamOwnerRepository.findByTeamsContainingIgnoreCase(team.getId());

					if (teamOwner == null) {

						throw new Exception();

					}

					System.out.println("Team owner find...");

					team.getScouts().add(scout.getId());

					team = teamRepository.save(team);

					if (team != null) {

						teamJoinRequestRepository.deleteById(teamJoinRequestId);

					}

				}

				// teamJoinRequestRepository.deleteById(teamJoinRequestId);

			} else {

				teamJoinRequestRepository.deleteById(teamJoinRequestId);

				return true;

			}

		} catch (Exception e) {

			System.out.println(e);

			throw new ArithmeticException("Requested user can not respond any thing...");

		}

		boolean yes = count != teamJoinRequestRepository.count();

		if (yes) {

			List<TeamJoinRequest> list = teamJoinRequestRepository.findByReceiverId(userId);

			for (TeamJoinRequest i : list) {

				try {

					teamJoinRequestRepository.deleteById(i.getId());

				} catch (Exception e) {

					throw new ArithmeticException("Some other's member request for that player is still at here...");

				}

			}

		}

		return yes;
	}

}
