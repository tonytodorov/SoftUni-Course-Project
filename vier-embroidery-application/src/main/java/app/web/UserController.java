package app.web;

import app.security.AuthenticationDetails;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.UserEditRequest;
import app.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getUsersPage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        User user = userService.getUserById(authenticationDetails.getId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/user");
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping("/{userId}/profile")
    public ModelAndView getEditProfilePage(@PathVariable UUID userId) {

        User user = userService.getUserById(userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit-profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("userEditRequest", DtoMapper.mapToUserEditRequest(user));

        return modelAndView;
    }

    @PostMapping("/{userId}/profile")
    public ModelAndView editProfile(@PathVariable UUID userId, @Valid UserEditRequest userEditRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            User user = userService.getUserById(userId);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("edit-profile");
            modelAndView.addObject("user", user);
            modelAndView.addObject("userEditRequest", userEditRequest);

            return modelAndView;
        }

        userService.editProfile(userId, userEditRequest);

        return new ModelAndView("redirect:/user");
    }
}
