package org.example.wishlist2semester2024.controllerTest;

import org.example.wishlist2semester2024.controller.WishController;
import org.example.wishlist2semester2024.model.Wishlist;
import org.example.wishlist2semester2024.model.User;
import org.example.wishlist2semester2024.service.WishService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WishController.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishService wishService;

    // Test for login page (GET request)
    @Test
    public void testLoginPage() throws Exception {
        List<Wishlist> mockWishlists = new ArrayList<>();
        when(wishService.fetchAllWishlists()).thenReturn(mockWishlists);

        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk()) 
                .andExpect(view().name("login")) // Tjekker at den går til login
                .andExpect(model().attributeExists("allWishlists")); // Tjekker at allWishlists er i modellen
    }
    @Test
    public void testUserPageWhenLoggedIn() throws Exception {
        String username = "testUser";
        User mockUser = new User();
        mockUser.setUsername(username);

        List<Wishlist> mockWishlists = new ArrayList<>();

        when(wishService.getUser(username)).thenReturn(mockUser);
        when(wishService.fetchWishList(username)).thenReturn(mockWishlists);

        mockMvc.perform(MockMvcRequestBuilders.get("/userPage")
                        .sessionAttr("isLoggedIn", true)
                        .sessionAttr("username", username))
                .andExpect(status().isOk()) 
                .andExpect(view().name("userPage")) // Tjekker at den viser userPage.html
                .andExpect(model().attributeExists("user")) // Tjekker at user er i modellen
                .andExpect(model().attribute("user", mockUser)) // Tjekker at user er i mockUser
                .andExpect(model().attributeExists("wishlist")) // Tjekker at wishlist er i modellen
                .andExpect(model().attribute("wishlist", mockWishlists)); // Tjekker at wishlist er i mockWishlists
    }
    @Test
    public void testCreateUserNewUser() throws Exception {
        User newUser = new User();
        newUser.setUsername("newUser");

        when(wishService.userAlreadyExist("newUser")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/createUser")
                        .flashAttr("user", newUser))
                .andExpect(status().is3xxRedirection()) // Tjekker at at den redirecter
                .andExpect(redirectedUrl("/login")); // Tjekker at brugeren kommer til login
    }

    @Test
    public void testCreateUserExistingUser() throws Exception {
        User existingUser = new User();
        existingUser.setUsername("existingUser");

        when(wishService.userAlreadyExist("existingUser")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/createUser")
                        .flashAttr("user", existingUser))
                .andExpect(status().is3xxRedirection()) // Tjekker at den redirecter
                .andExpect(redirectedUrl("/index#popup1")); // Tjekker at brugeren kommer til fejlsiden
    }


}
