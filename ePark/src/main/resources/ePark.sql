-- MySQL dump 10.13  Distrib 8.0.21, for macos10.15 (x86_64)
--
-- Host: 127.0.0.1    Database: ePark
-- ------------------------------------------------------
-- Server version	8.0.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `bookingId` bigint NOT NULL AUTO_INCREMENT,
  `endDate` date DEFAULT NULL,
  `endTime` time DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `startTime` time DEFAULT NULL,
  `carParkSpotId` bigint DEFAULT NULL,
  `paymentId` varchar(100) DEFAULT NULL,
  `userId` bigint DEFAULT NULL,
  `vehicleId` bigint DEFAULT NULL,
  `isDisabled` bit(1) NOT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `bookingCreated` datetime(4) DEFAULT NULL,
  `carParkId` bigint NOT NULL,
  `bookingStatus` varchar(255) DEFAULT NULL,
  `unitPrice` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`bookingId`),
  KEY `FK58fq8e3eckm2dsk2oawscj2ki` (`carParkSpotId`),
  KEY `FKqocmgav9d0ni97nayim9l5xio` (`paymentId`),
  KEY `FK2rgb3wqoda92euxecb2agubec` (`userId`),
  KEY `FKo7jpgisspn3jgit6el441g8t7` (`vehicleId`),
  KEY `FKkunara0gq9wvteytx4c29a3r0` (`carParkId`),
  CONSTRAINT `FK2rgb3wqoda92euxecb2agubec` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`),
  CONSTRAINT `FK58fq8e3eckm2dsk2oawscj2ki` FOREIGN KEY (`carParkSpotId`) REFERENCES `carParkSpots` (`carParkSpotId`),
  CONSTRAINT `FKkunara0gq9wvteytx4c29a3r0` FOREIGN KEY (`carParkId`) REFERENCES `carParks` (`carParkId`),
  CONSTRAINT `FKo7jpgisspn3jgit6el441g8t7` FOREIGN KEY (`vehicleId`) REFERENCES `vehicles` (`vehicleId`)
) ENGINE=InnoDB AUTO_INCREMENT=173 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES (7,'2020-12-27','02:15:00','2020-12-27','00:15:00',87,'pi_1I33ClDkduQieys243CGvZaE',1,2,_binary '',16.00,'2020-12-02 00:00:00.0000',12,'ACTIVE',NULL),(8,'2021-01-05','13:45:00','2021-01-05','12:45:00',87,'pi_1I5BooDkduQieys2hFb3yZS0',1,2,_binary '',8.00,'2021-01-02 00:00:00.0000',12,'ACTIVE',NULL),(9,'2021-01-30','03:15:00','2021-01-30','00:15:00',87,'pi_1IFNg6DkduQieys2g7Q28ESg',1,2,_binary '',24.00,'2021-01-30 00:00:00.0000',12,'ACTIVE',NULL),(10,'2021-01-30','01:00:00','2021-01-30','00:00:00',88,'pi_1IFOoHDkduQieys25MtV4Yfh',1,2,_binary '',8.00,'2021-01-30 00:00:00.0000',12,'ACTIVE',NULL),(11,'2021-01-30','01:15:00','2021-01-30','00:15:00',89,'pi_1IFOrHDkduQieys2bvWmMc9i',1,2,_binary '',8.00,'2021-01-30 00:00:00.0000',12,'ACTIVE',NULL),(12,'2021-01-30','03:15:00','2021-01-30','01:00:00',88,'pi_1IFPJZDkduQieys2bmmWBdaO',1,2,_binary '',16.00,'2021-01-30 00:00:00.0000',12,'ACTIVE',NULL),(13,'2021-01-31','04:30:00','2021-01-31','00:30:00',87,'pi_1IFPYwDkduQieys22mbrORvS',1,2,_binary '',32.00,'2021-01-30 00:00:00.0000',12,'CANCELLED',NULL),(14,'2021-01-30','03:15:00','2021-01-30','00:15:00',102,'pi_1IFRCqDkduQieys26Uy6jODg',1,2,_binary '',15.00,'2021-01-30 00:00:00.0000',14,'ACTIVE',NULL),(15,'2021-01-31','02:30:00','2021-01-31','00:30:00',88,'pi_1IFgA1DkduQieys20M9YFUGt',1,2,_binary '',16.00,'2021-01-31 00:00:00.0000',12,'ACTIVE',NULL),(16,'2021-02-04','20:45:00','2021-02-04','20:15:00',87,'pi_1IHEojDkduQieys2pr5kJIS8',1,2,_binary '',8.00,'2021-02-04 00:00:00.0000',12,'ACTIVE',NULL),(17,'2021-02-25','21:15:00','2021-02-25','20:15:00',87,'pi_1IOfjEDkduQieys2mOQxQ1BO',1,2,_binary '',16.00,'2021-02-25 00:00:00.0000',12,'ACTIVE',NULL),(18,'2021-02-26','14:15:00','2021-02-26','13:15:00',87,'pi_1IOflGDkduQieys2lJF6M0Zr',1,2,_binary '',16.00,'2021-02-25 00:00:00.0000',12,'ACTIVE',NULL),(19,'2021-02-25','23:00:00','2021-02-25','20:30:00',88,'pi_1IOgvuDkduQieys2K5hFece1',1,2,_binary '',40.00,'2021-02-25 00:00:00.0000',12,'ACTIVE',NULL),(20,'2021-03-01','03:45:00','2021-03-01','03:15:00',91,'pi_1IPoEIDkduQieys2lS05kD4k',1,2,_binary '',8.00,'2021-02-28 00:00:00.0000',12,'ACTIVE',NULL),(21,'2021-03-01','04:30:00','2021-03-01','03:30:00',88,'pi_1IPrfADkduQieys20BObGZUE',1,2,_binary '',16.00,'2021-02-28 00:00:00.0000',12,'ACTIVE',NULL),(28,'2021-03-04','20:30:00','2021-03-04','20:00:00',87,'pi_1IRMGYDkduQieys2NLyKZ7rk',1,2,_binary '',8.00,'2021-03-04 00:00:00.0000',12,'ACTIVE',NULL),(29,'2021-04-01','20:30:00','2021-04-01','20:00:00',87,'pi_1IS0n1DkduQieys22DKIYtkV',1,2,_binary '',8.00,'2021-03-06 00:00:00.0000',12,'ACTIVE',NULL),(30,'2021-03-08','03:30:00','2021-03-08','03:00:00',87,'pi_1IS1VMDkduQieys2TZcpTGxE',1,2,_binary '',8.00,'2021-03-06 00:00:00.0000',12,'CANCELLED',NULL),(31,'2021-03-10','09:30:00','2021-03-10','08:30:00',88,'pi_1ITNJ8DkduQieys2ppOAEMYC',1,2,_binary '',16.00,'2021-03-10 00:00:00.0000',12,'ACTIVE',NULL),(32,'2021-03-10','10:15:00','2021-03-10','08:45:00',91,'pi_1ITNTdDkduQieys2dIEV3IR3',1,2,_binary '',24.00,'2021-03-10 00:00:00.0000',12,'CANCELLED',NULL),(33,'2021-03-15','12:00:00','2021-03-15','04:00:00',87,'pi_1IV1VdDkduQieys2msIm4oGa',1,2,_binary '',128.00,'2021-03-14 21:30:01.5000',12,'ACTIVE',NULL),(34,'2021-03-16','13:00:00','2021-03-16','12:30:00',87,'pi_1IVGvoDkduQieys2hPaiwI6f',1,2,_binary '',8.00,'2021-03-15 13:58:04.1530',12,'ACTIVE',NULL),(35,'2021-03-16','13:30:00','2021-03-16','13:00:00',87,'pi_1IVGwBDkduQieys2tYpri4PN',1,2,_binary '',8.00,'2021-03-15 13:58:27.0220',12,'ACTIVE',NULL),(36,'2021-03-16','13:30:00','2021-03-16','12:30:00',88,'pi_1IVGxaDkduQieys2LqzCDvdd',1,2,_binary '',16.00,'2021-03-15 13:59:54.4730',12,'ACTIVE',NULL),(37,'2021-03-16','19:45:00','2021-03-16','19:15:00',87,'pi_1IVi8hDkduQieys2HgoOVs7K',1,2,_binary '',5.69,'2021-03-16 19:01:11.7330',12,'ACTIVE',NULL),(38,'2021-03-16','22:15:00','2021-03-16','21:45:00',87,'pi_1IVkYmDkduQieys2DrobCO2D',1,2,_binary '',5.70,'2021-03-16 21:36:16.2060',12,'ACTIVE',NULL),(57,'2021-03-24','12:00:00','2021-03-24','11:00:00',88,'pi_1IYC4wDkduQieys232eUNi1k',1,2,_binary '',12.92,'2021-03-23 15:23:36.6430',12,'ACTIVE',6.46),(58,'2021-03-24','12:00:00','2021-03-24','11:00:00',89,'pi_1IYC50DkduQieys2hA2hG9Yy',1,2,_binary '',14.46,'2021-03-23 15:23:40.4300',12,'ACTIVE',7.23),(62,'2021-03-24','12:00:00','2021-03-24','11:00:00',87,'pi_1IYDxNDkduQieys20sM962J9',1,2,_binary '',14.54,'2021-03-23 17:23:55.8070',12,'ACTIVE',7.27),(67,'2021-04-09','16:15:00','2021-04-09','15:45:00',87,'pi_1IeLQxDkduQieys2tkSdwgOp',1,2,_binary '',5.60,'2021-04-09 14:35:46.6760',12,'ACTIVE',5.60),(68,'2021-04-09','16:15:00','2021-04-09','15:45:00',88,'pi_1IeLWcDkduQieys2XOTjlRjr',1,2,_binary '',6.41,'2021-04-09 14:41:36.4520',12,'ACTIVE',6.41),(69,'2021-04-09','16:45:00','2021-04-09','16:15:00',87,'pi_1IeLeXDkduQieys2jO0x8wVE',1,2,_binary '',5.63,'2021-04-09 14:49:48.1790',12,'ACTIVE',5.63),(70,'2021-04-09','16:30:00','2021-04-09','16:00:00',89,'pi_1IeLi8DkduQieys28uCA31rm',1,2,_binary '',7.25,'2021-04-09 14:53:30.5990',12,'CANCELLED',7.25),(93,'2021-04-21','04:00:00','2021-04-21','03:00:00',87,'pi_1IiNBoDkduQieys2yftlSZvX',1,1,_binary '',11.20,'2021-04-20 17:16:46.9760',12,'ACTIVE',5.60),(97,'2021-04-22','04:00:00','2021-04-22','03:00:00',87,'pi_1IiOFeDkduQieys2mdG6YA1U',1,1,_binary '',11.26,'2021-04-20 18:24:55.5450',12,'ACTIVE',5.63),(104,'2021-04-30','05:15:00','2021-04-30','03:15:00',87,'pi_1IlacmDkduQieys2GmQqxos0',1,1,_binary '',22.40,'2021-04-29 14:14:00.3310',12,'CANCELLED',5.60),(155,'2021-05-07','03:30:00','2021-05-07','03:00:00',87,'pi_1Io3acDkduQieys2ZZZGD1Zq',1,1,_binary '',5.60,'2021-05-06 10:33:57.9010',12,'ACTIVE',5.60),(169,'2021-05-13','10:00:00','2021-05-13','09:00:00',92,'pi_1IpD2rDkduQieys25a4MafPN',26,130,_binary '\0',10.00,'2021-05-09 14:51:46.9470',13,'ACTIVE',10.00),(170,'2021-05-13','01:30:00','2021-05-13','00:30:00',92,'pi_1IpD2gDkduQieys2EZcYvuwy',1,1,_binary '\0',10.00,'2021-05-09 14:51:43.9770',13,'ACTIVE',10.00);
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carParkComments`
--

DROP TABLE IF EXISTS `carParkComments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carParkComments` (
  `carParkCommentId` bigint NOT NULL AUTO_INCREMENT,
  `comment` varchar(255) DEFAULT NULL,
  `dateCreated` datetime(6) DEFAULT NULL,
  `carParkId` bigint NOT NULL,
  `userId` bigint NOT NULL,
  PRIMARY KEY (`carParkCommentId`),
  KEY `FK10utdy9oj8l8tcpfi5y8fa1wp` (`carParkId`),
  KEY `FKnks81t5rm14qt2y9si9pvhmwu` (`userId`),
  CONSTRAINT `FK10utdy9oj8l8tcpfi5y8fa1wp` FOREIGN KEY (`carParkId`) REFERENCES `carParks` (`carParkId`),
  CONSTRAINT `FKnks81t5rm14qt2y9si9pvhmwu` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carParkComments`
--

LOCK TABLES `carParkComments` WRITE;
/*!40000 ALTER TABLE `carParkComments` DISABLE KEYS */;
INSERT INTO `carParkComments` VALUES (1,'test comment 1','2020-11-16 23:30:12.481000',12,1),(2,'test comment 2\r\n','2020-11-16 23:36:05.592000',12,1),(4,'test comment 3','2020-11-17 00:03:56.094000',12,1),(5,'dsafnasdfiwsddfiwdhIFHweiugbniukweGBUIJwbehdgiubdjGBUwdbgfuikjdgjuhgfuiweF','2020-11-17 13:55:27.879000',12,1),(6,'Test comment','2020-11-17 14:04:59.951000',13,1),(7,'test comment','2020-11-17 14:05:41.749000',13,1),(8,'test 5','2020-11-17 14:08:16.399000',12,1),(9,'test','2020-11-17 15:14:28.505000',14,1),(10,'Test','2020-12-29 19:18:25.440000',12,1),(11,'aa','2021-01-01 16:37:11.111000',12,1),(12,'as','2021-01-01 16:37:44.673000',12,1),(13,'f','2021-04-19 15:52:18.774000',12,1),(14,'test comment 5','2021-04-29 14:47:40.357000',12,1);
/*!40000 ALTER TABLE `carParkComments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carParkOwners`
--

DROP TABLE IF EXISTS `carParkOwners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carParkOwners` (
  `carParkId` bigint NOT NULL,
  `userId` bigint NOT NULL,
  PRIMARY KEY (`carParkId`,`userId`),
  KEY `FKpbp0uerys20rwt9bjatrtkpoj` (`userId`),
  CONSTRAINT `FK9ax9r0fmyu13ufnjtfb0g73uj` FOREIGN KEY (`carParkId`) REFERENCES `carParks` (`carParkId`),
  CONSTRAINT `FKpbp0uerys20rwt9bjatrtkpoj` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carParkOwners`
--

LOCK TABLES `carParkOwners` WRITE;
/*!40000 ALTER TABLE `carParkOwners` DISABLE KEYS */;
INSERT INTO `carParkOwners` VALUES (12,1),(13,1),(14,1),(15,1),(19,1),(25,1),(26,1),(12,3),(12,21),(12,23);
/*!40000 ALTER TABLE `carParkOwners` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carParkPayments`
--

DROP TABLE IF EXISTS `carParkPayments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carParkPayments` (
  `carParkPaymentId` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(19,2) DEFAULT NULL,
  `bookings` bigint NOT NULL,
  `paid` bit(1) NOT NULL,
  `yearMonth` varchar(255) DEFAULT NULL,
  `carParkId` bigint NOT NULL,
  `paymentId` varchar(255) DEFAULT NULL,
  `failedReason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`carParkPaymentId`),
  KEY `FKi99hg40q3o9lko2anxxs6u4mq` (`carParkId`),
  CONSTRAINT `FKi99hg40q3o9lko2anxxs6u4mq` FOREIGN KEY (`carParkId`) REFERENCES `carParks` (`carParkId`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carParkPayments`
--

LOCK TABLES `carParkPayments` WRITE;
/*!40000 ALTER TABLE `carParkPayments` DISABLE KEYS */;
INSERT INTO `carParkPayments` VALUES (1,261.31,13,_binary '','202103',12,'tr_1Iib2SDkduQieys2IiFMv00j',NULL),(7,48.10,6,_binary '','202104',12,'tr_1ImgrsDkduQieys2svjeBIdA',NULL),(8,0.00,0,_binary '\0','202104',13,NULL,NULL),(9,0.00,0,_binary '\0','202104',14,NULL,NULL),(10,0.00,0,_binary '\0','202104',15,NULL,NULL),(11,0.00,0,_binary '\0','202104',19,NULL,NULL);
/*!40000 ALTER TABLE `carParkPayments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carParks`
--

DROP TABLE IF EXISTS `carParks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carParks` (
  `carParkId` bigint NOT NULL AUTO_INCREMENT,
  `carParkAddress1` varchar(255) DEFAULT NULL,
  `carParkAddress2` varchar(255) DEFAULT NULL,
  `carParkCity` varchar(255) DEFAULT NULL,
  `carParkPostcode` varchar(255) DEFAULT NULL,
  `carParkStatus` varchar(255) DEFAULT NULL,
  `dateModified` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parkingRate` varchar(255) DEFAULT NULL,
  `price` decimal(19,2) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `accessControl` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `heightRestriction` int DEFAULT NULL,
  `dynamicPricing` bit(1) NOT NULL,
  `enableFutureWeeks` int NOT NULL,
  `serialNumber` varchar(255) DEFAULT NULL,
  `targetRevenue` decimal(19,2) DEFAULT NULL,
  `stripeId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`carParkId`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carParks`
--

LOCK TABLES `carParks` WRITE;
/*!40000 ALTER TABLE `carParks` DISABLE KEYS */;
INSERT INTO `carParks` VALUES (12,'57 Axholme Avenue','Edgware','London','HA85BD','APPROVED','2020-11-16 23:29:44.610000','Test car park 1','HALFHOUR',8.00,'testcarpark1@exmaple.com','NOBARRIER','Test Description for test car park 1',7,_binary '',3,NULL,300.00,'acct_1IiaOkRnmJZZ9Qpk'),(13,'86 Camrose Avenue','Edgware','London','HA86ET','APPROVED','2020-11-17 15:35:52.334000','Test car park 2','HOUR',10.00,'testcarpark2@exmaple.com','NOBARRIER',NULL,NULL,_binary '\0',1,NULL,NULL,'acct_1In0hyDHAYpaC0b0'),(14,'7 East Road','Burnt Oak','London','HA80BY','APPROVED','2020-11-13 13:13:22.564000','Test car park 3','HOUR',5.00,'testcarpark3@exmaple.com','NOBARRIER',NULL,NULL,_binary '\0',1,NULL,NULL,NULL),(15,'Shaldon Road','Edgware','London','HA86AN','APPROVED','2020-11-17 00:06:42.428000','Test car park 4','HOUR',6.50,'testcarpark4@example.com','NOBARRIER',NULL,NULL,_binary '\0',1,NULL,NULL,NULL),(19,'34 Playfield Road','Edgware','London','HA80DF','APPROVED','2021-02-07 12:22:54.111000','Test car park 5','HOUR',4.55,'testcarpark5@example.com','NOBARRIER',NULL,NULL,_binary '\0',1,NULL,NULL,NULL),(25,'69 The Greenway','Edgware','London','NW9 5AR','SUBMITTED','2021-05-01 22:51:08.484000','Test Car Park 6','HOUR',5.00,'test6@test.com','TICKET','Test 25',NULL,_binary '\0',6,'tt',NULL,NULL),(26,'26 Melrose Gardens','Edgware','London','HA8 5LN','SUBMITTED','2021-03-08 18:56:43.459000','Stadium Wembley Parking','HOUR',4.50,'testcarpark7@test.com','NOBARRIER',NULL,5,_binary '\0',1,NULL,NULL,NULL);
/*!40000 ALTER TABLE `carParks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carParkSpots`
--

DROP TABLE IF EXISTS `carParkSpots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carParkSpots` (
  `carParkSpotId` bigint NOT NULL AUTO_INCREMENT,
  `isDisabled` bit(1) NOT NULL,
  `spaceNumber` int NOT NULL,
  `carParkId` bigint NOT NULL,
  PRIMARY KEY (`carParkSpotId`),
  KEY `FKqxjaddqw5ucpcg703qmlpm9j4` (`carParkId`),
  CONSTRAINT `FKqxjaddqw5ucpcg703qmlpm9j4` FOREIGN KEY (`carParkId`) REFERENCES `carParks` (`carParkId`)
) ENGINE=InnoDB AUTO_INCREMENT=203 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carParkSpots`
--

LOCK TABLES `carParkSpots` WRITE;
/*!40000 ALTER TABLE `carParkSpots` DISABLE KEYS */;
INSERT INTO `carParkSpots` VALUES (87,_binary '',1,12),(88,_binary '',2,12),(89,_binary '',3,12),(90,_binary '',4,12),(91,_binary '',5,12),(92,_binary '\0',1,13),(102,_binary '',1,14),(103,_binary '',2,14),(104,_binary '',3,14),(105,_binary '',4,14),(106,_binary '',5,14),(107,_binary '',1,15),(108,_binary '\0',2,15),(109,_binary '\0',3,15),(110,_binary '\0',4,15),(111,_binary '\0',5,15),(112,_binary '\0',6,15),(115,_binary '\0',1,19),(121,_binary '\0',2,25),(122,_binary '',1,25),(123,_binary '\0',2,26),(124,_binary '\0',1,26);
/*!40000 ALTER TABLE `carParkSpots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carParkTimes`
--

DROP TABLE IF EXISTS `carParkTimes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carParkTimes` (
  `carParkTimeId` bigint NOT NULL AUTO_INCREMENT,
  `closeTime` time DEFAULT NULL,
  `dayOfWeek` varchar(255) DEFAULT NULL,
  `openTime` time DEFAULT NULL,
  `carParkId` bigint NOT NULL,
  PRIMARY KEY (`carParkTimeId`),
  KEY `FKthn4s2bbiud8fwww2lwbmv3bq` (`carParkId`),
  CONSTRAINT `FKthn4s2bbiud8fwww2lwbmv3bq` FOREIGN KEY (`carParkId`) REFERENCES `carParks` (`carParkId`)
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carParkTimes`
--

LOCK TABLES `carParkTimes` WRITE;
/*!40000 ALTER TABLE `carParkTimes` DISABLE KEYS */;
INSERT INTO `carParkTimes` VALUES (16,NULL,'SATURDAY',NULL,12),(17,'13:00:00','MONDAY','03:00:00',12),(18,NULL,'SUNDAY',NULL,12),(19,'21:30:00','FRIDAY','13:00:00',12),(20,'23:30:00','TUESDAY','12:30:00',12),(21,'20:00:00','WEDNESDAY','04:30:00',12),(22,'23:30:00','THURSDAY','20:00:00',12),(23,'18:00:00','WEDNESDAY','09:00:00',13),(24,'18:00:00','MONDAY','09:00:00',13),(25,'18:00:00','FRIDAY','09:00:00',13),(26,'18:00:00','THURSDAY','09:00:00',13),(27,'18:00:00','TUESDAY','09:00:00',13),(28,NULL,'SATURDAY',NULL,13),(29,'18:00:00','SUNDAY','09:00:00',13),(30,'23:59:00','SATURDAY','00:00:00',14),(31,'23:59:00','FRIDAY','00:00:00',14),(32,'23:59:00','SUNDAY','00:00:00',14),(33,'23:59:00','TUESDAY','00:00:00',14),(34,'23:59:00','THURSDAY','00:00:00',14),(35,'23:59:00','WEDNESDAY','00:00:00',14),(36,'23:59:00','MONDAY','00:00:00',14),(37,'18:00:00','SUNDAY','07:00:00',15),(38,'18:00:00','MONDAY','07:00:00',15),(39,'18:00:00','FRIDAY','07:00:00',15),(40,'18:00:00','SATURDAY','07:00:00',15),(41,'18:00:00','TUESDAY','07:00:00',15),(42,'18:00:00','THURSDAY','07:00:00',15),(43,'18:00:00','WEDNESDAY','07:00:00',15),(58,'18:00:00','SATURDAY','07:00:00',19),(59,'18:00:00','TUESDAY','07:00:00',19),(60,'18:00:00','SUNDAY','07:00:00',19),(61,'18:00:00','WEDNESDAY','07:00:00',19),(62,'18:00:00','FRIDAY','07:00:00',19),(63,'18:00:00','MONDAY','07:00:00',19),(64,'18:00:00','THURSDAY','07:00:00',19),(100,'21:30:00','MONDAY','07:00:00',25),(101,'18:00:00','WEDNESDAY','07:00:00',25),(102,'18:00:00','THURSDAY','07:00:00',25),(103,'18:00:00','FRIDAY','07:00:00',25),(104,'18:00:00','SUNDAY','07:00:00',25),(105,'18:00:00','SATURDAY','07:00:00',25),(106,'18:00:00','TUESDAY','07:00:00',25),(107,NULL,'SUNDAY',NULL,26),(108,NULL,'SATURDAY',NULL,26),(109,NULL,'TUESDAY',NULL,26),(110,NULL,'MONDAY',NULL,26),(111,NULL,'THURSDAY',NULL,26),(112,NULL,'WEDNESDAY',NULL,26),(113,NULL,'FRIDAY',NULL,26);
/*!40000 ALTER TABLE `carParkTimes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `closureDates`
--

DROP TABLE IF EXISTS `closureDates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `closureDates` (
  `ClosureDateId` bigint NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `carParkId` bigint NOT NULL,
  PRIMARY KEY (`ClosureDateId`),
  KEY `FKpmrlmu93d5gnolq1fq7clp2nl` (`carParkId`),
  CONSTRAINT `FK4eq5lrhh7vvj86vdjrc0r1qi1` FOREIGN KEY (`carParkId`) REFERENCES `carParks` (`carParkId`),
  CONSTRAINT `FKpmrlmu93d5gnolq1fq7clp2nl` FOREIGN KEY (`carParkId`) REFERENCES `carParks` (`carParkId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `closureDates`
--

LOCK TABLES `closureDates` WRITE;
/*!40000 ALTER TABLE `closureDates` DISABLE KEYS */;
INSERT INTO `closureDates` VALUES (1,'2021-04-15',12),(3,'2021-04-14',12);
/*!40000 ALTER TABLE `closureDates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `roleId` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ADMIN'),(2,'SITEADMIN'),(3,'USER'),(4,'CARPARKOWNER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `userId` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `stripeId` varchar(255) DEFAULT NULL,
  `resetPasswordToken` varchar(255) DEFAULT NULL,
  `roleId` bigint DEFAULT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`),
  KEY `FKntreoimw86ojl0fnss8bh0h9j` (`roleId`),
  CONSTRAINT `FKntreoimw86ojl0fnss8bh0h9j` FOREIGN KEY (`roleId`) REFERENCES `roles` (`roleId`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'marcusfung@hotmail.co.uk','admin','admin','$2a$10$ARDL0xLhedwKSpFJs7s/guWN0L7o9D5NKbsvl15hSpZAYJ4PJDbs.','admin','cus_IdJnINdGcvZP6H',NULL,1),(3,'marcus@test','test','test','$2a$10$Mc/3VaXt5PsuRKAdK0fxr.E4Ufg5CJvC7B4EVL.D9hv5Tk5UT8d1u','marcus',NULL,NULL,4),(21,'siteadmin1@test.com','site','admin','$2a$10$5OGXbqclZSEkAmcKcF31Nuhm5kRHGmj0ETvDt9ndfc1Qv84aS2BHi','siteadmin1',NULL,NULL,2),(23,'mtech-hd@hotmail.com','site','admin2','$2a$10$n79.6Dtpl8/chO1e38edeeGX1EzMZmYL7DLngphT0MRWbufCUxQgO','siteadmin2',NULL,NULL,2),(25,'ktfung@hotmail.co.uk','testuser1','user','$2a$10$BUkFP2UgT9prAN8CBcJB5O3/anf/6hSbN4PWsDxKH00E2RKr7Xmc2','testuser1','cus_JISDDJW7g0Un4h',NULL,3),(26,'testuser7@test.com','testuser7','testuser7','$2a$10$jbAW8IXFBJWgmOKii58lQOsySZFUwQwLLd2oZi5yRHVp0CxawJTae','testuser7','cus_JP7iJd9ANkAghL',NULL,3);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicles`
--

DROP TABLE IF EXISTS `vehicles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicles` (
  `vehicleId` bigint NOT NULL AUTO_INCREMENT,
  `colour` varchar(255) DEFAULT NULL,
  `make` varchar(255) DEFAULT NULL,
  `registration` varchar(255) DEFAULT NULL,
  `userId` bigint DEFAULT NULL,
  `isDefault` bit(1) NOT NULL,
  PRIMARY KEY (`vehicleId`),
  KEY `FK33nm4h2ebs5o22tw626fjkbwl` (`userId`),
  CONSTRAINT `FK33nm4h2ebs5o22tw626fjkbwl` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicles`
--

LOCK TABLES `vehicles` WRITE;
/*!40000 ALTER TABLE `vehicles` DISABLE KEYS */;
INSERT INTO `vehicles` VALUES (1,'White','VW','WP68GXY',1,_binary ''),(2,'Silver','ford','EJ12XYZ',NULL,_binary '\0'),(106,'fff','fff','tttttt',NULL,_binary ''),(108,'ddd','ddd','adsasd',NULL,_binary '\0'),(118,'TEST','TEST','TT1RR1',NULL,_binary '\0'),(128,'BLUE','FORD','EJ14XYZ',NULL,_binary ''),(129,'ff','ff','TT4TT5',NULL,_binary '\0'),(130,'gg','gg','EJ12XYZ',26,_binary '');
/*!40000 ALTER TABLE `vehicles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'ePark'
--
/*!50003 DROP PROCEDURE IF EXISTS `checkAvailable` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `checkAvailable`(IN Id long, In bookingDate date, In startT time, in endT time, in disabled bit, in length int)
BEGIN
select cp.* from carParkSpots cp where cp.carParkId = Id and cp.isDisabled = disabled
and not exists (
select * from bookings b 
inner join carParks c on c.carParkId = b.carParkId
inner join carParkSpots cps on cps.carParkSpotId = b.carParkSpotId
where b.carParkId = Id
and b.startDate = bookingDate
and b.endDate = bookingDate
and 
(
(startT >= b.startTime and endT <= b.endTime) 
or (startT < b.startTime and endT <= b.endTime and endT > b.startTime)
or (startT >= b.startTime and startT < b.endTime and endT > b.endTime)
or (startT < b.startTime and endT > b.endTime)
)
and cps.isDisabled = disabled
and cps.carParkSpotId = cp.carParkSpotId
and b.bookingStatus IN ('ACTIVE', 'INPROGRESS')
)
ORDER BY cp.carParkSpotId asc
LIMIT length;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getCarParkTimes` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getCarParkTimes`(IN Id long)
BEGIN
select * from carParkTimes where carParkId = Id order by dayOfWeek ASC;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `removeInvalidBookings` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `removeInvalidBookings`()
BEGIN
DELETE b.* FROM bookings b 
WHERE b.bookingStatus = 'INPROGRESS' 
AND (b.bookingCreated + INTERVAL 31 MINUTE < NOW());
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-09 15:42:05
