package app.user.service;

import app.email.service.EmailService;
import app.exception.DomainException;
import app.exception.EmailAlreadyExistException;
import app.exception.UserNotExistException;
import app.security.AuthenticationDetails;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.web.dto.ContactRequest;
import app.web.dto.RegisterRequest;
import app.web.dto.UserEditRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User register(RegisterRequest registerRequest) {

        Optional<User> optionalUser = userRepository.findByEmail(registerRequest.getEmail());

        if (optionalUser.isPresent()) {
            throw new EmailAlreadyExistException("Email [%s] already exist!".formatted(registerRequest.getEmail()));
        }

        User user = userRepository.save(initializeUser(registerRequest));

        log.info("User [%s] has been created!".formatted(user.getEmail()));

        return user;
    }

    public void editUserDetails(UUID userId, UserEditRequest userEditRequest) {

        User user = getUserById(userId);

        user.setFirstName(userEditRequest.getFirstName());
        user.setLastName(userEditRequest.getLastName());
        user.setPhoneNumber(userEditRequest.getPhoneNumber());
        user.setAddress(userEditRequest.getAddress());

        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void switchRole(UUID userId) {

        User user = getUserById(userId);

        if (user.getRole() == UserRole.USER) {
            user.setRole(UserRole.ADMIN);
        } else {
            user.setRole(UserRole.USER);
        }

        userRepository.save(user);
    }

    public void sendEmail(ContactRequest contactRequest) {

        String title = contactRequest.getTitle();
        String email = contactRequest.getEmail();
        String message = contactRequest.getMessage();

        emailService.sendEmail(title, email, message);
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotExistException("User with id [%s] does not exist.".formatted(userId)));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new DomainException("User [%s] does not exist!".formatted(email)));

        return new AuthenticationDetails(user.getId(), email, user.getPassword(), user.getRole());
    }

    private User initializeUser(RegisterRequest registerRequest) {
        return User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .createdOn(LocalDateTime.now())
                .build();
    }
}
