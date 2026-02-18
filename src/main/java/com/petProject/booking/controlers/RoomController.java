package com.petProject.booking.controlers;

import com.petProject.booking.accommodation.hotel.HotelService;
import com.petProject.booking.accommodation.hotel.Star;
import com.petProject.booking.accommodation.room.RoomCategory;
import com.petProject.booking.accommodation.room.RoomResponse;
import com.petProject.booking.accommodation.room.RoomService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final HotelService hotelService;

    @GetMapping("/result")
    public String result (Model model, @RequestParam List<Long> rooms, HttpSession session) {
        ArrayList<RoomResponse> result = this.roomService.getRoomsById(rooms);
        model.addAttribute("rooms", result);
        session.setAttribute("rooms", result);

        return "result";
    }

    @PostMapping("/result")
    public String postResult (Model model, HttpSession session, @RequestParam int min, @RequestParam int max, @RequestParam Star star, @RequestParam RoomCategory roomCategory) {
        ArrayList<RoomResponse> newResponses = new ArrayList<>();
        newResponses.addAll((ArrayList<RoomResponse>) session.getAttribute("rooms"));
        newResponses = this.hotelService.getRoomsByAnotherInput(min, max, star, roomCategory, newResponses);
        model.addAttribute("rooms", newResponses);

        return "result";
    }

}
