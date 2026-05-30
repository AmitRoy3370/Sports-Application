package com.example.demo700.Services.Athlete;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.Athlete.TeamFiles;
import com.example.demo700.Models.Athlete.TeamOwner;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.TeamFilesRepository;
import com.example.demo700.Repositories.Athelete.TeamOwnerRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Services.FileUploadServices.ImageService;

@Service
public class TeamFilesServiceImpl implements TeamFilesService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AtheleteRepository athleteRepository;

	@Autowired
	private TeamOwnerRepository teamOwnerRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private TeamFilesRepository teamFilesRepository;

	@Autowired
	private ImageService imageService;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public TeamFiles addTeamFiles(MultipartFile[] files, String userId, String teamId) {

		if (userId == null || teamId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			TeamFiles teamFiles = teamFilesRepository.findByTeamId(teamId);

			if (teamFiles != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This teams files are already uploaded...");

		} catch (Exception e) {

		}

		Team team = null;

		try {

			team = teamRepository.findById(teamId).get();

			if (team == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such teams find at here....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			Athelete athlete = athleteRepository.findByUserId(user.getId()).get();

			if (athlete == null) {

				throw new Exception();

			}

			TeamOwner teamOwner = teamOwnerRepository.findByAtheleteId(athlete.getId());

			if (teamOwner == null) {

				throw new Exception();

			}

			if (!team.getTeamOwnerId().equals(teamOwner.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here to add files in team section....");

		}

		List<String> fileIds = new ArrayList<>();

		try {

			for (MultipartFile i : files) {

				try {

					String id = imageService.upload(i);

					fileIds.add(id);

				} catch (Exception e) {

				}

			}

		} catch (Exception e) {

		}

		TeamFiles teamFiles = new TeamFiles();

		teamFiles.setTeamId(teamId);
		teamFiles.setFiles(fileIds);

		teamFiles = teamFilesRepository.save(teamFiles);

		if (teamFiles == null) {

			throw new ArithmeticException("Team files are not uploaded...");

		}

		return teamFiles;

	}

	@Override
	public TeamFiles updateTeamFiles(MultipartFile[] files, String userId, String teamFilesId,
			List<String> existingFiles, String teamId) {

		if (userId == null || teamId == null || teamFilesId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			TeamFiles teamFiles = teamFilesRepository.findById(teamFilesId).get();

			if (teamFiles == null) {

				throw new Exception();

			}

			for (String i : teamFiles.getFiles()) {

				try {

					if (!existingFiles.contains(i)) {

						imageService.delete(i);

					}

				} catch (Exception e) {

				}

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such team files find at here...");

		}

		try {

			TeamFiles teamFiles = teamFilesRepository.findByTeamId(teamId);

			if (teamFiles != null) {

				if (!teamFiles.getId().equals(teamFilesId)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This teams files are already uploaded...");

		} catch (Exception e) {

		}

		Team team = null;

		try {

			team = teamRepository.findById(teamId).get();

			if (team == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such teams find at here....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			Athelete athlete = athleteRepository.findByUserId(user.getId()).get();

			if (athlete == null) {

				throw new Exception();

			}

			TeamOwner teamOwner = teamOwnerRepository.findByAtheleteId(athlete.getId());

			if (teamOwner == null) {

				throw new Exception();

			}

			if (!team.getTeamOwnerId().equals(teamOwner.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here to add files in team section....");

		}

		List<String> fileIds = new ArrayList<>();

		try {

			for (String i : existingFiles) {

				try {

					if (imageService.attachmentExists(i)) {

						fileIds.add(i);

					}

				} catch (Exception e) {

				}

			}

		} catch (Exception e) {

		}

		try {

			for (MultipartFile i : files) {

				try {

					String id = imageService.upload(i);

					fileIds.add(id);

				} catch (Exception e) {

				}

			}

		} catch (Exception e) {

		}

		TeamFiles teamFiles = new TeamFiles();

		teamFiles.setId(teamFilesId);
		teamFiles.setTeamId(teamId);
		teamFiles.setFiles(fileIds);

		teamFiles = teamFilesRepository.save(teamFiles);

		if (teamFiles == null) {

			throw new ArithmeticException("Team files are not uploaded...");

		}

		return teamFiles;

	}

	@Override
	public List<TeamFiles> seeAll() {

		try {

			List<TeamFiles> list = teamFilesRepository.findAll();

			if (list == null || list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such team files find...");

		}

	}

	@Override
	public TeamFiles findByTeamId(String teamId) {

		if (teamId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			TeamFiles teamFiles = teamFilesRepository.findByTeamId(teamId);

			if (teamFiles == null) {

				throw new Exception();

			}

			return teamFiles;

		} catch (Exception e) {

			throw new NoSuchElementException("No such team files find...");

		}

	}

	@Override
	public List<TeamFiles> findByFilesContainingIgnoreCase(String fileId) {

		if (fileId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<TeamFiles> list = teamFilesRepository.findByFilesContainingIgnoreCase(fileId);

			if (list == null || list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such team files find...");

		}

	}

	@Override
	public boolean removeTeamFiles(String id, String userId) {

		if (id == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		Team team = null;

		try {

			TeamFiles teamFiles = teamFilesRepository.findById(id).get();

			if (teamFiles == null) {

				throw new Exception();

			}

			team = teamRepository.findById(teamFiles.getTeamId()).get();

			if (team == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such team files find at here...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = teamFilesRepository.count();

				cleaner.removeTeamFiles(id);

				return count != teamFilesRepository.count();

			}

			Athelete athlete = athleteRepository.findByUserId(user.getId()).get();

			if (athlete == null) {

				throw new Exception();

			}

			TeamOwner teamOwner = teamOwnerRepository.findByAtheleteId(athlete.getId());

			if (teamOwner == null) {

				throw new Exception();

			}

			if (!team.getTeamOwnerId().equals(teamOwner.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here to add files in team section....");

		}

		long count = teamFilesRepository.count();

		cleaner.removeTeamFiles(id);

		return count != teamFilesRepository.count();

	}

}
