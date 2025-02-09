package app.user.model;

import app.order.model.Order;
import app.paymentCard.model.PaymentCard;
import app.product.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String phoneNumber;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<PaymentCard> paymentCards = new ArrayList<>();
}
