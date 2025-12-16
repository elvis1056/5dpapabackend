package com.fivepapa.backend.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "分類名稱不能為空")
    @Size(max = 100, message = "分類名稱不能超過 100 字")
    private String name;

    private String description;

    // 父分類 ID（如果為 null，表示這是頂層分類）
    private Long parentId;

    private Boolean active = true;
}
