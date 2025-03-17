package app.scheduler;

import app.order.model.Order;
import app.order.model.OrderItem;
import app.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderDeleteScheduler {

    private final OrderService orderService;

    @Autowired
    public OrderDeleteScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void cleanUpOldOrders() {

        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(2);

        List<Order> oldOrders = orderService.findByOrderDateBefore(oneYearAgo);

        oldOrders.forEach(order -> {
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(orderService::deleteOrderItem);

            orderService.deleteOrder(order);
        });
    }
}
