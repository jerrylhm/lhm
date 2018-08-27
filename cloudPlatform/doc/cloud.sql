/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : cloud

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2018-06-19 16:13:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tb_classroom`
-- ----------------------------
DROP TABLE IF EXISTS `tb_classroom`;
CREATE TABLE `tb_classroom` (
  `cr_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '教室id',
  `cr_name` varchar(255) NOT NULL COMMENT '教室名称',
  `cr_address` varchar(255) DEFAULT NULL COMMENT '教室地址',
  `cr_orgid` int(11) DEFAULT NULL COMMENT '学校id',
  `cr_ttid` int(11) DEFAULT NULL COMMENT '课表id',
  `cr_camera` varchar(255) DEFAULT NULL COMMENT '摄像头流地址',
  PRIMARY KEY (`cr_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_classroom
-- ----------------------------
INSERT INTO `tb_classroom` VALUES ('1', '教室3', '地址3', '3', '4', '192.168.1.222');

-- ----------------------------
-- Table structure for `tb_node`
-- ----------------------------
DROP TABLE IF EXISTS `tb_node`;
CREATE TABLE `tb_node` (
  `node_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '课节ID',
  `node_index` int(11) unsigned DEFAULT NULL COMMENT '第几节',
  `node_start` varchar(255) NOT NULL COMMENT '本节课开始时间',
  `node_end` varchar(255) NOT NULL COMMENT '本节课结束时间',
  `node_ttid` int(11) unsigned NOT NULL COMMENT '课表ID',
  PRIMARY KEY (`node_id`),
  KEY `index_ttid` (`node_ttid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_node
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_organization`
-- ----------------------------
DROP TABLE IF EXISTS `tb_organization`;
CREATE TABLE `tb_organization` (
  `org_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '组织id',
  `org_name` varchar(255) NOT NULL COMMENT '组织名称',
  `org_pid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '父组织id',
  `org_type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '组织等级（0：学校，1：学院，2：专业，3：年级，4：班级）',
  `org_flag` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '学校类型（0：大学，1：普教）',
  PRIMARY KEY (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_organization
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_permission`
-- ----------------------------
DROP TABLE IF EXISTS `tb_permission`;
CREATE TABLE `tb_permission` (
  `ps_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `ps_name` varchar(255) DEFAULT NULL COMMENT '权限名称',
  PRIMARY KEY (`ps_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_permission
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_studenttable`
-- ----------------------------
DROP TABLE IF EXISTS `tb_studenttable`;
CREATE TABLE `tb_studenttable` (
  `st_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '学生课表id',
  `st_week` tinyint(4) DEFAULT NULL COMMENT '第几周',
  `st_index` tinyint(4) NOT NULL COMMENT '第几节课',
  `st_name` varchar(255) NOT NULL COMMENT '课程名称',
  `st_orgid` varchar(255) DEFAULT NULL,
  `st_teacher` varchar(255) DEFAULT NULL COMMENT '任课教室',
  `st_day` tinyint(4) DEFAULT NULL COMMENT '星期几',
  `st_num` int(11) DEFAULT NULL COMMENT '上课人数',
  `st_group` int(11) DEFAULT NULL COMMENT '课程分组',
  PRIMARY KEY (`st_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_studenttable
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_subject`
-- ----------------------------
DROP TABLE IF EXISTS `tb_subject`;
CREATE TABLE `tb_subject` (
  `sub_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '科目ID',
  `sub_name` varchar(255) DEFAULT NULL COMMENT '科目名',
  `sub_orgid` int(11) DEFAULT NULL COMMENT '组织id',
  PRIMARY KEY (`sub_id`),
  KEY `index_orgid` (`sub_orgid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_subject
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_timetable`
-- ----------------------------
DROP TABLE IF EXISTS `tb_timetable`;
CREATE TABLE `tb_timetable` (
  `tt_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '课表id',
  `tt_name` varchar(255) NOT NULL COMMENT '课表名称',
  `tt_num` tinyint(3) unsigned NOT NULL COMMENT '课节数',
  `tt_urid` int(11) unsigned DEFAULT NULL COMMENT '创建者id',
  `tt_createdate` varchar(255) DEFAULT NULL COMMENT '建表日期',
  `tt_firstweek` varchar(255) DEFAULT NULL COMMENT '第一周的日期',
  PRIMARY KEY (`tt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_timetable
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_user`
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `ur_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `ur_username` varchar(40) NOT NULL COMMENT '用户名',
  `ur_password` varchar(100) NOT NULL COMMENT '密码',
  `ur_image` varchar(100) DEFAULT NULL COMMENT '头像',
  `ur_email` varchar(60) DEFAULT NULL COMMENT '邮箱',
  `ur_nickname` varchar(60) DEFAULT NULL COMMENT '昵称',
  `ur_classid` int(11) unsigned NOT NULL COMMENT '所属班级id',
  `ur_createdate` varchar(20) NOT NULL COMMENT '注册日期',
  `ur_sex` tinyint(4) NOT NULL COMMENT '性别（0：男，1：女）',
  `ur_phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `ur_type` tinyint(4) DEFAULT '3' COMMENT '用户类型（1：管理员，2：教师，3：学生）',
  `ur_status` tinyint(4) DEFAULT '0' COMMENT '审核状态（0：未通过，1：通过）',
  PRIMARY KEY (`ur_id`),
  KEY `index_classid` (`ur_classid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_usergroup`
-- ----------------------------
DROP TABLE IF EXISTS `tb_usergroup`;
CREATE TABLE `tb_usergroup` (
  `ug_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户组id',
  `ug_name` varchar(255) NOT NULL COMMENT '用户组名称',
  `ug_permissionid` varchar(255) DEFAULT NULL COMMENT '用户组权限',
  PRIMARY KEY (`ug_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_usergroup
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_user_usergroup`
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_usergroup`;
CREATE TABLE `tb_user_usergroup` (
  `uug_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户-用户组id',
  `uug_urid` int(11) unsigned NOT NULL COMMENT '用户id',
  `uug_ugid` int(11) unsigned NOT NULL COMMENT '用户组id',
  PRIMARY KEY (`uug_id`),
  KEY `index_urid` (`uug_urid`) USING BTREE,
  KEY `index_ugid` (`uug_ugid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user_usergroup
-- ----------------------------
