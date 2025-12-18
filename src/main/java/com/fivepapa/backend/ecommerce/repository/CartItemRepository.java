package com.fivepapa.backend.ecommerce.repository;

import com.fivepapa.backend.ecommerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * 根據購物車 ID 和商品 ID 查詢購物車項目
     * 用於檢查購物車中是否已有該商品
     */
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    /**
     * 刪除購物車中的所有項目
     */
    void deleteByCartId(Long cartId);
}
