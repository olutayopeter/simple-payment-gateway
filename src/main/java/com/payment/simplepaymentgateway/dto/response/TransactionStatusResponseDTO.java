package com.payment.simplepaymentgateway.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionStatusResponseDTO {
    private String status;
    private String message;
    private TransactionStatusDataDTO data;

    @Data
    @Builder
    public static class TransactionStatusDataDTO {
        private String authorizationId;
        private String transactionRef;
        private Double amount;
        private String paymentStatus;
        private String transactionDescription;
        private LocalDateTime transactionDate;
        private LocalDateTime paymentDate;
        private String currency;
        private String destinationAccount;
        private String destinationBankCode;
        private String sourceAccount;
        private String sourceBankCode;
        private String destinationEmail;
        private String sourceEmail;
    }
}
