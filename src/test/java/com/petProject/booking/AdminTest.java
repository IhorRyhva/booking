package com.petProject.booking;

import com.petProject.booking.hotel.AdminService;
import com.petProject.booking.hotel.Hotel;
import com.petProject.booking.hotel.HotelRepository;
import com.petProject.booking.hotel.Star;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.TimeZone;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AdminService adminService;

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @BeforeEach
    void setAdminServiceBehaviour() {
        when(adminService.removeHotel(anyLong())).thenReturn(true);
    }

    @Test
    public void nonAdminCannotDelete() throws Exception {
        mockMvc.perform(post("/admin/hotel/2/delete")
                        .with(csrf())
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("user")))
                ).andExpect(status().isForbidden());
    }

    @Test
    public void forbiddenWithoutCSRF() throws Exception {
        mockMvc.perform(post("/admin/hotel/2/delete")).andExpect(status().isForbidden());
    }

    @Test
    public void adminPassedSecurityChecked() throws Exception {
        mockMvc.perform(
                post("/admin/hotel/delete")
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("admin")))
                        .with(csrf())
                        .param("id", "5")
        ).andExpect(status().isFound());
    }
}
