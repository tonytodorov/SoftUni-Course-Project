package app.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCategoryRequest {

    @NotNull(message = "Field cannot be null.")
    private String productCategory;
}
