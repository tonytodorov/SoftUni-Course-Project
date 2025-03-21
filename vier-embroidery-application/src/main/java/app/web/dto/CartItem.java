package app.web.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CartItem {

    private UUID id;

    private Integer quantity;

    private String size;
}
