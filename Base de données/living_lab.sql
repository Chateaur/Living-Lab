-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Client :  127.0.0.1
-- Généré le :  Mer 25 Janvier 2017 à 08:46
-- Version du serveur :  5.7.14
-- Version de PHP :  5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `living_lab`
--

-- --------------------------------------------------------

--
-- Structure de la table `debimetre_1`
--

CREATE TABLE `debimetre_1` (
  `id` int(11) NOT NULL,
  `date_time` datetime NOT NULL,
  `valeur` float NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Contenu de la table `debimetre_1`
--

INSERT INTO `debimetre_1` (`id`, `date_time`, `valeur`) VALUES
(1, '2017-01-25 09:35:04', 1),
(2, '2017-01-25 09:35:05', 0),
(3, '2017-01-25 09:35:06', 0),
(4, '2017-01-25 09:35:07', 0),
(5, '2017-01-25 09:35:08', 0),
(6, '2017-01-25 09:35:09', 0),
(7, '2017-01-25 09:35:10', 0),
(8, '2017-01-25 09:35:11', 0),
(9, '2017-01-25 09:35:12', 0),
(10, '2017-01-25 09:35:13', 0),
(11, '2017-01-25 09:35:14', 0),
(12, '2017-01-25 09:35:15', 0),
(13, '2017-01-25 09:35:16', 0),
(14, '2017-01-25 09:36:47', 0),
(15, '2017-01-25 09:36:53', 1),
(16, '2017-01-25 09:36:54', 0),
(17, '2017-01-25 09:36:55', 0),
(18, '2017-01-25 09:36:56', 0),
(19, '2017-01-25 09:36:57', 0),
(20, '2017-01-25 09:36:58', 0),
(21, '2017-01-25 09:36:59', 0),
(22, '2017-01-25 09:37:00', 0),
(23, '2017-01-25 09:37:01', 0),
(24, '2017-01-25 09:37:02', 0),
(25, '2017-01-25 09:38:41', 0),
(26, '2017-01-25 09:38:42', 0),
(27, '2017-01-25 09:38:41', 0),
(28, '2017-01-25 09:38:42', 0),
(29, '2017-01-25 09:38:48', 1),
(30, '2017-01-25 09:38:49', 0),
(31, '2017-01-25 09:38:50', 0),
(32, '2017-01-25 09:38:51', 0),
(33, '2017-01-25 09:38:52', 0),
(34, '2017-01-25 09:38:53', 0),
(35, '2017-01-25 09:38:54', 0),
(36, '2017-01-25 09:38:55', 0),
(37, '2017-01-25 09:38:56', 0),
(38, '2017-01-25 09:38:57', 0),
(39, '2017-01-25 09:38:58', 0),
(40, '2017-01-25 09:38:59', 0),
(41, '2017-01-25 09:39:00', 0),
(42, '2017-01-25 09:39:01', 0),
(43, '2017-01-25 09:39:02', 0),
(44, '2017-01-25 09:39:03', 0);

-- --------------------------------------------------------

--
-- Structure de la table `debimetre_2`
--

CREATE TABLE `debimetre_2` (
  `id` int(11) NOT NULL,
  `date_time` datetime NOT NULL,
  `valeur` float NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Contenu de la table `debimetre_2`
--

INSERT INTO `debimetre_2` (`id`, `date_time`, `valeur`) VALUES
(1, '2017-01-25 09:38:41', 0),
(2, '2017-01-25 09:38:42', 0),
(3, '2017-01-25 09:38:41', 0),
(4, '2017-01-25 09:38:42', 0),
(5, '2017-01-25 09:38:48', 0),
(6, '2017-01-25 09:38:49', 0),
(7, '2017-01-25 09:38:50', 40),
(8, '2017-01-25 09:38:51', 190),
(9, '2017-01-25 09:38:52', 228),
(10, '2017-01-25 09:38:53', 125),
(11, '2017-01-25 09:38:54', 52),
(12, '2017-01-25 09:38:55', 128),
(13, '2017-01-25 09:38:56', 0),
(14, '2017-01-25 09:38:57', 0),
(15, '2017-01-25 09:38:58', 102),
(16, '2017-01-25 09:38:59', 178),
(17, '2017-01-25 09:39:00', 71),
(18, '2017-01-25 09:39:01', 0),
(19, '2017-01-25 09:39:02', 0),
(20, '2017-01-25 09:39:03', 0);

--
-- Index pour les tables exportées
--

--
-- Index pour la table `debimetre_1`
--
ALTER TABLE `debimetre_1`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `debimetre_2`
--
ALTER TABLE `debimetre_2`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `debimetre_1`
--
ALTER TABLE `debimetre_1`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=45;
--
-- AUTO_INCREMENT pour la table `debimetre_2`
--
ALTER TABLE `debimetre_2`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
