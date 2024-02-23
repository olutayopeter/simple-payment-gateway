package com.payment.simplepaymentgateway.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.payment.simplepaymentgateway.dto.request.PaymentRequestDTO;
import com.payment.simplepaymentgateway.dto.response.PaymentResponseDTO;
import com.payment.simplepaymentgateway.dto.response.TransactionStatusResponseDTO;
import com.payment.simplepaymentgateway.model.BankCodes;
import com.payment.simplepaymentgateway.model.PaymentTransaction;
import com.payment.simplepaymentgateway.repository.BankCodeRepository;
import com.payment.simplepaymentgateway.repository.PaymentRepository;
import com.payment.simplepaymentgateway.service.KafkaProducerService;
import com.payment.simplepaymentgateway.service.PaymentService;
import com.payment.simplepaymentgateway.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final PaymentRepository paymentRepository;

    private final BankCodeRepository bankCodeRepository;

    private final KafkaProducerService kafkaProducerService;

    private final Gson gson;

    private ObjectMapper objectMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository,BankCodeRepository bankCodeRepository,KafkaProducerService kafkaProducerService,Gson gson, ObjectMapper objectMapper) {

        this.paymentRepository = paymentRepository;
        this.bankCodeRepository = bankCodeRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.gson = gson;
        this.objectMapper = objectMapper;
    }

    @Override
    public PaymentResponseDTO initiateTransaction(PaymentRequestDTO request) throws JsonProcessingException {

        // Log the initiation of a transaction
        log.info("Payment request: {}", gson.fromJson(objectMapper.writeValueAsString(request), PaymentRequestDTO.class));

        log.info("Initiating transaction with transactionRef: {}", request.getTransactionRef());

        String authId = Util.generateAuthorizationId();
        try {

            // Log the response details
            if(request.getDestinationAccount().equals(request.getSourceAccount())) {
                return PaymentResponseDTO.builder()
                        .status("58")
                        .message("Transaction not permitted to sender.")
                        .build();
            }
            Optional<PaymentTransaction> optionalTransaction = paymentRepository.findByTransactionRef(request.getTransactionRef());
            if (optionalTransaction.isPresent()) {
                return PaymentResponseDTO.builder()
                        .status("99")
                        .message("Duplicate transaction reference.")
                        .build();
            }

            Optional<BankCodes> optionalSourceBankCode = bankCodeRepository.findByBankCode(request.getSourceBankCode());
            if(!optionalSourceBankCode.isPresent()) {
                return PaymentResponseDTO.builder()
                        .status("15")
                        .message("Invalid source bank code.")
                        .build();
            }

            Optional<BankCodes> optionalDestinationBankCode = bankCodeRepository.findByBankCode(request.getDestinationBankCode());
            if(!optionalDestinationBankCode.isPresent()) {
                return PaymentResponseDTO.builder()
                        .status("16")
                        .message("Invalid destination bank code.")
                        .build();
            }

            PaymentTransaction paymentTransaction =  getPaymentTransaction(request,authId, "00","SUCCESS","The process was completed successfully");

            paymentRepository.save(paymentTransaction);

            // Check if the transaction is successful
            if (paymentTransaction.getStatus().equals("00")) {
                // Send notification for successful transaction using Kafka
                kafkaProducerService.sendNotification(paymentTransaction.getTransactionRef());
            }

            // Create and return the response
            return PaymentResponseDTO.builder()
                    .status(paymentTransaction.getStatus())
                    .message("The process was completed successfully")
                    .data(PaymentResponseDTO.PaymentDataDTO.builder()
                            .amount(paymentTransaction.getAmount())
                            .transactionRef(paymentTransaction.getTransactionRef())
                            .transactionDescription(paymentTransaction.getTransactionDescription())
                            .authorizationId(authId)
                            .transactionDate(paymentTransaction.getTransactionDate().toString())
                            .paymentDate(paymentTransaction.getPaymentDate().toString())
                            .destinationEmail(paymentTransaction.getDestinationEmail())
                            .sourceEmail(paymentTransaction.getSourceEmail())
                            .build())
                    .build();

        } catch (Exception e) {

            // Log the exception
            log.error("Initiate Transaction exception: {}", e.fillInStackTrace());
            PaymentTransaction paymentTransaction =  getPaymentTransaction(request,authId, "101","FAIL",e.getMessage());

            paymentRepository.save(paymentTransaction);

            return PaymentResponseDTO.builder()
                    .status("101")
                    .message("Unknown error.")
                    .data(PaymentResponseDTO.PaymentDataDTO.builder()
                            .amount(request.getAmount())
                            .transactionRef(request.getTransactionRef())
                            .transactionDescription(request.getTransactionDescription())
                            .authorizationId(authId)
                            .transactionDate(LocalDateTime.now().toString())
                            .paymentDate(LocalDateTime.now().toString())
                            .destinationEmail(request.getDestinationEmail())
                            .sourceEmail(request.getSourceEmail())
                            .build())
                    .build();
        }
    }

    @Override
    public TransactionStatusResponseDTO checkTransactionStatus(String transactionRef) {
        log.info("Checking transaction status for transactionRef: {}", transactionRef);
        Optional<PaymentTransaction> optionalTransaction = paymentRepository.findByTransactionRef(transactionRef);
        String message = "";

        try {

            if (optionalTransaction.isPresent()) {
                PaymentTransaction transaction = optionalTransaction.get();

                if (optionalTransaction.get().getStatus().equals("00")) {
                    message = "Account [" + transaction.getSourceAccount() + "] Debited successfully. Credit in progress.";
                } else {
                    message = optionalTransaction.get().getMessage();
                }

                // Create and return the response with actual status from the database
                return TransactionStatusResponseDTO.builder()
                        .status(transaction.getStatus()) // Assume there's a 'status' field in PaymentTransaction
                        .message(message)
                        .data(TransactionStatusResponseDTO.TransactionStatusDataDTO.builder()
                                .authorizationId(transaction.getAuthorizationId())
                                .transactionRef(transaction.getTransactionRef())
                                .amount(transaction.getAmount())
                                .paymentStatus(transaction.getPaymentStatus())
                                .transactionDescription(transaction.getTransactionDescription())
                                .transactionDate(transaction.getTransactionDate())
                                .paymentDate(transaction.getPaymentDate())
                                .currency(transaction.getCurrency())
                                .destinationAccount(transaction.getDestinationAccount())
                                .destinationBankCode(transaction.getDestinationBankCode())
                                .sourceAccount(transaction.getSourceAccount())
                                .sourceBankCode(transaction.getSourceBankCode())
                                .destinationEmail(transaction.getDestinationEmail())
                                .sourceEmail(transaction.getSourceEmail())
                                .build())
                        .build();
            } else {
                // Handle case where transactionRef is not found
                return TransactionStatusResponseDTO.builder()
                        .status("08")
                        .message("Transaction not found")
                        .build();
            }

        } catch(Exception e) {
            log.error("Check Transaction Status: {}", e.fillInStackTrace());
            return TransactionStatusResponseDTO.builder()
                    .status("101")
                    .message("Unknown error.")
                    .build();

        }
    }

    private static PaymentTransaction getPaymentTransaction(PaymentRequestDTO request,String authId,String status,String paymentStatus,String message) {
        PaymentTransaction paymentTransaction = PaymentTransaction.builder()
                .amount(request.getAmount())
                .transactionRef(request.getTransactionRef())
                .transactionDescription(request.getTransactionDescription())
                .channel(request.getChannel().toString())
                .currency(request.getCurrency())
                .destinationAccount(request.getDestinationAccount())
                .destinationAccountName(request.getDestinationAccountName())
                .destinationBankCode(request.getDestinationBankCode())
                .sourceAccount(request.getSourceAccount())
                .sourceAccountName(request.getSourceAccountName())
                .sourceBankCode(request.getSourceBankCode())
                .authorizationId(authId)
                .transactionDate(LocalDateTime.now())
                .paymentDate(LocalDateTime.now())
                .paymentStatus(paymentStatus)
                .status(status)
                .message(message)
                .destinationEmail(request.getDestinationEmail())
                .sourceEmail(request.getSourceEmail())
                .build();

        return paymentTransaction;
    }

}
