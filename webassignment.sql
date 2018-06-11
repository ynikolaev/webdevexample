-- MySQL dump 10.13  Distrib 5.7.21, for macos10.13 (x86_64)
--
-- Host: localhost    Database: webassignment
-- ------------------------------------------------------
-- Server version	5.7.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Messages`
--

DROP TABLE IF EXISTS `Messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Messages` (
  `msgid` int(11) NOT NULL AUTO_INCREMENT,
  `msgdate` datetime DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`msgid`),
  KEY `userid` (`userid`),
  CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `Users` (`userid`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Messages`
--

LOCK TABLES `Messages` WRITE;
/*!40000 ALTER TABLE `Messages` DISABLE KEYS */;
INSERT INTO `Messages` VALUES (52,'2018-04-16 11:26:42','Hello World!','public',8),(53,'2018-04-16 11:27:37','Hello, Yan!','public',9),(54,'2018-04-16 11:27:44','How are you?','public',9),(55,'2018-04-16 11:29:16','Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s','public',10),(56,'2018-04-16 11:29:46','Latin is cool!!!!','public',8),(57,'2018-04-16 11:30:05','Elene will never see this! (private)','private',8),(58,'2018-04-16 11:31:43','Latin is awesome!','public',9),(59,'2018-04-16 11:41:58','Bob\'s private message','private',9),(60,'2018-05-20 18:44:50','Private Hello!','private',11),(61,'2018-05-20 18:46:26','Public hello!','public',11),(62,'2018-05-20 19:17:19','Test','public',12);
/*!40000 ALTER TABLE `Messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Users` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `Users` VALUES (8,'Yan','Nikolaev','ynikolaev@gmail.com','$2a$10$9WGUCGcdBlFMDTC5b.PfCeipVrhF1.sdjq4qAcch2o5unznctziVG'),(9,'Bob','Smith','bob@smith.com','$2a$10$Yw4GQsUNB4nPW4WBCEoo9ePSGyUAsu.npCb5P0qu3OkV70MXJpIPG'),(10,'Elene','Helen','elen@helen.com','$2a$10$G/bsU4j.xX3ZLIz06NyoH.Xl7LcAM5eOh9XOb3fexPPxzrSWbE2mu'),(11,'Brad','Baguette','brad@gmail.com','$2a$10$QkCW9ala2NfK/XrLhO33pOFkc5u70AGhyyM.tZw/zeNKXqFVdiYqK'),(12,'qwerty','qwerty','qwerty@qwerty.com','$2a$10$1oSidpGbp/Cuzm8h0SAOqeHD1E4x5kl8xevUhFZMA5sH5bZF8.7qy');
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-05-20 19:24:09
