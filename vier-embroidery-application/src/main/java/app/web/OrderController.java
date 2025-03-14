package app.web;

import app.order.service.OrderService;
import app.web.dto.OrderRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
    public String createOrder(@Valid OrderRequest orderRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "order";
        }

        orderService.createOrder(orderRequest);

        return "redirect:/";
    }


}
