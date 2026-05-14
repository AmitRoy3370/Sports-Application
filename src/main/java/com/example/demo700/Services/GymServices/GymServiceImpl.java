package com.example.demo700.Services.GymServices;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.DTOFiles.GymMemberResponse;
import com.example.demo700.DTOFiles.GymResponse;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.GymModels.Gyms;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.GymRepositories.GymsRepository;
import com.example.demo700.Services.FileUploadServices.ImageService;

@Service
public class GymServiceImpl implements GymService {

	@Autowired
	private GymsRepository gymsRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GymMemberService gymMemberService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public Gyms addGyms(Gyms gyms, String userId, MultipartFile files[], MultipartFile coverImage) {

		if (gyms == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		if (gyms.getMonthlyFee() <= 0.0 || gyms.getEntryFee() <= 0.0) {

			throw new ArithmeticException("Price is in valid...");

		}

		if (gyms.getLongtitude() <= 0.0 || gyms.getLatitude() <= 0.0) {

			throw new ArithmeticException("Location axis is in valid....");

		}

		if (gyms.getLocationName() == null) {

			throw new NullPointerException("Gyms must have a location name....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getRoles().contains(Role.ROLE_GYM_OWNER)) {

				throw new Exception();

			}

			if (!user.getId().equals(gyms.getGymOwner())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Only gym owner can add the gyms....");

		}

		try {

			User user = userRepository.findById(gyms.getGymTrainer()).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getRoles().contains(Role.ROLE_GYM_TRAINER)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such valid user find at here.....");

		}

		try {

			if (gyms.getGymName() == null) {

				throw new Exception();

			}

			Gyms _gyms = gymsRepository.findByGymNameIgnoreCase(gyms.getGymName().trim());

			if (_gyms != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This gym name already exist at here....");

		} catch (Exception e) {

		}

		try {

			if (gyms.getOpeningTime() != null && gyms.getClosingTime() != null) {

				if (gyms.getOpeningTime().isAfter(gyms.getClosingTime())) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Opening and closing time schedule is not valid....");

		} catch (Exception e) {

		}

		try {

			if (gyms.getTradeLicenseId() != null) {

				Gyms oldGyms = gymsRepository.findByTradeLicenseId(gyms.getTradeLicenseId());

				if (oldGyms != null) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Every trade license should be unique....");

		} catch (Exception e) {

		}

		try {

			if (gyms.getTinNumber() != null) {

				Gyms oldGyms = gymsRepository.findByTinNumber(gyms.getTinNumber());

				if (oldGyms != null) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Every trade license should be unique....");

		} catch (Exception e) {

		}

		try {

			if (coverImage != null && !coverImage.isEmpty()) {

				if (coverImage.getContentType().startsWith("image/")) {

					String hexId = imageService.upload(coverImage);

					if (hexId != null) {

						gyms.setCoverImageId(hexId);

					} else {

						throw new ArithmeticException();

					}

				} else {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("No such cover image upload at here...");

		} catch (Exception e) {

		}

		try {

			List<String> allAttachment = new ArrayList<>();

			for (MultipartFile i : files) {

				if (!i.isEmpty()) {

					String hexId = imageService.upload(i);

					if (hexId != null) {

						allAttachment.add(hexId);

					}

				}

			}

			if (!allAttachment.isEmpty()) {

				gyms.setGymImages(allAttachment);

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Files are not uploaded....");

		} catch (Exception e) {

		}

		gyms = gymsRepository.save(gyms);

		if (gyms == null) {

			throw new ArithmeticException("value is not added....");

		}

		return gyms;
	}

	@Override
	public Gyms updateGyms(Gyms gyms, String userId, String gymId, MultipartFile files[], MultipartFile coverImage) {

		if (gyms == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Gyms oldGyms = gymsRepository.findById(gymId).get();

			if (oldGyms == null) {

				throw new Exception();

			}

			if (!oldGyms.getGymOwner().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such gym find at here...");

		}

		if (gyms.getMonthlyFee() <= 0.0 || gyms.getEntryFee() <= 0.0) {

			throw new ArithmeticException("Price is in valid...");

		}

		if (gyms.getLongtitude() <= 0.0 || gyms.getLatitude() <= 0.0) {

			throw new ArithmeticException("Location axis is in valid....");

		}

		if (gyms.getLocationName() == null) {

			throw new NullPointerException("Gyms must have a location name....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getRoles().contains(Role.ROLE_GYM_OWNER)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new ArithmeticException("Only gym owner can add the gyms....");

		}

		try {

			User user = userRepository.findById(gyms.getGymTrainer()).get();

			if (user == null) {

				throw new Exception();

			}

			if (!user.getRoles().contains(Role.ROLE_GYM_TRAINER)) {

				throw new Exception();

			}

			if (!user.getId().equals(gyms.getGymTrainer())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such valid user find at here.....");

		}

		try {

			if (gyms.getGymName() == null) {

				throw new Exception();

			}

			Gyms _gyms = gymsRepository.findByGymNameIgnoreCase(gyms.getGymName().trim());

			if (_gyms != null) {

				if (!_gyms.getId().equals(gymId)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("This gym name already exist at here....");

		} catch (Exception e) {

		}

		try {

			if (gyms.getOpeningTime() != null && gyms.getClosingTime() != null) {

				if (gyms.getOpeningTime().isAfter(gyms.getClosingTime())) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Opening and closing time schedule is not valid....");

		} catch (Exception e) {

		}

		try {

			if (gyms.getTradeLicenseId() != null) {

				Gyms oldGyms = gymsRepository.findByTradeLicenseId(gyms.getTradeLicenseId());

				if (oldGyms != null) {

					if (!oldGyms.getId().equals(gymId)) {

						throw new ArithmeticException();

					}

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Every trade license should be unique....");

		} catch (Exception e) {

		}

		try {

			if (gyms.getTinNumber() != null) {

				Gyms oldGyms = gymsRepository.findByTinNumber(gyms.getTinNumber());

				if (oldGyms != null) {

					if (!oldGyms.getId().equals(gymId)) {

						throw new ArithmeticException();

					}

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Every trade license should be unique....");

		} catch (Exception e) {

		}

		try {

			if (coverImage != null && !coverImage.isEmpty()) {

				if (coverImage.getContentType().startsWith("image/")) {

					String hexId = imageService.upload(coverImage);

					if (hexId != null) {

						gyms.setCoverImageId(hexId);

					} else {

						throw new ArithmeticException();

					}

				} else {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("No such cover image upload at here...");

		} catch (Exception e) {

		}

		try {

			List<String> allAttachment = new ArrayList<>();

			for (String i : gyms.getGymImages()) {

				try {

					if (imageService.attachmentExists(i)) {

						allAttachment.add(i);

					}

				} catch (Exception e) {

				}

			}

			Gyms oldGyms = gymsRepository.findById(gymId).get();

			for (String i : oldGyms.getGymImages()) {

				try {

					if (!allAttachment.contains(i)) {

						imageService.delete(i);

					}

				} catch (Exception e) {

				}

			}

			for (MultipartFile i : files) {

				if (!i.isEmpty()) {

					String hexId = imageService.upload(i);

					if (hexId != null) {

						allAttachment.add(hexId);

					}

				}

			}

			if (!allAttachment.isEmpty()) {

				gyms.setGymImages(allAttachment);

			}

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Files are not uploaded....");

		} catch (Exception e) {

		}

		gyms.setId(gymId);

		gyms = gymsRepository.save(gyms);

		if (gyms == null) {

			throw new ArithmeticException("value is not added....");

		}

		return gyms;
	}

	@Override
	public List<GymResponse> findByGymTrainer(String gymTrainer) {

		if (gymTrainer == null) {

			throw new NullPointerException("False request....");

		}

		List<Gyms> list = gymsRepository.findByGymTrainer(gymTrainer);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return getGymResponse(list);
	}

	@Override
	public GymResponse findByGymName(String gymName) {

		if (gymName == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Gyms gyms = gymsRepository.findByGymNameIgnoreCase(gymName.trim());

			if (gyms == null) {

				throw new Exception();

			}

			return getGymResponse(gyms);

		} catch (Exception e) {

			throw new NoSuchElementException("No such gym find at here...");

		}

	}

	@Override
	public List<GymResponse> findByLocationName(String locationName) {

		if (locationName == null) {

			throw new NullPointerException("False request...");

		}

		List<Gyms> list = gymsRepository.findByLocationNameContainingIgnoreCase(locationName.trim());

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return getGymResponse(list);
	}

	@Override
	public List<GymResponse> findByLatitudeAndLongtitude(double latitude, double longtitude) {

		List<Gyms> list = gymsRepository.findByLatitudeAndLongtitude(latitude, longtitude);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return getGymResponse(list);
	}

	@Override
	public List<GymResponse> findByEntryFeeLessThanOrEqual(double entryFee) {

		List<Gyms> list = gymsRepository.findByEntryFeeLessThanEqual(entryFee);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return getGymResponse(list);
	}

	@Override
	public List<GymResponse> findByMonthlyFeeLessThanOrEqual(double monthlyFee) {

		List<Gyms> list = gymsRepository.findByMonthlyFeeLessThanEqual(monthlyFee);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return getGymResponse(list);
	}

	@Override
	public List<GymResponse> seeAllGyms() {

		List<Gyms> list = gymsRepository.findAll();

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return getGymResponse(list);
	}

	@Override
	public GymResponse findById(String gymId) {

		if (gymId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Gyms gyms = gymsRepository.findById(gymId).get();

			if (gyms == null) {

				throw new Exception();

			}

			return getGymResponse(gyms);

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

	}

	@Override
	public boolean deleteGyms(String gymId, String userId) {

		if (gymId == null || userId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = gymsRepository.count();

				cleaner.removeGym(gymId);

				return count != gymsRepository.count();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here...");

		}

		try {

			Gyms _gyms = gymsRepository.findById(gymId).get();

			if (_gyms == null) {

				throw new Exception();

			}

			if (!_gyms.getGymOwner().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

		long count = gymsRepository.count();

		cleaner.removeGym(gymId);

		return count != gymsRepository.count();
	}

	@Override
	public GymResponse findByTradeLicenseId(String tradeLicenseId) {

		if (tradeLicenseId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Gyms gyms = gymsRepository.findByTradeLicenseId(tradeLicenseId);

			if (gyms == null) {

				throw new Exception();

			}

			return getGymResponse(gyms);

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

	}

	@Override
	public GymResponse findByTinNumber(String tinNumber) {

		if (tinNumber == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Gyms gyms = gymsRepository.findByTinNumber(tinNumber);

			if (gyms == null) {

				throw new Exception();

			}

			return getGymResponse(gyms);

		} catch (Exception e) {

			throw new NoSuchElementException("No such gym find at here...");

		}

	}

	@Override
	public List<GymResponse> findByOpeningTimeBefore(Instant openingTime) {

		if (openingTime == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Gyms> list = gymsRepository.findByOpeningTimeBefore(openingTime);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getGymResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

	}

	@Override
	public List<GymResponse> findByOpeningTimeAfter(Instant openingTime) {

		if (openingTime == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Gyms> list = gymsRepository.findByOpeningTimeAfter(openingTime);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getGymResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

	}

	@Override
	public List<GymResponse> findByClosingTimeBefore(Instant closingTime) {

		if (closingTime == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Gyms> list = gymsRepository.findByClosingTimeBefore(closingTime);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getGymResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

	}

	@Override
	public List<GymResponse> findByClosingTimeAfter(Instant closingTime) {

		if (closingTime == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Gyms> list = gymsRepository.findByClosingTimeAfter(closingTime);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getGymResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

	}

	@Override
	public List<GymResponse> findByGymOwner(String gymOwner) {

		if (gymOwner == null) {

			throw new NullPointerException("False request.....");

		}

		try {

			List<Gyms> gyms = gymsRepository.findByGymOwner(gymOwner);

			if (gyms.isEmpty()) {

				throw new Exception();

			}

			return getGymResponse(gyms);

		} catch (Exception e) {

			throw new NoSuchElementException("no such gyms find at here....");

		}

	}

	@Override
	public List<GymResponse> findByGymNameContainingIgnoreCase(String gymName) {

		if (gymName == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Gyms> list = gymsRepository.findByGymNameContainingIgnoreCase(gymName);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return getGymResponse(list);

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

	}

	private ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private GymResponse getGymResponse(Gyms gyms) {

		List<Gyms> list = new ArrayList<>();

		list.add(gyms);

		return getGymResponse(list).get(0);

	}

	private List<GymResponse> getGymResponse(List<Gyms> gyms) {

		List<GymResponse> responses = new ArrayList<>();

		CompletableFuture<Set<String>> allUserIdFuture = CompletableFuture.supplyAsync(() -> {

			Set<String> allUserId = new HashSet<>();

			gyms.stream().filter(Objects::nonNull).filter(gymInfo -> gymInfo.getGymOwner() != null)
					.map(Gyms::getGymOwner).distinct().forEach(allUserId::add);

			gyms.stream().filter(Objects::nonNull).filter(gymInfo -> gymInfo.getGymTrainer() != null)
					.map(Gyms::getGymTrainer).distinct().forEach(allUserId::add);

			return allUserId;

		}, executors);

		CompletableFuture<Map<String, User>> nameFuture = allUserIdFuture.thenApplyAsync(ownerIds -> {

			if (ownerIds.isEmpty())
				return new HashMap<>();
			List<User> owners = userRepository.findAllById(new ArrayList<>(ownerIds));
			return owners.stream().collect(Collectors.toMap(User::getId, Function.identity()));

		}, executors);

		CompletableFuture<List<String>> allGymIdFuture = CompletableFuture
				.supplyAsync(() -> gyms.stream().map(Gyms::getId).collect(Collectors.toList()), executors);

		CompletableFuture<Map<String, GymMemberResponse>> gymMemberFuture = allGymIdFuture.thenApplyAsync(allGymId -> {

			List<GymMemberResponse> list = gymMemberService.findByGymIdIn(allGymId);

			return list.stream().collect(Collectors.toMap(GymMemberResponse::getGymId, Function.identity()));

		}, executors);

		CompletableFuture.allOf(allUserIdFuture, nameFuture, allGymIdFuture, gymMemberFuture).join();

		Set<String> allUserId = allUserIdFuture.join();
		Map<String, User> nameMap = nameFuture.join();

		List<String> allGymId = allGymIdFuture.join();
		Map<String, GymMemberResponse> gymMemberMap = gymMemberFuture.join();

		for (Gyms gym : gyms) {

			try {

				GymResponse response = new GymResponse();

				response.setId(gym.getId());

				try {

					response.setGymImages(gym.getGymImages());

				} catch (Exception e) {

				}

				try {

					response.setCoverImageId(gym.getCoverImageId());

				} catch (Exception e) {

				}

				try {

					response.setClosingTime(gym.getClosingTime());

				} catch (Exception e) {

				}

				try {

					response.setOpeningTime(gym.getOpeningTime());

				} catch (Exception e) {

				}

				try {

					response.setEntryFee(gym.getEntryFee());

				} catch (Exception e) {

				}

				try {

					response.setGymName(gym.getGymName());

				} catch (Exception e) {

				}

				try {

					response.setGymTrainer(gym.getGymTrainer());

				} catch (Exception e) {

				}

				try {

					response.setGymOwner(gym.getGymOwner());

				} catch (Exception e) {

				}

				try {

					response.setMonthlyFee(gym.getMonthlyFee());

				} catch (Exception e) {

				}

				try {

					response.setTinNumber(gym.getTinNumber());

				} catch (Exception e) {

				}

				try {

					response.setLatitude(gym.getLatitude());

				} catch (Exception e) {

				}

				try {

					response.setLongtitude(gym.getLongtitude());

				} catch (Exception e) {

				}

				try {

					response.setLocationName(gym.getLocationName());

				} catch (Exception e) {

				}

				try {

					response.setTradeLicenseId(gym.getTradeLicenseId());

				} catch (Exception e) {

				}

				try {
					
					if (gym.getGymOwner() != null && nameMap.containsKey(gym.getGymOwner())) {
	                    response.setGymOwnerName(nameMap.get(gym.getGymOwner()).getName());
	                } else {
	                    response.setGymOwnerName("Unknown Owner");
	                }
					
				} catch (Exception e) {

				}

				try {

					if (gym.getGymTrainer() != null && nameMap.containsKey(gym.getGymTrainer())) {
	                    response.setGymTrainerName(nameMap.get(gym.getGymTrainer()).getName());
	                } else {
	                    response.setGymTrainerName("Unknown Trainer");
	                }
					
				} catch (Exception e) {

				}

				try {

					response.setGymMembers(gymMemberMap.get(gym.getId()));

				} catch (Exception e) {

				}

				responses.add(response);

			} catch (Exception e) {

			}

		}

		return responses;

	}

}
