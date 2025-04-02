package app.web;

import app.order.service.OrderService;
import app.security.AuthenticationDetails;
import app.web.dto.OrderRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ModelAndView getOrderPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("order");
        modelAndView.addObject("orderRequest", new OrderRequest());

        return modelAndView;
    }

    @GetMapping("/order-success")
    public String getOrderSuccessPage() {
        return "order-success";
    }

    @PostMapping
    public String createOrder(@Valid @ModelAttribute("orderRequest") OrderRequest orderRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        if (bindingResult.hasErrors()) {
            return "order";
        }

        UUID userId = authenticationDetails.getId();
        orderService.createOrder(orderRequest, userId);

        return "redirect:/order/order-success";
    }
}
