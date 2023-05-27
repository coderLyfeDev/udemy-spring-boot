package com.coderlyfe.productService.service;


import com.coderlyfe.productService.model.ProductRequest;
import com.coderlyfe.productService.model.ProductResponse;

public interface ProductService {

     long addProduct(ProductRequest productRequest);

     ProductResponse getProductById(long id);

    void reduceQuantity(long productId, long quantity);
}
