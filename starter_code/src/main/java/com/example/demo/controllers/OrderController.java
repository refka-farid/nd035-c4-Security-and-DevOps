package com.example.demo.controllers;

import java.util.List;

import javassist.tools.web.BadHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private static final Logger log = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) throws BadHttpRequest {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("ORDER : user {} not found. Fail submit order request ",username);
			log.error("ORDER: BAD_HTTP_REQUEST_EXCEPTION Exception{} Fail request Submit Order ", BadHttpRequest.class);
			throw new BadHttpRequest();
//			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		log.info("ORDER : Order for user{} Success submit order request ",username);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("ORDER:User {} not found. Cannot retrieve Order history", username);
			return ResponseEntity.notFound().build();
		}
		log.info("ORDER:Order history for user {} successfully retrieved", username);
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
