package com.fivepapa.backend.ecommerce.repository;

import com.fivepapa.backend.ecommerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * 根據用戶 ID 查詢購物車
     */
    Optional<Cart> findByUserId(Long userId);

    /**
     * 檢查用戶是否已有購物車
     */
    boolean existsByUserId(Long userId);
}
