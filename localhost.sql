-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Mar 29, 2024 at 12:47 AM
-- Server version: 10.5.20-MariaDB
-- PHP Version: 7.3.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id21090089_participantmanager`
--
CREATE DATABASE IF NOT EXISTS `id21090089_participantmanager` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `id21090089_participantmanager`;

-- --------------------------------------------------------

--
-- Table structure for table `participants`
--

CREATE TABLE `participants` (
  `id` int(100) NOT NULL,
  `name` text DEFAULT NULL,
  `gender` text DEFAULT NULL,
  `status` text DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `date_of_death` date DEFAULT NULL,
  `occupation` text DEFAULT NULL,
  `hobbies` text DEFAULT NULL,
  `upload` int(1) DEFAULT 2,
  `modifydate` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `participants`
--

INSERT INTO `participants` (`id`, `name`, `gender`, `status`, `date_of_birth`, `date_of_death`, `occupation`, `hobbies`, `upload`, `modifydate`) VALUES
(1, 'Hafiz ', 'Female', 'Live', '2005-10-12', '0000-00-00', 'Doctor', 'Hobby1,Hobby3', 1, '2024-03-28 20:25:40'),
(2, 'Jamal', 'Male', 'Death', '2021-09-30', '2023-11-14', 'Student', 'Hobby1', 1, '2024-03-28 20:25:40'),
(3, 'Enam1', 'Female', 'Live', '2023-08-10', '2022-10-05', 'Artist', 'Hobby1', 1, '2024-03-28 20:25:40'),
(4, 'Enam3', 'Male', 'Death', '2023-08-10', '2023-11-14', 'Teacher', 'Hobby1', 1, '2024-03-28 20:25:40'),
(5, 'Enam4', 'Male', 'Live', '2023-08-09', '0000-00-00', 'Doctor', 'Hobby1', 1, '2024-03-28 20:25:40'),
(6, 'Rofiq New', 'Male', 'Live', '2023-10-03', '0000-00-00', 'Doctor', 'Hobby1', 1, '2024-03-28 20:25:40'),
(7, 'Jaman', 'Male', 'Live', '2014-10-05', '0000-00-00', 'Other', 'Hobby1', 1, '2024-03-28 20:25:40'),
(8, 'Sumon', 'Female', 'Live', '2011-10-12', '0000-00-00', 'Artist', 'Hobby2', 1, '2024-03-28 20:25:40'),
(9, 'Papon Mia', 'Male', 'Live', '2023-10-12', '0000-00-00', 'Artist', 'Hobby1,Hobby3', 1, '2024-03-28 20:25:40'),
(10, 'Yeamin', 'Male', 'Death', '2023-10-20', '2023-11-14', 'Artist', 'Hobby1,Hobby2', 1, '2024-03-28 20:25:40'),
(11, 'irfan', 'Male', 'Live', '2015-10-10', '0000-00-00', 'Student', 'Hobby1', 1, '2024-03-28 20:25:40');

--
-- Triggers `participants`
--
DELIMITER $$
CREATE TRIGGER `update_modifydate` BEFORE UPDATE ON `participants` FOR EACH ROW SET NEW.modifydate = DATE_ADD(NOW(), INTERVAL 6 HOUR)
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `update_upload_trigger` BEFORE UPDATE ON `participants` FOR EACH ROW BEGIN
     IF NEW.name <> OLD.name OR
        NEW.gender <> OLD.gender OR
        NEW.status <> OLD.status OR
        NEW.date_of_birth <> OLD.date_of_birth OR
        NEW.date_of_death <> OLD.date_of_death OR
        NEW.occupation <> OLD.occupation OR
        NEW.hobbies <> OLD.hobbies THEN
       SET NEW.upload = 2;
     END IF;
   END
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `participants`
--
ALTER TABLE `participants`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `participants`
--
ALTER TABLE `participants`
  MODIFY `id` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=229;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
