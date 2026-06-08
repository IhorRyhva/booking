package com.petProject.booking.user;

import com.petProject.booking.booking.Book;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Column(unique = true)
    private String email;

    private boolean isBaned = false;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    @ToString.Exclude
    @OrderBy("bookedData.start DESC")
    private List<Book> books = new ArrayList<>();
}
