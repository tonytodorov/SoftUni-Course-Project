package app.email.service;

import app.email.client.EmailClient;
import app.email.client.dto.EmailRequest;
import app.email.client.dto.EmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final EmailClient emailClient;

    @Autowired
    public EmailService(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    public void sendEmail(String title, String email, String body) {

        EmailRequest emailRequest = EmailRequest.builder()
                .title(title)
                .email(email)
                .body(body)
                .build();

        ResponseEntity<EmailResponse> httpResponse;

        try {
            httpResponse = emailClient.sendEmail(emailRequest);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to email-svc failed] Can't send email.");
            }
        } catch (Exception e) {
            log.warn("Can't send email to user due to 500 Internal Server Error.");
        }
    }

}
