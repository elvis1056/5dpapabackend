package com.fivepapa.backend.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 購物車回應
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {

    private Long id;
    private Long userId;

    @Builder.Default
    private List<CartItemResponse> items = new ArrayList<>();

    // 購物車統計資訊
    private Integer totalItems;  // 總項目數（商品種類數）
    private Integer totalQuantity;  // 總數量（所有商品數量加總）
    private BigDecimal totalAmount;  // 總金額

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
