package com.example.springboot.services;

import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public ProductModel saveProductService(ProductModel productModel){
        return productRepository.save(productModel);
    }

    public Optional<ProductModel> getOneProductService(UUID id){
        //Com o Optinal é possível utilizar métodos dessa classe para verificações.
        return productRepository.findById(id);
    }

    public Page<ProductModel> getAllProductsService(){
        int page = 0;
        int size = 10;
        var pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "name");
        return new PageImpl<>(productRepository.findAll(), pageRequest, size);
    }

    public Page<ProductModel> searchProductsService(String searchTerm, int page, int size){
        var pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "name");
        return productRepository.search(searchTerm.toLowerCase(), pageRequest);
    }

    public void deleteProductService(ProductModel productModel){
        productRepository.delete(productModel);
    }
}
