package app.user.service;

import app.exception.DomainException;
import app.security.AuthenticationDetails;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
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
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new DomainException("User [%s] does not exist!".formatted(email)));

        return new AuthenticationDetails(user.getId(), email, user.getPassword(), user.getUserRole());
    }

    public User register(RegisterRequest registerRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(registerRequest.getEmail());

        if (optionalUser.isPresent()) {
            throw new DomainException("User with email [%s] already exist!".formatted(registerRequest.getEmail()));
        }

        User user = userRepository.save(initializeUser(registerRequest));

        log.info("User [%s] has been created!".formatted(user.getEmail()));

        return user;
    }

    private User initializeUser(RegisterRequest registerRequest) {
        return User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .userRole(UserRole.USER)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new DomainException("User with id [%s] does not exist.".formatted(userId)));
    }

    public void editProfile(UUID userId, UserEditRequest userEditRequest) {

        User user = getUserById(userId);

        user.setFirstName(userEditRequest.getFirstName());
        user.setLastName(userEditRequest.getLastName());
        user.setPhoneNumber(userEditRequest.getPhoneNumber());
        user.setAddress(userEditRequest.getAddress());

        userRepository.save(user);
    }
}
