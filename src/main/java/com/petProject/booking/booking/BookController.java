package com.petProject.booking.booking;

import com.petProject.booking.room.*;
import com.petProject.booking.room.dto.BookedData;
import com.petProject.booking.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;
@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final UserService userService;

    @GetMapping("/bookRoom")
    public String formForBook (@AuthenticationPrincipal OidcUser user, @ModelAttribute SortDTO sortDTO, Model model, HttpSession httpSession) {
        this.addAttributeForPage(user, sortDTO, model, httpSession);
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
    public String addNewBook (@ModelAttribute("bookDTO") @Valid BookDTO bookDTO, @ModelAttribute SortDTO sortDTO,
                              BindingResult bindingResult, HttpSession httpSession,
                              @AuthenticationPrincipal OidcUser oidcUser, Model model
                              ) {
        if (bindingResult.hasErrors()) {
            this.addAttributeForPage(oidcUser, sortDTO, model, httpSession);
            model.addAttribute("bookDTO", bookDTO);
            return "bookRoom";
        }
        BookedData bookedData = (BookedData) httpSession.getAttribute("bookedData");
        if (oidcUser != null) {
            this.bookService.registerBook(bookDTO.getNameOfHotel(), bookDTO.getNumber(), userService.addUser(oidcUser), bookedData);
            return "redirect:bookedRoom";
        } else {
            if (bookDTO.getEmail() == null || bookDTO.getEmail().isBlank()) {
                this.addAttributeForPage(null, sortDTO, model, httpSession);
                model.addAttribute("bookDTO", bookDTO);
                return "bookRoom";
            }
            /**TODO*Add register book logic for unauth user*/
            this.bookService.registerBook(bookDTO.getNameOfHotel(), bookDTO.getNumber(), null, bookedData);
            /**TODO* change redirect and add email service */
            this.addAttributeForPage(null, sortDTO, model, httpSession);
            model.addAttribute("bookDTO", bookDTO);
            return "bookRoom";
        }
    }

    private void addAttributeForPage(OidcUser user, SortDTO sortDTO,Model model, HttpSession httpSession) {
        String start = this.bookService.getFormattedDate((LocalDate) httpSession.getAttribute("start"));
        String end = this.bookService.getFormattedDate((LocalDate) httpSession.getAttribute("end"));
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("country", sortDTO.getCountry());
        model.addAttribute("town", sortDTO.getTown());
        model.addAttribute("nameOfHotel", sortDTO.getNameOfHotel());
        model.addAttribute("star", sortDTO.getStar());
        model.addAttribute("number", sortDTO.getNumber());
        model.addAttribute("category", sortDTO.getCategory());
        model.addAttribute("price", sortDTO.getPrice());

        if(user != null) {
            model.addAttribute("authorized", true);
            model.addAttribute("bookDTO", new BookDTO(user.getEmail(), user.getFullName()));
        } else {
            model.addAttribute("authorized", false);
        }
    }
    

}
