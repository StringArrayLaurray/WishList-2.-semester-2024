package org.example.wishlist2semester2024.service;

import org.example.wishlist2semester2024.model.User;
import org.example.wishlist2semester2024.repository.WishRepository;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public void saveNewUser(User user){
        wishRepository.saveNewUser(user);
    }
}
