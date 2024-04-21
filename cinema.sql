-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : dim. 21 avr. 2024 à 12:16
-- Version du serveur : 8.0.31
-- Version de PHP : 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `cinema`
--

-- --------------------------------------------------------

--
-- Structure de la table `commande`
--

DROP TABLE IF EXISTS `commande`;
CREATE TABLE IF NOT EXISTS `commande` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ID_seance` int NOT NULL,
  `ID_client` int NOT NULL,
  `Nb_places` int NOT NULL,
  `Paye` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `commande`
--

INSERT INTO `commande` (`ID`, `ID_seance`, `ID_client`, `Nb_places`, `Paye`) VALUES
(7, 9, 3, 6, 1),
(8, 4, 3, 3, 1),
(6, 6, 3, 3, 1),
(10, 11, 3, 1, 1),
(11, 17, 3, 5, 1),
(12, 17, 3, 2, 1),
(21, 22, 3, 5, 1),
(22, 13, 3, 4, 1),
(24, 10, 3, 1, 1),
(29, 14, 2, 1, 1),
(30, 51, 2, 6, 1),
(31, 15, 2, 4, 1),
(32, 31, 2, 10, 1),
(33, 39, 9, 3, 1),
(34, 2, 9, 7, 1),
(35, 22, 9, 3, 1),
(36, 9, 9, 3, 1),
(37, 33, 9, 2, 0),
(38, 34, 8, 6, 1),
(39, 31, 8, 2, 1),
(40, 22, 8, 3, 1),
(41, 37, 1, 3, 1),
(42, 34, 1, 8, 1),
(43, 36, 1, 3, 1),
(44, 17, 1, 3, 0),
(45, 19, 1, 3, 0);

-- --------------------------------------------------------

--
-- Structure de la table `films`
--

DROP TABLE IF EXISTS `films`;
CREATE TABLE IF NOT EXISTS `films` (
  `Titre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Realisateur` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ID` int NOT NULL AUTO_INCREMENT,
  `Chemin_image` varchar(255) DEFAULT NULL,
  `Synopsis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `films`
--

INSERT INTO `films` (`Titre`, `Realisateur`, `ID`, `Chemin_image`, `Synopsis`) VALUES
('Dune : Deuxième Partie', 'Denis Villeneuve', 0, 'DunePartieDeux.jpg', 'Paul Atréides, seul survivant avec sa mère de la Maison Atréides, se joint au peuple autochtone et réprouvé d\'Arrakis, les Fremen, tout en fomentant sa revanche contre ceux qui ont assassiné son père et détruit sa famille. Alors qu\'il doit faire un choix entre son amour pour la Fremen Chani et l\'accomplissement de sa vengeance, il essaiera de tout faire pour reconquérir le pouvoir sans déclencher une abominable guerre sainte que ses visions du futur lui révèlent.'),
('Kung Fu Panda 4', 'Mike Mitchell (V)', 1, 'KungFuPanda4.jpg', 'Après trois aventures dans lesquelles le guerrier dragon Po a combattu les maîtres du mal les plus redoutables grâce à un courage et des compétences en arts martiaux inégalés le destin va de nouveau frapper à sa porte pour l\'inviter à enfin se reposer. Plus précisément pour être nommé chef spirituel de la vallée de la Paix.'),
('Immaculée', 'Michael Mohan', 2, 'Immaculee.jpg', 'Cecilia une jeune femme de foi américaine est chaleureusement accueillie dans un illustre couvent isolé de la campagne italienne où elle se voit offrir un nouveau rôle. L\'accueil est chaleureux mais Cecilia comprend rapidement que sa nouvelle demeure abrite un sinistre secret et que des choses terribles s\'y produisent.'),
('Une Vie', 'James Hawes', 3, 'UneVie.jpg', 'À Prague en 1938 alors que la ville est sur le point de tomber aux mains des nazis un banquier londonien va tout mettre en oeuvre pour sauver des centaines d\'enfants promis à une mort certaine dans les camps de concentration. Au péril de sa vie Nicholas Winton va organiser des convois vers l\'Angleterre où 669 enfants juifs trouveront refuge.'),
('Bob Marley : One Love', 'Reinaldo Marcus Green', 4, 'BobMarleyOneLove.jpg', 'L\'auteur-compositeur-interprète jamaïcain Bob Marley fait face à l\'adversité pour devenir le musicien de reggae le plus célèbre au monde. Le chemin qu\' il a emprunté l\'a amené à réaliser une musique révolutionnaire.'),
('Imaginary', 'Jeff Wadlow', 5, 'Imaginary.jpg', 'Lorsque Jessica retourne dans sa maison d\'enfance avec sa famille sa plus jeune belle-fille Alice développe un attachement étrange pour un ours en peluche qu\'elle a trouvé dans le sous-sol et nommé Chauncey. Tout commence par des jeux innocents mais le comportement d\'Alice devient de plus en plus inquiétant. Jessica comprend alors que Chauncey est bien plus qu\'un simple jouet.');

-- --------------------------------------------------------

--
-- Structure de la table `membre`
--

DROP TABLE IF EXISTS `membre`;
CREATE TABLE IF NOT EXISTS `membre` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Type` int NOT NULL,
  `Nom` varchar(255) NOT NULL,
  `Prenom` varchar(255) NOT NULL,
  `Email` varchar(255) NOT NULL,
  `Mot_de_passe` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `membre`
--

INSERT INTO `membre` (`ID`, `Type`, `Nom`, `Prenom`, `Email`, `Mot_de_passe`) VALUES
(2, 2, 'Aloisi', 'Oscar', 'aloisi.oscar@gmail.com', 'Oscar2003'),
(1, 2, 'Gault', 'Alexandre', 'alexandre@gmail.com', 'Alexandre2003'),
(3, 2, 'Clément', 'Cachet', 'clement@gmail.com', 'clement2003'),
(4, 0, 'Vincent', 'Baré', 'vincent@gmail.com', 'Baré2003'),
(8, 2, 'Gabriel', 'De Olival', 'gabriel@gmail.com', 'Gab20'),
(6, 0, 'a', 'a', '@aa', 'z'),
(9, 3, 'Durand', 'Tom', 'tom@gmail.com', 'tom24');

-- --------------------------------------------------------

--
-- Structure de la table `reduction`
--

DROP TABLE IF EXISTS `reduction`;
CREATE TABLE IF NOT EXISTS `reduction` (
  `enfant` int NOT NULL,
  `regulier` int NOT NULL,
  `senior` int NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `reduction`
--

INSERT INTO `reduction` (`enfant`, `regulier`, `senior`) VALUES
(60, 20, 30);

-- --------------------------------------------------------

--
-- Structure de la table `seance`
--

DROP TABLE IF EXISTS `seance`;
CREATE TABLE IF NOT EXISTS `seance` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Date_diffusion` datetime NOT NULL,
  `Prix` float NOT NULL,
  `ID_film` int NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `seance`
--

INSERT INTO `seance` (`ID`, `Date_diffusion`, `Prix`, `ID_film`) VALUES
(1, '2024-05-25 11:16:00', 15, 0),
(2, '2024-04-28 11:30:00', 17, 1),
(3, '2024-04-27 12:45:00', 20, 3),
(4, '2024-04-30 13:20:00', 22, 3),
(5, '2024-04-29 14:45:00', 25, 5),
(6, '2024-04-25 15:10:00', 26.9, 0),
(7, '2024-05-05 16:35:00', 15, 1),
(8, '2024-05-11 17:50:00', 17, 2),
(9, '2024-05-13 18:15:00', 20, 2),
(10, '2024-05-13 19:40:00', 22, 2),
(11, '2024-04-25 16:30:00', 24.9, 0),
(12, '2024-05-05 21:30:00', 27, 1),
(13, '2024-06-21 10:45:00', 15, 1),
(14, '2024-06-21 11:20:00', 17, 1),
(15, '2024-05-15 12:35:00', 20, 4),
(16, '2024-05-20 13:50:00', 22, 0),
(17, '2024-05-25 14:15:00', 25.2, 0),
(22, '2024-05-30 12:15:00', 21.5, 0),
(19, '2024-05-05 16:05:00', 15, 1),
(20, '2024-05-10 17:30:00', 17, 5),
(21, '2024-05-25 21:40:00', 18, 0),
(23, '2024-05-29 16:35:00', 17.4, 2),
(24, '2024-01-04 00:00:00', 0, 6),
(31, '2024-05-06 12:00:00', 17.2, 3),
(32, '2024-05-14 17:10:00', 25, 3),
(33, '2024-05-06 20:55:00', 20.5, 3),
(34, '2024-04-30 19:45:00', 16.5, 3),
(35, '2024-05-29 17:10:00', 18.5, 5),
(36, '2024-05-29 16:15:00', 22.1, 5),
(37, '2024-05-29 19:55:00', 20, 2),
(38, '2024-05-15 20:00:00', 18.3, 4),
(39, '2024-05-27 20:10:00', 19, 4),
(40, '2024-05-28 19:25:00', 17.5, 4),
(41, '2024-05-14 11:10:00', 18.8, 4),
(42, '2024-05-15 13:20:00', 19.2, 1),
(43, '2024-05-15 18:45:00', 20, 1),
(44, '2024-05-06 21:00:00', 16, 2),
(45, '2024-05-06 12:10:00', 18.3, 2),
(46, '2024-05-15 10:00:00', 18, 2),
(47, '2024-04-30 19:40:00', 19.5, 3),
(48, '2024-05-29 22:20:00', 16.2, 4),
(49, '2024-05-15 10:30:00', 18.8, 4),
(50, '2024-05-27 13:15:00', 20, 4),
(51, '2024-05-28 21:10:00', 17.1, 5),
(52, '2024-05-28 19:14:00', 17.3, 5),
(53, '2024-05-10 16:00:00', 22, 5);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
