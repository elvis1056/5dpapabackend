package com.fivepapa.backend.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Category Entity
 * Supports two-level category hierarchy (parent-child relationship)
 */
@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;  // 分類名稱

    @Column(columnDefinition = "TEXT")
    private String description;  // 分類描述

    // 父分類（自關聯）- 一個分類可以有一個父分類
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore  // 避免 JSON 序列化時的循環引用
    private Category parent;

    // 子分類列表 - 一個分類可以有多個子分類
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore  // 避免 JSON 序列化時的循環引用
    private List<Category> children = new ArrayList<>();

    // 產品列表 - 一個分類可以有多個產品
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnore  // 避免返回大量產品資料
    private List<Product> products = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;  // 是否啟用

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // 輔助方法：判斷是否為頂層分類
    public boolean isTopLevel() {
        return parent == null;
    }

    // 輔助方法：判斷是否有子分類
    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }
}
