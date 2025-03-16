package app.email.client;

import app.email.client.dto.EmailRequest;
import app.email.client.dto.EmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "email-svc", url = "http://localhost:8081/api/v1/email")
public interface EmailClient {

    @GetMapping("/{email}/all-emails")
    ResponseEntity<List<EmailResponse>> getUserEmails(@PathVariable String email);

    @PostMapping
    ResponseEntity<EmailResponse> sendEmail(@RequestBody EmailRequest emailRequest);
}
