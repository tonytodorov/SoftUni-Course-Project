package app.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEditRequest {

    @NotNull
    @Size(max = 20, message = "First name must be max 20 symbols.")
    private String firstName;

    @NotNull
    @Size(max = 20, message = "Last name must be max 20 symbols.")
    private String lastName;

    @NotNull
    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 symbols.")
    private String phoneNumber;

    @NotNull
    private String address;
}
