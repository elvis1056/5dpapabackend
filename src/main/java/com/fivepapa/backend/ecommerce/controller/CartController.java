package com.fivepapa.backend.ecommerce.controller;

import com.fivepapa.backend.common.security.UserPrincipal;
import com.fivepapa.backend.ecommerce.dto.AddToCartRequest;
import com.fivepapa.backend.ecommerce.dto.CartResponse;
import com.fivepapa.backend.ecommerce.dto.UpdateCartItemRequest;
import com.fivepapa.backend.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 購物車控制器
 * 提供購物車相關的 RESTful API
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 查詢購物車
     * GET /api/cart
     */
    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        Long userId = getCurrentUserId();
        CartResponse cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    /**
     * 加入商品到購物車
     * POST /api/cart/items
     */
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(@Valid @RequestBody AddToCartRequest request) {
        Long userId = getCurrentUserId();
        CartResponse cart = cartService.addToCart(userId, request);
        return ResponseEntity.ok(cart);
    }

    /**
     * 更新購物車項目數量
     * PUT /api/cart/items/{id}
     */
    @PutMapping("/items/{id}")
    public ResponseEntity<CartResponse> updateCartItem(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCartItemRequest request) {
        Long userId = getCurrentUserId();
        CartResponse cart = cartService.updateCartItem(userId, id, request);
        return ResponseEntity.ok(cart);
    }

    /**
     * 移除購物車項目
     * DELETE /api/cart/items/{id}
     */
    @DeleteMapping("/items/{id}")
    public ResponseEntity<CartResponse> removeCartItem(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        CartResponse cart = cartService.removeCartItem(userId, id);
        return ResponseEntity.ok(cart);
    }

    /**
     * 清空購物車
     * DELETE /api/cart
     */
    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        Long userId = getCurrentUserId();
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 從 SecurityContext 獲取當前用戶 ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("用戶未登入");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).getId();
        }

        throw new RuntimeException("無法獲取用戶資訊");
    }
}
