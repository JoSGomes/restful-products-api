package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.services.ProductService;
import com.example.springboot.utils.Telemetry;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/*
Improvements:
Incluir paginação, filtros, pesquisa por filtros, customizações de erros, adicionar created_at e updated_at.
*/

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/api/products")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto){
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);

        var productSaved = productService.saveProductService(productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productSaved);
    }

    @GetMapping("/api/products")
    public ResponseEntity<Page<ProductModel>> getAllProducts(){
        var productsList = productService.getAllProductsService();
        if(!productsList.isEmpty()){
            for(ProductModel product : productsList){
                UUID id = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productsList);
    }

    @GetMapping("/api/products/search")
    public ResponseEntity<Page<ProductModel>> searchProducts(
            @RequestParam(
                    value = "searchTerm",
                    required = false
            ) String searchTerm,
            @RequestParam(
                    value = "page",
                    required = false,
                    defaultValue = "0") int page,
            @RequestParam(
                    value = "size",
                    required = false,
                    defaultValue = "10") int size
    ){
        var productsPage = productService.searchProductsService(searchTerm, page, size);
        if(!productsPage.isEmpty()){
            for(ProductModel product : productsPage){
                UUID id = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productsPage);
    }

    @GetMapping("/api/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id){
        var productO = productService.getOneProductService(id);
        if(productO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Telemetry("Product not Found."));
        }
        ProductModel product = productO.get();
        product.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Products list"));
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PutMapping("/api/products/{id}")
    public ResponseEntity<Object> updateProduct(
            @RequestBody @Valid ProductRecordDto productRecordDto,
            @PathVariable(value="id") UUID id){
        var productO = productService.getOneProductService(id);
        if(productO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Telemetry("Product not Found."));
        }
        var productModel = productO.get();
        BeanUtils.copyProperties(productRecordDto, productModel);

        var updatedProduct = productService.saveProductService(productModel);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }

    @DeleteMapping("/api/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id){
        var productO = productService.getOneProductService(id);
        if(productO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Telemetry("Product not Found."));
        }

        productService.deleteProductService(productO.get());
        return ResponseEntity.status(HttpStatus.OK).body(new Telemetry("Product deleted successfully."));
    }
}
