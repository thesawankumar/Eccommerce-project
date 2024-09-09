package com.lcwd.store.repositories;


import com.lcwd.store.entities.Category;
import com.lcwd.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findByTitleContaining(String subTitle, Pageable pageable);
    
    Page<Product> findByLiveTrue(Pageable pageable);
    
    Page<Product> findByCategory(Category category,Pageable pageable);
}
