package app.order.repository;

import app.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByOrderDateBefore(LocalDateTime oneYearAgo);

    List<Order> findAllByUserId(UUID userId);
}
