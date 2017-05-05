CREATE DATABASE  IF NOT EXISTS `clubdeportivo` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `clubdeportivo`;
-- MySQL dump 10.13  Distrib 5.1.40, for Win32 (ia32)
--
-- Host: localhost    Database: clubdeportivo
-- ------------------------------------------------------
-- Server version	5.1.50-community

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
-- Table structure for table `jornadasquiniela`
--

DROP TABLE IF EXISTS `jornadasquiniela`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jornadasquiniela` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `numero` int(11) NOT NULL,
  `descripcion` varchar(200) NOT NULL,
  `competicion` bigint(20) NOT NULL,
  `fecha` varchar(200) DEFAULT NULL,
  `partido1` varchar(200) NOT NULL,
  `partido2` varchar(200) NOT NULL,
  `partido3` varchar(200) NOT NULL,
  `partido4` varchar(200) NOT NULL,
  `partido5` varchar(200) NOT NULL,
  `partido6` varchar(200) NOT NULL,
  `partido7` varchar(200) NOT NULL,
  `partido8` varchar(200) NOT NULL,
  `partido9` varchar(200) NOT NULL,
  `partido10` varchar(200) NOT NULL,
  `partido11` varchar(200) NOT NULL,
  `partido12` varchar(200) NOT NULL,
  `partido13` varchar(200) NOT NULL,
  `partido14` varchar(200) NOT NULL,
  `partido15` varchar(200) NOT NULL,
  `resultado1` varchar(1) DEFAULT NULL,
  `resultado2` varchar(1) DEFAULT NULL,
  `resultado3` varchar(1) DEFAULT NULL,
  `resultado4` varchar(1) DEFAULT NULL,
  `resultado5` varchar(1) DEFAULT NULL,
  `resultado6` varchar(1) DEFAULT NULL,
  `resultado7` varchar(1) DEFAULT NULL,
  `resultado8` varchar(1) DEFAULT NULL,
  `resultado9` varchar(1) DEFAULT NULL,
  `resultado10` varchar(1) DEFAULT NULL,
  `resultado11` varchar(1) DEFAULT NULL,
  `resultado12` varchar(1) DEFAULT NULL,
  `resultado13` varchar(1) DEFAULT NULL,
  `resultado14` varchar(1) DEFAULT NULL,
  `resultado15` varchar(1) DEFAULT NULL,
  `validada` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jornadasquiniela`
--

LOCK TABLES `jornadasquiniela` WRITE;
/*!40000 ALTER TABLE `jornadasquiniela` DISABLE KEYS */;
/*!40000 ALTER TABLE `jornadasquiniela` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `puntuacionesquiniela`
--

DROP TABLE IF EXISTS `puntuacionesquiniela`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `puntuacionesquiniela` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `competicion` bigint(20) NOT NULL,
  `equipo` bigint(20) NOT NULL,
  `puntos` int(11) NOT NULL,
  `victorias` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `puntuacionesquiniela`
--

LOCK TABLES `puntuacionesquiniela` WRITE;
/*!40000 ALTER TABLE `puntuacionesquiniela` DISABLE KEYS */;
/*!40000 ALTER TABLE `puntuacionesquiniela` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `apuestasquiniela`
--

DROP TABLE IF EXISTS `apuestasquiniela`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `apuestasquiniela` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `jornada` bigint(20) NOT NULL,
  `equipo` bigint(20) NOT NULL,
  `resultado1` varchar(1) DEFAULT NULL,
  `resultado2` varchar(1) DEFAULT NULL,
  `resultado3` varchar(1) DEFAULT NULL,
  `resultado4` varchar(1) DEFAULT NULL,
  `resultado5` varchar(1) DEFAULT NULL,
  `resultado6` varchar(1) DEFAULT NULL,
  `resultado7` varchar(1) DEFAULT NULL,
  `resultado8` varchar(1) DEFAULT NULL,
  `resultado9` varchar(1) DEFAULT NULL,
  `resultado10` varchar(1) DEFAULT NULL,
  `resultado11` varchar(1) DEFAULT NULL,
  `resultado12` varchar(1) DEFAULT NULL,
  `resultado13` varchar(1) DEFAULT NULL,
  `resultado14` varchar(1) DEFAULT NULL,
  `resultado15` varchar(1) DEFAULT NULL,
  `actualizada` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apuestasquiniela`
--

LOCK TABLES `apuestasquiniela` WRITE;
/*!40000 ALTER TABLE `apuestasquiniela` DISABLE KEYS */;
/*!40000 ALTER TABLE `apuestasquiniela` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equiposfutbol8`
--

DROP TABLE IF EXISTS `equiposfutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `equiposfutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activo` bit(1) NOT NULL,
  `automatico` bit(1) NOT NULL,
  `moral` int(11) NOT NULL,
  `campo` int(11) NOT NULL,
  `publicidad` int(11) NOT NULL,
  `eqtecnico` int(11) NOT NULL,
  `ojeadores` int(11) NOT NULL,
  `jugadoresojeados` int(11) NOT NULL,
  `primas` int(11) NOT NULL,
  `presupuesto` int(11) NOT NULL,
  `credito` int(11) NOT NULL,
  `bolsa` int(11) NOT NULL,
  `entrenamiento` bit(1) NOT NULL,
  `simulaciones` int(11) NOT NULL,
  `modoauto` bit(1) NOT NULL,
  `esfuerzo` int(11) NOT NULL,
  `estrategia` int(11) NOT NULL,
  `club` int(11) NOT NULL,
  `ampliarcampo` bit(1) NOT NULL,
  `ampliarpublicidad` bit(1) NOT NULL,
  `ampliareqtecnico` bit(1) NOT NULL,
  `ampliarojeadores` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `club_UNIQUE` (`club`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equiposfutbol8`
--

LOCK TABLES `equiposfutbol8` WRITE;
/*!40000 ALTER TABLE `equiposfutbol8` DISABLE KEYS */;
INSERT INTO `equiposfutbol8` VALUES (8,'','\0',80,500,0,0,0,0,0,20000,0,0,'\0',0,'\0',0,0,5,'\0','\0','\0','\0'),(9,'','\0',80,500,0,0,0,0,0,20000,0,0,'\0',0,'\0',0,0,6,'\0','\0','\0','\0');
/*!40000 ALTER TABLE `equiposfutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jugadoresfutbol8`
--

DROP TABLE IF EXISTS `jugadoresfutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jugadoresfutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `equipo` bigint(20) NOT NULL,
  `grupo` bigint(20) NOT NULL,
  `ficha` int(11) NOT NULL,
  `clausula` int(11) NOT NULL,
  `blindado` bit(1) NOT NULL,
  `jornadasLesion` int(11) NOT NULL,
  `transferible` bit(1) NOT NULL,
  `contrato` int(11) NOT NULL,
  `posicion` varchar(50) NOT NULL,
  `valoracion` int(11) NOT NULL,
  `estadoFisico` int(11) NOT NULL,
  `tarjetaAmarilla` bit(1) NOT NULL,
  `tarjetaRoja` bit(1) NOT NULL,
  `juegaJornada` bit(1) NOT NULL,
  `goles` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jugadoresfutbol8`
--

LOCK TABLES `jugadoresfutbol8` WRITE;
/*!40000 ALTER TABLE `jugadoresfutbol8` DISABLE KEYS */;
/*!40000 ALTER TABLE `jugadoresfutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jugadoresmaestrofutbol8`
--

DROP TABLE IF EXISTS `jugadoresmaestrofutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jugadoresmaestrofutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `posicion` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jugadoresmaestrofutbol8`
--

LOCK TABLES `jugadoresmaestrofutbol8` WRITE;
/*!40000 ALTER TABLE `jugadoresmaestrofutbol8` DISABLE KEYS */;
INSERT INTO `jugadoresmaestrofutbol8` VALUES (3,'Adriano',2),(4,'Aimar',2),(5,'Albelda',2),(6,'Amancio',2),(7,'Amavisca',2),(8,'Amor',2),(9,'Asensi',2),(10,'Assuncao',2),(11,'Bakero',2),(12,'Baraja',2),(13,'Beckan',2),(14,'Begiristain',2),(15,'Berckam',2),(16,'Caldere',2),(17,'Cambiasso',2),(18,'Cani',2),(19,'Clos',2),(20,'Cocu',2),(21,'Cruyff Joan',2),(22,'Dani Alves',2),(23,'De la Pena',2),(24,'De Pedro',2),(25,'DeBoer',2),(26,'Deco',2),(27,'Emerson',2),(28,'Eusebio',2),(29,'Figo',2),(30,'Flavio',2),(31,'Gento',2),(32,'Gerard',2),(33,'Gio',2),(34,'Guardiola',2),(35,'Guerrero',2),(36,'Guiti',2),(37,'Gulit',2),(38,'Gurpegui',2),(39,'hagi',2),(40,'Helgera',2),(41,'Iniesta',2),(42,'Juan Senor',2),(43,'Juanfran',2),(44,'Juanito',2),(45,'Kaka',2),(46,'Karpin',2),(47,'Kiki Mussampa',2),(48,'La Torre',2),(49,'Lampard',2),(50,'Laudrup',2),(51,'Luis Enrique',2),(52,'Luque',2),(53,'Makelele',2),(54,'Marcial',2),(55,'Martin Vazquez',2),(56,'McManaman',2),(57,'Mendieta',2),(58,'Michel',2),(59,'Milla',2),(60,'Mota',2),(61,'Motta',2),(62,'Neeskens',2),(63,'Onesimo',2),(64,'Overmars',2),(65,'Pinilla',2),(66,'Pirri',2),(67,'Pochettino',2),(68,'Prosineky',2),(69,'Redondo',2),(70,'Rexach',2),(71,'Riquelme',2),(72,'Rochemback',2),(73,'Rufete',2),(74,'Schuster',2),(75,'Seedorf',2),(76,'Senor',2),(77,'Silvinho',2),(78,'Simeone',2),(79,'Solari',2),(80,'Soler',2),(81,'Toro Acuna',2),(82,'Urbano',2),(83,'Valeron',2),(84,'Vicente',2),(85,'Victor',2),(86,'Vieira',2),(87,'Xavi',2),(88,'Zenden',2),(89,'Zidane',2),(90,'Abelardo',1),(91,'Aitor Ocio',1),(92,'Alexanco',1),(93,'Alkorta',1),(94,'Amarillo',1),(95,'Andersson',1),(96,'Aranzabal',1),(97,'Ayala',1),(98,'Beckenbauer',1),(99,'Beletti',1),(100,'Camacho',1),(101,'Cannavaro',1),(102,'Capdevila',1),(103,'Carboni',1),(104,'Celades',1),(105,'Chendo',1),(106,'Christanval',1),(107,'Coco',1),(108,'Colochini',1),(109,'Costas',1),(110,'Curro Torres',1),(111,'De Boer',1),(112,'De la Cruz',1),(113,'Del horno',1),(114,'Del Sol',1),(115,'Edmilson',1),(116,'Ferrer',1),(117,'Gallego',1),(118,'Goikoechea',1),(119,'Gurrutxaga',1),(120,'Helguera',1),(121,'Hierro',1),(122,'Jarque',1),(123,'Javi Navarro',1),(124,'Jorge Andrade',1),(125,'Julen Guerrero',1),(126,'Julio Alberto',1),(127,'koeman',1),(128,'Lizarazu',1),(129,'Lopo',1),(130,'Maceda',1),(131,'Manuel Pablo',1),(132,'Marchena',1),(133,'Marquez',1),(134,'Martagon',1),(135,'Matterazzi',1),(136,'Michel Salgado',1),(137,'Migueli',1),(138,'Milito',1),(139,'Minambres',1),(140,'Moratalla',1),(141,'Navarro',1),(142,'Naybet',1),(143,'Olaizola',1),(144,'Oleguer',1),(145,'Olmo',1),(146,'Pablo',1),(147,'Pablo Alfaro',1),(148,'Pavon',1),(149,'Pellegrino',1),(150,'Pepe',1),(151,'Pernia',1),(152,'Pikabea',1),(153,'Poli',1),(154,'Pujol',1),(155,'Reiziger',1),(156,'Roberto Carlos',1),(157,'Sagi Barba',1),(158,'Sagnol',1),(159,'Sanchis',1),(160,'Sergi',1),(161,'Sergio Ramos',1),(162,'Serna',1),(163,'Sorin',1),(164,'Stilike',1),(165,'Tarzan Migueli',1),(166,'Terry',1),(167,'Thuram',1),(168,'Yago Yao',1),(169,'Zambrotta',1),(170,'Alfonso',3),(171,'Angelito Cuellar',3),(172,'Archivald',3),(173,'Canniga',3),(174,'Cantona',3),(175,'Carrasco',3),(176,'Cuellar',3),(177,'Dani',3),(178,'Derticia',3),(179,'DiStefano',3),(180,'Drogba',3),(181,'Dugarry',3),(182,'El burrito Ortega',3),(183,'Emilio Butragueno',3),(184,'Esteban',3),(185,'Etoo',3),(186,'Ezquerro',3),(187,'Fred',3),(188,'Hugo Sanchez',3),(189,'Joaquin',3),(190,'Julio Salinas',3),(191,'Kanute',3),(192,'Kluivert',3),(193,'Kodro',3),(194,'Kovacevic',3),(195,'Krankl',3),(196,'Kubala',3),(197,'Lineker',3),(198,'Luca Toni',3),(199,'Maradona',3),(200,'Marcos',3),(201,'Messi',3),(202,'Mijatovic',3),(203,'Munitis',3),(204,'Pandiani',3),(205,'Pedro Munitis',3),(206,'Pele',3),(207,'Portillo',3),(208,'Puskas',3),(209,'Quini',3),(210,'Raul',3),(211,'Ribaldo',3),(212,'Rife',3),(213,'Rivaldo',3),(214,'Robinho',3),(215,'Rojo',3),(216,'Romario',3),(217,'Ronaldinho',3),(218,'Ronaldo',3),(219,'Salinas',3),(220,'Samitier',3),(221,'Santillana',3),(222,'Satrustegui',3),(223,'Saviola',3),(224,'Segarra',3),(225,'Simonsen',3),(226,'Stoichkov',3),(227,'Stoikov',3),(228,'Suker',3),(229,'Tamudo',3),(230,'Torpedo Muller',3),(231,'Torres',3),(232,'Tote',3),(233,'Tristan',3),(234,'Turu Flores',3),(235,'Villa',3),(236,'Zamorano',3),(237,'Zoco',3),(238,'Abel Resino',0),(239,'Ablanedo',0),(240,'Alberto',0),(241,'Aranzubia',0),(242,'Arkonada',0),(243,'Artola',0),(244,'Bartez',0),(245,'Bonano',0),(246,'Buffon',0),(247,'Bufon',0),(248,'Buyo',0),(249,'Canizares',0),(250,'Carlos Roa',0),(251,'Casillas',0),(252,'Cesar',0),(253,'Chilavert',0),(254,'Enke',0),(255,'Garcia Remon',0),(256,'Gianluca Pavliuca',0),(257,'Iribar',0),(258,'Jacques Songoo',0),(259,'Jimenez',0),(260,'Jorquera',0),(261,'Juanmi',0),(262,'Kameni',0),(263,'Kokoloko',0),(264,'Lafuente',0),(265,'Leo Franco',0),(266,'Lopetegui',0),(267,'Molina',0),(268,'Mono Burgos',0),(269,'Nkono',0),(270,'Ochotorena',0),(271,'Oliver Kahn',0),(272,'Oliver Kan',0),(273,'Pablo Cavallero',0),(274,'Palop',0),(275,'Peter Schmeichel',0),(276,'Petr Cech',0),(277,'Pinto',0),(278,'Prats',0),(279,'Ramallets',0),(280,'Reina',0),(281,'Salvador Sadurni',0),(282,'Sanchez',0),(283,'Taffarel',0),(284,'Urruti',0),(285,'van der Sar',0),(286,'Victor Bahia',0),(287,'Victor Valdes',0),(288,'Walter Zenga',0),(289,'Westerveld',0),(290,'Zamora',0),(291,'Zubizarreta',0);
/*!40000 ALTER TABLE `jugadoresmaestrofutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `competicionesquiniela`
--

DROP TABLE IF EXISTS `competicionesquiniela`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `competicionesquiniela` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `grupo` bigint(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `activa` bit(1) NOT NULL,
  `proximajornada` int(11) NOT NULL,
  `ultimajornada` int(11) NOT NULL,
  `fecha` date DEFAULT NULL,
  `campeon` bigint(20) DEFAULT NULL,
  `subcampeon` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `competicionesquiniela`
--

LOCK TABLES `competicionesquiniela` WRITE;
/*!40000 ALTER TABLE `competicionesquiniela` DISABLE KEYS */;
/*!40000 ALTER TABLE `competicionesquiniela` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clubs`
--

DROP TABLE IF EXISTS `clubs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clubs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activo` bit(1) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `usuario` varchar(20) NOT NULL,
  `ranking` int(11) NOT NULL,
  `password` varchar(40) NOT NULL,
  `mail` varchar(255) NOT NULL,
  `fundacion` datetime NOT NULL,
  `ultimoacceso` datetime NOT NULL,
  `grupo` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`),
  UNIQUE KEY `usuario_UNIQUE` (`usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clubs`
--

LOCK TABLES `clubs` WRITE;
/*!40000 ALTER TABLE `clubs` DISABLE KEYS */;
/*!40000 ALTER TABLE `clubs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equiposquiniela`
--

DROP TABLE IF EXISTS `equiposquiniela`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `equiposquiniela` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `club` bigint(20) NOT NULL,
  `activo` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equiposquiniela`
--

LOCK TABLES `equiposquiniela` WRITE;
/*!40000 ALTER TABLE `equiposquiniela` DISABLE KEYS */;
/*!40000 ALTER TABLE `equiposquiniela` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equiposbasket`
--

DROP TABLE IF EXISTS `equiposbasket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `equiposbasket` (
  `idequiposbasket` int(11) NOT NULL,
  `club` bigint(20) NOT NULL,
  PRIMARY KEY (`idequiposbasket`,`club`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equiposbasket`
--

LOCK TABLES `equiposbasket` WRITE;
/*!40000 ALTER TABLE `equiposbasket` DISABLE KEYS */;
/*!40000 ALTER TABLE `equiposbasket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estadisticasquiniela`
--

DROP TABLE IF EXISTS `estadisticasquiniela`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estadisticasquiniela` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `equipo` bigint(20) NOT NULL,
  `competicion` bigint(20) NOT NULL,
  `jornada` bigint(20) NOT NULL,
  `puntos` int(11) NOT NULL,
  `aciertos` varchar(30) NOT NULL,
  PRIMARY KEY (`equipo`,`competicion`,`jornada`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estadisticasquiniela`
--

LOCK TABLES `estadisticasquiniela` WRITE;
/*!40000 ALTER TABLE `estadisticasquiniela` DISABLE KEYS */;
/*!40000 ALTER TABLE `estadisticasquiniela` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grupos`
--

DROP TABLE IF EXISTS `grupos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grupos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activo` bit(1) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `maxEquipos` int(11) NOT NULL,
  `completo` bit(1) NOT NULL,
  `privado` bit(1) NOT NULL,
  `generico` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grupos`
--

LOCK TABLES `grupos` WRITE;
/*!40000 ALTER TABLE `grupos` DISABLE KEYS */;
/*!40000 ALTER TABLE `grupos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-09-04 23:37:40
