package app.product.model;

import app.category.model.Category;
import app.order.model.Order;
import app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    private Category category;

    @ManyToMany(mappedBy = "products")
    private List<Order> orders = new ArrayList<>();
}
