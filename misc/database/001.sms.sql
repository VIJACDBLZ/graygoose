ALTER TABLE `Alert` CHANGE `email` `email` varchar(255) NULL DEFAULT NULL;
ALTER TABLE `Alert` ADD `smsServiceUrl` varchar(255) DEFAULT NULL AFTER `password`;
ALTER TABLE `Alert` ADD `smsServicePhoneParameterName` varchar(255) DEFAULT NULL AFTER `smsServiceUrl`;
ALTER TABLE `Alert` ADD `smsServicePhone` varchar(255) DEFAULT NULL AFTER `smsServicePhoneParameterName`;
ALTER TABLE `Alert` ADD `smsServiceMessageParameterName` varchar(255) DEFAULT NULL AFTER `smsServicePhone`;