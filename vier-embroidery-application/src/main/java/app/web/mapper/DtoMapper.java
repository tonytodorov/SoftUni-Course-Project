package app.web.mapper;

import app.user.model.User;
import app.web.dto.UserEditRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static UserEditRequest mapToUserEditRequest(User user) {
        return UserEditRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .build();
    }
}