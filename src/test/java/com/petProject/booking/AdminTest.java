package com.petProject.booking;

import com.petProject.booking.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "user")
    public void nonAdminCannotDelete() throws Exception {
        mockMvc.perform(delete("/admin/hotel/2/delete").with(csrf())).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "admin")
    public void forbiddenWithoutCSRF() throws Exception {
        mockMvc.perform(delete("/admin/hotel/2/delete")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "admin")
    public void adminPassedSecurityChecked() throws Exception {
        mockMvc.perform(
                delete("/admin/hotel/5/delete").with(csrf())
        ).andExpect(status().isFound());
    }
}
