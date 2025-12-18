package com.fivepapa.backend.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 購物車項目回應
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {

    private Long id;

    // 商品資訊
    private Long productId;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private String productImageUrl;

    // 購物車項目資訊
    private Integer quantity;
    private BigDecimal subtotal;  // 小計（單價 * 數量）

    // 庫存檢查
    private Integer availableStock;  // 可用庫存
    private Boolean inStock;  // 是否有庫存
}
