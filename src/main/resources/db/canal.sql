-- MySQL dump 10.13  Distrib 8.0.46, for Linux (x86_64)
--
-- Host: localhost    Database: db
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `canal`
--

LOCK TABLES `canal` WRITE;
/*!40000 ALTER TABLE `canal` DISABLE KEYS */;
INSERT INTO `canal` (`id`, `descricao`, `logotipo_url`, `nome`, `site`, `status_desc`, `uuid`) VALUES (1,'Canal de televisão aberta japonesa focado em transmissões de anime e programas de entretenimento. Sediado em Tóquio, é uma das principais emissoras para séries animadas no Japão.',NULL,'Tokyo MX',NULL,'Active','a8a7da53-853c-4840-b94f-a4ffbc6bbf1e'),(2,'Rede de televisão japonesa líder na produção e exibição de animes. Responsável por lançar icônicas séries como Naruto, Bleach e Pokémon. Sediada em Osaka.',NULL,'TV Tokyo',NULL,'Active','2e97cdf8-4e59-40e8-bac1-e8a0da761a7d'),(3,'Canal de televisão por assinatura dedicado exclusivamente à programação de anime. Transmite séries 24 horas por dia, abrangendo todos os gêneros do estilo.',NULL,'Animax',NULL,'Active','35613042-e13a-44fc-b659-61d9855e9b12'),(4,'Canal de televisão por assinatura de propriedade da Warner Bros. Discovery, focado em desenhos animados e programas infantis. Transmitido internacionalmente.',NULL,'Cartoon Network',NULL,'Active','9733be7d-2c57-46b8-bbce-3017d8319703'),(5,'Canal de televisão por assinatura voltado para o público infantojuvenil, com uma mistura de desenhos animados, séries de ação e programas de aventura.',NULL,'Disney XD',NULL,'Active','c1f2d669-17be-48af-8d2d-6d5c41bbf9e6'),(6,'Principal rede de televisão aberta do Brasil. Transmite telenovelas, jornalismo, esportes e programas de entretenimento. Uma das maiores emissoras da América Latina.',NULL,'Rede Globo',NULL,'Active','2e0a27a3-e533-4b51-bbb8-285a5c15241b'),(7,'Rede de televisão aberta brasileira conhecida por exibir dublagens de animes e desenhos animados clássicos, além de programas de auditório e novelas.',NULL,'SBT',NULL,'Active','36556f22-9679-4890-af0c-a870977787ba'),(8,'Rede de televisão aberta brasileira com programação variada, incluindo jornalismo, esportes, novelas e transmissão de eventos ao vivo.',NULL,'Band',NULL,'Active','b9b08077-7137-4c60-8276-70dddda594d1'),(9,'Rede de televisão aberta brasileira com programação focada em novelas, jornalismo e entretenimento. Possui parcerias com emissoras internacionais.',NULL,'Record',NULL,'Active','14c511fb-cce3-4ec2-949a-462492915ba3'),(10,'Canal de televisão por assinatura brasileiro voltado para o público infantil, com desenhos animados e séries educativas e de entretenimento.',NULL,'Gloob',NULL,'Active','b96625dd-1276-4ca3-bc3b-70f57e767c47');
/*!40000 ALTER TABLE `canal` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-18 10:59:13
