package com.example.demo700.Services.TeamLocationServices;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.Athlete.TeamOwner;
import com.example.demo700.Models.TeamLocationModels.TeamLocationModel;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.TeamOwnerRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.TeamLocationRepositories.TeamLocationRepository;
import com.example.demo700.Validator.AddressValidator;

@Service
public class TeamLocationServiceImpl implements TeamLocationService {

	@Autowired
	private TeamLocationRepository teamLocationRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private TeamOwnerRepository teamOwnerRepository;

	@Autowired
	private AtheleteRepository athleteRepository;

	@Autowired
	private UserRepository userRepository;
	
	private AddressValidator adressValidator = new AddressValidator();

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public TeamLocationModel addTeamLocation(TeamLocationModel teamLocation, String userId) {

		if (userId == null || teamLocation == null) {

			throw new NullPointerException("False request...");

		}

		try {

			TeamLocationModel teamLocationModel = teamLocationRepository.findByTeamId(teamLocation.getTeamId());

			if (teamLocationModel != null) {

				throw new Exception();

			}

		} catch (ArithmeticException e) {

			throw new NoSuchElementException("Team location is already added for this team...");

		} catch (Exception e) {

		}
		
		try {
			
			if(!adressValidator.isValidAddress(teamLocation.getLocationName())) {
				
				throw new Exception();
				
			}
			
		} catch(Exception e) {
			
			throw new ArithmeticException("Team location is not valid...");
			
		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			Team team = teamRepository.findById(teamLocation.getTeamId()).get();

			if (team == null) {

				throw new Exception();

			}

			TeamOwner teamOwner = teamOwnerRepository.findByTeamsContainingIgnoreCase(team.getId());

			if (teamOwner == null) {

				throw new ArithmeticException();

			}

			Athelete athlete = athleteRepository.findByUserId(userId).get();

			if (athlete == null) {

				throw new Exception();

			}

			if (!teamOwner.getAtheleteId().equals(athlete.getId())) {

				throw new Exception();

			}

		} catch (ArithmeticException e) {

			throw new NoSuchElementException("No such team exist at here...");

		} catch (Exception e) {

			throw new NoSuchElementException("No such valid user find at here....");

		}

		teamLocation = teamLocationRepository.save(teamLocation);

		if (teamLocation == null) {

			throw new ArithmeticException("Team location not added....");

		}

		return teamLocation;
	}

	@Override
	public TeamLocationModel upadteTeamLocation(TeamLocationModel teamLocation, String userId, String teamLocationId) {

		if (userId == null || teamLocation == null || teamLocationId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			TeamLocationModel teamLocationModel = teamLocationRepository.findById(teamLocationId).get();

			if (teamLocationModel == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such team exist at here...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			Team team = teamRepository.findById(teamLocation.getTeamId()).get();

			if (team == null) {

				throw new Exception();

			}

			TeamOwner teamOwner = teamOwnerRepository.findByTeamsContainingIgnoreCase(team.getId());

			if (teamOwner == null) {

				throw new ArithmeticException();

			}

			Athelete athlete = athleteRepository.findByUserId(userId).get();

			if (athlete == null) {

				throw new Exception();

			}

			if (!teamOwner.getAtheleteId().equals(athlete.getId())) {

				throw new Exception();

			}

		} catch (ArithmeticException e) {

			throw new NoSuchElementException("No such team exist at here...");

		} catch (Exception e) {

			throw new NoSuchElementException("No such valid user find at here....");

		}

		teamLocation.setId(teamLocationId);

		teamLocation = teamLocationRepository.save(teamLocation);

		if (teamLocation == null) {

			throw new ArithmeticException("Team location not added....");

		}

		return teamLocation;
	}

	@Override
	public List<TeamLocationModel> seeAllTeamLocation() {

		List<TeamLocationModel> list = teamLocationRepository.findAll();

		if (list.isEmpty()) {

			throw new ArithmeticException("No such team location add at here...");

		}

		return list;
	}

	@Override
	public TeamLocationModel getByTeamLocationId(String id) {

		if (id == null) {

			throw new NullPointerException("False request...");

		}

		try {

			return teamLocationRepository.findById(id).get();

		} catch (Exception e) {

			throw new NoSuchElementException("No such team loation find at here...");

		}

	}

	@Override
	public TeamLocationModel findByTeamId(String teamId) {

		if (teamId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			return teamLocationRepository.findByTeamId(teamId);

		} catch (Exception e) {

			throw new NoSuchElementException("No such team location find at here...");

		}

	}

	@Override
	public List<TeamLocationModel> findByLocationNameContainingIgnoreCase(String locationName) {

		if (locationName == null) {

			throw new NullPointerException("False request...");

		}

		List<TeamLocationModel> list = teamLocationRepository.findByLocationNameContainingIgnoreCase(locationName);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such team location find at here...");

		}

		return list;
	}

	@Override
	public List<TeamLocationModel> findByLatitudeAndLongitude(double latitude, double longitude) {

		List<TeamLocationModel> list = teamLocationRepository.findByLatitudeAndLongitude(latitude, longitude);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such team location find at here...");

		}

		return list;
	}

	@Override
	public boolean removeTeamLocation(String userId, String teamLocationId) {

		if (userId == null || teamLocationId == null) {

			throw new NullPointerException("false request...");

		}

		try {

			TeamLocationModel teamLocationModel = teamLocationRepository.findById(teamLocationId).get();

			if (teamLocationModel == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such team exist at here...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = teamLocationRepository.count();

				cleaner.removeTeamLocation(teamLocationId);

				return teamLocationRepository.count() != count;

			}

			TeamLocationModel teamLocation = teamLocationRepository.findById(teamLocationId).get();

			if (teamLocation == null) {

				throw new Exception();

			}

			Team team = teamRepository.findById(teamLocation.getTeamId()).get();

			if (team == null) {

				throw new Exception();

			}

			TeamOwner teamOwner = teamOwnerRepository.findByTeamsContainingIgnoreCase(team.getId());

			if (teamOwner == null) {

				throw new ArithmeticException();

			}

			Athelete athlete = athleteRepository.findByUserId(userId).get();

			if (athlete == null) {

				throw new Exception();

			}

			if (!teamOwner.getAtheleteId().equals(athlete.getId())) {

				throw new Exception();

			}

		} catch (ArithmeticException e) {

			throw new NoSuchElementException("No such team exist at here...");

		} catch (Exception e) {

			throw new NoSuchElementException("No such valid user find at here....");

		}

		long count = teamLocationRepository.count();

		cleaner.removeTeamLocation(teamLocationId);

		return teamLocationRepository.count() != count;
	}

}
