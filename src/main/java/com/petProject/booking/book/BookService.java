package com.petProject.booking.book;

import com.petProject.booking.user.User;
import com.petProject.booking.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    /**TODO**/
    //диви ти зле написав сортування по датам і ще емеіл додати треба і на фінал щоб авторизація треба була тільки для бронювання

    public List<BookResponse> getBooksByUser (String email) {
        Optional<User> user = this.userRepository.findByEmail(email);

        if (user.isPresent()) {
            ArrayList<BookResponse> result = new ArrayList<>();
            for (Book book: user.get().getBooks()) {
                result.add(BookResponse.builder()
                                .bookedData(book.getBookedData())
                                .room(book.getRoom())
                        .build());
            }
            return result;
        } else {
            return new ArrayList<>();
        }
    }
}
