/*
Navicat MySQL Data Transfer
​
Source Server         : ローカルVM
Source Server Version : 50626
Source Host           : localhost:3306
Source Database       : dbcareco
​
Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001
​
Date: 2015-10-06 20:05:48
*/
​
SET FOREIGN_KEY_CHECKS=0;
​
-- ----------------------------
-- Table structure for `mytest`
-- ----------------------------
DROP TABLE IF EXISTS `mytest`;
CREATE TABLE `mytest` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
​
-- ----------------------------
-- Records of mytest
-- ----------------------------
INSERT INTO `mytest` VALUES ('1', 'hoge', '20');
INSERT INTO `mytest` VALUES ('2', 'fuga', '30');
