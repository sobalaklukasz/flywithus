package com.flywithus.dao;

import com.flywithus.entity.AvailableDepartureSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
public interface AvailableDepartureSeatRepository extends JpaRepository<AvailableDepartureSeat, Long> {

    Set<AvailableDepartureSeat> findAvailableDepartureSeatsByReservationId(long reservationId);

    @Modifying
    @Query("UPDATE AvailableDepartureSeat SET reservation = null WHERE reservation.id = :id")
    @Transactional
    int cancelReservationForAvailableDepartureSeatsByReservationId(@Param("id") long reservationId);

}
