package com.example.demo700.Controllers.PaymentController;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo700.Models.PaymentGateway.BkashTransaction;
import com.example.demo700.Services.PaymentService.BkashService;

@RestController
@RequestMapping("/api/bkash")
public class BkashController {

    @Autowired
    private BkashService bkashService;

    /**
     * ✅ Send Money (Create Transaction)
     * 
     * @param bkashTransaction - transaction details
     * @return created BkashTransaction
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendMoney(@RequestBody BkashTransaction bkashTransaction) {
        try {
            BkashTransaction transaction = bkashService.sendMoney(bkashTransaction);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (NullPointerException | NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected error occurred while sending money", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * ✅ Delete / Remove Payment Record
     * 
     * @param bkashTransactionId - transaction id
     * @param userId - id of the receiver (owner)
     */
    @DeleteMapping("/delete/{bkashTransactionId}/{userId}")
    public ResponseEntity<?> removePayment(
            @PathVariable String bkashTransactionId,
            @PathVariable String userId) {
        try {
            boolean deleted = bkashService.removePayment(bkashTransactionId, userId);
            if (deleted) {
                return new ResponseEntity<>("Bkash Transaction removed successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No transaction deleted", HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException | NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected error occurred while deleting payment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * ✅ Get all Bkash Transactions
     */
    @GetMapping("/all")
    public ResponseEntity<?> seeAllTransactions() {
        List<BkashTransaction> transactions = bkashService.seeAll();
        if (transactions.isEmpty()) {
            return new ResponseEntity<>("No transactions found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * ✅ Get all transactions by Sender ID
     */
    @GetMapping("/sender/{senderId}")
    public ResponseEntity<?> findBySenderId(@PathVariable String senderId) {
        try {
            List<BkashTransaction> transactions = bkashService.findBySenderId(senderId);
            if (transactions.isEmpty()) {
                return new ResponseEntity<>("No transactions found for this sender", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * ✅ Get all transactions by Receiver ID
     */
    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<?> findByReceiverId(@PathVariable String receiverId) {
        try {
            List<BkashTransaction> transactions = bkashService.findByReceiverId(receiverId);
            if (transactions.isEmpty()) {
                return new ResponseEntity<>("No transactions found for this receiver", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * ✅ Get all transactions by Booking ID
     */
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<?> findByBookingId(@PathVariable String bookingId) {
        try {
            List<BkashTransaction> transactions = bkashService.findByBookingId(bookingId);
            if (transactions.isEmpty()) {
                return new ResponseEntity<>("No transactions found for this booking", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * ✅ Get single transaction by Transaction ID
     */
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<?> findByTransactionId(@PathVariable String transactionId) {
        try {
            BkashTransaction transaction = bkashService.findByTransactionId(transactionId);
            if (transaction == null) {
                return new ResponseEntity<>("No transaction found with this ID", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * ✅ Find all transactions with amount less than a given value
     */
    @GetMapping("/amount/less/{amount}")
    public ResponseEntity<?> findByAmountLessThan(@PathVariable double amount) {
        List<BkashTransaction> transactions = bkashService.findByAmountLessThan(amount);
        if (transactions.isEmpty()) {
            return new ResponseEntity<>("No transactions found below this amount", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

}
