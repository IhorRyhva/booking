package com.petProject.booking;

import com.petProject.booking.hotel.Hotel;
import com.petProject.booking.hotel.Location;
import com.petProject.booking.hotel.Star;
import com.petProject.booking.room.Room;
import com.petProject.booking.room.RoomCategory;
import com.petProject.booking.room.RoomRepository;
import com.petProject.booking.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.TimeZone;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookTest {
    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    RoomRepository roomRepository;

    @MockitoBean
    UserRepository userRepository;

    @BeforeEach
    public void setBookServiceLogic() {
        when(roomRepository.findById(any())).thenReturn(Optional.of(Room.builder()
                .id(5L)
                .hotel(Hotel.builder()
                        .id(5L)
                        .location(Location.builder()
                                .city("Town")
                                .country("Country")
                                .build())
                        .star(Star.FIVE)
                        .build())
                .category(RoomCategory.BASIC)
                .build()));
    }

    @Test
    public void notAuthRedirectTest() throws Exception {
        mockMvc.perform(post("/bookRoom")
                .with(csrf())
                .param("roomId", "5")
        ).andExpect(redirectedUrl("/login"));
    }

}
