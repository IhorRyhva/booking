package com.petProject.booking.booking;

import com.petProject.booking.hotel.Star;
import com.petProject.booking.room.*;
import com.petProject.booking.room.dto.BookedData;
import com.petProject.booking.user.User;
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

import java.time.LocalDate;
import java.util.List;
@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final UserService userService;

    @GetMapping("/bookRoom")
    public String formForBook (@AuthenticationPrincipal OidcUser user, @RequestParam String country,
                               @RequestParam String town, @RequestParam String nameOfHotel,
                               @RequestParam Star star, @RequestParam int number,
                               @RequestParam RoomCategory category, @RequestParam int price, Model model, HttpSession httpSession) {
        this.addAttributeForPage(user, country, town, nameOfHotel, star, number, category, price, model, httpSession);
        return "bookRoom";
    }

    @GetMapping("/bookedRoom")
    public String getUsersBookedRooms (Model model, @AuthenticationPrincipal OidcUser oidcUser) {
        model.addAttribute("email", oidcUser.getEmail());
        List<BookResponse> books = bookService.getBooksByUser(oidcUser.getEmail());
        model.addAttribute("books", books);
        return "myBooks";
    }

    @PostMapping("/bookRoom")
    public String addNewBook (@RequestParam String email, @RequestParam String userName,
                              @RequestParam String nameOfHotel, @RequestParam int number, HttpSession httpSession,
                              @AuthenticationPrincipal OidcUser oidcUser
                              ) {
        if (oidcUser != null) {
            BookedData bookedData = (BookedData) httpSession.getAttribute("bookedData");
            //this.bookService.registerBook(nameOfHotel, number, userService.toUser(oidcUser), bookedData);
            /**TODO*/
            return "redirect:bookedRoom";
        } else {
            return "redirect:main";
        }
    }

    private void addAttributeForPage(OidcUser user, String country, String town, String nameOfHotel, Star star, int number, RoomCategory category, int price, Model model, HttpSession httpSession) {
        String start = this.bookService.getFormattedDate((LocalDate) httpSession.getAttribute("start"));
        String end = this.bookService.getFormattedDate((LocalDate) httpSession.getAttribute("end"));
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("country", country);
        model.addAttribute("town", town);
        model.addAttribute("nameOfHotel", nameOfHotel);
        model.addAttribute("star", star);
        model.addAttribute("number", number);
        model.addAttribute("category", category);
        model.addAttribute("price", price);

        if(user != null) {
            model.addAttribute("authorized", true);
            model.addAttribute("email", user.getEmail());
            model.addAttribute("userName", user.getFullName());
        } else {
            model.addAttribute("authorized", false);
        }
    }
}
