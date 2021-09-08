package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {

    private CartController cartController;

    private ItemRepository itemRepository = mock(ItemRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        when(userRepository.findByUsername("user_1")).thenReturn(TestUtils.createUser());
        when(itemRepository.findById((long) 1)).thenReturn(Optional.of(TestUtils.createItem(1)));
    }

    @Test
    public void test_addToCard() {
        //arrange
        BigDecimal expected = new BigDecimal(6000);
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(5);
        request.setUsername("user_1");

        //act
        final ResponseEntity<Cart> response = cartController.addTocart(request);
        Cart cart = response.getBody();

        //assert
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertNotNull(cart);
        assertEquals(cart.getUser().getUsername(), "user_1");
        assertEquals(cart.getItems().size(), 6);
        assertEquals(cart.getTotal(), expected);

    }

    @Test
    public void test_addToCart_userName_not_found() {
        //arrange
        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(3);
        request.setItemId(123);
        request.setUsername("User_1");

        //act
        final ResponseEntity<Cart> response = cartController.addTocart(request);

        //assert
        assertEquals(response.getStatusCodeValue(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void test_removeFromCard() {
        //arrange
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(5);
        request.setUsername("user_1");

        //act
        final ResponseEntity<Cart> response = cartController.removeFromcart(request);
        Cart cart = response.getBody();

        //assert
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertNotNull(cart);
        assertEquals(cart.getUser().getUsername(), "user_1");
        assertEquals(cart.getItems().size(), 0);

    }
}

