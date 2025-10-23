package com.example.demo700.CyclicCleaner;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo700.Models.User;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.Coach;
import com.example.demo700.Models.Athlete.Scouts;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.Athlete.TeamOwner;
import com.example.demo700.Models.EventOrganaizer.EventOrganaizer;
import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Models.Turf.Booking;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.CoachRepository;
import com.example.demo700.Repositories.Athelete.ScoutsRepository;
import com.example.demo700.Repositories.Athelete.TeamOwnerRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.EventOrganaizer.EventOrganaizerRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;
import com.example.demo700.Repositories.Turf.BookingRepository;

public class CyclicCleaner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AtheleteRepository atheleteRepository;

	@Autowired
	private CoachRepository coachRepository;

	@Autowired
	private ScoutsRepository scoutsRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private TeamOwnerRepository teamOwnerRepository;
	
	@Autowired
	private EventOrganaizerRepository eventOrganaizerRepository;
	
	@Autowired
	private BookingRepository bookingRepository;

	public void removeUser(String userId) {

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			long count = userRepository.count();

			if (count != userRepository.count()) {

				userRepository.deleteById(userId);

				Athelete athelete = atheleteRepository.findByUserId(userId).get();

				if (athelete == null) {

					throw new Exception();

				}

				removeAthelete(athelete.getId());

				EventOrganaizer eventOrganaizer = eventOrganaizerRepository.findByUserId(userId);

				if (eventOrganaizer == null) {

					throw new Exception();

				}

				removeEventOrganaizer(eventOrganaizer.getId());
				
				List<Booking> list = bookingRepository.findByUserId(userId);
				
				for(Booking booking : list) {
					
					if(booking != null) {
						
						removeBooking(booking.getId());
						
					}
					
				}

			}

		} catch (Exception e) {

		}

	}

	public void removeAthelete(String athelteId) {

		try {

			Athelete athelete = atheleteRepository.findById(athelteId).get();

			if (athelete == null) {

				throw new Exception();

			}

			long count = atheleteRepository.count();

			atheleteRepository.deleteById(athelteId);

			if (count != atheleteRepository.count()) {

				Coach coach = coachRepository.findByAtheleteId(athelteId);

				if (coach != null) {

					removeCoach(coach.getId());

				}

				Scouts scouts = scoutsRepository.findByAtheleteId(athelteId);

				if (scouts != null) {

					removeScout(scouts.getId());

				}

				TeamOwner teamOwner = teamOwnerRepository.findByAtheleteId(athelteId);

				if (teamOwner != null) {

					removeTeamOwner(teamOwner.getId());

				}

				Team team = teamRepository.findByAtheletesContainingIgnoreCase(athelteId);

				if (team != null) {

					team.getAtheletes().remove(athelteId);

					teamRepository.save(team);

				}

				List<Match> list = matchRepository.findByTeamsContainingIgnoreCase(team.getId());

				if (!list.isEmpty()) {

					for (Match i : list) {

						removeMatch(i.getId());

					}

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeCoach(String coachId) {

		try {

			Coach coach = coachRepository.findById(coachId).get();

			if (coach != null) {

				long count = coachRepository.count();

				coachRepository.deleteById(coachId);

				if (count != coachRepository.count()) {

					Team team = teamRepository.findByCoachesContainingIgnoreCase(coachId);

					if (team != null) {

						team.getCoaches().remove(coachId);

						teamRepository.save(team);

					}

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeScout(String scoutId) {

		try {

			Scouts scout = scoutsRepository.findById(scoutId).get();

			if (scout != null) {

				long count = scoutsRepository.count();

				scoutsRepository.deleteById(scoutId);

				if (count != scoutsRepository.count()) {

					Team team = teamRepository.findByScoutsContainingIgnoreCase(scoutId);

					if (team != null) {

						team.getScouts().remove(scoutId);

						teamRepository.save(team);

					}

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeTeamOwner(String teamOwnerId) {

		try {

			TeamOwner teamOwner = teamOwnerRepository.findById(teamOwnerId).get();

			if (teamOwner != null) {

				long count = teamOwnerRepository.count();

				teamOwnerRepository.deleteById(teamOwnerId);

				if (count != teamOwnerRepository.count()) {

					List<String> list = teamOwner.getTeams();

					if (!list.isEmpty()) {

						for (String i : list) {

							removeTeam(i);

						}

					}

					list = new ArrayList<>();

					list = teamOwner.getMatches();

					if (!list.isEmpty()) {

						for (String i : list) {

							removeMatch(i);

						}

					}

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeTeam(String teamId) {

		try {

			Team team = teamRepository.findById(teamId).get();

			if (team != null) {

				long count = teamRepository.count();

				teamRepository.deleteById(teamId);

				if (count != teamRepository.count()) {
					
					List<Athelete> list = atheleteRepository.findByPresentTeamIgnoreCase(teamId);
					
					if(!list.isEmpty()) {
						
						for(Athelete i : list) {
							
							i.setPresentTeam("");
							
							atheleteRepository.save(i);
							
						}
						
					}
					
					List<Match> matches = matchRepository.findByTeamsContainingIgnoreCase(team.getId());
					
					if(!matches.isEmpty()) {
						
						for(Match i : matches) {
							
							removeMatch(i.getId());
							
						}
						
					}
					
				}

			}

		} catch (Exception e) {

		}

	}

	public void removeMatch(String matchId) {
		
		try {
			
			Match match = matchRepository.findById(matchId).get();
			
			if(match != null) {
				
				long count = matchRepository.count();
				
				matchRepository.deleteById(matchId);
				
				if(count != matchRepository.count()) {
					
					List<Athelete> list = atheleteRepository.findByEventAttendenceContainingIgnoreCase(matchId);
					
					if(!list.isEmpty()) {
						
						for(Athelete i : list) {
							
							i.getEventAttendence().remove(matchId);
							
							atheleteRepository.save(i);
							
						}
						
					}
					
					List<Scouts> _list = scoutsRepository.findByMatchesContainingIgnoreCase(matchId);
					
					if(!_list.isEmpty()) {
						
						for(Scouts scout : _list) {
							
							scout.getMatches().remove(matchId);
							
							scoutsRepository.save(scout);
							
						}
						
					}
					
					_list.clear();
					
					_list = scoutsRepository.findByEventsContainingIgnoreCase(matchId);
					
					if(!_list.isEmpty()) {
						
						for(Scouts scout : _list) {
							
							scout.getEvents().remove(matchId);
							
							scoutsRepository.save(scout);
							
						}
						
					}
					
					List<String> teams = match.getTeams();
					
					for(String i : teams) {
						
						Team _team = teamRepository.findById(i).get();
						
						if(_team != null) {
							
							_team.getMatches().remove(matchId);
							
							teamRepository.save(_team);
							
						}
						
					}
					
				}
				
			}
			
		} catch(Exception e) {
			
		}

	}

	public void removeEventOrganaizer(String eventOrganaizerId) {
		
		try {
			
			EventOrganaizer eventOrganaizer = eventOrganaizerRepository.findById(eventOrganaizerId).get();
			
			if(eventOrganaizer != null) {
				
				long count = eventOrganaizerRepository.count();
				
				eventOrganaizerRepository.deleteById(eventOrganaizerId);
				
				if(count != eventOrganaizerRepository.count()) {
					
					List<String> matches = eventOrganaizer.getMatches();
					
					if(!matches.isEmpty()) {
						
						for(String i : matches) {
							
							removeMatch(i);
							
						}
						
					}
					
				}
				
			}
			
		} catch(Exception e) {
			
			
		}

	}
	
	public void removeBooking(String bookingId) {
		
		try {
			
			Booking booking = bookingRepository.findById(bookingId).get();
			
			if(booking == null) {
				
				throw new Exception();
				
			}
			
			bookingRepository.deleteById(booking.getId());
			
		} catch(Exception e) {
			
		}
		
	}

}
