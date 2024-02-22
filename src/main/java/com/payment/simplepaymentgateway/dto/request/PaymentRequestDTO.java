package com.payment.simplepaymentgateway.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;
import com.payment.simplepaymentgateway.model.enumeration.Channel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be a positive value")
    @Digits(integer = 10, fraction = 2, message = "Amount must be a valid numeric value")
    private Double amount;

    @NotBlank(message = "Transaction reference cannot be blank")
    private String transactionRef;

    @NotBlank(message = "Transaction description cannot be blank")
    private String transactionDescription;

    @NotNull(message = "Channel cannot be null")
    private Channel channel;

    @NotBlank(message = "Currency cannot be blank")
    private String currency;

     @NotBlank(message = "Destination account cannot be blank")
    private String destinationAccount;

    @NotBlank(message = "Destination account name cannot be blank")
    private String destinationAccountName;

    @NotBlank(message = "Destination bank code cannot be blank")
    private String destinationBankCode;

    @NotBlank(message = "Source account cannot be blank")
    private String sourceAccount;

    private String sourceAccountName;

    @NotBlank(message = "Source bank code cannot be blank")
    private String sourceBankCode;

    private String destinationEmail;

    private String sourceEmail;

}
