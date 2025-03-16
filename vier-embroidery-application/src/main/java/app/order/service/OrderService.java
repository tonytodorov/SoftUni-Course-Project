package app.order.service;

import app.order.model.Order;
import app.order.model.OrderItem;
import app.order.model.OrderStatus;
import app.order.model.PaymentMethod;
import app.order.repository.OrderItemRepository;
import app.order.repository.OrderRepository;
import app.product.model.Product;
import app.product.service.ProductService;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserService userService, ProductService productService, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productService = productService;
        this.orderItemRepository = orderItemRepository;
    }

    public void createOrder(OrderRequest orderRequest, UUID userId) {

        User user = userService.getUserById(userId);

        Order order = Order.builder()
                .firstName(orderRequest.getFirstName())
                .lastName(orderRequest.getLastName())
                .email(orderRequest.getEmail())
                .phoneNumber(orderRequest.getPhoneNumber())
                .city(orderRequest.getCity())
                .address(orderRequest.getAddress())
                .paymentMethod(PaymentMethod.UPON_DELIVERY)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING)
                .user(user)
                .build();

        List<OrderItem> orderItems = orderRequest.getCartItems().stream()
                .map(cartItem -> {
                    Product product = productService.findById(cartItem.getProductId());

                    OrderItem orderItem = OrderItem.builder()
                            .order(order)
                            .product(product)
                            .quantity(cartItem.getQuantity())
                            .price(product.getPrice())
                            .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                            .build();

                    orderItemRepository.save(orderItem);

                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        orderRepository.save(order);
    }
}
