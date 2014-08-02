/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `graygoose`
--

-- --------------------------------------------------------

--
-- Структура таблицы `Alert`
--

CREATE TABLE IF NOT EXISTS `Alert` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `maxAlertCountPerHour` int(11) NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `creationTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_Alert_name` (`name`),
  KEY `index_Alert_deleted` (`deleted`),
  KEY `index_Alert_creationTime` (`creationTime`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `AlertTriggerEvent`
--

CREATE TABLE IF NOT EXISTS `AlertTriggerEvent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `alertId` bigint(20) NOT NULL,
  `ruleCheckEventId` bigint(20) NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `creationTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_AlertTriggerEvent_deleted` (`deleted`),
  KEY `index_AlertTriggerEvent_creationTime` (`creationTime`),
  KEY `fk_AlertTriggerEvent_alertId` (`alertId`),
  KEY `fk_AlertTriggerEvent_ruleCheckEventId` (`ruleCheckEventId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `Response`
--

CREATE TABLE IF NOT EXISTS `Response` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `siteUrl` varchar(255) NOT NULL,
  `code` int(11) NOT NULL,
  `text` longtext NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `creationTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_Response_deleted` (`deleted`),
  KEY `index_Response_creationTime` (`creationTime`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `Rule`
--

CREATE TABLE IF NOT EXISTS `Rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `siteId` bigint(20) NOT NULL,
  `ruleType` enum('RESPONSE_CODE_RULE_TYPE','SUBSTRING_RULE_TYPE','REGEX_MATCH_RULE_TYPE','REGEX_NOT_MATCH_RULE_TYPE','REGEX_FIND_RULE_TYPE') NOT NULL,
  `dataJson` varchar(255) NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `creationTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_Rule_deleted` (`deleted`),
  KEY `index_Rule_creationTime` (`creationTime`),
  KEY `fk_Rule_siteId` (`siteId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `RuleAlertRelation`
--

CREATE TABLE IF NOT EXISTS `RuleAlertRelation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ruleId` bigint(20) NOT NULL,
  `alertId` bigint(20) NOT NULL,
  `maxConsecutiveFailCount` bigint(20) NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `creationTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_RuleAlertRelation_deleted` (`deleted`),
  KEY `index_RuleAlertRelation_creationTime` (`creationTime`),
  KEY `fk_RuleAlertRelation_ruleId` (`ruleId`),
  KEY `fk_RuleAlertRelation_alertId` (`alertId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `RuleCheckEvent`
--

CREATE TABLE IF NOT EXISTS `RuleCheckEvent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ruleId` bigint(20) NOT NULL,
  `siteId` bigint(20) NOT NULL,
  `responseId` bigint(20) DEFAULT NULL,
  `status` enum('PENDING','SUCCEEDED','FAILED') NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `checkTime` datetime DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL,
  `creationTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_RuleCheckEvent_status` (`status`),
  KEY `index_RuleCheckEvent_deleted` (`deleted`),
  KEY `index_RuleCheckEvent_creationTime` (`creationTime`),
  KEY `ruleId` (`ruleId`),
  KEY `siteId` (`siteId`),
  KEY `responseId` (`responseId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `Site`
--

CREATE TABLE IF NOT EXISTS `Site` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  `rescanPeriodSeconds` int(11) NOT NULL,
  `pauseFromMinute` bigint(20) DEFAULT NULL,
  `pauseToMinute` bigint(20) DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL,
  `creationTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_Site_deleted` (`deleted`),
  KEY `index_Site_creationTime` (`creationTime`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `passwordSha1` varchar(255) DEFAULT NULL COMMENT 'SHA1(password+''$e96ea31e54c52901'')',
  `deleted` tinyint(1) NOT NULL,
  `creationTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `index_User_deleted` (`deleted`),
  KEY `index_User_creationTime` (`creationTime`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `AlertTriggerEvent`
--
ALTER TABLE `AlertTriggerEvent`
  ADD CONSTRAINT `fk_AlertTriggerEvent_alertId` FOREIGN KEY (`alertId`) REFERENCES `Alert` (`id`),
  ADD CONSTRAINT `fk_AlertTriggerEvent_ruleCheckEventId` FOREIGN KEY (`ruleCheckEventId`) REFERENCES `RuleCheckEvent` (`id`);

--
-- Ограничения внешнего ключа таблицы `Rule`
--
ALTER TABLE `Rule`
  ADD CONSTRAINT `fk_Rule_siteId` FOREIGN KEY (`siteId`) REFERENCES `Site` (`id`);

--
-- Ограничения внешнего ключа таблицы `RuleAlertRelation`
--
ALTER TABLE `RuleAlertRelation`
  ADD CONSTRAINT `fk_RuleAlertRelation_ruleId` FOREIGN KEY (`ruleId`) REFERENCES `Rule` (`id`),
  ADD CONSTRAINT `fk_RuleAlertRelation_alertId` FOREIGN KEY (`alertId`) REFERENCES `Alert` (`id`);

--
-- Ограничения внешнего ключа таблицы `RuleCheckEvent`
--
ALTER TABLE `RuleCheckEvent`
  ADD CONSTRAINT `fk_RuleCheckEvent_responseId` FOREIGN KEY (`responseId`) REFERENCES `Response` (`id`),
  ADD CONSTRAINT `fk_RuleCheckEvent_ruleId` FOREIGN KEY (`ruleId`) REFERENCES `Rule` (`id`),
  ADD CONSTRAINT `fk_RuleCheckEvent_siteId` FOREIGN KEY (`siteId`) REFERENCES `Site` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
