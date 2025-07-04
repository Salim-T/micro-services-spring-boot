package com.ecommerce.microcommerce.service;

import com.ecommerce.microcommerce.dto.CreateOrderRequest;
import com.ecommerce.microcommerce.dto.OrderCreatedEvent;
import com.ecommerce.microcommerce.dto.OrderProductRequest;
import com.ecommerce.microcommerce.dto.ProductDTO;
import com.ecommerce.microcommerce.model.Order;
import com.ecommerce.microcommerce.model.OrderProduct;
import com.ecommerce.microcommerce.repository.OrderRepository;
import com.ecommerce.microcommerce.repository.OrderProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ExternalClientService externalClientService;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public OrderService(OrderRepository orderRepository,
            OrderProductRepository orderProductRepository,
            ExternalClientService externalClientService,
            KafkaProducerService kafkaProducerService) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.externalClientService = externalClientService;
        this.kafkaProducerService = kafkaProducerService;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(int id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        logger.info("Creating order for client {} with {} products",
                request.getClientId(), request.getProducts().size());

        // Validate client exists
        if (!externalClientService.clientExists(request.getClientId())) {
            throw new IllegalArgumentException("Client with ID " + request.getClientId() + " does not exist");
        }

        // Validate all products exist and get their details
        List<Integer> productIds = request.getProducts().stream()
                .map(OrderProductRequest::getProductId)
                .collect(Collectors.toList());

        if (!externalClientService.allProductsExist(productIds)) {
            throw new IllegalArgumentException("One or more products do not exist");
        }

        // Create order
        Order order = new Order(request.getDescription(), request.getClientId());
        Order savedOrder = orderRepository.save(order);

        // Create order lines with products
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderProductRequest productRequest : request.getProducts()) {
            ProductDTO product = externalClientService.getProductById(productRequest.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("Product with ID " + productRequest.getProductId() + " not found");
            }

            OrderProduct orderProduct = new OrderProduct(
                    savedOrder.getId(),
                    productRequest.getProductId(),
                    productRequest.getQuantity(),
                    product.getPrice());

            orderProductRepository.save(orderProduct);
            totalAmount = totalAmount.add(orderProduct.getSubtotal());
        }

        // Update order total
        savedOrder.setTotal(totalAmount);
        savedOrder = orderRepository.save(savedOrder);

        // Publish event to Kafka
        List<Integer> allProductIds = request.getProducts().stream()
                .map(OrderProductRequest::getProductId)
                .collect(Collectors.toList());

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getClientId(),
                allProductIds,
                savedOrder.getTotal().doubleValue(),
                savedOrder.getDescription());
        kafkaProducerService.publishOrderCreatedEvent(event);

        logger.info("Order created successfully with ID: {} and total: {}",
                savedOrder.getId(), savedOrder.getTotal());
        return savedOrder;
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }

    public List<Order> searchOrders(String keyword) {
        return orderRepository.findByDescriptionContaining(keyword);
    }

    public boolean orderExists(int id) {
        return orderRepository.existsById(id);
    }

    public List<Order> getOrdersByClientId(int clientId) {
        return orderRepository.findByClientId(clientId);
    }
}
