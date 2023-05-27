package com.coderlyfe.productService.service;

import com.coderlyfe.productService.entity.ProductEntity;
import com.coderlyfe.productService.model.ProductRequest;
import com.coderlyfe.productService.model.ProductResponse;
import com.coderlyfe.productService.repository.ProductRepository;
import com.coderlyfe.productService.exception.ProductServiceCustomException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Log4j2
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository;

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding product");
        ProductEntity productEntity = new ProductEntity();

        productEntity = productRepository.save(productEntity);
        log.info("Product created");
        return productEntity.getProductId();
    }

    @Override
    public ProductResponse getProductById(long id) {
        log.info("Get the product for productID "+id);
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ProductServiceCustomException(
                        "Product with given id not found",
                        "PRODUCT_NOT_FOUND"));
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(productEntity, productResponse);
        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce quantity {} for id {}", quantity, productId);
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException(
                        "Product with given id not found", "PRODUCT_NOT_FOUND"
                ));

        if(productEntity.getQuantity() < quantity){
            throw new ProductServiceCustomException("Product does not have sufficient quantity", "INSUFFICIENT_QUANTITY");
        }

        productEntity.setQuantity(productEntity.getQuantity() -  quantity);
        productRepository.save(productEntity);
        log.info("Product quantity updated successfully");
    }
}
