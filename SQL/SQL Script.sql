-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Oct 19, 2021 at 07:00 AM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.3.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id16288491_fyp2`
--
CREATE DATABASE IF NOT EXISTS `id16288491_fyp2` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `id16288491_fyp2`;

-- --------------------------------------------------------

--
-- Table structure for table `advanced_feature_setting`
--

CREATE TABLE `advanced_feature_setting` (
  `adv_feature_id` int(11) NOT NULL,
  `detect_blurry` varchar(255) DEFAULT NULL,
  `detect_adult` varchar(255) DEFAULT NULL,
  `detect_size` varchar(255) DEFAULT NULL,
  `printer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `advanced_feature_setting`
--

INSERT INTO `advanced_feature_setting` (`adv_feature_id`, `detect_blurry`, `detect_adult`, `detect_size`, `printer_id`) VALUES
(4, 'Yes', 'Yes', 'No', 4),
(5, 'Yes', 'No', 'No', 1),
(7, 'No', 'No', 'Yes', 6),
(8, 'Yes', 'Yes', 'No', 7);

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE `comments` (
  `comment_id` int(11) NOT NULL,
  `cust_id` int(11) DEFAULT NULL,
  `printer_id` int(11) DEFAULT NULL,
  `comment_content` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `comments`
--

INSERT INTO `comments` (`comment_id`, `cust_id`, `printer_id`, `comment_content`) VALUES
(1, 1, 1, 'Services shit'),
(2, 4, 4, 'Services Nice!!'),
(4, 5, 4, 'Good  Service'),
(5, 1, 6, 'Nice service!!'),
(6, 1, 4, 'Good Quality of Printing');

-- --------------------------------------------------------

--
-- Table structure for table `complaint_record`
--

CREATE TABLE `complaint_record` (
  `complaint_rec_id` int(11) NOT NULL,
  `cust_id` int(11) DEFAULT NULL,
  `printer_id` int(11) DEFAULT NULL,
  `complaint_content` varchar(255) DEFAULT NULL,
  `complaint_status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `complaint_record`
--

INSERT INTO `complaint_record` (`complaint_rec_id`, `cust_id`, `printer_id`, `complaint_content`, `complaint_status`) VALUES
(1, 1, 1, 'Services bad!!', 'read'),
(2, 4, 4, 'Bad services', 'default'),
(3, 1, 1, 'Shit services', 'read'),
(4, 1, 4, 'This printer is a scam!!!!', 'read'),
(5, 1, 6, 'This service provider did not appear on time.', 'default'),
(6, 1, 6, 'shit', 'read'),
(7, 1, 4, 'Services bad!!!', 'default'),
(8, 1, 4, 'Bad services!!!', 'default'),
(9, 1, 4, 'Services bad!!!', 'default');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `cust_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`cust_id`, `user_id`) VALUES
(1, 2),
(4, 8),
(5, 11),
(6, 13),
(7, 14);

-- --------------------------------------------------------

--
-- Table structure for table `delivery_time_setting`
--

CREATE TABLE `delivery_time_setting` (
  `delivery_time_id` int(11) NOT NULL,
  `delivery_time_start` varchar(255) DEFAULT NULL,
  `printer_id` int(11) NOT NULL,
  `delivery_time_end` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `delivery_time_setting`
--

INSERT INTO `delivery_time_setting` (`delivery_time_id`, `delivery_time_start`, `printer_id`, `delivery_time_end`) VALUES
(1, 'default', 1, 'default'),
(3, '1200', 4, '1500'),
(5, '1200', 6, '1500'),
(6, '1200', 7, '1400');

-- --------------------------------------------------------

--
-- Table structure for table `delivery_zone`
--

CREATE TABLE `delivery_zone` (
  `dev_zone_id` int(11) NOT NULL,
  `delivery_zone_dist` varchar(255) DEFAULT NULL,
  `printer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `delivery_zone`
--

INSERT INTO `delivery_zone` (`dev_zone_id`, `delivery_zone_dist`, `printer_id`) VALUES
(1, '3', 4),
(3, '0', 1),
(4, '3', 6),
(5, '3', 7);

-- --------------------------------------------------------

--
-- Table structure for table `document_printing_setting`
--

CREATE TABLE `document_printing_setting` (
  `doc_printing_id` int(11) NOT NULL,
  `doc_print_pref_json` varchar(1000) DEFAULT NULL,
  `printer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `document_printing_setting`
--

INSERT INTO `document_printing_setting` (`doc_printing_id`, `doc_print_pref_json`, `printer_id`) VALUES
(1, 'default', 4),
(2, 'default', 1),
(6, '{\"ColorSelected\":[\"Color\",\"BlackWhite\"],\"Edge\":[\"ShortEdge\",\"LongEdge\"],\"PL\":[\"Portrait\",\"Landscape\"],\"Slided\":[\"One-Sided\"],\"SlidesArrangement\":[\"Horizontal\"],\"SlidesPerPage\":[\"1\",\"4\"],\"available\":\"Yes\",\"maxCopies\":\"10\",\"minCopies\":\"1\",\"pricePerBlackWhitePage\":\"0.10\",\"pricePerColorPage\":\"0.20\"}', 6),
(7, '{\"ColorSelected\":[\"Color\",\"Color\",\"Color\"],\"Edge\":[\"LongEdge\",\"LongEdge\",\"LongEdge\"],\"PL\":[\"Portrait\",\"Portrait\",\"Portrait\"],\"Slided\":[\"One-Sided\",\"One-Sided\",\"One-Sided\"],\"SlidesArrangement\":[\"Horizontal\",\"Horizontal\"],\"SlidesPerPage\":[\"2\",\"4\",\"2\",\"4\",\"2\",\"4\"],\"available\":\"Yes\",\"maxCopies\":\"50\",\"minCopies\":\"1\",\"pricePerBlackWhitePage\":\"0.30\",\"pricePerColorPage\":\"1.20\"}', 7);

-- --------------------------------------------------------

--
-- Table structure for table `image_printing_setting`
--

CREATE TABLE `image_printing_setting` (
  `img_printing_id` int(11) NOT NULL,
  `img_print_pref_json` varchar(1000) DEFAULT NULL,
  `printer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `image_printing_setting`
--

INSERT INTO `image_printing_setting` (`img_printing_id`, `img_print_pref_json`, `printer_id`) VALUES
(1, '{\"PaperSize\":[\"3.5x5(3R)\"],\"PaperType\":[\"CardStack\",\"Matte\"],\"available\":\"Yes\",\"border\":[\"No Border\",\"5cm x 5cm\",\"10cm x 10cm\"],\"maxQuantity\":\"20\",\"minQuantity\":\"3\",\"pricePerPage\":\"1.90\"}', 4),
(2, '{\"PaperSize\":[\"3.5x5(3R)\"],\"PaperType\":[\"CardStack\"],\"available\":\"Yes\",\"border\":[\"10cm x 10cm\"],\"maxQuantity\":\"20\",\"minQuantity\":\"1\",\"pricePerPage\":\"1.30\"}', 6),
(3, '{\"PaperSize\":[\"3.5x5(3R)\",\"4X6(4R)\",\"5X5(5R)\"],\"PaperType\":[\"Glossy\",\"Matte\"],\"available\":\"Yes\",\"border\":[\"No Border\",\"5cm x 5cm\"],\"maxQuantity\":\"100\",\"minQuantity\":\"1\",\"pricePerPage\":\"1.50\"}', 7);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `cust_id` int(11) NOT NULL,
  `printer_id` int(11) NOT NULL,
  `order_status` varchar(255) DEFAULT NULL,
  `delivery_mode` varchar(255) DEFAULT NULL,
  `order_date` varchar(255) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  `created_datetime` varchar(255) DEFAULT NULL,
  `order_code_reference` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `cust_id`, `printer_id`, `order_status`, `delivery_mode`, `order_date`, `time`, `created_datetime`, `order_code_reference`) VALUES
(29, 1, 4, 'Pending', 'SelfPick', '14-8-2021', '1626', '2021-08-14 06:08:47', '4[1IMG14820211626'),
(30, 1, 4, 'Pending', 'SelfPick', '14-8-2021', '1422', '2021-08-14 10:08:03', '41IMG14820211422'),
(31, 1, 6, 'Pending', 'SelfPick', '20-8-2021', '1636', '2021-08-16 07:08:18', '6YI1IMG20820211636'),
(32, 1, 6, 'Pending', 'SelfPick', '31-8-2021', '1421', '2021-08-31 03:08:27', '61IMG31820211421'),
(33, 1, 7, 'Pending', 'Delivery', '18-9-2021', '1315', '2021-09-08 07:09:20', '7<1DOC18920211315');

-- --------------------------------------------------------

--
-- Table structure for table `order_address`
--

CREATE TABLE `order_address` (
  `order_add_id` int(11) NOT NULL,
  `customer_address` varchar(255) DEFAULT NULL,
  `distance` double DEFAULT NULL,
  `printer_address` varchar(255) DEFAULT NULL,
  `order_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `order_address`
--

INSERT INTO `order_address` (`order_add_id`, `customer_address`, `distance`, `printer_address`, `order_id`) VALUES
(19, '', 0, '177,Jalan Perak 10,Taman Bandar Baru Selatan,31900,Kampar,Perak', 29),
(20, '', 0, '177,Jalan Perak 10,Taman Bandar Baru Selatan,31900,Kampar,Perak', 30),
(21, '', 0, '176,Jln5,Kampar,Perak', 31),
(22, '', 0, '176,Jln5,Kampar,Perak', 32),
(23, 'Lot 21949, Jalan Batu Karang, Taman Bandar Baru, 31900,Kampar, Perak', 0, '177, Jalan Perak 10, Bandar Baru Selatan, 31900, Kampar, Perak', 33);

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE `payment` (
  `payment_id` int(11) NOT NULL,
  `payment_cost` varchar(255) DEFAULT NULL,
  `payment_type` varchar(255) DEFAULT NULL,
  `payment_status` varchar(255) DEFAULT NULL,
  `order_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `payment`
--

INSERT INTO `payment` (`payment_id`, `payment_cost`, `payment_type`, `payment_status`, `order_id`) VALUES
(30, '43.40', 'OnlinePayment', 'Pending', 29),
(31, '44.80', 'OnlinePayment', 'Pending', 30),
(32, '15.60', 'OnlinePayment', 'Pending', 31),
(33, '13.00', 'OnlinePayment', 'Pending', 32),
(34, '36.0', 'OnlinePayment', 'Pending', 33);

-- --------------------------------------------------------

--
-- Table structure for table `payment_setting`
--

CREATE TABLE `payment_setting` (
  `payment_setting_id` int(11) NOT NULL,
  `online_payment` varchar(255) DEFAULT NULL,
  `COD` varchar(255) DEFAULT NULL,
  `printer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `payment_setting`
--

INSERT INTO `payment_setting` (`payment_setting_id`, `online_payment`, `COD`, `printer_id`) VALUES
(1, 'Yes', 'Yes', 4),
(2, 'No', 'Yes', 1),
(4, 'Yes', 'Yes', 6),
(5, 'Yes', 'No', 7);

-- --------------------------------------------------------

--
-- Table structure for table `pbe_bank_receipt`
--

CREATE TABLE `pbe_bank_receipt` (
  `bank_receipt_id` int(11) NOT NULL,
  `bank_receipt_name` varchar(255) NOT NULL,
  `payment_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `preparation_status`
--

CREATE TABLE `preparation_status` (
  `prep_status_id` int(11) NOT NULL,
  `prep_status` varchar(255) NOT NULL,
  `order_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `preparation_status`
--

INSERT INTO `preparation_status` (`prep_status_id`, `prep_status`, `order_id`) VALUES
(39, 'Completed', 29),
(40, 'Pending', 30),
(41, 'Accepted', 31),
(42, 'Pending', 32),
(43, 'Completed', 33);

-- --------------------------------------------------------

--
-- Table structure for table `printer`
--

CREATE TABLE `printer` (
  `printer_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `printer`
--

INSERT INTO `printer` (`printer_id`, `user_id`) VALUES
(1, 3),
(4, 9),
(6, 12),
(7, 16);

-- --------------------------------------------------------

--
-- Table structure for table `product_printing_preferences`
--

CREATE TABLE `product_printing_preferences` (
  `printing_pref_id` int(11) NOT NULL,
  `printing_preferences` varchar(255) DEFAULT NULL,
  `sub_order_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `product_printing_preferences`
--

INSERT INTO `product_printing_preferences` (`printing_pref_id`, `printing_preferences`, `sub_order_id`) VALUES
(23, '{\"Copies\":\"12\",\"PaperSize\":\"3.5x5(3R)\",\"PaperType\":\"CardStack\",\"border\":\"10cm x 10cm\"}', 30),
(24, '{\"Copies\":\"12\",\"PaperSize\":\"3.5x5(3R)\",\"PaperType\":\"CardStack\",\"border\":\"10cm x 10cm\"}', 31),
(25, '{\"Copies\":\"12\",\"PaperSize\":\"3.5x5(3R)\",\"PaperType\":\"CardStack\",\"border\":\"10cm x 10cm\"}', 32),
(26, '{\"Copies\":\"12\",\"PaperSize\":\"3.5x5(3R)\",\"PaperType\":\"CardStack\",\"border\":\"10cm x 10cm\"}', 33),
(27, '{\"Copies\":\"12\",\"PaperSize\":\"3.5x5(3R)\",\"PaperType\":\"CardStack\",\"border\":\"10cm x 10cm\"}', 34),
(28, '{\"Copies\":\"10\",\"PaperSize\":\"3.5x5(3R)\",\"PaperType\":\"CardStack\",\"border\":\"10cm x 10cm\"}', 35),
(29, '{\"ColorSelected\":\"Color\",\"Copies\":\"15\",\"Edge\":\"LongEdge\",\"PL\":\"Portrait\",\"PageRange\":\"All\",\"SlidePerPage\":\"2\",\"Slided\":\"One-Sided\",\"SlidesArrangment\":\"Horizontal\"}', 36);

-- --------------------------------------------------------

--
-- Table structure for table `rating`
--

CREATE TABLE `rating` (
  `rating_id` int(11) NOT NULL,
  `cust_id` int(11) DEFAULT NULL,
  `printer_id` int(11) DEFAULT NULL,
  `rating_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `rating`
--

INSERT INTO `rating` (`rating_id`, `cust_id`, `printer_id`, `rating_value`) VALUES
(101, 1, 1, 3),
(113, 1, 7, 3),
(114, 1, 6, 3),
(115, 7, 4, 4),
(116, 7, 6, 4),
(117, 1, 4, 5);

-- --------------------------------------------------------

--
-- Table structure for table `resources_record`
--

CREATE TABLE `resources_record` (
  `resources_id` int(11) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_description` varchar(255) DEFAULT NULL,
  `sub_order_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `resources_record`
--

INSERT INTO `resources_record` (`resources_id`, `file_name`, `file_type`, `file_description`, `sub_order_id`) VALUES
(30, 'Apple.png', 'Image', 'default', 30),
(31, 'Orange.png', 'Image', 'default', 31),
(32, 'Apple.png', 'Image', 'default', 32),
(33, 'Orange.png', 'Image', 'default', 33),
(34, '66314', 'Image', 'default', 34),
(35, '69667', 'Image', 'default', 35),
(36, 'Frequently-Asked-Question-MUET.pdf', 'Document', 'default', 36);

-- --------------------------------------------------------

--
-- Table structure for table `shipping_option`
--

CREATE TABLE `shipping_option` (
  `ship_option_id` int(11) NOT NULL,
  `delivery_avail` varchar(255) DEFAULT NULL,
  `self_pick_up_avail` varchar(255) DEFAULT NULL,
  `printer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `shipping_option`
--

INSERT INTO `shipping_option` (`ship_option_id`, `delivery_avail`, `self_pick_up_avail`, `printer_id`) VALUES
(2, 'Yes', 'Yes', 4),
(4, 'default', 'Yes', 1),
(5, 'Yes', 'Yes', 6),
(6, 'Yes', 'No', 7);

-- --------------------------------------------------------

--
-- Table structure for table `sub_orders`
--

CREATE TABLE `sub_orders` (
  `sub_order_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `cost` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `sub_orders`
--

INSERT INTO `sub_orders` (`sub_order_id`, `order_id`, `cost`) VALUES
(30, 29, '16.80'),
(31, 29, '26.60'),
(32, 30, '16.80'),
(33, 30, '28.00'),
(34, 31, '15.60'),
(35, 32, '13.00'),
(36, 33, '36.0');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `user_email` varchar(255) DEFAULT NULL,
  `user_role` varchar(255) DEFAULT NULL,
  `user_address` varchar(255) DEFAULT NULL,
  `user_hp` varchar(255) DEFAULT NULL,
  `user_password` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `bank_references` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `username`, `user_email`, `user_role`, `user_address`, `user_hp`, `user_password`, `status`, `bank_references`) VALUES
(1, 'admin', 'admin@gmail.com', 'admin', NULL, NULL, 'fyp2021', 'Approved', NULL),
(2, 'Elon Wan', 'elonwan@gmail.com', 'customer', 'Lot 21949, Jalan Batu Karang, Taman Bandar Baru, 31900,Kampar, Perak', '012-2727853', 'fyp2021', 'Blocked', 'default'),
(3, 'Jimmy Wan', 'jimmywan@gmail.com', 'printer', '172, Jln Bandar Baru,Kampar ,Perak', '017-7113456', 'fyp2021', 'Blocked', '6904578304 '),
(8, 'wan kar hou', 'wankarhou@gmail.com', 'customer', '177,Jalan Perak 10,Taman Bandar Baru Selatan,31900,Kampar,Perak', '011-1234567', 'fyp2021', 'Approved', 'default'),
(9, 'John Wan', 'johndoe@gmail.com', 'printer', '185,Jalan Perak 10,Taman Bandar Baru Selatan,31900,Kampar,Perak', '014-3304567', 'fyp2021', 'Approved', '6904578304'),
(11, 'james wan', 'jameswan@gmail.com', 'customer', '177,Jalan Perak 12, 31900, Kampar', '0125727733', 'fyp2021', 'Approved', 'default'),
(12, 'richardwan', 'richardwan@gmail.com', 'printer', '176,Jalan Perak6,Bandar Baru,Kampar,Perak', '012-5867890', 'fyp2022', 'Approved', '6904567890'),
(13, 'JohnnyWan', 'johnnywan@gmail.com', 'customer', '156,Jln 10, Kampar, Perak', '013-4456789', 'fyp2021', 'Approved', '6904578304 '),
(14, 'DannyWan', 'dannywan@gmail.com', 'customer', '121, Jalan Perak 10, Taman Bandar Baru, 31900, Kampar, Perak', '012-7123456', 'fyp2021', 'Approved', ''),
(16, 'KelvinWan', 'kelvinwan@gmail.com', 'printer', '177, Jalan Perak 10, Bandar Baru Selatan, 31900, Kampar, Perak', '012-6678945', 'fyp2021', 'Approved', '6900571234');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `advanced_feature_setting`
--
ALTER TABLE `advanced_feature_setting`
  ADD PRIMARY KEY (`adv_feature_id`),
  ADD KEY `printer_advanced` (`printer_id`);

--
-- Indexes for table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`comment_id`),
  ADD KEY `customer_comment` (`cust_id`),
  ADD KEY `printer_comment` (`printer_id`);

--
-- Indexes for table `complaint_record`
--
ALTER TABLE `complaint_record`
  ADD PRIMARY KEY (`complaint_rec_id`),
  ADD KEY `customer_complaint` (`cust_id`),
  ADD KEY `printer_complaint` (`printer_id`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`cust_id`),
  ADD KEY `user_customer` (`user_id`);

--
-- Indexes for table `delivery_time_setting`
--
ALTER TABLE `delivery_time_setting`
  ADD PRIMARY KEY (`delivery_time_id`),
  ADD KEY `printer_delivery` (`printer_id`);

--
-- Indexes for table `delivery_zone`
--
ALTER TABLE `delivery_zone`
  ADD PRIMARY KEY (`dev_zone_id`),
  ADD KEY `printer_delivery_zone` (`printer_id`);

--
-- Indexes for table `document_printing_setting`
--
ALTER TABLE `document_printing_setting`
  ADD PRIMARY KEY (`doc_printing_id`),
  ADD KEY `Printer_DOC` (`printer_id`);

--
-- Indexes for table `image_printing_setting`
--
ALTER TABLE `image_printing_setting`
  ADD PRIMARY KEY (`img_printing_id`),
  ADD KEY `Printer_IMG` (`printer_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `printer&orders` (`printer_id`),
  ADD KEY `customer&orders` (`cust_id`);

--
-- Indexes for table `order_address`
--
ALTER TABLE `order_address`
  ADD PRIMARY KEY (`order_add_id`),
  ADD KEY `orders_N_orderAddress` (`order_id`);

--
-- Indexes for table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`payment_id`),
  ADD KEY `orders_N_payment` (`order_id`);

--
-- Indexes for table `payment_setting`
--
ALTER TABLE `payment_setting`
  ADD PRIMARY KEY (`payment_setting_id`),
  ADD KEY `printer_paymentSetting` (`printer_id`);

--
-- Indexes for table `pbe_bank_receipt`
--
ALTER TABLE `pbe_bank_receipt`
  ADD PRIMARY KEY (`bank_receipt_id`),
  ADD KEY `PBE_Payment` (`payment_id`);

--
-- Indexes for table `preparation_status`
--
ALTER TABLE `preparation_status`
  ADD PRIMARY KEY (`prep_status_id`),
  ADD KEY `order_preparationStatus` (`order_id`);

--
-- Indexes for table `printer`
--
ALTER TABLE `printer`
  ADD PRIMARY KEY (`printer_id`),
  ADD KEY `user_printer` (`user_id`);

--
-- Indexes for table `product_printing_preferences`
--
ALTER TABLE `product_printing_preferences`
  ADD PRIMARY KEY (`printing_pref_id`),
  ADD KEY `subOrders_ppp` (`sub_order_id`);

--
-- Indexes for table `rating`
--
ALTER TABLE `rating`
  ADD PRIMARY KEY (`rating_id`),
  ADD KEY `customer_rating` (`cust_id`),
  ADD KEY `printer_rating` (`printer_id`);

--
-- Indexes for table `resources_record`
--
ALTER TABLE `resources_record`
  ADD PRIMARY KEY (`resources_id`),
  ADD KEY `resources_subOrders` (`sub_order_id`);

--
-- Indexes for table `shipping_option`
--
ALTER TABLE `shipping_option`
  ADD PRIMARY KEY (`ship_option_id`),
  ADD KEY `printer_shipping` (`printer_id`);

--
-- Indexes for table `sub_orders`
--
ALTER TABLE `sub_orders`
  ADD PRIMARY KEY (`sub_order_id`),
  ADD KEY `orders&sub_orders` (`order_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `advanced_feature_setting`
--
ALTER TABLE `advanced_feature_setting`
  MODIFY `adv_feature_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `comments`
--
ALTER TABLE `comments`
  MODIFY `comment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `complaint_record`
--
ALTER TABLE `complaint_record`
  MODIFY `complaint_rec_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `cust_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `delivery_time_setting`
--
ALTER TABLE `delivery_time_setting`
  MODIFY `delivery_time_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `delivery_zone`
--
ALTER TABLE `delivery_zone`
  MODIFY `dev_zone_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `document_printing_setting`
--
ALTER TABLE `document_printing_setting`
  MODIFY `doc_printing_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `image_printing_setting`
--
ALTER TABLE `image_printing_setting`
  MODIFY `img_printing_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `order_address`
--
ALTER TABLE `order_address`
  MODIFY `order_add_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `payment`
--
ALTER TABLE `payment`
  MODIFY `payment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT for table `payment_setting`
--
ALTER TABLE `payment_setting`
  MODIFY `payment_setting_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `pbe_bank_receipt`
--
ALTER TABLE `pbe_bank_receipt`
  MODIFY `bank_receipt_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `preparation_status`
--
ALTER TABLE `preparation_status`
  MODIFY `prep_status_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT for table `printer`
--
ALTER TABLE `printer`
  MODIFY `printer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `product_printing_preferences`
--
ALTER TABLE `product_printing_preferences`
  MODIFY `printing_pref_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `rating`
--
ALTER TABLE `rating`
  MODIFY `rating_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=118;

--
-- AUTO_INCREMENT for table `resources_record`
--
ALTER TABLE `resources_record`
  MODIFY `resources_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `shipping_option`
--
ALTER TABLE `shipping_option`
  MODIFY `ship_option_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `sub_orders`
--
ALTER TABLE `sub_orders`
  MODIFY `sub_order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `advanced_feature_setting`
--
ALTER TABLE `advanced_feature_setting`
  ADD CONSTRAINT `printer_advanced` FOREIGN KEY (`printer_id`) REFERENCES `printer` (`printer_id`);

--
-- Constraints for table `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `customer_comment` FOREIGN KEY (`cust_id`) REFERENCES `customer` (`cust_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `printer_comment` FOREIGN KEY (`printer_id`) REFERENCES `printer` (`printer_id`);

--
-- Constraints for table `complaint_record`
--
ALTER TABLE `complaint_record`
  ADD CONSTRAINT `customer_complaint` FOREIGN KEY (`cust_id`) REFERENCES `customer` (`cust_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `printer_complaint` FOREIGN KEY (`printer_id`) REFERENCES `printer` (`printer_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `customer`
--
ALTER TABLE `customer`
  ADD CONSTRAINT `user_customer` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `delivery_time_setting`
--
ALTER TABLE `delivery_time_setting`
  ADD CONSTRAINT `printer_delivery` FOREIGN KEY (`printer_id`) REFERENCES `printer` (`printer_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `delivery_zone`
--
ALTER TABLE `delivery_zone`
  ADD CONSTRAINT `printer_delivery_zone` FOREIGN KEY (`printer_id`) REFERENCES `printer` (`printer_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `document_printing_setting`
--
ALTER TABLE `document_printing_setting`
  ADD CONSTRAINT `Printer_DOC` FOREIGN KEY (`printer_id`) REFERENCES `printer` (`printer_id`);

--
-- Constraints for table `image_printing_setting`
--
ALTER TABLE `image_printing_setting`
  ADD CONSTRAINT `Printer_IMG` FOREIGN KEY (`printer_id`) REFERENCES `printer` (`printer_id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `customer&orders` FOREIGN KEY (`cust_id`) REFERENCES `customer` (`cust_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `printer&orders` FOREIGN KEY (`printer_id`) REFERENCES `printer` (`printer_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `order_address`
--
ALTER TABLE `order_address`
  ADD CONSTRAINT `orders_N_orderAddress` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`);

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `orders_N_payment` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`);

--
-- Constraints for table `payment_setting`
--
ALTER TABLE `payment_setting`
  ADD CONSTRAINT `printer_paymentSetting` FOREIGN KEY (`printer_id`) REFERENCES `printer` (`printer_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `pbe_bank_receipt`
--
ALTER TABLE `pbe_bank_receipt`
  ADD CONSTRAINT `PBE_Payment` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`payment_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `preparation_status`
--
ALTER TABLE `preparation_status`
  ADD CONSTRAINT `order_preparationStatus` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`);

--
-- Constraints for table `printer`
--
ALTER TABLE `printer`
  ADD CONSTRAINT `user_printer` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `product_printing_preferences`
--
ALTER TABLE `product_printing_preferences`
  ADD CONSTRAINT `subOrders_ppp` FOREIGN KEY (`sub_order_id`) REFERENCES `sub_orders` (`sub_order_id`);

--
-- Constraints for table `rating`
--
ALTER TABLE `rating`
  ADD CONSTRAINT `customer_rating` FOREIGN KEY (`cust_id`) REFERENCES `customer` (`cust_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `printer_rating` FOREIGN KEY (`printer_id`) REFERENCES `printer` (`printer_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `resources_record`
--
ALTER TABLE `resources_record`
  ADD CONSTRAINT `resources_subOrders` FOREIGN KEY (`sub_order_id`) REFERENCES `sub_orders` (`sub_order_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `shipping_option`
--
ALTER TABLE `shipping_option`
  ADD CONSTRAINT `printer_shipping` FOREIGN KEY (`printer_id`) REFERENCES `printer` (`printer_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `sub_orders`
--
ALTER TABLE `sub_orders`
  ADD CONSTRAINT `orders&sub_orders` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
