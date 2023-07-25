package com.example.springboot.repositories;

import com.example.springboot.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
    @Query("FROM ProductModel pm " +
            "WHERE LOWER(pm.name) like %:searchTerm% ")
    Page<ProductModel> search(
            @Param("searchTerm") String searchTerm,
            Pageable pageable);
}
