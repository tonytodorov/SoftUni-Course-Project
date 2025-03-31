package app.web;

import app.exception.EmailAlreadyExistException;
import app.exception.ProductCategoryAlreadyExistException;
import app.exception.UserNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistException.class)
    public String handleEmailAlreadyExist(RedirectAttributes redirectAttributes, EmailAlreadyExistException exception) {

        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute("emailAlreadyExistMessage", message);
        return "redirect:/register";
    }

    @ExceptionHandler(ProductCategoryAlreadyExistException.class)
    public String handleProductCategoryAlreadyExist(RedirectAttributes redirectAttributes, ProductCategoryAlreadyExistException exception) {

        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute("productCategoryAlreadyExistMessage", message);
        return "redirect:/products/add-category";
    }

    @ExceptionHandler(UserNotExistException.class)
    public String handleUserNotFound(RedirectAttributes redirectAttributes, UserNotExistException exception) {

        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute("userNotExistMessage", message);
        return "redirect:/edit-profile";
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(Exception exception) {
        return new ModelAndView("access-denied");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            NoResourceFoundException.class,
            MethodArgumentTypeMismatchException.class,
            MissingRequestValueException.class
    })
    public ModelAndView handleNotFoundExceptions(Exception exception) {
        return new ModelAndView("not-found");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception exception) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("internal-server-error");
        modelAndView.addObject("errorMessage", exception.getClass().getSimpleName());

        return modelAndView;
    }
}