package com.payment.simplepaymentgateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.simplepaymentgateway.controller.PaymentController;
import com.payment.simplepaymentgateway.dto.request.PaymentRequestDTO;
import com.payment.simplepaymentgateway.dto.response.PaymentResponseDTO;
import com.payment.simplepaymentgateway.dto.response.TransactionStatusResponseDTO;
import com.payment.simplepaymentgateway.model.enumeration.Channel;
import com.payment.simplepaymentgateway.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private MockMvc mockMvc;

    @Test
    void initiateTransaction_Success() throws Exception {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();

        PaymentRequestDTO request = buildSamplePaymentRequestDTO();

        when(paymentService.initiateTransaction(any())).thenReturn(buildSamplePaymentResponseDTO());

        mockMvc.perform(post("/api/v1/payment/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("00"))
                .andExpect(jsonPath("$.message").value("The process was completed successfully"));
    }

    @Test
    void checkTransactionStatus_Success() throws Exception {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();

        String transactionRef = "sampleTransactionRef";

        when(paymentService.checkTransactionStatus(transactionRef)).thenReturn(buildSampleTransactionStatusResponseDTO());

        mockMvc.perform(get("/api/v1/payment/status/{transactionRef}", transactionRef))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("00"))
                .andExpect(jsonPath("$.message").value("Account [sampleSourceAccount] Debited successfully. Credit in progress"));
    }

    private PaymentRequestDTO buildSamplePaymentRequestDTO() {
        return PaymentRequestDTO.builder()
                .amount(100.0)
                .transactionRef("345678900123")
                .transactionDescription("Sample Transaction")
                .channel(Channel.WEB)
                .currency("NGN")
                .destinationAccount("0692887918")
                .destinationAccountName("Olutayo Adelodun")
                .destinationBankCode("044")
                .sourceAccount("0036251435")
                .sourceAccountName("Edith Otulugbu")
                .sourceBankCode("027")
                .build();
    }

    private PaymentResponseDTO buildSamplePaymentResponseDTO() {
        return PaymentResponseDTO.builder()
                .status("00")
                .message("The process was completed successfully")
                .data(PaymentResponseDTO.PaymentDataDTO.builder()
                        .amount(100.0)
                        .transactionRef("345678900123")
                        .transactionDescription("Sample Transaction")
                        .transactionDate("2022-02-22T10:00:00")
                        .paymentDate("2022-02-22T10:05:00")
                        .build())
                .build();
    }

    private TransactionStatusResponseDTO buildSampleTransactionStatusResponseDTO() {
        return TransactionStatusResponseDTO.builder()
                .status("00")
                .message("Account [sampleSourceAccount] Debited successfully. Credit in progress")
                .data(TransactionStatusResponseDTO.TransactionStatusDataDTO.builder()
                        .transactionRef("345678900123")
                        .amount(100.0)
                        .paymentStatus("SUCCESS")
                        .transactionDescription("Sample Transaction")
                        .currency("NGN")
                        .destinationAccount("0692887918")
                        .destinationBankCode("044")
                        .sourceAccount("0036251435")
                        .sourceBankCode("017")
                        .build())
                .build();
    }

    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}