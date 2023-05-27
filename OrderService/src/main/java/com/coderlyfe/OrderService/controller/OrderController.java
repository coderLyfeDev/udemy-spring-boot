package com.coderlyfe.OrderService.controller;

import com.coderlyfe.OrderService.model.OrderRequest;
import com.coderlyfe.OrderService.model.OrderResponse;
import com.coderlyfe.OrderService.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    @PreAuthorize("hasAuthority('Customer')")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest){
        log.info("test");
        long orderId = orderService.placeOrder(orderRequest);
        log.info("order id:  {}",orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer')")
        public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable long orderId){
            OrderResponse orderResponse =
                    orderService.getOrderDetails(orderId);
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        }
}
