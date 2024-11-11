package org.example.wishlist2semester2024.repository;

import org.example.wishlist2semester2024.model.User;
import org.example.wishlist2semester2024.model.Wish;
import org.example.wishlist2semester2024.model.Wishlist;
import org.springframework.stereotype.Repository;

import javax.swing.tree.RowMapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WishRepository {

    private String url = System.getenv("JDBC_DB_URL");
    private String username = System.getenv("JDBC_DB_USERNAME");
    private String password = System.getenv("JDBC_DB_PASSWORD");


    //Henter alle ønsker (wish) tilknyttet en bestemt ønskeliste (wishlist_id).
    public List<Wish> fetchAllWishes(int wishlist_id){
        List<Wish> wishes = new ArrayList<>();
        String sql = "SELECT * FROM wish WHERE wishlist_id = ?";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, wishlist_id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Wish wish = new Wish();
                    wish.setWish_id(rs.getInt("wish_id"));
                    wish.setWish_name(rs.getString("wish_name"));
                    wish.setWish_price(rs.getString("wish_price"));
                    wish.setWish_description(rs.getString("wish_description"));
                    wish.setWish_link(rs.getString("wish_link"));
                    wishes.add(wish);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved hentning af ønsker", e);
        }

        return wishes;
    }

    //Henter alle brugere (users) fra databasen.
    public List<User> fetchAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setUser_id(rs.getInt("user_id"));
                user.setFirst_name(rs.getString("first_name"));
                user.setLast_name(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved hentning af brugere", e);
        }

        return users;
    }

    // Tjekker, om en bruger allerede eksisterer baseret på brugernavn
    public boolean userAlreadyExist(String username) {
        String sql = "SELECT COUNT(*) FROM user WHERE username = ?";

        try (Connection con = DriverManager.getConnection(url, this.username, password);
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved kontrol af brugereksistens", e);
        }

        return false;
    }


    //Henter en specifik bruger baseret på brugernavn.
    public User getUser(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        User user = null;

        try (Connection con = DriverManager.getConnection(url, this.username, password);
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUser_id(rs.getInt("user_id"));
                    user.setFirst_name(rs.getString("first_name"));
                    user.setLast_name(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved hentning af bruger", e);
        }

        return user;
    }

    public void addUser(User user) {
        String sql = "INSERT INTO user (user_id, first_name, last_name, email, username, password) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, user.getUser_id());
            stmt.setString(2, user.getFirst_name());
            stmt.setString(3, user.getLast_name());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getUsername());
            stmt.setString(6, user.getPassword());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved oprettelse af bruger", e);
        }
    }

    public void addWishlist(Wishlist wishlist, String username) {
        String sql = "INSERT INTO wishlist (wishlist_name, user_id) VALUES (?, (SELECT user_id FROM user WHERE username = ?))";

        try (Connection con = DriverManager.getConnection(url, this.username, password);
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, wishlist.getWishlist_name());
            stmt.setString(2, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved tilføjelse af ønskeliste", e);
        }
    }


    public Boolean deleteWishList(int wishlist_id) {
        String sql = "DELETE FROM wishlist WHERE wishlist_id = ?";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, wishlist_id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved sletning af ønskeliste", e);
        }
    }


    public List<Wishlist> fetchAllWishlist(String username) {
        List<Wishlist> wishlists = new ArrayList<>();
        String sql = "SELECT * FROM wishlist WHERE user_id = (SELECT user_id FROM user WHERE username = ?)";

        try (Connection con = DriverManager.getConnection(url, this.username, password);
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Wishlist wishlist = new Wishlist();
                    wishlist.setWishlist_id(rs.getInt("wishlist_id"));
                    wishlist.setWishlist_name(rs.getString("wishlist_name"));
                    wishlists.add(wishlist);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved hentning af ønskelister", e);
        }

        return wishlists;
    }


    public void addWishToWishlist(Wish wish, int wishlist_id) {
        String sql = "INSERT INTO wish (wish_name, wish_price, wish_description, wish_link, wishlist_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, wish.getWish_name());
            stmt.setString(2, wish.getWish_price());
            stmt.setString(3, wish.getWish_description());
            stmt.setString(4, wish.getWish_link());
            stmt.setInt(5, wishlist_id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved tilføjelse af ønske til ønskeliste", e);
        }
    }


    public Boolean deleteWish(int wish_id) {
        String sql = "DELETE FROM wish WHERE wish_id = ?";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, wish_id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved sletning af ønske", e);
        }
    }


    public void updateWish(int id, Wish wish) {
        String sql = "UPDATE wish SET wish_name = ?, wish_price = ?, wish_link = ?, wish_description = ? WHERE wish_id = ?";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, wish.getWish_name());
            stmt.setString(2, wish.getWish_price());
            stmt.setString(3, wish.getWish_link());
            stmt.setString(4, wish.getWish_description());
            stmt.setInt(5, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved opdatering af ønske", e);
        }
    }

    public Wish getWishById(int wish_id) {
        String sql = "SELECT * FROM wish WHERE wish_id = ?";
        Wish wish = null;

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, wish_id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    wish = new Wish();
                    wish.setWish_id(rs.getInt("wish_id"));
                    wish.setWish_name(rs.getString("wish_name"));
                    wish.setWish_price(rs.getString("wish_price"));
                    wish.setWish_description(rs.getString("wish_description"));
                    wish.setWish_link(rs.getString("wish_link"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fejl ved hentning af ønske", e);
        }

        return wish;
    }



}
