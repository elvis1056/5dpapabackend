package com.fivepapa.backend.ecommerce.service;

import com.fivepapa.backend.ecommerce.dto.ProductRequest;
import com.fivepapa.backend.ecommerce.dto.ProductResponse;
import com.fivepapa.backend.ecommerce.entity.Product;
import com.fivepapa.backend.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 建立商品
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setActive(request.getActive());

        Product saved = productRepository.save(product);
        return convertToResponse(saved);
    }

    // 查詢所有商品
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 查詢單一商品
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在，ID: " + id));
        return convertToResponse(product);
    }

    // 更新商品
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在，ID: " + id));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setActive(request.getActive());

        Product updated = productRepository.save(product);
        return convertToResponse(updated);
    }

    // 刪除商品
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("商品不存在，ID: " + id);
        }
        productRepository.deleteById(id);
    }

    // 搜尋商品
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.findByNameContaining(keyword).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 查詢上架商品
    public List<ProductResponse> getActiveProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Entity 轉 Response
    private ProductResponse convertToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl(),
                product.getActive(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
