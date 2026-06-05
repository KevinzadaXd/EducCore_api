package com.EducCore.EduCore.repositories;

import com.EducCore.EduCore.domain.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Métodos de CRUD padrões (save, findAll, findById, deleteById) herdados automaticamente
}