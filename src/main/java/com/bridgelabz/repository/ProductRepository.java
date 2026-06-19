package com.bridgelabz.repository;
import com.bridgelabz.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitleContainingIgnoreCase(
            String keyword);

    List<Product> findByCategoryId(
            Long categoryId);

    List<Product>
    findTop8ByOrderByIdDesc();
}
