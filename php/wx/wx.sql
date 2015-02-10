/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50157
Source Host           : localhost:3306
Source Database       : wx

Target Server Type    : MYSQL
Target Server Version : 50157
File Encoding         : 65001

Date: 2014-06-27 16:48:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET gbk NOT NULL,
  `content` mediumtext CHARACTER SET gbk NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog
-- ----------------------------
INSERT INTO `blog` VALUES ('4', '得得得得得得得得得得得得', 'test 3');
INSERT INTO `blog` VALUES ('6', '踩踩踩踩踩踩踩踩踩', '我我我我我我我我，呵呵');
INSERT INTO `blog` VALUES ('16', '你额的鼠大王', 'WWW');
INSERT INTO `blog` VALUES ('17', '问问死定了卡斯加', '是点击发送见多了1111111');
INSERT INTO `blog` VALUES ('18', '1111111111111111111111', '22222222222222');
INSERT INTO `blog` VALUES ('19', '65', '565');
INSERT INTO `blog` VALUES ('20', '发士大夫', '范文芳');
INSERT INTO `blog` VALUES ('23', 'rttr', 'eeeewf');
INSERT INTO `blog` VALUES ('24', 'we', 'ww');
INSERT INTO `blog` VALUES ('25', 'rt', 'r');
INSERT INTO `blog` VALUES ('26', '鹅鹅鹅饿饿', '驱蚊器');
INSERT INTO `blog` VALUES ('27', '是我', '人人影视');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shop_id` int(10) unsigned NOT NULL,
  `name` varchar(20) NOT NULL,
  `category_id` int(10) unsigned DEFAULT '0',
  `tag` varchar(50) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `img_url` varchar(100) DEFAULT NULL,
  `price` decimal(10,0) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------

-- ----------------------------
-- Table structure for menu_category
-- ----------------------------
DROP TABLE IF EXISTS `menu_category`;
CREATE TABLE `menu_category` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shop_id` int(10) unsigned NOT NULL,
  `name` varchar(30) NOT NULL,
  `parent_id` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu_category
-- ----------------------------

-- ----------------------------
-- Table structure for shop
-- ----------------------------
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `tags` varchar(200) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `address` varchar(300) DEFAULT NULL,
  `logo_url` varchar(100) DEFAULT NULL,
  `telephone` varchar(100) DEFAULT NULL,
  `open_time` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shop
-- ----------------------------

-- ----------------------------
-- Table structure for shop_category
-- ----------------------------
DROP TABLE IF EXISTS `shop_category`;
CREATE TABLE `shop_category` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `parent_id` int(10) unsigned NOT NULL DEFAULT '0',
  `type` enum('default') NOT NULL DEFAULT 'default',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shop_category
-- ----------------------------

-- ----------------------------
-- Table structure for shop_category_relation
-- ----------------------------
DROP TABLE IF EXISTS `shop_category_relation`;
CREATE TABLE `shop_category_relation` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `shop_id` int(10) unsigned DEFAULT NULL,
  `category_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shop_category_relation
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `type` char(1) DEFAULT 'u' COMMENT '账号类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('5', 'admin', 'admin', 'a');
