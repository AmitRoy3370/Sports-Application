package com.example.demo700.Repositories.PaymentRepositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo700.Models.PaymentGateway.BkashTransaction;

@Repository
public interface BkashTransactionRepository extends MongoRepository<BkashTransaction, String> {
	
	List<BkashTransaction> findBySenderId(String senderId);
    List<BkashTransaction> findByReceiverId(String receiverId);
    List<BkashTransaction> findByBookingId(String bookingId);
    BkashTransaction findByTransactionId(String transactionId);
    List<BkashTransaction> findByAmountLessThan(double amount);

}
