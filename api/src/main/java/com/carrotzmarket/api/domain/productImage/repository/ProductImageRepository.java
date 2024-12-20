package com.carrotzmarket.api.domain.productImage.repository;

import com.carrotzmarket.db.productImage.ProductImageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {

    // 상품 ID로 이미지 조회
    List<ProductImageEntity> findByProductId(Long productId);
}