package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {

    private OrderController orderController;
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        when(userRepository.findByUsername("user_1")).thenReturn(TestUtils.createUser());
        when(orderRepository.findByUser(any())).thenReturn(createUserOrders());
    }

    @Test
    public void test_submit() {
        BigDecimal expected = new BigDecimal(1000);
        final ResponseEntity<UserOrder> response = orderController.submit("user_1");
        UserOrder userOrder = response.getBody();
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertNotNull(userOrder);
        assertEquals(userOrder.getUser().getUsername(), "user_1");
        assertEquals(userOrder.getItems().size(), 1);
        assertEquals(userOrder.getTotal(), expected);
    }

    @Test
    public void test_get_orders_for_user() {
        final ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("user_1");
        assertNotNull(ordersForUser);
        assertEquals(200, ordersForUser.getStatusCodeValue());
        List<UserOrder> orders = ordersForUser.getBody();
        assertNotNull(orders);
    }

    private static List<UserOrder> createUserOrders() {
        UserOrder userOrder = UserOrder.createFromCart(TestUtils.createUser().getCart());
        return Lists.list(userOrder);
    }
}