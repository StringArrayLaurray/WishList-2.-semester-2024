package org.example.wishlist2semester2024.repositoryTest;

import org.example.wishlist2semester2024.repository.WishRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RepositoryTest {

    @Mock
    private WishRepository wishRepository;

    @Test
    void testUserAlreadyExist() {

        when(wishRepository.userAlreadyExist("testUser")).thenReturn(true);

        boolean result = wishRepository.userAlreadyExist("testUser");
        assertTrue(result);

    }
}
