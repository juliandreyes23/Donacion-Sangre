-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: donacionsangre_db
-- ------------------------------------------------------
-- Server version	8.0.45

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
-- Table structure for table `blood_inventory`
--

DROP TABLE IF EXISTS `blood_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blood_inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cantidad_disponible_ml` int NOT NULL,
  `tipo_sangre` enum('AB_NEGATIVO','AB_POSITIVO','A_NEGATIVO','A_POSITIVO','B_NEGATIVO','B_POSITIVO','O_NEGATIVO','O_POSITIVO') NOT NULL,
  `ultima_actualizacion` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKnugvtt8st10bmgidelnla1g60` (`tipo_sangre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blood_inventory`
--

LOCK TABLES `blood_inventory` WRITE;
/*!40000 ALTER TABLE `blood_inventory` DISABLE KEYS */;
/*!40000 ALTER TABLE `blood_inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consents`
--

DROP TABLE IF EXISTS `consents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consents` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `acepta_consentimiento` bit(1) NOT NULL,
  `fecha_firma` date NOT NULL,
  `firma_consentimiento` text NOT NULL,
  `donante_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKflsipy7eidddnjcx4fa4sete5` (`donante_id`),
  CONSTRAINT `FK40qgdip07m2k4lecgqdp6goo8` FOREIGN KEY (`donante_id`) REFERENCES `donors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consents`
--

LOCK TABLES `consents` WRITE;
/*!40000 ALTER TABLE `consents` DISABLE KEYS */;
INSERT INTO `consents` VALUES (1,'2026-05-24 23:23:46.332826','admin','2026-05-24 23:23:46.332826','admin',_binary '','2026-05-24','uploads\\firmas\\6ae0074b-574d-4f6a-9e5f-20ad111949a7.png',1);
/*!40000 ALTER TABLE `consents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `donations`
--

DROP TABLE IF EXISTS `donations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `donations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cantidad_ml` int NOT NULL,
  `codigo_donacion` varchar(30) NOT NULL,
  `estado` enum('DESCARTADA','PROCESADA','REGISTRADA') NOT NULL,
  `fecha_donacion` datetime(6) NOT NULL,
  `observaciones` text,
  `tipo_sangre` enum('AB_NEGATIVO','AB_POSITIVO','A_NEGATIVO','A_POSITIVO','B_NEGATIVO','B_POSITIVO','O_NEGATIVO','O_POSITIVO') NOT NULL,
  `donante_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhys34vxlhg1ff9mqt3te7bare` (`codigo_donacion`),
  KEY `FKjhlhvtu11b7goe5wf3lqr19i0` (`donante_id`),
  CONSTRAINT `FKjhlhvtu11b7goe5wf3lqr19i0` FOREIGN KEY (`donante_id`) REFERENCES `donors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `donations`
--

LOCK TABLES `donations` WRITE;
/*!40000 ALTER TABLE `donations` DISABLE KEYS */;
/*!40000 ALTER TABLE `donations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `donors`
--

DROP TABLE IF EXISTS `donors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `donors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `acepta_consentimiento` bit(1) NOT NULL,
  `activo` bit(1) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `correo` varchar(150) NOT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `documento` varchar(20) NOT NULL,
  `fecha_nacimiento` date NOT NULL,
  `fecha_ultima_donacion` date DEFAULT NULL,
  `nombres` varchar(100) NOT NULL,
  `peso` double NOT NULL,
  `telefono` varchar(15) NOT NULL,
  `tipo_sangre` enum('AB_NEGATIVO','AB_POSITIVO','A_NEGATIVO','A_POSITIVO','B_NEGATIVO','B_POSITIVO','O_NEGATIVO','O_POSITIVO') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlcxn7pcm3mk39doc60pkg4wm4` (`correo`),
  UNIQUE KEY `UKlcaj1skkgfj38wk2djek5ih4x` (`documento`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `donors`
--

LOCK TABLES `donors` WRITE;
/*!40000 ALTER TABLE `donors` DISABLE KEYS */;
INSERT INTO `donors` VALUES (1,_binary '',_binary '','Estupiñan','marioestupinan@gmail.com','calle 10 # 45-78','CC','2000-12-12','2018-05-12','Mario',58,'3000000001','O_POSITIVO');
/*!40000 ALTER TABLE `donors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `rol` enum('ADMIN','DONANTE','ENFERMERA','MEDICO') DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKm2dvbwfge291euvmk6vkkocao` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'$2a$10$xlLmJivf37jORd4r/dtL2ONP.qrTOw8mB2tKfhUQw3r4X.BKd5liW','ADMIN','admin');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-26 10:40:57
