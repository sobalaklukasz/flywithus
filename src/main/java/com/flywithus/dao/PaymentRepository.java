package com.flywithus.dao;

import com.flywithus.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Modifying
    @Query("UPDATE Payment p SET p.paid = true WHERE p.id = :id")
    @Transactional
    int setPaymentAsDone(@Param("id") long id);

    List<Payment> findAllByPaidIsFalse();

}
