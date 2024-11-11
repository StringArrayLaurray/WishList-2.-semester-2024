package org.example.wishlist2semester2024.controller;

import jakarta.servlet.http.HttpServletRequest;
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



    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        if(wishService.userAlreadyExist((String) session.getAttribute("tried_username"))){
            model.addAttribute("show_popup", true);
            model.addAttribute("error_username", "Brugernavn eksisterer allerede...");
            return "login";
        } else {
            return "login";
        }
    }


    @PostMapping("/login")
    public String userPage(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           Model model, HttpSession session) {
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

    @GetMapping("/userPage")
    public String userPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null || !isLoggedIn) {
            return "redirect:/login";
        } else {
            String username = (String) session.getAttribute("username");
            model.addAttribute("user", wishService.getUser(username));
            model.addAttribute("wishlist", wishService.fetchWishList(username));
            return "userPage";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("isLoggedIn", false);
        session.invalidate();
        return "redirect:/login";
    }


    @PostMapping("/createUser")
    public String createUser(@ModelAttribute User user, Model model, HttpServletRequest request){
        if (wishService.userAlreadyExist(user.getUsername())){
            HttpSession session = request.getSession();
            session.setAttribute("tried_username", user.getUsername());
            return "redirect:/index#popup1"; //slettes?
        } else {
            wishService.addUser(user);
            return "redirect:/userPage";
        }
    }

    @GetMapping("/createUser")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User()); // Tilføjer en tom bruger til formular
        return "createUser";
    }


    @PostMapping("/createWishlist")
    public String createWishlist(@ModelAttribute Wishlist wishlist, HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        wishService.addWishlist(wishlist, username);
        return "redirect:/userPage";
    }

    //Formål: Viser en specifik ønskeliste baseret på wishlist_id.
    @GetMapping("/viewWishList/{wishlist_id}")
    public String viewWishList(@PathVariable("wishlist_id") int wishlist_id, Model model,
                               HttpServletRequest request){
        HttpSession session = request.getSession();
        List<Wish> wishList = wishService.fetchAllWishes(wishlist_id);
        String username = (String) session.getAttribute("username");
        model.addAttribute("wishes", wishList);
        model.addAttribute("wishlist", wishlist_id);
        model.addAttribute("user", wishService.getUser(username));
        return "wishList";
    }

    @GetMapping("/addWish/{wishlist_id}")
    public String addWish(@ModelAttribute Wish wish, @PathVariable("wishlist_id") int wishlist_id) {
        wishService.addWishToWishlist(wish, wishlist_id);
        return "redirect:/viewWishList/" + wishlist_id;
    }


    @GetMapping("deleteWish/{wishlist_id}/{wish_id}")
    public String deleteWish(@PathVariable("wishlist_id") int wishlist_id, @PathVariable("wish_id") int wish_id) {
        boolean deleted = wishService.deleteWish(wish_id);
        if(deleted){
            return "redirect:/viewWishList/" + wishlist_id;
        } else {
            return "redirect:/viewWishList/" + wishlist_id;
        }
    }

    //(U)opdater
    @PostMapping("updateWish/{wishlist_id}/{wish_id}")
    public String updateWish(@PathVariable("wishlist_id") int wishlist_id,@PathVariable("wish_id") int wish_id, @ModelAttribute Wish wish){
        wishService.updateWish(wish_id, wish);
        return "redirect:/viewWishList/" + wishlist_id;
    }

    @GetMapping("/updateWish/{wishlist_id}/{wish_id}")
    public String showUpdateWishForm(@PathVariable("wishlist_id") int wishlist_id,
                                     @PathVariable("wish_id") int wish_id,
                                     Model model) {
        Wish wish = wishService.getWishById(wish_id); // Metode til at hente ønsket fra databasen
        model.addAttribute("wish", wish);
        model.addAttribute("wishlist_id", wishlist_id);
        return "updateWish"; // Side, der viser opdateringsformularen
    }

    //(D)slet
    @GetMapping("deleteWishList/{wishlist_id}")
    public String deleteWishlist(@PathVariable("wishlist_id") int wishlist_id){
        boolean deleted = wishService.deleteWishList(wishlist_id);
        if(deleted){
            return "redirect:/userPage"; //samme
        } else {
            return "redirect:/userPage"; //samme
        }
    }

}


