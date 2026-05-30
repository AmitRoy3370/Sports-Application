package com.example.demo700.Services.TurfServices;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.ENUMS.Role;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Turf.Venue;
import com.example.demo700.Models.Turf.VenueFilesRepository;
import com.example.demo700.Models.Turf.VenueImages;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Turf.VenueRepository;
import com.example.demo700.Services.FileUploadServices.ImageService;

@Service
public class VenueImagesServiceImpl implements VenueImagesService {

	@Autowired
	private VenueFilesRepository venueFilesRepository;

	@Autowired
	private VenueRepository venueRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ImageService imageService;

	@Autowired
	private CyclicCleaner cleaner;

	@Override
	public VenueImages addVenueImages(MultipartFile[] file, String venueId, String userId) {

		if (venueId == null || userId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		try {

			Venue venue = venueRepository.findById(venueId).get();

			if (venue == null) {

				throw new Exception();

			}

			if (!venue.getOwnerId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such venue find at here...");

		}

		try {

			VenueImages images = venueFilesRepository.findByVenueId(venueId);

			if (images != null) {

				throw new ArithmeticException();

			}

		} catch (ArithmeticException e) {

			throw new NoSuchElementException("This venues files are already setted...");

		} catch (Exception e) {

		}

		List<String> fileIds = new ArrayList<>();

		try {

			for (MultipartFile i : file) {

				try {

					String id = imageService.upload(i);

					if (id != null) {

						fileIds.add(id);

					}

				} catch (Exception e) {

				}

			}

		} catch (Exception e) {

		}

		VenueImages venueImages = new VenueImages();

		venueImages.setVenueFiles(fileIds);

		venueImages.setVenueId(venueId);

		venueImages = venueFilesRepository.save(venueImages);

		if (venueImages == null) {

			throw new ArithmeticException("Venue files are not added...");

		}

		return venueImages;
	}

	@Override
	public VenueImages updateVenueImages(MultipartFile[] file, String venueId, String userId, String venueImagesId,
			List<String> existingFiles) {

		if (venueId == null || userId == null || venueImagesId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			VenueImages images = venueFilesRepository.findById(venueImagesId).get();

			if (images == null) {

				throw new Exception();

			}

			for (String i : images.getVenueFiles()) {

				try {

					if (!existingFiles.contains(i)) {

						imageService.delete(i);

					}

				} catch (Exception e) {

				}

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such venue images exist at here...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		try {

			Venue venue = venueRepository.findById(venueId).get();

			if (venue == null) {

				throw new Exception();

			}

			if (!venue.getOwnerId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such venue find at here...");

		}

		try {

			VenueImages images = venueFilesRepository.findByVenueId(venueId);

			if (images != null) {

				if (!images.getId().equals(venueImagesId)) {

					throw new ArithmeticException();

				}

			}

		} catch (ArithmeticException e) {

			throw new NoSuchElementException("This venues files are already setted...");

		} catch (Exception e) {

		}

		List<String> fileIds = new ArrayList<>();

		try {

			try {

				for (String i : existingFiles) {

					try {

						if (imageService.attachmentExists(i.trim())) {

							fileIds.add(i.trim());

						}

					} catch (Exception e) {

					}

				}

			} catch (Exception e) {

			}

			for (MultipartFile i : file) {

				try {

					String id = imageService.upload(i);

					if (id != null) {

						fileIds.add(id);

					}

				} catch (Exception e) {

				}

			}

		} catch (Exception e) {

		}

		VenueImages venueImages = new VenueImages();

		venueImages.setId(venueImagesId);

		venueImages.setVenueFiles(fileIds);

		venueImages.setVenueId(venueId);

		venueImages = venueFilesRepository.save(venueImages);

		if (venueImages == null) {

			throw new ArithmeticException("Venue files are not added...");

		}

		return venueImages;
	}

	@Override
	public List<VenueImages> seeAll() {

		try {

			List<VenueImages> list = venueFilesRepository.findAll();

			if (list == null || list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such venue files find at here..");

		}

	}

	@Override
	public VenueImages findByVenueId(String venueId) {

		if (venueId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			VenueImages venueImages = venueFilesRepository.findByVenueId(venueId);

			if (venueImages == null) {

				throw new Exception();

			}

			return venueImages;

		} catch (Exception e) {

			throw new NoSuchElementException("No such venue files find at here...");

		}

	}

	@Override
	public List<VenueImages> findByVenueFilesContainingIgnoreCase(String fileId) {

		if (fileId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			List<VenueImages> list = venueFilesRepository.findByVenueFilesContainingIgnoreCase(fileId);

			if (list == null || list.isEmpty()) {

				throw new Exception();

			}

			return list;

		} catch (Exception e) {

			throw new NoSuchElementException("No such venue files find at here...");

		}

	}

	@Override
	public boolean deleteVenueImages(String userId, String venueImagesId) {

		if (userId == null || venueImagesId == null) {

			throw new NullPointerException("False request...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			if (user.getRoles().contains(Role.ROLE_ADMIN)) {

				long count = venueFilesRepository.count();

				cleaner.removeVenueImages(venueImagesId);

				return count != venueFilesRepository.count();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user exist at here...");

		}

		try {

			VenueImages images = venueFilesRepository.findById(venueImagesId).get();

			if (images == null) {

				throw new Exception();

			}

			Venue _images = venueRepository.findById(images.getVenueId()).get();

			if (_images != null) {

				if (!_images.getOwnerId().equals(userId)) {

					throw new Exception();

				}

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such venue files find at here...");

		}

		long count = venueFilesRepository.count();

		cleaner.removeVenueImages(venueImagesId);

		return count != venueFilesRepository.count();
	}

}
