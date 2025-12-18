package com.fivepapa.backend.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "商品名稱不能為空")
    @Size(max = 200, message = "商品名稱不能超過 200 字")
    private String name;

    private String description;

    @NotNull(message = "商品價格不能為空")
    @DecimalMin(value = "0.0", inclusive = false, message = "價格必須大於 0")
    private BigDecimal price;

    @NotNull(message = "庫存數量不能為空")
    @Min(value = 0, message = "庫存不能為負數")
    private Integer stock;

    private String imageUrl;

    // 商品分類 ID（可選，允許商品沒有分類）
    private Long categoryId;

    private Boolean active = true;
    private Boolean featured = false;
}