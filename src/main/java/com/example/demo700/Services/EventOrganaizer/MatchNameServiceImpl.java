package com.example.demo700.Services.EventOrganaizer;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.EventOrganaizer.EventOrganaizer;
import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Models.EventOrganaizer.MatchName;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.EventOrganaizer.EventOrganaizerRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchNameRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;

@Service
public class MatchNameServiceImpl implements MatchNameService {

	@Autowired
	private EventOrganaizerRepository eventOrganaizerRepository;

	@Autowired
	private MatchNameRepository matchNameRepository;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public MatchName addMatchName(MatchName matchName, String userId) {

		if (matchName == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			MatchName _matchName = matchNameRepository.findByNameIgnoreCase(matchName.getName());

			if (_matchName != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Match name already exist...");

		} catch (Exception e) {

		}

		try {

			if (matchNameRepository.findByMatchId(matchName.getMatchId()) != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This match name is already added...");

		} catch (Exception e) {

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

			Match match = matchRepository.findById(matchName.getMatchId()).get();

			if (match == null) {

				throw new ArithmeticException();

			}

			if (!match.getOrganaizerId().equals(eventOrganaizer.getId())) {

				throw new Exception();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("No such match exist at here...");

		} catch (Exception e) {

			throw new NoSuchElementException("No such valid user find at here...");

		}

		matchName = matchNameRepository.save(matchName);

		return matchName;
	}

	@Override
	public MatchName updateMatchName(MatchName matchName, String userId, String matchNameId) {

		if (matchName == null || userId == null || matchNameId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			MatchName matchName1 = matchNameRepository.findById(matchNameId).get();

			if (matchName1 == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such match name find at here...");

		}

		try {

			if (matchNameRepository.findByMatchId(matchName.getMatchId()) != null) {

				if (!matchNameRepository.findByMatchId(matchName.getMatchId()).getId().equals(matchNameId)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This match name is already added...");

		} catch (Exception e) {

		}

		try {

			MatchName matchName1 = matchNameRepository.findById(matchNameId).get();

			if (matchName1 == null) {

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

			Match match = matchRepository.findById(matchName1.getMatchId()).get();

			if (match == null) {

				throw new ArithmeticException();

			}

			if (!match.getOrganaizerId().equals(eventOrganaizer.getId())) {

				throw new Exception();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("No such match exist at here...");

		} catch (Exception e) {

			throw new NoSuchElementException("No such valid user find at here...");

		}

		matchName.setId(matchNameId);

		matchName = matchNameRepository.save(matchName);

		return matchName;
	}

	@Override
	public MatchName findByNameIgnoreCase(String name) {

		if (name == null) {

			throw new NullPointerException("False request....");

		}

		MatchName matchName = matchNameRepository.findByNameIgnoreCase(name);

		if (matchName == null) {

			throw new NoSuchElementException("No such match name find at here...");

		}

		return matchName;
	}

	@Override
	public MatchName findByMatchId(String matchId) {

		if (matchId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			MatchName matchName = matchNameRepository.findByMatchId(matchId);

			if (matchName == null) {

				throw new Exception();

			}

			return matchName;

		} catch (Exception e) {

			throw new NoSuchElementException("No such match name find at here...");

		}

	}

	@Override
	public List<MatchName> findByNameContainingIgnoreCase(String name) {

		if (name == null) {

			throw new NullPointerException("False request....");

		}

		List<MatchName> list = matchNameRepository.findByNameContainingIgnoreCase(name);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such match name find at here...");

		}

		return list;
	}

	@Override
	public MatchName findById(String id) {

		if (id == null) {

			throw new NullPointerException("False request...");

		}

		try {

			MatchName matchName = matchNameRepository.findById(id).get();

			if (matchName == null) {

				throw new Exception();

			}

			return matchName;

		} catch (Exception e) {

			throw new NoSuchElementException("No such match name find at here...");

		}

	}

	@Override
	public boolean removeMatchName(String matchNameId, String userId) {

		if (matchNameId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			MatchName matchName = matchNameRepository.findById(matchNameId).get();

			if (matchName == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such match name find at here...");

		}

		try {

			MatchName matchName = matchNameRepository.findById(matchNameId).get();

			if (matchName == null) {

				throw new Exception();

			}

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = matchNameRepository.count();

				cleaner.removeMatchName(matchNameId);

				return count != matchNameRepository.count();

			}

			EventOrganaizer eventOrganaizer = eventOrganaizerRepository.findByUserId(user.getId());

			if (eventOrganaizer == null) {

				throw new Exception();

			}

			Match match = matchRepository.findById(matchName.getMatchId()).get();

			if (match == null) {

				throw new ArithmeticException();

			}

			if (!match.getOrganaizerId().equals(eventOrganaizer.getId())) {

				throw new Exception();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("No such match exist at here...");

		} catch (Exception e) {

			throw new NoSuchElementException("No such valid user find at here...");

		}

		long count = matchNameRepository.count();

		cleaner.removeMatchName(matchNameId);

		return count != matchNameRepository.count();
	}

	@Override
	public List<MatchName> seeAll() {

		List<MatchName> list = matchNameRepository.findAll();

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such match name find at here...");

		}

		return list;
	}

}
