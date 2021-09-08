package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
        Item item1 = TestUtils.createItem(111);
        Item item2 = TestUtils.createItem(222);
        when(itemRepository.findById((long) 111)).thenReturn(Optional.of(item1));
        when(itemRepository.findByName(item1.getName())).thenReturn(Lists.list(item1, item2));
        when(itemRepository.findAll()).thenReturn(Lists.list(item1, item2));
    }

    @Test
    public void test_get_item_by_id() {
        ResponseEntity<Item> response = itemController.getItemById((long) 111);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void test_get_all_items() {
        final ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    public void test_not_found() {
        ResponseEntity<Item> response = itemController.getItemById((long) 222);
        assertNotNull(response);
        assertEquals(response.getStatusCodeValue(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void get_items_by_name() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item_1" + 111);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    public void test_get_by_name_not_found() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("");
        assertNotNull(response);
        assertEquals(response.getStatusCodeValue(), HttpStatus.NOT_FOUND.value());
    }
}

