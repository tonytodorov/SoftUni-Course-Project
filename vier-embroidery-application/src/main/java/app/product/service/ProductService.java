package app.product.service;

import app.category.model.Category;
import app.category.service.CategoryService;
import app.exception.ProductCategoryAlreadyExistException;
import app.product.model.Product;
import app.product.model.ProductCategory;
import app.product.repository.ProductCategoryRepository;
import app.product.repository.ProductRepository;
import app.web.dto.AddCategoryRequest;
import app.web.dto.AddProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final CategoryService categoryService;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.categoryService = categoryService;
    }

    public List<Product> getWomenClothes() {
        return productRepository.findByCategoryName("Women");
    }

    public List<Product> getMensClothes() {
        return productRepository.findByCategoryName("Men");
    }

    public List<Product> getKidsClothes() {
        return productRepository.findByCategoryName("Kid");
    }

    public Product findById(UUID productId) {
        return productRepository.findProductById(productId);
    }

    public List<ProductCategory> getAllProductCategories() {
        return productCategoryRepository.findAll();
    }

    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    public void addCategory(AddCategoryRequest addCategoryRequest) {

        Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findByProductCategory(addCategoryRequest.getProductCategory());

        if (optionalProductCategory.isPresent()) {
            throw new ProductCategoryAlreadyExistException("Product category [%s] already exist.".formatted(addCategoryRequest.getProductCategory()));
        }

        ProductCategory productCategory = ProductCategory.builder()
                .productCategory(addCategoryRequest.getProductCategory())
                .build();

        productCategoryRepository.save(productCategory);
    }

    public void addProduct(AddProductRequest addProductRequest) {

        Product product = Product.builder()
                .description(addProductRequest.getDescription())
                .imageUrl(addProductRequest.getImageUrl())
                .price(addProductRequest.getPrice())
                .category(addProductRequest.getCategory())
                .productCategory(addProductRequest.getProductCategory())
                .build();

        productRepository.save(product);
    }
}
