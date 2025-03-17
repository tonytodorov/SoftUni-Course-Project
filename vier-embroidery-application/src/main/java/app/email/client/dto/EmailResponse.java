package app.email.client.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmailResponse {

    private String title;

    private String email;

    private String message;

    private LocalDateTime sentAt;
}
