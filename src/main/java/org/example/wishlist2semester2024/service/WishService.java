package org.example.wishlist2semester2024.service;

import org.example.wishlist2semester2024.model.User;
import org.example.wishlist2semester2024.model.Wish;
import org.example.wishlist2semester2024.model.Wishlist;
import org.example.wishlist2semester2024.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

@Service
public class WishService {

    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public void saveNewUser(User user){
        wishRepository.saveNewUser(user);
    }

    public User findUserByName(String name){
        String sql = "SELECT * FROM user WHERE username = ?";
        try (Connection con = DriverManager.getConnection(wishRepository.getURL(), wishRepository.getUSERNAME(), wishRepository.getPASSWORD());
             PreparedStatement stmt = con.prepareStatement(sql)){

            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    User user = new User();
                    user.setUser_id((int) rs.getLong("user_id"));
                    user.setFirst_name(rs.getString("first_name"));
                    user.setLast_name(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean validateUserPassword(User user){
        String sql = "SELECT password FROM user WHERE username = ?";
        try (Connection con = DriverManager.getConnection(wishRepository.getURL(), wishRepository.getUSERNAME(), wishRepository.getPASSWORD());
             PreparedStatement stmt = con.prepareStatement(sql)){

            stmt.setString(1, user.getUsername());
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    if (user.getPassword().equals(rs.getString("password"))) return true;
                }
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return false;
    }
    public List<Wish> getWishesByWishlistId(int wishlistId) {
        return wishRepository.findWishesByWishlistId(wishlistId);
    }

    public void saveWish(Wish wish) {
        wishRepository.saveWish(wish);
    }

    public Wishlist getWishlistById(int wishlistId) {
        return wishRepository.getWishlistById(wishlistId);
    }
    public void saveWishlist(Wishlist wishlist) {
        wishRepository.saveWishlist(wishlist);
    }
}

