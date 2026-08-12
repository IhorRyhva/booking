package com.petProject.booking.booking;

import com.petProject.booking.common.exception.IncorrectBookTimeException;
import com.petProject.booking.hotel.Hotel;
import com.petProject.booking.room.*;
import com.petProject.booking.room.dto.BookedData;
import com.petProject.booking.user.UserService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final UserService userService;

    @GetMapping("/bookRoom")
    public String formForBook (@AuthenticationPrincipal OidcUser user, @RequestParam long roomId,
                               Model model, @RequestParam LocalDate start, @RequestParam LocalDate end) {
        this.addAttributeForPage(user, roomId, model, start, end);
        return "bookRoom";
    }

    @GetMapping("/bookedRoom")
    public String getUsersBookedRooms (Model model, @AuthenticationPrincipal OidcUser oidcUser) {
        model.addAttribute("email", oidcUser.getEmail());
        List<Book> books = bookService.getBooksByUser(oidcUser.getEmail());
        model.addAttribute("books", books);
        return "myBooks";
    }

    @PostMapping("/bookRoom")
    public String addNewBook (@ModelAttribute @Valid BookDTO bookDTO,
                              BindingResult bindingResult,
                              @AuthenticationPrincipal OidcUser oidcUser, Model model
                              ) throws IncorrectBookTimeException {
        if (bindingResult.hasErrors()) {
            this.addAttributeForPage(oidcUser, bookDTO.getRoomId(), model, bookDTO.getStart(), bookDTO.getEnd());
            return "bookRoom";
        }
        this.userService.addUser(oidcUser);
        BookedData bookedData = new BookedData(bookDTO.getStart(), bookDTO.getEnd());
        this.bookService.registerBook(oidcUser.getEmail(), bookedData, bookDTO.getRoomId());
        return "redirect:bookedRoom";
    }

    private void addAttributeForPage(OidcUser user, long id, Model model, LocalDate startDate, LocalDate endDate) {
        String start = this.bookService.getFormattedDate(startDate);
        String end = this.bookService.getFormattedDate(endDate);
        model.addAttribute("startString", start);
        model.addAttribute("endString", end);
        model.addAttribute("start", startDate);
        model.addAttribute("end", endDate);
        model.addAttribute("roomId", id);
        Room room = this.bookService.getRoom(id);
        Hotel hotel = room.getHotel();
        model.addAttribute("country", hotel.getLocation().country());
        model.addAttribute("town", hotel.getLocation().city());
        model.addAttribute("nameOfHotel", hotel.getNameOfHotel());
        model.addAttribute("star", hotel.getStar());
        model.addAttribute("number", room.getNumber());
        model.addAttribute("category", room.getCategory());
        model.addAttribute("price", room.getPrice());

        model.addAttribute("authorized", true);
        model.addAttribute("email", user.getEmail());
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("userName", user.getFullName());

    }
    

}
