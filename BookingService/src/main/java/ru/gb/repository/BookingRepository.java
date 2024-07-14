package ru.gb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.model.Booking;
@Repository
public interface BookingRepository extends JpaRepository <Booking, Long> {

}
