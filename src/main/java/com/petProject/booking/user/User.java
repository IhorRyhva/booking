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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq_name", allocationSize = 50)
    private Long id;

    private String userName;

    @Column(unique = true)
    private String email;

    private boolean isBanned = false;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    @ToString.Exclude
    @OrderBy("bookedData.startDate DESC")
    private List<Book> books = new ArrayList<>();

}
