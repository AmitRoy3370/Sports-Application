package com.example.demo700.Services.GymServices;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
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
	public List<Gyms> findByGymTrainer(String gymTrainer) {

		if (gymTrainer == null) {

			throw new NullPointerException("False request....");

		}

		List<Gyms> list = gymsRepository.findByGymTrainer(gymTrainer);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return list;
	}

	@Override
	public Gyms findByGymName(String gymName) {

		if (gymName == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Gyms gyms = gymsRepository.findByGymNameIgnoreCase(gymName.trim());

			if (gyms == null) {

				throw new Exception();

			}

			return gyms;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gym find at here...");

		}

	}

	@Override
	public List<Gyms> findByLocationName(String locationName) {

		if (locationName == null) {

			throw new NullPointerException("False request...");

		}

		List<Gyms> list = gymsRepository.findByLocationNameContainingIgnoreCase(locationName.trim());

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return list;
	}

	@Override
	public List<Gyms> findByLatitudeAndLongtitude(double latitude, double longtitude) {

		List<Gyms> list = gymsRepository.findByLatitudeAndLongtitude(latitude, longtitude);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return list;
	}

	@Override
	public List<Gyms> findByEntryFeeLessThanOrEqual(double entryFee) {

		List<Gyms> list = gymsRepository.findByEntryFeeLessThanEqual(entryFee);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return list;
	}

	@Override
	public List<Gyms> findByMonthlyFeeLessThanOrEqual(double monthlyFee) {

		List<Gyms> list = gymsRepository.findByMonthlyFeeLessThanEqual(monthlyFee);

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return list;
	}

	@Override
	public List<Gyms> seeAllGyms() {

		List<Gyms> list = gymsRepository.findAll();

		if (list.isEmpty()) {

			throw new NoSuchElementException("No such gyms find at here....");

		}

		return list;
	}

	@Override
	public Gyms findById(String gymId) {

		if (gymId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Gyms gyms = gymsRepository.findById(gymId).get();

			if (gyms == null) {

				throw new Exception();

			}

			return gyms;

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
	public Gyms findByTradeLicenseId(String tradeLicenseId) {

		if (tradeLicenseId == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Gyms gyms = gymsRepository.findByTradeLicenseId(tradeLicenseId);

			if (gyms == null) {

				throw new Exception();

			}

			return gyms;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

	}

	@Override
	public Gyms findByTinNumber(String tinNumber) {

		if (tinNumber == null) {

			throw new NullPointerException("False request....");

		}

		try {

			Gyms gyms = gymsRepository.findByTinNumber(tinNumber);

			if (gyms == null) {

				throw new Exception();

			}

			return gyms;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gym find at here...");

		}

	}

	@Override
	public List<Gyms> findByOpeningTimeBefore(Instant openingTime) {

		if (openingTime == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Gyms> list = gymsRepository.findByOpeningTimeBefore(openingTime);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

	}

	@Override
	public List<Gyms> findByOpeningTimeAfter(Instant openingTime) {

		if (openingTime == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Gyms> list = gymsRepository.findByOpeningTimeAfter(openingTime);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

	}

	@Override
	public List<Gyms> findByClosingTimeBefore(Instant closingTime) {

		if (closingTime == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Gyms> list = gymsRepository.findByClosingTimeBefore(closingTime);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

	}

	@Override
	public List<Gyms> findByClosingTimeAfter(Instant closingTime) {

		if (closingTime == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Gyms> list = gymsRepository.findByClosingTimeAfter(closingTime);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

	}

	@Override
	public List<Gyms> findByGymOwner(String gymOwner) {

		if (gymOwner == null) {

			throw new NullPointerException("False request.....");

		}

		try {

			List<Gyms> gyms = gymsRepository.findByGymOwner(gymOwner);

			if (gyms.isEmpty()) {

				throw new Exception();

			}

			return gyms;

		} catch (Exception e) {

			throw new NoSuchElementException("no such gyms find at here....");

		}

	}

	@Override
	public List<Gyms> findByGymNameContainingIgnoreCase(String gymName) {

		if (gymName == null) {

			throw new NullPointerException("False request....");

		}

		try {

			List<Gyms> list = gymsRepository.findByGymNameContainingIgnoreCase(gymName);

			if (list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such gyms find at here...");

		}

	}

}
