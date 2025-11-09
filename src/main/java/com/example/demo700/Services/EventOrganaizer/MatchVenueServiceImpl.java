package com.example.demo700.Services.EventOrganaizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.BookingStatus;
import com.example.demo700.Models.User;
import com.example.demo700.Models.EventOrganaizer.EventOrganaizer;
import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Models.EventOrganaizer.MatchVenue;
import com.example.demo700.Models.Turf.Booking;
import com.example.demo700.Models.Turf.Venue;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.EventOrganaizer.EventOrganaizerRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchVenueRepository;
import com.example.demo700.Repositories.Turf.BookingRepository;
import com.example.demo700.Repositories.Turf.VenueRepository;

@Service
public class MatchVenueServiceImpl implements MatchVenueService {

	@Autowired
	private MatchVenueRepository matchVenueRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EventOrganaizerRepository eventOrganaizerRepository;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private VenueRepository venueRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public MatchVenue addMatchVenue(MatchVenue matchVenue, String userId) {

		if (matchVenue == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			System.out.println("User find...");

			EventOrganaizer eventOrganaizer = eventOrganaizerRepository.findByUserId(user.getId());

			if (eventOrganaizer == null) {

				throw new Exception();

			}

			System.out.println("event organaizer find...");

			List<Match> matches = matchRepository.findByOrganaizerId(eventOrganaizer.getId());

			if (matches.isEmpty()) {

				throw new Exception();

			}

			System.out.println("Matches find...");

			System.out.println("matches :- " + matches.toString());

			List<String> matchIds = new ArrayList<>();

			for (Match i : matches) {

				matchIds.add(i.getId());

			}

			if (!matchIds.contains(matchVenue.getMatchId())) {

				throw new Exception();

			}

			System.out.println("match contains in the evern organaizer's match list...");

			Venue venue = venueRepository.findById(matchVenue.getVenueId()).get();

			if (venue == null) {

				System.out.println("Venue not find...");

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("Valid user not find to add a match venue at here...");

		}

		try {

			MatchVenue _matchVenue = matchVenueRepository.findByMatchId(matchVenue.getMatchId());

			if (_matchVenue != null) {

				throw new ArithmeticException("Same match can't be organaize in multiple venue...");

			}

			// matchVenueRepository.save(_matchVenue);

		} catch (ArithmeticException e) {

			throw new ArithmeticException(e.getMessage());

		} catch (Exception e) {

		}

		String matchVenueId = null;

		Match match = null;

		try {

			match = matchRepository.findById(matchVenue.getMatchId()).get();

			if (match == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Match information is not valid...");

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

			Map<String, List<Booking>> map = new HashMap<>();

			for (Booking booking : bookings) {

				if (!map.containsKey(booking.getVenueId())) {

					map.put(booking.getVenueId(), new ArrayList<>());
					map.get(booking.getVenueId()).add(booking);

				} else {

					map.get(booking.getVenueId()).add(booking);

				}

			}

			boolean find = false;

			for (String i : map.keySet()) {

				List<Booking> _bookings = map.get(i);

				find = false;

				for (Booking booking : _bookings) {

					if (booking.getStartTime().isBefore(match.getMatchStartTime())
							&& booking.getEndTime().isAfter(match.getMatchEndTime())) {

						if (booking.getStatus() == BookingStatus.CONFIRMED) {

							// find = true;
							matchVenueId = booking.getVenueId();

							if (matchVenueId.equals(matchVenue.getVenueId())) {

								find = true;
								break;

							}

						}

					}

				}

				if (find) {

					break;

				}

			}

			if (!find) {

				System.out.println("Not find any valid schedule...");

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Match time schedule is not valid...");

		}

		matchVenue = matchVenueRepository.save(matchVenue);

		return matchVenue;
	}

	@Override
	public List<MatchVenue> seeAllMatchVenue() {

		return matchVenueRepository.findAll();
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public MatchVenue updateMatchVenue(MatchVenue matchVenue, String userId, String matchVenueId) {

		if (matchVenue == null || userId == null || matchVenueId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			EventOrganaizer eventOrganaizer = eventOrganaizerRepository.findByUserId(user.getId());

			if (eventOrganaizer == null) {

				throw new Exception();

			}

			List<Match> matches = matchRepository.findByOrganaizerId(eventOrganaizer.getId());

			if (matches.isEmpty()) {

				throw new Exception();

			}
			
			List<String> matchIds = new ArrayList<>();
			
			for(Match i : matches) {
				
				matchIds.add(i.getId());
				
			}

			if (!matchIds.contains(matchVenue.getMatchId())) {

				throw new Exception();

			}

			Match match = matchRepository.findById(matchVenue.getMatchId()).get();

			if (!match.getOrganaizerId().equals(eventOrganaizer.getId())) {

				throw new Exception();

			}

			if (!match.getOrganaizerId().equals(eventOrganaizer.getId())) {

				throw new Exception();

			}

			Venue venue = venueRepository.findById(matchVenue.getVenueId()).get();

			if (venue == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("Valid user not find to add a match venue at here...");

		}

		try {

			MatchVenue _matchVenue = matchVenueRepository.findById(matchVenueId).get();

			if (_matchVenue == null) {

				throw new Exception();

			}

			// matchVenueRepository.save(_matchVenue);

		} catch (Exception e) {

			throw new NoSuchElementException("Your match venue id is not valid....");

		}

		try {

			MatchVenue _matchVenue = matchVenueRepository.findByMatchId(matchVenue.getMatchId());

			if (_matchVenue != null && !_matchVenue.getId().equals(matchVenueId)) {

				throw new ArithmeticException("Same match can't be organaize in multiple venue...");

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException(e.getMessage());

		} catch (Exception e) {

			throw new ArithmeticException("No such match venue find to update...");

		}

		matchVenue.setId(matchVenueId);

		String _matchVenueId = null;

		Match match = null;

		try {

			match = matchRepository.findById(matchVenue.getMatchId()).get();

			if (match == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Match information is not valid...");

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

			Map<String, List<Booking>> map = new HashMap<>();

			for (Booking booking : bookings) {

				if (!map.containsKey(booking.getVenueId())) {

					map.put(booking.getVenueId(), new ArrayList<>());
					map.get(booking.getVenueId()).add(booking);

				} else {

					map.get(booking.getVenueId()).add(booking);

				}

			}

			boolean find = false;

			for (String i : map.keySet()) {

				List<Booking> _bookings = map.get(i);

				find = false;

				for (Booking booking : _bookings) {

					if (booking.getStartTime().isBefore(match.getMatchStartTime())
							&& booking.getEndTime().isAfter(match.getMatchEndTime())) {

						if (booking.getStatus() == BookingStatus.CONFIRMED) {

							// find = true;
							_matchVenueId = booking.getVenueId();

							if (_matchVenueId.equals(matchVenue.getVenueId())) {

								find = true;
								break;

							}

						}

					}

				}

				if (find) {

					break;

				}

			}

			if (!find) {

				System.out.println("Not find any valid schedule...");

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Match time schedule is not valid...");

		}

		matchVenue = matchVenueRepository.save(matchVenue);

		return matchVenue;
	}

	@Override
	public List<MatchVenue> findByVenueId(String venueId) {

		if (venueId == null) {

			throw new NullPointerException("False request...");

		}

		return matchVenueRepository.findByVenueId(venueId);
	}

	@Override
	public MatchVenue findByMatchId(String matchId) {

		if (matchId == null) {

			throw new NullPointerException("False request...");

		}

		return matchVenueRepository.findByMatchId(matchId);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public boolean deleteMatchVenue(String matchVenueId, String userId) {

		if (matchVenueId == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			MatchVenue matchVenue = matchVenueRepository.findById(matchVenueId).get();

			if (matchVenue == null) {

				throw new Exception();

			}

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			EventOrganaizer eventOrganaizer = eventOrganaizerRepository.findByUserId(user.getId());

			if (eventOrganaizer == null) {

				throw new Exception();

			}

			List<Match> matches = matchRepository.findByOrganaizerId(eventOrganaizer.getId());

			if (matches.isEmpty()) {

				throw new Exception();

			}

			if (!matches.contains(matchVenue.getMatchId())) {

				throw new Exception();

			}

			Match match = matchRepository.findById(matchVenueId).get();

			if (!match.getOrganaizerId().equals(eventOrganaizer.getId())) {

				throw new Exception();

			}

			if (!match.getOrganaizerId().equals(eventOrganaizer.getId())) {

				throw new Exception();

			}

			Venue venue = venueRepository.findById(matchVenue.getVenueId()).get();

			if (venue == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("Valid user not find to add a match venue at here...");

		}

		long count = matchVenueRepository.count();

		cleaner.removeMatchVenue(matchVenueId);

		return count != matchVenueRepository.count();
	}

}
