package com.HotelAlexa.rezervaCamera.dao;
import com.HotelAlexa.rezervaCamera.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends CrudRepository<User, Integer> {
    List<User> findByEmail(String email);
}
