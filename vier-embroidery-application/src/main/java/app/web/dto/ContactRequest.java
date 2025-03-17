package app.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContactRequest {

    @NotNull
    @Size(max = 50, message = "Title must be max 50 symbols")
    private String title;

    @Email
    private String email;

    @NotNull
    @Size(max = 800, message = "Message must be max 800 symbols")
    private String message;
}
