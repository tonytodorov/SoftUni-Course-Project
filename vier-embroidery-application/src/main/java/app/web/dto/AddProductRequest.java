package app.web.dto;

import app.category.model.Category;
import app.product.model.ProductCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProductRequest {

    @NotNull
    @Size(min = 5, message = "Description must be at least 5 symbols.")
    private String description;

    @URL
    private String imageUrl;

    @NotNull
    private BigDecimal price;

    private Category category;

    private ProductCategory productCategory;
}
