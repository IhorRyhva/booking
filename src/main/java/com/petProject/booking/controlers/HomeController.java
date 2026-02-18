package com.petProject.booking.controlers;

import com.petProject.booking.accommodation.hotel.HotelRequest;
import com.petProject.booking.accommodation.hotel.HotelService;
import com.petProject.booking.accommodation.hotel.Location;
import com.petProject.booking.accommodation.hotel.Star;
import com.petProject.booking.accommodation.room.RoomCategory;
import com.petProject.booking.accommodation.room.RoomResponse;
import com.petProject.booking.accommodation.room.RoomService;
import com.petProject.booking.data.ExtractDataFromBooking;
import com.petProject.booking.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;
    private final HotelService hotelService;
    private final ExtractDataFromBooking data;

    @GetMapping("/main")
    public String home(Model model, @AuthenticationPrincipal OidcUser user) throws IOException {
        if (user != null) {
            model.addAttribute("name", user.getFullName());
            model.addAttribute("username", user.getPreferredUsername());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("roles", user.getAuthorities());
            model.addAttribute("authorizeUser", true);
            data.putData();
            this.userService.addUser(user);
        } else {
            model.addAttribute("authorizeUser", false);
        }
        return "bookMain";
    }

    @PostMapping("/main")
    public String searchAccommodation (
            @RequestParam String country, @RequestParam String city,
            @RequestParam LocalDate start, @RequestParam LocalDate end, RedirectAttributes redirectAttributes, HttpSession httpSession
    ) {
        List<Long> rooms = this.hotelService.getRoomsByDataAndLocation(country, city, start, end);
        redirectAttributes.addAttribute("rooms", rooms);
        httpSession.setAttribute("start", start);
        httpSession.setAttribute("end", end);
        return "redirect:/result";
    }

}
