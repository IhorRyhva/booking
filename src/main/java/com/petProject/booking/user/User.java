package com.petProject.booking.user;

import com.petProject.booking.book.Book;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String userName;

    private String role;

    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @ToString.Exclude
    @OrderBy("bookedData.start DESC")
    private List<Book> books = new ArrayList<>();
}
