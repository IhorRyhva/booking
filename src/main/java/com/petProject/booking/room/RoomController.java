package com.petProject.booking.room;

import com.petProject.booking.common.exception.IncorrectMaxMinPriceException;
import com.petProject.booking.room.dto.FilterData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/result/{page}")
    public String result (Model model, @RequestParam String city, @RequestParam String country, @RequestParam LocalDate start,
                          @RequestParam LocalDate end, @AuthenticationPrincipal OidcUser oidcUser,
                          @PathVariable int page) {
        model.addAttribute("error", false);
        model.addAttribute("maxPrice", 1000);
        model.addAttribute("minPrice", 0);
        model.addAttribute("authorizeUser", oidcUser != null);
        Page<Room> rooms = this.roomService.getRooms(FilterData.builder()
                .max(1000)
                .min(0)
                .start(start)
                .end(end)
                .city(city)
                .country(country)
                .build(), page);
        model.addAttribute("rooms", rooms);
        model.addAttribute("country", country);
        model.addAttribute("city", city);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", rooms.getSize() - 1);
        return "result";
    }

    @PostMapping("/result")
    public String postResult (Model model, @ModelAttribute FilterData filterData,
                              @AuthenticationPrincipal OidcUser oidcUser) throws IncorrectMaxMinPriceException {
        if (filterData.min() > filterData.max()) {
            throw new IncorrectMaxMinPriceException("You enter incorrect values");
        }
        Page<Room> rooms = this.roomService.getRooms(filterData, 0);
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", rooms.getSize() - 1);
        model.addAttribute("authorizeUser", oidcUser != null);
        model.addAttribute("star", filterData.star());
        model.addAttribute("roomCategory", filterData.roomCategory());
        model.addAttribute("error", false);
        model.addAttribute("maxPrice", filterData.max());
        model.addAttribute("minPrice", filterData.min());
        model.addAttribute("rooms", rooms);
        model.addAttribute("country", filterData.country());
        model.addAttribute("city", filterData.city());
        model.addAttribute("start", filterData.start());
        model.addAttribute("end", filterData.end());
        model.addAttribute("bedNumber", filterData.bedNumber());
        return "result";
    }

}
