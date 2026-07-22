package com.petProject.booking.web;

import com.petProject.booking.room.dto.FilterData;
import com.petProject.booking.room.Room;
import com.petProject.booking.room.RoomService;
import com.petProject.booking.room.dto.BookedData;
import com.petProject.booking.common.exception.IncorrectBookTimeException;
import com.petProject.booking.tool.ExtractDataFromBooking;
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

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final RoomService roomService;

    @GetMapping("/main")
    public String home(Model model, @AuthenticationPrincipal OidcUser user) {
        model.addAttribute("exception", false);
        extractUserInfo(model, user);
        return "bookMain";
    }

    private void extractUserInfo(Model model, OidcUser user) {
        if (user != null) {
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
        List<Room> rooms;
        try {
            BookedData bookedData = new BookedData(start, end);
            rooms = this.roomService.getRooms(FilterData.builder()
                    .city(city)
                    .country(country)
                    .bookedData(new BookedData(start, end))
                    .build());
            httpSession.setAttribute("bookedData", bookedData);
            httpSession.setAttribute("start", start);
            httpSession.setAttribute("end", end);
        } catch (IncorrectBookTimeException e) {
            model.addAttribute("country", country);
            model.addAttribute("city", city);
            model.addAttribute("exception", true);
            model.addAttribute("authorizeUser", oidcUser != null);
            return "bookMain";
        }
        redirectAttributes.addAttribute("rooms", rooms);
        redirectAttributes.addAttribute("start", start);
        redirectAttributes.addAttribute("end", end);
        redirectAttributes.addAttribute("country", country);
        redirectAttributes.addAttribute("city", city);
        return "redirect:/result";
    }
}
