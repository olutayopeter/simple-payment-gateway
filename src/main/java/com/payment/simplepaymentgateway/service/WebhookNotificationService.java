package com.payment.simplepaymentgateway.service;

import com.payment.simplepaymentgateway.dto.response.PaymentResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class WebhookNotificationService {

//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final JavaMailSender javaMailSender;
//
//    private static final Logger log = LoggerFactory.getLogger(WebhookNotificationService.class);
//
//    public WebhookNotificationService(KafkaTemplate<String, String> kafkaTemplate,JavaMailSender javaMailSender) {
//        this.kafkaTemplate = kafkaTemplate;
//        this.javaMailSender = javaMailSender;
//    }
//
//
//    @KafkaListener(topics = "notification-topic", groupId = "notification-consumer-group")
//    public void receiveNotification(PaymentResponseDTO paymentResponseDTO) {
//        sendNotification(paymentResponseDTO);
//    }
//
//    private void sendNotification(PaymentResponseDTO paymentResponseDTO) {
//        log.info("Sending notification to destination account: {}", paymentResponseDTO);
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(paymentResponseDTO.getData().getDestinationEmail());
//        mailMessage.setSubject("Payment Notification");
//        mailMessage.setText("Dear user,\n\nYour payment has been processed successfully.");
//
//        javaMailSender.send(mailMessage);
//
//        log.info("Notification email sent to: {}", paymentResponseDTO.getData().getDestinationEmail());
//    }


}
