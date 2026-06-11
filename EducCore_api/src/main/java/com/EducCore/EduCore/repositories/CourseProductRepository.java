package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.Product.CourseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseProductRepository extends JpaRepository<CourseProduct, Long> {

    @Modifying
    @Query("DELETE FROM CourseProduct cp WHERE cp.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}