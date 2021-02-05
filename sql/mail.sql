/*
 Navicat Premium Data Transfer

 Source Server         : Localhost
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : mail

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 05/02/2021 17:07:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account_property
-- ----------------------------
DROP TABLE IF EXISTS `account_property`;
CREATE TABLE `account_property`  (
  `pk_AccountProperty` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表主键',
  `pk_MailAccount` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联表mail_account的主键',
  `protocol` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件收发协议',
  `host` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务器IP地址',
  `port` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '端口号',
  `auth` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否需要认证',
  `isSSL` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否使用SSL',
  PRIMARY KEY (`pk_AccountProperty`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for attachment
-- ----------------------------
DROP TABLE IF EXISTS `attachment`;
CREATE TABLE `attachment`  (
  `pk_attachment` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表主键',
  `pk_MailEntity` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联表main_entity的主键',
  `contentID` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件被引用的ID',
  `relativePath` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件的相对路径',
  `filename` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件名称',
  `contentDisposition` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主要为两种取值，inline和attachment',
  `contentType` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件内容说明，例如：text/html;charset=utf-8',
  `size` bigint(20) NULL DEFAULT NULL COMMENT '附件大小',
  PRIMARY KEY (`pk_attachment`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for deleted_mail_base_info
-- ----------------------------
DROP TABLE IF EXISTS `deleted_mail_base_info`;
CREATE TABLE `deleted_mail_base_info`  (
  `pk_MailBaseInfo` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表主键',
  `pk_MailEntity` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联表mail_entity的主键',
  `from` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发件人',
  `to` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人',
  `subject` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件主题',
  `sentDate` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件发送日期',
  `owner` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件所属邮箱的邮件账号',
  `msgUID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件的msgUID',
  PRIMARY KEY (`pk_MailBaseInfo`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for draft_mail_base_info
-- ----------------------------
DROP TABLE IF EXISTS `draft_mail_base_info`;
CREATE TABLE `draft_mail_base_info`  (
  `pk_MailBaseInfo` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表主键',
  `pk_MailEntity` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联表mail_entity的主键',
  `from` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发件人',
  `to` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人',
  `subject` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件主题',
  `sentDate` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件发送日期',
  `owner` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件所属邮箱的邮件账号',
  `msgUID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件的msgUID',
  `flag` int(255) NULL DEFAULT NULL COMMENT '邮件标记等级',
  PRIMARY KEY (`pk_MailBaseInfo`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for inbox_mail_base_info
-- ----------------------------
DROP TABLE IF EXISTS `inbox_mail_base_info`;
CREATE TABLE `inbox_mail_base_info`  (
  `pk_MailBaseInfo` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表主键',
  `pk_MailEntity` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联表mail_entity的主键',
  `from` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发件人',
  `to` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人',
  `subject` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件主题',
  `sentDate` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件发送日期',
  `owner` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件所属邮箱的邮件账号',
  `msgUID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件的msgUID',
  `flag` int(255) NULL DEFAULT NULL COMMENT '邮件标记等级',
  `receivedDate` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件接收时的时间',
  `readFlag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件是否已读标记',
  PRIMARY KEY (`pk_MailBaseInfo`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for junk_mail_base_info
-- ----------------------------
DROP TABLE IF EXISTS `junk_mail_base_info`;
CREATE TABLE `junk_mail_base_info`  (
  `pk_MailBaseInfo` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表主键',
  `pk_MailEntity` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联表mail_entity的主键',
  `from` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发件人',
  `to` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人',
  `subject` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件主题',
  `sentDate` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件发送日期',
  `owner` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件所属邮箱的邮件账号',
  `msgUID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件的msgUID',
  `flag` int(255) NULL DEFAULT NULL COMMENT '邮件标记等级',
  PRIMARY KEY (`pk_MailBaseInfo`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mail_account
-- ----------------------------
DROP TABLE IF EXISTS `mail_account`;
CREATE TABLE `mail_account`  (
  `pk_MailAccount` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表主键',
  `pk_UserID` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联的user表中的用户ID',
  `account` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱账号',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱密码',
  PRIMARY KEY (`pk_MailAccount`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mail_entity
-- ----------------------------
DROP TABLE IF EXISTS `mail_entity`;
CREATE TABLE `mail_entity`  (
  `pk_MailEntity` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '关联表mail_entity的主键',
  `from` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发件人',
  `to` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人',
  `subject` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件主题',
  `sentDate` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件发送日期',
  `owner` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件所属邮箱的邮件账号',
  `msgUID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件的msgUID',
  `cc` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件抄送对象',
  `bcc` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件暗送对象',
  `receivedDate` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件接收日期',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '邮件文本内容',
  `contentType` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件内容说明',
  `attachFlag` int(255) NULL DEFAULT NULL COMMENT '是否含有附件',
  PRIMARY KEY (`pk_MailEntity`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sent_mail_base_info
-- ----------------------------
DROP TABLE IF EXISTS `sent_mail_base_info`;
CREATE TABLE `sent_mail_base_info`  (
  `pk_MailBaseInfo` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表主键',
  `pk_MailEntity` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联表mail_entity的主键',
  `from` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发件人',
  `to` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人',
  `subject` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件主题',
  `sentDate` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件发送日期',
  `owner` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件所属邮箱的邮件账号',
  `msgUID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件的msgUID',
  `flag` int(255) NULL DEFAULT NULL COMMENT '邮件标记等级',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件发送状态',
  PRIMARY KEY (`pk_MailBaseInfo`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `pk_UserID` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表主键，也即用户的ID',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `nickname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `registeredTime` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户注册时的时间',
  `bandingMailAccount` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户账号绑定的邮箱账号',
  PRIMARY KEY (`pk_UserID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
