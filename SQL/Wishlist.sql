CREATE DATABASE IF NOT EXISTS wishlist;
CREATE SCHEMA IF NOT EXISTS wishlist;
USE wishlist;

CREATE TABLE user (
	user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUEuser,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL
);

CREATE TABLE wishlist (
	wishlist_id INT AUTO_INCREMENT PRIMARY KEY,
    wishlist_name VARCHAR(100),
    user_id  INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE wish (
	wish_id INT AUTO_INCREMENT PRIMARY KEY,
    wish_name VARCHAR(100) NOT NULL,
    wish_description TEXT,
    wish_price DECIMAL(10, 2),
    wish_link VARCHAR(255),
    wishlist_id INT NOT NULL,
    FOREIGN KEY (wishlist_id) REFERENCES wishlist(wishlist_id)
);
