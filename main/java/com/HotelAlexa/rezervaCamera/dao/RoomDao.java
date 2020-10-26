package com.HotelAlexa.rezervaCamera.dao;


import com.HotelAlexa.rezervaCamera.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomDao extends CrudRepository<Room, Integer> {
    List<Room> findAll();
}