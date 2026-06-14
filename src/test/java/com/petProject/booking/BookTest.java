package com.petProject.booking;

import com.petProject.booking.booking.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class BookTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BookService bookService;

    @Test
    public void cannotBeNullEmail() throws Exception {
        mockMvc.perform(post("/bookRoom").with(csrf())
                .param("userName", "Pepe")
                .param("nameOfHotel", "Hotel")
                .param("number", "5")
        ).andExpect(status().isOk())
                .andExpect(view().name("bookRoom"));
        verify(bookService, never()).registerBook(any(), anyInt(), any(), any());
    }

    @Test
    public void cannotBeNullEmpty() throws Exception {
        mockMvc.perform(post("/bookRoom").with(csrf())
                .param("userName", "Pepe")
                .param("nameOfHotel", "Hotel")
                .param("number", "5")
                .param("email", "")
        ).andExpect(status().isOk())
                .andExpect(view().name("bookRoom"));
        verify(bookService, never()).registerBook(any(), anyInt(), any(), any());
    }
}
