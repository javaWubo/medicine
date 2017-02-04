/*
Navicat MySQL Data Transfer

Source Server         : SelfDB
Source Server Version : 50527
Source Host           : localhost:3306
Source Database       : medicine

Target Server Type    : MYSQL
Target Server Version : 50527
File Encoding         : 65001

Date: 2016-12-23 14:03:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `medicine`
-- ----------------------------
DROP TABLE IF EXISTS `medicine`;
CREATE TABLE `medicine` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '药物ID',
  `medicine_name` varchar(64) DEFAULT NULL COMMENT '药物名称',
  `produced_time` timestamp NULL DEFAULT NULL COMMENT '生产日期',
  `expiration_num` int(5) DEFAULT NULL COMMENT '过期数量',
  `expiration_time` timestamp NULL DEFAULT NULL COMMENT '失效时间',
  `status` int(2) DEFAULT NULL COMMENT '状态',
  `surplus_num` int(5) DEFAULT NULL COMMENT '剩余数量',
  `sale_num` int(10) DEFAULT NULL COMMENT '销售数量',
  `price` double DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of medicine
-- ----------------------------
INSERT INTO `medicine` VALUES ('1', '阿莫', '2016-12-22 11:30:51', '2', '2016-12-22 11:30:51', '1', '20', '100', '200', '2016-12-22 11:30:51', '2016-12-22 11:30:51');
