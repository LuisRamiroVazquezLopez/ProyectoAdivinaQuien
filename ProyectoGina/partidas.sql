-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 23-06-2025 a las 01:31:52
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `adivina_quien`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `partidas`
--

CREATE TABLE `partidas` (
  `id` int(11) NOT NULL,
  `jugador1` varchar(50) NOT NULL,
  `jugador2` varchar(50) NOT NULL,
  `ganador` varchar(50) NOT NULL,
  `personaje ganador` varchar(50) NOT NULL,
  `fecha` datetime NOT NULL,
  `duracion` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `partidas`
--

INSERT INTO `partidas` (`id`, `jugador1`, `jugador2`, `ganador`, `personaje ganador`, `fecha`, `duracion`) VALUES
(1, 'luis', 'juan', 'luis', 'Zidane', '2025-06-22 14:56:16', 15),
(2, 'luis', 'juan', 'luis', 'NeymarJR', '2025-06-22 15:05:47', 18),
(3, 'luis', 'juan', 'luis', 'Abandono', '2025-06-22 15:09:05', 216),
(4, 'juan', 'luis', 'juan', 'Pele', '2025-06-22 15:12:50', 17),
(5, 'jose', 'juan', 'jose', 'Jorge Campos', '2025-06-22 15:14:46', 72),
(6, 'juan', 'santiago', 'juan', 'Pele', '2025-06-22 15:15:43', 31),
(7, 'jose', 'juan', 'juan', 'Abandono', '2025-06-22 15:15:56', 142),
(8, 'juan', 'santiago', 'santiago', 'Abandono', '2025-06-22 15:15:56', 44),
(9, 'juan', 'luis', 'luis', 'Abandono', '2025-06-22 15:15:56', 203);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `partidas`
--
ALTER TABLE `partidas`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `partidas`
--
ALTER TABLE `partidas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
