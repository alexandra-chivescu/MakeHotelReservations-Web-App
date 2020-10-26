package com.HotelAlexa.rezervaCamera.service;

import com.HotelAlexa.rezervaCamera.dao.RoomDao;
import com.HotelAlexa.rezervaCamera.model.Reservation;
import com.HotelAlexa.rezervaCamera.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class RoomService {
    @Autowired
    RoomDao roomDao;

    public List<Room> getCartProducts(HashMap<Integer, Integer> mapCart) {
        List<Room> list = new ArrayList<>();
        for (Integer id : mapCart.keySet()) {
            list.add(roomDao.findById(id).get());
        }
        return list;
    }

    public List<Room> getListRooms() {
        return roomDao.findAll();
    }

}
