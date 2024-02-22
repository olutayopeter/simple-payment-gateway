package com.payment.simplepaymentgateway.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponseDTO {
    private String status;
    private String message;
    private PaymentDataDTO data;

    @Data
    @Builder
    public static class PaymentDataDTO {
        private Double amount;
        private String transactionRef;
        private String transactionDescription;
        private String authorizationId;
        private String transactionDate;
        private String paymentDate;
        private String destinationEmail;
        private String sourceEmail;
    }
}
