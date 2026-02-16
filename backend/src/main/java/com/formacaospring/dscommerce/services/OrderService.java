package com.formacaospring.dscommerce.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacaospring.dscommerce.dto.OrderDTO;
import com.formacaospring.dscommerce.dto.OrderItemDTO;
import com.formacaospring.dscommerce.entities.Order;
import com.formacaospring.dscommerce.entities.OrderItem;
import com.formacaospring.dscommerce.entities.OrderStatus;
import com.formacaospring.dscommerce.entities.Product;
import com.formacaospring.dscommerce.entities.User;
import com.formacaospring.dscommerce.repositories.OrderItemRepository;
import com.formacaospring.dscommerce.repositories.OrderRepository;
import com.formacaospring.dscommerce.repositories.ProductRepository;
import com.formacaospring.dscommerce.services.exceptions.DatabaseException;
import com.formacaospring.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;
    
    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado!"));
        authService.validateSelfOrAdmin(order.getUser().getId());
        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        Order order = new Order();

        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_APPROVAL);

        User user = userService.authenticated();
        order.setUser(user);

        for(OrderItemDTO itemDto : dto.getItems()){
            Product product = productRepository.getReferenceById(itemDto.getProductId());
            OrderItem item = new OrderItem(order, product, itemDto.getQuantity(), product.getPrice());
            if(item.getQuantity() > product.getQuantity()) {
            	throw new DatabaseException("Item " + product.getName() +": quantidade no pedido maior que o item no estoque!");
            }
            Integer currentQuantity = product.getQuantity();
            product.setQuantity(currentQuantity - itemDto.getQuantity());
            order.getItems().add(item);
        }

        repository.save(order);
        orderItemRepository.saveAll(order.getItems());

        return new OrderDTO(order);
    }
}
