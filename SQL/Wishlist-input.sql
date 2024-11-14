USE wishlist;

INSERT INTO user (first_name, last_name, email, password, username) VALUES 
('Laura', 'Nielsen', 'laura@example.com', 'password123', 'laura23'),
('Kasper', 'Hansen', 'kasper@example.com', 'securepass', 'kasperH'),
('Anna', 'Jensen', 'anna@example.com', 'mypassword', 'annaJ');


INSERT INTO wishlist (wishlist_name, user_id) VALUES 
('Jule2024 ønskeliste', 1),
('Fødselsdag2024 ønskeliste', 1),
('Rejse ønskeliste', 2),
('Bøger ønskeliste', 3);


INSERT INTO wish (wish_name, wish_description, wish_price, wish_link, wishlist_id) VALUES 
('Smartphone', 'Latest model smartphone', 799.99, 'https://example.com/smartphone', 1),
('Laptop', 'High-performance laptop for work', 1299.50, 'https://example.com/laptop', 1),
('Headphones', 'Noise-cancelling headphones', 199.99, 'https://example.com/headphones', 2),
('Backpack', 'Travel backpack with ample space', 79.99, 'https://example.com/backpack', 3),
('Book: The Great Gatsby', 'Classic novel by F. Scott Fitzgerald', 14.99, 'https://example.com/great-gatsby', 4);
