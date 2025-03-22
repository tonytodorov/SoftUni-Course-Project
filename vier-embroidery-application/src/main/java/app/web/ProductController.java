package app.web;

import app.category.model.Category;
import app.product.model.ProductCategory;
import app.product.service.ProductService;
import app.web.dto.AddCategoryRequest;
import app.web.dto.AddProductRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String getProductsPage() {
        return "products";
    }

    @GetMapping("/add-product")
    public ModelAndView getAddProductPage() {

        List<Category> categories = productService.getAllCategories();
        List<ProductCategory> productCategoryList = productService.getAllProductCategories();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add-product");
        modelAndView.addObject("addProductRequest", new AddProductRequest());
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("productCategoryList", productCategoryList);

        return modelAndView;
    }

    @PostMapping("/add-product")
    public String addProduct(@Valid AddProductRequest addProductRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "add-product";
        }

        productService.addProduct(addProductRequest);

        return "redirect:/products";
    }

    @GetMapping("/add-category")
    public ModelAndView getAddCategoryPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add-category");
        modelAndView.addObject("addProductCategory", new AddCategoryRequest());

        return modelAndView;
    }

    @PostMapping("/add-category")
    public String addCategory(@Valid AddCategoryRequest addCategoryRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "add-category";
        }

        productService.addCategory(addCategoryRequest);

        return "redirect:/products";
    }
}
