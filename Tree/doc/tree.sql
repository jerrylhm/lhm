/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50530
Source Host           : localhost:3306
Source Database       : tree

Target Server Type    : MYSQL
Target Server Version : 50530
File Encoding         : 65001

Date: 2018-03-30 14:41:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tb_clientpermission`
-- ----------------------------
DROP TABLE IF EXISTS `tb_clientpermission`;
CREATE TABLE `tb_clientpermission` (
  `per_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `per_name` varchar(100) DEFAULT NULL COMMENT '权限名',
  `per_type` int(11) DEFAULT '0' COMMENT '1、树节点相关权限    2、管理相关权限',
  PRIMARY KEY (`per_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_clientpermission
-- ----------------------------
INSERT INTO `tb_clientpermission` VALUES ('1', '控制', '1');
INSERT INTO `tb_clientpermission` VALUES ('2', '查询', '1');
INSERT INTO `tb_clientpermission` VALUES ('3', '浏览', '1');
INSERT INTO `tb_clientpermission` VALUES ('4', '资产管理', '2');
INSERT INTO `tb_clientpermission` VALUES ('5', '用户组管理', '2');
INSERT INTO `tb_clientpermission` VALUES ('6', '权限管理', '2');
INSERT INTO `tb_clientpermission` VALUES ('7', '场景管理', '2');
INSERT INTO `tb_clientpermission` VALUES ('8', '用户审核', '2');
INSERT INTO `tb_clientpermission` VALUES ('9', '模板管理', '2');

-- ----------------------------
-- Table structure for `tb_conferee`
-- ----------------------------
DROP TABLE IF EXISTS `tb_conferee`;
CREATE TABLE `tb_conferee` (
  `con_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '参议员主键',
  `con_meid` int(11) DEFAULT NULL COMMENT '会议id',
  `con_userid` int(11) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`con_id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_conferee
-- ----------------------------
INSERT INTO `tb_conferee` VALUES ('1', '1', '3');
INSERT INTO `tb_conferee` VALUES ('2', '1', '2');
INSERT INTO `tb_conferee` VALUES ('3', '1', '5');
INSERT INTO `tb_conferee` VALUES ('4', '2', '1');
INSERT INTO `tb_conferee` VALUES ('5', '2', '2');
INSERT INTO `tb_conferee` VALUES ('6', '2', '3');
INSERT INTO `tb_conferee` VALUES ('7', '2', '4');
INSERT INTO `tb_conferee` VALUES ('8', '2', '5');
INSERT INTO `tb_conferee` VALUES ('9', '3', '4');
INSERT INTO `tb_conferee` VALUES ('10', '4', '4');
INSERT INTO `tb_conferee` VALUES ('11', '5', '3');
INSERT INTO `tb_conferee` VALUES ('12', '5', '5');
INSERT INTO `tb_conferee` VALUES ('13', '6', '3');
INSERT INTO `tb_conferee` VALUES ('14', '6', '5');
INSERT INTO `tb_conferee` VALUES ('15', '7', '3');
INSERT INTO `tb_conferee` VALUES ('16', '7', '5');
INSERT INTO `tb_conferee` VALUES ('17', '8', '3');
INSERT INTO `tb_conferee` VALUES ('18', '8', '5');
INSERT INTO `tb_conferee` VALUES ('19', '9', '3');
INSERT INTO `tb_conferee` VALUES ('20', '9', '5');
INSERT INTO `tb_conferee` VALUES ('21', '10', '1');
INSERT INTO `tb_conferee` VALUES ('22', '10', '4');
INSERT INTO `tb_conferee` VALUES ('23', '11', '4');
INSERT INTO `tb_conferee` VALUES ('24', '11', '3');
INSERT INTO `tb_conferee` VALUES ('25', '12', '2');
INSERT INTO `tb_conferee` VALUES ('26', '13', '1');
INSERT INTO `tb_conferee` VALUES ('27', '13', '3');
INSERT INTO `tb_conferee` VALUES ('28', '14', '4');
INSERT INTO `tb_conferee` VALUES ('29', '15', '3');
INSERT INTO `tb_conferee` VALUES ('30', '16', '4');
INSERT INTO `tb_conferee` VALUES ('31', '16', '3');
INSERT INTO `tb_conferee` VALUES ('32', '17', '2');
INSERT INTO `tb_conferee` VALUES ('33', '18', '3');
INSERT INTO `tb_conferee` VALUES ('34', '19', '4');
INSERT INTO `tb_conferee` VALUES ('35', '19', '3');
INSERT INTO `tb_conferee` VALUES ('36', '20', '2');
INSERT INTO `tb_conferee` VALUES ('37', '20', '1');
INSERT INTO `tb_conferee` VALUES ('38', '20', '3');
INSERT INTO `tb_conferee` VALUES ('39', '20', '4');
INSERT INTO `tb_conferee` VALUES ('40', '20', '5');
INSERT INTO `tb_conferee` VALUES ('41', '21', '1');
INSERT INTO `tb_conferee` VALUES ('42', '21', '2');
INSERT INTO `tb_conferee` VALUES ('43', '21', '3');
INSERT INTO `tb_conferee` VALUES ('44', '21', '4');
INSERT INTO `tb_conferee` VALUES ('45', '21', '5');
INSERT INTO `tb_conferee` VALUES ('46', '22', '1');
INSERT INTO `tb_conferee` VALUES ('47', '22', '2');
INSERT INTO `tb_conferee` VALUES ('48', '22', '3');
INSERT INTO `tb_conferee` VALUES ('49', '22', '4');
INSERT INTO `tb_conferee` VALUES ('50', '22', '5');
INSERT INTO `tb_conferee` VALUES ('51', '23', '4');
INSERT INTO `tb_conferee` VALUES ('52', '24', '4');

-- ----------------------------
-- Table structure for `tb_group_permission`
-- ----------------------------
DROP TABLE IF EXISTS `tb_group_permission`;
CREATE TABLE `tb_group_permission` (
  `gp_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id(主键)',
  `gp_usergroupid` int(11) DEFAULT NULL COMMENT '用户组id',
  `gp_treeid` int(11) DEFAULT NULL COMMENT '树id',
  `gp_nodeid` int(11) DEFAULT NULL COMMENT '节点ID',
  `gp_perid` varchar(3000) DEFAULT '0' COMMENT '权限ID',
  PRIMARY KEY (`gp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_group_permission
-- ----------------------------
INSERT INTO `tb_group_permission` VALUES ('8', '1', '189', '189', '1,2,3,4,5,6');

-- ----------------------------
-- Table structure for `tb_meeting`
-- ----------------------------
DROP TABLE IF EXISTS `tb_meeting`;
CREATE TABLE `tb_meeting` (
  `me_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '会议id',
  `me_userid` int(11) DEFAULT NULL COMMENT '创建该会议的用户id',
  `me_nodeid` int(11) DEFAULT NULL COMMENT '该会议属于的节点id',
  `me_title` varchar(255) DEFAULT NULL COMMENT '会议名称',
  `me_number` int(11) DEFAULT NULL COMMENT '参加会议的人数',
  `me_starttime` varchar(255) DEFAULT NULL COMMENT '会议开始时间',
  `me_endtime` varchar(255) DEFAULT NULL COMMENT '会议结束时间',
  `me_description` text COMMENT '会议说明',
  `me_status` int(11) DEFAULT NULL COMMENT '会议状态[0:未审核;0:已审核]',
  PRIMARY KEY (`me_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_meeting
-- ----------------------------
INSERT INTO `tb_meeting` VALUES ('1', '2', '230', '会议', '123', '2018-03-26 00:04:00', '2018-03-26 05:00:00', 'dsfsfsdfsd', '0');
INSERT INTO `tb_meeting` VALUES ('2', '2', '230', '会议2', '123123', '2018-03-26 06:00:00', '2018-03-26 21:00:00', 'sdfsfs', '0');
INSERT INTO `tb_meeting` VALUES ('3', '2', '230', 'dsfsd', '123', '2018-03-27 04:00:00', '2018-03-27 05:00:00', 'sdfs', '0');
INSERT INTO `tb_meeting` VALUES ('4', '2', '230', 'dsfsd', '123', '2018-03-27 14:00:00', '2018-03-27 22:00:00', 'sdfs', '0');
INSERT INTO `tb_meeting` VALUES ('5', '2', '230', '狐疑03-25', '10', '2018-03-25 15:03:00', '2018-03-25 21:00:00', 'dsf', '0');
INSERT INTO `tb_meeting` VALUES ('6', '2', '230', '狐疑03-25', '10', '2018-03-26 05:03:00', '2018-03-26 05:32:00', 'dsf', '0');
INSERT INTO `tb_meeting` VALUES ('7', '2', '230', '狐疑03-25', '10', '2018-03-27 05:03:00', '2018-03-27 05:32:00', 'dsf', '0');
INSERT INTO `tb_meeting` VALUES ('8', '2', '230', '狐疑03-25', '10', '2018-03-27 07:03:00', '2018-03-27 09:32:00', 'dsf', '0');
INSERT INTO `tb_meeting` VALUES ('9', '2', '230', '狐疑03-25', '10', '2018-03-27 10:03:02', '2018-03-27 10:32:00', 'dsf', '0');
INSERT INTO `tb_meeting` VALUES ('10', '2', '230', '会议名称长度溢出测试会议名称长度溢出测试会议名称长度溢出测试会议名称长度溢出测试会议名称长度溢出测试', '100', '2018-03-27 23:00:00', '2018-03-27 23:03:00', '', '0');
INSERT INTO `tb_meeting` VALUES ('11', '2', '230', '会议1221', '124', '2018-03-27 23:29:00', '2018-03-27 23:57:05', '', '0');
INSERT INTO `tb_meeting` VALUES ('12', '2', '230', 'sdfsd', '234', '2018-03-27 06:00:00', '2018-03-27 07:00:00', '', '0');
INSERT INTO `tb_meeting` VALUES ('13', '2', '230', '会议0328', '12', '2018-03-28 04:00:00', '2018-03-28 09:00:00', '', '0');
INSERT INTO `tb_meeting` VALUES ('14', '2', '230', '会议2555', '10', '2018-03-28 03:00:00', '2018-03-28 03:38:00', '', '0');
INSERT INTO `tb_meeting` VALUES ('15', '2', '230', '1231', '123', '2018-03-28 03:39:00', '2018-03-28 03:57:00', '', '0');
INSERT INTO `tb_meeting` VALUES ('16', '2', '230', '会议5161', '12', '2018-03-28 09:03:00', '2018-03-28 10:00:00', '', '0');
INSERT INTO `tb_meeting` VALUES ('17', '2', '230', '5465456', '12', '2018-03-28 21:00:00', '2018-03-28 21:01:00', '', '0');
INSERT INTO `tb_meeting` VALUES ('18', '2', '230', '你好', '12', '2018-03-28 19:00:00', '2018-03-28 19:03:00', '', '0');
INSERT INTO `tb_meeting` VALUES ('19', '2', '230', '111', '12', '2018-03-29 04:00:00', '2018-03-29 05:00:00', '', '0');
INSERT INTO `tb_meeting` VALUES ('20', '2', '230', '测试会议0329', '123', '2018-03-29 06:02:00', '2018-03-29 07:05:00', '第三方水电费地方大丰收士大夫阿斯蒂芬暗室逢灯发送端', '0');
INSERT INTO `tb_meeting` VALUES ('21', '2', '230', '杰哥十大优点会议', '1300000000', '2018-03-29 08:00:00', '2018-03-29 23:59:59', '', '0');
INSERT INTO `tb_meeting` VALUES ('22', '2', '230', '杰哥十大优点会议第二场', '1300000000', '2018-03-30 01:00:00', '2018-03-30 23:59:59', '样衰，口臭，', '0');
INSERT INTO `tb_meeting` VALUES ('23', '2', '230', 'sfsd', '2', '2018-03-31 00:00:00', '2018-03-31 01:00:00', '', '0');
INSERT INTO `tb_meeting` VALUES ('24', '2', '230', 'sfsd', '2', '2018-03-30 00:00:00', '2018-03-30 00:05:00', '', '0');

-- ----------------------------
-- Table structure for `tb_node_attr`
-- ----------------------------
DROP TABLE IF EXISTS `tb_node_attr`;
CREATE TABLE `tb_node_attr` (
  `attr_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '属性id',
  `attr_nodeid` int(11) NOT NULL DEFAULT '0' COMMENT '节点id',
  `attr_type` int(11) NOT NULL DEFAULT '1' COMMENT '属性的类型(1、节点对应的模板id   2、节点对应的图片或视频url)',
  `attr_value` varchar(255) DEFAULT NULL COMMENT '节点对应的属性值',
  PRIMARY KEY (`attr_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_node_attr
-- ----------------------------
INSERT INTO `tb_node_attr` VALUES ('17', '168', '1', '17');
INSERT INTO `tb_node_attr` VALUES ('20', '174', '1', '21');

-- ----------------------------
-- Table structure for `tb_permission`
-- ----------------------------
DROP TABLE IF EXISTS `tb_permission`;
CREATE TABLE `tb_permission` (
  `urnode_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户权限表ID',
  `urnode_userid` int(11) NOT NULL COMMENT '用户ID',
  `urnode_nodeid` varchar(800) DEFAULT NULL COMMENT '树节点ID',
  `urnode_iscreator` int(11) DEFAULT '0' COMMENT '是否创建者（1是，其他不是）',
  `urnode_treeid` int(11) NOT NULL COMMENT '树组id',
  PRIMARY KEY (`urnode_id`),
  KEY `index_treeid` (`urnode_treeid`) USING BTREE,
  KEY `index_userid` (`urnode_userid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_permission
-- ----------------------------
INSERT INTO `tb_permission` VALUES ('74', '1', '149,150,151,152,153,154,155,156,160,161,162,163,167,221,222', '1', '149');
INSERT INTO `tb_permission` VALUES ('75', '1', '168,169,170,171,172,173,174,175,177,178,200,201,202,203,231,232', '1', '168');
INSERT INTO `tb_permission` VALUES ('83', '1', '234,235,236,237,238,239,240', '1', '234');

-- ----------------------------
-- Table structure for `tb_scene`
-- ----------------------------
DROP TABLE IF EXISTS `tb_scene`;
CREATE TABLE `tb_scene` (
  `sc_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '场景id',
  `sc_name` varchar(50) DEFAULT NULL COMMENT '场景名称',
  `sc_treeid` int(11) DEFAULT NULL COMMENT '场景所属树id',
  `sc_action` varchar(255) DEFAULT NULL COMMENT '场景动作',
  `sc_nodeid` varchar(255) DEFAULT NULL COMMENT '场景所属节点id',
  PRIMARY KEY (`sc_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_scene
-- ----------------------------
INSERT INTO `tb_scene` VALUES ('2', ' 场景2', '168', '175', '89,171');
INSERT INTO `tb_scene` VALUES ('4', '101课室场景', '189', '202,203,204,205', '196');

-- ----------------------------
-- Table structure for `tb_template`
-- ----------------------------
DROP TABLE IF EXISTS `tb_template`;
CREATE TABLE `tb_template` (
  `tp_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tp_name` varchar(40) DEFAULT NULL COMMENT '模板名称',
  `tp_data` text COMMENT '模板信息的json',
  `tp_treeid` int(11) DEFAULT NULL COMMENT '所属树ID',
  PRIMARY KEY (`tp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_template
-- ----------------------------
INSERT INTO `tb_template` VALUES ('17', '11', '[{\"name\":\"11\",\"type\":\"text\"},{\"name\":\"file\",\"type\":\"file\"}]', null);

-- ----------------------------
-- Table structure for `tb_tpcontent`
-- ----------------------------
DROP TABLE IF EXISTS `tb_tpcontent`;
CREATE TABLE `tb_tpcontent` (
  `tpc_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '模板内容id',
  `tpc_tpid` int(11) unsigned NOT NULL COMMENT '模板id',
  `tpc_nodeid` int(11) unsigned NOT NULL COMMENT '节点id',
  `tpc_content` text COMMENT '模板内容',
  `tpc_userid` int(11) unsigned NOT NULL COMMENT '填写模板的客户的id',
  `tpc_date` varchar(20) NOT NULL COMMENT '填写模板的日期',
  PRIMARY KEY (`tpc_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_tpcontent
-- ----------------------------
INSERT INTO `tb_tpcontent` VALUES ('2', '17', '11', '[{\"name\":\"设备名称\",\"type\":\"text\",\"content\":\"\"}]', '33', '2017');
INSERT INTO `tb_tpcontent` VALUES ('3', '17', '168', '{\"key\":\"1\"}', '1', '2018-03-26');

-- ----------------------------
-- Table structure for `tb_tree`
-- ----------------------------
DROP TABLE IF EXISTS `tb_tree`;
CREATE TABLE `tb_tree` (
  `node_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '节点ID',
  `node_name` varchar(255) DEFAULT NULL COMMENT '节点名称',
  `node_pid` int(11) DEFAULT NULL COMMENT '节点父ID',
  `node_userid` int(11) NOT NULL COMMENT '树或节点创建人ID',
  `node_treeid` int(11) NOT NULL COMMENT '树ID（根节点ID）',
  `node_url` varchar(500) DEFAULT NULL COMMENT '跳转链接',
  `node_type` int(11) DEFAULT '1' COMMENT '节点类型',
  `node_protocol` text COMMENT '协议',
  `node_class` varchar(255) DEFAULT NULL COMMENT '用户自定义类（属性）',
  `node_sn` varchar(255) DEFAULT NULL COMMENT '设备的SN号',
  `node_tstype` int(11) NOT NULL DEFAULT '0' COMMENT '数据传输类型  0、协议传输   1、透传',
  PRIMARY KEY (`node_id`),
  KEY `index_treeid` (`node_treeid`) USING BTREE,
  KEY `index_pid` (`node_pid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=232 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_tree
-- ----------------------------
INSERT INTO `tb_tree` VALUES ('149', '广州', '0', '1', '149', '', '1', '[]', null, null, '0');
INSERT INTO `tb_tree` VALUES ('150', 'creator快捷', '149', '1', '149', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('151', '3楼', '150', '1', '149', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('152', '空调1', '151', '1', '149', '', '2', '[{\"identification\":\"time\",\"name\":\"时间\",\"nodeId\":152,\"remark\":\"消息发送时间\"},{\"identification\":\"state\",\"name\":\"空调状态\",\"nodeId\":152,\"remark\":\"空调的运行状态\"}]', '空调', 'AZA1002', '0');
INSERT INTO `tb_tree` VALUES ('153', '电量', '152', '1', '149', '', '3', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('154', '空调开关', '152', '1', '149', '', '11', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('160', '电灯', '151', '1', '149', '', '2', null, null, '100', '0');
INSERT INTO `tb_tree` VALUES ('161', '2楼', '150', '1', '149', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('162', '电脑1', '161', '1', '149', '', '2', null, '电脑', 'AS2011', '0');
INSERT INTO `tb_tree` VALUES ('163', '合同', '161', '1', '149', '', '4', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('167', '投影仪1', '161', '1', '149', '', '2', null, '投影仪', 'ACX5214', '0');
INSERT INTO `tb_tree` VALUES ('168', '广州中小学', '0', '1', '168', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('169', '广州一小', '168', '1', '168', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('170', '广州二小', '168', '1', '168', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('171', '教学一区', '169', '1', '168', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('172', '3楼', '171', '1', '168', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('173', '浏览室', '172', '1', '168', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('174', '电脑一', '173', '1', '168', '', '2', null, '电脑', 'AWR1254', '0');
INSERT INTO `tb_tree` VALUES ('175', '开机', '174', '1', '168', '', '11', '[{\"identification\":\"code\",\"name\":\"设备操作代码\",\"nodeId\":175,\"remark\":\"操作电脑开机的代码\"},{\"identification\":\"name\",\"name\":\"操作名称\",\"nodeId\":175,\"remark\":\"该操作的名称\"}]', null, null, '0');
INSERT INTO `tb_tree` VALUES ('177', '重启', '174', '1', '168', '', '11', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('178', '电量', '174', '1', '168', '', '3', '[{\"identification\":\"value\",\"name\":\"名称\",\"nodeId\":178,\"remark\":\"dsadasdasdsa\"},{\"identification\":\"xxxxxx\",\"name\":\"2312\",\"nodeId\":178,\"remark\":\"cccc\"}]', null, null, '0');
INSERT INTO `tb_tree` VALUES ('188', '是大大', '170', '1', '168', '', '2', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('189', '广州市', '0', '4', '189', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('190', '广州东校', '189', '4', '189', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('191', '广州西校', '189', '4', '189', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('193', '广州北校', '189', '4', '189', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('194', 'A栋教学楼', '190', '4', '189', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('195', '一楼', '194', '4', '189', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('196', '101课室', '195', '4', '189', '', '3', null, '课室', null, '0');
INSERT INTO `tb_tree` VALUES ('197', '102课室', '195', '4', '189', '', '3', null, '课室', null, '0');
INSERT INTO `tb_tree` VALUES ('198', '电脑', '196', '4', '189', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('199', '电脑一', '198', '4', '189', '', '2', null, '电脑', '001122334455PCC44614C5CFB1D4EDF3', '0');
INSERT INTO `tb_tree` VALUES ('200', '电脑二', '198', '4', '189', '', '2', null, '电脑', '001122334455PCC44614C5CFB1D4EDF3', '0');
INSERT INTO `tb_tree` VALUES ('201', '电脑三', '198', '4', '189', '', '2', null, '电脑', '03', '0');
INSERT INTO `tb_tree` VALUES ('202', '开', '199', '4', '189', '', '11', null, null, '', '0');
INSERT INTO `tb_tree` VALUES ('203', '关', '199', '4', '189', '', '11', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('204', '强制关', '199', '4', '189', '', '11', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('205', '重启', '199', '4', '189', '', '11', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('206', '101班', '199', '4', '189', '', '5', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('207', '空调', '196', '4', '189', '', '2', null, '空调', '11', '0');
INSERT INTO `tb_tree` VALUES ('208', '灯光', '196', '4', '189', '', '2', null, '灯', '21', '0');
INSERT INTO `tb_tree` VALUES ('209', '门禁', '196', '4', '189', '', '2', null, '门', '31', '0');
INSERT INTO `tb_tree` VALUES ('210', '投影仪', '196', '4', '189', '', '2', null, '投影', '41', '0');
INSERT INTO `tb_tree` VALUES ('211', '投影幕', '196', '4', '189', '', '2', null, '投影', '51', '0');
INSERT INTO `tb_tree` VALUES ('212', '传感器', '196', '4', '189', '', '2', null, '传感器', '61', '0');
INSERT INTO `tb_tree` VALUES ('213', '摄像头', '196', '4', '189', '', '2', null, '摄像头', '71', '0');
INSERT INTO `tb_tree` VALUES ('214', '音量控制', '196', '4', '189', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('215', '视频源切换', '196', '4', '189', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('222', '场景', '196', '4', '189', '', '1', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('223', '上课场景', '222', '4', '189', '', '11', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('224', '下课场景', '222', '4', '189', '', '11', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('225', '电影场景', '222', '4', '189', '', '11', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('226', '互动场景', '222', '4', '189', '', '11', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('227', '会议场景', '222', '4', '189', '', '11', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('228', '娱乐场景', '222', '4', '189', '', '11', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('229', '影像', '213', '4', '189', '', '5', null, null, null, '0');
INSERT INTO `tb_tree` VALUES ('230', '会议', '161', '1', '149', null, '12', null, '会议', null, '0');
INSERT INTO `tb_tree` VALUES ('231', '温度', '174', '1', '168', null, '11', null, null, null, '1');

-- ----------------------------
-- Table structure for `tb_type`
-- ----------------------------
DROP TABLE IF EXISTS `tb_type`;
CREATE TABLE `tb_type` (
  `type_id` int(11) NOT NULL COMMENT 'id',
  `type_name` varchar(25) DEFAULT NULL COMMENT '类型的英文名字',
  `type_name_cn` varchar(50) DEFAULT NULL COMMENT '类型中文名称',
  `type_pid` int(11) DEFAULT NULL COMMENT '父类型id',
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_type
-- ----------------------------
INSERT INTO `tb_type` VALUES ('1', 'normal', '普通节点', '0');
INSERT INTO `tb_type` VALUES ('2', 'equipment', '设备', '0');
INSERT INTO `tb_type` VALUES ('3', 'text', '文本', '0');
INSERT INTO `tb_type` VALUES ('4', 'image', '图片', '0');
INSERT INTO `tb_type` VALUES ('5', 'video', '视频', '0');
INSERT INTO `tb_type` VALUES ('6', 'enum', '枚举', '0');
INSERT INTO `tb_type` VALUES ('7', 'enumaction', '枚举动作', '6');
INSERT INTO `tb_type` VALUES ('8', 'table', '表格', '0');
INSERT INTO `tb_type` VALUES ('9', 'chart', '图表', '0');
INSERT INTO `tb_type` VALUES ('10', 'range', '滑块', '0');
INSERT INTO `tb_type` VALUES ('11', 'action', '动作', '0');
INSERT INTO `tb_type` VALUES ('12', 'meeting', '会议', '0');

-- ----------------------------
-- Table structure for `tb_user`
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `ur_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `ur_username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `ur_password` varchar(255) NOT NULL COMMENT '用户密码',
  `ur_type` int(11) DEFAULT NULL COMMENT '身份类型（1管理员，其他值代表普通用户）',
  `ur_datetime` varchar(255) DEFAULT NULL COMMENT '注册日期',
  `ur_group` int(11) DEFAULT '0' COMMENT '用户组id',
  PRIMARY KEY (`ur_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('1', 'admin', '4QrcOUm6Wau+VuBX8g+IPg==', '1', '2018-1-23', '0');
INSERT INTO `tb_user` VALUES ('2', 'user1', '4QrcOUm6Wau+VuBX8g+IPg==', '0', '2018-1-23', '1');
INSERT INTO `tb_user` VALUES ('3', 'user2', '4QrcOUm6Wau+VuBX8g+IPg==', '0', '2018-1-23', '0');
INSERT INTO `tb_user` VALUES ('4', 'ZhuXiaoHui', '4QrcOUm6Wau+VuBX8g+IPg==', '0', '2018-3-23', '0');
INSERT INTO `tb_user` VALUES ('5', 'admin12345', '4QrcOUm6Wau+VuBX8g+IPg==', '0', '2018-03-26', '0');

-- ----------------------------
-- Table structure for `tb_usergroup`
-- ----------------------------
DROP TABLE IF EXISTS `tb_usergroup`;
CREATE TABLE `tb_usergroup` (
  `urg_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户组ID',
  `urg_name` varchar(60) DEFAULT NULL COMMENT '用户组名',
  `urg_userid` int(11) DEFAULT NULL COMMENT '创人ID',
  `urg_treeid` int(11) DEFAULT NULL COMMENT '树id',
  PRIMARY KEY (`urg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_usergroup
-- ----------------------------
INSERT INTO `tb_usergroup` VALUES ('1', '辅助管理员组', '1', '168');
INSERT INTO `tb_usergroup` VALUES ('3', '用户组一', '1', '149');
INSERT INTO `tb_usergroup` VALUES ('11', '我的树', '1', '234');

-- ----------------------------
-- Table structure for `tb_userhandle`
-- ----------------------------
DROP TABLE IF EXISTS `tb_userhandle`;
CREATE TABLE `tb_userhandle` (
  `uh_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uh_content` text COMMENT '操作组件的id和代码的json字符串',
  `uh_nodeid` int(11) DEFAULT NULL COMMENT '节点id',
  `uh_update` varchar(10) DEFAULT 'false' COMMENT '是否需要更新',
  PRIMARY KEY (`uh_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_userhandle
-- ----------------------------
INSERT INTO `tb_userhandle` VALUES ('2', 'aaaa', '144', 'false');

-- ----------------------------
-- Table structure for `tb_value`
-- ----------------------------
DROP TABLE IF EXISTS `tb_value`;
CREATE TABLE `tb_value` (
  `value_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '节点值ID',
  `value_nodeid` int(11) NOT NULL COMMENT '节点id',
  `value_data` text COMMENT '节点值',
  `value_key` text COMMENT '键值',
  `value_datetime` varchar(255) DEFAULT NULL COMMENT '记录保存时间',
  PRIMARY KEY (`value_id`),
  KEY `index_nodeid` (`value_nodeid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_value
-- ----------------------------
INSERT INTO `tb_value` VALUES ('97', '154', '{code:daima}', 'open', '2018-03-27 11:07:08');
INSERT INTO `tb_value` VALUES ('107', '152', '{\"time\":\"2018-03-13\",\"state\":\"open\"}', null, '2018-03-27 11:07:12');
INSERT INTO `tb_value` VALUES ('108', '152', '{\"time\":\"\",\"state\":\"\"}', null, '2018-03-20 11:07:16');
INSERT INTO `tb_value` VALUES ('109', '175', '{\"code\":\"int state = 0;\",\"name\":\"开机\"}', null, '2018-04-01 11:07:20');
INSERT INTO `tb_value` VALUES ('110', '178', '{\"name\":\"asasdash\",\"xxxxxx\":\"\"}', null, '2018-04-04 11:07:24');
INSERT INTO `tb_value` VALUES ('111', '202', 'fafba10000020013000101000003e80000fdfe', null, '2018-03-27 11:07:08');
INSERT INTO `tb_value` VALUES ('112', '178', '{\"value\":\"18.3\"}', null, '2018-03-27 11:07:10');
INSERT INTO `tb_value` VALUES ('113', '178', '{\"value\":\"28.3\"}', null, '2018-03-28 11:07:10');
INSERT INTO `tb_value` VALUES ('114', '178', '{\"value\":\"20.3\"}', null, '2018-03-28 12:07:10');
INSERT INTO `tb_value` VALUES ('115', '178', '{\"value\":\"21.3\"}', null, '2018-03-28 13:07:10');
INSERT INTO `tb_value` VALUES ('116', '178', '{\"value\":\"ba\"}', null, '2018-03-27 13:07:10');
INSERT INTO `tb_value` VALUES ('117', '231', '86.58', null, '2018-03-28 11:07:10');
INSERT INTO `tb_value` VALUES ('118', '231', '56.147', null, '2018-03-27 11:07:10');
INSERT INTO `tb_value` VALUES ('119', '231', '45.123', null, '2018-03-27 11:07:10');
INSERT INTO `tb_value` VALUES ('120', '231', '51.4578', null, '2018-03-27 18:07:10');
INSERT INTO `tb_value` VALUES ('121', '231', '14sdada', null, '2018-03-27 15:07:10');
