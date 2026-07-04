package com.petProject.booking.room;

import com.petProject.booking.hotel.HotelService;
import com.petProject.booking.hotel.Star;
import com.petProject.booking.room.dto.RoomResponse;
import com.petProject.booking.common.exception.IncorrectMaxMinPriceException;
import com.petProject.booking.room.dto.SortDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String result (Model model, @RequestParam List<Room> rooms, HttpSession session, @AuthenticationPrincipal OidcUser oidcUser) {
        model.addAttribute("error", false);
        model.addAttribute("star", Star.ANY);
        model.addAttribute("roomCategory", RoomCategory.ANY);
        model.addAttribute("maxPrice", 1000);
        model.addAttribute("minPrice", 0);
        session.setAttribute("rooms", rooms);
        model.addAttribute("authorizeUser", oidcUser != null);
        return "result";
    }


    /**TODO*
     * make as in BookController
     * */
    @PostMapping("/result")
    public String postResult (Model model, HttpSession session, @ModelAttribute SortDTO sortDTO) {
        ArrayList<Room> newResponses = new ArrayList<>();
        newResponses.addAll((ArrayList<Room>) session.getAttribute("rooms"));
        model.addAttribute("star", sortDTO.star());
        model.addAttribute("roomCategory", sortDTO.roomCategory());
        //try {
            model.addAttribute("error", false);
            /**TODO* створи getRoomsByAnotherInput нормальний!!!!*/
            newResponses = this.hotelService.getRoomsByAnotherInput(sortDTO.min(), sortDTO.max(), sortDTO.star(), sortDTO.roomCategory(), newResponses);
            model.addAttribute("maxPrice", sortDTO.max());
            model.addAttribute("minPrice", sortDTO.min());
//        } catch (IncorrectMaxMinPriceException e) {
//            ArrayList<Room> responses = (ArrayList<Room>) session.getAttribute("rooms");
//            model.addAttribute("rooms", responses);
//            model.addAttribute("error", true);
//            model.addAttribute("maxPrice", 1000);
//            model.addAttribute("minPrice", 0);
//        }
        model.addAttribute("rooms", newResponses);

        return "result";
    }

}
