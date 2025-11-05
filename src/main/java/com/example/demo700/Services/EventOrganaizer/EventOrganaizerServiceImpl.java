package com.example.demo700.Services.EventOrganaizer;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.EventOrganaizer.EventOrganaizer;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.EventOrganaizer.EventOrganaizerRepository;

@Service
public class EventOrganaizerServiceImpl implements EventOrganaizerService {

	@Autowired
	private EventOrganaizerRepository eventOrganaizerRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public EventOrganaizer addEventOrganaizer(EventOrganaizer eventOrganaizer, String userId) {

		if (userId == null | eventOrganaizer == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getRoles().contains(Role.ROLE_ORGANIZER)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		try {

			EventOrganaizer _eventOrganaizer = eventOrganaizerRepository
					.findByOrganaizationName(eventOrganaizer.getOrganaizationName());

			if (_eventOrganaizer != null) {

				throw new Exception("This event organaization is already exist at here...");

			}

			_eventOrganaizer = eventOrganaizerRepository.findByUserId(userId);

			if (_eventOrganaizer != null) {

				throw new Exception("One user can't be the owner of multiple event organaizer...");

			}

		} catch (Exception e) {

			throw new ArithmeticException(e.getMessage());

		}
		
		try {
			
			if(!eventOrganaizer.getMatches().isEmpty()) {
				
				throw new Exception();
				
			}
			
		} catch(Exception e) {
			
			throw new ArithmeticException("No event organaizer can arrange event in time of creation...");
			
		}

		eventOrganaizer = eventOrganaizerRepository.save(eventOrganaizer);

		if (eventOrganaizer == null) {

			return null;

		}

		return eventOrganaizer;
	}

	@Override
	public List<EventOrganaizer> seeAll() {

		return eventOrganaizerRepository.findAll();

	}

	@Override
	public EventOrganaizer findByUserId(String userId) {

		if (userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("This is not a valid user...");

		}

		EventOrganaizer eventOrganaizer = eventOrganaizerRepository.findByUserId(userId);

		return eventOrganaizer;
	}

	@Override
	public EventOrganaizer findByOrganaizationName(String organaizationName) {

		if (organaizationName == null) {

			throw new NullPointerException("False request..");

		}

		EventOrganaizer eventOrganaizer = eventOrganaizerRepository.findByOrganaizationName(organaizationName);

		return eventOrganaizer;
	}

	@Override
	public List<EventOrganaizer> findByMatchesContainingIgnoreCase(String matchId) {

		if (matchId == null) {

			throw new NullPointerException("False request...");

		}

		return eventOrganaizerRepository.findByMatchesContainingIgnoreCase(matchId);
	}

	@SuppressWarnings("null")
	@Override
	public EventOrganaizer updateEventOrganaizer(String userId, String eventOrganaizerId,
			EventOrganaizer eventOrganaizer) {

		if (userId == null | eventOrganaizer == null || eventOrganaizerId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getRoles().contains(Role.ROLE_ORGANIZER)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		try {

			EventOrganaizer _eventOrganaizer = eventOrganaizerRepository.findById(eventOrganaizerId).get();

			if (_eventOrganaizer == null) {

				throw new Exception();

			}

			if (!_eventOrganaizer.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException(
					"There is no such valid event organaization according to your information...");

		}

		try {

			EventOrganaizer _eventOrganaizer = eventOrganaizerRepository
					.findByOrganaizationName(eventOrganaizer.getOrganaizationName());

			if (!_eventOrganaizer.getUserId().equals(userId)) {

				throw new Exception("Duplicate venet organaization name...");

			}

			_eventOrganaizer = eventOrganaizerRepository.findByUserId(userId);

			if (_eventOrganaizer == null) {

				throw new Exception("One user can't be the owner of multiple event organaizer...");

			}

		} catch (Exception e) {

			throw new ArithmeticException(e.getMessage());

		}

		eventOrganaizer.setId(eventOrganaizerId);
		
		eventOrganaizer = eventOrganaizerRepository.save(eventOrganaizer);

		if (eventOrganaizer == null) {

			return null;

		}

		return eventOrganaizer;
	}

	@Override
	public boolean deleteEventOrganaizer(String userId, String eventOrganaizerId) {

		if (userId == null || eventOrganaizerId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = eventOrganaizerRepository.count();

				eventOrganaizerRepository.deleteById(eventOrganaizerId);

				return count != eventOrganaizerRepository.count();

			}

			if (!user.getRoles().contains(Role.ROLE_ORGANIZER)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		try {

			EventOrganaizer _eventOrganaizer = eventOrganaizerRepository.findById(eventOrganaizerId).get();

			if (_eventOrganaizer == null) {

				throw new Exception();

			}

			if (!_eventOrganaizer.getUserId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException(
					"There is no such valid event organaization according to your information...");

		}

		long count = eventOrganaizerRepository.count();
		
		cleaner.removeEventOrganaizer(eventOrganaizerId);

		return count != eventOrganaizerRepository.count();
	}

}
