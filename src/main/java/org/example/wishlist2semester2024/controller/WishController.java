package org.example.wishlist2semester2024.controller;

import jakarta.servlet.http.HttpSession;
import org.example.wishlist2semester2024.model.User;
import org.example.wishlist2semester2024.model.Wish;
import org.example.wishlist2semester2024.model.Wishlist;
import org.example.wishlist2semester2024.service.WishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping("")
    public String mainPage(){
        return "login";
    }

    // Login side
    @GetMapping("/login")
    public String login(HttpSession session, Model model) {
        // Hent alle ønskelister og vis dem på loginsiden
        List<Wishlist> allWishlists = wishService.fetchAllWishlists();
        model.addAttribute("allWishlists", allWishlists);

        // Tjek om brugernavnet allerede findes i systemet
        if (wishService.userAlreadyExist((String) session.getAttribute("tried_username"))) {
            model.addAttribute("show_popup", true);
            model.addAttribute("error_username", "Brugernavn eksisterer allerede...");
        }
        return "login";
    }

    // Håndterer login for bruger
    @PostMapping("/login")
    public String userPage(@RequestParam("username") String username, Model model, HttpSession session) {
        if (wishService.userExist(username)) {
            model.addAttribute("user", wishService.getUser(username));
            session.setAttribute("username", username);
            session.setAttribute("isLoggedIn", true);
            return "redirect:/userPage";
        } else {
            model.addAttribute("error", "Forkert Brugernavn eller Adgangskode");
            return "login";
        }
    }

    // Brugerside når logget ind
    @GetMapping("/userPage")
    public String userPage(Model model, HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null || !isLoggedIn) {
            return "redirect:/login";
        } else {
            String username = (String) session.getAttribute("username");
            User user = wishService.getUser(username);
            model.addAttribute("user", user);

            List<Wishlist> wishlists = wishService.fetchWishList(username);
            model.addAttribute("wishlist", wishlists);

            return "userPage";
        }
    }

    // Log ud
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.setAttribute("isLoggedIn", false);
        session.invalidate();
        return "redirect:/login";
    }

    // Opretter ny bruger
    @PostMapping("/createUser")
    public String createUser(@ModelAttribute User user, Model model, HttpSession session) {
        if (wishService.userAlreadyExist(user.getUsername())) {
            session.setAttribute("tried_username", user.getUsername());
            return "redirect:/index#popup1";
        } else {
            wishService.addUser(user);
            return "redirect:/login";
        }
    }

    // Viser oprettelse af bruger skabelon
    @GetMapping("/createUser")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "createUser";
    }

    // Oprettelse af ønskeliste
    @PostMapping("/createWishlist")
    public String createWishlist(@ModelAttribute Wishlist wishlist, HttpSession session) {
        String username = (String) session.getAttribute("username");
        wishService.addWishlist(wishlist, username);
        return "redirect:/userPage";
    }

    // Viser en specifik ønskeliste baseret på wishlist_id
    @GetMapping("/viewWishList/{wishlist_id}")
    public String viewWishList(@PathVariable("wishlist_id") int wishlist_id, Model model, HttpSession session) {
        List<Wish> wishList = wishService.fetchAllWishes(wishlist_id);
        Wishlist wishlist = wishService.getWishlistById(wishlist_id);
        String username = (String) session.getAttribute("username");

        model.addAttribute("wishes", wishList);
        model.addAttribute("wishlist_id", wishlist_id);
        model.addAttribute("wishlistName", wishlist.getWishlist_name());
        model.addAttribute("user", wishService.getUser(username));
        return "wishList";
    }

    // Sletter et ønske
    @GetMapping("deleteWish/{wishlist_id}/{wish_id}")
    public String deleteWish(@PathVariable("wishlist_id") int wishlist_id,
                             @PathVariable("wish_id") int wish_id) {
        boolean deleted = wishService.deleteWish(wish_id);
        return "redirect:/viewWishList/" + wishlist_id;
    }

    // Tilføjer et ønske
    @PostMapping("/addWish/{wishlist_id}")
    public String addWish(@ModelAttribute Wish wish,
                          @PathVariable("wishlist_id") int wishlist_id) {
        wishService.addWishToWishlist(wish, wishlist_id);
        return "redirect:/viewWishList/" + wishlist_id;
    }

    // Opdaterer et ønske
    @PostMapping("updateWish/{wishlist_id}/{wish_id}")
    public String updateWish(@PathVariable("wishlist_id") int wishlist_id,
                             @PathVariable("wish_id") int wish_id,
                             @ModelAttribute Wish wish) {
        wishService.updateWish(wish_id, wish);
        return "redirect:/viewWishList/" + wishlist_id;
    }

    // Viser formular til opdatering af ønske
    @GetMapping("/updateWish/{wishlist_id}/{wish_id}")
    public String showUpdateWishForm(@PathVariable("wishlist_id") int wishlist_id,
                                     @PathVariable("wish_id") int wish_id, Model model) {
        Wish wish = wishService.getWishById(wish_id);
        model.addAttribute("wish", wish);
        model.addAttribute("wishlist_id", wishlist_id);
        return "updateWish";
    }

    // Sletter en ønskeliste
    @GetMapping("deleteWishList/{wishlist_id}")
    public String deleteWishlist(@PathVariable("wishlist_id") int wishlist_id) {
        boolean deleted = wishService.deleteWishList(wishlist_id);
        return "redirect:/userPage";
    }

    // Viser alle offentlige ønskelister uden redigeringsmuligheder
    @GetMapping("/publicWishlist/{wishlist_id}")
    public String viewPublicWishlist(@PathVariable("wishlist_id") int wishlist_id, Model model) {
        List<Wish> wishes = wishService.fetchAllWishes(wishlist_id);
        Wishlist wishlist = wishService.getWishlistById(wishlist_id);

        model.addAttribute("wishes", wishes);
        model.addAttribute("wishlistName", wishlist.getWishlist_name());
        return "publicWishlist";
    }
}