package com.petProject.booking.web;

import com.petProject.booking.hotel.HotelService;
import com.petProject.booking.room.dto.BookedData;
import com.petProject.booking.common.exception.IncorrectBookTimeException;
import com.petProject.booking.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final HotelService hotelService;

    @GetMapping("/main")
    public String home(Model model, @AuthenticationPrincipal OidcUser user) {
        model.addAttribute("exception", false);
        extractUserInfo(model, user);
        return "bookMain";
    }

    private void extractUserInfo(Model model, OidcUser user) {
        if (user != null) {
            model.addAttribute("name", user.getFullName());
            model.addAttribute("username", user.getPreferredUsername());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("roles", user.getAuthorities());
            model.addAttribute("authorizeUser", true);
            this.userService.addUser(user);
        } else {
            model.addAttribute("authorizeUser", false);
        }
    }

    @PostMapping("/main")
    public String searchAccommodation (
            @RequestParam String country, @RequestParam String city,
            @RequestParam LocalDate start, @RequestParam LocalDate end, RedirectAttributes redirectAttributes, HttpSession httpSession,
            Model model, @AuthenticationPrincipal OidcUser oidcUser
    ){
        List<Long> rooms;
        try {
            BookedData bookedData = new BookedData(start, end);

            rooms = this.hotelService.getRoomsByDataAndLocation(country, city, bookedData);
            httpSession.setAttribute("bookedData", bookedData);
            httpSession.setAttribute("start", start);
            httpSession.setAttribute("end", end);
        } catch (IncorrectBookTimeException e) {
            model.addAttribute("country", country);
            model.addAttribute("city", city);
            model.addAttribute("exception", true);
            this.extractUserInfo(model, oidcUser);
            return "bookMain";
        }
        redirectAttributes.addAttribute("rooms", rooms);
        return "redirect:/result";
    }
}
