package com.example.demo700.Services.PaymentService;

import java.util.List;

import com.example.demo700.Models.PaymentGateway.BkashTransaction;

public interface BkashService {
	
	public BkashTransaction sendMoney(BkashTransaction bkashTransaction);
	public boolean removePayment(String bkashTransactionId, String userId);
	public List<BkashTransaction> seeAll();
	List<BkashTransaction> findBySenderId(String senderId);
    List<BkashTransaction> findByReceiverId(String receiverId);
    List<BkashTransaction> findByBookingId(String bookingId);
    BkashTransaction findByTransactionId(String transactionId);
    List<BkashTransaction> findByAmountLessThan(double amount);

}
