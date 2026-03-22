package com.petProject.booking.room;

import com.petProject.booking.hotel.HotelService;
import com.petProject.booking.hotel.Star;
import com.petProject.booking.room.dto.RoomResponse;
import com.petProject.booking.common.exception.IncorrectMaxMinPriceException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
        model.addAttribute("error", false);
        model.addAttribute("star", Star.ANY);
        model.addAttribute("roomCategory", RoomCategory.ANY);
        model.addAttribute("maxPrice", 1000);
        model.addAttribute("minPrice", 0);
        session.setAttribute("rooms", result);

        return "result";
    }

    @PostMapping("/result")
    public String postResult (Model model, HttpSession session, @RequestParam int min, @RequestParam int max, @RequestParam Star star,
                              @RequestParam RoomCategory roomCategory) {
        ArrayList<RoomResponse> newResponses = new ArrayList<>();
        newResponses.addAll((ArrayList<RoomResponse>) session.getAttribute("rooms"));
        model.addAttribute("star", star);
        model.addAttribute("roomCategory", roomCategory);
        try {
            model.addAttribute("error", false);
            newResponses = this.hotelService.getRoomsByAnotherInput(min, max, star, roomCategory, newResponses);
            model.addAttribute("maxPrice", max);
            model.addAttribute("minPrice", min);
        } catch (IncorrectMaxMinPriceException e) {
            ArrayList<RoomResponse> responses = (ArrayList<RoomResponse>) session.getAttribute("rooms");
            model.addAttribute("rooms", responses);
            model.addAttribute("error", true);
            model.addAttribute("maxPrice", 1000);
            model.addAttribute("minPrice", 0);
        }
        model.addAttribute("rooms", newResponses);

        return "result";
    }

}
