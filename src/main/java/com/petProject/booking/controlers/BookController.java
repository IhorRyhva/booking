package com.petProject.booking.controlers;

import com.petProject.booking.accommodation.hotel.HotelRepository;
import com.petProject.booking.accommodation.hotel.HotelRequest;
import com.petProject.booking.accommodation.hotel.Location;
import com.petProject.booking.accommodation.hotel.Star;
import com.petProject.booking.accommodation.room.*;
import com.petProject.booking.book.Book;
import com.petProject.booking.book.BookResponse;
import com.petProject.booking.book.BookService;
import com.petProject.booking.user.User;
import com.petProject.booking.user.UserRepository;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    @GetMapping("/bookRoom")
    public String formForBook (@AuthenticationPrincipal OidcUser user, @RequestParam String country,
                               @RequestParam String town,@RequestParam String nameOfHotel,
                               @RequestParam Star star, @RequestParam int number,
                               @RequestParam RoomCategory category, @RequestParam int price, Model model, HttpSession httpSession) {
        model.addAttribute("country", country);
        model.addAttribute("town", town);
        model.addAttribute("nameOfHotel", nameOfHotel);
        model.addAttribute("star", star);
        model.addAttribute("number", number);
        model.addAttribute("category", category);
        model.addAttribute("price", price);
        String start = getFormatedDate((LocalDate) httpSession.getAttribute("start"));
        String end = getFormatedDate((LocalDate) httpSession.getAttribute("end"));
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        if(user != null) {
            model.addAttribute("authorized", true);
            model.addAttribute("email", user.getEmail());
            model.addAttribute("userName", user.getFullName());
        } else {
            model.addAttribute("authorized", false);
        }
        return "bookRoom";
    }

    private String getFormatedDate(LocalDate start) {
        return start.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    @GetMapping("/bookedRoom")
    public String getUsersBookedRooms (@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        if (oidcUser == null) {
            return "redirect:main";
        }

        model.addAttribute("email", oidcUser.getEmail());

        List<BookResponse> books = bookService.getBooksByUser(oidcUser.getEmail());
        model.addAttribute("books", books);
        System.out.println(books.size());
        return "myBooks";
    }

    @PostMapping("/bookRoom")
    public String addNewBook (@RequestParam String email, @RequestParam String userName,
                              @RequestParam String country, @RequestParam String town,@RequestParam String nameOfHotel,
                              @RequestParam Star star, @RequestParam int number,
                              @RequestParam RoomCategory category, @RequestParam int price, HttpSession httpSession
                              ) {
        Optional<User> optionalUser = this.userRepository.findByEmail(email);


        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            registerBook(country, town, nameOfHotel, star, number, category, price, httpSession, user);
        } else {
            User user = User.builder()
                    .email(email)
                    .userName(userName)
                    .role("")
                    .books(new ArrayList<>())
                    .build();
            registerBook(country, town, nameOfHotel, star, number, category, price, httpSession, user);
        }

        return "redirect:bookedRoom";
    }

    private void registerBook(String country, String town, String nameOfHotel, Star star, int number, RoomCategory category, int price, HttpSession httpSession, User user) {
        LocalDate start = (LocalDate) httpSession.getAttribute("start");
        LocalDate end = (LocalDate) httpSession.getAttribute("end");
        Book book = Book.builder()
                .user(user)
                .room(getRoom(price, category, number, nameOfHotel, star, new Location(country, town)))
                .bookedData(new BookedData(start, end))
                .build();
        user.getBooks().add(book);
        addBookedForRoom(nameOfHotel, number, start, end);
        this.userRepository.save(user);
    }

    private void addBookedForRoom(String nameOfHotel, int number, LocalDate start, LocalDate end) {
        Room room = this.roomRepository.findRoomByHotelAndNumber(this.hotelRepository.getHotelByNameOfHotel(nameOfHotel), number);
        room.getBookedData().add(new BookedData(start, end));
        this.roomRepository.save(room);
    }

    private RoomResponse getRoom(int price, RoomCategory category, int number,
                                 String nameOfHotel, Star star, Location location
    ) {
        HotelRequest hotel = HotelRequest.builder()
                .nameOfHotel(nameOfHotel)
                .location(location)
                .star(star)
                .build();
        return RoomResponse.builder()
                .roomCategory(category)
                .number(number)
                .price(price)
                .hotel(hotel)
                .build();
    }
}
