package app.product.service;

import app.product.model.Product;
import app.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
}
