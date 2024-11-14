import jakarta.servlet.http.HttpSession;
import org.example.wishlist2semester2024.controller.WishController;
import org.example.wishlist2semester2024.model.User;
import org.example.wishlist2semester2024.model.Wish;
import org.example.wishlist2semester2024.model.Wishlist;
import org.example.wishlist2semester2024.service.WishService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WishController.class)
public class WishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishService wishService;

    // Denne test validerer, at login-siden fungerer korrekt og viser de forventede elementer
    @Test
    public void testLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk()) // Tjekker at status er 200 (OK)
                .andExpect(view().name("login")) // Tjekker at view’et er "login"
                .andExpect(model().attributeExists("allWishlists")); // Tjekker at "allWishlists" er i modellen
    }

    // Denne test tjekker, hvordan userPage-siden fungerer, når brugeren er logget ind
    @Test
    public void testUserPageWhenLoggedIn() throws Exception {
        // Simulerer en aktiv session med en brugers username og login-status
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("isLoggedIn")).thenReturn(true);
        when(session.getAttribute("username")).thenReturn("testUser");
        when(wishService.getUser("testUser")).thenReturn(new User());

        mockMvc.perform(MockMvcRequestBuilders.get("/userPage").sessionAttr("isLoggedIn", true).sessionAttr("username", "testUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("userPage")) // Tjekker at view’et er userPage
                .andExpect(model().attributeExists("user")) // Tjekker at "user" er i modellen
                .andExpect(model().attributeExists("wishlist")); // Tjekker at "wishlist" er i modellen
    }

    // Denne test kontrollerer, at ønskeliste-visningen fungerer og henter data korrekt
    @Test
    public void testViewWishList() throws Exception {
        int wishlistId = 1;

        // Mock session attributter og service kald
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("username")).thenReturn("testUser");

        // Mock data for ønskeliste og ønsker
        Wishlist wishlist = new Wishlist();
        wishlist.setWishlist_id(wishlistId);
        wishlist.setWishlist_name("Test Wishlist");

        List<Wish> wishes = new ArrayList<>();
        wishes.add(new Wish()); // Tilføj en test-ønske

        // Mock service metoder
        when(wishService.fetchAllWishes(wishlistId)).thenReturn(wishes);
        when(wishService.getWishlistById(wishlistId)).thenReturn(wishlist);
        when(wishService.getUser("testUser")).thenReturn(new User());

        mockMvc.perform(MockMvcRequestBuilders.get("/viewWishList/{wishlist_id}", wishlistId)
                        .sessionAttr("username", "testUser")) // Tilføj session attribut for "username"
                .andExpect(status().isOk()) // Tjekker at status er 200 (OK)
                .andExpect(view().name("wishList")) // Tjekker at view’et er "wishList"
                .andExpect(model().attributeExists("wishes")) // Tjekker at "wishes" er i modellen
                .andExpect(model().attributeExists("wishlist_id")) // Tjekker at "wishlist_id" er i modellen
                .andExpect(model().attributeExists("wishlistName")) // Tjekker at "wishlistName" er i modellen
                .andExpect(model().attribute("wishlistName", "Test Wishlist")) // Tjekker at "wishlistName" har værdi "Test Wishlist"
                .andExpect(model().attributeExists("user")); // Tjekker at "user" er i modellen
    }

}
