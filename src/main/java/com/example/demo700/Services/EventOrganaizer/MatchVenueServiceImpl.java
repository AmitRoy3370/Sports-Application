package com.example.demo700.Services.EventOrganaizer;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.Models.User;
import com.example.demo700.Models.EventOrganaizer.EventOrganaizer;
import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Models.EventOrganaizer.MatchVenue;
import com.example.demo700.Models.Turf.Venue;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.EventOrganaizer.EventOrganaizerRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchVenueRepository;
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

			Venue venue = venueRepository.findById(matchVenue.getVenueId()).get();

			if (venue == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("Valid user not find to add a match venue at here...");

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

			if (!matches.contains(matchVenue.getMatchId())) {

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

		} catch (Exception e) {

			throw new NoSuchElementException("Your match venue id is not valid....");

		}

		matchVenue.setId(matchVenueId);

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
			
			if(matchVenue == null) {
				
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
