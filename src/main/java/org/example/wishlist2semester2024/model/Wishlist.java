package org.example.wishlist2semester2024.model;

import java.util.ArrayList;
import java.util.List;

public class Wishlist {
    private int wishlist_id;
    private String wishlist_name;
    private List<Wish> wishes = new ArrayList<>();
    private int user_id;

    // basic konstruktør uden argumenter
    public Wishlist() {
    }

    // Konstruktør uden id'er, når en ønskeliste bliver dannet
    public Wishlist(String wishlist_name, int user_id) {
        this.wishlist_name = wishlist_name;
        this.user_id = user_id;
    }

    // Konstruktør med id, når vi læser det fra databasen
    public Wishlist(int wishlist_id, String wishlist_name, int user_id) {
        this.wishlist_id = wishlist_id;
        this.wishlist_name = wishlist_name;
        this.user_id = user_id;
    }

    public int getWishlist_id() {
        return wishlist_id;
    }

    public void setWishlist_id(int wishlist_id) {
        this.wishlist_id = wishlist_id;
    }

    public String getWishlist_name() {
        return wishlist_name;
    }

    public void setWishlist_name(String wishlist_name) {
        this.wishlist_name = wishlist_name;
    }

    public List<Wish> getWishes() {
        return wishes;
    }

    public void setWishes(List<Wish> wishes) {
        this.wishes = wishes;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    // Metode til at tilføje et ønske
    public void addWish(Wish wish) {
        wishes.add(wish);
    }
}
