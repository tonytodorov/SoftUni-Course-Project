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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PostMapping
    public String createOrder(@Valid @RequestBody OrderRequest orderRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        UUID userId = authenticationDetails.getId();
        log.info("Order!");

        if (bindingResult.hasErrors()) {
            return "order";
        }

        orderService.createOrder(orderRequest, userId);

        return "redirect:/";
    }


}
