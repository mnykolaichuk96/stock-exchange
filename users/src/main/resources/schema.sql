CREATE TABLE IF NOT EXISTS `user_detail` (
    `user_detail_id` int AUTO_INCREMENT  PRIMARY KEY,
    `user_id` varchar(150) NOT NULL,
    `balance` numeric(12,2) NOT NULL,
    `first_name` varchar(100) NOT NULL,
    `last_name` varchar(100) NOT NULL,
    `email` varchar(150) NOT NULL,
    `created_at` date NOT NULL,
    `created_by` varchar(20) NOT NULL,
    `updated_at` date DEFAULT NULL,
    `updated_by` varchar(20) DEFAULT NULL
    );

CREATE TABLE IF NOT EXISTS `user_stock` (
    `user_stock_id` int AUTO_INCREMENT  PRIMARY KEY,
    `stock_id` int NOT NULL,
    FOREIGN KEY (`user_detail_id`) REFERENCES `user_detail`(`user_detail_id`)
    `stock_amount` int NOT NULL
    );