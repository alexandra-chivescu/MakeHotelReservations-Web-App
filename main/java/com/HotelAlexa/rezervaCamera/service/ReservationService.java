package com.HotelAlexa.rezervaCamera.service;

import com.HotelAlexa.rezervaCamera.dao.ReservationDao;
import com.HotelAlexa.rezervaCamera.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    @Autowired
    ReservationDao reservationDao;

    public void save(String checkInDate, String checkOutDate, Integer idRoom, Integer idUser) {
        Reservation reservation = new Reservation();
        reservation.setCheckInDate(checkInDate);
        reservation.setCheckOutDate(checkOutDate);
        reservation.setIdRoom(idRoom);
        reservation.setIdUser(idUser);
        reservationDao.save(reservation);
    }

    public List<Reservation> getListReservations() {
        return reservationDao.findAll();
    }
}
