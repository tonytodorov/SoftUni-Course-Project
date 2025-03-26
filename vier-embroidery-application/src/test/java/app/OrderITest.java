package app;

import app.order.model.Order;
import app.order.repository.OrderRepository;
import app.order.service.OrderService;
import app.product.model.Product;
import app.product.repository.ProductRepository;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.CartItemRequest;
import app.web.dto.OrderRequest;
import app.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class OrderITest {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void createOrder_shouldCreateOrderSuccessfully() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("test@abv.bg")
                .password("123123")
                .build();

        User user = userService.register(registerRequest);

        Product product = Product.builder()
                .description("Test")
                .imageUrl("https://img.freepik.com/free-photo/embroidered-shirts-bag-hanging_23-2149338938.jpg?semt=ais_hybrid")
                .price(BigDecimal.valueOf(100.00))
                .build();

        productRepository.save(product);

        CartItemRequest cartItem = CartItemRequest.builder()
                .id(product.getId())
                .quantity(2)
                .size("L")
                .build();

        OrderRequest orderRequest = OrderRequest.builder()
                .firstName("Test")
                .lastName("Test")
                .email("test@abv.bg")
                .phoneNumber("0888512291")
                .city("Plovdiv")
                .address("Test")
                .paymentMethod("CASH_ON_DELIVERY")
                .cartItems(List.of(cartItem))
                .build();

        Order createdOrder = orderService.createOrder(orderRequest, user.getId());

        Optional<Order> savedOrder = orderRepository.findById(createdOrder.getId());

        assertTrue(savedOrder.isPresent());
        assertEquals("Test", savedOrder.get().getFirstName());
        assertEquals("Test", savedOrder.get().getLastName());
        assertEquals("test@abv.bg", savedOrder.get().getEmail());
        assertEquals("0888512291", savedOrder.get().getPhoneNumber());
        assertEquals("Plovdiv", savedOrder.get().getCity());
        assertEquals("Test", savedOrder.get().getAddress());
    }
}
