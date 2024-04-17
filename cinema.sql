-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mer. 17 avr. 2024 à 10:13
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
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `commande`
--

INSERT INTO `commande` (`ID`, `ID_seance`, `ID_client`, `Nb_places`, `Paye`) VALUES
(7, 9, 3, 6, 1),
(8, 4, 3, 3, 1),
(6, 6, 3, 3, 1),
(10, 11, 3, 1, 1),
(11, 17, 3, 5, 1);

-- --------------------------------------------------------

--
-- Structure de la table `films`
--

DROP TABLE IF EXISTS `films`;
CREATE TABLE IF NOT EXISTS `films` (
  `Titre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Realisateur` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Synopsis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ID` int NOT NULL AUTO_INCREMENT,
  `Chemin_image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `films`
--

INSERT INTO `films` (`Titre`, `Realisateur`, `Synopsis`, `ID`, `Chemin_image`) VALUES
('Dune : Deuxième Partie', 'Denis Villeneuve', 'Dans DUNE : DEUXIÈME PARTIE, Paul Atreides s’unit à Chani et aux Fremen pour mener la révolte contre ceux qui ont anéanti sa famille. Hanté par de sombres prémonitions, il se trouve confronté au plus grand des dilemmes : choisir entre l’amour de sa vie et le destin de l’univers.', 0, 'DunePartieDeux.jpg'),
('Kung Fu Panda 4', 'Mike Mitchell (V)', 'Après trois aventures dans lesquelles le guerrier dragon Po a combattu les maîtres du mal les plus redoutables grâce à un courage et des compétences en arts martiaux inégalés, le destin va de nouveau frapper à sa porte pour être nommé chef spirituel de la vallée de la Paix. Il est question de l’apparition récente d’une sorcière aussi mal intentionnée que puissante, Caméléone, une lézarde minuscule qui peut se métamorphoser en n\'importe quelle créature, et ce sans distinction de taille.  Afin de réussir à protéger la Vallée de la Paix des griffes reptiliennes de Caméléone, ce drôle de duo va devoir trouver un terrain d’entente.', 1, 'KungFuPanda4.jpg'),
('Immaculée', 'Michael Mohan', 'Cecilia (Sydney Sweeney), une jeune religieuse américaine, s’installe dans un couvent isolé de la campagne italienne. L’accueil est chaleureux, mais rapidement Cecilia comprend que sa nouvelle demeure abrite un sinistre secret et que des choses terribles s’y produisent…', 2, 'Immaculee.jpg'),
('Une Vie', 'James Hawes', 'Prague, 1938. Alors que la ville est sur le point de tomber aux mains des nazis, un banquier londonien va tout mettre en œuvre pour sauver des centaines d’enfants promis à une mort certaine dans les camps de concentration. Au péril de sa vie, Nicholas Winton va organiser des convois vers l’Angleterre, où 669 enfants juifs trouveront refuge. Cette histoire vraie, restée méconnue pendant des décennies, est dévoilée au monde entier lorsqu’en 1988, une émission britannique invite Nicholas à témoigner. Celui-ci ne se doute pas que dans le public se trouvent les enfants – désormais adultes – qui ont survécu grâce à lui...', 3, 'UneVie.jpg'),
('Bob Marley : One Love', 'Reinaldo Marcus Green', 'BOB MARLEY : ONE LOVE célèbre la vie et la musique d’une icône qui a inspiré des générations à travers son message d’amour et d’unité. Pour la première fois sur grand écran, découvrez l’histoire puissante de Bob Marley, sa résilience face à l’adversité, le chemin qui l’a amené à sa musique révolutionnaire. Produit en partenariat avec la famille Marley et mettant en vedette Kingsley Ben-Adir dans le rôle du musicien légendaire et Lashana Lynch dans celui de sa femme Rita, BOB MARLEY : ONE LOVE sortira en salle en 2024.', 4, 'BobMarleyOneLove.jpg'),
('Imaginary', 'Jeff Wadlow', 'Lorsque Jessica (DeWanda Wise) retourne dans sa maison d’enfance avec sa famille, sa plus jeune belle-fille Alice (Pyper Braun) développe un attachement étrange pour un ours en peluche qu’elle a trouvé dans le sous-sol et nommé Chauncey. Tout commence par des jeux innocents, mais le comportement d’Alice devient de plus en plus inquiétant. Jessica comprend alors que Chauncey est bien plus qu\'un simple jouet…', 5, 'Imaginary.jpg');

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
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `membre`
--

INSERT INTO `membre` (`ID`, `Type`, `Nom`, `Prenom`, `Email`, `Mot_de_passe`) VALUES
(2, 2, 'Aloisi', 'Oscar', 'aloisi.oscar@gmail.com', 'Oscar2003'),
(1, 0, 'Gault', 'Alexandre', 'alexandre@gmail.com', 'Alexandre2003'),
(3, 1, 'a', 'a', '@', 'a'),
(4, 1, 'ALO', 'AA', 'aa@', 'a'),
(5, 1, 'RTYU', 'GHJ', '@a', 'a'),
(6, 0, 'a', 'a', '@aa', 'z');

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
(50, 20, 30);

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
) ENGINE=MyISAM AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `seance`
--

INSERT INTO `seance` (`ID`, `Date_diffusion`, `Prix`, `ID_film`) VALUES
(1, '2024-04-30 10:15:00', 15, 0),
(2, '2024-04-28 11:30:00', 17, 1),
(3, '2024-04-27 12:45:00', 20, 3),
(4, '2024-04-30 13:20:00', 22, 3),
(5, '2024-04-29 14:45:00', 25, 5),
(6, '2024-04-30 15:10:00', 27, 0),
(7, '2024-05-05 16:35:00', 15, 1),
(8, '2024-05-11 17:50:00', 17, 2),
(9, '2024-05-13 18:15:00', 20, 2),
(10, '2024-05-13 19:40:00', 22, 2),
(11, '2024-05-30 20:05:00', 25, 0),
(12, '2024-05-19 21:30:00', 27, 1),
(13, '2024-06-21 10:45:00', 15, 1),
(14, '2024-06-22 11:20:00', 17, 1),
(15, '2024-05-15 12:35:00', 20, 4),
(16, '2024-05-20 13:50:00', 22, 0),
(17, '2024-05-25 14:15:00', 25, 0),
(18, '2024-05-30 15:40:00', 27, 0),
(19, '2024-05-05 16:05:00', 15, 1),
(20, '2024-05-10 17:30:00', 17, 5),
(21, '2024-05-20 21:40:00', 18, 0);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
