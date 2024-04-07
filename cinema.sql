-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mer. 03 avr. 2024 à 11:40
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
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
('Dune : Deuxième Partie', 'Denis Villeneuve', 'Dans DUNE : DEUXIÈME PARTIE, Paul Atreides s’unit à Chani et aux Fremen pour mener la révolte contre ceux qui ont anéanti sa famille. Hanté par de sombres prémonitions, il se trouve confronté au plus grand des dilemmes : choisir entre l’amour de sa vie et le destin de l’univers.', 0, 'Dune2.jpg'),
('Kung Fu Panda 4', 'Mike Mitchell (V), Stephanie Stine', 'Après trois aventures dans lesquelles le guerrier dragon Po a combattu les maîtres du mal les plus redoutables grâce à un courage et des compétences en arts martiaux inégalés, le destin va de nouveau frapper à sa porte pour être nommé chef spirituel de la vallée de la Paix. Il est question de l’apparition récente d’une sorcière aussi mal intentionnée que puissante, Caméléone, une lézarde minuscule qui peut se métamorphoser en n\'importe quelle créature, et ce sans distinction de taille.  Afin de réussir à protéger la Vallée de la Paix des griffes reptiliennes de Caméléone, ce drôle de duo va devoir trouver un terrain d’entente.', 1, 'KungFuPanda4.jpg'),
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
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `membre`
--

INSERT INTO `membre` (`ID`, `Type`, `Nom`, `Prenom`, `Email`, `Mot_de_passe`) VALUES
(2, 2, 'Aloisi', 'Oscar', 'aloisi.oscar@gmail.com', 'Oscar2003'),
(1, 0, 'Gault', 'Alexandre', 'alexandre@gmail.com', 'Alexandre2003');

-- --------------------------------------------------------

--
-- Structure de la table `seance`
--

DROP TABLE IF EXISTS `seance`;
CREATE TABLE IF NOT EXISTS `seance` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Date_diffusion` date NOT NULL,
  `Prix` int NOT NULL,
  `ID_film` int NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
