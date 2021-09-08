package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object toInject) {
        boolean wasPrivate = false;

        try {
            Field declaredField = target.getClass().getDeclaredField(fieldName);
            if (!declaredField.isAccessible()) {
                declaredField.setAccessible(true);
                wasPrivate = true;
            }
            declaredField.set(target, toInject);

            if (wasPrivate) {
                declaredField.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    public static User createUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("user_1");
        user.setPassword("testPassword");
        user.setCart(createCart(user));
        return user;
    }

    public static Item createItem(long id) {
        Item item = new Item();
        item.setId(id);
        item.setPrice(BigDecimal.valueOf(id * 1000));
        item.setName("Item_1" + item.getId());
        item.setDescription("Test Item Description ");
        return item;
    }

    public static Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(createItem(1));
        return cart;
    }
}
