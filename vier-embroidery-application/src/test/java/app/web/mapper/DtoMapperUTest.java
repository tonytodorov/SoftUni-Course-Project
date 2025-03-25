package app.web.mapper;

import app.user.model.User;
import app.web.dto.UserEditRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DtoMapperUTest {

    @Test
    void givenUser_whenMappingToUserEditRequest_thenCorrectMapping() {

        User user = User.builder()
                .firstName("Test")
                .lastName("Test")
                .phoneNumber("0889128412")
                .address("Plovdiv")
                .build();

        UserEditRequest userEditRequest = DtoMapper.mapToUserEditRequest(user);

        assertEquals("Test", userEditRequest.getFirstName());
        assertEquals("Test", userEditRequest.getLastName());
        assertEquals("0889128412", userEditRequest.getPhoneNumber());
        assertEquals("Plovdiv", userEditRequest.getAddress());
    }
}