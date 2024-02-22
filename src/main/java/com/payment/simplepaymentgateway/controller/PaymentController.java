package com.payment.simplepaymentgateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.payment.simplepaymentgateway.dto.request.PaymentRequestDTO;
import com.payment.simplepaymentgateway.dto.response.PaymentResponseDTO;
import com.payment.simplepaymentgateway.dto.response.TransactionStatusResponseDTO;
import com.payment.simplepaymentgateway.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    private Gson gson;

    private ObjectMapper objectMapper;

    public PaymentController(PaymentService paymentService,Gson gson,ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.gson = gson;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponseDTO> initiateTransaction(@RequestBody PaymentRequestDTO request) throws JsonProcessingException {
        // Log the receipt of an initiate transaction request
        log.info("Received initiate transaction request: {}", gson.fromJson(objectMapper.writeValueAsString(request), PaymentRequestDTO.class));
        try {
            PaymentResponseDTO response = paymentService.initiateTransaction(request);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            // Log the exception for debugging purposes
            log.info("Initiate Payment exception from controller: {}"  +  e.fillInStackTrace());
            // Return an error response with the required fields
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(PaymentResponseDTO.builder()
                            .status("102")
                            .message("Server error. Unable to process the transaction.")
                            .build());
        }
    }

    @GetMapping("/status/{transactionRef}")
    public ResponseEntity<TransactionStatusResponseDTO> checkTransactionStatus(@PathVariable String transactionRef) {
        // Log the receipt of a check transaction status request
        log.info("Received check transaction status request for transactionRef: {}", transactionRef);

        try {
            TransactionStatusResponseDTO response = paymentService.checkTransactionStatus(transactionRef);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.info("Initiate Payment exception from controller: {}"  +  e.fillInStackTrace());
            // Return an error response with the required fields
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(TransactionStatusResponseDTO.builder()
                            .status("102")
                            .message("Server error. Unable to check transaction status.")
                            .build());
        }
    }
}