package org.example.wishlist2semester2024.controller;

import org.example.wishlist2semester2024.model.User;
import org.example.wishlist2semester2024.model.Wish;
import org.example.wishlist2semester2024.model.Wishlist;
import org.example.wishlist2semester2024.service.WishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping("")
    public String showMain() {
        return "main";
    }

    @GetMapping("/add")
    public String addNewUserForm(Model model) {
        model.addAttribute("userModel", new User());
        return "createNewUser";
    }

    @PostMapping("/saveNewUser")
    public String saveNewUser(@ModelAttribute("userModel") User user) {
        wishService.saveNewUser(user);
        return "redirect:/login";  // Omdiriger til login efter brugeroprettelse
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userModel", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("userModel") User user, Model model, HttpSession session) {
        User userFoundInDB = wishService.findUserByName(user.getUsername());
        if (userFoundInDB != null && wishService.validateUserPassword(user)) {
            session.setAttribute("userId", userFoundInDB.getUser_id());
            return "redirect:/main";  // Omdiriger til hovedsiden efter login
        }
        model.addAttribute("loginError", "Invalid username or password");
        return "login";
    }

    @GetMapping("/main")
    public String showMain(@RequestParam(value = "showForm", required = false) Boolean showForm, Model model) {
        if (Boolean.TRUE.equals(showForm)) {
            model.addAttribute("wishlistModel", new Wishlist());
        }
        return "main";
    }

    @PostMapping("/saveWishlist")
    public String saveWishlist(@ModelAttribute("wishlistModel") Wishlist wishlist, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            wishlist.setUser_id(userId);
            wishService.saveWishlist(wishlist);
            return "redirect:/wishlist/" + wishlist.getWishlist_id();
        }
        return "redirect:/main";
    }

    @GetMapping("/wishlist/{wishlistId}")
    public String viewWishlist(@PathVariable int wishlistId, Model model) {
        Wishlist wishlist = wishService.getWishlistById(wishlistId);
        List<Wish> wishes = wishService.getWishesByWishlistId(wishlistId);
        model.addAttribute("wishlist", wishlist);
        model.addAttribute("wishes", wishes);
        model.addAttribute("newWish", new Wish());
        return "wishlist";
    }

    @PostMapping("/wishlist/{wishlistId}/addWish")
    public String addWish(@PathVariable int wishlistId, @ModelAttribute("newWish") Wish wish) {
        wish.setWishlist_id(wishlistId);
        wishService.saveWish(wish);
        return "redirect:/wishlist/" + wishlistId;
    }
}
