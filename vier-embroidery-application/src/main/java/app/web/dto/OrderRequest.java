package app.web.dto;

import app.order.model.PaymentMethod;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotNull
    @Size(max = 20, message = "First name must be max 20 symbols.")
    private String firstName;

    @NotNull
    @Size(max = 20, message = "Last name must be max 20 symbols.")
    private String lastName;

    @Email
    private String email;

    @NotNull
    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 symbols.")
    private String phoneNumber;

    @NotNull
    @Size(max = 30, message = "City must be max 30 symbols.")
    private String city;

    @NotNull
    @Size(max = 100, message = "Address must be max 100 symbols.")
    private String address;

    private PaymentMethod paymentMethod;

    @NotNull
    private List<CartItemRequest> cartItems;

}
