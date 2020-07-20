package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartRepository cartRepository = mock(CartRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private CartController cartController;

//    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);

    }

    @Test
    public void add_to_cart_happy_path() {
        when(userRepository.findByUsername("test")).thenReturn(testUser());
        when(itemRepository.findById(1L)).thenReturn(testItem());
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("test");
        r.setItemId(1);
        r.setQuantity(1);

        ResponseEntity<Cart> response = cartController.addTocart(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void add_to_cart_user_not_found() {
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("test");
        r.setItemId(1);
        r.setQuantity(1);

        ResponseEntity<Cart> response = cartController.addTocart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void add_to_cart_item_not_found() {
        when(userRepository.findByUsername("test")).thenReturn(testUser());
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("test");
        r.setItemId(1);
        r.setQuantity(1);

        ResponseEntity<Cart> response = cartController.addTocart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_happy_path() {
        User user = testUser();
        Item item = testItem().get();
        user.getCart().addItem(item);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("test");
        r.setItemId(1);
        r.setQuantity(1);

        ResponseEntity<Cart> response = cartController.removeFromcart(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_user_not_found() {

        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("test");
        r.setItemId(1);
        r.setQuantity(1);

        ResponseEntity<Cart> response = cartController.removeFromcart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void remove_from_cart_item_not_found() {
        User user = testUser();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("test");
        r.setItemId(1);
        r.setQuantity(1);

        ResponseEntity<Cart> response = cartController.removeFromcart(r);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private User testUser() {
        User u = new User();
        u.setUsername("test");
        u.setPassword("1234567");
        u.setCart(new Cart());

        return u;
    }

    private Optional<Item> testItem() {
        Item t = new Item();

        t.setId(1L);
        t.setName("car");
        t.setDescription("nissan");
        t.setPrice(new BigDecimal(232));

        return Optional.of(t);
    }


}
