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
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `book` (
  `ISBN` varchar(14) NOT NULL,
  `BookTitle` varchar(150) DEFAULT NULL,
  `BookPrice` varchar(45) DEFAULT NULL,
  `BookReleaseDate` date DEFAULT NULL,
  `PUBLISHER_PublisherID` int(11) NOT NULL,
  `AUTHOR_AuthorID` int(11) NOT NULL,
  PRIMARY KEY (`ISBN`),
  KEY `fk_BOOK_AUTHOR_idx` (`AUTHOR_AuthorID`),
  KEY `fk_BOOK_PUBLISHER1_idx` (`PUBLISHER_PublisherID`),
  CONSTRAINT `fk_BOOK_AUTHOR` FOREIGN KEY (`AUTHOR_AuthorID`) REFERENCES `author` (`AuthorID`),
  CONSTRAINT `fk_BOOK_PUBLISHER1` FOREIGN KEY (`PUBLISHER_PublisherID`) REFERENCES `publisher` (`PublisherID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES ('978-0142424179','The Fault in Our Stars','8','2014-04-08',1,11),('978-0262533515','Turing\'s Vision: The Birth of Computer Science','15','2017-04-21',8,3),('978-0312169855','The Rise and Fall of the British Empire','13','1997-09-15',4,4),('978-0385736008','The Alchemyst: The Secrets of the Immortal Nicholas Flamel','12','2007-05-22',1,8),('978-0441013593','Dune','10','1965-08-01',1,2),('978-0452262935','1984','12','1949-06-08',1,12),('978-0486275437','Alice\'s Adventures in Wonderland','15','1865-11-26',4,10),('978-0618574940','The Fellowship of the Ring','10','1954-07-29',5,5),('978-0670020553','The Magicians','25','2009-05-22',1,7),('978-0670025329','Napoleon: A Life','24','2014-11-04',1,1),('978-0674979772','The Royalist Revolution: Monarchy and the American Founding','24','2017-11-20',9,9),('978-1439297254','Leviathan','10','1651-04-00',1,90),('978-1505577129','The Wealth of Nations','18','1776-03-09',2,6),('978-1975771072','Report on the Subject of Manufactures','10','1791-12-05',2,13);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
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
