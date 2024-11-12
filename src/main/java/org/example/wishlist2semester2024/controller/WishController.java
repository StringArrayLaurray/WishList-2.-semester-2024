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

    //login
    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        if(wishService.userAlreadyExist((String) session.getAttribute("tried_username"))){ //tjekker om brugernavnet allerede findes
            model.addAttribute("show_popup", true);
            model.addAttribute("error_username", "Brugernavn eksisterer allerede...");
            return "login";
        } else {
            return "login";
        }
    }

    //behandler login data fra bruger
    @PostMapping("/login")
    public String userPage(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           Model model, HttpSession session) {
        if (wishService.userExist(username)) {
            model.addAttribute("user", wishService.getUser(username)); //tjekker om brugeren findes
            session.setAttribute("username", username);
            session.setAttribute("isLoggedIn", true); //gemmer login status i sessionen og omddirigere til brugerens egen side
            return "redirect:/userPage";
        } else {
            model.addAttribute("error", "Forkert Brugernavn eller Adgangskode");
            return "login";
        }
    }

    //brugersiden
    @GetMapping("/userPage")
    public String userPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn"); //kigger på session om bruger er logget ind
        if (isLoggedIn == null || !isLoggedIn) { //hvis ikke, så login side
            return "redirect:/login";
        } else {
            String username = (String) session.getAttribute("username");
            model.addAttribute("user", wishService.getUser(username));
            model.addAttribute("wishlist", wishService.fetchWishList(username));
            return "userPage"; //hvis logget ind, så hentes deres data og vises på deres brugerside
        }
    }

    //log ud
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("isLoggedIn", false);
        session.invalidate();
        return "redirect:/login"; //"fjerner" deres session og omdirigere til login
    }


    //opretter bruger - tilføjer ny bruger til systemet
    @PostMapping("/createUser")
    public String createUser(@ModelAttribute User user, Model model, HttpServletRequest request){
        if (wishService.userAlreadyExist(user.getUsername())){
            HttpSession session = request.getSession();
            session.setAttribute("tried_username", user.getUsername());
            return "redirect:/index#popup1"; //skal laves, lige nu er det bare error page
        } else {
            wishService.addUser(user);
            return "redirect:/userPage";
        }
    }

    //viser oprettelse af brugeren skabelon
    @GetMapping("/createUser")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User()); // Tilføjer en tom bruger til formular
        return "createUser";
    }

    //oprettelse af ønskeliste
    @PostMapping("/createWishlist")
    public String createWishlist(@ModelAttribute Wishlist wishlist, HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        wishService.addWishlist(wishlist, username); //tilføjer ønskelisten
        return "redirect:/userPage";
    }

    //Viser en specifik ønskeliste baseret på wishlist_id.
    @GetMapping("/viewWishList/{wishlist_id}")
    public String viewWishList(@PathVariable("wishlist_id") int wishlist_id, Model model,
                               HttpServletRequest request){
        HttpSession session = request.getSession();
        //henter alle ønsker der er på ønskelisten
        List<Wish> wishList = wishService.fetchAllWishes(wishlist_id);
        // Hent ønskelisten baseret på ID for at få navnet
        Wishlist wishlist = wishService.getWishlistById(wishlist_id);
        String username = (String) session.getAttribute("username");
        model.addAttribute("wishes", wishList);
        model.addAttribute("wishlist_id", wishlist_id);  // Passer wishlist_id til view
        model.addAttribute("wishlistName", wishlist.getWishlist_name()); // Send navnet på ønskelisten til view
        model.addAttribute("user", wishService.getUser(username));
        return "wishList";
    }


    // sletter et ønske
    @GetMapping("deleteWish/{wishlist_id}/{wish_id}")
    public String deleteWish(@PathVariable("wishlist_id") int wishlist_id, @PathVariable("wish_id") int wish_id) {
        boolean deleted = wishService.deleteWish(wish_id);
        if(deleted){
            return "redirect:/viewWishList/" + wishlist_id;
        } else {
            return "redirect:/viewWishList/" + wishlist_id;
        }
    }

    // tilføjer et ønske
    @PostMapping("/addWish/{wishlist_id}")
    public String addWish(@ModelAttribute Wish wish, @PathVariable("wishlist_id") int wishlist_id) { //wish objekt og wishlist_id som input
        wishService.addWishToWishlist(wish, wishlist_id);
        return "redirect:/viewWishList/" + wishlist_id;
    }



    //(U)opdatere et ønske
    @PostMapping("updateWish/{wishlist_id}/{wish_id}")
    public String updateWish(@PathVariable("wishlist_id") int wishlist_id,@PathVariable("wish_id") int wish_id, @ModelAttribute Wish wish){
        wishService.updateWish(wish_id, wish);
        return "redirect:/viewWishList/" + wishlist_id;
    }

    //viser formular til opdatering af ønske
    @GetMapping("/updateWish/{wishlist_id}/{wish_id}")
    public String showUpdateWishForm(@PathVariable("wishlist_id") int wishlist_id,
                                     @PathVariable("wish_id") int wish_id,
                                     Model model) {
        Wish wish = wishService.getWishById(wish_id); // henter eksisterende ønske
        model.addAttribute("wish", wish);
        model.addAttribute("wishlist_id", wishlist_id);
        return "updateWish"; // Side, der viser opdateringsformularen
    }

    //(D)sletter en ønskeliste
    @GetMapping("deleteWishList/{wishlist_id}")
    public String deleteWishlist(@PathVariable("wishlist_id") int wishlist_id){
        boolean deleted = wishService.deleteWishList(wishlist_id);
        if(deleted){
            return "redirect:/userPage";
        } else {
            return "redirect:/userPage";
        }
    }

}


