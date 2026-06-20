package com.petProject.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void nonAdminCannotDelete() throws Exception {
        mockMvc.perform(delete("/admin/hotel/2/delete")
                        .with(csrf())
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("user")))
                ).andExpect(status().isForbidden());
    }

    @Test
    public void forbiddenWithoutCSRF() throws Exception {
        mockMvc.perform(delete("/admin/hotel/2/delete")).andExpect(status().isForbidden());
    }

    @Test
    public void adminPassedSecurityChecked() throws Exception {
        mockMvc.perform(
                delete("/admin/hotel/5/delete")
                        .with(oidcLogin().authorities(new SimpleGrantedAuthority("admin")))
                        .with(csrf())
        ).andExpect(status().isFound());
    }
}
