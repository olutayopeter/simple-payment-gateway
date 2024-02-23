package com.payment.simplepaymentgateway.repository;

import com.payment.simplepaymentgateway.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentTransaction, Long> {
    Optional<PaymentTransaction> findByTransactionRef(String transactionRef);

    @Transactional
    @Modifying
    @Query("UPDATE PaymentTransaction p " +
            "SET p.paymentStatus = 'COMPLETED', p.paymentDate = :paymentDate " +
            "WHERE p.transactionRef = :transactionRef")
    void updatePaymentStatus(@Param("transactionRef") String transactionRef,
                             @Param("paymentDate") LocalDateTime paymentDate);
}
