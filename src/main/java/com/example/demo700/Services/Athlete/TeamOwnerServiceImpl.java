package com.example.demo700.Services.Athlete;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.TeamOwnerResponseDTO;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.Coach;
import com.example.demo700.Models.Athlete.Scouts;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.Athlete.TeamOwner;
import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Models.EventOrganaizer.MatchName;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.CoachRepository;
import com.example.demo700.Repositories.Athelete.ScoutsRepository;
import com.example.demo700.Repositories.Athelete.TeamOwnerRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchNameRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;

@Service
public class TeamOwnerServiceImpl implements TeamOwnerService {

	@Autowired
	private TeamOwnerRepository teamOwnerRepository;

	@Autowired
	private AtheleteRepository atheleteRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private MatchNameRepository matchNameRepository;

	@Autowired
	private ScoutsRepository scoutsRepository;

	@Autowired
	private CoachRepository coachRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public TeamOwner addTeamOwner(TeamOwner teamOwner) {

		if (teamOwner == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Athelete athelete = atheleteRepository.findById(teamOwner.getAtheleteId()).get();

			if (athelete == null) {

				throw new Exception();

			}

			User user = userRepository.findById(athelete.getUserId()).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getRoles().contains(Role.ROLE_TEAM_OWNER)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("No such athelete exist at here...");

		}

		try {

			if (!teamOwner.getMatches().isEmpty() || !teamOwner.getTeams().isEmpty()) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("No such team owner have match or team as the time of creation....");

		}

		teamOwner = teamOwnerRepository.save(teamOwner);

		if (teamOwner == null) {

			return null;

		}

		return teamOwner;
	}

	@Override
	public List<TeamOwnerResponseDTO> seeAllTeamOwner() {

		List<TeamOwner> list = teamOwnerRepository.findAll();
		
		List<TeamOwnerResponseDTO> response = new ArrayList<>();
		
		for(TeamOwner i : list) {
			
			response.add(getTeamOwnerResponse(i));
			
		}
		
		return response;

	}

	@Override
	public TeamOwner updateTeamOwner(TeamOwner teamOwner, String userId) {

		if (teamOwner == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Athelete athelete = atheleteRepository.findById(teamOwner.getAtheleteId()).get();

			if (athelete == null) {

				throw new Exception();

			}

			User user = userRepository.findById(athelete.getUserId()).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getRoles().contains(Role.ROLE_TEAM_OWNER) || !userId.equals(user.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("No such athelete exist at here...");

		}

		try {

			TeamOwner _teamOwner = teamOwnerRepository.findById(teamOwner.getId()).get();

			if (_teamOwner == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("Your team owner not exist at here...");

		}

		try {

			if (!teamOwner.getTeams().isEmpty()) {

				for (String i : teamOwner.getTeams()) {

					try {

						Team team = teamRepository.findById(i).get();

						if (team == null) {

							throw new Exception();

						}

						if (!team.getTeamOwnerId().equals(teamOwner.getId())) {

							throw new Exception();

						}

					} catch (Exception e) {

						throw new Exception();

					}

				}

			}

		} catch (Exception e) {

			throw new NoSuchElementException("You teams are not valid...");

		}

		try {

			if (!teamOwner.getMatches().isEmpty()) {

				for (String i : teamOwner.getMatches()) {

					try {

						Match match = matchRepository.findById(i).get();

						if (match == null) {

							throw new Exception();

						}

						if (match.getTeams().isEmpty()) {

							throw new Exception();

						}

						boolean find = false;

						for (String j : match.getTeams()) {

							Team team = teamRepository.findById(j).get();

							if (team == null) {

								throw new Exception();

							}

							if (team.getTeamOwnerId().equals(teamOwner.getId())) {

								find = true;
								break;

							}

						}

						if (!find) {

							throw new Exception();

						}

					} catch (Exception e) {

						throw new Exception();

					}

				}

			}

		} catch (Exception e) {

			throw new NoSuchElementException("Your matches are not valid...");

		}

		teamOwner = teamOwnerRepository.save(teamOwner);

		return teamOwner;

	}

	@Override
	public boolean deleteTeamOwner(String teamOwnerId, String userId) {

		if (teamOwnerId == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		TeamOwner teamOwner = teamOwnerRepository.findById(teamOwnerId).get();

		if (teamOwner == null) {

			return false;

		}

		try {

			Athelete athelete = atheleteRepository.findById(teamOwner.getAtheleteId()).get();

			if (athelete == null) {

				throw new Exception();

			}

			User user = userRepository.findById(athelete.getUserId()).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = teamOwnerRepository.count();

				teamOwnerRepository.deleteById(teamOwnerId);

				if (teamOwnerRepository.count() != count) {

					for (String i : teamOwner.getTeams()) {

						try {

							Team team = teamRepository.findById(i).get();

							if (team != null) {

								List<Match> list = matchRepository.findByTeamsContainingIgnoreCase(team.getId());

								if (list != null) {

									for (Match j : list) {

										Match match = matchRepository.findById(j.getId()).get();

										if (match != null) {

											matchRepository.deleteById(j.getId());

											List<Athelete> atheletes = atheleteRepository
													.findBypresentTeam(team.getTeamName());

											for (Athelete _athelete : atheletes) {

												_athelete.getEventAttendence().remove(team.getId());
												_athelete.setPresentTeam("");

												atheleteRepository.save(_athelete);

											}

											for (String _scout : team.getScouts()) {

												Scouts scout = scoutsRepository.findById(_scout).get();

												if (scout != null) {

													scout.getEvents().remove(team.getId());
													scout.getMatches().remove(team.getId());

													scoutsRepository.save(scout);

												}

											}

											for (String _coach : team.getCoaches()) {

												Coach coach = coachRepository.findById(_coach).get();

												if (coach != null) {

													coach.setTeamName("");

													coachRepository.save(coach);

												}

											}

										}

									}

								}

								cleaner.removeTeam(team.getId());

							}

						} catch (Exception e) {

						}

					}

				}

				return teamOwnerRepository.count() != count;

			}

			if (!user.getRoles().contains(Role.ROLE_TEAM_OWNER) || !userId.equals(user.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("No such athelete exist at here...");

		}

		long count = teamOwnerRepository.count();

		cleaner.removeTeamOwner(teamOwnerId);

		return teamOwnerRepository.count() != count;
	}

	@Override
	public List<TeamOwnerResponseDTO> findByAchivementsContainingIgnoreCase(String achivement) {

		if (achivement == null) {

			throw new NullPointerException("False request...");

		}

		List<TeamOwner> list = teamOwnerRepository.findByAchivementsContainingIgnoreCase(achivement);

		List<TeamOwnerResponseDTO> response = new ArrayList<>();
		
		for(TeamOwner i : list) {
			
			response.add(getTeamOwnerResponse(i));
			
		}
		
		return response;

	}

	@Override
	public List<TeamOwnerResponseDTO> findByMatchesContainingIgnoreCase(String matchId) {
		if (matchId == null) {

			throw new NullPointerException("False request...");

		}

		List<TeamOwner> list = teamOwnerRepository.findByMatchesContainingIgnoreCase(matchId);

		List<TeamOwnerResponseDTO> response = new ArrayList<>();
		
		for(TeamOwner i : list) {
			
			response.add(getTeamOwnerResponse(i));
			
		}
		
		return response;
	}

	@Override
	public TeamOwnerResponseDTO findByTeamsContainingIgnoreCase(String teamId) {
		if (teamId == null) {

			throw new NullPointerException("False request...");

		}

		TeamOwner list = teamOwnerRepository.findByTeamsContainingIgnoreCase(teamId);

		return getTeamOwnerResponse(list);
	}

	@Override
	public TeamOwnerResponseDTO findByAthleteId(String athleteId) {

		if (athleteId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			TeamOwner teamOwner = teamOwnerRepository.findByAtheleteId(athleteId);

			if (teamOwner == null) {

				throw new Exception();

			}

			return getTeamOwnerResponse(teamOwner);

		} catch (Exception e) {

			throw new NoSuchElementException("No such team owner exist at here...");

		}

	}

	@Override
	public TeamOwnerResponseDTO findByTeamOwnerId(String teamOwnerId) {

		if (teamOwnerId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			return getTeamOwnerResponse(teamOwnerRepository.findById(teamOwnerId).get());

		} catch (Exception e) {

			throw new NoSuchElementException("No such team owner find at here..");

		}

	}

	private TeamOwnerResponseDTO getTeamOwnerResponse(TeamOwner teamOwner) {

		TeamOwnerResponseDTO response = new TeamOwnerResponseDTO();

		response.setId(teamOwner.getId());
		response.setAthleteId(teamOwner.getAtheleteId());

		if (!teamOwner.getTeams().isEmpty()) {

			response.setTeamsId(teamOwner.getTeams());

		}

		if (!teamOwner.getMatches().isEmpty()) {

			response.setMatchesId(teamOwner.getMatches());

		}

		if (!teamOwner.getAchivements().isEmpty()) {

			response.setAchivements(teamOwner.getAchivements());

		}

		List<Team> teams = teamRepository.findAllById(teamOwner.getTeams());

		List<String> teamNames = new ArrayList<>();

		for (Team i : teams) {

			teamNames.add(i.getTeamName());

		}

		response.setTeamsName(teamNames);

		List<MatchName> matches = matchNameRepository.findByMatchIdIn(teamOwner.getMatches());

		List<String> matchesName = new ArrayList<>();

		for (MatchName i : matches) {

			matchesName.add(i.getName());

		}

		response.setMatchesName(matchesName);

		try {

			Athelete athlete = atheleteRepository.findById(teamOwner.getAtheleteId()).get();
			
			User user = userRepository.findById(athlete.getUserId()).get();
			
			response.setTeamOwnerName(user.getName());
			
		} catch (Exception e) {

		}

		return response;

	}

}
