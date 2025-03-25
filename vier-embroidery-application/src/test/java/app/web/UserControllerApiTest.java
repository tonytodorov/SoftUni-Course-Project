package app.web;

import app.security.AuthenticationDetails;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.service.UserService;
import app.web.dto.UserEditRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerApiTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRequestToUserEndpoint_shouldReturnUserView() throws Exception {

        UUID userId = UUID.randomUUID();
        AuthenticationDetails principal = new AuthenticationDetails(userId,
                "test@abv.bg",
                "123123",
                UserRole.ADMIN);

        User user = User.builder()
                .id(userId)
                .email("test@abv.bg")
                .build();

        when(userService.getUserById(userId)).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/user")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user"))
                .andExpect(model().attributeExists("user"));

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @WithMockUser
    void getRequestToEditProfilePage_shouldReturnEditProfileView() throws Exception {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .email("test@abv.bg")
                .build();

        when(userService.getUserById(userId)).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/user/" + userId + "/profile");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("edit-profile"))
                .andExpect(model().attributeExists("user", "userEditRequest"));

        verify(userService, times(1)).getUserById(userId);
    }
}
