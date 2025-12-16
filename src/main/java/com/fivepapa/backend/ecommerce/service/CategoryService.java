package com.fivepapa.backend.ecommerce.service;

import com.fivepapa.backend.ecommerce.dto.CategoryRequest;
import com.fivepapa.backend.ecommerce.dto.CategoryResponse;
import com.fivepapa.backend.ecommerce.entity.Category;
import com.fivepapa.backend.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 建立分類
     */
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        // 檢查名稱是否已存在
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("分類名稱已存在：" + request.getName());
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(request.getActive() != null ? request.getActive() : true);

        // 設定父分類（如果有）
        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("父分類不存在，ID: " + request.getParentId()));

            // 檢查父分類是否為頂層分類（防止建立三層以上分類）
            if (!parent.isTopLevel()) {
                throw new RuntimeException("不允許建立超過兩層的分類結構");
            }

            category.setParent(parent);
        }

        Category saved = categoryRepository.save(category);
        return convertToResponse(saved);
    }

    /**
     * 查詢所有分類
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 查詢所有頂層分類（包含子分類）
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getTopLevelCategories() {
        return categoryRepository.findByParentIsNull().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 查詢單一分類
     */
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分類不存在，ID: " + id));
        return convertToResponse(category);
    }

    /**
     * 查詢特定父分類的子分類
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getChildCategories(Long parentId) {
        return categoryRepository.findByParentId(parentId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 更新分類
     */
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分類不存在，ID: " + id));

        // 檢查名稱是否與其他分類重複
        if (categoryRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new RuntimeException("分類名稱已存在：" + request.getName());
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(request.getActive() != null ? request.getActive() : true);

        // 更新父分類
        if (request.getParentId() != null) {
            // 不能將分類設為自己的子分類
            if (request.getParentId().equals(id)) {
                throw new RuntimeException("分類不能成為自己的子分類");
            }

            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("父分類不存在，ID: " + request.getParentId()));

            // 檢查父分類是否為頂層分類
            if (!parent.isTopLevel()) {
                throw new RuntimeException("不允許建立超過兩層的分類結構");
            }

            // 如果該分類原本有子分類，不允許變更為子分類
            if (category.hasChildren()) {
                throw new RuntimeException("已有子分類的分類不能變更為子分類");
            }

            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        Category updated = categoryRepository.save(category);
        return convertToResponse(updated);
    }

    /**
     * 刪除分類
     */
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分類不存在，ID: " + id));

        // 檢查是否有子分類
        if (category.hasChildren()) {
            throw new RuntimeException("該分類下有子分類，無法刪除");
        }

        // 檢查是否有產品
        if (!category.getProducts().isEmpty()) {
            throw new RuntimeException("該分類下有產品，無法刪除");
        }

        categoryRepository.deleteById(id);
    }

    /**
     * Entity 轉 Response
     */
    private CategoryResponse convertToResponse(Category category) {
        CategoryResponse response = CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.getActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .isTopLevel(category.isTopLevel())
                .productCount(category.getProducts().size())
                .build();

        // 設定父分類資訊
        if (category.getParent() != null) {
            response.setParentId(category.getParent().getId());
            response.setParentName(category.getParent().getName());
        }

        // 設定子分類列表（簡化版）
        if (category.hasChildren()) {
            List<CategoryResponse.CategorySimpleResponse> children = category.getChildren().stream()
                    .map(child -> CategoryResponse.CategorySimpleResponse.builder()
                            .id(child.getId())
                            .name(child.getName())
                            .description(child.getDescription())
                            .active(child.getActive())
                            .productCount(child.getProducts().size())
                            .build())
                    .collect(Collectors.toList());
            response.setChildren(children);
        }

        return response;
    }
}
