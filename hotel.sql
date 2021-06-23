/*
Navicat MySQL Data Transfer

Source Server         : Ali_Hotel
Source Server Version : 50726
Source Host           : jiangwei.online:3306
Source Database       : hotel

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2020-05-29 13:13:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for consume_records
-- ----------------------------
DROP TABLE IF EXISTS `consume_records`;
CREATE TABLE `consume_records` (
  `id` int(60) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `id_number` varchar(18) DEFAULT NULL COMMENT '身份证',
  `item_name` varchar(255) DEFAULT NULL COMMENT '项目名称',
  `item_money` int(40) DEFAULT NULL COMMENT '项目金额',
  `description` varchar(255) DEFAULT NULL COMMENT '项目金额',
  `is_pay` char(1) DEFAULT NULL COMMENT '是否支付',
  `create_at` timestamp NULL DEFAULT NULL COMMENT '订单时间',
  `update_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id_number` (`id_number`) USING BTREE,
  CONSTRAINT `id_number` FOREIGN KEY (`id_number`) REFERENCES `users` (`id_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `iphone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `num` varchar(255) DEFAULT NULL,
  `role` varchar(30) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `create_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for hotel_imgs
-- ----------------------------
DROP TABLE IF EXISTS `hotel_imgs`;
CREATE TABLE `hotel_imgs` (
  `id` int(60) NOT NULL AUTO_INCREMENT,
  `house_type` int(60) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL COMMENT '照片存储路径',
  `create_at` timestamp NULL DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `house_type` (`house_type`) USING BTREE,
  CONSTRAINT `house_type_key` FOREIGN KEY (`house_type`) REFERENCES `houses` (`type`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for house_orders
-- ----------------------------
DROP TABLE IF EXISTS `house_orders`;
CREATE TABLE `house_orders` (
  `id` int(60) NOT NULL AUTO_INCREMENT,
  `id_number` varchar(18) DEFAULT NULL,
  `house_type` int(60) DEFAULT NULL COMMENT '房间id',
  `check_in_time` timestamp NULL DEFAULT NULL COMMENT '入住时间',
  `check_out_time` timestamp NULL DEFAULT NULL COMMENT '退房时间',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `status` int(10) DEFAULT NULL COMMENT '订单状态\r\n0 已过期\r\n1 预定失败\r\n2 已预定\r\n3 已使用\r\n4 已取消\r\n',
  `create_at` timestamp NULL DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL,
  `house_Id` int(11) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  `room_number` int(60) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id_number` (`id_number`) USING BTREE,
  KEY `house_id` (`house_type`) USING BTREE,
  KEY `house_orders_ibfk_3` (`house_Id`) USING BTREE,
  KEY `openid` (`open_id`) USING BTREE,
  KEY `house_orders_roomnumber` (`room_number`),
  CONSTRAINT `house_orders_ibfk_3` FOREIGN KEY (`house_Id`) REFERENCES `houses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `house_orders_ibfk_4` FOREIGN KEY (`id_number`) REFERENCES `users` (`id_number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `house_orders_roomnumber` FOREIGN KEY (`room_number`) REFERENCES `houses` (`room_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for houses
-- ----------------------------
DROP TABLE IF EXISTS `houses`;
CREATE TABLE `houses` (
  `id` int(60) NOT NULL AUTO_INCREMENT,
  `room_number` int(60) DEFAULT NULL COMMENT '房间号',
  `name` varchar(255) DEFAULT NULL COMMENT '房间名称',
  `type` int(10) DEFAULT NULL COMMENT ' * 0 酒店环境\r\n * 1 大床房\r\n * 2 双人房\r\n * 3 套房\r\n * 4 主题房\r\n * 5 游泳馆\r\n * 6 会议室\r\n * 7 KTV\r\n * 8 棋牌室\r\n * 9 健身房',
  `money` int(40) DEFAULT NULL COMMENT '金额',
  `area` int(40) DEFAULT NULL COMMENT '面积',
  `status` varchar(255) DEFAULT NULL COMMENT '房间状态\r\n0 打扫\r\n1 未使用\r\n2 已使用\r\n3 已预定',
  `description` varchar(255) DEFAULT NULL COMMENT '房间描述',
  `create_at` timestamp NULL DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `type` (`type`) USING BTREE,
  KEY `room_number` (`room_number`),
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for id_cards
-- ----------------------------
DROP TABLE IF EXISTS `id_cards`;
CREATE TABLE `id_cards` (
  `id` int(60) NOT NULL AUTO_INCREMENT,
  `id_number` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `front` varchar(255) DEFAULT NULL COMMENT '身份证正面路径',
  `back` varchar(255) DEFAULT NULL COMMENT '身份证反面路径',
  `recent` varchar(255) DEFAULT NULL COMMENT '最近照片',
  `create_at` timestamp NULL DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id_number` (`id_number`) USING BTREE,
  KEY `recent` (`recent`) USING BTREE,
  KEY `id` (`id`) USING BTREE,
  CONSTRAINT `id_cards_ibfk_1` FOREIGN KEY (`id_number`) REFERENCES `users` (`id_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(60) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `money` int(40) DEFAULT NULL COMMENT '金额',
  `type` int(10) DEFAULT NULL COMMENT '类型|套餐|\r\n0 西餐 \r\n1 甜点\r\n2 特色菜\r\n3 北方菜\r\n4 南方菜',
  `create_at` timestamp NULL DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `name` (`name`) USING BTREE,
  KEY `tpe` (`type`) USING BTREE,
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for restaurant_orders
-- ----------------------------
DROP TABLE IF EXISTS `restaurant_orders`;
CREATE TABLE `restaurant_orders` (
  `id` int(60) NOT NULL AUTO_INCREMENT,
  `is_delivery` char(1) DEFAULT NULL COMMENT '是否配送',
  `id_number` varchar(18) DEFAULT NULL COMMENT '身份证号',
  `items` text COMMENT '订单内容',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `check_in_time` timestamp NULL DEFAULT NULL COMMENT '预计吃饭时间',
  `status` int(10) DEFAULT NULL COMMENT '订单状态\r\n0已过期\r\n1 预定失败\r\n2 已预定\r\n3 已使用\r\n4 已取消\r\n',
  `create_at` timestamp NULL DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id_number` (`id_number`) USING BTREE,
  KEY `id` (`id`) USING BTREE,
  CONSTRAINT `restaurant_orders_ibfk_1` FOREIGN KEY (`id_number`) REFERENCES `users` (`id_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(60) NOT NULL AUTO_INCREMENT,
  `real_name` varchar(255) DEFAULT NULL COMMENT '身份证姓名',
  `id_number` varchar(18) DEFAULT NULL COMMENT '身份证id',
  `open_id` varchar(255) DEFAULT NULL COMMENT 'openid',
  `create_at` timestamp NULL DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id_number` (`id_number`) USING BTREE,
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
