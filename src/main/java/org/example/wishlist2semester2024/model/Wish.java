package org.example.wishlist2semester2024.model;

public class Wish {
    private int wish_id;
    private String wish_name;
    private String wish_description;
    private String wish_price;
    private boolean is_reserved;

    public Wish(int wish_id, String wish_name) {
        this.wish_id = wish_id;
        this.wish_name = wish_name;
        this.wish_description = "";
        this.wish_price = "";
        this.is_reserved = false;
    }

    public int getWish_id() {
        return wish_id;
    }

    public void setWish_id(int wish_id) {
        this.wish_id = wish_id;
    }

    public String getWish_name() {
        return wish_name;
    }

    public void setWish_name(String wish_name) {
        this.wish_name = wish_name;
    }

    public String getWish_description() {
        return wish_description;
    }

    public void setWish_description(String wish_description) {
        this.wish_description = wish_description;
    }

    public String getWish_price() {
        return wish_price;
    }

    public void setWish_price(String wish_price) {
        this.wish_price = wish_price;
    }

    public boolean isReserved() {
        return is_reserved;
    }

    public void setReserved(boolean reserved) {
        is_reserved = reserved;
    }
}
