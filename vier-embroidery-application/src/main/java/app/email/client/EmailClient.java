package app.email.client;

import app.email.client.dto.EmailRequest;
import app.email.client.dto.EmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "email-svc", url = "http://localhost:8081/api/v1/email")
public interface EmailClient {

    @PostMapping
    ResponseEntity<EmailResponse> sendEmail(@RequestBody EmailRequest emailRequest);
}
