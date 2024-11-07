package org.example.wishlist2semester2024.repository;

import org.example.wishlist2semester2024.model.User;
import org.example.wishlist2semester2024.model.Wish;
import org.springframework.stereotype.Repository;
import org.example.wishlist2semester2024.model.Wishlist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WishRepository {

    private String URL = System.getenv("JDBC_DB_URL");
    private String USERNAME = System.getenv("JDBC_USERNAME");
    private String PASSWORD = System.getenv("JDBC_PASSWORD");

    public String getURL() {
        return URL;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void saveNewUser(User user) {
        String sql = "INSERT INTO user (first_name, last_name, email, username, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, user.getFirst_name());
            stmt.setString(2, user.getLast_name());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getUsername());
            stmt.setString(5, user.getPassword());
            stmt.executeUpdate();

        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void saveWishlist(Wishlist wishlist) {
        String sql = "INSERT INTO wishlist (wishlist_name, user_id) VALUES (?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, wishlist.getWishlist_name());
            stmt.setInt(2, wishlist.getUser_id());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    wishlist.setWishlist_id(generatedKeys.getInt(1)); // tager det genererede ID
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fejl ved gemning af wishlist", e);
        }
    }

    public List<Wish> findWishesByWishlistId(int wishlistId) {
        String sql = "SELECT * FROM wish WHERE wishlist_id = ?";
        List<Wish> wishes = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, wishlistId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Wish wish = new Wish();
                    wish.setWish_id(rs.getInt("wish_id"));
                    wish.setWish_name(rs.getString("wish_name"));
                    wish.setWish_description(rs.getString("wish_description"));
                    wish.setWish_price(rs.getString("wish_price"));
                    wish.setWish_link(rs.getString("wish_link"));
                    wish.setWishlist_id(rs.getInt("wishlist_id"));
                    wishes.add(wish);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Fejl ved hentning af ønsker for ønskelisten", e);
        }
        return wishes;
    }
    public void saveWish(Wish wish) {
        String sql = "INSERT INTO wish (wish_name, wish_description, wish_price, wish_link, wishlist_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, wish.getWish_name());
            stmt.setString(2, wish.getWish_description());
            stmt.setString(3, wish.getWish_price());
            stmt.setString(4, wish.getWish_link());
            stmt.setInt(5, wish.getWishlist_id());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Fejl ved gemning af wish", e);
        }
    }

    public Wishlist getWishlistById(int wishlistId) {
        String sql = "SELECT * FROM wishlist WHERE wishlist_id = ?";
        Wishlist wishlist = null;

        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, wishlistId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    wishlist = new Wishlist();
                    wishlist.setWishlist_id(rs.getInt("wishlist_id"));
                    wishlist.setWishlist_name(rs.getString("wishlist_name"));
                    wishlist.setUser_id(rs.getInt("user_id"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Fejl ved hentning af ønskelisten", e);
        }
        return wishlist;
    }


}
