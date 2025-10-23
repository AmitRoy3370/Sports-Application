package com.example.demo700.Services.EventOrganaizer;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.ENUMS.BookingStatus;

import com.example.demo700.ENUMS.Role;
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
import com.example.demo700.Validator.URLValidator;

@Service
public class MatchServiceImpl implements MatchService {

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private EventOrganaizerRepository eventOrganaizerRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private AtheleteRepository atheleteRepository;

	@Autowired
	private CoachRepository coachRepository;

	@Autowired
	private ScoutsRepository scoutsRepository;

	private TeamOwnerRepository teamOwnerRepository;

	private URLValidator urlValidator = new URLValidator();

	@Override
	public Match createMatch(Match match, String userId) {

		if (userId == null || match == null) {

			throw new NullPointerException();

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			EventOrganaizer eventOrganaizer = eventOrganaizerRepository.findByUserId(userId);

			if (eventOrganaizer == null) {

				throw new Exception();

			}

			if (!eventOrganaizer.getId().equals(match.getOrganaizerId())) {

				throw new Exception();

			}

			List<Booking> bookings = bookingRepository.findByUserId(userId);

			if (bookings.isEmpty()) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			if (match.getMatchStartTime().isAfter(match.getMatchEndTime())) {

				throw new Exception();

			}

			List<Booking> bookings = bookingRepository.findByUserId(userId);

			boolean find = false;

			for (Booking booking : bookings) {

				if (booking.getStartTime().isBefore(match.getMatchStartTime())
						&& booking.getEndTime().isAfter(match.getMatchEndTime())) {

					if (booking.getStatus() == BookingStatus.CONFIRMED) {

						find = true;
						break;

					}

				}

			}

			if (!find) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Match time schedule is not valid...");

		}

		try {

			if (!match.getGameLogs().isEmpty()) {

				boolean valid = urlValidator.isValid(match.getGameLogs());

				if (!valid) {

					throw new Exception();

				}

			}

			if (!match.getVideos().isEmpty()) {

				boolean valid = urlValidator.isValid(match.getVideos());

				if (!valid) {

					throw new Exception();

				}

			}

		} catch (Exception e) {

			throw new ArithmeticException("Link's are not valid...");

		}

		try {

			if (match.getTeams().isEmpty() || match.getTeams().size() < 2) {

				throw new Exception();

			}

			for (String i : match.getTeams()) {

				if (i == null) {

					throw new Exception();

				}

				Team team = teamRepository.findById(i).get();

				if (team == null) {

					throw new Exception();

				}

			}

		} catch (Exception e) {

			throw new NoSuchElementException("Team's are not valid...");

		}

		match = matchRepository.save(match);

		if (match != null) {

			try {

				if (match.getTeams().isEmpty() || match.getTeams().size() < 2) {

					throw new Exception();

				}

				for (String i : match.getTeams()) {

					if (i == null) {

						throw new Exception();

					}

					Team team = teamRepository.findById(i).get();

					if (team == null) {

						throw new Exception();

					}

					if (!team.getMatches().contains(match.getId())) {

						team.getMatches().add(match.getId());

						teamRepository.save(team);

					}

					TeamOwner teamOwner = teamOwnerRepository.findByTeamsContainingIgnoreCase(team.getId());

					if (teamOwner != null) {

						if (!teamOwner.getMatches().contains(match.getId())) {

							teamOwner.getMatches().add(match.getId());

							teamOwnerRepository.save(teamOwner);

						}

					}

					for (String j : team.getAtheletes()) {

						Athelete athelete = atheleteRepository.findById(j).get();

						if (athelete == null) {

							throw new Exception();

						}

						athelete.getEventAttendence().add(match.getId());

						atheleteRepository.save(athelete);

					}

					for (String j : team.getScouts()) {

						Scouts scouts = scoutsRepository.findById(j).get();

						if (scouts == null) {

							throw new Exception();

						}

						scouts.getMatches().add(match.getId());

						scoutsRepository.save(scouts);

					}

				}

			} catch (Exception e1) {

				throw new NoSuchElementException("Team's are not valid...");

			}

		}

		return match;
	}

	@Override
	public List<Match> seeAllMatch() {

		return matchRepository.findAll();
	}

	@Override
	public List<Match> findByOrganaizerId(String organaizerId) {

		if (organaizerId == null) {

			throw new NullPointerException("False request...");

		}

		return matchRepository.findByOrganaizerId(organaizerId);
	}

	@Override
	public List<Match> findByTeamsContainingIgnoreCase(String teamId) {

		if (teamId == null) {

			throw new NullPointerException("False request...");

		}

		return matchRepository.findByTeamsContainingIgnoreCase(teamId);
	}

	@Override
	public List<Match> findByGameLogsContainingIgnoreCase(String gameLogs) {

		if (gameLogs == null) {

			throw new NullPointerException("False request...");

		}

		return matchRepository.findByGameLogsContainingIgnoreCase(gameLogs);
	}

	@Override
	public List<Match> findByVideosContainingIgnoreCase(String video) {

		if (video == null) {

			throw new NullPointerException("False request...");

		}

		return matchRepository.findByVideosContainingIgnoreCase(video);
	}

	@Override
	public Match findByMatchStartTimeAndMatchEndTime(Instant matchStartTime, Instant matchEndTime) {

		if (matchStartTime == null || matchEndTime == null) {

			throw new NullPointerException("False request...");

		}

		if (matchEndTime.isBefore(matchStartTime)) {

			throw new ArithmeticException("Error is time schedule...");

		}

		return matchRepository.findByMatchStartTimeAndMatchEndTime(matchStartTime, matchEndTime);
	}

	@Override
	public Match updateMatch(Match match, String userId, String matchId) {

		if (match == null || userId == null || matchId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			EventOrganaizer eventOrganaizer = eventOrganaizerRepository.findByUserId(userId);

			if (eventOrganaizer == null) {

				throw new Exception();

			}

			if (!eventOrganaizer.getId().equals(match.getOrganaizerId())) {

				throw new Exception();

			}

			List<Booking> bookings = bookingRepository.findByUserId(userId);

			if (bookings.isEmpty()) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			if (match.getMatchStartTime().isAfter(match.getMatchEndTime())) {

				throw new Exception();

			}

			List<Booking> bookings = bookingRepository.findByUserId(userId);

			boolean find = false;

			for (Booking booking : bookings) {

				if (booking.getStartTime().isBefore(match.getMatchStartTime())
						&& booking.getEndTime().isAfter(match.getMatchEndTime())) {

					if (booking.getStatus() == BookingStatus.CONFIRMED) {

						find = true;
						break;

					}

				}

			}

			if (!find) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Match time schedule is not valid...");

		}

		try {

			if (!match.getGameLogs().isEmpty()) {

				boolean valid = urlValidator.isValid(match.getGameLogs());

				if (!valid) {

					throw new Exception();

				}

			}

			if (!match.getVideos().isEmpty()) {

				boolean valid = urlValidator.isValid(match.getVideos());

				if (!valid) {

					throw new Exception();

				}

			}

		} catch (Exception e) {

			throw new ArithmeticException("Link's are not valid...");

		}

		try {

			if (match.getTeams().isEmpty() || match.getTeams().size() < 2) {

				throw new Exception();

			}

			for (String i : match.getTeams()) {

				if (i == null) {

					throw new Exception();

				}

				Team team = teamRepository.findById(i).get();

				if (team == null) {

					throw new Exception();

				}

				if (!team.getMatches().contains(match.getId())) {

					team.getMatches().add(match.getId());

					teamRepository.save(team);

				}

				TeamOwner teamOwner = teamOwnerRepository.findByTeamsContainingIgnoreCase(team.getId());

				if (teamOwner != null) {

					if (!teamOwner.getMatches().contains(match.getId())) {

						teamOwner.getMatches().add(match.getId());

						teamOwnerRepository.save(teamOwner);

					}

				}

				for (String j : team.getAtheletes()) {

					Athelete athelete = atheleteRepository.findById(j).get();

					if (athelete == null) {

						throw new Exception();

					}

					athelete.getEventAttendence().add(matchId);

					atheleteRepository.save(athelete);

				}

				for (String j : team.getScouts()) {

					Scouts scouts = scoutsRepository.findById(j).get();

					if (scouts == null) {

						throw new Exception();

					}

					scouts.getMatches().add(matchId);

					scoutsRepository.save(scouts);

				}

			}

		} catch (Exception e1) {

			throw new NoSuchElementException("Team's are not valid...");

		}

		try {

			Match _match = matchRepository.findById(matchId).get();

			if (_match == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("There is no such match find at here...");

		}

		match.setId(matchId);

		match = matchRepository.save(match);

		return match;
	}

	@Override
	public boolean deleteMatch(String matchId, String userId) {

		if (matchId == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Match _match = matchRepository.findById(matchId).get();

			if (_match == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("There is no such match find at here...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = matchRepository.count();

				matchRepository.deleteById(matchId);

				return count != matchRepository.count();

			}

			EventOrganaizer eventOrganaizer = eventOrganaizerRepository.findByUserId(userId);

			if (eventOrganaizer == null) {

				throw new Exception();

			}

			List<Booking> bookings = bookingRepository.findByUserId(userId);

			if (bookings.isEmpty()) {

				throw new Exception();

			}

			Match match = matchRepository.findById(matchId).get();

			if (match == null) {

				throw new Exception();

			}

			if (!match.getOrganaizerId().equals(eventOrganaizer.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here to delete a match...");

		}

		try {

			Match match = matchRepository.findById(matchId).get();

			if (match.getTeams().isEmpty() || match.getTeams().size() < 2) {

				throw new Exception();

			}

			for (String i : match.getTeams()) {

				if (i == null) {

					throw new Exception();

				}

				Team team = teamRepository.findById(i).get();

				if (team == null) {

					throw new Exception();

				}

				for (String j : team.getAtheletes()) {

					Athelete athelete = atheleteRepository.findById(j).get();

					if (athelete == null) {

						throw new Exception();

					}

					athelete.getEventAttendence().remove(matchId);

					atheleteRepository.save(athelete);

				}

				for (String j : team.getScouts()) {

					Scouts scouts = scoutsRepository.findById(j).get();

					if (scouts == null) {

						throw new Exception();

					}

					scouts.getMatches().remove(matchId);

					scoutsRepository.save(scouts);

				}

			}

		} catch (Exception e1) {

			throw new NoSuchElementException("Team's are not valid...");

		}

		long count = matchRepository.count();

		matchRepository.deleteById(matchId);

		return count != matchRepository.count();
	}

	@Override
	public List<Match> findByPriceGreaterThan(double price) {

		return matchRepository.findByPriceGreaterThan(price);
	}

}
