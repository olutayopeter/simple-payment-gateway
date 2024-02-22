package com.payment.simplepaymentgateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.payment.simplepaymentgateway.dto.request.PaymentRequestDTO;
import com.payment.simplepaymentgateway.dto.response.PaymentResponseDTO;
import com.payment.simplepaymentgateway.dto.response.TransactionStatusResponseDTO;

public interface PaymentService {
    PaymentResponseDTO initiateTransaction(PaymentRequestDTO request) throws JsonProcessingException;

    TransactionStatusResponseDTO checkTransactionStatus(String transactionRef);
}
