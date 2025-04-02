package app.order;

import app.order.model.Order;
import app.order.model.OrderItem;
import app.order.model.PaymentMethod;
import app.order.repository.OrderItemRepository;
import app.order.repository.OrderRepository;
import app.order.service.OrderService;
import app.product.model.Product;
import app.product.service.ProductService;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.CartItemRequest;
import app.web.dto.OrderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceUTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void givenOrderRequest_whenCreateOrder_thenSaveOrderAndItems() {

        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        User user = User.builder().build();

        OrderRequest orderRequest = OrderRequest.builder()
                .firstName("Test")
                .lastName("Test")
                .email("test@abv.bg")
                .phoneNumber("0889102394")
                .city("Plovdiv")
                .address("Test")
                .paymentMethod(PaymentMethod.CASH_ON_DELIVERY)
                .build();

        Product product = Product.builder()
                .id(productId)
                .description("T-shirt")
                .price(BigDecimal.valueOf(49.99))
                .build();

        CartItemRequest cartItemRequest = CartItemRequest.builder()
                .id(productId)
                .quantity(2)
                .size("L")
                .build();

        orderRequest.setCartItems(List.of((cartItemRequest)));

        Order order = Order.builder().build();

        when(userService.getUserById(userId)).thenReturn(user);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(productService.findById(product.getId())).thenReturn(product);

        orderService.createOrder(orderRequest, userId);

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).saveAll(anyList());
    }

    @Test
    void givenDate_whenFindByOrderDateBefore_thenReturnOrders() {

        Order order = Order.builder()
                .firstName("Test")
                .lastName("Test")
                .phoneNumber("0889102394")
                .city("Plovdiv")
                .build();

        LocalDateTime twoYearsAgo = LocalDateTime.now().minusYears(2);
        List<Order> expectedOrders = List.of(order);

        when(orderRepository.findByOrderDateBefore(twoYearsAgo)).thenReturn(expectedOrders);

        List<Order> actualOrders = orderService.findByOrderDateBefore(twoYearsAgo);

        assertEquals(expectedOrders, actualOrders);
        verify(orderRepository, times(1)).findByOrderDateBefore(twoYearsAgo);
    }

    @Test
    void givenOrderItem_whenDeleteOrderItem_thenDeleteIt() {

        OrderItem orderItem = OrderItem.builder()
                .id(UUID.randomUUID())
                .productCategory(any())
                .totalPrice(BigDecimal.valueOf(49.99))
                .build();

        orderService.deleteOrderItem(orderItem);

        verify(orderItemRepository, times(1)).delete(orderItem);
    }

    @Test
    void givenOrder_whenDeleteOrder_thenDeleteIt() {

        Order order = Order.builder()
                .firstName("Test")
                .lastName("Test")
                .phoneNumber("0889102394")
                .city("Plovdiv")
                .build();

        orderService.deleteOrder(order);

        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void givenUserId_whenGetUserOrders_thenReturnUserOrders() {

        UUID userId = UUID.randomUUID();

        Order order = Order.builder()
                .firstName("Test")
                .lastName("Test")
                .city("Plovdiv")
                .build();

        List<Order> expectedOrders = List.of(order);

        when(orderRepository.findAllByUserId(userId)).thenReturn(expectedOrders);

        List<Order> actualOrders = orderService.getUserOrders(userId);

        assertEquals(expectedOrders, actualOrders);
        verify(orderRepository, times(1)).findAllByUserId(userId);
    }
}
