package org.example.wishlist2semester2024.controller;

import org.example.wishlist2semester2024.model.User;
import org.example.wishlist2semester2024.repository.WishRepository;
import org.example.wishlist2semester2024.service.WishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

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
}
