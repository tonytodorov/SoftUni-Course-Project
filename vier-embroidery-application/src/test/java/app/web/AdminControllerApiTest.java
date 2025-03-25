package app.web;

import app.email.client.dto.EmailResponse;
import app.email.service.EmailService;
import app.order.model.Order;
import app.order.service.OrderService;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@WithMockUser(roles = "ADMIN")
public class AdminControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private EmailService emailService;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRequestToAdminEndpoint_shouldReturnAdminView() throws Exception {

        MockHttpServletRequestBuilder request = get("/admin");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("admin"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void putRequestToChangeRoleEndpoint_shouldChangeUserRole_thenRedirectToAdminPage() throws Exception {

        UUID userId = UUID.randomUUID();

        MockHttpServletRequestBuilder request = put("/admin/{userId}/change-role", userId)
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));
    }

    @Test
    void getRequestToUserEmailsEndpoint_shouldReturnEmailView() throws Exception {

        String email = "test@abv.bg";
        EmailResponse emailResponse = EmailResponse.builder()
                .email(email)
                .build();

        when(emailService.getUserEmails(email)).thenReturn(List.of(emailResponse));

        MockHttpServletRequestBuilder request = get("/admin/{email}/all-emails", email);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user-emails"))
                .andExpect(model().attributeExists("userEmails"));

        verify(emailService, times(1)).getUserEmails(email);
    }

    @Test
    void getRequestToUserOrdersEndpoint_shouldReturnUserOrdersView() throws Exception {

        UUID userId = UUID.randomUUID();
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .build();

        when(orderService.getUserOrders(userId)).thenReturn(List.of(order));

        MockHttpServletRequestBuilder request = get("/admin/{userId}/orders", userId);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user-orders"))
                .andExpect(model().attributeExists("userOrders"));

        verify(orderService, times(1)).getUserOrders(userId);
    }
}
