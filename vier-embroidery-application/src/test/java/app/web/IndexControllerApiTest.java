package app.web;

import app.exception.EmailAlreadyExistException;
import app.product.service.ProductService;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(IndexController.class)
public class IndexControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRequestToIndexEndpoint_shouldReturnIndexView() throws Exception {

        MockHttpServletRequestBuilder request = get("/");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("featuredProducts"));
    }

    @Test
    void getRequestToLoginEndpoint_shouldReturnLoginView() throws Exception {

        MockHttpServletRequestBuilder request = get("/login");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest"));
    }

    @Test
    void getRequestToRegisterEndpoint_shouldReturnRegisterView() throws Exception {

        MockHttpServletRequestBuilder request = get("/register");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registerRequest"));
    }

    @Test
    void postRequestToRegisterEndpoint_shouldRegisterUser_thenRedirectToLoginPage() throws Exception {

        MockHttpServletRequestBuilder request = post("/register")
                .formField("email", "test@abv.bg")
                .formField("password", "123123")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService, times(1)).register(any());
    }

    @Test
    void postRequestToRegisterEndpointWhenEmailAlreadyExist_thenRedirectToRegisterWithFlashParameter() throws Exception {

        when(userService.register(any())).thenThrow(new EmailAlreadyExistException("Email already exist!"));

        MockHttpServletRequestBuilder request = post("/register")
                .formField("email", "test@abv.bg")
                .formField("password", "123456")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("emailAlreadyExistMessage"));

        verify(userService, times(1)).register(any());
    }

    @Test
    void getRequestToWomensEndpoint_shouldReturnWomensView() throws Exception {

        MockHttpServletRequestBuilder request = get("/womens");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("womens"))
                .andExpect(model().attributeExists("womenClothes"));
    }

    @Test
    void getRequestToMensEndpoint_shouldReturnMensView() throws Exception {

        MockHttpServletRequestBuilder request = get("/mens");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("mens"))
                .andExpect(model().attributeExists("mensClothes"));
    }

    @Test
    void getRequestToKidsEndpoint_shouldReturnKidsView() throws Exception {

        MockHttpServletRequestBuilder request = get("/kids");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("kids"))
                .andExpect(model().attributeExists("kidsClothes"));
    }

    @Test
    void getRequestToContactEndpoint_shouldReturnContactView() throws Exception {

        MockHttpServletRequestBuilder request = get("/contact");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeExists("contactRequest"));
    }

    @Test
    void postRequestToContactEndpoint_shouldSendEmail_thenRedirectToIndexPage() throws Exception {

        MockHttpServletRequestBuilder request = post("/contact")
                .formField("title", "Test title")
                .formField("email", "test@abv.bg")
                .formField("message", "Test message")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(userService, times(1)).sendEmail(any());
    }

    @Test
    void getRequestToAboutUsEndpoint_shouldReturnAboutUsView() throws Exception {

        MockHttpServletRequestBuilder request = get("/about-us");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("about-us"));
    }

    @Test
    @WithMockUser
    void getRequestToShoppingCartEndpoint_shouldReturnShoppingCartView() throws Exception {

        MockHttpServletRequestBuilder request = get("/shopping-cart");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("shopping-cart"));
    }

    @Test
    void getRequestToWishlistEndpoint_shouldReturnWishlistView() throws Exception {

        MockHttpServletRequestBuilder request = get("/wishlist");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("wishlist"));
    }
}
