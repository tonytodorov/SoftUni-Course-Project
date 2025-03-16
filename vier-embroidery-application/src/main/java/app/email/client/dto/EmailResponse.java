package app.email.client.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EmailResponse {

    private String title;

    private String email;

    private String message;

    private LocalDateTime sentAt;
}
