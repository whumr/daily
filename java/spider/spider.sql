/*
Navicat MySQL Data Transfer

Source Server         : localhost_test
Source Server Version : 50612
Source Host           : localhost:3306
Source Database       : hualvtu

Target Server Type    : MYSQL
Target Server Version : 50612
File Encoding         : 65001

Date: 2015-01-09 10:10:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for album
-- ----------------------------
DROP TABLE IF EXISTS `album`;
CREATE TABLE `album` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `dest_id` int(11) NOT NULL,
  `poi_id` int(11) DEFAULT NULL,
  `name` varchar(500) DEFAULT NULL,
  `title_img` varchar(50) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `up_count` int(11) DEFAULT NULL,
  `view_count` int(11) DEFAULT NULL,
  `comment_count` int(11) DEFAULT NULL,
  `music_name` varchar(50) DEFAULT NULL,
  `music_url` varchar(200) DEFAULT NULL,
  `last_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `id` int(11) NOT NULL,
  `album_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `at_user_id` int(11) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `img_no` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for country
-- ----------------------------
DROP TABLE IF EXISTS `country`;
CREATE TABLE `country` (
  `id` int(11) NOT NULL,
  `name` varchar(60) NOT NULL,
  `land_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dest
-- ----------------------------
DROP TABLE IF EXISTS `dest`;
CREATE TABLE `dest` (
  `id` int(11) NOT NULL,
  `name` varchar(60) NOT NULL,
  `country_id` int(11) NOT NULL,
  `description` varchar(4000) DEFAULT NULL,
  `title_album_id` int(11) DEFAULT NULL,
  `last_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for imgs
-- ----------------------------
DROP TABLE IF EXISTS `imgs`;
CREATE TABLE `imgs` (
  `id` int(11) NOT NULL,
  `album_id` int(11) DEFAULT NULL,
  `zip_url` varchar(100) DEFAULT NULL,
  `real_url` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for land
-- ----------------------------
DROP TABLE IF EXISTS `land`;
CREATE TABLE `land` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for poi
-- ----------------------------
DROP TABLE IF EXISTS `poi`;
CREATE TABLE `poi` (
  `id` int(11) NOT NULL,
  `name` varchar(60) DEFAULT NULL,
  `dest_id` int(11) DEFAULT NULL,
  `description` varchar(4000) DEFAULT NULL,
  `last_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `account` varchar(30) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `weibo` varchar(30) DEFAULT NULL,
  `last_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
