package app.web;

import app.product.service.ProductService;
import app.web.dto.AddCategoryRequest;
import app.web.dto.AddProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@WithMockUser(roles = "ADMIN")
public class ProductControllerApiTest {

    @MockitoBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRequestToProductsEndpoint_shouldReturnProductsView() throws Exception {

        MockHttpServletRequestBuilder request = get("/products");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("products"));
    }

    @Test
    void getRequestToAddProductEndpoint_shouldReturnAddProductView() throws Exception {

        MockHttpServletRequestBuilder request = get("/products/add-product");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("add-product"))
                .andExpect(model().attributeExists("addProductRequest", "categories", "productCategoryList"));
    }

    @Test
    public void postRequestToAddProductEndpoint_shouldAddProduct_thenRedirectToProductsPage() throws Exception {

        MockHttpServletRequestBuilder request = post("/products/add-product")
                .formField("description", "Test description")
                .formField("price", "49.99")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        verify(productService, times(1)).addProduct(any(AddProductRequest.class));
    }

    @Test
    public void postRequestToAddProductEndpoint_shouldThrowException_whenRequiredFieldMissing() throws Exception {

        MockHttpServletRequestBuilder request = post("/products/add-product")
                .formField("price", "49.99")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("add-product"));

        verify(productService, never()).addProduct(any(AddProductRequest.class));
    }

    @Test
    void getRequestToAddCategoryEndpoint_shouldReturnAddCategoryView() throws Exception {

        MockHttpServletRequestBuilder request = get("/products/add-category");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("add-category"))
                .andExpect(model().attributeExists("addCategoryRequest"));
    }

    @Test
    public void postRequestToAddCategoryEndpoint_shouldAddCategory_thenRedirectToProductsPage() throws Exception {

        MockHttpServletRequestBuilder request = post("/products/add-category")
                .formField("productCategory", "T-shirt")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        verify(productService, times(1)).addCategory(any(AddCategoryRequest.class));
    }

    @Test
    public void postRequestToAddCategoryEndpoint_shouldThrowException_whenRequiredFieldMissing() throws Exception {

        MockHttpServletRequestBuilder request = post("/products/add-category")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("add-category"));

        verify(productService, never()).addCategory(any(AddCategoryRequest.class));
    }
}
