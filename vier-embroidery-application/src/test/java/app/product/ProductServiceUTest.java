package app.product;

import app.category.model.Category;
import app.category.service.CategoryService;
import app.exception.ProductCategoryAlreadyExistException;
import app.product.model.Product;
import app.product.model.ProductCategory;
import app.product.repository.ProductCategoryRepository;
import app.product.repository.ProductRepository;
import app.product.service.ProductService;
import app.web.dto.AddCategoryRequest;
import app.web.dto.AddProductRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    @Test
    void givenWomenCategory_whenGetWomenClothes_thenReturnProductList() {

        Product product = Product.builder()
                .description("Shirt")
                .price(BigDecimal.valueOf(109.99))
                .build();

        List<Product> expectedProducts = List.of(product);

        when(productRepository.findByCategoryName("Women")).thenReturn(expectedProducts);

        List<Product> actualProducts = productService.getWomenClothes();

        assertEquals(expectedProducts, actualProducts);
        verify(productRepository, times(1)).findByCategoryName("Women");
    }

    @Test
    void givenMenCategory_whenGetMenClothes_thenReturnProductList() {

        Product product = Product.builder()
                .description("T-shirt")
                .price(BigDecimal.valueOf(49.99))
                .build();

        List<Product> expectedProducts = List.of(product);

        when(productRepository.findByCategoryName("Men")).thenReturn(expectedProducts);

        List<Product> actualProducts = productService.getMensClothes();

        assertEquals(expectedProducts, actualProducts);
        verify(productRepository, times(1)).findByCategoryName("Men");
    }

    @Test
    void givenKidCategory_whenGetKidClothes_thenReturnProductList() {

        Product product = Product.builder()
                .description("Blouse")
                .price(BigDecimal.valueOf(59.99))
                .build();

        List<Product> expectedProducts = List.of(product);

        when(productRepository.findByCategoryName("Kid")).thenReturn(expectedProducts);

        List<Product> actualProducts = productService.getKidsClothes();

        assertEquals(expectedProducts, actualProducts);
        verify(productRepository, times(1)).findByCategoryName("Kid");
    }

    @Test
    void givenProductId_whenFindById_thenReturnProduct() {

        UUID productId = UUID.randomUUID();

        Product product = Product.builder()
                .description("Coat")
                .price(BigDecimal.valueOf(399.99))
                .build();

        when(productRepository.findProductById(productId)).thenReturn(product);

        Product foundProduct = productService.findById(productId);

        assertEquals(product, foundProduct);
        verify(productRepository, times(1)).findProductById(productId);
    }

    @Test
    void whenGetAllProductCategories_thenReturnProductCategoryList() {

        ProductCategory productCategory = ProductCategory.builder()
                .productCategory("Jacket")
                .build();

        List<ProductCategory> expectedCategories = List.of(productCategory);

        when(productCategoryRepository.findAll()).thenReturn(expectedCategories);

        List<ProductCategory> actualCategories = productService.getAllProductCategories();

        assertEquals(expectedCategories, actualCategories);
        verify(productCategoryRepository, times(1)).findAll();
    }

    @Test
    void whenGetAllCategories_thenReturnCategoryList() {

        Category category = Category.builder()
                .name("Others")
                .build();

        List<Category> expectedCategories = List.of(category);

        when(categoryService.getAllCategories()).thenReturn(expectedCategories);

        List<Category> actualCategories = productService.getAllCategories();

        assertEquals(expectedCategories, actualCategories);
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void givenExistingCategory_whenAddCategory_thenThrowException() {

        String productCategoryName = "Jacket";

        AddCategoryRequest addCategoryRequest = AddCategoryRequest.builder()
                .productCategory(productCategoryName)
                .build();

        ProductCategory productCategory = ProductCategory.builder()
                .productCategory(productCategoryName)
                .build();

        when(productCategoryRepository.findByProductCategory(productCategoryName)).thenReturn(Optional.of(productCategory));

        assertThrows(ProductCategoryAlreadyExistException.class, () -> productService.addCategory(addCategoryRequest));
        verify(productCategoryRepository, times(1)).findByProductCategory(productCategoryName);
        verify(productCategoryRepository, never()).save(any());
    }

    @Test
    void givenNewCategory_whenAddCategory_thenSaveSuccessfully() {

        String productCategoryName = "Skirt";

        AddCategoryRequest addCategoryRequest = AddCategoryRequest.builder()
                .productCategory(productCategoryName)
                .build();

        when(productCategoryRepository.findByProductCategory(productCategoryName)).thenReturn(Optional.empty());

        productService.addCategory(addCategoryRequest);

        assertEquals("Skirt", addCategoryRequest.getProductCategory());
        verify(productCategoryRepository, times(1)).save(any(ProductCategory.class));
    }

    @Test
    void givenProductRequest_whenAddProduct_thenSaveSuccessfully() {

        AddProductRequest addProductRequest = AddProductRequest.builder()
                .description("Jeans")
                .price(BigDecimal.valueOf(159.99))
                .build();

        productService.addProduct(addProductRequest);

        assertEquals("Jeans", addProductRequest.getDescription());
        assertEquals(BigDecimal.valueOf(159.99), addProductRequest.getPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

}
