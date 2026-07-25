package com.petProject.booking.hotel;

import com.petProject.booking.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class HotelController {
    private final HotelService hotelService;
    private final UserService userService;

    /**TODO* change to DeleteMapping and id to long*/
    @PostMapping("/hotel/delete")
    public String deleteHotel(@RequestParam long id) {
        this.hotelService.remove(id);
        return "redirect:/admin";
    }

    @PutMapping("/user/ban/{email}")
    public String banUser(@PathVariable String email) {
        this.userService.ban(email);
        return "redirect:main";
    }

    @GetMapping()
    public String admin() {
        return "admin";
    }
}
