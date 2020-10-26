package com.HotelAlexa.rezervaCamera.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;

@SessionScope
@Component
public class UserSession {
    private int id;


    HashMap<Integer, Integer> cart = new HashMap<>();
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<Integer, Integer> getCart() {
        return cart;
    }

    public void setCart(HashMap<Integer, Integer> cart) {
        this.cart = cart;
    }

    public void addIntoCart(int id, int quantity){
        if(cart.containsKey(id)){
            int quantityFinal =  cart.get(id) + quantity;
            cart.put(id, quantityFinal);
        } else {
            cart.put(id, quantity);
        }
    }

    public Integer getSizeOfCart(){
        Integer items =0;
        for (Integer i : cart.keySet()){
            items +=cart.get(i);
        }
        return items;
    }
}
