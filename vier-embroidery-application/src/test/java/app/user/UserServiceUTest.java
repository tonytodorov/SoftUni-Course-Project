package app.user;

import app.email.service.EmailService;
import app.exception.DomainException;
import app.exception.EmailAlreadyExistException;
import app.security.AuthenticationDetails;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.ContactRequest;
import app.web.dto.RegisterRequest;
import app.web.dto.UserEditRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    @Test
    void givenExistingUsersInDatabase_whenGetAllUsers_thenReturnThemAll() {

        List<User> userList = List.of(new User(), new User(), new User());
        when(userRepository.findAll()).thenReturn(userList);

        List<User> users = userService.getAllUsers();

        assertThat(users).hasSize(3);
    }

    @Test
    void givenExistingUsername_whenRegister_thenExceptionIsThrown() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("test@abv.bg")
                .password("test")
                .build();

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistException.class, () -> userService.register(registerRequest));

        verify(userRepository, never()).save(any());
    }

    @Test
    void givenExistingUsername_whenRegister_thenRegisterUser() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("test@abv.bg")
                .password("test")
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .email(registerRequest.getEmail())
                .build();

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);

        User registeredUser = userService.register(registerRequest);

        assertEquals(registeredUser.getEmail(), user.getEmail());
        assertEquals(registeredUser.getPassword(), passwordEncoder.encode(user.getPassword()));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void givenMissingUserFromDatabase_whenEditUserProfile_thenExceptionIsThrown() {

        UUID userId = UUID.randomUUID();
        UserEditRequest userEditRequest = UserEditRequest.builder().build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> userService.editUserDetails(userId, userEditRequest));
    }

    @Test
    void givenUserFromDatabase_whenEditUserProfile_thenChangeUserDetails() {

        UUID userId = UUID.randomUUID();

        UserEditRequest userEditRequest = UserEditRequest.builder()
                .firstName("Test")
                .lastName("Data")
                .phoneNumber("0889140201")
                .address("Plovdiv")
                .build();

        User user = User.builder().build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.editUserDetails(userId, userEditRequest);

        assertEquals(userEditRequest.getFirstName(), user.getFirstName());
        assertEquals(userEditRequest.getLastName(), user.getLastName());
        assertEquals(userEditRequest.getPhoneNumber(), user.getPhoneNumber());
        assertEquals(userEditRequest.getAddress(), user.getAddress());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUserWithAdminRole_whenSwitchRole_thenUserReceivesUserRole() {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .role(UserRole.ADMIN)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.switchRole(userId);

        assertThat(user.getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void givenUserWithUserRole_whenSwitchRole_thenUserReceivesAdminRole() {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .role(UserRole.USER)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.switchRole(userId);

        assertThat(user.getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    void givenValidContactRequest_whenSendEmail_thenEmailServiceIsCalled() {

        ContactRequest contactRequest = ContactRequest.builder()
                .title("Test")
                .email("test@abv.bg")
                .message("This is a test message")
                .build();

        assertDoesNotThrow(() -> userService.sendEmail(contactRequest));

        verify(emailService, times(1)).sendEmail(contactRequest.getTitle(), contactRequest.getEmail(), contactRequest.getMessage());
    }

    @Test
    void givenExistingUser_whenLoadUserByUsername_thenReturnCorrectAuthenticationMetadata() {

        String email = "test@abv.bg";

        User user = User.builder()
                .id(UUID.randomUUID())
                .password("test")
                .role(UserRole.ADMIN)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails authenticationMetadata = userService.loadUserByUsername(email);

        assertInstanceOf(AuthenticationDetails.class, authenticationMetadata);
        AuthenticationDetails result = (AuthenticationDetails) authenticationMetadata;

        assertEquals(user.getId(), result.getId());
        assertEquals(email, result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getRole(), result.getUserRole());
        assertThat(result.getAuthorities()).hasSize(1);
    }
}
