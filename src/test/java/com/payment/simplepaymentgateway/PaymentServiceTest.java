package com.payment.simplepaymentgateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.payment.simplepaymentgateway.dto.request.PaymentRequestDTO;
import com.payment.simplepaymentgateway.dto.response.PaymentResponseDTO;
import com.payment.simplepaymentgateway.model.BankCodes;
import com.payment.simplepaymentgateway.model.PaymentTransaction;
import com.payment.simplepaymentgateway.repository.BankCodeRepository;
import com.payment.simplepaymentgateway.repository.PaymentRepository;
import com.payment.simplepaymentgateway.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BankCodeRepository bankCodeRepository;

    @Mock
    private Gson gson;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInitiateTransaction_Success() throws JsonProcessingException {
        // Mocking dependencies
        when(paymentRepository.findByTransactionRef(any())).thenReturn(Optional.empty());
        when(bankCodeRepository.findByBankCode(any())).thenReturn(Optional.of(new BankCodes())); // Assuming a BankCodes class exists

        // Mocking behavior of gson and objectMapper
        when(gson.fromJson(any(String.class), eq(PaymentRequestDTO.class))).thenReturn(new PaymentRequestDTO());
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        // Mocking behavior of save method in paymentRepository
        when(paymentRepository.save(any())).thenReturn(new PaymentTransaction());

        // Test the initiateTransaction method
        PaymentRequestDTO request = new PaymentRequestDTO();
        paymentService.initiateTransaction(request);

        // Verify that save method is called once
        verify(paymentRepository, times(1)).save(any());
    }

    @Test
    void testInitiateTransaction_DuplicateTransactionRef() throws JsonProcessingException {
        // Mocking dependencies
        when(paymentRepository.findByTransactionRef(any())).thenReturn(Optional.of(new PaymentTransaction()));

        // Mocking behavior of gson and objectMapper
        when(gson.fromJson(anyString(), eq(PaymentRequestDTO.class))).thenReturn(new PaymentRequestDTO());
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        // Test the initiateTransaction method
        PaymentRequestDTO request = new PaymentRequestDTO();
        PaymentResponseDTO response = paymentService.initiateTransaction(request);

        // Verify that save method is not called in case of a duplicate transaction reference
        verify(paymentRepository, never()).save(any());
    }

}