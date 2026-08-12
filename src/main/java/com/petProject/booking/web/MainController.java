package com.petProject.booking.web;

import com.petProject.booking.room.Room;
import com.petProject.booking.room.RoomRepository;
import com.petProject.booking.room.dto.BookedData;
import com.petProject.booking.common.exception.IncorrectBookTimeException;
import com.petProject.booking.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Score;
import org.springframework.data.domain.ScoringFunction;
import org.springframework.data.domain.Vector;
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

    @GetMapping("/main")
    public String home(Model model, @AuthenticationPrincipal OidcUser user) {
        model.addAttribute("exception", false);
        extractUserInfo(model, user);
        return "bookMain";
    }


    private void extractUserInfo(Model model, OidcUser user) {
        if (user != null) {
            this.userService.addUser(user);
            model.addAttribute("authorizeUser", true);
        } else {
            model.addAttribute("authorizeUser", false);
        }
    }

    @PostMapping("/main")
    public String searchAccommodation (
            @RequestParam String country, @RequestParam String city,
            @RequestParam LocalDate start, @RequestParam LocalDate end, RedirectAttributes redirectAttributes,
            Model model, @AuthenticationPrincipal OidcUser oidcUser
    ){
        try {
            BookedData bookedData = new BookedData(start, end);
            redirectAttributes.addAttribute("start", bookedData.getStartDate());
            redirectAttributes.addAttribute("end", bookedData.getEndDate());
            redirectAttributes.addAttribute("country", country);
            redirectAttributes.addAttribute("city", city);
            return "redirect:/result";
        } catch (IncorrectBookTimeException e) {
            model.addAttribute("country", country);
            model.addAttribute("city", city);
            model.addAttribute("exception", true);
            model.addAttribute("authorizeUser", oidcUser != null);
            return "bookMain";
        }
    }
}
