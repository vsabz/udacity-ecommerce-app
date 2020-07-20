package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private ItemController itemController;

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
        Item item = testItem();
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        List<Item> items = new LinkedList<>();
        items.add(item);
        when(itemRepository.findByName("car")).thenReturn(items);
        when(itemRepository.findAll()).thenReturn(items);
    }

    @Test
    public void get_item_by_id() {
        Item item = itemController.getItemById(1L).getBody();
        assertNotNull(item);
        assertEquals(testItem(), item);
    }

    @Test
    public void get_items_by_name_happy_path() {
        Item item = itemController.getItemsByName("car").getBody().get(0);
        assertNotNull(item);
        assertEquals(testItem(), item);
    }

    @Test
    public void get_all_items_happy_path() {
        Item item = itemController.getItems().getBody().get(0);
        assertNotNull(item);
        assertEquals(testItem(), item);
    }

    @Test
    public void get_items_by_name_not_found() {
        ResponseEntity response = itemController.getItemsByName("car2");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private Item testItem() {
        Item t = new Item();

        t.setId(1L);
        t.setName("car");
        t.setDescription("nissan");
        t.setPrice(new BigDecimal(232));

        return t;
    }
}
