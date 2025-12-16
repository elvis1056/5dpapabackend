package com.fivepapa.backend.ecommerce.repository;

import com.fivepapa.backend.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 查詢所有頂層分類（沒有父分類的）
    List<Category> findByParentIsNull();

    // 查詢特定父分類下的所有子分類
    List<Category> findByParentId(Long parentId);

    // 查詢所有啟用的分類
    List<Category> findByActiveTrue();

    // 查詢所有頂層且啟用的分類
    List<Category> findByParentIsNullAndActiveTrue();

    // 根據名稱查詢分類
    Optional<Category> findByName(String name);

    // 檢查名稱是否已存在
    boolean existsByName(String name);

    // 檢查名稱是否已存在（排除特定 ID）
    boolean existsByNameAndIdNot(String name, Long id);
}
