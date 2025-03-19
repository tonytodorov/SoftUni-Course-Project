package app.web;


import app.product.model.Product;
import app.product.service.ProductService;
import app.user.service.UserService;
import app.web.dto.ContactRequest;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class IndexController {

    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public IndexController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/")
    public ModelAndView getIndexPage() {

        List<Product> featuredProducts = productService.getWomenClothes().stream().limit(3).toList();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("featuredProducts", featuredProducts);

        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());

        return modelAndView;
    }

    @PostMapping("/register")
    public String registerNewUser(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.register(registerRequest);

        return "redirect:/login";
    }

    @GetMapping("/women")
    public ModelAndView getWomenPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("women");

        List<Product> womenClothes = productService.getWomenClothes();
        modelAndView.addObject("womenClothes", womenClothes);

        return modelAndView;
    }

    @GetMapping("/mens")
    public ModelAndView getMensPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("mens");

        List<Product> mensClothes = productService.getMensClothes();
        modelAndView.addObject("mensClothes", mensClothes);

        return modelAndView;
    }

    @GetMapping("/kids")
    public ModelAndView getKidsPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("kids");

        List<Product> kidsClothes = productService.getKidsClothes();
        modelAndView.addObject("kidsClothes", kidsClothes);

        return modelAndView;
    }

    @GetMapping("/contact")
    public ModelAndView getContactPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("contact");
        modelAndView.addObject("contactRequest", new ContactRequest());
        
        return modelAndView;
    }

    @PostMapping("/contact")
    public String sendEmail(@Valid ContactRequest contactRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "contact";
        }

        userService.sendEmail(contactRequest);

        return "redirect:/";
    }

    @GetMapping("/about-us")
    public String getAboutPage() {
        return "about-us";
    }

    @GetMapping("/shopping-cart")
    public String getShoppingCartPage() {
        return "shopping-cart";
    }

    @GetMapping("/wishlist")
    public String getWishlistPage() {
        return "wishlist";
    }
}
