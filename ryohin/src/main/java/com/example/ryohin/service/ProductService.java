package com.example.ryohin.service;
import com.example.ryohin.dto.product.ProductDetail;
import com.example.ryohin.dto.product.ProductListItem;
import com.example.ryohin.entity.Product;
import com.example.ryohin.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductService {
   private final ProductRepository productRepository;
    

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public List<ProductListItem> findAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToListItem)
                .collect(Collectors.toList());
    }
    
    public ProductDetail findProductById(Integer productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        return productOpt.map(this::convertToDetail).orElse(null);
    }
    
    private ProductListItem convertToListItem(Product product) {
        return new ProductListItem(
                product.getProductId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice()
        );
    }
    
    private ProductDetail convertToDetail(Product product) {
        return new ProductDetail(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getMaterial(),
                product.getPrice(),
                product.getImageUrl(),
                product.getStockQuantity()
                
        );
    } 
}
