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

    @DeleteMapping("/hotel/{id}/delete")
    public String deleteHotel(@PathVariable long id, Model model) {
        boolean isRemoved = this.hotelService.remove(id);
        model.addAttribute("isRemoved", isRemoved);
        return "redirect:admin";
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
