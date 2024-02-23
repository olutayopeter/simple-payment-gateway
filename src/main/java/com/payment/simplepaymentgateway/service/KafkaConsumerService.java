package com.payment.simplepaymentgateway.service;

import com.payment.simplepaymentgateway.model.PaymentTransaction;
import com.payment.simplepaymentgateway.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class KafkaConsumerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final PaymentRepository paymentRepository;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.from}")
    private String emailFrom;

    public KafkaConsumerService(PaymentRepository paymentRepository,JavaMailSender javaMailSender) {
        this.paymentRepository = paymentRepository;
        this.javaMailSender = javaMailSender;
    }

    @KafkaListener(topics = "notification-topic", groupId = "notification-consumer-group")
    public void receiveNotification(String transactionRef) {
        // Retrieve the payment details based on transactionRef from the database
        Optional<PaymentTransaction> optionalTransaction = paymentRepository.findByTransactionRef(transactionRef);

        if (optionalTransaction.isPresent()) {
            PaymentTransaction transaction = optionalTransaction.get();

            paymentRepository.updatePaymentStatus(transactionRef, LocalDateTime.now());

            // Send email notification
            sendEmailNotification(transaction.getDestinationEmail(), "Successful Transaction Notification",
                    "Your transaction with reference: " + transactionRef + " has been completed successfully.");

            // Log the successful transaction notification
            log.info("Received successful transaction notification for transactionRef: " + transactionRef);
        } else {
            // Log an error if the transactionRef is not found in the database
            log.error("Error: Transaction not found for transactionRef: " + transactionRef);
        }
    }

    private void sendEmailNotification(String recipientEmail, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
    }
}
