package app.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @NotNull
    @Size(max = 20, message = "First name must be max 20 symbols")
    private String firstName;

    @NotNull
    @Size(max = 20, message = "Last name must be max 20 symbols")
    private String lastName;

    @Email
    private String email;

    @NotNull
    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 symbols")
    private String phoneNumber;

    @NotNull
    @Size(max = 50, message = "City must be max 50 symbols")
    private String city;

    @NotNull
    @Size(max = 200, message = "Address must be max 200 symbols")
    private String address;

    @NotNull
    private String paymentMethod;

    @NotNull
    private List<CartItem> cartItems;

}
