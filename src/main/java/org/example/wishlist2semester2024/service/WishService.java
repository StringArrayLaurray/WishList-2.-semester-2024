package org.example.wishlist2semester2024.service;

import org.example.wishlist2semester2024.model.User;
import org.example.wishlist2semester2024.model.Wish;
import org.example.wishlist2semester2024.model.Wishlist;
import org.example.wishlist2semester2024.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class WishService {

    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public List<Wish> fetchAllWishes(int wishlist_id) {
        return wishRepository.fetchAllWishes(wishlist_id);
    }

    public List<User> fetchAllUsers() {
        return wishRepository.fetchAllUsers();
    }

    public boolean userExist(String username){
        return wishRepository.userAlreadyExist(username);
    }

    public User getUser(String username){
        return wishRepository.getUser(username);
    }

    public List<Wishlist> fetchWishList(String username){
        return wishRepository.fetchAllWishlist(username);
    }
    public void addUser(User user){
        wishRepository.addUser(user);
    }
    public void addWishToWishlist(Wish wish,int wishlist_id){
        wishRepository.addWishToWishlist(wish, wishlist_id);
    }
    public void addWishlist(Wishlist wishlist, String username){
        wishRepository.addWishlist(wishlist, username);
    }
    public Boolean deleteWish(int wish_id){
        return wishRepository.deleteWish(wish_id);
    }
    public void updateWish(int id, Wish wish){
        wishRepository.updateWish(id,wish);
    }
    public Boolean deleteWishList(int wishlist_id){
        return wishRepository.deleteWishList(wishlist_id);
    }
    public boolean userAlreadyExist(String username){
        return wishRepository.userAlreadyExist(username);
    }

    public Wish getWishById(int wish_id) {
        return wishRepository.getWishById(wish_id);
    }

}