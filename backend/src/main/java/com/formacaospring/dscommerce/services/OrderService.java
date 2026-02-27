package com.formacaospring.dscommerce.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacaospring.dscommerce.dto.OrderDTO;
import com.formacaospring.dscommerce.dto.OrderItemDTO;
import com.formacaospring.dscommerce.dto.OrderMinDTO;
import com.formacaospring.dscommerce.dto.UpdateOrderDTO;
import com.formacaospring.dscommerce.entities.Client;
import com.formacaospring.dscommerce.entities.Order;
import com.formacaospring.dscommerce.entities.OrderItem;
import com.formacaospring.dscommerce.entities.OrderStatus;
import com.formacaospring.dscommerce.entities.Payment;
import com.formacaospring.dscommerce.entities.PaymentStatus;
import com.formacaospring.dscommerce.entities.Product;
import com.formacaospring.dscommerce.entities.User;
import com.formacaospring.dscommerce.repositories.ClientRepository;
import com.formacaospring.dscommerce.repositories.OrderItemRepository;
import com.formacaospring.dscommerce.repositories.OrderRepository;
import com.formacaospring.dscommerce.repositories.ProductRepository;
import com.formacaospring.dscommerce.services.exceptions.DatabaseException;
import com.formacaospring.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public Page<OrderMinDTO> findAll(Pageable pageable){
        Page<OrderMinDTO> result = repository.searchAll(pageable);
        return result;
    }
    
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
        Client client = clientRepository.findById(dto.getClient().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Cliente não encontrado!"));

        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_APPROVAL);
        order.setPayment(insertPayment(order));
        order.setClient(client);

        User user = userService.authenticated();
        order.setUser(user);
        order.setUserUpdate(user);

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
    
    @Transactional
    public UpdateOrderDTO update(Long id, UpdateOrderDTO dto) {
    	try {
    		Order order = repository.getReferenceById(id);

        	if(dto.getClientId() != null) {
                Client client = clientRepository.findById(dto.getClientId()).orElseThrow(
                        () -> new ResourceNotFoundException("Cliente não encontrado!"));

        		order.setClient(client);
        	}

            User user = userService.authenticated();
            order.setUserUpdate(user);
        	
        	order.setStatus(dto.getStatus());
        	order.setUpdateMoment(Instant.now());
        	
        	order = repository.save(order);
        	
        	return new UpdateOrderDTO(order);
        	
    	}catch(EntityNotFoundException e) {
    		throw new ResourceNotFoundException("Recurso não encontrado!");
    	}
    	
    }

    private Payment insertPayment(Order order){
        Payment payment = new Payment();
        payment.setMoment(Instant.now());
        payment.setStatus(PaymentStatus.WAITING_PAYMENT);
        payment.setOrder(order);
        return payment;
    }
}
