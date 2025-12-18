package com.fivepapa.backend.ecommerce.service;

import com.fivepapa.backend.ecommerce.dto.AddToCartRequest;
import com.fivepapa.backend.ecommerce.dto.CartItemResponse;
import com.fivepapa.backend.ecommerce.dto.CartResponse;
import com.fivepapa.backend.ecommerce.dto.UpdateCartItemRequest;
import com.fivepapa.backend.ecommerce.entity.Cart;
import com.fivepapa.backend.ecommerce.entity.CartItem;
import com.fivepapa.backend.ecommerce.entity.Product;
import com.fivepapa.backend.ecommerce.repository.CartItemRepository;
import com.fivepapa.backend.ecommerce.repository.CartRepository;
import com.fivepapa.backend.ecommerce.repository.ProductRepository;
import com.fivepapa.backend.member.entity.User;
import com.fivepapa.backend.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /**
     * 查詢用戶購物車
     */
    @Transactional
    public CartResponse getCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return convertToResponse(cart);
    }

    /**
     * 加入商品到購物車
     */
    @Transactional
    public CartResponse addToCart(Long userId, AddToCartRequest request) {
        // 1. 獲取或創建購物車
        Cart cart = getOrCreateCart(userId);

        // 2. 檢查商品是否存在且上架
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("商品不存在，ID: " + request.getProductId()));

        if (!product.getActive()) {
            throw new RuntimeException("商品已下架");
        }

        // 3. 檢查庫存
        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("庫存不足，目前庫存：" + product.getStock());
        }

        // 4. 檢查購物車中是否已有該商品
        CartItem existingItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);

        if (existingItem != null) {
            // 已存在，累加數量
            int newQuantity = existingItem.getQuantity() + request.getQuantity();

            // 檢查累加後是否超過庫存
            if (product.getStock() < newQuantity) {
                throw new RuntimeException("庫存不足，目前庫存：" + product.getStock() +
                        "，購物車已有：" + existingItem.getQuantity());
            }

            existingItem.setQuantity(newQuantity);
            cartItemRepository.save(existingItem);
        } else {
            // 不存在，新增項目
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);
            cart.addCartItem(newItem);
        }

        return convertToResponse(cart);
    }

    /**
     * 更新購物車項目數量
     */
    @Transactional
    public CartResponse updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequest request) {
        // 1. 查詢購物車項目
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("購物車項目不存在，ID: " + cartItemId));

        // 2. 驗證該項目屬於該用戶
        Cart cart = cartItem.getCart();
        if (!cart.getUser().getId().equals(userId)) {
            throw new RuntimeException("無權操作該購物車項目");
        }

        // 3. 檢查庫存
        Product product = cartItem.getProduct();
        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("庫存不足，目前庫存：" + product.getStock());
        }

        // 4. 更新數量
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);

        return convertToResponse(cart);
    }

    /**
     * 移除購物車項目
     */
    @Transactional
    public CartResponse removeCartItem(Long userId, Long cartItemId) {
        // 1. 查詢購物車項目
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("購物車項目不存在，ID: " + cartItemId));

        // 2. 驗證該項目屬於該用戶
        Cart cart = cartItem.getCart();
        if (!cart.getUser().getId().equals(userId)) {
            throw new RuntimeException("無權操作該購物車項目");
        }

        // 3. 移除項目
        cart.removeCartItem(cartItem);
        cartItemRepository.delete(cartItem);

        return convertToResponse(cart);
    }

    /**
     * 清空購物車
     */
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("購物車不存在"));

        cartItemRepository.deleteByCartId(cart.getId());
        cart.getCartItems().clear();
    }

    /**
     * 獲取或創建購物車
     */
    @Transactional
    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("用戶不存在，ID: " + userId));

            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
    }

    /**
     * 轉換為 Response
     */
    private CartResponse convertToResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getCartItems().stream()
                .map(this::convertItemToResponse)
                .collect(Collectors.toList());

        // 計算統計資訊
        int totalItems = itemResponses.size();
        int totalQuantity = itemResponses.stream()
                .mapToInt(CartItemResponse::getQuantity)
                .sum();
        BigDecimal totalAmount = itemResponses.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(itemResponses)
                .totalItems(totalItems)
                .totalQuantity(totalQuantity)
                .totalAmount(totalAmount)
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    /**
     * 轉換購物車項目為 Response
     */
    private CartItemResponse convertItemToResponse(CartItem item) {
        Product product = item.getProduct();
        BigDecimal subtotal = item.getSubtotal();
        boolean inStock = product.getStock() >= item.getQuantity();

        return CartItemResponse.builder()
                .id(item.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productDescription(product.getDescription())
                .productPrice(product.getPrice())
                .productImageUrl(product.getImageUrl())
                .quantity(item.getQuantity())
                .subtotal(subtotal)
                .availableStock(product.getStock())
                .inStock(inStock)
                .build();
    }
}
