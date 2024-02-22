package com.payment.simplepaymentgateway.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'NONE'")
    private String status;
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'NONE'")
    private String paymentStatus;
    @Column(columnDefinition = "VARCHAR(3000)")
    private String message;
    @Column(name = "transaction_reference",nullable = false, unique = true)
    private String transactionRef;
    private String transactionDescription;
    private String channel;
    private String currency;
    private String destinationAccount;
    private String destinationAccountName;
    private String destinationBankCode;
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'olutayopeter2014@gmail.com'")
    private String destinationEmail;
    private String sourceAccount;
    private String sourceAccountName;
    private String sourceBankCode;
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'olutayo.adelodun@9psb.com.ng'")
    private String sourceEmail;
    private String authorizationId;
    private LocalDateTime transactionDate;
    private LocalDateTime paymentDate;


}
