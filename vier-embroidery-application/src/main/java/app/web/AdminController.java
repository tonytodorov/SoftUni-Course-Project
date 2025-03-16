package app.web;

import app.email.client.dto.EmailResponse;
import app.email.service.EmailService;
import app.security.AuthenticationDetails;
import app.user.model.User;
import app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public AdminController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping
    public ModelAndView getAdminPage() {

        List<User> users = userService.getAllUsers();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        modelAndView.addObject("users", users);

        return modelAndView;
    }

    @PutMapping("/{userId}/change-role")
    public String changeRole(@PathVariable UUID userId) {

        userService.switchRole(userId);

        return "redirect:/admin";
    }

    @GetMapping("/{email}/all-emails")
    public ModelAndView getUserEmails(@PathVariable String email) {

        List<EmailResponse> userEmails = emailService.getUserEmails(email);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userEmails", userEmails);
        modelAndView.setViewName("email");

        return modelAndView;
    }
}
