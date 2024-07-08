CREATE TABLE IF NOT EXISTS `stock` (
    `stock_id` int AUTO_INCREMENT  PRIMARY KEY,
    FOREIGN KEY (`company_id`) REFERENCES `company`(`company_id`)
    `name` varchar(150) NOT NULL,
    `price_id` int, --NOT NULL
    `avoid_amount` int NOT NULL,
    );

CREATE TABLE IF NOT EXISTS `company` (
    `company_id` int AUTO_INCREMENT  PRIMARY KEY,
    `name` varchar(150) NOT NULL
    );