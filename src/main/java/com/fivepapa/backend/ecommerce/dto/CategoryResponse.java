package com.fivepapa.backend.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Long id;
    private String name;
    private String description;

    // 父分類資訊（簡化版，避免循環引用）
    private Long parentId;
    private String parentName;

    // 子分類列表（簡化版，只包含基本資訊）
    @Builder.Default
    private List<CategorySimpleResponse> children = new ArrayList<>();

    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 額外資訊：是否為頂層分類
    private Boolean isTopLevel;

    // 額外資訊：產品數量
    private Integer productCount;

    /**
     * 簡化的子分類資訊（避免無限遞迴）
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategorySimpleResponse {
        private Long id;
        private String name;
        private String description;
        private Boolean active;
        private Integer productCount;
    }
}
