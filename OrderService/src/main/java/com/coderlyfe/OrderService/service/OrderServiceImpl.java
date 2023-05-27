package com.coderlyfe.OrderService.service;

import com.coderlyfe.OrderService.entity.Order;
import com.coderlyfe.OrderService.exception.CustomException;
import com.coderlyfe.OrderService.external.client.PaymentService;
import com.coderlyfe.OrderService.external.client.ProductService;
import com.coderlyfe.OrderService.external.request.PaymentRequest;
import com.coderlyfe.OrderService.external.response.PaymentResponse;
import com.coderlyfe.OrderService.model.OrderRequest;
import com.coderlyfe.OrderService.model.OrderResponse;
import com.coderlyfe.OrderService.repository.OrderRepository;
import com.coderlyfe.OrderService.model.ProductResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        log.info("Placing order request {}", orderRequest);
        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());
        log.info("Creating order with status CREATED");
        Order order = Order.builder()
                .amount(orderRequest.getAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();
        order =  orderRepository.save(order);
        log.info("Order placed successfully for {}", order.getId());

        log.info("Calling payment service to complete the payment");
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getAmount())
                .referenceNumber("")
                .build();
        String orderStatus = null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done successfully. Changing order status to PLACED");
            orderStatus = "PLACED";
        } catch(Exception e){
            log.error("Payment  unsuccessful. Changing order status to PAYMENT_FAILED");
            orderStatus = "PAYMENT_FAILED";
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        log.info("ORder Placed successfully with order ID: {}", order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("get order details for orderId {}", orderId);

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() -> new CustomException("Order not found for the orderId "+orderId, "NOT_FOUND", 404) );

        log.info("Invoking product service to fetch product for id {}", order.getProductId());
        ProductResponse productResponse = restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/"+order.getProductId(),
                ProductResponse.class
        );

        log.info("Getting payment information from payment service");
        PaymentResponse paymentResponse = restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payment/order/"+order.getId(), PaymentResponse.class
        );


        OrderResponse.ProductDetails productDetails =
                OrderResponse.ProductDetails.builder()
                        .productName(productResponse.getProductName())
                        .productId(productResponse.getProductId())
                        .price(productResponse.getPrice())
                        .quantity(productResponse.getQuantity())
                        .build();

        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails
                .builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentStatus(paymentResponse.getStatus())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
        return orderResponse;
    }
}
