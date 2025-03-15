package app.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CartItem {

    @NotNull
    private UUID productId;

    @NotNull
    private Integer quantity;
}
