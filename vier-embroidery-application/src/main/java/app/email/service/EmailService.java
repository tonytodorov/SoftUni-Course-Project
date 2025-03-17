package app.email.service;

import app.email.client.EmailClient;
import app.email.client.dto.EmailRequest;
import app.email.client.dto.EmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class EmailService {

    private final EmailClient emailClient;

    @Autowired
    public EmailService(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    public List<EmailResponse> getUserEmails(String email) {

        try {
            ResponseEntity<List<EmailResponse>> response = emailClient.getUserEmails(email);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                log.warn("Failed to fetch emails. Response status: {}", response.getStatusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            log.warn("Failed to fetch emails. The reason is: %s".formatted(e.getMessage()));
            return Collections.emptyList();
        }
    }

    public void sendEmail(String emailTitle, String userEmail, String emailMessage) {

        EmailRequest emailRequest = EmailRequest.builder()
                .title(emailTitle)
                .email(userEmail)
                .message(emailMessage)
                .build();

        ResponseEntity<EmailResponse> httpResponse;

        try {
            httpResponse = emailClient.sendEmail(emailRequest);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error("Email send successfully.");
            }
        } catch (Exception e) {
            log.warn("Can't send email to user due to 500 Internal Server Error.");
        }
    }

}
