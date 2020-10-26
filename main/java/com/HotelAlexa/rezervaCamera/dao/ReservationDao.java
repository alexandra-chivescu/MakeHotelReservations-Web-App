package com.HotelAlexa.rezervaCamera.dao;

import com.HotelAlexa.rezervaCamera.model.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Component
public interface ReservationDao extends CrudRepository<Reservation, Integer> {
   // @Query("SELECT checkInDate, checkOutDate from reservation r WHERE r.id=(SELECT idRoom FROM rooms")


    List<Reservation> findAll();


}
