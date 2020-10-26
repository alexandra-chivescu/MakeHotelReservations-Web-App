package com.HotelAlexa.rezervaCamera.controller;

import com.HotelAlexa.rezervaCamera.dao.RoomDao;
import com.HotelAlexa.rezervaCamera.dao.ReservationDao;
import com.HotelAlexa.rezervaCamera.model.Reservation;
import com.HotelAlexa.rezervaCamera.model.Room;
import com.HotelAlexa.rezervaCamera.model.User;
import com.HotelAlexa.rezervaCamera.security.UserSession;
import com.HotelAlexa.rezervaCamera.service.RoomService;
import com.HotelAlexa.rezervaCamera.service.ReservationService;
import com.HotelAlexa.rezervaCamera.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserSession userSession;

    @Autowired
    UserService userService;

    @Autowired
    RoomDao roomDao;

    @Autowired
    RoomService roomService;

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    ReservationService reservationService;

    @GetMapping("/menu")
    public ModelAndView menu() {
        ModelAndView registerMV = new ModelAndView("index.html");
        return registerMV;
    }

    @GetMapping("/login-form")
    public ModelAndView login(@RequestParam("email") String email,
                              @RequestParam("password") String password) {
        ModelAndView modelAndView = new ModelAndView("login");

        //cum fac loginul? cum verific daca utilizatorul este inregistrat?
        List<User> userList = userService.getUsersByEmail(email);
        if(userList.size() == 0){
            modelAndView.addObject("message", "Credentialele nu sunt corecte");
        }
        if(userList.size() > 1){
            modelAndView.addObject("message", "Credentialele nu sunt corecte");
        }

        if(userList.size() == 1){
            User userFromDatabase = userList.get(0);
            if(!userFromDatabase.getPassword().equals(password)){
                modelAndView.addObject("message", "Credentialele nu sunt corecte");
            } else {
                userSession.setId(userFromDatabase.getId());
                modelAndView = new ModelAndView("redirect:/reservations");
            }
        }
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        int id = userSession.getId();
        if (id != 0){
            return new ModelAndView("redirect:/reservations");
        }
        return new ModelAndView("login");
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("register");
    }

    @GetMapping("/register-form")
    public ModelAndView registerPost(@RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     @RequestParam("password-again") String password2) {
        ModelAndView modelAndView = new ModelAndView("register");

        List<User> users = userService.getUsersByEmail(email);

        if(users.size() > 0) {
            modelAndView.addObject("message", "Acest email este deja folosit");
            return modelAndView;
        }
        if(!password.equals(password2)) {
            modelAndView.addObject("message", "Parolele nu sunt identice");
        } else {
            userService.save(email, password);
        }

        return new ModelAndView("redirect:/login");
    }

    @GetMapping("reservations")
    public ModelAndView reservations(){
        int id = userSession.getId();
        if (id == 0){
            return new ModelAndView("redirect:/login");
        }
        ModelAndView modelAndView = new ModelAndView("reservations");
        List<Room> rooms = roomDao.findAll();
        modelAndView.addObject("rooms", rooms);
        int cartSize = userSession.getSizeOfCart();
        modelAndView.addObject("items", cartSize);

        return modelAndView;
    }

    @PostMapping("/add-to-cart")
    public ModelAndView addToCart(@RequestParam("checkInDate") String checkInDate,
                                  @RequestParam("checkOutDate") String checkOutDate,
                                  @RequestParam("productId") Integer id){
        //1.Cart usage
        ModelAndView modelAndView = new ModelAndView("reservations");
        userSession.addIntoCart(id, 1);
        int cartSize = userSession.getSizeOfCart();
        modelAndView.addObject("items", cartSize);

        List<Room> rooms = roomDao.findAll();
        modelAndView.addObject("rooms", rooms);

        //2.Check in and check out dates + Reservation
        String[] wholeCheckInDate = checkInDate.split("/");
        String dbCheckInDate = wholeCheckInDate[2] + "-" + wholeCheckInDate[0] + "-" + wholeCheckInDate[1];

        String[] wholeCheckOutDate = checkOutDate.split("/");
        String dbCheckOutDate = wholeCheckOutDate[2] + "-" + wholeCheckOutDate[0] + "-" + wholeCheckOutDate[1];
        int idUser = userSession.getId();
        reservationService.save(dbCheckInDate, dbCheckOutDate, id, idUser);
        return modelAndView;
    }

    @GetMapping("/cart")
    public ModelAndView getCartItems(){
        int id = userSession.getId();
        if (id == 0){
            return new ModelAndView("redirect:/login");
        }
        ModelAndView modelAndView = new ModelAndView("cart");
        List<Room> roomList = roomService.getListRooms();
        List<Reservation> reservationList = reservationService.getListReservations();
        modelAndView.addObject("userId", userSession.getId());
        modelAndView.addObject("rooms", roomList);
        modelAndView.addObject("reservations", reservationList);
        double sum = 0;
        //Presupun ca toti anii au 365 zile si o luna are 30 de zi
        for(int i = 0; i< reservationList.size(); i++) {
            //am luat data din tabelul sql
            String dayCheckin = reservationList.get(i).getCheckInDate();
            String dayCheckout = reservationList.get(i).getCheckOutDate();
            //am dat split la data ca sa o bag in vectori
            String[] wholeCheckInDate = dayCheckin.split("-");
            String[] wholeCheckOutDate = dayCheckout.split("-");
            //am calculat numarul de zile rezervate
            //aici am verificat daca este an bisect sau nu scazatorul
            int daysYear = 0;
            if( (Integer.parseInt(wholeCheckInDate[0]) %4 == 0 && Integer.parseInt(wholeCheckInDate[0]) %4 == 0 && Integer.parseInt(wholeCheckOutDate[0]) != Integer.parseInt(wholeCheckInDate[0])) || (Integer.parseInt(wholeCheckInDate[0]) % 400 == 0 && Integer.parseInt(wholeCheckOutDate[0]) != Integer.parseInt(wholeCheckInDate[0])))
                daysYear = 366;
            else
                daysYear = 365;
            //aici verific daca e luna cu 28/29/30/31 de zile
            int numberDays = (Integer.parseInt(wholeCheckOutDate[0]) - Integer.parseInt(wholeCheckInDate[0])) * daysYear +
                    (Integer.parseInt(wholeCheckOutDate[1]) - Integer.parseInt(wholeCheckInDate[1])) * 30 +
                    (Integer.parseInt(wholeCheckOutDate[2]) - Integer.parseInt(wholeCheckInDate[2]));

            if(userSession.getId() == reservationList.get(i).getIdUser()) {
                for (int j = 0; j < roomList.size(); j++) {
                    if (roomService.getListRooms().get(j).getId() == reservationList.get(i).getIdRoom()) {
                        sum = sum + numberDays * roomService.getListRooms().get(j).getPrice();
                    }
                }
            }
        }
        modelAndView.addObject("totalSum", sum);
        return modelAndView;
    }

    @GetMapping("/logout")
    public ModelAndView logout() {
        userSession.setId(0);
        return new ModelAndView("redirect:/login");
    }

}
