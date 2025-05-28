-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: internet_db
-- ------------------------------------------------------
-- Server version	8.4.3

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
-- Table structure for table `packages`
--

DROP TABLE IF EXISTS `packages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `packages` (
  `id` char(10) NOT NULL COMMENT 'Mã gói (do người dùng hoặc chương trình tự tạo)',
  `name` varchar(100) NOT NULL COMMENT 'Tên gói cước',
  `type` varchar(20) NOT NULL COMMENT 'Loại gói: ngày, tháng, năm hoặc các loại khác',
  `duration_days` int NOT NULL COMMENT 'Thời gian sử dụng (số ngày)',
  `data_gb` double NOT NULL DEFAULT '0' COMMENT 'Dung lượng data (GB)',
  `price` double NOT NULL COMMENT 'Giá gói cước',
  `description` text COMMENT 'Mô tả chi tiết về gói',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `packages`
--

LOCK TABLES `packages` WRITE;
/*!40000 ALTER TABLE `packages` DISABLE KEYS */;
INSERT INTO `packages` VALUES ('n1','1 ngày','ngày',1,2,10,'1 ngày, 2GB data'),('na1',' 1 năm','năm',365,240,1500,'1 năm, 240GB data'),('P001','Gói Cơ Bản','tháng',30,0,150000,'Tốc độ 30Mbps, không giới hạn dung lượng'),('P002','Gói Nâng Cao','tháng',30,0,250000,'Tốc độ 50Mbps, kèm dịch vụ truyền hình'),('P003','Gói VIP','tháng',30,0,400000,'Tốc độ 100Mbps, hỗ trợ 24/7, không giới hạn thiết bị'),('t1',' 1 tháng','tháng',30,20,150,'30 ngày, 20GB data');
/*!40000 ALTER TABLE `packages` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-28 21:20:03
