-- MySQL dump 10.13  Distrib 8.0.16, for Win64 (x86_64)
--
-- Host: localhost    Database: bookdb
-- ------------------------------------------------------
-- Server version	8.0.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `shoppingcart_book`
--

DROP TABLE IF EXISTS `shoppingcart_book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `shoppingcart_book` (
  `BOOK_ISBN` varchar(14) NOT NULL,
  `SHOPPINGCART_ShoppingCartID` int(11) NOT NULL,
  `BookCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`BOOK_ISBN`,`SHOPPINGCART_ShoppingCartID`),
  KEY `fk_SHOPPINGCART_BOOK_SHOPPINGCART1_idx` (`SHOPPINGCART_ShoppingCartID`),
  CONSTRAINT `fk_SHOPPINGCART_BOOK_BOOK1` FOREIGN KEY (`BOOK_ISBN`) REFERENCES `book` (`ISBN`),
  CONSTRAINT `fk_SHOPPINGCART_BOOK_SHOPPINGCART1` FOREIGN KEY (`SHOPPINGCART_ShoppingCartID`) REFERENCES `shoppingcart` (`ShoppingCartID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shoppingcart_book`
--

LOCK TABLES `shoppingcart_book` WRITE;
/*!40000 ALTER TABLE `shoppingcart_book` DISABLE KEYS */;
/*!40000 ALTER TABLE `shoppingcart_book` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-12-03 14:44:24
