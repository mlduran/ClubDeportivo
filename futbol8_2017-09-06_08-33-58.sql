-- MySQL dump 10.14  Distrib 5.5.52-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: futbol8
-- ------------------------------------------------------
-- Server version	5.5.52-MariaDB

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
-- Current Database: `futbol8`
--


--
-- Table structure for table `alineacionesfutbol8`
--

DROP TABLE IF EXISTS `alineacionesfutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alineacionesfutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `equipo` bigint(20) NOT NULL,
  `partido` bigint(20) NOT NULL,
  `tactica` int(11) DEFAULT NULL,
  `portero` bigint(20) DEFAULT NULL,
  `jugador1` bigint(20) DEFAULT NULL,
  `jugador2` bigint(20) DEFAULT NULL,
  `jugador3` bigint(20) DEFAULT NULL,
  `jugador4` bigint(20) DEFAULT NULL,
  `jugador5` bigint(20) DEFAULT NULL,
  `jugador6` bigint(20) DEFAULT NULL,
  `jugador7` bigint(20) DEFAULT NULL,
  `cuadrantep` varchar(4) DEFAULT NULL,
  `cuadrante1` varchar(4) DEFAULT NULL,
  `cuadrante2` varchar(4) DEFAULT NULL,
  `cuadrante3` varchar(4) DEFAULT NULL,
  `cuadrante4` varchar(4) DEFAULT NULL,
  `cuadrante5` varchar(4) DEFAULT NULL,
  `cuadrante6` varchar(4) DEFAULT NULL,
  `cuadrante7` varchar(4) DEFAULT NULL,
  `primas` int(11) NOT NULL,
  `esfuerzo` int(11) NOT NULL,
  `estrategia` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alineacionesfutbol8`
--

LOCK TABLES `alineacionesfutbol8` WRITE;
/*!40000 ALTER TABLE `alineacionesfutbol8` DISABLE KEYS */;
/*!40000 ALTER TABLE `alineacionesfutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `competicionesfutbol8`
--

DROP TABLE IF EXISTS `competicionesfutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `competicionesfutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `grupo` bigint(20) NOT NULL,
  `clase` varchar(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `activa` tinyint(1) NOT NULL,
  `proximajornada` int(11) NOT NULL,
  `ultimajornada` int(11) NOT NULL,
  `fecha` date DEFAULT NULL,
  `campeon` varchar(50) DEFAULT NULL,
  `subcampeon` varchar(50) DEFAULT NULL,
  `maximosgoleadores` varchar(999) DEFAULT NULL,
  `recaudacion` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `competicionesfutbol8`
--

LOCK TABLES `competicionesfutbol8` WRITE;
/*!40000 ALTER TABLE `competicionesfutbol8` DISABLE KEYS */;
/*!40000 ALTER TABLE `competicionesfutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configeconomiasfutbol8`
--

DROP TABLE IF EXISTS `configeconomiasfutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configeconomiasfutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `grupo` bigint(20) NOT NULL,
  `equipogestor` bigint(20) DEFAULT NULL,
  `diasgestion` int(11) DEFAULT NULL,
  `modificado` tinyint(1) DEFAULT NULL,
  `interescredito` int(11) DEFAULT NULL,
  `retencionhaciendabase` int(11) DEFAULT NULL,
  `retencionlineal` tinyint(1) DEFAULT NULL,
  `iva` int(11) DEFAULT NULL,
  `subidamaxbolsa` int(11) DEFAULT NULL,
  `crackbolsa` tinyint(1) DEFAULT NULL,
  `porcentajepremioliga` int(11) DEFAULT NULL,
  `porcentajecampeoncopa` int(11) DEFAULT NULL,
  `ibi` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `grupo_UNIQUE` (`grupo`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configeconomiasfutbol8`
--

LOCK TABLES `configeconomiasfutbol8` WRITE;
/*!40000 ALTER TABLE `configeconomiasfutbol8` DISABLE KEYS */;
INSERT INTO `configeconomiasfutbol8` VALUES (4,4,7,12,0,5,5,1,10,20,0,10,70,10);
/*!40000 ALTER TABLE `configeconomiasfutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cronicasfutbol8`
--

DROP TABLE IF EXISTS `cronicasfutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cronicasfutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `partido` bigint(20) NOT NULL,
  `accion` varchar(255) NOT NULL,
  `cuadrante` varchar(3) NOT NULL,
  `minuto` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=359070 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cronicasfutbol8`
--

LOCK TABLES `cronicasfutbol8` WRITE;
/*!40000 ALTER TABLE `cronicasfutbol8` DISABLE KEYS */;
/*!40000 ALTER TABLE `cronicasfutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entrenadoresfutbol8`
--

DROP TABLE IF EXISTS `entrenadoresfutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entrenadoresfutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `grupo` bigint(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `equipo` bigint(20) DEFAULT NULL,
  `tacticas` varchar(255) NOT NULL,
  `ficha` int(11) NOT NULL,
  `contrato` int(11) NOT NULL,
  `ultimatacticautilizada` int(11) DEFAULT NULL,
  `plustactica` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entrenadoresfutbol8`
--

LOCK TABLES `entrenadoresfutbol8` WRITE;
/*!40000 ALTER TABLE `entrenadoresfutbol8` DISABLE KEYS */;
/*!40000 ALTER TABLE `entrenadoresfutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entrenadoresmaestrofutbol8`
--

DROP TABLE IF EXISTS `entrenadoresmaestrofutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entrenadoresmaestrofutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=225 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entrenadoresmaestrofutbol8`
--

LOCK TABLES `entrenadoresmaestrofutbol8` WRITE;
/*!40000 ALTER TABLE `entrenadoresmaestrofutbol8` DISABLE KEYS */;
INSERT INTO `entrenadoresmaestrofutbol8` VALUES (118,'Abel Carlos da Silva Braga'),(151,'Adenor Leonardo Bachi »Tite«'),(141,'Adnan Hamad Majeed'),(54,'Aimé Jacquet'),(212,'Akira Nishino'),(142,'Alberto Malesani'),(213,'Alberto Zaccheroni'),(68,'Alejandro Javier Sabella'),(91,'Aleksandrs Starkovs'),(178,'Alexander McLeish'),(159,'Alexandre Borges Guimarães'),(95,'Alfio Rubén Oscar Basile'),(22,'Aloysius Paulus Maria \"Louis\" van Gaal'),(179,'Américo Rubén Gallego'),(46,'Anghel Iordanescu'),(96,'Antônio Lopes dos Santos'),(171,'Antonio López'),(132,'Antônio Luís Alves Ribeiro Oliveira'),(2,'Arsène Wenger'),(57,'Arthur Antunes Coimbra »Zico«'),(127,'Avram Grant'),(40,'Bert van Marwick'),(191,'Bertrand Marchand'),(79,'Bo Johansson'),(160,'Branko Ivankovic'),(29,'Bruce Arena'),(92,'Bruno Metsu'),(11,'Carlo Ancelotti'),(18,'Carlos Alberto Gomes Parreira'),(16,'Carlos Bianchi'),(21,'Carlos Caetano Bledorn Verri »Dunga«'),(119,'Carlos Luis Ischia'),(172,'Carlos Rexach'),(131,'Celso Juárez Roth'),(143,'Cesare Maldini'),(128,'Christian Gross'),(200,'Claude Puel'),(144,'Claudio Borghi'),(152,'Claudio Gentile'),(73,'Claudio Ranieri'),(105,'Clive Barker'),(168,'Craig Brown'),(53,'Daniel Alberto Passarella'),(64,'David Leary'),(70,'Didier Deschamps'),(180,'Diego Garzitto'),(69,'Dino Zoff'),(30,'Dirk Nicolaat \"Dick\" Advocaat'),(207,'Dr. Francisco Maturana'),(97,'Edgardo Bauza'),(56,'Egil Olsen'),(214,'Elie Baup'),(145,'Émerson Leão'),(122,'Eric Gerets'),(146,'Erich Ribbeck'),(208,'Ernesto Valverde Tejedor'),(10,'Fabio Capello'),(43,'Fatih Terim'),(32,'Felix Magath'),(161,'Francisco Guidolin'),(12,'Franklin Rijkaard'),(181,'Freddy Santos Ternero'),(27,'Gerard Houllier'),(72,'Gerardo Daniel Martino'),(58,'Gianluca Vialli'),(17,'Giovanni Trapattoni'),(50,'Glenn Hoddle'),(192,'Gustavo Benítez'),(147,'Gustavo Julio Alfaro'),(4,'Guus Hiddink'),(98,'Guy Roux'),(36,'Hans-Hubert Vogts'),(63,'Hassan Shehata'),(19,'Héctor Raúl Cúper'),(51,'Henri Michel'),(176,'Henry James Redknapp'),(162,'Hernán Darío Gómez'),(209,'Hernán Evaristo Medford'),(193,'Holger Osieck'),(110,'Hubertus Jozef Margaretha \"Huub\" Stevens'),(182,'Hugo Daniel Tocalli'),(111,'Hugo Sánchez'),(82,'Humberto Manuel Jesus Coelho'),(220,'Ilija Petkovic'),(215,'Iosif Szabo'),(216,'Jacobus \"Co\" Adriaanse'),(87,'Jacques Santini'),(194,'Jaime de La Pava'),(148,'Jair Picerni'),(85,'Jakob Kuhn'),(86,'Javier Aguirre'),(74,'Javier Clemente'),(80,'Javier Iruretagoyena »Irureta«'),(123,'Jean Tigana'),(202,'Jean-François De Sart'),(112,'Jean-Paul Okono'),(120,'Jerzy Engel'),(23,'Joachim Löw'),(183,'Joel Natalino Santana'),(99,'Johannes \"Jo\" Bonfrère'),(184,'Jong-Hun Kim'),(133,'Jorge Daniel Fossatti'),(100,'Jorvan Vieira'),(26,'José Antonio Camacho'),(185,'José Ignacio »Iñaki« Sáez'),(49,'José Luis Aragonés Suárez'),(195,'José Luís Trejo'),(163,'José Manuel Esnal »Mane«'),(5,'José Mário dos Santos Mourinho Felix'),(31,'José Néstor Pekerman'),(65,'Josef Heynckes'),(41,'Josep Guardiola'),(153,'Joseph Michael McCarthy'),(52,'Jozef Chovanec'),(186,'Juan Ramón Carrasco'),(47,'Juande la Cruz Ramos Cano'),(62,'Jürgen Klinsmann'),(199,'Jürgen Norbert Klopp'),(15,'Karel Brückner'),(113,'Kevin Keegan'),(101,'Klaus Toppmöller'),(154,'Kurban Berdyev'),(150,'Lamine N\'Diaye'),(129,'Laurent Robert Blanc'),(102,'Leo Beenhakker'),(89,'Luciano Spalletti'),(210,'Luigi Del Negri'),(103,'Luigi Simoni'),(104,'Luis Fernandez'),(124,'Luis Fernando Montoya'),(134,'Luis Fernando Suárez'),(217,'Luis Fernando Tena'),(7,'Luiz Felipe Scolari'),(173,'Mahmoud El-Gohary'),(198,'Manour Ebrahimzadeh'),(88,'Manuel José Jesus Silva'),(37,'Manuel Lapuente'),(59,'Manuel Pellegrini'),(39,'Marcel van Basten'),(3,'Marcello Lippi'),(8,'Marcelo Alberto Bielsa'),(44,'Mário Jorge Lobo Zagallo'),(149,'Martin O\'Neill'),(164,'Matjaz Kek'),(155,'Matthias Sammer'),(221,'Mehmed Baždarevic'),(135,'Miguel Ángel Russo'),(117,'Milovan Rajevac'),(48,'Mircea Lucescu'),(60,'Miroslav Blaževic'),(45,'Morten Olsen'),(66,'Muricy Ramalho'),(222,'Mustafa Denizli'),(156,'Nelson Bonifacio Acosta'),(114,'Nery Alberto Pumpido'),(115,'Nils Johan Semb'),(187,'Nils-Arne Eggen'),(81,'Oleg Blokhin'),(75,'Oleg Romantsev'),(109,'Óscar Wáschington Tabárez'),(106,'Oswaldo de Oliveira Filho'),(6,'Ottmar Hitzfeld'),(20,'Otto Rehhagel'),(76,'Paulo Autuori de Mello'),(169,'Paulo César Carpegiani'),(203,'Pawel Janas'),(42,'Philippe Troussier'),(130,'Pierre Lechantre'),(108,'Quique Sánchez Flores'),(188,'Rabah Sâadane'),(14,'Rafael Benítez'),(174,'Ralf Rangnick'),(25,'Ramón Ángel Díaz'),(211,'Ramón Armando Cabrero'),(218,'Ramón Enrique Maradiaga'),(136,'Ratomir Dujkovic'),(55,'Raymond Domenech'),(223,'Reinaldo Rueda'),(189,'René Rodrigues Simões'),(77,'Ricardo Antonio La Volpe'),(165,'Ricardo Raimundo Gomes'),(166,'Richard Alfred Páez'),(177,'Richard Robert Herbert'),(71,'Robert Bradley'),(93,'Roberto Donadoni'),(33,'Roberto Mancini'),(137,'Rodomir Antic'),(24,'Roger Lemerre'),(138,'Ronald Koeman'),(139,'Roy Hodgson'),(219,'Rubén Dario Insúa'),(34,'Rudolf Völler'),(125,'Ruud Gullit'),(190,'Samson Siasia'),(78,'Senol Günes'),(107,'Sergio Daniel Batista'),(83,'Sergio Markarián'),(175,'Sérgio Ricardo de Paiva Farias'),(167,'Silvia Neid'),(1,'Sir Alexander Ferguson'),(94,'Sir Robert Robson'),(61,'Slaven Bilic'),(67,'Srecko Katanec'),(196,'Stephen Keshi'),(9,'Sven-Göran Eriksson'),(201,'Tae-Yong Shin'),(158,'Takeshi Okada'),(204,'Temuri Ketsbaia'),(157,'Thomas Schaaf'),(116,'Tommy Söderberg'),(197,'Unal Emery Etxegoien'),(121,'Valery Gazzaev'),(35,'Valery Lobonovsky'),(38,'Velibor Milutinovic'),(13,'Vicente del Bosque'),(224,'Víctor Fernández'),(170,'Víctor Haroldo Pua'),(126,'Victor Piturca'),(205,'Viktor Mikhailovich Goncharenko'),(90,'Vladimir Weiss'),(206,'Vujadin Boškov'),(28,'Wanderley Luxemburgo'),(84,'Winfried Schäfer'),(140,'Zlatko Kranjcar');
/*!40000 ALTER TABLE `entrenadoresmaestrofutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equiposfutbol8`
--

DROP TABLE IF EXISTS `equiposfutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `equiposfutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activo` tinyint(1) NOT NULL,
  `automatico` tinyint(1) NOT NULL,
  `moral` int(11) NOT NULL,
  `campo` int(11) NOT NULL,
  `publicidad` int(11) NOT NULL,
  `eqtecnico` int(11) NOT NULL,
  `ojeadores` int(11) NOT NULL,
  `jugadoresojeados` int(11) NOT NULL,
  `presupuesto` int(11) NOT NULL,
  `credito` int(11) NOT NULL,
  `bolsa` int(11) NOT NULL,
  `fechabolsa` date DEFAULT NULL,
  `entrenamiento` tinyint(1) NOT NULL,
  `simulaciones` int(11) NOT NULL,
  `modoauto` tinyint(1) NOT NULL,
  `club` bigint(11) NOT NULL,
  `ampliarcampo` tinyint(1) NOT NULL,
  `ampliarpublicidad` tinyint(1) NOT NULL,
  `ampliareqtecnico` tinyint(1) NOT NULL,
  `ampliarojeadores` tinyint(1) NOT NULL,
  `precioentradas` int(11) NOT NULL,
  `equipacion` int(11) NOT NULL,
  `espectativa` int(11) NOT NULL,
  `posicionJuvenil` int(11) NOT NULL,
  `nombrecampo` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `club_UNIQUE` (`club`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equiposfutbol8`
--

LOCK TABLES `equiposfutbol8` WRITE;
/*!40000 ALTER TABLE `equiposfutbol8` DISABLE KEYS */;
/*!40000 ALTER TABLE `equiposfutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equiposmaestrofutbol8`
--

DROP TABLE IF EXISTS `equiposmaestrofutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `equiposmaestrofutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=869 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equiposmaestrofutbol8`
--

LOCK TABLES `equiposmaestrofutbol8` WRITE;
/*!40000 ALTER TABLE `equiposmaestrofutbol8` DISABLE KEYS */;
INSERT INTO `equiposmaestrofutbol8` VALUES (796,'1. FSV Mainz 05'),(660,'Aalesunds FK'),(839,'Aberdeen FC'),(636,'AC Chievo Verona'),(671,'AC Horsens'),(655,'AC Siena'),(532,'AC Sparta Praha'),(619,'Adelaide United FC'),(609,'AEK Athens'),(602,'AEK Larnaka'),(649,'AEL Lemesos'),(812,'AFA Djekanou'),(513,'AFC Ajax Amsterdam'),(490,'AFC Rapid Bucuresti'),(627,'Ajaccio AC'),(614,'Al-Ahli SC Jeddah'),(587,'Al-Ahly Cairo'),(670,'Al-Faysali'),(866,'Al-Gharrafa SC Doha'),(545,'Al-Hilal FC Riyadh'),(523,'Al-Hilal Omdurman'),(612,'Al-Ittifaq Dammam'),(562,'Al-Ittihad'),(588,'Al-Jazeera Abu Dhabi'),(632,'Al-Kuwait SC Kaifan'),(617,'Al-Merreikh Omdurman'),(781,'Al-Muharraq Manama'),(724,'Al-Sadd SC Doha'),(753,'Al-Safa SC Beirut'),(765,'Al-Shorta'),(647,'Al-Wehdat Club Amman'),(638,'Alajuelense'),(658,'Ammochostos'),(543,'Antonio'),(479,'APOEL Lefkosia'),(635,'Arbil Sport Club'),(745,'Arema FC Malang'),(676,'Argentinos'),(457,'Arsenal FC London'),(476,'Arsenal FC Sarandí'),(752,'AS de Nancy-Lorraine'),(637,'AS de Saint-Etienne'),(584,'AS Roma'),(739,'ASEC Mimosas Abidjan'),(715,'ASO Chlef'),(683,'Asociación'),(805,'Associação'),(847,'Aston Villa FC'),(580,'Asunción'),(445,'Athletic'),(439,'Atlético'),(622,'Atromitos Athens'),(556,'Avellaneda'),(460,'AZ Alkmaar'),(767,'Bany Yas SCC'),(597,'Barcelona'),(558,'Barranquilla'),(469,'Benfica'),(569,'Beograd'),(688,'Bergamasca'),(507,'Besiktas JK Istanbul'),(517,'Blue-Wings'),(700,'Bologna Calcio'),(824,'Bolton Wanderers FC'),(565,'Botafogo'),(763,'Boyacá Chicó FC'),(708,'Brisbane Roar FC'),(783,'Bromwich'),(591,'BSC Young Boys Bern'),(576,'Bunyodkor'),(611,'Buriram'),(560,'Bursaspor K Bursa'),(470,'BV Borussia Dortmund'),(668,'CA All Boys'),(634,'CA Colón de Santa Fe'),(483,'CA Lanús'),(687,'CA Rafaela'),(554,'CA Tigre'),(716,'CA Unión Santa Fe'),(444,'CA Vélez Sarsfield'),(800,'Cagliari Calcio'),(718,'Catania Calcio'),(719,'CCD Tolima Ibague'),(652,'CD Aurora Cochabamba'),(673,'CD Cobreloa Calama'),(729,'CD Iquique'),(546,'CD Isidro Metapán'),(803,'CD Itagüí Ditaires'),(653,'CD La Equidad Bogotá'),(791,'CD Unión Comerico'),(842,'Cerezo Osaka FC'),(732,'CFR 1907 Cluj'),(606,'Chelsea FC Berekum'),(441,'Chelsea FC London'),(564,'Chiclayo'),(578,'Chonburi FC'),(516,'Ciudad'),(590,'Club Africain Tunis'),(669,'Club Alianza Lima'),(573,'Club Bolívar La Paz'),(489,'Club Brugge KV'),(582,'Club Sport Herediano'),(838,'Club Tijuana'),(836,'COD Meknés'),(646,'Comunicaciones'),(443,'Corinthians'),(536,'Coritiba FC'),(728,'Cotonsport'),(861,'Crusaders FC'),(665,'CS Gaz Methan Medias'),(848,'CS Marítimo Funchal'),(856,'CS Panduri Târgu Jiu'),(785,'CSD Leon de Huánuco'),(822,'CSKA Sofia'),(835,'Daugavpils'),(681,'Debreceni VSC'),(855,'Deportes'),(487,'Deportivo'),(550,'Desportivo'),(863,'Djoliba AC Bamako'),(766,'Dnipropetrovsk'),(831,'Dolphins'),(777,'Dundee United FC'),(840,'Dynamos'),(674,'Dynamos FC Harare'),(613,'Dzepcište'),(726,'ENPPI Cairo'),(592,'Eskisehirspor'),(768,'España'),(505,'Espérance'),(525,'Esteghlal FC Tehran'),(625,'Estudiantes'),(621,'Evergrande'),(570,'Everton FC'),(758,'F91 Dudelange'),(471,'FBC Olimpia Asunción'),(735,'FC Anzhi Makhachkala'),(703,'FC Asteras Tripolis'),(435,'FC Barcelona'),(455,'FC Basel'),(540,'FC BATE Barysau'),(438,'FC Bayern München'),(827,'FC Busan I\'Park'),(577,'FC Dacia Chisinau'),(813,'FC Dallas'),(651,'FC de Toulouse'),(685,'FC Dila Gori'),(539,'FC Dinamo Bucuresti'),(503,'FC Dinamo Kyiv'),(810,'FC Dinamo Moscow'),(832,'FC Dinamo Tbilisi'),(526,'FC do Porto'),(738,'FC Flora Tallínn'),(852,'FC Gamba Osaka'),(845,'FC Genoa 1893'),(500,'FC København'),(628,'FC Levadia Tallínn'),(511,'FC Lokomotiv Moscow'),(631,'FC Luzern'),(644,'FC Metalurh Donetsk'),(828,'FC Midtjylland'),(816,'FC Milsami Orhei'),(829,'FC Neftchi Fergana'),(776,'FC Otelul Galati'),(667,'FC Parma'),(534,'FC Pohang Steelers'),(491,'FC Red Bull Salzburg'),(458,'FC Schalke 04'),(595,'FC Seoul'),(502,'FC Shakhtar Donetsk'),(583,'FC Sheriff Tiraspol'),(598,'FC Sion'),(579,'FC Slovan Liberec'),(792,'FC Spartak Moscow'),(643,'FC Spartak Trnava'),(521,'FC Steaua Bucuresti'),(450,'FC Twente Enschede'),(650,'FC Vorskla Poltava'),(692,'FC Zimbru Chisinau'),(648,'FC Zürich'),(538,'Fenerbahçe'),(746,'Feyenoord Rotterdam'),(809,'FH Hafnafjörður'),(736,'Fiorentina'),(549,'FK Austria Wien'),(773,'FK Baumit Jablonec'),(551,'FK Ekranas Panevežys'),(618,'FK Gomel'),(464,'FK Metalist Charkiv'),(864,'FK Metalurg Skopje'),(663,'FK Mladá Boleslav'),(547,'FK Partizan Beograd'),(492,'FK Rubin Kazan'),(761,'FK Senica'),(690,'FK Skonto Riga'),(820,'FK Teplice'),(770,'FK Vadar Skopje'),(661,'FK Zestafoni'),(494,'Flamengo'),(733,'Florianópolis'),(456,'Fluminense'),(527,'FM Sepahan Isfahan'),(509,'Fulham FC'),(717,'Gaillard'),(608,'Galatasaray'),(754,'Getafe FC'),(615,'Girondins'),(496,'Glasgow Celtic FC'),(610,'Glasgow Rangers FC'),(695,'Goianiense'),(823,'Grenada CF'),(747,'Guadalajara'),(834,'Guaraní FC Asunción'),(518,'Guayaquil'),(467,'Hannoverscher'),(801,'Hapoel'),(553,'Hapoel Tel-Aviv FC'),(706,'Heartland FC Oweri'),(639,'Helsingborg IF'),(790,'Heracles Almelo'),(853,'Hibernian'),(559,'HJK Helsinki'),(641,'Home United FC'),(600,'Horizonte'),(498,'Hyundai'),(691,'IF Elfsborg Borås'),(585,'Independiente'),(468,'Internacional'),(596,'International'),(488,'Internazionale'),(851,'Italiano'),(446,'Janeiro'),(495,'Jeonbuk'),(807,'JK Nõmme Kalju'),(440,'Juniors'),(485,'Juventus FC Torino'),(571,'KAA Gent'),(764,'Kalmar FF'),(841,'Karabükspor'),(514,'Kashiwa Reysol'),(574,'Kelantan'),(722,'Kitchee SC'),(705,'KKS Lech Poznan'),(830,'Koprivnica'),(499,'KP Legia Warszawa'),(552,'KRC Genk'),(723,'KS Ruch Chorzów'),(814,'KSV Cercle Brugge'),(788,'Kuopio Palloseura'),(662,'Kuwait-City'),(640,'KV Kortrijk'),(786,'LDU de Loja'),(519,'Leverkusen'),(442,'Libertad'),(860,'Lierse SK'),(581,'Linfield FAC'),(531,'Liverpool FC'),(710,'Ljubljana'),(862,'Lokomotiv'),(689,'Lorenzo'),(633,'Los Angeles Galaxy'),(725,'Ludogorets'),(594,'Maccabi Haifa FC'),(740,'Maccabi Tel-Aviv'),(666,'Málaga CF'),(857,'Malien'),(742,'Malmö FF'),(453,'Manchester City FC'),(463,'Manchester United FC'),(774,'Manizales'),(510,'Maribor'),(698,'Marijampole'),(686,'Mariners'),(535,'MAS Fès'),(680,'Metalurgist'),(603,'Metalurgs'),(566,'Midlothian'),(454,'Milan AC'),(808,'Molde FK'),(477,'Monterrey'),(567,'Montevideo'),(520,'Montpellier'),(759,'Motherwell FC'),(727,'Municipal'),(548,'Mönchengladbach'),(702,'MŠK Žilina'),(533,'Nacional'),(544,'Nagoya Grampus'),(645,'Nasaf Qarshi FC'),(771,'ND Mura 05'),(604,'Newcastle United FC'),(696,'Newell\'s'),(530,'NK Dinamo Zagreb'),(743,'NK Osijek'),(629,'Nordsjælland'),(756,'Norwich City FC'),(730,'O\'Higgins'),(657,'Odense BK'),(819,'OGC Nice'),(462,'Olympiakos'),(493,'Olympique'),(448,'Olympique Lyonnais'),(794,'Omonia FC Lefkosia'),(787,'Pachuca CF'),(656,'Pakhtakor'),(537,'Palmeiras'),(599,'Panathinaikos'),(486,'PAOK Thessaloniki'),(817,'Paranaense'),(679,'Patrick\'s'),(709,'Persepolis FC Tehran'),(512,'Petersburg'),(859,'Petrolero'),(616,'PFC CSKA Moscow'),(795,'Portadown FC'),(555,'Porto-Alegrense'),(447,'PSV Eindhoven'),(751,'RAEC Mons'),(778,'Real Betis Balompié'),(825,'Real Estelí FC'),(437,'Real Madrid CF'),(798,'Real Zaragoza CD'),(630,'Rosenborg'),(466,'RSC Anderlecht'),(461,'Saint-Germain'),(750,'Salvador'),(854,'Sanfrecce'),(506,'Santa Fe CD Bogotá'),(682,'Santiago'),(459,'Santos FC'),(557,'São Paulo FC'),(659,'SBV Vitesse Arnhem'),(642,'SC Heerenveen'),(605,'SC Lokeren'),(528,'Seattle Sounders FC'),(749,'Sebastián'),(524,'Seongnam'),(744,'Servette FC Geneve'),(677,'Sevilla FC'),(707,'Shamrock Rovers FC'),(799,'Shirak FC Gyumri'),(804,'SK Karpaty Lviv'),(684,'SK SIGMA Olomouc'),(762,'SK Skënderbeu Korçë'),(693,'SK Sturm Graz'),(672,'SK Tiranë'),(473,'SK Viktoria Plzen'),(760,'Sligo Rovers FC'),(843,'SM Caen'),(780,'Sochaux-Montbeliard'),(821,'Sportife'),(449,'Sporting'),(501,'Sporting Club Vaslui'),(846,'Sportive'),(481,'SS Lazio Roma'),(452,'SSC Napoli'),(802,'Stade Brest'),(480,'Stade Rennais FC'),(472,'Standard'),(478,'Stoke City FC'),(607,'Strongest'),(701,'Sunderland AFC'),(620,'Sunshine'),(755,'SV Ried'),(844,'SV Werder Bremen'),(757,'Swansea City FC'),(806,'Székesféhervár'),(775,'Sønderjysk'),(712,'Tampines Rovers FC'),(858,'Tauro FC Panamá-City'),(833,'TCDK'),(779,'Tegucigalpa'),(793,'Tel-Aviv'),(815,'Terengganu'),(589,'Tokyo FC'),(593,'Toronto FC (Canada)'),(475,'Torreon'),(465,'Tottenham Hotspur FC'),(522,'Trabzonspor'),(714,'TSG Hoffenheim'),(734,'Tucuman'),(623,'UD Levante'),(482,'Udinese Calcio'),(748,'United'),(436,'Universidad'),(474,'Universitaria'),(561,'Universitario'),(699,'US Citta di Palermo'),(451,'Valencia CF'),(713,'Valenciennes AFC'),(868,'Valletta FC'),(697,'Vegalta Sendai'),(624,'VfB Stuttgart'),(704,'Villarreal CF'),(741,'Vojvodina'),(826,'Wigan Athletic FC'),(529,'Wisla Kraków SA'),(769,'WSK Slask Wroclaw'),(542,'Wydad AC Casablanca'),(811,'Yokohama'),(731,'Zalgiris'),(568,'Zamalek SC Cairo'),(837,'Zvishavane'),(654,'ŠK Slovan Bratislava');
/*!40000 ALTER TABLE `equiposmaestrofutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estadisticaspartidofutbol8`
--

DROP TABLE IF EXISTS `estadisticaspartidofutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estadisticaspartidofutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `partido` bigint(20) NOT NULL,
  `eqlocal` varchar(50) NOT NULL,
  `eqvisitante` varchar(50) NOT NULL,
  `alineacionlocal` varchar(999) NOT NULL,
  `alineacionvisitante` varchar(999) NOT NULL,
  `goleslocal` int(11) NOT NULL,
  `golesvisitante` int(11) NOT NULL,
  `tacticalocal` int(11) NOT NULL,
  `tacticavisitante` int(11) NOT NULL,
  `esfuerzolocal` varchar(10) NOT NULL,
  `esfuerzovisitante` varchar(10) NOT NULL,
  `estrategialocal` varchar(30) NOT NULL,
  `estrategiavisitante` varchar(30) NOT NULL,
  `primaslocal` int(11) NOT NULL,
  `primasvisitante` int(11) NOT NULL,
  `posesionlocal` int(11) NOT NULL,
  `posesionvisitante` int(11) NOT NULL,
  `jugadaslocal` int(11) NOT NULL,
  `jugadasvisitante` int(11) NOT NULL,
  `ocasioneslocal` int(11) NOT NULL,
  `ocasionesvisitante` int(11) NOT NULL,
  `tirospuertalocal` int(11) NOT NULL,
  `tirospuertavisitante` int(11) NOT NULL,
  `tiroslejanoslocal` int(11) NOT NULL,
  `tiroslejanosvisitante` int(11) NOT NULL,
  `faltasdirectaslocal` int(11) NOT NULL,
  `faltasdirectasvisitante` int(11) NOT NULL,
  `cornerslocal` int(11) NOT NULL,
  `cornersvisitante` int(11) NOT NULL,
  `penaltieslocal` int(11) NOT NULL,
  `penaltiesvisitante` int(11) NOT NULL,
  `goleadoreslocal` varchar(999) NOT NULL,
  `goleadoresvisitante` varchar(999) NOT NULL,
  `tarjetaslocal` varchar(999) NOT NULL,
  `tarjetasvisitante` varchar(999) NOT NULL,
  `lesionadoslocal` varchar(999) NOT NULL,
  `lesionadosvisitante` varchar(999) NOT NULL,
  `extraseqlocal` varchar(999) NOT NULL,
  `extraseqvisitante` varchar(999) NOT NULL,
  `morallocal` int(11) NOT NULL,
  `moralvisitante` int(11) NOT NULL,
  `victoriaslocal` int(11) NOT NULL,
  `victoriasvisitante` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1303 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estadisticaspartidofutbol8`
--

LOCK TABLES `estadisticaspartidofutbol8` WRITE;
/*!40000 ALTER TABLE `estadisticaspartidofutbol8` DISABLE KEYS */;
/*!40000 ALTER TABLE `estadisticaspartidofutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goleadoresfutbol8`
--

DROP TABLE IF EXISTS `goleadoresfutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goleadoresfutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `competicion` bigint(20) NOT NULL,
  `equipo` varchar(50) NOT NULL,
  `posicion` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `golesliga` int(11) NOT NULL,
  `golescopa` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goleadoresfutbol8`
--

LOCK TABLES `goleadoresfutbol8` WRITE;
/*!40000 ALTER TABLE `goleadoresfutbol8` DISABLE KEYS */;
/*!40000 ALTER TABLE `goleadoresfutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jornadasfutbol8`
--

DROP TABLE IF EXISTS `jornadasfutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jornadasfutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `numero` int(11) NOT NULL,
  `descripcion` varchar(200) NOT NULL,
  `competicion` bigint(20) NOT NULL,
  `fecha` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=143 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jornadasfutbol8`
--

LOCK TABLES `jornadasfutbol8` WRITE;
/*!40000 ALTER TABLE `jornadasfutbol8` DISABLE KEYS */;
/*!40000 ALTER TABLE `jornadasfutbol8` ENABLE KEYS */;
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
  `equipo` bigint(20) DEFAULT NULL,
  `grupo` bigint(20) NOT NULL,
  `ficha` int(11) NOT NULL,
  `clausula` int(11) NOT NULL,
  `blindado` tinyint(1) NOT NULL,
  `jornadasLesion` int(11) NOT NULL,
  `transferible` tinyint(1) NOT NULL,
  `contrato` int(11) NOT NULL,
  `posicion` int(11) NOT NULL,
  `valoracion` int(11) NOT NULL,
  `estadoFisico` int(11) NOT NULL,
  `tarjetaAmarilla` tinyint(1) NOT NULL,
  `tarjetaRoja` tinyint(1) NOT NULL,
  `juegaJornada` tinyint(1) NOT NULL,
  `golesliga` int(11) NOT NULL,
  `golescopa` int(11) NOT NULL,
  `partidosjugados` int(11) NOT NULL,
  `ensubasta` tinyint(1) NOT NULL,
  `puja` int(11) NOT NULL,
  `equipopuja` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1682 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=336 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jugadoresmaestrofutbol8`
--

LOCK TABLES `jugadoresmaestrofutbol8` WRITE;
/*!40000 ALTER TABLE `jugadoresmaestrofutbol8` DISABLE KEYS */;
INSERT INTO `jugadoresmaestrofutbol8` VALUES (3,'Adriano',2),(4,'Aimar',2),(5,'Albelda',2),(6,'Amancio',2),(7,'Amavisca',2),(8,'Amor',2),(9,'Asensi',2),(10,'Assuncao',2),(11,'Bakero',2),(12,'Baraja',2),(13,'Beckan',2),(14,'Begiristain',2),(15,'Berckam',2),(16,'Caldere',2),(17,'Cambiasso',2),(18,'Cani',2),(19,'Clos',2),(20,'Cocu',2),(21,'Cruyff Joan',2),(22,'Dani Alves',2),(23,'De la Pena',2),(24,'De Pedro',2),(25,'DeBoer',2),(26,'Deco',2),(27,'Emerson',2),(28,'Eusebio',2),(29,'Figo',2),(30,'Flavio',2),(31,'Gento',2),(32,'Gerard',2),(33,'Gio',2),(34,'Guardiola',2),(35,'Guerrero',2),(36,'Guiti',2),(37,'Gulit',2),(38,'Gurpegui',2),(39,'hagi',2),(40,'Helgera',2),(41,'Iniesta',2),(42,'Juan Senor',2),(43,'Juanfran',2),(44,'Juanito',2),(45,'Kaka',2),(46,'Karpin',2),(47,'Kiki Mussampa',2),(48,'La Torre',2),(49,'Lampard',2),(50,'Laudrup',2),(51,'Luis Enrique',2),(52,'Luque',2),(53,'Makelele',2),(54,'Marcial',2),(55,'Martin Vazquez',2),(56,'McManaman',2),(57,'Mendieta',2),(58,'Michel',2),(59,'Milla',2),(60,'Mota',2),(61,'Motta',2),(62,'Neeskens',2),(63,'Onesimo',2),(64,'Overmars',2),(65,'Pinilla',2),(66,'Pirri',2),(67,'Pochettino',2),(68,'Prosineky',2),(69,'Redondo',2),(70,'Rexach',2),(71,'Riquelme',2),(72,'Rochemback',2),(73,'Rufete',2),(74,'Schuster',2),(75,'Seedorf',2),(76,'Senor',2),(77,'Silvinho',2),(78,'Simeone',2),(79,'Solari',2),(80,'Soler',2),(81,'Toro Acuna',2),(82,'Urbano',2),(83,'Valeron',2),(84,'Vicente',2),(85,'Victor',2),(86,'Vieira',2),(87,'Xavi',2),(88,'Zenden',2),(89,'Zidane',2),(90,'Abelardo',1),(91,'Aitor Ocio',1),(92,'Alexanco',1),(93,'Alkorta',1),(94,'Amarillo',1),(95,'Andersson',1),(96,'Aranzabal',1),(97,'Ayala',1),(98,'Beckenbauer',1),(99,'Beletti',1),(100,'Camacho',1),(101,'Cannavaro',1),(102,'Capdevila',1),(103,'Carboni',1),(104,'Celades',1),(105,'Chendo',1),(106,'Christanval',1),(107,'Coco',1),(108,'Colochini',1),(109,'Costas',1),(110,'Curro Torres',1),(111,'De Boer',1),(112,'De la Cruz',1),(113,'Del horno',1),(114,'Del Sol',1),(115,'Edmilson',1),(116,'Ferrer',1),(117,'Gallego',1),(118,'Goikoechea',1),(119,'Gurrutxaga',1),(120,'Helguera',1),(121,'Hierro',1),(122,'Jarque',1),(123,'Javi Navarro',1),(124,'Jorge Andrade',1),(125,'Julen Guerrero',1),(126,'Julio Alberto',1),(127,'koeman',1),(128,'Lizarazu',1),(129,'Lopo',1),(130,'Maceda',1),(131,'Manuel Pablo',1),(132,'Marchena',1),(133,'Marquez',1),(134,'Martagon',1),(135,'Matterazzi',1),(136,'Michel Salgado',1),(137,'Migueli',1),(138,'Milito',1),(139,'Minambres',1),(140,'Moratalla',1),(141,'Navarro',1),(142,'Naybet',1),(143,'Olaizola',1),(144,'Oleguer',1),(145,'Olmo',1),(146,'Pablo',1),(147,'Pablo Alfaro',1),(148,'Pavon',1),(149,'Pellegrino',1),(150,'Pepe',1),(151,'Pernia',1),(152,'Pikabea',1),(153,'Poli',1),(154,'Pujol',1),(155,'Reiziger',1),(156,'Roberto Carlos',1),(157,'Sagi Barba',1),(158,'Sagnol',1),(159,'Sanchis',1),(160,'Sergi',1),(161,'Sergio Ramos',1),(162,'Serna',1),(163,'Sorin',1),(164,'Stilike',1),(165,'Tarzan Migueli',1),(166,'Terry',1),(167,'Thuram',1),(168,'Yago Yao',1),(169,'Zambrotta',1),(170,'Alfonso',3),(171,'Angelito Cuellar',3),(172,'Archivald',3),(173,'Canniga',3),(174,'Cantona',3),(175,'Carrasco',3),(176,'Cuellar',3),(177,'Dani',3),(178,'Derticia',3),(179,'DiStefano',3),(180,'Drogba',3),(181,'Dugarry',3),(182,'El burrito Ortega',3),(183,'Emilio Butragueno',3),(184,'Esteban',3),(185,'Etoo',3),(186,'Ezquerro',3),(187,'Fred',3),(188,'Hugo Sanchez',3),(189,'Joaquin',3),(190,'Julio Salinas',3),(191,'Kanute',3),(192,'Kluivert',3),(193,'Kodro',3),(194,'Kovacevic',3),(195,'Krankl',3),(196,'Kubala',3),(197,'Lineker',3),(198,'Luca Toni',3),(199,'Maradona',3),(200,'Marcos',3),(201,'Messi',3),(202,'Mijatovic',3),(203,'Munitis',3),(204,'Pandiani',3),(205,'Pedro Munitis',3),(206,'Pele',3),(207,'Portillo',3),(208,'Puskas',3),(209,'Quini',3),(210,'Raul',3),(211,'Ribaldo',3),(212,'Rife',3),(213,'Rivaldo',3),(214,'Robinho',3),(215,'Rojo',3),(216,'Romario',3),(217,'Ronaldinho',3),(218,'Ronaldo',3),(219,'Salinas',3),(220,'Samitier',3),(221,'Santillana',3),(222,'Satrustegui',3),(223,'Saviola',3),(224,'Segarra',3),(225,'Simonsen',3),(226,'Stoichkov',3),(227,'Stoikov',3),(228,'Suker',3),(229,'Tamudo',3),(230,'Torpedo Muller',3),(231,'Torres',3),(232,'Tote',3),(233,'Tristan',3),(234,'Turu Flores',3),(235,'Villa',3),(236,'Zamorano',3),(237,'Zoco',3),(238,'Abel Resino',0),(239,'Ablanedo',0),(241,'Aranzubia',0),(242,'Arkonada',0),(243,'Artola',0),(244,'Bartez',0),(245,'Bonano',0),(246,'Buffon',0),(248,'Buyo',0),(249,'Cañizares',0),(250,'Carlos Roa',0),(251,'Casillas',0),(253,'Chilavert',0),(255,'Garcia Remon',0),(256,'Gianluca Pavliuca',0),(257,'Iribar',0),(265,'Leo Franco',0),(266,'Lopetegui',0),(267,'Molina',0),(268,'Mono Burgos',0),(269,'Nkono',0),(270,'Ochotorena',0),(271,'Oliver Kahn',0),(274,'Palop',0),(276,'Petr Cech',0),(277,'Pinto',0),(279,'Ramallets',0),(280,'Reina',0),(281,'Salvador Sadurni',0),(283,'Taffarel',0),(284,'Urruti',0),(286,'Victor Bahia',0),(287,'Victor Valdes',0),(288,'Walter Zenga',0),(290,'Zamora',0),(291,'Zubizarreta',0),(292,'Higuain',3),(293,'Benzema',3),(294,'Alexis',3),(295,'Pedro',3),(318,'Arbeloa',1),(319,'Abidal',1),(320,'Albiol',1),(321,'Busquets',2),(322,'Ballack',2),(323,'Cesc',2),(324,'Gattuso',1),(325,'Miguel',1),(331,'Beto',1),(332,'Caceres',1),(333,'Cuartero',1),(334,'Felipe',1),(335,'Ramis',1);
/*!40000 ALTER TABLE `jugadoresmaestrofutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `juvenilesfutbol8`
--

DROP TABLE IF EXISTS `juvenilesfutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `juvenilesfutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `equipo` bigint(20) NOT NULL,
  `posicion` int(11) NOT NULL,
  `valoracion` int(11) NOT NULL,
  `jornadas` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `equipo_UNIQUE` (`equipo`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `juvenilesfutbol8`
--

LOCK TABLES `juvenilesfutbol8` WRITE;
/*!40000 ALTER TABLE `juvenilesfutbol8` DISABLE KEYS */;
/*!40000 ALTER TABLE `juvenilesfutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partidosfutbol8`
--

DROP TABLE IF EXISTS `partidosfutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `partidosfutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `eqlocal` bigint(20) DEFAULT NULL,
  `eqvisitante` bigint(20) DEFAULT NULL,
  `jornada` bigint(20) NOT NULL,
  `descripcion` varchar(200) DEFAULT '""',
  `goleslocal` int(11) DEFAULT NULL,
  `golesvisitante` int(11) DEFAULT NULL,
  `espectadores` int(11) DEFAULT NULL,
  `precioentradas` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=513 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partidosfutbol8`
--

LOCK TABLES `partidosfutbol8` WRITE;
/*!40000 ALTER TABLE `partidosfutbol8` DISABLE KEYS */;
/*!40000 ALTER TABLE `partidosfutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `porterosfutbol8`
--

DROP TABLE IF EXISTS `porterosfutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `porterosfutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `competicion` bigint(20) NOT NULL,
  `equipo` varchar(50) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `golesliga` int(11) NOT NULL,
  `golescopa` int(11) NOT NULL,
  `partidos` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=239 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `porterosfutbol8`
--

LOCK TABLES `porterosfutbol8` WRITE;
/*!40000 ALTER TABLE `porterosfutbol8` DISABLE KEYS */;
/*!40000 ALTER TABLE `porterosfutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `puntuacionesfutbol8`
--

DROP TABLE IF EXISTS `puntuacionesfutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `puntuacionesfutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `competicion` bigint(20) NOT NULL,
  `equipo` bigint(20) NOT NULL,
  `club` varchar(20) NOT NULL,
  `puntos` int(11) NOT NULL,
  `victorias` int(11) NOT NULL,
  `empates` int(11) NOT NULL,
  `derrotas` int(11) NOT NULL,
  `golesfavor` int(11) NOT NULL,
  `golescontra` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `puntuacionesfutbol8`
--

LOCK TABLES `puntuacionesfutbol8` WRITE;
/*!40000 ALTER TABLE `puntuacionesfutbol8` DISABLE KEYS */;
/*!40000 ALTER TABLE `puntuacionesfutbol8` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vacacionesfutbol8`
--

DROP TABLE IF EXISTS `vacacionesfutbol8`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vacacionesfutbol8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `equipo` bigint(20) NOT NULL,
  `activo` tinyint(1) NOT NULL,
  `renovacion` tinyint(1) NOT NULL,
  `tactica` tinyint(1) NOT NULL,
  `numerotactica` int(11) NOT NULL,
  `entreno` tinyint(1) NOT NULL,
  `posicionentreno` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vacacionesfutbol8`
--

LOCK TABLES `vacacionesfutbol8` WRITE;
/*!40000 ALTER TABLE `vacacionesfutbol8` DISABLE KEYS */;
/*!40000 ALTER TABLE `vacacionesfutbol8` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-09-06  8:34:25

