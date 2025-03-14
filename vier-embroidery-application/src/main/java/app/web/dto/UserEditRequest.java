package app.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEditRequest {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String address;

}
