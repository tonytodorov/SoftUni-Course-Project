package app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "register";
    }

    @GetMapping("/mens")
    public String getMensPage() {
        return "mens";
    }

    @GetMapping("/women")
    public String getWomenPage() {
        return "women";
    }

    @GetMapping("/kids")
    public String getKidsPage() {
        return "kids";
    }

    @GetMapping("/contact")
    public String getContactPage() {
        return "contact";
    }

    @GetMapping("/about-us")
    public String getAboutPage() {
        return "about-us";
    }

    @GetMapping("/shopping-cart")
    public String getShoppingCartPage() {
        return "shopping-cart";
    }

    @GetMapping("/checkout")
    public String getCheckoutPage() {
        return "checkout";
    }
}
