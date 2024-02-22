package com.payment.simplepaymentgateway.repository;


import com.payment.simplepaymentgateway.model.BankCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankCodeRepository extends JpaRepository<BankCodes,Long> {
    Optional<BankCodes> findByBankCode(String bankCode);
}
