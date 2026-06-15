package com.example.demo700.Services.Athlete;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.DTOFiles.TeamJoinRequestResponse;
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
import com.example.demo700.Models.DoctorModels.Doctor;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.CoachRepository;
import com.example.demo700.Repositories.Athelete.ScoutsRepository;
import com.example.demo700.Repositories.Athelete.TeamJoinRequestRepository;
import com.example.demo700.Repositories.Athelete.TeamOwnerRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.DoctorRepositories.DoctorRepository;
import com.example.demo700.Services.NotificationServices.NotificationService;

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

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private NotificationService notificationService;

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

		Team _team = null;

		try {

			_team = teamRepository.findById(teamJoinRequest.getTeamId()).get();

			if (_team == null) {

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

		String receiverId = null;

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

				receiverId = athelete.getUserId();

			} else if (teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_COACH) {

				Coach coach = coachRepository.findById(teamJoinRequest.getReceiverId()).get();

				if (coach == null) {

					throw new Exception();

				}

				Team team = teamRepository.findByCoachesContainingIgnoreCase(coach.getId());

				if (team != null) {

					throw new Exception();

				}

				Athelete _athlete = atheleteRepository.findById(coach.getAtheleteId()).get();

				receiverId = _athlete.getUserId();

			} else if (teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_SCOUT) {

				Scouts scout = scoutsRepository.findById(teamJoinRequest.getReceiverId()).get();

				if (scout == null) {

					throw new Exception();

				}

				Team team = teamRepository.findByScoutsContainingIgnoreCase(scout.getId());

				if (team != null) {

					throw new Exception();

				}

				Athelete _athlete = atheleteRepository.findById(scout.getAtheleteId()).get();

				receiverId = _athlete.getUserId();

			} else if (teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_DOCTOR) {

				Doctor doctor = doctorRepository.findById(teamJoinRequest.getReceiverId()).get();

				if (doctor == null) {

					throw new NoSuchElementException();

				}

				Team team = teamRepository.findByDoctorsContainingIgnoreCase(doctor.getId());

				if (team != null) {

					throw new Exception();

				}

				receiverId = doctor.getUserId();

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

		try {

			User user = userRepository.findById(receiverId).get();

			notificationService.sendNotification(receiverId,
					user.getName() + " you get a team join request from the team " + _team.getTeamName());

			notificationService.sendNotification(userId,
					"You send a join request to " + user.getName() + " for your team " + _team.getTeamName());

		} catch (Exception e) {

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

		Team _team = null;

		try {

			_team = teamRepository.findById(teamJoinRequest.getTeamId()).get();

			if (_team == null) {

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

		String receiverId = null;

		try {

			if (teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_ATHLETE) {

				Athelete athelete = atheleteRepository.findById(teamJoinRequest.getReceiverId()).get();

				if (athelete == null) {

					throw new Exception();

				}

				try {

					Team team = teamRepository.findByAtheletesContainingIgnoreCase(athelete.getId());

					if (team != null) {

						throw new Exception();

					}

				} catch (Exception e) {

				}

				receiverId = athelete.getUserId();

			} else if (teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_COACH) {

				Coach coach = coachRepository.findById(teamJoinRequest.getReceiverId()).get();

				if (coach == null) {

					throw new Exception();

				}

				try {

					Team team = teamRepository.findByCoachesContainingIgnoreCase(coach.getId());

					if (team != null) {

						throw new Exception();

					}

				} catch (Exception e) {

				}

				Athelete athlete = atheleteRepository.findById(coach.getAtheleteId()).get();

				receiverId = athlete.getUserId();

			} else if (teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_SCOUT) {

				Scouts scout = scoutsRepository.findById(teamJoinRequest.getReceiverId()).get();

				if (scout == null) {

					throw new Exception();

				}

				try {

					Team team = teamRepository.findByScoutsContainingIgnoreCase(scout.getId());

					if (team != null) {

						throw new Exception();

					}

				} catch (Exception e) {

				}

				Athelete athlete = atheleteRepository.findById(scout.getAtheleteId()).get();

				receiverId = athlete.getUserId();

			} else if (teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_DOCTOR) {

				Doctor doctor = doctorRepository.findById(teamJoinRequest.getReceiverId()).get();

				if (doctor == null) {

					throw new NoSuchElementException();

				}

				try {

					Team team = teamRepository.findByDoctorsContainingIgnoreCase(doctor.getId());

					if (team != null) {

						throw new Exception();

					}

				} catch (Exception e) {

				}

				receiverId = doctor.getUserId();

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

		teamJoinRequest = teamJoinRequestRepository.save(teamJoinRequest);

		if (teamJoinRequest != null) {

			try {

				User user = userRepository.findById(receiverId).get();

				notificationService.sendNotification(receiverId,
						user.getName() + " you get a team join request from the team " + _team.getTeamName());

				notificationService.sendNotification(userId,
						"You send a join request to " + user.getName() + " for your team " + _team.getTeamName());

			} catch (Exception e) {

			}

		}

		return teamJoinRequest;
	}

	@Override
	public List<TeamJoinRequestResponse> getRequestsByReceiver(String receiverId) {

		if (receiverId == null) {

			throw new NullPointerException("False request...");

		}

		return getTeamJoinRequestResponse(teamJoinRequestRepository.findByReceiverId(receiverId));
	}

	@Override
	public List<TeamJoinRequestResponse> getRequestsBySender(String senderId) {
		if (senderId == null) {

			throw new NullPointerException("False request...");

		}

		return getTeamJoinRequestResponse(teamJoinRequestRepository.findBySenderId(senderId));
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
	public List<TeamJoinRequestResponse> seeAllTeamJoinRequest() {

		return getTeamJoinRequestResponse(teamJoinRequestRepository.findAll());
	}

	@Override
	public List<TeamJoinRequestResponse> searchByPrice(double price) {

		return getTeamJoinRequestResponse(teamJoinRequestRepository.findByPriceGreaterThan(price));
	}

	@Override
	public boolean handleJoinResponse(String teamJoinRequestId, String userId,
			AtheletesTeamJoiningResponse atheleteResponse) {

		if (teamJoinRequestId == null || userId == null || atheleteResponse == null) {

			throw new NullPointerException("False request....");

		}

		TeamJoinRequest teamJoinRequest = null;

		long count = 0;

		Team _team = null;

		User user = null;

		try {

			user = userRepository.findById(userId).get();

			if (user == null) {

				throw new ArithmeticException();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("Responsed user is in valid...");

		}

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

				throw new Exception("No such team join request exist at here....");

			}

			count = teamJoinRequestRepository.count();

			System.out.println("team join requst find....");

			if (Instant.now().isAfter(teamJoinRequest.getRequestEndTime())) {

				teamJoinRequestRepository.deleteById(teamJoinRequestId);

				throw new Exception("Time expired....");

			}

			try {

				if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_ATHLETE)
						|| teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_ATHLETE) {

					Athelete athlete = atheleteRepository.findByUserId(userId).get();

					if (athlete == null) {

						throw new Exception("Athlete not exist for this request....");

					}

					if (!teamJoinRequest.getReceiverId().equals(athlete.getId())) {

						throw new Exception("This team not request to you....");

					}

				} else if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_COACH)
						|| teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_COACH) {

					Athelete athlete = atheleteRepository.findByUserId(userId).get();

					if (athlete == null) {

						throw new Exception("No such coach find at here....");

					}

					Coach coach = coachRepository.findByAtheleteId(athlete.getId());

					if (coach == null) {

						throw new Exception("This coach not exist for this request....");

					}

					if (!teamJoinRequest.getReceiverId().equals(coach.getId())) {

						throw new Exception("This coach is not requested for this team....");

					}

				} else if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_SCOUT)
						|| teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_SCOUT) {

					Athelete athlete = atheleteRepository.findByUserId(userId).get();

					if (athlete == null) {

						throw new Exception("No such scout present at here...");

					}

					Scouts scout = scoutsRepository.findByAtheleteId(athlete.getId());

					if (scout == null) {

						throw new Exception("In valid scout....");

					}

					if (!teamJoinRequest.getReceiverId().equals(scout.getId())) {

						throw new Exception("This scout is not requested for this team...");

					}

				} else if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_DOCTOR)
						|| teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_DOCTOR) {

					Doctor doctor = doctorRepository.findByUserId(userId);

					if (doctor == null) {

						throw new Exception("This doctor is in valid....");

					}

					if (!teamJoinRequest.getReceiverId().equals(doctor.getId())) {

						throw new Exception("This doctor is not requested for this team...");

					}

				}

			} catch (Exception e) {

				throw new ArithmeticException(e.getMessage());

			}

			if (teamJoinRequest.getStatus().equals(TeamJoinRequestStatus.ROLE_CANCEL)) {

				teamJoinRequestRepository.deleteById(teamJoinRequestId);

				try {

					notificationService.sendNotification(userId,
							user.getName() + " you reject the request of team " + _team.getTeamName());

					TeamOwner teamOwner = teamOwnerRepository.findById(_team.getTeamOwnerId()).get();

					Athelete athlete = atheleteRepository.findById(teamOwner.getAtheleteId()).get();

					notificationService.sendNotification(athlete.getUserId(),
							user.getName() + " accept your team join request for team " + _team.getTeamName());

				} catch (Exception e) {

				}

				return false;

			}

			if (teamJoinRequest.getStatus().equals(TeamJoinRequestStatus.ROLE_ACCEPT)) {

				teamJoinRequestRepository.deleteById(teamJoinRequestId);

				user = userRepository.findById(userId)
						.orElseThrow(() -> new NoSuchElementException("There is no user based on this id..."));

				if (user == null) {

					throw new NoSuchElementException();

				}

				_team = teamRepository.findById(teamJoinRequest.getTeamId()).get();

				if (_team == null) {

					throw new Exception();

				}

				if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_ATHLETE)
						|| teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_ATHLETE
						|| TeamJoinRequestRole.ROLE_ATHLETE.equals(teamJoinRequest.getRoleType())) {

					try {

						Athelete athelete = atheleteRepository.findByUserId(userId).get();

						if (athelete == null) {

							throw new Exception();

						}

						if (athelete.getPresentTeam() != null) {

							throw new Exception();

						}

						athelete.setPresentTeam(_team.getTeamName());

						System.out.println("User is accept the request for join the team :- " + _team.getTeamName());

						atheleteRepository.save(athelete);

					} catch (Exception e) {

						throw new NoSuchElementException("No such athelete find at here...");

					}

				} else if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_COACH)
						|| teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_COACH
						|| (TeamJoinRequestRole.ROLE_COACH.equals(teamJoinRequest.getRoleType()))) {

					try {

						Athelete athelete = atheleteRepository.findByUserId(userId).get();

						if (athelete == null) {

							throw new Exception();

						}

						Coach coach = coachRepository.findByAtheleteId(athelete.getId());

						if (coach == null) {

							throw new Exception();

						}

						if (coach.getTeamName() != null) {

							throw new Exception();

						}

						coach.setTeamName(_team.getTeamName());

						coachRepository.save(coach);

					} catch (Exception e) {

						throw new NoSuchElementException("No such coach find at here..");

					}

				} else if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_SCOUT)
						|| teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_SCOUT
						|| TeamJoinRequestRole.ROLE_SCOUT.equals(teamJoinRequest.getRoleType())) {

					try {

						Athelete athelete = atheleteRepository.findByUserId(userId).get();

						if (athelete == null) {

							throw new Exception();

						}

						Scouts scouts = scoutsRepository.findByAtheleteId(athelete.getId());

						if (scouts == null) {

							throw new Exception();

						}

						try {

							Team team2 = teamRepository.findByScoutsContainingIgnoreCase(scouts.getId());

							if (team2 != null) {

								throw new ArithmeticException();

							}

						} catch (ArithmeticException e) {

							throw new ArithmeticException("You are already in a team...");

						} catch (Exception e) {

						}

						if (athelete.getPresentTeam() != null) {

						} else {

							athelete.setPresentTeam(_team.getTeamName());

							atheleteRepository.save(athelete);

						}

					} catch (Exception e) {

						throw new NoSuchElementException("No such scouts find at here...");

					}

				} else if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_DOCTOR)
						|| teamJoinRequest.getRoleType() == TeamJoinRequestRole.ROLE_DOCTOR
						|| TeamJoinRequestRole.ROLE_DOCTOR.equals(teamJoinRequest.getRoleType())) {

					try {

						Doctor doctor = doctorRepository.findByUserId(userId);

						if (doctor == null) {

							throw new Exception();

						}

						try {

							Team team2 = teamRepository.findByDoctorsContainingIgnoreCase(doctor.getId());

							if (team2 != null) {

								throw new ArithmeticException();

							}

						} catch (ArithmeticException e) {

							throw new ArithmeticException("You are already in a team");

						} catch (Exception e) {

						}

					} catch (Exception e) {

						throw new NoSuchElementException("No such doctor present at here...");

					}

				}

			}

			if (!teamJoinRequest.getStatus().equals(TeamJoinRequestStatus.ROLE_PENDING)) {

				throw new Exception("existing team join request is not applicable by the team owner....");

			}

		} catch (NoSuchElementException e) {

			throw new NoSuchElementException(e.getMessage());

		} catch (Exception e) {

			throw new NoSuchElementException(e.getMessage());

		}

		try {

			Team team = null;

			System.out.println(
					"Given athelete response :- " + atheleteResponse.equals(AtheletesTeamJoiningResponse.ROLE_ACCEPT));

			if (atheleteResponse == (AtheletesTeamJoiningResponse.ROLE_ACCEPT)) {

				if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_ATHLETE)) {

					System.out.println("Athelete present team name is adding...");

					Athelete athelete = atheleteRepository.findByUserId(userId).get();

					team = teamRepository.findById(teamJoinRequest.getTeamId()).get();

					if (!teamJoinRequest.getReceiverId().equals(athelete.getId())) {

						throw new Exception("User id and reciver id is matched...");

					}

					athelete.setPresentTeam(team.getTeamName());

					atheleteRepository.save(athelete);

					TeamOwner teamOwner = teamOwnerRepository.findByTeamsContainingIgnoreCase(team.getId());

					if (teamOwner == null) {

						throw new Exception("Team owner not find...");

					}

					try {

						team.getAtheletes().add(athelete.getId());

						team = teamRepository.save(team);

					} catch (Exception e) {

						List<String> list = new ArrayList<>();

						list.add(athelete.getId());

						team.setAtheletes(list);
						team = teamRepository.save(team);

					}

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

					coach.setTeamName(team.getTeamName());

					coachRepository.save(coach);

					try {

						team.getCoaches().add(coach.getId());

						team = teamRepository.save(team);

					} catch (Exception e) {

						List<String> list = new ArrayList<>();

						list.add(coach.getId());
						team.setCoaches(list);

						team = teamRepository.save(team);

					}

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

					try {

						Team _team1 = teamRepository.findByAtheletesContainingIgnoreCase(athelete.getId());

						if (_team1 == null) {

							athelete.setPresentTeam(team.getTeamName());

						}

					} catch (Exception e) {

						athelete.setPresentTeam(team.getTeamName());

					}

					TeamOwner teamOwner = teamOwnerRepository.findByTeamsContainingIgnoreCase(team.getId());

					if (teamOwner == null) {

						throw new Exception();

					}

					System.out.println("Team owner find...");

					try {

						team.getScouts().add(scout.getId());

						team = teamRepository.save(team);

					} catch (Exception e) {

						List<String> list = new ArrayList<>();

						list.add(scout.getId());

						team.setScouts(list);

						team = teamRepository.save(team);

					}

					if (team != null) {

						teamJoinRequestRepository.deleteById(teamJoinRequestId);

					}

				} else if (teamJoinRequest.getRoleType().equals(TeamJoinRequestRole.ROLE_DOCTOR)) {

					Doctor doctor = doctorRepository.findByUserId(userId);

					if (doctor == null) {

						throw new Exception();

					}

					System.out.println("find doctor");

					if (!teamJoinRequest.getReceiverId().equals(doctor.getId())) {

						throw new Exception();

					}

					System.out.println("match receiver id...");

					team = teamRepository.findById(teamJoinRequest.getTeamId()).get();

					if (team == null) {

						throw new Exception();

					}

					TeamOwner teamOwner = teamOwnerRepository.findByTeamsContainingIgnoreCase(team.getId());

					if (teamOwner == null) {

						throw new Exception();

					}

					try {

						Team _team1 = teamRepository.findByDoctorsContainingIgnoreCase(doctor.getId());

						if (_team1 != null) {

							throw new ArithmeticException();

						}

					} catch (ArithmeticException e) {

						throw new Exception();

					} catch (Exception e) {

					}

					try {

						team.getDoctors().add(doctor.getId());
						team = teamRepository.save(team);

					} catch (Exception e) {

						List<String> doctors = new ArrayList<>();

						doctors.add(doctor.getId());

						team.setDoctors(doctors);
						team = teamRepository.save(team);

					}

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

			List<TeamJoinRequest> list = teamJoinRequestRepository.findByReceiverId(teamJoinRequest.getReceiverId());

			for (TeamJoinRequest i : list) {

				try {

					teamJoinRequestRepository.deleteById(i.getId());

				} catch (Exception e) {

					throw new ArithmeticException("Some other's member request for that player is still at here...");

				}

			}

			try {

				notificationService.sendNotification(userId,
						user.getName() + " you are now a member of " + _team.getTeamName());

				TeamOwner teamOwner = teamOwnerRepository.findById(_team.getTeamOwnerId()).get();

				Athelete athlete = atheleteRepository.findById(teamOwner.getAtheleteId()).get();

				notificationService.sendNotification(athlete.getUserId(),
						user.getName() + " accept your team join request for team " + _team.getTeamName());

			} catch (Exception e) {

			}

		}

		return yes;
	}

	@Override
	public List<TeamJoinRequestResponse> searchByTeamId(String teamId) {

		if (teamId == null) {

			throw new NullPointerException("False request...");

		}

		List<TeamJoinRequest> list = teamJoinRequestRepository.findByTeamId(teamId);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No value find at here...");

		}

		return getTeamJoinRequestResponse(list);
	}

	private ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

	private TeamJoinRequestResponse getTeamJoinRequestResponse(TeamJoinRequest teamJoinRequest) {

		return getTeamJoinRequestResponse(List.of(teamJoinRequest)).get(0);

	}

	private List<TeamJoinRequestResponse> getTeamJoinRequestResponse(List<TeamJoinRequest> list) {

		List<TeamJoinRequestResponse> responses = new ArrayList<>();

		CompletableFuture<List<String>> teamIdFuture = CompletableFuture.supplyAsync(
				() -> list.stream().map(TeamJoinRequest::getTeamId).collect(Collectors.toList()), executor);

		CompletableFuture<Map<String, Team>> teamFuture = teamIdFuture.thenApplyAsync(teamsId -> {

			if (teamsId.isEmpty()) {

				return new HashMap<>();

			}

			return teamRepository.findAllById(teamsId).stream()
					.collect(Collectors.toMap(Team::getId, Function.identity()));

		}, executor);

		CompletableFuture<List<String>> userIdFuture = CompletableFuture.supplyAsync(() -> {

			List<String> sendersId = list.stream().map(TeamJoinRequest::getSenderId).collect(Collectors.toList());

			Set<String> set = new HashSet<>(sendersId);

			/*List<String> receiversId = list.stream().map(TeamJoinRequest::getReceiverId).collect(Collectors.toList());

			for (String id : receiversId) {

				set.add(id);

			}*/

			return new ArrayList<>(set);

		}, executor);

		CompletableFuture<Map<String, User>> userFuture = userIdFuture.thenApplyAsync(usersId -> {

			if (usersId.isEmpty()) {

				return new HashMap<>();

			}

			return userRepository.findAllById(usersId).stream()
					.collect(Collectors.toMap(User::getId, Function.identity()));

		}, executor);

		CompletableFuture.allOf(teamIdFuture, teamFuture, userIdFuture, userFuture).join();

		Map<String, Team> teamMap = teamFuture.join();
		Map<String, User> userMap = userFuture.join();
		
		for(TeamJoinRequest request : list) {
			
			try {
				
				TeamJoinRequestResponse response = new TeamJoinRequestResponse();
				
				response.setId(request.getId());
				response.setPrice(request.getPrice());
				response.setRequestStartTime(request.getRequestStartTime());
				response.setRequestEndTime(request.getRequestEndTime());
				response.setRoleType(request.getRoleType());
				response.setSenderId(request.getSenderId());
				response.setReceiverId(request.getReceiverId());
				response.setSenderName(userMap.get(request.getSenderId()).getName());
				response.setTeamId(request.getTeamId());
				response.setTeamName(teamMap.get(request.getTeamId()).getTeamName());
				response.setStatus(request.getStatus());
				
				responses.add(response);
				
			} catch(Exception e) {
				
			}
			
		}
		
		return responses;

	}

}
