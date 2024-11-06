package org.example.wishlist2semester2024.controller;

import org.example.wishlist2semester2024.model.User;
import org.example.wishlist2semester2024.service.WishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class WishController {

    private final WishService wishService;


    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping("")
    public String showMain(){
        return "main";
    }

    @GetMapping("/add")
    public String addNewUserForm(Model model) {
        User newlyCreatedUser = new User();
        model.addAttribute("userModel", newlyCreatedUser);
        return "createNewUser";
    }

    @PostMapping("/saveNewUser")
    public String saveNewUser(@ModelAttribute("userModel") User user){
        wishService.saveNewUser(user);
        return "main";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userModel", new User());  // Bind an empty User object for the form
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("userModel") User user, Model model) {
        User userFoundInDB = wishService.findUserByName(user.getUsername());
        if (userFoundInDB != null && wishService.validateUserPassword(user)) {
            return "userPage";
        }
        model.addAttribute("loginError", "Invalid username or password");
        return "login";
    }
}
