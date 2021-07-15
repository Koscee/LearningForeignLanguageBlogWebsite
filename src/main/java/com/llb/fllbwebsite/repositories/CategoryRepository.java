package com.llb.fllbwebsite.repositories;

import com.llb.fllbwebsite.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category getById(Long categoryId);
    Category findByName(String categoryName);
}
