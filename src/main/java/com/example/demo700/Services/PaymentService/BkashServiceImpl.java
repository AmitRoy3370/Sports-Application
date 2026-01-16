package com.example.demo700.Services.PaymentService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.CyclicCleaner.CyclicCleaner;
import com.example.demo700.Models.User;
import com.example.demo700.Models.PaymentGateway.BkashTransaction;
import com.example.demo700.Models.Turf.Booking;
import com.example.demo700.Models.Turf.Venue;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.PaymentRepositories.BkashTransactionRepository;
import com.example.demo700.Repositories.Turf.BookingRepository;
import com.example.demo700.Repositories.Turf.VenueRepository;

@Service
public class BkashServiceImpl implements BkashService {

	@Autowired
	private BkashTransactionRepository bikashRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private VenueRepository venueRepository;

	@Autowired
	private CyclicCleaner cleaner;

	@SuppressWarnings("null")
	@Override
	public BkashTransaction sendMoney(BkashTransaction bkashTransaction) {

		if (bkashTransaction == null) {

			throw new NullPointerException("False request...");

		}

		try {

			Booking booking = bookingRepository.findById(bkashTransaction.getBookingId()).get();

			if (booking == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such valid booking is present...");

		}

		try {

			User user = userRepository.findById(bkashTransaction.getSenderId()).get();

			if (user == null) {

				throw new Exception();

			}

			List<Booking> booking = bookingRepository.findByUserId(bkashTransaction.getSenderId());

			if (booking.isEmpty()) {

				throw new Exception();

			}

			List<String> ownersId = new ArrayList<>();

			for (Booking i : booking) {

				ownersId.add(i.getUserId());

			}

			if (!ownersId.contains(user.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No valid sender user find at here...");

		}

		try {

			User user = userRepository.findById(bkashTransaction.getReceiverId()).get();

			if (user == null) {

				throw new Exception();

			}

			Booking booking = bookingRepository.findById(bkashTransaction.getBookingId()).get();

			if (booking == null) {

				throw new Exception();

			}

			Venue venue = venueRepository.findById(booking.getVenueId()).get();

			if (venue == null) {

				throw new Exception();

			}

			if (!venue.getOwnerId().equals(bkashTransaction.getReceiverId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No valid receiver user find at here...");

		}

		try {

			BkashTransaction _bikashTransaction = bikashRepository
					.findByTransactionId(bkashTransaction.getTransactionId());

			if (_bikashTransaction != null) {

				throw new ArithmeticException("Two payment's transaction id can't be the same...");

			}

			System.out.println(_bikashTransaction.toString());

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Two payment's transaction id can't be the same...");

		} catch (Exception e) {

			System.out.println(e);

		}

		bkashTransaction = bikashRepository.save(bkashTransaction);

		if (bkashTransaction == null) {

			return null;

		}

		return bkashTransaction;
	}

	@Override
	public boolean removePayment(String bkashTransactionId, String userId) {

		if (bkashTransactionId == null || userId == null) {

			throw new NullPointerException("False requst...");

		}

		try {

			BkashTransaction bkashTransaction = bikashRepository.findById(bkashTransactionId).get();

			if (bkashTransaction == null) {

				throw new Exception();

			}

			User user = userRepository.findById(bkashTransaction.getReceiverId()).get();

			if (user == null) {

				throw new Exception();

			}

			Booking booking = bookingRepository.findById(bkashTransaction.getBookingId()).get();

			if (booking == null) {

				throw new Exception();

			}

			Venue venue = venueRepository.findById(booking.getVenueId()).get();

			if (venue == null) {

				throw new Exception();

			}

			if (!venue.getOwnerId().equals(bkashTransaction.getReceiverId())) {

				throw new Exception();

			}

			if (!venue.getOwnerId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No valid receiver user find at here...");

		}

		long count = bikashRepository.count();

		cleaner.removeBikashTransaction(bkashTransactionId);

		return count != bikashRepository.count();
	}

	@Override
	public List<BkashTransaction> seeAll() {

		return bikashRepository.findAll();
	}

	@Override
	public List<BkashTransaction> findBySenderId(String senderId) {

		if (senderId == null) {

			throw new NullPointerException("False request...");

		}

		return bikashRepository.findBySenderId(senderId);
	}

	@Override
	public List<BkashTransaction> findByReceiverId(String receiverId) {

		if (receiverId == null) {

			throw new NullPointerException("False request...");

		}

		return bikashRepository.findByReceiverId(receiverId);
	}

	@Override
	public List<BkashTransaction> findByBookingId(String bookingId) {

		if (bookingId == null) {

			throw new NullPointerException("False request...");

		}

		return bikashRepository.findByBookingId(bookingId);
	}

	@Override
	public BkashTransaction findByTransactionId(String transactionId) {

		if (transactionId == null) {

			throw new NullPointerException("False request...");

		}

		return bikashRepository.findByTransactionId(transactionId);
	}

	@Override
	public List<BkashTransaction> findByAmountLessThan(double amount) {

		return bikashRepository.findByAmountLessThan(amount);
	}

	@Override
	public BkashTransaction approveOrDenyPayment(boolean approve, String userId, String paymentId) {

		if (userId == null || paymentId == null) {

			throw new NullPointerException("False request......");

		}

		String bookingId = null;

		try {

			BkashTransaction bkashTransaction = bikashRepository.findById(paymentId).get();

			if (bkashTransaction == null) {

				throw new Exception();

			}

			bookingId = bkashTransaction.getBookingId();

		} catch (Exception e) {

			throw new NoSuchElementException("No such payment find at here...");

		}

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			Booking booking = bookingRepository.findById(bookingId).get();

			if (booking == null) {

				throw new Exception();

			}

			Venue venue = venueRepository.findById(booking.getVenueId()).get();

			if (venue == null) {

				throw new Exception();

			}

			if (!venue.getOwnerId().equals(user.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such user find at here to approve or deny....");

		}

		BkashTransaction bkashTransaction = bikashRepository.findById(paymentId).get();

		bkashTransaction.setApprove(approve);

		bkashTransaction = bikashRepository.save(bkashTransaction);

		if (bkashTransaction == null) {

			throw new ArithmeticException("approval not count....");

		}

		return bkashTransaction;
	}

	@Override
	public BkashTransaction updateSendMoney(BkashTransaction bkashTransaction, String userId, String id) {

		if (bkashTransaction == null || userId == null || id == null) {

			throw new NullPointerException("False request...");

		}

		try {

			BkashTransaction transaction = bikashRepository.findById(id).get();

			if (transaction == null) {

				throw new Exception();

			}

			if (!transaction.getSenderId().equals(userId)) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such payment find at here...");

		}

		try {

			Booking booking = bookingRepository.findById(bkashTransaction.getBookingId()).get();

			if (booking == null) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No such valid booking is present...");

		}

		try {

			User user = userRepository.findById(bkashTransaction.getSenderId()).get();

			if (user == null) {

				throw new Exception();

			}

			List<Booking> booking = bookingRepository.findByUserId(bkashTransaction.getSenderId());

			if (booking.isEmpty()) {

				throw new Exception();

			}

			List<String> ownersId = new ArrayList<>();

			for (Booking i : booking) {

				ownersId.add(i.getUserId());

			}

			if (!ownersId.contains(user.getId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No valid sender user find at here...");

		}

		try {

			User user = userRepository.findById(bkashTransaction.getReceiverId()).get();

			if (user == null) {

				throw new Exception();

			}

			Booking booking = bookingRepository.findById(bkashTransaction.getBookingId()).get();

			if (booking == null) {

				throw new Exception();

			}

			Venue venue = venueRepository.findById(booking.getVenueId()).get();

			if (venue == null) {

				throw new Exception();

			}

			if (!venue.getOwnerId().equals(bkashTransaction.getReceiverId())) {

				throw new Exception();

			}

		} catch (Exception e) {

			throw new NoSuchElementException("No valid receiver user find at here...");

		}

		try {

			BkashTransaction _bikashTransaction = bikashRepository
					.findByTransactionId(bkashTransaction.getTransactionId());

			if (_bikashTransaction != null) {

				if (!_bikashTransaction.getId().equals(id)) {

					throw new ArithmeticException("Two payment's transaction id can't be the same...");

				}

			}

			System.out.println(_bikashTransaction.toString());

		} catch (ArithmeticException e) {

			throw new ArithmeticException("Two payment's transaction id can't be the same...");

		} catch (Exception e) {

			System.out.println(e);

		}

		bkashTransaction.setId(id);

		bkashTransaction = bikashRepository.save(bkashTransaction);

		if (bkashTransaction == null) {

			return null;

		}

		return bkashTransaction;
	}

}
