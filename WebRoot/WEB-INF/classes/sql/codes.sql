/*
Navicat MySQL Data Transfer

Source Server         : codes2
Source Server Version : 50153
Source Host           : localhost:6033
Source Database       : codes2

Target Server Type    : MYSQL
Target Server Version : 50153
File Encoding         : 65001

Date: 2023-06-19 09:50:09
*/

SET FOREIGN_KEY_CHECKS=0;

--

CREATE DATABASE IF NOT EXISTS codes;
USE codes;
-- ----------------------------
-- Table structure for c3p0_test
-- ----------------------------
DROP TABLE IF EXISTS `c3p0_test`;
CREATE TABLE `c3p0_test` (
  `a` char(1) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of c3p0_test
-- ----------------------------

-- ----------------------------
-- Table structure for temp_identity_num_mgr
-- ----------------------------
DROP TABLE IF EXISTS `temp_identity_num_mgr`;
CREATE TABLE `temp_identity_num_mgr` (
  `num` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL,
  `name` varchar(45) CHARACTER SET latin1 NOT NULL,
  `sex` varchar(10) CHARACTER SET latin1 NOT NULL,
  `nation` varchar(10) CHARACTER SET latin1 NOT NULL,
  `birthday` datetime NOT NULL,
  `addres` varchar(200) CHARACTER SET latin1 NOT NULL,
  `identity_type` varchar(10) CHARACTER SET latin1 NOT NULL,
  `identity_num` varchar(45) CHARACTER SET latin1 NOT NULL,
  `tel_num` varchar(45) CHARACTER SET latin1 NOT NULL,
  `distribute_dept` varchar(80) CHARACTER SET latin1 NOT NULL,
  `valid_start_date` datetime NOT NULL,
  `valid_end_date` datetime NOT NULL,
  PRIMARY KEY (`num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of temp_identity_num_mgr
-- ----------------------------

-- ----------------------------
-- Table structure for t_broadcast_msg
-- ----------------------------
DROP TABLE IF EXISTS `t_broadcast_msg`;
CREATE TABLE `t_broadcast_msg` (
  `LOGIC_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `COMP_ID` varchar(32) DEFAULT NULL,
  `OVERDUE_DATE` date DEFAULT NULL,
  `CONTENT` varchar(1000) DEFAULT NULL,
  `STATE` int(2) DEFAULT '0',
  `SEND_DATE` date DEFAULT NULL,
  `SENDER_ID` varchar(32) DEFAULT NULL,
  `TYPE` int(2) DEFAULT '0',
  `AND_MAIL` int(1) DEFAULT '0',
  `RECIP_ID` varchar(32) DEFAULT NULL,
  `ATTACH_URL` varchar(200) DEFAULT NULL,
  `START_DATE` date DEFAULT NULL,
  `TITLE` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`LOGIC_ID`),
  KEY `T_BROADCAST_MSG_IDX3` (`LOGIC_ID`,`COMP_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_broadcast_msg
-- ----------------------------

-- ----------------------------
-- Table structure for t_broadmsg_recip_rela
-- ----------------------------
DROP TABLE IF EXISTS `t_broadmsg_recip_rela`;
CREATE TABLE `t_broadmsg_recip_rela` (
  `MSG_LOGIC_ID` bigint(20) NOT NULL,
  `RECIP_ID` varchar(32) NOT NULL,
  KEY `FK_MG_RECPI_MSG` (`MSG_LOGIC_ID`) USING BTREE,
  KEY `FK_MG_RECPI_USR` (`RECIP_ID`) USING BTREE,
  CONSTRAINT `t_broadmsg_recip_rela_ibfk_1` FOREIGN KEY (`MSG_LOGIC_ID`) REFERENCES `t_broadcast_msg` (`LOGIC_ID`),
  CONSTRAINT `t_broadmsg_recip_rela_ibfk_2` FOREIGN KEY (`RECIP_ID`) REFERENCES `t_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 10240 kB; (`MSG_LOGIC_ID`) REFER `mypmbugtrace/';

-- ----------------------------
-- Records of t_broadmsg_recip_rela
-- ----------------------------

-- ----------------------------
-- Table structure for t_bugbaseinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_bugbaseinfo`;
CREATE TABLE `t_bugbaseinfo` (
  `TASK_ID` varchar(32) DEFAULT NULL,
  `TEST_PHASE` int(1) NOT NULL DEFAULT '0',
  `MODULEID` bigint(20) NOT NULL DEFAULT '0',
  `BUGCARDID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BUGTYPE` bigint(20) DEFAULT '0',
  `BUGLEVEL` bigint(20) DEFAULT '0',
  `BUGFREQ` bigint(20) DEFAULT '0',
  `BUGOCCA` bigint(20) DEFAULT '0',
  `REPRODUCT_TXT` varchar(4000) DEFAULT NULL,
  `BUGDISVPERID` varchar(32) DEFAULT '',
  `DISCOVER_VER` bigint(20) DEFAULT NULL,
  `VERIFY_VER` bigint(20) DEFAULT NULL,
  `RESBUGVER` varchar(30) DEFAULT NULL,
  `BUGDISVDATE` datetime DEFAULT NULL,
  `MSG_FLAG` int(2) NOT NULL,
  `BUGANTIMODDATE` date DEFAULT NULL,
  `PRI` bigint(20) DEFAULT NULL,
  `GENERATEPHASE` bigint(20) DEFAULT NULL,
  `DEV_OWNER` varchar(32) DEFAULT NULL,
  `PLANAMENDHOUR` float DEFAULT NULL,
  `GENERATECAUSE` bigint(20) DEFAULT NULL,
  `SOURCE` bigint(20) DEFAULT NULL,
  `CLIENT_PLATFORM` varchar(40) DEFAULT NULL,
  `PLATFORMID` bigint(20) DEFAULT NULL,
  `BUGDESC` varchar(500) DEFAULT NULL,
  `CURRENT_STATE` int(3) DEFAULT NULL,
  `CURRENT_HANDLER` varchar(32) DEFAULT NULL,
  `CURRENT_HANDL_DATE` datetime DEFAULT NULL,
  `CURRENT_REMARK` varchar(200) DEFAULT NULL,
  `CURR_FLOW_CODE` int(3) DEFAULT NULL,
  `MODULENUM` varchar(300) DEFAULT NULL,
  `TEST_SEQ` int(3) DEFAULT NULL,
  `INIT_SEQ` int(3) DEFAULT NULL,
  `BUGCARDFLOWID` varchar(32) DEFAULT NULL,
  `NEXT_FLOW_CODE` int(3) DEFAULT NULL,
  `INSDATE` datetime DEFAULT NULL,
  `REAL_CASE_FLAG` int(1) DEFAULT NULL,
  `REPRODUCTSTEP` varchar(4000) DEFAULT NULL,
  `TEST_OWNER` varchar(32) DEFAULT NULL,
  `ANALYSER_OWNER` varchar(32) DEFAULT NULL,
  `INTERCESS_OWNER` varchar(32) NOT NULL,
  `ASSIGNER_OWNER` varchar(32) DEFAULT NULL,
  `REPRO_PERSENT` varchar(30) DEFAULT NULL,
  `REPEATE_ID` bigint(20) DEFAULT NULL,
  `INIT_STATE` int(3) DEFAULT NULL,
  `ATTACH_URL` varchar(200) DEFAULT NULL,
  `FIX_VERSION` bigint(20) DEFAULT NULL,
  `SOURCE_BUG_ID` bigint(20) DEFAULT NULL,
  `BUG_NUM` varchar(50) DEFAULT NULL,
  `FIX_DATE` datetime DEFAULT NULL,
  `CHARGE_OWNER` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`BUGCARDID`),
  KEY `T_BUGBASEINFO_IDX3` (`TASK_ID`) USING BTREE,
  KEY `T_BUGBASEINFO_IDX4` (`TEST_PHASE`) USING BTREE,
  KEY `FK845179E89445B524` (`DISCOVER_VER`),
  CONSTRAINT `FK845179E89445B524` FOREIGN KEY (`DISCOVER_VER`) REFERENCES `t_software_version` (`VERSION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_bugbaseinfo
-- ----------------------------

-- ----------------------------
-- Table structure for t_bughandhistory
-- ----------------------------
DROP TABLE IF EXISTS `t_bughandhistory`;
CREATE TABLE `t_bughandhistory` (
  `TASK_ID` varchar(32) DEFAULT NULL,
  `TEST_PHASE` int(1) DEFAULT '0',
  `MODULEID` bigint(20) DEFAULT '0',
  `BUGCARDID` bigint(20) NOT NULL DEFAULT '0',
  `TEST_FLOW_CODE` int(3) NOT NULL DEFAULT '0',
  `BUGSTATE` int(3) NOT NULL DEFAULT '0',
  `REMARK` varchar(4000) DEFAULT NULL,
  `HANDLER` varchar(32) DEFAULT NULL,
  `INSDATE` datetime DEFAULT NULL,
  `TEST_SEQ` int(3) DEFAULT '0',
  `MODULENUM` varchar(60) DEFAULT NULL,
  `CURR_VER` bigint(20) DEFAULT NULL,
  `HISTORY_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FLOW_NODE_NAME` varchar(40) DEFAULT NULL,
  `INIT_STATE` int(3) DEFAULT NULL,
  `RESULT` varchar(4000) DEFAULT NULL,
  `CURR_DAY_FINAL` int(1) DEFAULT NULL,
  PRIMARY KEY (`HISTORY_ID`),
  KEY `T_BUGHANDHISTORY_IDX2` (`TASK_ID`,`BUGCARDID`) USING BTREE,
  KEY `T_BUGHANDHISTORY_IDX3` (`CURR_VER`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_bughandhistory
-- ----------------------------

-- ----------------------------
-- Table structure for t_bugstateinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_bugstateinfo`;
CREATE TABLE `t_bugstateinfo` (
  `STATEID` bigint(20) NOT NULL DEFAULT '0',
  `STATENAME` varchar(50) NOT NULL,
  `INSDATE` date DEFAULT NULL,
  PRIMARY KEY (`STATEID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_bugstateinfo
-- ----------------------------

-- ----------------------------
-- Table structure for t_bugtype
-- ----------------------------
DROP TABLE IF EXISTS `t_bugtype`;
CREATE TABLE `t_bugtype` (
  `TYPE_NAME` varchar(30) DEFAULT NULL,
  `TYPE_ID` bigint(20) NOT NULL,
  `COMP_ID` varchar(32) NOT NULL,
  PRIMARY KEY (`TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_bugtype
-- ----------------------------

-- ----------------------------
-- Table structure for t_case_bug_rela
-- ----------------------------
DROP TABLE IF EXISTS `t_case_bug_rela`;
CREATE TABLE `t_case_bug_rela` (
  `TASK_ID` varchar(32) DEFAULT NULL,
  `TESTCASEID` bigint(20) NOT NULL,
  `BUGCARDID` bigint(20) NOT NULL,
  `INSDATE` date DEFAULT NULL,
  `INSUSEID` varchar(32) DEFAULT NULL,
  `BUG_CASE_RELA_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`BUG_CASE_RELA_ID`),
  KEY `FKA811617759ACC5F8` (`BUGCARDID`),
  KEY `FKA81161772F3A627` (`TESTCASEID`),
  CONSTRAINT `FKA81161772F3A627` FOREIGN KEY (`TESTCASEID`) REFERENCES `t_testcasebaseinfo` (`TESTCASEID`),
  CONSTRAINT `FKA811617759ACC5F8` FOREIGN KEY (`BUGCARDID`) REFERENCES `t_bugbaseinfo` (`BUGCARDID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_case_bug_rela
-- ----------------------------

-- ----------------------------
-- Table structure for t_company
-- ----------------------------
DROP TABLE IF EXISTS `t_company`;
CREATE TABLE `t_company` (
  `TYPE` int(2) DEFAULT '0',
  `COMPSIZE` int(5) DEFAULT '0',
  `STATUS` int(2) DEFAULT '0',
  `OWNER` varchar(32) NOT NULL,
  `OWNEREMAIL` varchar(32) NOT NULL,
  `OWNERTEL` varchar(20) DEFAULT '',
  `TELEPHONE` varchar(20) DEFAULT '',
  `FAX` varchar(20) DEFAULT NULL,
  `ADDRESS` varchar(255) DEFAULT NULL,
  `URL` varchar(255) DEFAULT NULL,
  `STARTTIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ENDTIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `EXTENDTIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `REMARK` varchar(150) DEFAULT NULL,
  `INSERTDATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `UPDATEDATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `LOGINID` varchar(15) DEFAULT NULL,
  `DELFLAG` int(1) DEFAULT NULL,
  `KNOW_WAY` int(1) DEFAULT NULL,
  `ACCOUNT_TYPE` int(1) DEFAULT NULL,
  `REGIS_IP` varchar(50) DEFAULT NULL,
  `DOC_ROOT_ID` bigint(20) unsigned DEFAULT NULL,
  `DOC_PRO_ROOT_ID` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_company
-- ----------------------------
INSERT INTO `t_company` VALUES ('0', '0', '0', 'admin', 'test@111.com', '', '', null, null, null, '2010-11-13 14:37:28', '0000-00-00 00:00:00', '0000-00-00 00:00:00', '1289630248328', 'compName', null, '0000-00-00 00:00:00', '0000-00-00 00:00:00', null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for t_concern_other_mission
-- ----------------------------
DROP TABLE IF EXISTS `t_concern_other_mission`;
CREATE TABLE `t_concern_other_mission` (
  `concern_mission_id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `mission_id` varchar(32) NOT NULL,
  PRIMARY KEY (`concern_mission_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_concern_other_mission
-- ----------------------------

-- ----------------------------
-- Table structure for t_cycle_task_bug_or_calse_real
-- ----------------------------
DROP TABLE IF EXISTS `t_cycle_task_bug_or_calse_real`;
CREATE TABLE `t_cycle_task_bug_or_calse_real` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cycle_task_id` int(11) DEFAULT NULL,
  `bug_or_case_id` int(11) DEFAULT NULL,
  `exe_id` varchar(255) DEFAULT NULL,
  `exe_date` datetime DEFAULT NULL,
  `exe_result` varchar(500) DEFAULT NULL,
  `cycle_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_cycle_task_bug_or_calse_real
-- ----------------------------

-- ----------------------------
-- Table structure for t_exe_history
-- ----------------------------
DROP TABLE IF EXISTS `t_exe_history`;
CREATE TABLE `t_exe_history` (
  `TASKID` varchar(32) DEFAULT NULL,
  `TEST_PHASE` tinyint(1) DEFAULT NULL,
  `MODULEID` bigint(20) unsigned NOT NULL,
  `TESTCASEID` bigint(20) unsigned NOT NULL,
  `TESTVER` bigint(20) unsigned DEFAULT NULL,
  `TESTRESULT` tinyint(1) DEFAULT NULL,
  `TESTACTOR` varchar(32) DEFAULT NULL,
  `INSDATE` datetime DEFAULT NULL,
  `UPDDATE` datetime DEFAULT NULL,
  `RESULTID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `TESTCONFIGID` bigint(20) unsigned DEFAULT NULL,
  `REMARK` varchar(200) DEFAULT NULL,
  `SEQ` tinyint(3) DEFAULT NULL,
  `PLANT_FORM_ID` bigint(20) unsigned DEFAULT NULL,
  `OPERA_TYPE` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`RESULTID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_exe_history
-- ----------------------------

-- ----------------------------
-- Table structure for t_file_info
-- ----------------------------
DROP TABLE IF EXISTS `t_file_info`;
CREATE TABLE `t_file_info` (
  `file_id` varchar(32) NOT NULL,
  `type` varchar(8) DEFAULT NULL,
  `type_id` bigint(20) DEFAULT NULL,
  `file_path` varchar(1024) DEFAULT NULL,
  `relative_name` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`file_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_file_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_function
-- ----------------------------
DROP TABLE IF EXISTS `t_function`;
CREATE TABLE `t_function` (
  `FUNCTIONID` varchar(32) NOT NULL,
  `FUNCTIONNAME` varchar(40) DEFAULT NULL,
  `PARENTID` varchar(20) DEFAULT NULL,
  `LEVELNUM` int(2) DEFAULT '0',
  `ISLEAF` int(1) DEFAULT '0',
  `METHODS` varchar(200) DEFAULT NULL,
  `SEQ` bigint(11) DEFAULT '0',
  `URL` varchar(300) DEFAULT NULL,
  `INSERTDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATEDATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `PAGE` varchar(60) DEFAULT NULL,
  `SECURITY_URL` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`FUNCTIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_function
-- ----------------------------
INSERT INTO `t_function` VALUES ('1', '系统管理', '-1', '1', '0', null, '1', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', '');
INSERT INTO `t_function` VALUES ('100', '修改', '87', '2', '1', null, '100', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'caseManagerAction!upInit;loadCase');
INSERT INTO `t_function` VALUES ('101', '删除', '87', '2', '1', null, '101', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'caseManagerAction!delCase;loadCase');
INSERT INTO `t_function` VALUES ('102', '执行', '87', '2', '1', null, '102', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'caseManagerAction!exeCase;loadCase');
INSERT INTO `t_function` VALUES ('103', '审核', '87', '2', '1', null, '103', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'caseManagerAction!auditCase;loadCase');
INSERT INTO `t_function` VALUES ('104', '执行记录', '87', '2', '1', null, '104', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'caseManagerAction!viewHistory;loadCase');
INSERT INTO `t_function` VALUES ('105', '最近执行的用例', '-1', '1', '0', null, '105', '/caseManager/caseManagerAction!lastExeCase.action', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '0', '/caseManager/caseManagerAction!lastExeCase.action');
INSERT INTO `t_function` VALUES ('106', '查看', '105', '2', '1', null, '106', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', '');
INSERT INTO `t_function` VALUES ('107', '缺陷管理', '-1', '1', '0', null, '107', '/bugManager/bugManagerAction!loadAllMyBug.action', '2018-10-16 11:21:08', '0000-00-00 00:00:00', '0', '/bugManager/bugManagerAction!loadAllMyBug.action');
INSERT INTO `t_function` VALUES ('108', 'BUG列表', '107', '2', '1', null, '108', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'bugManagerAction!loadMyBug');
INSERT INTO `t_function` VALUES ('109', '修改BUG', '107', '2', '1', null, '109', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'bugManagerAction!upInit;loadMyBug');
INSERT INTO `t_function` VALUES ('11', '用例包管理', '-1', '1', '0', null, '233', '/testCasePkgManager/testCasePackageAction!goTestCasePkgMain.action', '2018-10-11 15:11:15', '0000-00-00 00:00:00', '0', '/testCasePkgManager/testCasePackageAction!goTestCasePkgMain.action');
INSERT INTO `t_function` VALUES ('111', '浏览', '11', '2', '1', '', '111', '/testCasePkgManager/testCasePackageAction!goTestCasePkgMain.action', '2018-09-21 13:40:26', '0000-00-00 00:00:00', '', '/testCasePkgManager/testCasePackageAction!goTestCasePkgMain.action');
INSERT INTO `t_function` VALUES ('112', '增加', '11', '2', '1', '', '112', '', '2018-10-11 14:28:26', '0000-00-00 00:00:00', '', 'testCasePackageAction!addTestCasePkg;loadTestCasePackageList');
INSERT INTO `t_function` VALUES ('113', '修改', '11', '2', '1', '', '113', '', '2018-10-11 14:29:17', '0000-00-00 00:00:00', '', 'testCasePackageAction!updateTestCasePkg;loadTestCasePackageList');
INSERT INTO `t_function` VALUES ('114', '删除', '11', '2', '1', '', '114', '', '2018-10-11 14:29:17', '0000-00-00 00:00:00', '', 'testCasePackageAction!deleteTestCasePkg;loadTestCasePackageList');
INSERT INTO `t_function` VALUES ('120', '删除BUG', '107', '2', '1', null, '120', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'bugManagerAction!delete;loadMyBug');
INSERT INTO `t_function` VALUES ('121', '消息管理', '-1', '1', '0', null, '121', '', '2011-03-24 17:27:18', '0000-00-00 00:00:00', '0', '');
INSERT INTO `t_function` VALUES ('122', '消息维护', '121', '2', '0', null, '122', '/msgManager/commonMsgAction!loadMsg.action', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '0', '/msgManager/commonMsgAction!loadMsg.action');
INSERT INTO `t_function` VALUES ('123', '发布', '122', '3', '1', null, '123', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '0', 'commonMsgAction!publish;loadMsg');
INSERT INTO `t_function` VALUES ('124', '修改', '122', '3', '1', null, '124', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '0', 'commonMsgAction!upinit;loadMsg');
INSERT INTO `t_function` VALUES ('125', '删除', '122', '3', '1', null, '125', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '0', 'commonMsgAction!delBroMsg;loadMsg');
INSERT INTO `t_function` VALUES ('126', '接收消息', '121', '2', '0', null, '126', '/msgManager/commonMsgAction!loadRecevMsg.action', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '0', '/msgManager/commonMsgAction!loadRecevMsg.action');
INSERT INTO `t_function` VALUES ('127', '查看', '126', '3', '1', null, '127', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '0', 'commonMsgAction!loadRecevMsg');
INSERT INTO `t_function` VALUES ('128', '发布消息', '121', '2', '0', null, '128', '/msgManager/commonMsgAction!loadSendMsg.action', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '0', '/msgManager/commonMsgAction!loadSendMsg.action');
INSERT INTO `t_function` VALUES ('129', '查看', '128', '3', '1', null, '129', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '0', 'commonMsgAction!loadSendMsg');
INSERT INTO `t_function` VALUES ('13', '总览', '-1', '1', '0', null, '12', '/overview/overviewAction!loadInformation.action', '2018-09-14 08:36:40', '0000-00-00 00:00:00', '0', '/overview/overviewAction!loadInformation.action');
INSERT INTO `t_function` VALUES ('131', '浏览', '13', '2', '1', '', '131', '/overview/overviewAction!loadInformation.action', '2018-09-21 13:50:35', '0000-00-00 00:00:00', null, '/overview/overviewAction!loadInformation.action');
INSERT INTO `t_function` VALUES ('140', '发布', '128', '3', '1', null, '140', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '0', 'commonMsgAction!sendBroMsg;loadSendMsg');
INSERT INTO `t_function` VALUES ('141', '修改', '128', '3', '1', null, '141', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '0', 'commonMsgAction!sendMsgUpInit;loadSendMsg');
INSERT INTO `t_function` VALUES ('142', '删除', '128', '3', '1', null, '142', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '0', 'commonMsgAction!sendBroMsgDel;loadSendMsg');
INSERT INTO `t_function` VALUES ('143', '分析度量', '-1', '1', '0', null, '143', '/analysis/analysisAction!goAnalysisMain.action', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '0', '/analysis/analysisAction!goAnalysisMain.action');
INSERT INTO `t_function` VALUES ('144', '趋势分析', '143', '2', '0', null, '144', 'bl', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', 'bl');
INSERT INTO `t_function` VALUES ('145', '日编写用例趋势', '144', '3', '0', null, '145', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=writeCaseDayTrend_750_540_wrCaseDate', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=writeCaseDayTrend_750_540_wrCaseDate');
INSERT INTO `t_function` VALUES ('146', '查看', '145', '4', '1', null, '146', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'writeCaseDayTrend');
INSERT INTO `t_function` VALUES ('147', '提交|打开|待处理|修改|关闭BUG趋势', '144', '3', '0', null, '147', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=reptFixCloseDayTrend_750_540_bugDate', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=reptFixCloseDayTrend_750_540_bugDate');
INSERT INTO `t_function` VALUES ('148', '查看', '147', '4', '1', null, '148', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'reptFixCloseDayTrend');
INSERT INTO `t_function` VALUES ('149', '开发人员日修改BUG趋势', '144', '3', '0', null, '149', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=devDayFixTrend_750_540_bugDate', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=devDayFixTrend_750_540_bugDate');
INSERT INTO `t_function` VALUES ('160', '查看', '149', '4', '1', null, '160', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'devDayFixTrend');
INSERT INTO `t_function` VALUES ('161', '日提交BUG趋势', '144', '3', '0', null, '161', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=testerDayCommitTrend_750_540_bugDate', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=testerDayCommitTrend_750_540_bugDate');
INSERT INTO `t_function` VALUES ('162', '查看', '161', '4', '1', null, '162', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'testerDayCommitTrend');
INSERT INTO `t_function` VALUES ('163', '日提交及BUG总数趋势', '144', '3', '0', null, '163', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=commitExistBugDayStat_750_540_bugDate', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=commitExistBugDayStat_750_540_bugDate');
INSERT INTO `t_function` VALUES ('164', '查看', '163', '4', '1', null, '164', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'commitExistBugDayStat');
INSERT INTO `t_function` VALUES ('165', '版本间提交及BUG总数趋势', '144', '3', '0', null, '165', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=commitExistBugBuildStat_750_540', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=commitExistBugBuildStat_750_540');
INSERT INTO `t_function` VALUES ('166', '查看', '165', '4', '1', null, '166', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'commitExistBugBuildStat');
INSERT INTO `t_function` VALUES ('167', '日执行用例趋势及明细', '144', '3', '0', null, '167', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=testerExeCaseDayTrend_750_540_exeCaseDate', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=testerExeCaseDayTrend_750_540_exeCaseDate');
INSERT INTO `t_function` VALUES ('168', '查看', '167', '4', '1', null, '168', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'testerExeCaseDayTrend');
INSERT INTO `t_function` VALUES ('169', '开发人员待改|BUG修改统计', '143', '2', '0', null, '169', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugFixPersonStat_750_540', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugFixPersonStat_750_540');
INSERT INTO `t_function` VALUES ('180', '查看', '169', '3', '1', null, '180', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'bugFixPersonStat');
INSERT INTO `t_function` VALUES ('181', '测试人员提交|关闭BUG统计', '143', '2', '0', null, '181', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=testerBugStat_750_540', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=testerBugStat_750_540');
INSERT INTO `t_function` VALUES ('182', '查看', '181', '3', '1', null, '182', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'testerBugStat');
INSERT INTO `t_function` VALUES ('183', '己关闭BUG按天龄期统计', '143', '2', '0', null, '183', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugExistDayStat_750_540', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugExistDayStat_750_540');
INSERT INTO `t_function` VALUES ('184', '查看', '183', '3', '1', null, '184', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'bugExistDayStat');
INSERT INTO `t_function` VALUES ('185', '己关闭BUG按周龄期统计', '143', '2', '0', null, '185', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugExistWeekStat_750_540', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugExistWeekStat_750_540');
INSERT INTO `t_function` VALUES ('186', '查看', '185', '3', '1', null, '186', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'bugExistWeekStat');
INSERT INTO `t_function` VALUES ('187', '待处理BUG按天龄期统计', '143', '2', '0', null, '187', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugExistDay4NoFixStat_750_540', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugExistDay4NoFixStat_750_540');
INSERT INTO `t_function` VALUES ('188', '查看', '187', '3', '1', null, '188', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'bugExistDay4NoFixStat');
INSERT INTO `t_function` VALUES ('189', '待处理BUG按周龄期统计', '143', '2', '0', null, '189', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugExistWeek4NoFixStat_750_540', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugExistWeek4NoFixStat_750_540');
INSERT INTO `t_function` VALUES ('2', '用户管理', '1', '2', '0', null, '2', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', '');
INSERT INTO `t_function` VALUES ('20', '用户组管理', '2', '3', '0', null, '20', '/userManager/userManagerAction!groupList.action', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '0', '/userManager/userManagerAction!groupList.action');
INSERT INTO `t_function` VALUES ('200', '查看', '189', '3', '1', null, '200', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'bugExistWeek4NoFixStat');
INSERT INTO `t_function` VALUES ('201', 'BUG类型统计', '143', '2', '0', null, '201', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugTypeStat_750_540', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugTypeStat_750_540');
INSERT INTO `t_function` VALUES ('202', '查看', '201', '3', '1', null, '202', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'bugTypeStat');
INSERT INTO `t_function` VALUES ('203', 'BUG等级统计', '143', '2', '0', null, '203', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugGradeStat_750_540', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugGradeStat_750_540');
INSERT INTO `t_function` VALUES ('204', '查看', '203', '3', '1', null, '204', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'bugGradeStat');
INSERT INTO `t_function` VALUES ('205', 'BUG引入阶段分析', '143', '2', '0', null, '205', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugImpPhaseStat_750_540', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugImpPhaseStat_750_540');
INSERT INTO `t_function` VALUES ('206', '查看', '205', '3', '1', null, '206', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'bugImpPhaseStat');
INSERT INTO `t_function` VALUES ('207', '遗留BUG分析', '143', '2', '0', null, '207', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugBequeathStat_750_540', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugBequeathStat_750_540');
INSERT INTO `t_function` VALUES ('208', '查看', '207', '3', '1', null, '208', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'bugBequeathStat');
INSERT INTO `t_function` VALUES ('209', '测试人员BUG质量分析', '143', '2', '0', null, '209', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=testerBugQuality_750_540', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=testerBugQuality_750_540');
INSERT INTO `t_function` VALUES ('21', '浏览', '20', '4', '1', null, '21', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'userManagerAction!groupList');
INSERT INTO `t_function` VALUES ('22', '新增', '20', '4', '1', null, '22', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'userManagerAction!addGroup;groupList');
INSERT INTO `t_function` VALUES ('220', '查看', '209', '3', '1', null, '220', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'testerBugQuality');
INSERT INTO `t_function` VALUES ('221', 'BUG密度分析', '143', '2', '0', null, '221', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugDensityStat_750_540', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugDensityStat_750_540');
INSERT INTO `t_function` VALUES ('222', '查看', '221', '3', '1', null, '222', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'bugDensityStat');
INSERT INTO `t_function` VALUES ('223', '测试需求项BUG分布明细', '143', '2', '0', null, '223', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugModuleDistbuStat_750_540', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugModuleDistbuStat_750_540');
INSERT INTO `t_function` VALUES ('224', '查看', '223', '3', '1', null, '224', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'bugModuleDistbuStat');
INSERT INTO `t_function` VALUES ('225', 'BUG状态分布统计', '143', '2', '0', null, '225', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugStatusDistbuStat_750_540', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '1', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugStatusDistbuStat_750_540');
INSERT INTO `t_function` VALUES ('226', '查看', '225', '3', '1', null, '226', '', '2011-03-24 17:27:19', '0000-00-00 00:00:00', '1', 'bugStatusDistbuStat');
INSERT INTO `t_function` VALUES ('227', '简要统计', '143', '2', '0', null, '227', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=bugSummary_750_540_bugDate', '2012-07-26 10:18:59', '0000-00-00 00:00:00', '1', 'bugSummary');
INSERT INTO `t_function` VALUES ('228', '查看', '227', '3', '1', null, '228', '', '2012-07-26 10:18:59', '0000-00-00 00:00:00', '1', 'bugSummary');
INSERT INTO `t_function` VALUES ('229', '责任人分析', '143', '2', '0', null, '229', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=chargeOwner_750_540_bugDate', '2014-01-21 14:47:40', '0000-00-00 00:00:00', '1', 'chargeOwner');
INSERT INTO `t_function` VALUES ('23', '修改', '20', '4', '1', null, '23', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'userManagerAction!updGroup;groupList');
INSERT INTO `t_function` VALUES ('230', '查看', '229', '3', '1', null, '230', '', '2014-01-21 16:19:34', '0000-00-00 00:00:00', '1', 'chargeOwnerByProject;chargeOwner');
INSERT INTO `t_function` VALUES ('231', '责任人引入原因分析', '143', '2', '0', null, '231', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=importCase_750_540_wrCaseDate', '2014-01-21 17:05:27', '0000-00-00 00:00:00', '1', 'importCase');
INSERT INTO `t_function` VALUES ('232', '查看', '231', '3', '1', null, '232', '/singleTestTask/singleTestTaskAction!swTestTask4Report.action?dto.repTemplet=importCase_750_540_bugDate', '2014-01-21 16:33:31', '0000-00-00 00:00:00', '1', 'importCase;importCaseByProject');
INSERT INTO `t_function` VALUES ('24', '删除', '20', '4', '1', null, '24', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'userManagerAction!delGroup;groupList');
INSERT INTO `t_function` VALUES ('25', '权限管理', '1', '2', '0', null, '25', '/role/roleAction!roleList.action', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '0', '/role/roleAction!roleList.action');
INSERT INTO `t_function` VALUES ('26', '浏览', '25', '3', '1', null, '26', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'roleAction!roleList');
INSERT INTO `t_function` VALUES ('27', '增加', '25', '3', '1', null, '27', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'roleAction!new;roleList');
INSERT INTO `t_function` VALUES ('28', '修改', '25', '3', '1', null, '28', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'roleAction!update;roleList');
INSERT INTO `t_function` VALUES ('29', '删除', '25', '3', '1', null, '29', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'roleAction!delete;roleList');
INSERT INTO `t_function` VALUES ('3', '用户管理', '2', '3', '0', null, '3', '/userManager/userManagerAction!userList.action', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '0', '/userManager/userManagerAction!userList.action');
INSERT INTO `t_function` VALUES ('4', '浏览', '3', '4', '1', null, '4', '', '2018-08-13 15:38:56', '0000-00-00 00:00:00', '0', '/userManager/userManagerAction!userList.action');
INSERT INTO `t_function` VALUES ('40', '人员明细', '25', '3', '1', null, '40', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'roleAction!roleUserList;roleList');
INSERT INTO `t_function` VALUES ('41', '人员维护', '25', '3', '1', null, '41', '', '2018-08-16 09:43:29', '0000-00-00 00:00:00', '0', 'roleAction!userManager;roleList');
INSERT INTO `t_function` VALUES ('42', '权限查看', '25', '3', '1', null, '42', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'roleAction!browserAuth;roleList');
INSERT INTO `t_function` VALUES ('43', '权限设置', '25', '3', '1', null, '43', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'roleAction!grantRoleAuth;roleList');
INSERT INTO `t_function` VALUES ('44', '基础字典维护', '1', '2', '0', null, '44', '/testBaseSet/testBaseSetAction!testBaseSetList.action', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '0', '/testBaseSet/testBaseSetAction!testBaseSetList.action');
INSERT INTO `t_function` VALUES ('45', '浏览', '44', '3', '1', null, '45', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'testBaseSetAction!testBaseSetList');
INSERT INTO `t_function` VALUES ('46', '新建', '44', '3', '1', null, '46', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'testBaseSetAction!add;testBaseSetList');
INSERT INTO `t_function` VALUES ('47', '修改', '44', '3', '1', null, '47', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'testBaseSetAction!update;testBaseSetList');
INSERT INTO `t_function` VALUES ('48', '启用|停用', '44', '3', '1', null, '48', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'testBaseSetAction!swStatus;testBaseSetList');
INSERT INTO `t_function` VALUES ('49', '删除', '44', '3', '1', null, '49', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'testBaseSetAction!delete;testBaseSetList');
INSERT INTO `t_function` VALUES ('5', '新建', '3', '4', '1', null, '5', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'userManagerAction!addUser;userList');
INSERT INTO `t_function` VALUES ('6', '修改', '3', '4', '1', null, '6', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'userManagerAction!updUser;userList');
INSERT INTO `t_function` VALUES ('60', '测试项目管理', '-1', '1', '0', null, '60', '/singleTestTask/singleTestTaskAction!magrTaskListLoad.action', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '0', '/singleTestTask/singleTestTaskAction!magrTaskListLoad.action');
INSERT INTO `t_function` VALUES ('61', '浏览', '60', '2', '1', null, '61', '', '2018-06-26 09:15:33', '0000-00-00 00:00:00', '0', 'singleTestTaskAction!magrTaskListLoad');
INSERT INTO `t_function` VALUES ('62', '新建', '60', '2', '1', null, '62', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'singleTestTaskAction!add;magrTaskList');
INSERT INTO `t_function` VALUES ('63', '修改', '60', '2', '1', null, '63', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'singleTestTaskAction!update;magrTaskList');
INSERT INTO `t_function` VALUES ('64', '删除', '60', '2', '1', null, '64', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'singleTestTaskAction!delete;magrTaskList');
INSERT INTO `t_function` VALUES ('65', '测试流程|版本 管理', '-1', '1', '0', null, '65', '/testTaskManager/testTaskManagerAction!flwSetInit.action', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '0', '/testTaskManager/testTaskManagerAction!flwSetInit.action');
INSERT INTO `t_function` VALUES ('66', '流程|版本 查看', '65', '2', '1', null, '66', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'testTaskManagerAction!flwSetInit');
INSERT INTO `t_function` VALUES ('67', '流程|版本 设置', '65', '2', '1', null, '67', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'testTaskManagerAction!update;flwSetInit;');
INSERT INTO `t_function` VALUES ('68', '测试需求管理', '-1', '1', '0', null, '68', '/outLineManager/outLineAction!index.action', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '0', '/outLineManager/outLineAction!index.action');
INSERT INTO `t_function` VALUES ('69', '浏览', '68', '2', '1', null, '69', '', '2018-09-12 13:26:26', '0000-00-00 00:00:00', '0', 'outLineAction!initList');
INSERT INTO `t_function` VALUES ('7', '切换状态', '3', '4', '1', null, '7', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'userManagerAction!swUserStatus;userList');
INSERT INTO `t_function` VALUES ('70', '查看预估测试成本', '68', '2', '1', null, '70', '', '2011-03-24 16:17:13', '0000-00-00 00:00:00', '0', 'cost_prediction');
INSERT INTO `t_function` VALUES ('8', '密码重置', '3', '4', '1', null, '8', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'userManagerAction!update2Init;userList');
INSERT INTO `t_function` VALUES ('80', '新建', '68', '2', '1', null, '80', '', '2018-09-12 13:26:34', '0000-00-00 00:00:00', '0', 'outLineAction!addNodes;initList');
INSERT INTO `t_function` VALUES ('81', '启用|停用', '68', '2', '1', null, '81', '', '2018-09-12 13:26:39', '0000-00-00 00:00:00', '0', 'outLineAction!switchState;initList');
INSERT INTO `t_function` VALUES ('82', '修改', '68', '2', '1', null, '82', '', '2018-09-12 13:26:45', '0000-00-00 00:00:00', '0', 'outLineAction!updateNode;initList');
INSERT INTO `t_function` VALUES ('83', '删除', '68', '2', '1', null, '83', '', '2018-09-12 13:26:50', '0000-00-00 00:00:00', '0', 'outLineAction!deleteNode;initList');
INSERT INTO `t_function` VALUES ('84', '移动', '68', '2', '1', null, '84', '', '2018-09-12 13:26:57', '0000-00-00 00:00:00', '0', 'outLineAction!move;initList');
INSERT INTO `t_function` VALUES ('85', '人员分配', '68', '2', '1', null, '85', '', '2018-09-12 10:07:35', '0000-00-00 00:00:00', '0', 'outLineAction!assignPeople;initList');
INSERT INTO `t_function` VALUES ('86', '提交测试', '68', '2', '1', null, '86', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'outLineAction!submitModule;initList');
INSERT INTO `t_function` VALUES ('87', '测试用例管理', '-1', '1', '0', null, '87', '/caseManager/caseManagerAction!loadCase.action', '2018-09-05 17:30:56', '0000-00-00 00:00:00', '0', '/caseManager/caseManagerAction!loadCase.action');
INSERT INTO `t_function` VALUES ('88', '浏览', '87', '2', '1', null, '88', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'caseManagerAction!loadCase');
INSERT INTO `t_function` VALUES ('89', '新建', '87', '2', '1', null, '89', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'caseManagerAction!addInit;loadCase');
INSERT INTO `t_function` VALUES ('9', '删除', '3', '4', '1', null, '9', '', '2010-11-13 14:37:29', '0000-00-00 00:00:00', '0', 'userManagerAction!ldeleteUser;userList');

-- ----------------------------
-- Table structure for t_group
-- ----------------------------
DROP TABLE IF EXISTS `t_group`;
CREATE TABLE `t_group` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(32) NOT NULL,
  `ADMINFLAG` int(1) NOT NULL DEFAULT '0',
  `REMARK` varchar(255) DEFAULT NULL,
  `COMPANYID` varchar(32) DEFAULT NULL,
  `INSERTDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATEDATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_group
-- ----------------------------

-- ----------------------------
-- Table structure for t_helper
-- ----------------------------
DROP TABLE IF EXISTS `t_helper`;
CREATE TABLE `t_helper` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of t_helper
-- ----------------------------
INSERT INTO `t_helper` VALUES ('0');
INSERT INTO `t_helper` VALUES ('1');
INSERT INTO `t_helper` VALUES ('2');
INSERT INTO `t_helper` VALUES ('3');
INSERT INTO `t_helper` VALUES ('4');
INSERT INTO `t_helper` VALUES ('5');
INSERT INTO `t_helper` VALUES ('6');
INSERT INTO `t_helper` VALUES ('7');
INSERT INTO `t_helper` VALUES ('8');
INSERT INTO `t_helper` VALUES ('9');
INSERT INTO `t_helper` VALUES ('10');
INSERT INTO `t_helper` VALUES ('11');
INSERT INTO `t_helper` VALUES ('12');
INSERT INTO `t_helper` VALUES ('13');
INSERT INTO `t_helper` VALUES ('14');
INSERT INTO `t_helper` VALUES ('15');
INSERT INTO `t_helper` VALUES ('16');
INSERT INTO `t_helper` VALUES ('17');
INSERT INTO `t_helper` VALUES ('18');
INSERT INTO `t_helper` VALUES ('19');
INSERT INTO `t_helper` VALUES ('20');
INSERT INTO `t_helper` VALUES ('21');
INSERT INTO `t_helper` VALUES ('22');
INSERT INTO `t_helper` VALUES ('23');
INSERT INTO `t_helper` VALUES ('24');
INSERT INTO `t_helper` VALUES ('25');
INSERT INTO `t_helper` VALUES ('26');
INSERT INTO `t_helper` VALUES ('27');
INSERT INTO `t_helper` VALUES ('28');
INSERT INTO `t_helper` VALUES ('29');
INSERT INTO `t_helper` VALUES ('30');
INSERT INTO `t_helper` VALUES ('31');
INSERT INTO `t_helper` VALUES ('32');
INSERT INTO `t_helper` VALUES ('33');
INSERT INTO `t_helper` VALUES ('34');
INSERT INTO `t_helper` VALUES ('35');
INSERT INTO `t_helper` VALUES ('36');
INSERT INTO `t_helper` VALUES ('37');
INSERT INTO `t_helper` VALUES ('38');
INSERT INTO `t_helper` VALUES ('39');
INSERT INTO `t_helper` VALUES ('40');
INSERT INTO `t_helper` VALUES ('41');
INSERT INTO `t_helper` VALUES ('42');
INSERT INTO `t_helper` VALUES ('43');
INSERT INTO `t_helper` VALUES ('44');
INSERT INTO `t_helper` VALUES ('45');
INSERT INTO `t_helper` VALUES ('46');
INSERT INTO `t_helper` VALUES ('47');
INSERT INTO `t_helper` VALUES ('48');
INSERT INTO `t_helper` VALUES ('49');
INSERT INTO `t_helper` VALUES ('50');
INSERT INTO `t_helper` VALUES ('51');
INSERT INTO `t_helper` VALUES ('52');
INSERT INTO `t_helper` VALUES ('53');
INSERT INTO `t_helper` VALUES ('54');
INSERT INTO `t_helper` VALUES ('55');
INSERT INTO `t_helper` VALUES ('56');
INSERT INTO `t_helper` VALUES ('57');
INSERT INTO `t_helper` VALUES ('58');
INSERT INTO `t_helper` VALUES ('59');
INSERT INTO `t_helper` VALUES ('60');
INSERT INTO `t_helper` VALUES ('61');
INSERT INTO `t_helper` VALUES ('62');
INSERT INTO `t_helper` VALUES ('63');
INSERT INTO `t_helper` VALUES ('64');
INSERT INTO `t_helper` VALUES ('65');
INSERT INTO `t_helper` VALUES ('66');
INSERT INTO `t_helper` VALUES ('67');
INSERT INTO `t_helper` VALUES ('68');
INSERT INTO `t_helper` VALUES ('69');
INSERT INTO `t_helper` VALUES ('70');
INSERT INTO `t_helper` VALUES ('71');
INSERT INTO `t_helper` VALUES ('72');
INSERT INTO `t_helper` VALUES ('73');
INSERT INTO `t_helper` VALUES ('74');
INSERT INTO `t_helper` VALUES ('75');
INSERT INTO `t_helper` VALUES ('76');
INSERT INTO `t_helper` VALUES ('77');
INSERT INTO `t_helper` VALUES ('78');
INSERT INTO `t_helper` VALUES ('79');
INSERT INTO `t_helper` VALUES ('80');
INSERT INTO `t_helper` VALUES ('81');
INSERT INTO `t_helper` VALUES ('82');
INSERT INTO `t_helper` VALUES ('83');
INSERT INTO `t_helper` VALUES ('84');
INSERT INTO `t_helper` VALUES ('85');
INSERT INTO `t_helper` VALUES ('86');
INSERT INTO `t_helper` VALUES ('87');
INSERT INTO `t_helper` VALUES ('88');
INSERT INTO `t_helper` VALUES ('89');
INSERT INTO `t_helper` VALUES ('90');
INSERT INTO `t_helper` VALUES ('91');
INSERT INTO `t_helper` VALUES ('92');
INSERT INTO `t_helper` VALUES ('93');
INSERT INTO `t_helper` VALUES ('94');
INSERT INTO `t_helper` VALUES ('95');
INSERT INTO `t_helper` VALUES ('96');
INSERT INTO `t_helper` VALUES ('97');
INSERT INTO `t_helper` VALUES ('98');
INSERT INTO `t_helper` VALUES ('99');
INSERT INTO `t_helper` VALUES ('100');
INSERT INTO `t_helper` VALUES ('101');
INSERT INTO `t_helper` VALUES ('102');
INSERT INTO `t_helper` VALUES ('103');
INSERT INTO `t_helper` VALUES ('104');
INSERT INTO `t_helper` VALUES ('105');
INSERT INTO `t_helper` VALUES ('106');
INSERT INTO `t_helper` VALUES ('107');
INSERT INTO `t_helper` VALUES ('108');
INSERT INTO `t_helper` VALUES ('109');
INSERT INTO `t_helper` VALUES ('110');
INSERT INTO `t_helper` VALUES ('111');
INSERT INTO `t_helper` VALUES ('112');
INSERT INTO `t_helper` VALUES ('113');
INSERT INTO `t_helper` VALUES ('114');
INSERT INTO `t_helper` VALUES ('115');
INSERT INTO `t_helper` VALUES ('116');
INSERT INTO `t_helper` VALUES ('117');
INSERT INTO `t_helper` VALUES ('118');
INSERT INTO `t_helper` VALUES ('119');
INSERT INTO `t_helper` VALUES ('120');
INSERT INTO `t_helper` VALUES ('121');
INSERT INTO `t_helper` VALUES ('122');
INSERT INTO `t_helper` VALUES ('123');
INSERT INTO `t_helper` VALUES ('124');
INSERT INTO `t_helper` VALUES ('125');
INSERT INTO `t_helper` VALUES ('126');
INSERT INTO `t_helper` VALUES ('127');
INSERT INTO `t_helper` VALUES ('128');
INSERT INTO `t_helper` VALUES ('129');
INSERT INTO `t_helper` VALUES ('130');
INSERT INTO `t_helper` VALUES ('131');
INSERT INTO `t_helper` VALUES ('132');
INSERT INTO `t_helper` VALUES ('133');
INSERT INTO `t_helper` VALUES ('134');
INSERT INTO `t_helper` VALUES ('135');
INSERT INTO `t_helper` VALUES ('136');
INSERT INTO `t_helper` VALUES ('137');
INSERT INTO `t_helper` VALUES ('138');
INSERT INTO `t_helper` VALUES ('139');
INSERT INTO `t_helper` VALUES ('140');
INSERT INTO `t_helper` VALUES ('141');
INSERT INTO `t_helper` VALUES ('142');
INSERT INTO `t_helper` VALUES ('143');
INSERT INTO `t_helper` VALUES ('144');
INSERT INTO `t_helper` VALUES ('145');
INSERT INTO `t_helper` VALUES ('146');
INSERT INTO `t_helper` VALUES ('147');
INSERT INTO `t_helper` VALUES ('148');
INSERT INTO `t_helper` VALUES ('149');
INSERT INTO `t_helper` VALUES ('150');
INSERT INTO `t_helper` VALUES ('151');
INSERT INTO `t_helper` VALUES ('152');
INSERT INTO `t_helper` VALUES ('153');
INSERT INTO `t_helper` VALUES ('154');
INSERT INTO `t_helper` VALUES ('155');
INSERT INTO `t_helper` VALUES ('156');
INSERT INTO `t_helper` VALUES ('157');
INSERT INTO `t_helper` VALUES ('158');
INSERT INTO `t_helper` VALUES ('159');
INSERT INTO `t_helper` VALUES ('160');
INSERT INTO `t_helper` VALUES ('161');
INSERT INTO `t_helper` VALUES ('162');
INSERT INTO `t_helper` VALUES ('163');
INSERT INTO `t_helper` VALUES ('164');
INSERT INTO `t_helper` VALUES ('165');
INSERT INTO `t_helper` VALUES ('166');
INSERT INTO `t_helper` VALUES ('167');
INSERT INTO `t_helper` VALUES ('168');
INSERT INTO `t_helper` VALUES ('169');
INSERT INTO `t_helper` VALUES ('170');
INSERT INTO `t_helper` VALUES ('171');
INSERT INTO `t_helper` VALUES ('172');
INSERT INTO `t_helper` VALUES ('173');
INSERT INTO `t_helper` VALUES ('174');
INSERT INTO `t_helper` VALUES ('175');
INSERT INTO `t_helper` VALUES ('176');
INSERT INTO `t_helper` VALUES ('177');
INSERT INTO `t_helper` VALUES ('178');
INSERT INTO `t_helper` VALUES ('179');
INSERT INTO `t_helper` VALUES ('180');
INSERT INTO `t_helper` VALUES ('181');
INSERT INTO `t_helper` VALUES ('182');
INSERT INTO `t_helper` VALUES ('183');
INSERT INTO `t_helper` VALUES ('184');
INSERT INTO `t_helper` VALUES ('185');
INSERT INTO `t_helper` VALUES ('186');
INSERT INTO `t_helper` VALUES ('187');
INSERT INTO `t_helper` VALUES ('188');
INSERT INTO `t_helper` VALUES ('189');
INSERT INTO `t_helper` VALUES ('190');
INSERT INTO `t_helper` VALUES ('191');
INSERT INTO `t_helper` VALUES ('192');
INSERT INTO `t_helper` VALUES ('193');
INSERT INTO `t_helper` VALUES ('194');
INSERT INTO `t_helper` VALUES ('195');
INSERT INTO `t_helper` VALUES ('196');
INSERT INTO `t_helper` VALUES ('197');
INSERT INTO `t_helper` VALUES ('198');
INSERT INTO `t_helper` VALUES ('199');
INSERT INTO `t_helper` VALUES ('200');
INSERT INTO `t_helper` VALUES ('201');
INSERT INTO `t_helper` VALUES ('202');
INSERT INTO `t_helper` VALUES ('203');
INSERT INTO `t_helper` VALUES ('204');
INSERT INTO `t_helper` VALUES ('205');
INSERT INTO `t_helper` VALUES ('206');
INSERT INTO `t_helper` VALUES ('207');
INSERT INTO `t_helper` VALUES ('208');
INSERT INTO `t_helper` VALUES ('209');
INSERT INTO `t_helper` VALUES ('210');
INSERT INTO `t_helper` VALUES ('211');
INSERT INTO `t_helper` VALUES ('212');
INSERT INTO `t_helper` VALUES ('213');
INSERT INTO `t_helper` VALUES ('214');
INSERT INTO `t_helper` VALUES ('215');
INSERT INTO `t_helper` VALUES ('216');
INSERT INTO `t_helper` VALUES ('217');
INSERT INTO `t_helper` VALUES ('218');
INSERT INTO `t_helper` VALUES ('219');
INSERT INTO `t_helper` VALUES ('220');
INSERT INTO `t_helper` VALUES ('221');
INSERT INTO `t_helper` VALUES ('222');
INSERT INTO `t_helper` VALUES ('223');
INSERT INTO `t_helper` VALUES ('224');
INSERT INTO `t_helper` VALUES ('225');
INSERT INTO `t_helper` VALUES ('226');
INSERT INTO `t_helper` VALUES ('227');
INSERT INTO `t_helper` VALUES ('228');
INSERT INTO `t_helper` VALUES ('229');
INSERT INTO `t_helper` VALUES ('230');
INSERT INTO `t_helper` VALUES ('231');
INSERT INTO `t_helper` VALUES ('232');
INSERT INTO `t_helper` VALUES ('233');
INSERT INTO `t_helper` VALUES ('234');
INSERT INTO `t_helper` VALUES ('235');
INSERT INTO `t_helper` VALUES ('236');
INSERT INTO `t_helper` VALUES ('237');
INSERT INTO `t_helper` VALUES ('238');
INSERT INTO `t_helper` VALUES ('239');
INSERT INTO `t_helper` VALUES ('240');
INSERT INTO `t_helper` VALUES ('241');
INSERT INTO `t_helper` VALUES ('242');
INSERT INTO `t_helper` VALUES ('243');
INSERT INTO `t_helper` VALUES ('244');
INSERT INTO `t_helper` VALUES ('245');
INSERT INTO `t_helper` VALUES ('246');
INSERT INTO `t_helper` VALUES ('247');
INSERT INTO `t_helper` VALUES ('248');
INSERT INTO `t_helper` VALUES ('249');
INSERT INTO `t_helper` VALUES ('250');
INSERT INTO `t_helper` VALUES ('251');
INSERT INTO `t_helper` VALUES ('252');
INSERT INTO `t_helper` VALUES ('253');
INSERT INTO `t_helper` VALUES ('254');
INSERT INTO `t_helper` VALUES ('255');
INSERT INTO `t_helper` VALUES ('256');
INSERT INTO `t_helper` VALUES ('257');
INSERT INTO `t_helper` VALUES ('258');
INSERT INTO `t_helper` VALUES ('259');
INSERT INTO `t_helper` VALUES ('260');
INSERT INTO `t_helper` VALUES ('261');
INSERT INTO `t_helper` VALUES ('262');
INSERT INTO `t_helper` VALUES ('263');
INSERT INTO `t_helper` VALUES ('264');
INSERT INTO `t_helper` VALUES ('265');
INSERT INTO `t_helper` VALUES ('266');
INSERT INTO `t_helper` VALUES ('267');
INSERT INTO `t_helper` VALUES ('268');
INSERT INTO `t_helper` VALUES ('269');
INSERT INTO `t_helper` VALUES ('270');
INSERT INTO `t_helper` VALUES ('271');
INSERT INTO `t_helper` VALUES ('272');
INSERT INTO `t_helper` VALUES ('273');
INSERT INTO `t_helper` VALUES ('274');
INSERT INTO `t_helper` VALUES ('275');
INSERT INTO `t_helper` VALUES ('276');
INSERT INTO `t_helper` VALUES ('277');
INSERT INTO `t_helper` VALUES ('278');
INSERT INTO `t_helper` VALUES ('279');
INSERT INTO `t_helper` VALUES ('280');
INSERT INTO `t_helper` VALUES ('281');
INSERT INTO `t_helper` VALUES ('282');
INSERT INTO `t_helper` VALUES ('283');
INSERT INTO `t_helper` VALUES ('284');
INSERT INTO `t_helper` VALUES ('285');
INSERT INTO `t_helper` VALUES ('286');
INSERT INTO `t_helper` VALUES ('287');
INSERT INTO `t_helper` VALUES ('288');
INSERT INTO `t_helper` VALUES ('289');
INSERT INTO `t_helper` VALUES ('290');
INSERT INTO `t_helper` VALUES ('291');
INSERT INTO `t_helper` VALUES ('292');
INSERT INTO `t_helper` VALUES ('293');
INSERT INTO `t_helper` VALUES ('294');
INSERT INTO `t_helper` VALUES ('295');
INSERT INTO `t_helper` VALUES ('296');
INSERT INTO `t_helper` VALUES ('297');
INSERT INTO `t_helper` VALUES ('298');
INSERT INTO `t_helper` VALUES ('299');
INSERT INTO `t_helper` VALUES ('300');
INSERT INTO `t_helper` VALUES ('301');
INSERT INTO `t_helper` VALUES ('302');
INSERT INTO `t_helper` VALUES ('303');
INSERT INTO `t_helper` VALUES ('304');
INSERT INTO `t_helper` VALUES ('305');
INSERT INTO `t_helper` VALUES ('306');
INSERT INTO `t_helper` VALUES ('307');
INSERT INTO `t_helper` VALUES ('308');
INSERT INTO `t_helper` VALUES ('309');
INSERT INTO `t_helper` VALUES ('310');
INSERT INTO `t_helper` VALUES ('311');
INSERT INTO `t_helper` VALUES ('312');
INSERT INTO `t_helper` VALUES ('313');
INSERT INTO `t_helper` VALUES ('314');
INSERT INTO `t_helper` VALUES ('315');
INSERT INTO `t_helper` VALUES ('316');
INSERT INTO `t_helper` VALUES ('317');
INSERT INTO `t_helper` VALUES ('318');
INSERT INTO `t_helper` VALUES ('319');
INSERT INTO `t_helper` VALUES ('320');
INSERT INTO `t_helper` VALUES ('321');
INSERT INTO `t_helper` VALUES ('322');
INSERT INTO `t_helper` VALUES ('323');
INSERT INTO `t_helper` VALUES ('324');
INSERT INTO `t_helper` VALUES ('325');
INSERT INTO `t_helper` VALUES ('326');
INSERT INTO `t_helper` VALUES ('327');
INSERT INTO `t_helper` VALUES ('328');
INSERT INTO `t_helper` VALUES ('329');
INSERT INTO `t_helper` VALUES ('330');
INSERT INTO `t_helper` VALUES ('331');
INSERT INTO `t_helper` VALUES ('332');
INSERT INTO `t_helper` VALUES ('333');
INSERT INTO `t_helper` VALUES ('334');
INSERT INTO `t_helper` VALUES ('335');
INSERT INTO `t_helper` VALUES ('336');
INSERT INTO `t_helper` VALUES ('337');
INSERT INTO `t_helper` VALUES ('338');
INSERT INTO `t_helper` VALUES ('339');
INSERT INTO `t_helper` VALUES ('340');
INSERT INTO `t_helper` VALUES ('341');
INSERT INTO `t_helper` VALUES ('342');
INSERT INTO `t_helper` VALUES ('343');
INSERT INTO `t_helper` VALUES ('344');
INSERT INTO `t_helper` VALUES ('345');
INSERT INTO `t_helper` VALUES ('346');
INSERT INTO `t_helper` VALUES ('347');
INSERT INTO `t_helper` VALUES ('348');
INSERT INTO `t_helper` VALUES ('349');
INSERT INTO `t_helper` VALUES ('350');
INSERT INTO `t_helper` VALUES ('351');
INSERT INTO `t_helper` VALUES ('352');
INSERT INTO `t_helper` VALUES ('353');
INSERT INTO `t_helper` VALUES ('354');
INSERT INTO `t_helper` VALUES ('355');
INSERT INTO `t_helper` VALUES ('356');
INSERT INTO `t_helper` VALUES ('357');
INSERT INTO `t_helper` VALUES ('358');
INSERT INTO `t_helper` VALUES ('359');
INSERT INTO `t_helper` VALUES ('360');
INSERT INTO `t_helper` VALUES ('361');
INSERT INTO `t_helper` VALUES ('362');
INSERT INTO `t_helper` VALUES ('363');
INSERT INTO `t_helper` VALUES ('364');
INSERT INTO `t_helper` VALUES ('365');
INSERT INTO `t_helper` VALUES ('366');
INSERT INTO `t_helper` VALUES ('367');
INSERT INTO `t_helper` VALUES ('368');
INSERT INTO `t_helper` VALUES ('369');
INSERT INTO `t_helper` VALUES ('370');
INSERT INTO `t_helper` VALUES ('371');
INSERT INTO `t_helper` VALUES ('372');
INSERT INTO `t_helper` VALUES ('373');
INSERT INTO `t_helper` VALUES ('374');
INSERT INTO `t_helper` VALUES ('375');
INSERT INTO `t_helper` VALUES ('376');
INSERT INTO `t_helper` VALUES ('377');
INSERT INTO `t_helper` VALUES ('378');
INSERT INTO `t_helper` VALUES ('379');
INSERT INTO `t_helper` VALUES ('380');
INSERT INTO `t_helper` VALUES ('381');
INSERT INTO `t_helper` VALUES ('382');
INSERT INTO `t_helper` VALUES ('383');
INSERT INTO `t_helper` VALUES ('384');
INSERT INTO `t_helper` VALUES ('385');
INSERT INTO `t_helper` VALUES ('386');
INSERT INTO `t_helper` VALUES ('387');
INSERT INTO `t_helper` VALUES ('388');
INSERT INTO `t_helper` VALUES ('389');
INSERT INTO `t_helper` VALUES ('390');
INSERT INTO `t_helper` VALUES ('391');
INSERT INTO `t_helper` VALUES ('392');
INSERT INTO `t_helper` VALUES ('393');
INSERT INTO `t_helper` VALUES ('394');
INSERT INTO `t_helper` VALUES ('395');
INSERT INTO `t_helper` VALUES ('396');
INSERT INTO `t_helper` VALUES ('397');
INSERT INTO `t_helper` VALUES ('398');
INSERT INTO `t_helper` VALUES ('399');
INSERT INTO `t_helper` VALUES ('400');
INSERT INTO `t_helper` VALUES ('401');
INSERT INTO `t_helper` VALUES ('402');
INSERT INTO `t_helper` VALUES ('403');
INSERT INTO `t_helper` VALUES ('404');
INSERT INTO `t_helper` VALUES ('405');
INSERT INTO `t_helper` VALUES ('406');
INSERT INTO `t_helper` VALUES ('407');
INSERT INTO `t_helper` VALUES ('408');
INSERT INTO `t_helper` VALUES ('409');
INSERT INTO `t_helper` VALUES ('410');
INSERT INTO `t_helper` VALUES ('411');
INSERT INTO `t_helper` VALUES ('412');
INSERT INTO `t_helper` VALUES ('413');
INSERT INTO `t_helper` VALUES ('414');
INSERT INTO `t_helper` VALUES ('415');
INSERT INTO `t_helper` VALUES ('416');
INSERT INTO `t_helper` VALUES ('417');
INSERT INTO `t_helper` VALUES ('418');
INSERT INTO `t_helper` VALUES ('419');
INSERT INTO `t_helper` VALUES ('420');
INSERT INTO `t_helper` VALUES ('421');
INSERT INTO `t_helper` VALUES ('422');
INSERT INTO `t_helper` VALUES ('423');
INSERT INTO `t_helper` VALUES ('424');
INSERT INTO `t_helper` VALUES ('425');
INSERT INTO `t_helper` VALUES ('426');
INSERT INTO `t_helper` VALUES ('427');
INSERT INTO `t_helper` VALUES ('428');
INSERT INTO `t_helper` VALUES ('429');
INSERT INTO `t_helper` VALUES ('430');
INSERT INTO `t_helper` VALUES ('431');
INSERT INTO `t_helper` VALUES ('432');
INSERT INTO `t_helper` VALUES ('433');
INSERT INTO `t_helper` VALUES ('434');
INSERT INTO `t_helper` VALUES ('435');
INSERT INTO `t_helper` VALUES ('436');
INSERT INTO `t_helper` VALUES ('437');
INSERT INTO `t_helper` VALUES ('438');
INSERT INTO `t_helper` VALUES ('439');
INSERT INTO `t_helper` VALUES ('440');
INSERT INTO `t_helper` VALUES ('441');
INSERT INTO `t_helper` VALUES ('442');
INSERT INTO `t_helper` VALUES ('443');
INSERT INTO `t_helper` VALUES ('444');
INSERT INTO `t_helper` VALUES ('445');
INSERT INTO `t_helper` VALUES ('446');
INSERT INTO `t_helper` VALUES ('447');
INSERT INTO `t_helper` VALUES ('448');
INSERT INTO `t_helper` VALUES ('449');
INSERT INTO `t_helper` VALUES ('450');
INSERT INTO `t_helper` VALUES ('451');
INSERT INTO `t_helper` VALUES ('452');
INSERT INTO `t_helper` VALUES ('453');
INSERT INTO `t_helper` VALUES ('454');
INSERT INTO `t_helper` VALUES ('455');
INSERT INTO `t_helper` VALUES ('456');
INSERT INTO `t_helper` VALUES ('457');
INSERT INTO `t_helper` VALUES ('458');
INSERT INTO `t_helper` VALUES ('459');
INSERT INTO `t_helper` VALUES ('460');
INSERT INTO `t_helper` VALUES ('461');
INSERT INTO `t_helper` VALUES ('462');
INSERT INTO `t_helper` VALUES ('463');
INSERT INTO `t_helper` VALUES ('464');
INSERT INTO `t_helper` VALUES ('465');
INSERT INTO `t_helper` VALUES ('466');
INSERT INTO `t_helper` VALUES ('467');
INSERT INTO `t_helper` VALUES ('468');
INSERT INTO `t_helper` VALUES ('469');
INSERT INTO `t_helper` VALUES ('470');
INSERT INTO `t_helper` VALUES ('471');
INSERT INTO `t_helper` VALUES ('472');
INSERT INTO `t_helper` VALUES ('473');
INSERT INTO `t_helper` VALUES ('474');
INSERT INTO `t_helper` VALUES ('475');
INSERT INTO `t_helper` VALUES ('476');
INSERT INTO `t_helper` VALUES ('477');
INSERT INTO `t_helper` VALUES ('478');
INSERT INTO `t_helper` VALUES ('479');
INSERT INTO `t_helper` VALUES ('480');
INSERT INTO `t_helper` VALUES ('481');
INSERT INTO `t_helper` VALUES ('482');
INSERT INTO `t_helper` VALUES ('483');
INSERT INTO `t_helper` VALUES ('484');
INSERT INTO `t_helper` VALUES ('485');
INSERT INTO `t_helper` VALUES ('486');
INSERT INTO `t_helper` VALUES ('487');
INSERT INTO `t_helper` VALUES ('488');
INSERT INTO `t_helper` VALUES ('489');
INSERT INTO `t_helper` VALUES ('490');
INSERT INTO `t_helper` VALUES ('491');
INSERT INTO `t_helper` VALUES ('492');
INSERT INTO `t_helper` VALUES ('493');
INSERT INTO `t_helper` VALUES ('494');
INSERT INTO `t_helper` VALUES ('495');
INSERT INTO `t_helper` VALUES ('496');
INSERT INTO `t_helper` VALUES ('497');
INSERT INTO `t_helper` VALUES ('498');
INSERT INTO `t_helper` VALUES ('499');
INSERT INTO `t_helper` VALUES ('500');
INSERT INTO `t_helper` VALUES ('501');
INSERT INTO `t_helper` VALUES ('502');
INSERT INTO `t_helper` VALUES ('503');
INSERT INTO `t_helper` VALUES ('504');
INSERT INTO `t_helper` VALUES ('505');
INSERT INTO `t_helper` VALUES ('506');
INSERT INTO `t_helper` VALUES ('507');
INSERT INTO `t_helper` VALUES ('508');
INSERT INTO `t_helper` VALUES ('509');
INSERT INTO `t_helper` VALUES ('510');
INSERT INTO `t_helper` VALUES ('511');
INSERT INTO `t_helper` VALUES ('512');
INSERT INTO `t_helper` VALUES ('513');
INSERT INTO `t_helper` VALUES ('514');
INSERT INTO `t_helper` VALUES ('515');
INSERT INTO `t_helper` VALUES ('516');
INSERT INTO `t_helper` VALUES ('517');
INSERT INTO `t_helper` VALUES ('518');
INSERT INTO `t_helper` VALUES ('519');
INSERT INTO `t_helper` VALUES ('520');
INSERT INTO `t_helper` VALUES ('521');
INSERT INTO `t_helper` VALUES ('522');
INSERT INTO `t_helper` VALUES ('523');
INSERT INTO `t_helper` VALUES ('524');
INSERT INTO `t_helper` VALUES ('525');
INSERT INTO `t_helper` VALUES ('526');
INSERT INTO `t_helper` VALUES ('527');
INSERT INTO `t_helper` VALUES ('528');
INSERT INTO `t_helper` VALUES ('529');
INSERT INTO `t_helper` VALUES ('530');
INSERT INTO `t_helper` VALUES ('531');
INSERT INTO `t_helper` VALUES ('532');
INSERT INTO `t_helper` VALUES ('533');
INSERT INTO `t_helper` VALUES ('534');
INSERT INTO `t_helper` VALUES ('535');
INSERT INTO `t_helper` VALUES ('536');
INSERT INTO `t_helper` VALUES ('537');
INSERT INTO `t_helper` VALUES ('538');
INSERT INTO `t_helper` VALUES ('539');
INSERT INTO `t_helper` VALUES ('540');
INSERT INTO `t_helper` VALUES ('541');
INSERT INTO `t_helper` VALUES ('542');
INSERT INTO `t_helper` VALUES ('543');
INSERT INTO `t_helper` VALUES ('544');
INSERT INTO `t_helper` VALUES ('545');
INSERT INTO `t_helper` VALUES ('546');
INSERT INTO `t_helper` VALUES ('547');
INSERT INTO `t_helper` VALUES ('548');
INSERT INTO `t_helper` VALUES ('549');
INSERT INTO `t_helper` VALUES ('550');
INSERT INTO `t_helper` VALUES ('551');
INSERT INTO `t_helper` VALUES ('552');
INSERT INTO `t_helper` VALUES ('553');
INSERT INTO `t_helper` VALUES ('554');
INSERT INTO `t_helper` VALUES ('555');
INSERT INTO `t_helper` VALUES ('556');
INSERT INTO `t_helper` VALUES ('557');
INSERT INTO `t_helper` VALUES ('558');
INSERT INTO `t_helper` VALUES ('559');
INSERT INTO `t_helper` VALUES ('560');
INSERT INTO `t_helper` VALUES ('561');
INSERT INTO `t_helper` VALUES ('562');
INSERT INTO `t_helper` VALUES ('563');
INSERT INTO `t_helper` VALUES ('564');
INSERT INTO `t_helper` VALUES ('565');
INSERT INTO `t_helper` VALUES ('566');
INSERT INTO `t_helper` VALUES ('567');
INSERT INTO `t_helper` VALUES ('568');
INSERT INTO `t_helper` VALUES ('569');
INSERT INTO `t_helper` VALUES ('570');
INSERT INTO `t_helper` VALUES ('571');
INSERT INTO `t_helper` VALUES ('572');
INSERT INTO `t_helper` VALUES ('573');
INSERT INTO `t_helper` VALUES ('574');
INSERT INTO `t_helper` VALUES ('575');
INSERT INTO `t_helper` VALUES ('576');
INSERT INTO `t_helper` VALUES ('577');
INSERT INTO `t_helper` VALUES ('578');
INSERT INTO `t_helper` VALUES ('579');
INSERT INTO `t_helper` VALUES ('580');
INSERT INTO `t_helper` VALUES ('581');
INSERT INTO `t_helper` VALUES ('582');
INSERT INTO `t_helper` VALUES ('583');
INSERT INTO `t_helper` VALUES ('584');
INSERT INTO `t_helper` VALUES ('585');
INSERT INTO `t_helper` VALUES ('586');
INSERT INTO `t_helper` VALUES ('587');
INSERT INTO `t_helper` VALUES ('588');
INSERT INTO `t_helper` VALUES ('589');
INSERT INTO `t_helper` VALUES ('590');
INSERT INTO `t_helper` VALUES ('591');
INSERT INTO `t_helper` VALUES ('592');
INSERT INTO `t_helper` VALUES ('593');
INSERT INTO `t_helper` VALUES ('594');
INSERT INTO `t_helper` VALUES ('595');
INSERT INTO `t_helper` VALUES ('596');
INSERT INTO `t_helper` VALUES ('597');
INSERT INTO `t_helper` VALUES ('598');
INSERT INTO `t_helper` VALUES ('599');
INSERT INTO `t_helper` VALUES ('600');
INSERT INTO `t_helper` VALUES ('601');
INSERT INTO `t_helper` VALUES ('602');
INSERT INTO `t_helper` VALUES ('603');
INSERT INTO `t_helper` VALUES ('604');
INSERT INTO `t_helper` VALUES ('605');
INSERT INTO `t_helper` VALUES ('606');
INSERT INTO `t_helper` VALUES ('607');
INSERT INTO `t_helper` VALUES ('608');
INSERT INTO `t_helper` VALUES ('609');
INSERT INTO `t_helper` VALUES ('610');
INSERT INTO `t_helper` VALUES ('611');
INSERT INTO `t_helper` VALUES ('612');
INSERT INTO `t_helper` VALUES ('613');
INSERT INTO `t_helper` VALUES ('614');
INSERT INTO `t_helper` VALUES ('615');
INSERT INTO `t_helper` VALUES ('616');
INSERT INTO `t_helper` VALUES ('617');
INSERT INTO `t_helper` VALUES ('618');
INSERT INTO `t_helper` VALUES ('619');
INSERT INTO `t_helper` VALUES ('620');
INSERT INTO `t_helper` VALUES ('621');
INSERT INTO `t_helper` VALUES ('622');
INSERT INTO `t_helper` VALUES ('623');
INSERT INTO `t_helper` VALUES ('624');
INSERT INTO `t_helper` VALUES ('625');
INSERT INTO `t_helper` VALUES ('626');
INSERT INTO `t_helper` VALUES ('627');
INSERT INTO `t_helper` VALUES ('628');
INSERT INTO `t_helper` VALUES ('629');
INSERT INTO `t_helper` VALUES ('630');
INSERT INTO `t_helper` VALUES ('631');
INSERT INTO `t_helper` VALUES ('632');
INSERT INTO `t_helper` VALUES ('633');
INSERT INTO `t_helper` VALUES ('634');
INSERT INTO `t_helper` VALUES ('635');
INSERT INTO `t_helper` VALUES ('636');
INSERT INTO `t_helper` VALUES ('637');
INSERT INTO `t_helper` VALUES ('638');
INSERT INTO `t_helper` VALUES ('639');
INSERT INTO `t_helper` VALUES ('640');
INSERT INTO `t_helper` VALUES ('641');
INSERT INTO `t_helper` VALUES ('642');
INSERT INTO `t_helper` VALUES ('643');
INSERT INTO `t_helper` VALUES ('644');
INSERT INTO `t_helper` VALUES ('645');
INSERT INTO `t_helper` VALUES ('646');
INSERT INTO `t_helper` VALUES ('647');
INSERT INTO `t_helper` VALUES ('648');
INSERT INTO `t_helper` VALUES ('649');
INSERT INTO `t_helper` VALUES ('650');
INSERT INTO `t_helper` VALUES ('651');
INSERT INTO `t_helper` VALUES ('652');
INSERT INTO `t_helper` VALUES ('653');
INSERT INTO `t_helper` VALUES ('654');
INSERT INTO `t_helper` VALUES ('655');
INSERT INTO `t_helper` VALUES ('656');
INSERT INTO `t_helper` VALUES ('657');
INSERT INTO `t_helper` VALUES ('658');
INSERT INTO `t_helper` VALUES ('659');
INSERT INTO `t_helper` VALUES ('660');
INSERT INTO `t_helper` VALUES ('661');
INSERT INTO `t_helper` VALUES ('662');
INSERT INTO `t_helper` VALUES ('663');
INSERT INTO `t_helper` VALUES ('664');
INSERT INTO `t_helper` VALUES ('665');
INSERT INTO `t_helper` VALUES ('666');
INSERT INTO `t_helper` VALUES ('667');
INSERT INTO `t_helper` VALUES ('668');
INSERT INTO `t_helper` VALUES ('669');
INSERT INTO `t_helper` VALUES ('670');
INSERT INTO `t_helper` VALUES ('671');
INSERT INTO `t_helper` VALUES ('672');
INSERT INTO `t_helper` VALUES ('673');
INSERT INTO `t_helper` VALUES ('674');
INSERT INTO `t_helper` VALUES ('675');
INSERT INTO `t_helper` VALUES ('676');
INSERT INTO `t_helper` VALUES ('677');
INSERT INTO `t_helper` VALUES ('678');
INSERT INTO `t_helper` VALUES ('679');
INSERT INTO `t_helper` VALUES ('680');
INSERT INTO `t_helper` VALUES ('681');
INSERT INTO `t_helper` VALUES ('682');
INSERT INTO `t_helper` VALUES ('683');
INSERT INTO `t_helper` VALUES ('684');
INSERT INTO `t_helper` VALUES ('685');
INSERT INTO `t_helper` VALUES ('686');
INSERT INTO `t_helper` VALUES ('687');
INSERT INTO `t_helper` VALUES ('688');
INSERT INTO `t_helper` VALUES ('689');
INSERT INTO `t_helper` VALUES ('690');
INSERT INTO `t_helper` VALUES ('691');
INSERT INTO `t_helper` VALUES ('692');
INSERT INTO `t_helper` VALUES ('693');
INSERT INTO `t_helper` VALUES ('694');
INSERT INTO `t_helper` VALUES ('695');
INSERT INTO `t_helper` VALUES ('696');
INSERT INTO `t_helper` VALUES ('697');
INSERT INTO `t_helper` VALUES ('698');
INSERT INTO `t_helper` VALUES ('699');
INSERT INTO `t_helper` VALUES ('700');
INSERT INTO `t_helper` VALUES ('701');
INSERT INTO `t_helper` VALUES ('702');
INSERT INTO `t_helper` VALUES ('703');
INSERT INTO `t_helper` VALUES ('704');
INSERT INTO `t_helper` VALUES ('705');
INSERT INTO `t_helper` VALUES ('706');
INSERT INTO `t_helper` VALUES ('707');
INSERT INTO `t_helper` VALUES ('708');
INSERT INTO `t_helper` VALUES ('709');
INSERT INTO `t_helper` VALUES ('710');
INSERT INTO `t_helper` VALUES ('711');
INSERT INTO `t_helper` VALUES ('712');
INSERT INTO `t_helper` VALUES ('713');
INSERT INTO `t_helper` VALUES ('714');
INSERT INTO `t_helper` VALUES ('715');
INSERT INTO `t_helper` VALUES ('716');
INSERT INTO `t_helper` VALUES ('717');
INSERT INTO `t_helper` VALUES ('718');
INSERT INTO `t_helper` VALUES ('719');
INSERT INTO `t_helper` VALUES ('720');
INSERT INTO `t_helper` VALUES ('721');
INSERT INTO `t_helper` VALUES ('722');
INSERT INTO `t_helper` VALUES ('723');
INSERT INTO `t_helper` VALUES ('724');
INSERT INTO `t_helper` VALUES ('725');
INSERT INTO `t_helper` VALUES ('726');
INSERT INTO `t_helper` VALUES ('727');
INSERT INTO `t_helper` VALUES ('728');
INSERT INTO `t_helper` VALUES ('729');
INSERT INTO `t_helper` VALUES ('730');
INSERT INTO `t_helper` VALUES ('731');
INSERT INTO `t_helper` VALUES ('732');
INSERT INTO `t_helper` VALUES ('733');
INSERT INTO `t_helper` VALUES ('734');
INSERT INTO `t_helper` VALUES ('735');
INSERT INTO `t_helper` VALUES ('736');
INSERT INTO `t_helper` VALUES ('737');
INSERT INTO `t_helper` VALUES ('738');
INSERT INTO `t_helper` VALUES ('739');
INSERT INTO `t_helper` VALUES ('740');
INSERT INTO `t_helper` VALUES ('741');
INSERT INTO `t_helper` VALUES ('742');
INSERT INTO `t_helper` VALUES ('743');
INSERT INTO `t_helper` VALUES ('744');
INSERT INTO `t_helper` VALUES ('745');
INSERT INTO `t_helper` VALUES ('746');
INSERT INTO `t_helper` VALUES ('747');
INSERT INTO `t_helper` VALUES ('748');
INSERT INTO `t_helper` VALUES ('749');
INSERT INTO `t_helper` VALUES ('750');
INSERT INTO `t_helper` VALUES ('751');
INSERT INTO `t_helper` VALUES ('752');
INSERT INTO `t_helper` VALUES ('753');
INSERT INTO `t_helper` VALUES ('754');
INSERT INTO `t_helper` VALUES ('755');
INSERT INTO `t_helper` VALUES ('756');
INSERT INTO `t_helper` VALUES ('757');
INSERT INTO `t_helper` VALUES ('758');
INSERT INTO `t_helper` VALUES ('759');
INSERT INTO `t_helper` VALUES ('760');
INSERT INTO `t_helper` VALUES ('761');
INSERT INTO `t_helper` VALUES ('762');
INSERT INTO `t_helper` VALUES ('763');
INSERT INTO `t_helper` VALUES ('764');
INSERT INTO `t_helper` VALUES ('765');
INSERT INTO `t_helper` VALUES ('766');
INSERT INTO `t_helper` VALUES ('767');
INSERT INTO `t_helper` VALUES ('768');
INSERT INTO `t_helper` VALUES ('769');
INSERT INTO `t_helper` VALUES ('770');
INSERT INTO `t_helper` VALUES ('771');
INSERT INTO `t_helper` VALUES ('772');
INSERT INTO `t_helper` VALUES ('773');
INSERT INTO `t_helper` VALUES ('774');
INSERT INTO `t_helper` VALUES ('775');
INSERT INTO `t_helper` VALUES ('776');
INSERT INTO `t_helper` VALUES ('777');
INSERT INTO `t_helper` VALUES ('778');
INSERT INTO `t_helper` VALUES ('779');
INSERT INTO `t_helper` VALUES ('780');
INSERT INTO `t_helper` VALUES ('781');
INSERT INTO `t_helper` VALUES ('782');
INSERT INTO `t_helper` VALUES ('783');
INSERT INTO `t_helper` VALUES ('784');
INSERT INTO `t_helper` VALUES ('785');
INSERT INTO `t_helper` VALUES ('786');
INSERT INTO `t_helper` VALUES ('787');
INSERT INTO `t_helper` VALUES ('788');
INSERT INTO `t_helper` VALUES ('789');
INSERT INTO `t_helper` VALUES ('790');
INSERT INTO `t_helper` VALUES ('791');
INSERT INTO `t_helper` VALUES ('792');
INSERT INTO `t_helper` VALUES ('793');
INSERT INTO `t_helper` VALUES ('794');
INSERT INTO `t_helper` VALUES ('795');
INSERT INTO `t_helper` VALUES ('796');
INSERT INTO `t_helper` VALUES ('797');
INSERT INTO `t_helper` VALUES ('798');
INSERT INTO `t_helper` VALUES ('799');
INSERT INTO `t_helper` VALUES ('800');
INSERT INTO `t_helper` VALUES ('801');
INSERT INTO `t_helper` VALUES ('802');
INSERT INTO `t_helper` VALUES ('803');
INSERT INTO `t_helper` VALUES ('804');
INSERT INTO `t_helper` VALUES ('805');
INSERT INTO `t_helper` VALUES ('806');
INSERT INTO `t_helper` VALUES ('807');
INSERT INTO `t_helper` VALUES ('808');
INSERT INTO `t_helper` VALUES ('809');
INSERT INTO `t_helper` VALUES ('810');
INSERT INTO `t_helper` VALUES ('811');
INSERT INTO `t_helper` VALUES ('812');
INSERT INTO `t_helper` VALUES ('813');
INSERT INTO `t_helper` VALUES ('814');
INSERT INTO `t_helper` VALUES ('815');
INSERT INTO `t_helper` VALUES ('816');
INSERT INTO `t_helper` VALUES ('817');
INSERT INTO `t_helper` VALUES ('818');
INSERT INTO `t_helper` VALUES ('819');
INSERT INTO `t_helper` VALUES ('820');
INSERT INTO `t_helper` VALUES ('821');
INSERT INTO `t_helper` VALUES ('822');
INSERT INTO `t_helper` VALUES ('823');
INSERT INTO `t_helper` VALUES ('824');
INSERT INTO `t_helper` VALUES ('825');
INSERT INTO `t_helper` VALUES ('826');
INSERT INTO `t_helper` VALUES ('827');
INSERT INTO `t_helper` VALUES ('828');
INSERT INTO `t_helper` VALUES ('829');
INSERT INTO `t_helper` VALUES ('830');
INSERT INTO `t_helper` VALUES ('831');
INSERT INTO `t_helper` VALUES ('832');
INSERT INTO `t_helper` VALUES ('833');
INSERT INTO `t_helper` VALUES ('834');
INSERT INTO `t_helper` VALUES ('835');
INSERT INTO `t_helper` VALUES ('836');
INSERT INTO `t_helper` VALUES ('837');
INSERT INTO `t_helper` VALUES ('838');
INSERT INTO `t_helper` VALUES ('839');
INSERT INTO `t_helper` VALUES ('840');
INSERT INTO `t_helper` VALUES ('841');
INSERT INTO `t_helper` VALUES ('842');
INSERT INTO `t_helper` VALUES ('843');
INSERT INTO `t_helper` VALUES ('844');
INSERT INTO `t_helper` VALUES ('845');
INSERT INTO `t_helper` VALUES ('846');
INSERT INTO `t_helper` VALUES ('847');
INSERT INTO `t_helper` VALUES ('848');
INSERT INTO `t_helper` VALUES ('849');
INSERT INTO `t_helper` VALUES ('850');
INSERT INTO `t_helper` VALUES ('851');
INSERT INTO `t_helper` VALUES ('852');
INSERT INTO `t_helper` VALUES ('853');
INSERT INTO `t_helper` VALUES ('854');
INSERT INTO `t_helper` VALUES ('855');
INSERT INTO `t_helper` VALUES ('856');
INSERT INTO `t_helper` VALUES ('857');
INSERT INTO `t_helper` VALUES ('858');
INSERT INTO `t_helper` VALUES ('859');
INSERT INTO `t_helper` VALUES ('860');
INSERT INTO `t_helper` VALUES ('861');
INSERT INTO `t_helper` VALUES ('862');
INSERT INTO `t_helper` VALUES ('863');
INSERT INTO `t_helper` VALUES ('864');
INSERT INTO `t_helper` VALUES ('865');
INSERT INTO `t_helper` VALUES ('866');
INSERT INTO `t_helper` VALUES ('867');
INSERT INTO `t_helper` VALUES ('868');
INSERT INTO `t_helper` VALUES ('869');
INSERT INTO `t_helper` VALUES ('870');
INSERT INTO `t_helper` VALUES ('871');
INSERT INTO `t_helper` VALUES ('872');
INSERT INTO `t_helper` VALUES ('873');
INSERT INTO `t_helper` VALUES ('874');
INSERT INTO `t_helper` VALUES ('875');
INSERT INTO `t_helper` VALUES ('876');
INSERT INTO `t_helper` VALUES ('877');
INSERT INTO `t_helper` VALUES ('878');
INSERT INTO `t_helper` VALUES ('879');
INSERT INTO `t_helper` VALUES ('880');
INSERT INTO `t_helper` VALUES ('881');
INSERT INTO `t_helper` VALUES ('882');
INSERT INTO `t_helper` VALUES ('883');
INSERT INTO `t_helper` VALUES ('884');
INSERT INTO `t_helper` VALUES ('885');
INSERT INTO `t_helper` VALUES ('886');
INSERT INTO `t_helper` VALUES ('887');
INSERT INTO `t_helper` VALUES ('888');
INSERT INTO `t_helper` VALUES ('889');
INSERT INTO `t_helper` VALUES ('890');
INSERT INTO `t_helper` VALUES ('891');
INSERT INTO `t_helper` VALUES ('892');
INSERT INTO `t_helper` VALUES ('893');
INSERT INTO `t_helper` VALUES ('894');
INSERT INTO `t_helper` VALUES ('895');
INSERT INTO `t_helper` VALUES ('896');
INSERT INTO `t_helper` VALUES ('897');
INSERT INTO `t_helper` VALUES ('898');
INSERT INTO `t_helper` VALUES ('899');
INSERT INTO `t_helper` VALUES ('900');
INSERT INTO `t_helper` VALUES ('901');
INSERT INTO `t_helper` VALUES ('902');
INSERT INTO `t_helper` VALUES ('903');
INSERT INTO `t_helper` VALUES ('904');
INSERT INTO `t_helper` VALUES ('905');
INSERT INTO `t_helper` VALUES ('906');
INSERT INTO `t_helper` VALUES ('907');
INSERT INTO `t_helper` VALUES ('908');
INSERT INTO `t_helper` VALUES ('909');
INSERT INTO `t_helper` VALUES ('910');
INSERT INTO `t_helper` VALUES ('911');
INSERT INTO `t_helper` VALUES ('912');
INSERT INTO `t_helper` VALUES ('913');
INSERT INTO `t_helper` VALUES ('914');
INSERT INTO `t_helper` VALUES ('915');
INSERT INTO `t_helper` VALUES ('916');
INSERT INTO `t_helper` VALUES ('917');
INSERT INTO `t_helper` VALUES ('918');
INSERT INTO `t_helper` VALUES ('919');
INSERT INTO `t_helper` VALUES ('920');
INSERT INTO `t_helper` VALUES ('921');
INSERT INTO `t_helper` VALUES ('922');
INSERT INTO `t_helper` VALUES ('923');
INSERT INTO `t_helper` VALUES ('924');
INSERT INTO `t_helper` VALUES ('925');
INSERT INTO `t_helper` VALUES ('926');
INSERT INTO `t_helper` VALUES ('927');
INSERT INTO `t_helper` VALUES ('928');
INSERT INTO `t_helper` VALUES ('929');
INSERT INTO `t_helper` VALUES ('930');
INSERT INTO `t_helper` VALUES ('931');
INSERT INTO `t_helper` VALUES ('932');
INSERT INTO `t_helper` VALUES ('933');
INSERT INTO `t_helper` VALUES ('934');
INSERT INTO `t_helper` VALUES ('935');
INSERT INTO `t_helper` VALUES ('936');
INSERT INTO `t_helper` VALUES ('937');
INSERT INTO `t_helper` VALUES ('938');
INSERT INTO `t_helper` VALUES ('939');
INSERT INTO `t_helper` VALUES ('940');
INSERT INTO `t_helper` VALUES ('941');
INSERT INTO `t_helper` VALUES ('942');
INSERT INTO `t_helper` VALUES ('943');
INSERT INTO `t_helper` VALUES ('944');
INSERT INTO `t_helper` VALUES ('945');
INSERT INTO `t_helper` VALUES ('946');
INSERT INTO `t_helper` VALUES ('947');
INSERT INTO `t_helper` VALUES ('948');
INSERT INTO `t_helper` VALUES ('949');
INSERT INTO `t_helper` VALUES ('950');
INSERT INTO `t_helper` VALUES ('951');
INSERT INTO `t_helper` VALUES ('952');
INSERT INTO `t_helper` VALUES ('953');
INSERT INTO `t_helper` VALUES ('954');
INSERT INTO `t_helper` VALUES ('955');
INSERT INTO `t_helper` VALUES ('956');
INSERT INTO `t_helper` VALUES ('957');
INSERT INTO `t_helper` VALUES ('958');
INSERT INTO `t_helper` VALUES ('959');
INSERT INTO `t_helper` VALUES ('960');
INSERT INTO `t_helper` VALUES ('961');
INSERT INTO `t_helper` VALUES ('962');
INSERT INTO `t_helper` VALUES ('963');
INSERT INTO `t_helper` VALUES ('964');
INSERT INTO `t_helper` VALUES ('965');
INSERT INTO `t_helper` VALUES ('966');
INSERT INTO `t_helper` VALUES ('967');
INSERT INTO `t_helper` VALUES ('968');
INSERT INTO `t_helper` VALUES ('969');
INSERT INTO `t_helper` VALUES ('970');
INSERT INTO `t_helper` VALUES ('971');
INSERT INTO `t_helper` VALUES ('972');
INSERT INTO `t_helper` VALUES ('973');
INSERT INTO `t_helper` VALUES ('974');
INSERT INTO `t_helper` VALUES ('975');
INSERT INTO `t_helper` VALUES ('976');
INSERT INTO `t_helper` VALUES ('977');
INSERT INTO `t_helper` VALUES ('978');
INSERT INTO `t_helper` VALUES ('979');
INSERT INTO `t_helper` VALUES ('980');
INSERT INTO `t_helper` VALUES ('981');
INSERT INTO `t_helper` VALUES ('982');
INSERT INTO `t_helper` VALUES ('983');
INSERT INTO `t_helper` VALUES ('984');
INSERT INTO `t_helper` VALUES ('985');
INSERT INTO `t_helper` VALUES ('986');
INSERT INTO `t_helper` VALUES ('987');
INSERT INTO `t_helper` VALUES ('988');
INSERT INTO `t_helper` VALUES ('989');
INSERT INTO `t_helper` VALUES ('990');
INSERT INTO `t_helper` VALUES ('991');
INSERT INTO `t_helper` VALUES ('992');
INSERT INTO `t_helper` VALUES ('993');
INSERT INTO `t_helper` VALUES ('994');
INSERT INTO `t_helper` VALUES ('995');
INSERT INTO `t_helper` VALUES ('996');
INSERT INTO `t_helper` VALUES ('997');
INSERT INTO `t_helper` VALUES ('998');
INSERT INTO `t_helper` VALUES ('999');
INSERT INTO `t_helper` VALUES ('1000');
INSERT INTO `t_helper` VALUES ('1001');
INSERT INTO `t_helper` VALUES ('1002');
INSERT INTO `t_helper` VALUES ('1003');
INSERT INTO `t_helper` VALUES ('1004');
INSERT INTO `t_helper` VALUES ('1005');
INSERT INTO `t_helper` VALUES ('1006');
INSERT INTO `t_helper` VALUES ('1007');
INSERT INTO `t_helper` VALUES ('1008');
INSERT INTO `t_helper` VALUES ('1009');
INSERT INTO `t_helper` VALUES ('1010');
INSERT INTO `t_helper` VALUES ('1011');
INSERT INTO `t_helper` VALUES ('1012');
INSERT INTO `t_helper` VALUES ('1013');
INSERT INTO `t_helper` VALUES ('1014');
INSERT INTO `t_helper` VALUES ('1015');
INSERT INTO `t_helper` VALUES ('1016');
INSERT INTO `t_helper` VALUES ('1017');
INSERT INTO `t_helper` VALUES ('1018');
INSERT INTO `t_helper` VALUES ('1019');
INSERT INTO `t_helper` VALUES ('1020');
INSERT INTO `t_helper` VALUES ('1021');
INSERT INTO `t_helper` VALUES ('1022');
INSERT INTO `t_helper` VALUES ('1023');
INSERT INTO `t_helper` VALUES ('1024');
INSERT INTO `t_helper` VALUES ('1025');
INSERT INTO `t_helper` VALUES ('1026');
INSERT INTO `t_helper` VALUES ('1027');
INSERT INTO `t_helper` VALUES ('1028');
INSERT INTO `t_helper` VALUES ('1029');
INSERT INTO `t_helper` VALUES ('1030');
INSERT INTO `t_helper` VALUES ('1031');
INSERT INTO `t_helper` VALUES ('1032');
INSERT INTO `t_helper` VALUES ('1033');
INSERT INTO `t_helper` VALUES ('1034');
INSERT INTO `t_helper` VALUES ('1035');
INSERT INTO `t_helper` VALUES ('1036');
INSERT INTO `t_helper` VALUES ('1037');
INSERT INTO `t_helper` VALUES ('1038');
INSERT INTO `t_helper` VALUES ('1039');
INSERT INTO `t_helper` VALUES ('1040');
INSERT INTO `t_helper` VALUES ('1041');
INSERT INTO `t_helper` VALUES ('1042');
INSERT INTO `t_helper` VALUES ('1043');
INSERT INTO `t_helper` VALUES ('1044');
INSERT INTO `t_helper` VALUES ('1045');
INSERT INTO `t_helper` VALUES ('1046');
INSERT INTO `t_helper` VALUES ('1047');
INSERT INTO `t_helper` VALUES ('1048');
INSERT INTO `t_helper` VALUES ('1049');
INSERT INTO `t_helper` VALUES ('1050');
INSERT INTO `t_helper` VALUES ('1051');
INSERT INTO `t_helper` VALUES ('1052');
INSERT INTO `t_helper` VALUES ('1053');
INSERT INTO `t_helper` VALUES ('1054');
INSERT INTO `t_helper` VALUES ('1055');
INSERT INTO `t_helper` VALUES ('1056');
INSERT INTO `t_helper` VALUES ('1057');
INSERT INTO `t_helper` VALUES ('1058');
INSERT INTO `t_helper` VALUES ('1059');
INSERT INTO `t_helper` VALUES ('1060');
INSERT INTO `t_helper` VALUES ('1061');
INSERT INTO `t_helper` VALUES ('1062');
INSERT INTO `t_helper` VALUES ('1063');
INSERT INTO `t_helper` VALUES ('1064');
INSERT INTO `t_helper` VALUES ('1065');
INSERT INTO `t_helper` VALUES ('1066');
INSERT INTO `t_helper` VALUES ('1067');
INSERT INTO `t_helper` VALUES ('1068');
INSERT INTO `t_helper` VALUES ('1069');
INSERT INTO `t_helper` VALUES ('1070');
INSERT INTO `t_helper` VALUES ('1071');
INSERT INTO `t_helper` VALUES ('1072');
INSERT INTO `t_helper` VALUES ('1073');
INSERT INTO `t_helper` VALUES ('1074');
INSERT INTO `t_helper` VALUES ('1075');
INSERT INTO `t_helper` VALUES ('1076');
INSERT INTO `t_helper` VALUES ('1077');
INSERT INTO `t_helper` VALUES ('1078');
INSERT INTO `t_helper` VALUES ('1079');
INSERT INTO `t_helper` VALUES ('1080');
INSERT INTO `t_helper` VALUES ('1081');
INSERT INTO `t_helper` VALUES ('1082');
INSERT INTO `t_helper` VALUES ('1083');
INSERT INTO `t_helper` VALUES ('1084');
INSERT INTO `t_helper` VALUES ('1085');
INSERT INTO `t_helper` VALUES ('1086');
INSERT INTO `t_helper` VALUES ('1087');
INSERT INTO `t_helper` VALUES ('1088');
INSERT INTO `t_helper` VALUES ('1089');
INSERT INTO `t_helper` VALUES ('1090');
INSERT INTO `t_helper` VALUES ('1091');
INSERT INTO `t_helper` VALUES ('1092');
INSERT INTO `t_helper` VALUES ('1093');
INSERT INTO `t_helper` VALUES ('1094');
INSERT INTO `t_helper` VALUES ('1095');
INSERT INTO `t_helper` VALUES ('1096');
INSERT INTO `t_helper` VALUES ('1097');
INSERT INTO `t_helper` VALUES ('1098');
INSERT INTO `t_helper` VALUES ('1099');
INSERT INTO `t_helper` VALUES ('1100');
INSERT INTO `t_helper` VALUES ('1101');
INSERT INTO `t_helper` VALUES ('1102');
INSERT INTO `t_helper` VALUES ('1103');
INSERT INTO `t_helper` VALUES ('1104');
INSERT INTO `t_helper` VALUES ('1105');
INSERT INTO `t_helper` VALUES ('1106');
INSERT INTO `t_helper` VALUES ('1107');
INSERT INTO `t_helper` VALUES ('1108');
INSERT INTO `t_helper` VALUES ('1109');
INSERT INTO `t_helper` VALUES ('1110');
INSERT INTO `t_helper` VALUES ('1111');
INSERT INTO `t_helper` VALUES ('1112');
INSERT INTO `t_helper` VALUES ('1113');
INSERT INTO `t_helper` VALUES ('1114');
INSERT INTO `t_helper` VALUES ('1115');
INSERT INTO `t_helper` VALUES ('1116');
INSERT INTO `t_helper` VALUES ('1117');
INSERT INTO `t_helper` VALUES ('1118');
INSERT INTO `t_helper` VALUES ('1119');
INSERT INTO `t_helper` VALUES ('1120');
INSERT INTO `t_helper` VALUES ('1121');
INSERT INTO `t_helper` VALUES ('1122');
INSERT INTO `t_helper` VALUES ('1123');
INSERT INTO `t_helper` VALUES ('1124');
INSERT INTO `t_helper` VALUES ('1125');
INSERT INTO `t_helper` VALUES ('1126');
INSERT INTO `t_helper` VALUES ('1127');
INSERT INTO `t_helper` VALUES ('1128');
INSERT INTO `t_helper` VALUES ('1129');
INSERT INTO `t_helper` VALUES ('1130');
INSERT INTO `t_helper` VALUES ('1131');
INSERT INTO `t_helper` VALUES ('1132');
INSERT INTO `t_helper` VALUES ('1133');
INSERT INTO `t_helper` VALUES ('1134');
INSERT INTO `t_helper` VALUES ('1135');
INSERT INTO `t_helper` VALUES ('1136');
INSERT INTO `t_helper` VALUES ('1137');
INSERT INTO `t_helper` VALUES ('1138');
INSERT INTO `t_helper` VALUES ('1139');
INSERT INTO `t_helper` VALUES ('1140');
INSERT INTO `t_helper` VALUES ('1141');
INSERT INTO `t_helper` VALUES ('1142');
INSERT INTO `t_helper` VALUES ('1143');
INSERT INTO `t_helper` VALUES ('1144');
INSERT INTO `t_helper` VALUES ('1145');
INSERT INTO `t_helper` VALUES ('1146');
INSERT INTO `t_helper` VALUES ('1147');
INSERT INTO `t_helper` VALUES ('1148');
INSERT INTO `t_helper` VALUES ('1149');
INSERT INTO `t_helper` VALUES ('1150');
INSERT INTO `t_helper` VALUES ('1151');
INSERT INTO `t_helper` VALUES ('1152');
INSERT INTO `t_helper` VALUES ('1153');
INSERT INTO `t_helper` VALUES ('1154');
INSERT INTO `t_helper` VALUES ('1155');
INSERT INTO `t_helper` VALUES ('1156');
INSERT INTO `t_helper` VALUES ('1157');
INSERT INTO `t_helper` VALUES ('1158');
INSERT INTO `t_helper` VALUES ('1159');
INSERT INTO `t_helper` VALUES ('1160');
INSERT INTO `t_helper` VALUES ('1161');
INSERT INTO `t_helper` VALUES ('1162');
INSERT INTO `t_helper` VALUES ('1163');
INSERT INTO `t_helper` VALUES ('1164');
INSERT INTO `t_helper` VALUES ('1165');
INSERT INTO `t_helper` VALUES ('1166');
INSERT INTO `t_helper` VALUES ('1167');
INSERT INTO `t_helper` VALUES ('1168');
INSERT INTO `t_helper` VALUES ('1169');
INSERT INTO `t_helper` VALUES ('1170');
INSERT INTO `t_helper` VALUES ('1171');
INSERT INTO `t_helper` VALUES ('1172');
INSERT INTO `t_helper` VALUES ('1173');
INSERT INTO `t_helper` VALUES ('1174');
INSERT INTO `t_helper` VALUES ('1175');
INSERT INTO `t_helper` VALUES ('1176');
INSERT INTO `t_helper` VALUES ('1177');
INSERT INTO `t_helper` VALUES ('1178');
INSERT INTO `t_helper` VALUES ('1179');
INSERT INTO `t_helper` VALUES ('1180');
INSERT INTO `t_helper` VALUES ('1181');
INSERT INTO `t_helper` VALUES ('1182');
INSERT INTO `t_helper` VALUES ('1183');
INSERT INTO `t_helper` VALUES ('1184');
INSERT INTO `t_helper` VALUES ('1185');
INSERT INTO `t_helper` VALUES ('1186');
INSERT INTO `t_helper` VALUES ('1187');
INSERT INTO `t_helper` VALUES ('1188');
INSERT INTO `t_helper` VALUES ('1189');
INSERT INTO `t_helper` VALUES ('1190');
INSERT INTO `t_helper` VALUES ('1191');
INSERT INTO `t_helper` VALUES ('1192');
INSERT INTO `t_helper` VALUES ('1193');
INSERT INTO `t_helper` VALUES ('1194');
INSERT INTO `t_helper` VALUES ('1195');
INSERT INTO `t_helper` VALUES ('1196');
INSERT INTO `t_helper` VALUES ('1197');
INSERT INTO `t_helper` VALUES ('1198');
INSERT INTO `t_helper` VALUES ('1199');
INSERT INTO `t_helper` VALUES ('1200');
INSERT INTO `t_helper` VALUES ('1201');
INSERT INTO `t_helper` VALUES ('1202');
INSERT INTO `t_helper` VALUES ('1203');
INSERT INTO `t_helper` VALUES ('1204');
INSERT INTO `t_helper` VALUES ('1205');
INSERT INTO `t_helper` VALUES ('1206');
INSERT INTO `t_helper` VALUES ('1207');
INSERT INTO `t_helper` VALUES ('1208');
INSERT INTO `t_helper` VALUES ('1209');
INSERT INTO `t_helper` VALUES ('1210');
INSERT INTO `t_helper` VALUES ('1211');
INSERT INTO `t_helper` VALUES ('1212');
INSERT INTO `t_helper` VALUES ('1213');
INSERT INTO `t_helper` VALUES ('1214');
INSERT INTO `t_helper` VALUES ('1215');
INSERT INTO `t_helper` VALUES ('1216');
INSERT INTO `t_helper` VALUES ('1217');
INSERT INTO `t_helper` VALUES ('1218');
INSERT INTO `t_helper` VALUES ('1219');
INSERT INTO `t_helper` VALUES ('1220');
INSERT INTO `t_helper` VALUES ('1221');
INSERT INTO `t_helper` VALUES ('1222');
INSERT INTO `t_helper` VALUES ('1223');
INSERT INTO `t_helper` VALUES ('1224');
INSERT INTO `t_helper` VALUES ('1225');
INSERT INTO `t_helper` VALUES ('1226');
INSERT INTO `t_helper` VALUES ('1227');
INSERT INTO `t_helper` VALUES ('1228');
INSERT INTO `t_helper` VALUES ('1229');
INSERT INTO `t_helper` VALUES ('1230');
INSERT INTO `t_helper` VALUES ('1231');
INSERT INTO `t_helper` VALUES ('1232');
INSERT INTO `t_helper` VALUES ('1233');
INSERT INTO `t_helper` VALUES ('1234');
INSERT INTO `t_helper` VALUES ('1235');
INSERT INTO `t_helper` VALUES ('1236');
INSERT INTO `t_helper` VALUES ('1237');
INSERT INTO `t_helper` VALUES ('1238');
INSERT INTO `t_helper` VALUES ('1239');
INSERT INTO `t_helper` VALUES ('1240');
INSERT INTO `t_helper` VALUES ('1241');
INSERT INTO `t_helper` VALUES ('1242');
INSERT INTO `t_helper` VALUES ('1243');
INSERT INTO `t_helper` VALUES ('1244');
INSERT INTO `t_helper` VALUES ('1245');
INSERT INTO `t_helper` VALUES ('1246');
INSERT INTO `t_helper` VALUES ('1247');
INSERT INTO `t_helper` VALUES ('1248');
INSERT INTO `t_helper` VALUES ('1249');
INSERT INTO `t_helper` VALUES ('1250');
INSERT INTO `t_helper` VALUES ('1251');
INSERT INTO `t_helper` VALUES ('1252');
INSERT INTO `t_helper` VALUES ('1253');
INSERT INTO `t_helper` VALUES ('1254');
INSERT INTO `t_helper` VALUES ('1255');
INSERT INTO `t_helper` VALUES ('1256');
INSERT INTO `t_helper` VALUES ('1257');
INSERT INTO `t_helper` VALUES ('1258');
INSERT INTO `t_helper` VALUES ('1259');
INSERT INTO `t_helper` VALUES ('1260');
INSERT INTO `t_helper` VALUES ('1261');
INSERT INTO `t_helper` VALUES ('1262');
INSERT INTO `t_helper` VALUES ('1263');
INSERT INTO `t_helper` VALUES ('1264');
INSERT INTO `t_helper` VALUES ('1265');
INSERT INTO `t_helper` VALUES ('1266');
INSERT INTO `t_helper` VALUES ('1267');
INSERT INTO `t_helper` VALUES ('1268');
INSERT INTO `t_helper` VALUES ('1269');
INSERT INTO `t_helper` VALUES ('1270');
INSERT INTO `t_helper` VALUES ('1271');
INSERT INTO `t_helper` VALUES ('1272');
INSERT INTO `t_helper` VALUES ('1273');
INSERT INTO `t_helper` VALUES ('1274');
INSERT INTO `t_helper` VALUES ('1275');
INSERT INTO `t_helper` VALUES ('1276');
INSERT INTO `t_helper` VALUES ('1277');
INSERT INTO `t_helper` VALUES ('1278');
INSERT INTO `t_helper` VALUES ('1279');
INSERT INTO `t_helper` VALUES ('1280');
INSERT INTO `t_helper` VALUES ('1281');
INSERT INTO `t_helper` VALUES ('1282');
INSERT INTO `t_helper` VALUES ('1283');
INSERT INTO `t_helper` VALUES ('1284');
INSERT INTO `t_helper` VALUES ('1285');
INSERT INTO `t_helper` VALUES ('1286');
INSERT INTO `t_helper` VALUES ('1287');
INSERT INTO `t_helper` VALUES ('1288');
INSERT INTO `t_helper` VALUES ('1289');
INSERT INTO `t_helper` VALUES ('1290');
INSERT INTO `t_helper` VALUES ('1291');
INSERT INTO `t_helper` VALUES ('1292');
INSERT INTO `t_helper` VALUES ('1293');
INSERT INTO `t_helper` VALUES ('1294');
INSERT INTO `t_helper` VALUES ('1295');
INSERT INTO `t_helper` VALUES ('1296');
INSERT INTO `t_helper` VALUES ('1297');
INSERT INTO `t_helper` VALUES ('1298');
INSERT INTO `t_helper` VALUES ('1299');
INSERT INTO `t_helper` VALUES ('1300');
INSERT INTO `t_helper` VALUES ('1301');
INSERT INTO `t_helper` VALUES ('1302');
INSERT INTO `t_helper` VALUES ('1303');
INSERT INTO `t_helper` VALUES ('1304');
INSERT INTO `t_helper` VALUES ('1305');
INSERT INTO `t_helper` VALUES ('1306');
INSERT INTO `t_helper` VALUES ('1307');
INSERT INTO `t_helper` VALUES ('1308');
INSERT INTO `t_helper` VALUES ('1309');
INSERT INTO `t_helper` VALUES ('1310');
INSERT INTO `t_helper` VALUES ('1311');
INSERT INTO `t_helper` VALUES ('1312');
INSERT INTO `t_helper` VALUES ('1313');
INSERT INTO `t_helper` VALUES ('1314');
INSERT INTO `t_helper` VALUES ('1315');
INSERT INTO `t_helper` VALUES ('1316');
INSERT INTO `t_helper` VALUES ('1317');
INSERT INTO `t_helper` VALUES ('1318');
INSERT INTO `t_helper` VALUES ('1319');
INSERT INTO `t_helper` VALUES ('1320');
INSERT INTO `t_helper` VALUES ('1321');
INSERT INTO `t_helper` VALUES ('1322');
INSERT INTO `t_helper` VALUES ('1323');
INSERT INTO `t_helper` VALUES ('1324');
INSERT INTO `t_helper` VALUES ('1325');
INSERT INTO `t_helper` VALUES ('1326');
INSERT INTO `t_helper` VALUES ('1327');
INSERT INTO `t_helper` VALUES ('1328');
INSERT INTO `t_helper` VALUES ('1329');
INSERT INTO `t_helper` VALUES ('1330');
INSERT INTO `t_helper` VALUES ('1331');
INSERT INTO `t_helper` VALUES ('1332');
INSERT INTO `t_helper` VALUES ('1333');
INSERT INTO `t_helper` VALUES ('1334');
INSERT INTO `t_helper` VALUES ('1335');
INSERT INTO `t_helper` VALUES ('1336');
INSERT INTO `t_helper` VALUES ('1337');
INSERT INTO `t_helper` VALUES ('1338');
INSERT INTO `t_helper` VALUES ('1339');
INSERT INTO `t_helper` VALUES ('1340');
INSERT INTO `t_helper` VALUES ('1341');
INSERT INTO `t_helper` VALUES ('1342');
INSERT INTO `t_helper` VALUES ('1343');
INSERT INTO `t_helper` VALUES ('1344');
INSERT INTO `t_helper` VALUES ('1345');
INSERT INTO `t_helper` VALUES ('1346');
INSERT INTO `t_helper` VALUES ('1347');
INSERT INTO `t_helper` VALUES ('1348');
INSERT INTO `t_helper` VALUES ('1349');
INSERT INTO `t_helper` VALUES ('1350');
INSERT INTO `t_helper` VALUES ('1351');
INSERT INTO `t_helper` VALUES ('1352');
INSERT INTO `t_helper` VALUES ('1353');
INSERT INTO `t_helper` VALUES ('1354');
INSERT INTO `t_helper` VALUES ('1355');
INSERT INTO `t_helper` VALUES ('1356');
INSERT INTO `t_helper` VALUES ('1357');
INSERT INTO `t_helper` VALUES ('1358');
INSERT INTO `t_helper` VALUES ('1359');
INSERT INTO `t_helper` VALUES ('1360');
INSERT INTO `t_helper` VALUES ('1361');
INSERT INTO `t_helper` VALUES ('1362');
INSERT INTO `t_helper` VALUES ('1363');
INSERT INTO `t_helper` VALUES ('1364');
INSERT INTO `t_helper` VALUES ('1365');
INSERT INTO `t_helper` VALUES ('1366');
INSERT INTO `t_helper` VALUES ('1367');
INSERT INTO `t_helper` VALUES ('1368');
INSERT INTO `t_helper` VALUES ('1369');
INSERT INTO `t_helper` VALUES ('1370');
INSERT INTO `t_helper` VALUES ('1371');
INSERT INTO `t_helper` VALUES ('1372');
INSERT INTO `t_helper` VALUES ('1373');
INSERT INTO `t_helper` VALUES ('1374');
INSERT INTO `t_helper` VALUES ('1375');
INSERT INTO `t_helper` VALUES ('1376');
INSERT INTO `t_helper` VALUES ('1377');
INSERT INTO `t_helper` VALUES ('1378');
INSERT INTO `t_helper` VALUES ('1379');
INSERT INTO `t_helper` VALUES ('1380');
INSERT INTO `t_helper` VALUES ('1381');
INSERT INTO `t_helper` VALUES ('1382');
INSERT INTO `t_helper` VALUES ('1383');
INSERT INTO `t_helper` VALUES ('1384');
INSERT INTO `t_helper` VALUES ('1385');
INSERT INTO `t_helper` VALUES ('1386');
INSERT INTO `t_helper` VALUES ('1387');
INSERT INTO `t_helper` VALUES ('1388');
INSERT INTO `t_helper` VALUES ('1389');
INSERT INTO `t_helper` VALUES ('1390');
INSERT INTO `t_helper` VALUES ('1391');
INSERT INTO `t_helper` VALUES ('1392');
INSERT INTO `t_helper` VALUES ('1393');
INSERT INTO `t_helper` VALUES ('1394');
INSERT INTO `t_helper` VALUES ('1395');
INSERT INTO `t_helper` VALUES ('1396');
INSERT INTO `t_helper` VALUES ('1397');
INSERT INTO `t_helper` VALUES ('1398');
INSERT INTO `t_helper` VALUES ('1399');
INSERT INTO `t_helper` VALUES ('1400');
INSERT INTO `t_helper` VALUES ('1401');
INSERT INTO `t_helper` VALUES ('1402');
INSERT INTO `t_helper` VALUES ('1403');
INSERT INTO `t_helper` VALUES ('1404');
INSERT INTO `t_helper` VALUES ('1405');
INSERT INTO `t_helper` VALUES ('1406');
INSERT INTO `t_helper` VALUES ('1407');
INSERT INTO `t_helper` VALUES ('1408');
INSERT INTO `t_helper` VALUES ('1409');
INSERT INTO `t_helper` VALUES ('1410');
INSERT INTO `t_helper` VALUES ('1411');
INSERT INTO `t_helper` VALUES ('1412');
INSERT INTO `t_helper` VALUES ('1413');
INSERT INTO `t_helper` VALUES ('1414');
INSERT INTO `t_helper` VALUES ('1415');
INSERT INTO `t_helper` VALUES ('1416');
INSERT INTO `t_helper` VALUES ('1417');
INSERT INTO `t_helper` VALUES ('1418');
INSERT INTO `t_helper` VALUES ('1419');
INSERT INTO `t_helper` VALUES ('1420');
INSERT INTO `t_helper` VALUES ('1421');
INSERT INTO `t_helper` VALUES ('1422');
INSERT INTO `t_helper` VALUES ('1423');
INSERT INTO `t_helper` VALUES ('1424');
INSERT INTO `t_helper` VALUES ('1425');
INSERT INTO `t_helper` VALUES ('1426');
INSERT INTO `t_helper` VALUES ('1427');
INSERT INTO `t_helper` VALUES ('1428');
INSERT INTO `t_helper` VALUES ('1429');
INSERT INTO `t_helper` VALUES ('1430');
INSERT INTO `t_helper` VALUES ('1431');
INSERT INTO `t_helper` VALUES ('1432');
INSERT INTO `t_helper` VALUES ('1433');
INSERT INTO `t_helper` VALUES ('1434');
INSERT INTO `t_helper` VALUES ('1435');
INSERT INTO `t_helper` VALUES ('1436');
INSERT INTO `t_helper` VALUES ('1437');
INSERT INTO `t_helper` VALUES ('1438');
INSERT INTO `t_helper` VALUES ('1439');
INSERT INTO `t_helper` VALUES ('1440');
INSERT INTO `t_helper` VALUES ('1441');
INSERT INTO `t_helper` VALUES ('1442');
INSERT INTO `t_helper` VALUES ('1443');
INSERT INTO `t_helper` VALUES ('1444');
INSERT INTO `t_helper` VALUES ('1445');
INSERT INTO `t_helper` VALUES ('1446');
INSERT INTO `t_helper` VALUES ('1447');
INSERT INTO `t_helper` VALUES ('1448');
INSERT INTO `t_helper` VALUES ('1449');
INSERT INTO `t_helper` VALUES ('1450');
INSERT INTO `t_helper` VALUES ('1451');
INSERT INTO `t_helper` VALUES ('1452');
INSERT INTO `t_helper` VALUES ('1453');
INSERT INTO `t_helper` VALUES ('1454');
INSERT INTO `t_helper` VALUES ('1455');
INSERT INTO `t_helper` VALUES ('1456');
INSERT INTO `t_helper` VALUES ('1457');
INSERT INTO `t_helper` VALUES ('1458');
INSERT INTO `t_helper` VALUES ('1459');
INSERT INTO `t_helper` VALUES ('1460');
INSERT INTO `t_helper` VALUES ('1461');
INSERT INTO `t_helper` VALUES ('1462');
INSERT INTO `t_helper` VALUES ('1463');
INSERT INTO `t_helper` VALUES ('1464');
INSERT INTO `t_helper` VALUES ('1465');
INSERT INTO `t_helper` VALUES ('1466');
INSERT INTO `t_helper` VALUES ('1467');
INSERT INTO `t_helper` VALUES ('1468');
INSERT INTO `t_helper` VALUES ('1469');
INSERT INTO `t_helper` VALUES ('1470');
INSERT INTO `t_helper` VALUES ('1471');
INSERT INTO `t_helper` VALUES ('1472');
INSERT INTO `t_helper` VALUES ('1473');
INSERT INTO `t_helper` VALUES ('1474');
INSERT INTO `t_helper` VALUES ('1475');
INSERT INTO `t_helper` VALUES ('1476');
INSERT INTO `t_helper` VALUES ('1477');
INSERT INTO `t_helper` VALUES ('1478');
INSERT INTO `t_helper` VALUES ('1479');
INSERT INTO `t_helper` VALUES ('1480');
INSERT INTO `t_helper` VALUES ('1481');
INSERT INTO `t_helper` VALUES ('1482');
INSERT INTO `t_helper` VALUES ('1483');
INSERT INTO `t_helper` VALUES ('1484');
INSERT INTO `t_helper` VALUES ('1485');
INSERT INTO `t_helper` VALUES ('1486');
INSERT INTO `t_helper` VALUES ('1487');
INSERT INTO `t_helper` VALUES ('1488');
INSERT INTO `t_helper` VALUES ('1489');
INSERT INTO `t_helper` VALUES ('1490');
INSERT INTO `t_helper` VALUES ('1491');
INSERT INTO `t_helper` VALUES ('1492');
INSERT INTO `t_helper` VALUES ('1493');
INSERT INTO `t_helper` VALUES ('1494');
INSERT INTO `t_helper` VALUES ('1495');
INSERT INTO `t_helper` VALUES ('1496');
INSERT INTO `t_helper` VALUES ('1497');
INSERT INTO `t_helper` VALUES ('1498');
INSERT INTO `t_helper` VALUES ('1499');

-- ----------------------------
-- Table structure for t_helper2
-- ----------------------------
DROP TABLE IF EXISTS `t_helper2`;
CREATE TABLE `t_helper2` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of t_helper2
-- ----------------------------

-- ----------------------------
-- Table structure for t_helper3
-- ----------------------------
DROP TABLE IF EXISTS `t_helper3`;
CREATE TABLE `t_helper3` (
  `id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of t_helper3
-- ----------------------------

-- ----------------------------
-- Table structure for t_iteration_bug_real
-- ----------------------------
DROP TABLE IF EXISTS `t_iteration_bug_real`;
CREATE TABLE `t_iteration_bug_real` (
  `iteration_bug_id` varchar(32) NOT NULL,
  `iteration_id` varchar(32) NOT NULL COMMENT '迭代id',
  `bug_card_id` varchar(32) NOT NULL COMMENT '缺陷id',
  PRIMARY KEY (`iteration_bug_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of t_iteration_bug_real
-- ----------------------------

-- ----------------------------
-- Table structure for t_iteration_list
-- ----------------------------
DROP TABLE IF EXISTS `t_iteration_list`;
CREATE TABLE `t_iteration_list` (
  `iteration_id` varchar(32) NOT NULL,
  `iteration_bag_name` varchar(60) DEFAULT NULL COMMENT '迭代包名',
  `association_project` varchar(60) DEFAULT NULL,
  `task_id` varchar(32) DEFAULT NULL,
  `create_person` varchar(60) DEFAULT NULL COMMENT '创建人',
  `status` varchar(1) DEFAULT NULL,
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `note` varchar(1000) DEFAULT NULL COMMENT '备注',
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`iteration_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of t_iteration_list
-- ----------------------------

-- ----------------------------
-- Table structure for t_iteration_task_real
-- ----------------------------
DROP TABLE IF EXISTS `t_iteration_task_real`;
CREATE TABLE `t_iteration_task_real` (
  `iteration_mission_id` varchar(32) NOT NULL,
  `iteration_id` varchar(32) NOT NULL,
  `mission_id` varchar(32) NOT NULL,
  PRIMARY KEY (`iteration_mission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of t_iteration_task_real
-- ----------------------------

-- ----------------------------
-- Table structure for t_iteration_testcasepackage_real
-- ----------------------------
DROP TABLE IF EXISTS `t_iteration_testcasepackage_real`;
CREATE TABLE `t_iteration_testcasepackage_real` (
  `iteration_package_id` varchar(32) NOT NULL,
  `iteration_id` varchar(32) NOT NULL,
  `package_id` varchar(32) NOT NULL,
  PRIMARY KEY (`iteration_package_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of t_iteration_testcasepackage_real
-- ----------------------------

-- ----------------------------
-- Table structure for t_loginfo
-- ----------------------------
DROP TABLE IF EXISTS `t_loginfo`;
CREATE TABLE `t_loginfo` (
  `CALLNUMBER` varchar(13) DEFAULT NULL,
  `INFOCODE` varchar(13) DEFAULT NULL,
  `LOGINTIME` varchar(20) DEFAULT NULL,
  `ACTIVETIME` varchar(20) DEFAULT NULL,
  `LOGOUTTIME` varchar(20) DEFAULT NULL,
  `LONGTIME` varchar(20) DEFAULT NULL,
  `PROGRAMCODE` varchar(10) DEFAULT NULL,
  `FEEPERCENT` varchar(6) DEFAULT NULL,
  `GROUPTYPE` varchar(6) DEFAULT NULL,
  `ENCAPCODEGROUP` varchar(6) DEFAULT NULL,
  `EMPLOYEENUMBER` varchar(15) DEFAULT NULL,
  `CATEGORYS` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_loginfo
-- ----------------------------

-- ----------------------------
-- Table structure for t_log_config
-- ----------------------------
DROP TABLE IF EXISTS `t_log_config`;
CREATE TABLE `t_log_config` (
  `ID` varchar(32) NOT NULL,
  `TITLE` varchar(20) DEFAULT NULL,
  `METHOD` varchar(40) DEFAULT NULL,
  `ARGS` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_log_config
-- ----------------------------

-- ----------------------------
-- Table structure for t_log_configure
-- ----------------------------
DROP TABLE IF EXISTS `t_log_configure`;
CREATE TABLE `t_log_configure` (
  `log_id` varchar(32) NOT NULL,
  `log_args` varchar(300) DEFAULT NULL,
  `log_insertDate` date DEFAULT NULL,
  `log_method` varchar(300) DEFAULT NULL,
  `log_title` varchar(300) DEFAULT NULL,
  `log_updateDate` date DEFAULT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of t_log_configure
-- ----------------------------

-- ----------------------------
-- Table structure for t_mission_log
-- ----------------------------
DROP TABLE IF EXISTS `t_mission_log`;
CREATE TABLE `t_mission_log` (
  `log_id` varchar(32) NOT NULL COMMENT '日志唯一标识符',
  `operate_type` varchar(1) DEFAULT NULL COMMENT '操作类型(1.创建并分配，2.改状态，3.填进度)',
  `operate_person` varchar(64) DEFAULT NULL COMMENT '操作者',
  `operate_detail` varchar(255) DEFAULT NULL COMMENT '具体操作内容',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `mission_id` varchar(32) NOT NULL COMMENT '任务id',
  PRIMARY KEY (`log_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_mission_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_moduleverrec
-- ----------------------------
DROP TABLE IF EXISTS `t_moduleverrec`;
CREATE TABLE `t_moduleverrec` (
  `TASKID` varchar(32) NOT NULL,
  `TEST_PHASE` int(1) DEFAULT '0',
  `MODULEID` bigint(20) NOT NULL,
  `MODULEVERSION` varchar(30) NOT NULL DEFAULT '0',
  `CREATDATE` date NOT NULL,
  `INSDATE` date DEFAULT NULL,
  `UPDDATE` date DEFAULT NULL,
  `VERRECID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SEQ` int(3) DEFAULT NULL,
  PRIMARY KEY (`VERRECID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_moduleverrec
-- ----------------------------

-- ----------------------------
-- Table structure for t_moduleversion
-- ----------------------------
DROP TABLE IF EXISTS `t_moduleversion`;
CREATE TABLE `t_moduleversion` (
  `TASKID` varchar(32) DEFAULT NULL,
  `TEST_PHASE` int(1) DEFAULT '0',
  `MODULEID` bigint(20) NOT NULL,
  `MODULEVERSION` varchar(30) NOT NULL DEFAULT '0',
  `INSDATE` date DEFAULT NULL,
  `UPDDATE` date DEFAULT NULL,
  `MODULEVERID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SEQ` int(3) DEFAULT NULL,
  PRIMARY KEY (`MODULEVERID`),
  KEY `T_MODULEVERSION_IDX4` (`TASKID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_moduleversion
-- ----------------------------

-- ----------------------------
-- Table structure for t_msg_communion
-- ----------------------------
DROP TABLE IF EXISTS `t_msg_communion`;
CREATE TABLE `t_msg_communion` (
  `TASK_ID` varchar(32) NOT NULL,
  `BUG_ID` bigint(20) NOT NULL,
  `SENDER_ID` varchar(32) DEFAULT NULL,
  `MESSAGE` varchar(255) DEFAULT NULL,
  `INS_DATE` date DEFAULT NULL,
  `MSG_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `RECIP_CD` int(2) DEFAULT NULL,
  PRIMARY KEY (`MSG_ID`),
  KEY `FKC9DA0F1AAAD2BD4A` (`SENDER_ID`),
  CONSTRAINT `FKC9DA0F1AAAD2BD4A` FOREIGN KEY (`SENDER_ID`) REFERENCES `t_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_msg_communion
-- ----------------------------

-- ----------------------------
-- Table structure for t_other_mission
-- ----------------------------
DROP TABLE IF EXISTS `t_other_mission`;
CREATE TABLE `t_other_mission` (
  `mission_id` varchar(32) NOT NULL COMMENT '其他任务唯一标示符',
  `mission_name` varchar(64) DEFAULT NULL COMMENT '任务名称',
  `mission_category` bigint(20) DEFAULT NULL COMMENT '任务类别',
  `mission_type` varchar(1) DEFAULT NULL COMMENT '任务类型',
  `project_id` varchar(32) DEFAULT NULL COMMENT '所属项目',
  `project_type` varchar(1) DEFAULT NULL COMMENT '项目类型（0测试项目，1自己创建的项目）',
  `charge_person_id` varchar(32) DEFAULT NULL COMMENT '负责人id',
  `emergency_degree` bigint(20) DEFAULT NULL COMMENT '紧急程度',
  `difficulty_degree` bigint(20) DEFAULT NULL COMMENT '难易程度',
  `predict_start_time` datetime DEFAULT NULL COMMENT '预计开始时间',
  `predict_end_time` datetime DEFAULT NULL COMMENT '预计结束时间',
  `standard_workload` varchar(32) DEFAULT NULL COMMENT '标准工作量',
  `actual_workload` varchar(32) DEFAULT NULL COMMENT '实际工作量',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `completion_degree` varchar(32) DEFAULT NULL COMMENT '任务完成度',
  `create_time` datetime DEFAULT NULL COMMENT '任务创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '任务更新时间',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '任务创建人id',
  `status` varchar(1) DEFAULT NULL COMMENT '任务状态',
  `stop_reason` varchar(255) DEFAULT NULL COMMENT '终止原因',
  `mission_num` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`mission_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_other_mission
-- ----------------------------

-- ----------------------------
-- Table structure for t_outlineinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_outlineinfo`;
CREATE TABLE `t_outlineinfo` (
  `TASKID` varchar(32) DEFAULT NULL,
  `TEST_PHASE` int(1) DEFAULT '0',
  `MODULEID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MODULENAME` varchar(50) DEFAULT NULL,
  `MODULELEVEL` int(2) NOT NULL DEFAULT '0',
  `ISLEAFNODE` int(1) DEFAULT '0',
  `SUPERMODULEID` bigint(20) DEFAULT NULL,
  `MODULESTATE` int(1) DEFAULT '0',
  `KLC` double(15,3) DEFAULT '0.000',
  `INSDATE` datetime DEFAULT NULL,
  `UPDDATE` datetime DEFAULT NULL,
  `COMPANYID` varchar(32) DEFAULT NULL,
  `MODULENUM` varchar(300) DEFAULT NULL,
  `ISROOT` int(1) DEFAULT NULL,
  `CURR_VERSION` varchar(30) DEFAULT NULL,
  `CASE_COUNT` int(7) DEFAULT '0',
  `QUOTIETY` double(7,2) DEFAULT '1.00',
  `REQ_TYPE` int(1) DEFAULT '0',
  `SCENE_COUNT` int(7) DEFAULT '0',
  `SCRP_COUNT` int(7) DEFAULT '0',
  `SEQ` int(8) DEFAULT NULL,
  `REQ_TYPE2` int(1) DEFAULT '0',
  PRIMARY KEY (`MODULEID`),
  KEY `T_OUTLINEINFO_IDX4` (`TASKID`,`TEST_PHASE`,`MODULEID`,`MODULENAME`,`ISLEAFNODE`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_outlineinfo
-- ----------------------------
INSERT INTO `t_outlineinfo` VALUES ('d75078f5c83f443a98c4c73b6378fd0b', null, '1', 'codes', '1', '0', '0', '0', null, null, null, '1289630248328', null, null, null, null, null, null, null, null, null, '0');
INSERT INTO `t_outlineinfo` VALUES ('d75078f5c83f443a98c4c73b6378fd0b', null, '2', '1', '2', '1', '1', '0', null, null, null, '1289630248328', '001', null, null, '3', '1.00', '0', null, null, '1', '0');
INSERT INTO `t_outlineinfo` VALUES ('d75078f5c83f443a98c4c73b6378fd0b', null, '3', '2', '2', '1', '1', '0', null, null, null, '1289630248328', '002', null, null, '3', '1.00', '0', null, null, '2', '0');
INSERT INTO `t_outlineinfo` VALUES ('d75078f5c83f443a98c4c73b6378fd0b', null, '4', '3', '2', '1', '1', '0', null, null, null, '1289630248328', '003', null, null, '3', '1.00', '0', null, null, '3', '0');
INSERT INTO `t_outlineinfo` VALUES ('d75078f5c83f443a98c4c73b6378fd0b', null, '5', '4', '2', '1', '1', '0', null, null, null, '1289630248328', '004', null, null, '3', '1.00', '0', null, null, '4', '0');

-- ----------------------------
-- Table structure for t_outline_teammember
-- ----------------------------
DROP TABLE IF EXISTS `t_outline_teammember`;
CREATE TABLE `t_outline_teammember` (
  `TASKID` varchar(32) NOT NULL,
  `TEST_PHASE` int(1) DEFAULT '0',
  `MODULEID` bigint(20) NOT NULL,
  `USERID` varchar(32) NOT NULL,
  `INSDATE` date DEFAULT NULL,
  `UPDDATE` date DEFAULT NULL,
  `COMPANYID` varchar(32) DEFAULT NULL,
  `USER_ROLE` int(11) DEFAULT '0',
  `MODULE_MEMBER_ID` varchar(32) NOT NULL,
  PRIMARY KEY (`MODULE_MEMBER_ID`),
  KEY `T_OUTLINE_TEAMMEMBER_IDX4` (`TASKID`) USING BTREE,
  KEY `FKCCF7953FC320653D` (`MODULEID`),
  KEY `FKCCF7953F7712C00B` (`USERID`),
  CONSTRAINT `FKCCF7953F7712C00B` FOREIGN KEY (`USERID`) REFERENCES `t_user` (`ID`),
  CONSTRAINT `FKCCF7953FC320653D` FOREIGN KEY (`MODULEID`) REFERENCES `t_outlineinfo` (`MODULEID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_outline_teammember
-- ----------------------------

-- ----------------------------
-- Table structure for t_project
-- ----------------------------
DROP TABLE IF EXISTS `t_project`;
CREATE TABLE `t_project` (
  `project_id` varchar(32) NOT NULL COMMENT '项目id',
  `project_type` varchar(1) DEFAULT NULL,
  `project_name` varchar(64) DEFAULT NULL COMMENT '项目名称',
  `create_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`project_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_project
-- ----------------------------

-- ----------------------------
-- Table structure for t_queryinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_queryinfo`;
CREATE TABLE `t_queryinfo` (
  `QUERY_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `QUERY_NAME` varchar(100) NOT NULL,
  `OWNER_ID` varchar(32) NOT NULL,
  `PARA_VALUE` varchar(1200) NOT NULL,
  `IS_OWNER` int(1) DEFAULT '0',
  `INDENTIFIER` int(3) NOT NULL,
  `HQL_STR` varchar(600) NOT NULL,
  `TASK_ID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`QUERY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_queryinfo
-- ----------------------------

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `ROLEID` varchar(32) NOT NULL,
  `ROLENAME` varchar(40) DEFAULT NULL,
  `INSERTDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATEDATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `REMARK` varchar(50) DEFAULT NULL,
  `ACCESSIP` varchar(500) DEFAULT NULL,
  `COMPANYID` varchar(32) DEFAULT NULL,
  `RELA_DOC_ROLE_ID` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`ROLEID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('402880ea25b62b6a0125b66873290012', 'superRole', '2010-11-13 14:37:28', '0000-00-00 00:00:00', 'superRole', null, '402880ea25b62b6a0125b66873290012', '1');

-- ----------------------------
-- Table structure for t_role_function_real
-- ----------------------------
DROP TABLE IF EXISTS `t_role_function_real`;
CREATE TABLE `t_role_function_real` (
  `ROLEID` varchar(32) DEFAULT NULL,
  `FUNCTIONID` varchar(32) DEFAULT NULL,
  `INSERTDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATEDATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  KEY `FK65222F67B8F63E5` (`FUNCTIONID`),
  KEY `FK65222F67FA806C37` (`FUNCTIONID`),
  KEY `FK65222F6771BD6AA1` (`ROLEID`),
  CONSTRAINT `FK65222F6771BD6AA1` FOREIGN KEY (`ROLEID`) REFERENCES `t_role` (`ROLEID`),
  CONSTRAINT `FK65222F67B8F63E5` FOREIGN KEY (`FUNCTIONID`) REFERENCES `t_function` (`FUNCTIONID`),
  CONSTRAINT `FK65222F67FA806C37` FOREIGN KEY (`FUNCTIONID`) REFERENCES `t_function` (`FUNCTIONID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_function_real
-- ----------------------------
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '1', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '100', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '101', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '102', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '103', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '104', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '105', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '106', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '107', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '108', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '109', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '11', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '111', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '112', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '113', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '114', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '120', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '121', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '122', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '123', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '124', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '125', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '126', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '127', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '128', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '129', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '13', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '131', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '140', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '141', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '142', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '143', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '144', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '145', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '146', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '147', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '148', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '149', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '160', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '161', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '162', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '163', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '164', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '165', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '166', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '167', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '168', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '169', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '180', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '181', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '182', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '183', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '184', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '185', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '186', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '187', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '188', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '189', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '2', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '20', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '200', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '201', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '202', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '203', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '204', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '205', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '206', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '207', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '208', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '209', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '21', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '22', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '220', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '221', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '222', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '223', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '224', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '225', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '226', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '227', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '228', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '229', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '23', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '230', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '231', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '232', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '24', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '25', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '26', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '27', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '28', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '29', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '3', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '4', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '40', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '41', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '42', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '43', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '44', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '45', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '46', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '47', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '48', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '49', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '5', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '6', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '60', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '61', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '62', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '63', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '64', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '65', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '66', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '67', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '68', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '69', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '7', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '70', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '8', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '80', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '81', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '82', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '83', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '84', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '85', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '86', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '87', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '88', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '89', '2018-11-16 20:58:30', '0000-00-00 00:00:00');
INSERT INTO `t_role_function_real` VALUES ('402880ea25b62b6a0125b66873290012', '9', '2018-11-16 20:58:30', '0000-00-00 00:00:00');

-- ----------------------------
-- Table structure for t_sent_ticketinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_sent_ticketinfo`;
CREATE TABLE `t_sent_ticketinfo` (
  `id` int(11) NOT NULL,
  `bus_num` varchar(255) DEFAULT NULL,
  `bus_start_time` varchar(255) DEFAULT NULL,
  `buy_time` varchar(255) DEFAULT NULL,
  `certi_type` varchar(255) DEFAULT NULL,
  `dest_station_name` varchar(255) DEFAULT NULL,
  `identity_id` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `seat_num` varchar(255) DEFAULT NULL,
  `start_station_name` varchar(255) DEFAULT NULL,
  `station_code` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `ticket_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of t_sent_ticketinfo
-- ----------------------------

-- ----------------------------
-- Table structure for t_sent_ticketstatus
-- ----------------------------
DROP TABLE IF EXISTS `t_sent_ticketstatus`;
CREATE TABLE `t_sent_ticketstatus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `brake_machine` varchar(255) DEFAULT NULL,
  `card_num` varchar(255) DEFAULT NULL,
  `card_type` varchar(255) DEFAULT NULL,
  `operation_state` varchar(255) DEFAULT NULL,
  `operation_time` datetime DEFAULT NULL,
  `ticket_sign` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of t_sent_ticketstatus
-- ----------------------------

-- ----------------------------
-- Table structure for t_single_test_task
-- ----------------------------
DROP TABLE IF EXISTS `t_single_test_task`;
CREATE TABLE `t_single_test_task` (
  `TASKID` varchar(32) NOT NULL,
  `INSDATE` date DEFAULT NULL,
  `UPDDATE` date DEFAULT NULL,
  `COMPANYID` varchar(32) NOT NULL,
  `CREATE_ID` varchar(32) DEFAULT NULL,
  `PSM_ID` varchar(32) DEFAULT NULL,
  `PRO_NAME` varchar(60) DEFAULT NULL,
  `PRO_NUM` varchar(30) DEFAULT NULL,
  `DEV_DEPT` varchar(60) DEFAULT NULL,
  `TASK_PROJECT_ID` varchar(32) DEFAULT NULL COMMENT '测试任务与其他任务关联id',
  `STATUS_FLG` int(1) DEFAULT '3',
  `PLAN_START_DATE` date DEFAULT NULL,
  `PLAN_END_DATE` date DEFAULT NULL,
  `FACT_START_DATE` date DEFAULT NULL,
  `FACT_END_DATE` date DEFAULT NULL,
  `TEST_PHASE` int(1) DEFAULT NULL,
  `PLAN_ATTACH` varchar(80) DEFAULT NULL,
  `FILTER_FLAG` varchar(1) DEFAULT NULL COMMENT '过滤已有项目标志',
  PRIMARY KEY (`TASKID`),
  KEY `FK9BD40266255E9362` (`TASKID`),
  KEY `FK9BD40266E5E3E1E7` (`PSM_ID`),
  CONSTRAINT `FK9BD40266E5E3E1E7` FOREIGN KEY (`PSM_ID`) REFERENCES `t_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_single_test_task
-- ----------------------------
INSERT INTO `t_single_test_task` VALUES ('d75078f5c83f443a98c4c73b6378fd0b', '2023-06-15', '2023-06-15', '1289630248328', '1289630248984', '1289630248984', 'codes', '007', 'dev1', null, '0', '2023-06-15', '2023-06-22', null, null, '0', null, '0');

-- ----------------------------
-- Table structure for t_software_version
-- ----------------------------
DROP TABLE IF EXISTS `t_software_version`;
CREATE TABLE `t_software_version` (
  `TASKID` varchar(32) DEFAULT NULL,
  `VERSION_NUM` varchar(40) NOT NULL,
  `INSDATE` date DEFAULT NULL,
  `UPDDATE` date DEFAULT NULL,
  `VERSION_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `REMARK` varchar(200) DEFAULT NULL,
  `SEQ` int(4) DEFAULT '0',
  `VER_STATUS` int(1) DEFAULT '9',
  PRIMARY KEY (`VERSION_ID`),
  UNIQUE KEY `T_SOFTWARE_VERSION_UQ` (`VERSION_NUM`,`TASKID`,`SEQ`) USING BTREE,
  KEY `T_SOFTWARE_VERSION_IDX4` (`TASKID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_software_version
-- ----------------------------
INSERT INTO `t_software_version` VALUES ('d75078f5c83f443a98c4c73b6378fd0b', 'v1.0', '2023-06-15', '2023-06-15', '1', '', '1', '0');

-- ----------------------------
-- Table structure for t_syslog
-- ----------------------------
DROP TABLE IF EXISTS `t_syslog`;
CREATE TABLE `t_syslog` (
  `LOG_ID` varchar(32) NOT NULL,
  `OPER_DESC` varchar(2000) DEFAULT NULL,
  `OPER_ID` varchar(16) DEFAULT NULL,
  `OPER_DATE` datetime DEFAULT NULL,
  `OPER_SUMMARY` varchar(100) DEFAULT NULL,
  `LOG_TYPE` int(1) DEFAULT '0',
  `ACCESS_IP` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`LOG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_syslog
-- ----------------------------
INSERT INTO `t_syslog` VALUES ('1431574085681', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1431574173166', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1431574340149', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1431574372129', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1431574469894', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1431574509270', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1431677895420', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1431678035523', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1431823729981', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1431823794649', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1431824087210', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1431824111646', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1435203862605', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1435203987137', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1435204052084', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1435204081438', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1435204092628', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1435204352945', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1435204798124', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1435204809514', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1435204830528', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1435204844982', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1435204913466', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1435204935170', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1438067036259', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447478131996', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447481970337', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447482084579', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447482100076', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447482204137', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447482231893', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447482246583', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447482467077', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447482550936', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447482631411', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447482732221', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447482832488', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447482929304', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447483027214', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447483173790', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447483286689', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447483369468', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447483445532', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447483470809', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447483516670', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447483560231', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447483677861', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1447483787413', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1463128779350', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1463128860936', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1463128876826', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1463129236782', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1515480990653', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1515481078740', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1515481087384', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1515481126744', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('1515481144092', 'rept', '1289630248984', null, 'rept', '1', '1289630248984');
INSERT INTO `t_syslog` VALUES ('2c9ba38561dbc2320161dbcd5d950001', 'admin', null, '2018-02-28 17:45:24', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402880264d5f56bd014d5f575d5e0001', 'admin', null, '2015-05-17 08:47:50', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402880845104697d0151046ae69a0001', 'admin', null, '2015-11-14 13:14:53', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402880845104a510015104a5aabd0001', 'admin', null, '2015-11-14 14:19:04', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402880845104c314015104c3d1160001', 'admin', null, '2015-11-14 14:52:00', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('40288117518ab85c01518add92160001', 'admin', null, '2015-12-10 15:49:15', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('40288117518af35501518af55ed30001', 'admin', null, '2015-12-10 16:15:15', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('40288117518af35501518af6b8db0002', 'admin', null, '2015-12-10 16:16:43', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('40288117518aff8601518b00b2a30001', 'admin', null, '2015-12-10 16:27:37', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('40288117518b02ad01518b07967c0001', 'admin', null, '2015-12-10 16:35:08', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('40288117518b277c01518b2a38b40001', 'admin', null, '2015-12-10 17:12:58', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881744c544d65014c5451db850002', 'admin', null, '2015-03-26 12:23:03', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881744c54b421014c54b4c09c0001', 'admin', null, '2015-03-26 14:11:14', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881744c54c7d0014c54cd6ade0001', 'admin', null, '2015-03-26 14:38:10', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881744c54d4c2014c54d688110001', 'admin', null, '2015-03-26 14:48:07', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881744c54dd1d014c54decd870001', 'admin', null, '2015-03-26 14:57:09', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881744c54e37c014c54e4293b0001', 'admin', null, '2015-03-26 15:03:01', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881744c54e572014c54e602b20001', 'admin', null, '2015-03-26 15:05:02', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881744c54e572014c54fe864b0002', 'admin', null, '2015-03-26 15:31:48', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881744c550060014c550143e30001', 'admin', null, '2015-03-26 15:34:48', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881744c593214014c5932bc040001', 'admin', null, '2015-03-27 11:07:19', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881a45d35175a015d351827c90001', 'admin', null, '2017-07-12 12:39:21', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881a45d351bca015d351e32be0001', 'admin', null, '2017-07-12 12:45:57', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881a460d979d40160d97f28380001', 'admin', null, '2018-01-09 13:57:57', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881a460d99f720160d9a01a6c0001', 'admin', null, '2018-01-09 14:33:56', '用户登录', '0', '192.168.1.118');
INSERT INTO `t_syslog` VALUES ('402881a460d99f720160d9a297290002', 'admin', null, '2018-01-09 14:36:39', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a460d99f720160d9a3862c0003', 'admin', null, '2018-01-09 14:37:40', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a460d99f720160d9b523cb0004', 'admin', null, '2018-01-09 14:56:55', '用户登录', '0', '192.168.1.10');
INSERT INTO `t_syslog` VALUES ('402881a460d99f720160d9b5e7c60005', 'admin', null, '2018-01-09 14:57:45', '用户登录', '0', '192.168.1.10');
INSERT INTO `t_syslog` VALUES ('402881a460d99f720160d9ba2aca0006', 'admin', null, '2018-01-09 15:02:24', '用户登录', '0', '192.168.1.10');
INSERT INTO `t_syslog` VALUES ('402881a460d99f720160d9be6f290007', 'admin', null, '2018-01-09 15:07:04', '用户登录', '0', '192.168.1.118');
INSERT INTO `t_syslog` VALUES ('402881a460d99f720160d9bebf8b0008', 'admin', null, '2018-01-09 15:07:24', '用户登录', '0', '192.168.1.118');
INSERT INTO `t_syslog` VALUES ('402881a46159f3b8016159f469390001', 'admin', null, '2018-02-03 12:37:25', '用户登录', '0', '192.168.1.118');
INSERT INTO `t_syslog` VALUES ('402881a46159fe8401615a026b5e0001', 'admin', null, '2018-02-03 12:52:43', '用户登录', '0', '192.168.1.118');
INSERT INTO `t_syslog` VALUES ('402881a46159fe8401615a0346ff0002', 'admin', null, '2018-02-03 12:53:39', '用户登录', '0', '192.168.1.118');
INSERT INTO `t_syslog` VALUES ('402881a46159fe8401615a306d860003', 'admin', null, '2018-02-03 13:42:58', '用户登录', '0', '192.168.1.118');
INSERT INTO `t_syslog` VALUES ('402881a46164a121016164b9497c0001', 'admin', null, '2018-02-05 14:48:39', '用户登录', '0', '192.168.1.118');
INSERT INTO `t_syslog` VALUES ('402881a46164a121016164f34ef90002', 'admin', null, '2018-02-05 15:52:02', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a46164a121016168b82c1a0003', 'admin', null, '2018-02-06 09:25:55', '用户登录', '0', '192.168.1.118');
INSERT INTO `t_syslog` VALUES ('402881a46164a121016168e05abe0004', 'admin', null, '2018-02-06 10:09:49', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a46164a121016168e683450005', 'admin', null, '2018-02-06 10:16:32', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a46164a121016168e9c0830006', 'admin', null, '2018-02-06 10:20:05', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a46164a121016168f674590007', 'admin', null, '2018-02-06 10:33:57', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a46164a121016169c7bf100008', 'admin', null, '2018-02-06 14:22:33', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a46164a121016169da44790009', 'admin', null, '2018-02-06 14:42:47', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a4616e1acd01616e1bce4a0001', 'admin', null, '2018-02-07 10:32:51', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881a4616e1acd01616e1f62d20002', 'admin', null, '2018-02-07 10:36:46', '用户登录', '0', '192.168.1.118');
INSERT INTO `t_syslog` VALUES ('402881a4616e1acd01616ef46fa10003', 'admin', null, '2018-02-07 14:29:28', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a4616e1acd01616ef87d280004', 'admin', null, '2018-02-07 14:33:54', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a4616e1acd01616ef960e20005', 'admin', null, '2018-02-07 14:34:52', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a4616e1acd01616ef9bf960006', 'admin', null, '2018-02-07 14:35:16', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a4616e1acd01616f1064d30007', 'admin', null, '2018-02-07 15:00:00', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a4616e1acd01616f5c08b10008', 'admin', null, '2018-02-07 16:22:37', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a46172d99201617454507b0001', 'admin', null, '2018-02-08 15:32:17', '用户登录', '0', '192.168.1.118');
INSERT INTO `t_syslog` VALUES ('402881a46172d99201617454b9e90002', 'admin', null, '2018-02-08 15:32:45', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a46172d992016174a9ec0d0003', 'admin', null, '2018-02-08 17:05:48', '用户登录', '0', '192.168.1.45');
INSERT INTO `t_syslog` VALUES ('402881a46172d99201617814a6c60004', 'admin', null, '2018-02-09 09:01:14', '用户登录', '0', '192.168.1.118');
INSERT INTO `t_syslog` VALUES ('402881a4617962e40161796341400001', 'admin', null, '2018-02-09 15:06:43', '用户登录', '0', '192.168.1.118');
INSERT INTO `t_syslog` VALUES ('402881bc4c9c7723014c9c819c9d0001', 'admin', null, '2015-04-09 12:48:02', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881bc4cfecaa1014cfecb0ff50001', 'admin', null, '2015-04-28 14:51:02', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881bc4cfecaa1014cfef29d7a0002', 'admin', null, '2015-04-28 15:34:15', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54d4c9206014d4c92a2e50001', 'admin', null, '2015-05-13 17:19:47', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54d4c9206014d4c92d0b70002', 'admin', null, '2015-05-13 17:19:59', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54d4c9ac9014d4c9bbcf70001', 'admin', null, '2015-05-13 17:29:44', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54d5065a5014d5069c93c0001', 'admin', null, '2015-05-14 11:13:39', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54d506ec9014d506f2f670001', 'admin', null, '2015-05-14 11:19:33', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54d5074af014d507647fa0001', 'admin', null, '2015-05-14 11:27:18', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54d56a48d014d56a69b1e0001', 'admin', null, '2015-05-15 16:17:48', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54dd78345014dd7838a6a0001', 'admin', null, '2015-06-09 16:50:31', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54dd78345014dd799b8f30002', 'admin', null, '2015-06-09 17:14:45', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54e28cfcf014e28d062e70001', 'admin', null, '2015-06-25 11:43:42', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54e28cfcf014e28d0b4420002', 'admin', null, '2015-06-25 11:44:03', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54e28cfcf014e28d2b6a10003', 'admin', null, '2015-06-25 11:46:14', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54e28cfcf014e28e37e570004', 'admin', null, '2015-06-25 12:04:34', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54e28cfcf014e28e3933b0005', 'admin', null, '2015-06-25 12:04:39', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54e28cfcf014e28e48d7f0006', 'admin', null, '2015-06-25 12:05:43', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54e488912014e488b93830001', 'admin', null, '2015-07-01 15:36:23', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881c54e488f9f014e488ff3a10001', 'admin', null, '2015-07-01 15:41:10', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881cf519f020001519f0430e80001', 'admin', null, '2015-12-14 13:43:50', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881cf519f1a8a01519f1bde2a0001', 'admin', null, '2015-12-14 14:09:42', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881dc4ed35813014ed3594b7f0001', 'admin', null, '2015-07-28 14:28:41', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881dc4ed35813014ed35c59740002', 'admin', null, '2015-07-28 14:32:01', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881dc4ed35813014ed365040e0003', 'admin', null, '2015-07-28 14:41:29', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881dc511e649501511e69a4fe0001', 'admin', null, '2015-11-19 14:23:38', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881dc51418290015141853f900001', 'admin', null, '2015-11-26 10:00:30', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881dc5252adf9015252b42c470001', 'admin', null, '2016-01-18 11:08:05', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881dc5252b971015252baba6b0001', 'admin', null, '2016-01-18 11:15:15', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('402881dc5252c4bd015252c52e590001', 'admin', null, '2016-01-18 11:26:39', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('4028e6c554a941590154a944e1d70001', 'admin', null, '2016-05-13 16:39:04', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('8a8a0e7c60c02a5d0160c02d6a0a0001', 'admin', null, '2018-01-04 15:58:09', '用户登录', '0', '127.0.0.1');
INSERT INTO `t_syslog` VALUES ('8a8a0e7c60c02a5d0160c031e9460002', 'admin', null, '2018-01-04 16:03:04', '用户登录', '0', '192.168.1.118');
INSERT INTO `t_syslog` VALUES ('8a8a0e7c60c02a5d0160c040eedf0003', 'admin', null, '2018-01-04 16:19:29', '用户登录', '0', '192.168.1.167');

-- ----------------------------
-- Table structure for t_task_useactor
-- ----------------------------
DROP TABLE IF EXISTS `t_task_useactor`;
CREATE TABLE `t_task_useactor` (
  `TASKID` varchar(32) NOT NULL,
  `TEST_PHASE` int(1) NOT NULL DEFAULT '0',
  `USERID` varchar(32) NOT NULL DEFAULT '0',
  `ACTOR` int(2) NOT NULL DEFAULT '0',
  `INSDATE` date DEFAULT NULL,
  `UPDDATE` date DEFAULT NULL,
  `ACTORID` varchar(32) NOT NULL,
  `IS_ENABLE` int(1) DEFAULT NULL,
  PRIMARY KEY (`ACTORID`),
  KEY `T_TASK_USEACTOR_IDX4` (`TASKID`) USING BTREE,
  KEY `FK7F50499D255E9362` (`TASKID`),
  KEY `FK7F50499DFEBAB499` (`TASKID`),
  KEY `FK7F50499D7712C00B` (`USERID`),
  CONSTRAINT `FK7F50499D255E9362` FOREIGN KEY (`TASKID`) REFERENCES `t_test_task_detail` (`TASKID`),
  CONSTRAINT `FK7F50499D7712C00B` FOREIGN KEY (`USERID`) REFERENCES `t_user` (`ID`),
  CONSTRAINT `FK7F50499DFEBAB499` FOREIGN KEY (`TASKID`) REFERENCES `t_single_test_task` (`TASKID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_task_useactor
-- ----------------------------
INSERT INTO `t_task_useactor` VALUES ('d75078f5c83f443a98c4c73b6378fd0b', '0', '1289630248984', '5', null, null, '40289f7e88be26240188be2decf20005', '1');
INSERT INTO `t_task_useactor` VALUES ('d75078f5c83f443a98c4c73b6378fd0b', '0', '1289630248984', '8', null, null, '40289f7e88be26240188be2decf20006', '1');
INSERT INTO `t_task_useactor` VALUES ('d75078f5c83f443a98c4c73b6378fd0b', '0', '1289630248984', '1', null, null, '40289f7e88be26240188be2decf20007', '1');
INSERT INTO `t_task_useactor` VALUES ('d75078f5c83f443a98c4c73b6378fd0b', '0', '1289630248984', '7', null, null, '40289f7e88be26240188be2decf20008', '1');

-- ----------------------------
-- Table structure for t_testcasebaseinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_testcasebaseinfo`;
CREATE TABLE `t_testcasebaseinfo` (
  `TASKID` varchar(32) DEFAULT '',
  `TEST_PHASE` int(1) DEFAULT '0',
  `MODULEID` bigint(20) DEFAULT NULL,
  `TESTCASEID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CREATERID` varchar(32) DEFAULT '0',
  `TESTCASEDES` varchar(1000) DEFAULT NULL,
  `TESTCASEOPERDATA` varchar(4000) DEFAULT NULL,
  `EXPRESULT` varchar(4000) DEFAULT NULL,
  `VALPROC` varchar(4000) DEFAULT NULL,
  `ISRELEASED` int(1) DEFAULT NULL,
  `CREATVER` varchar(20) DEFAULT NULL,
  `CREATDATE` datetime DEFAULT NULL,
  `ATTCHURL` varchar(200) DEFAULT NULL,
  `INSDATE` datetime DEFAULT NULL,
  `UPDDATE` datetime DEFAULT NULL,
  `STEPID` varchar(32) DEFAULT NULL,
  `STATUS` int(2) DEFAULT NULL,
  `CASETYPE` bigint(20) DEFAULT NULL,
  `WEIGHT` int(3) DEFAULT NULL,
  `EXCEPTRESULT` varchar(4000) DEFAULT NULL,
  `TESTACTOR` varchar(32) DEFAULT NULL,
  `EXE_DATE` datetime DEFAULT NULL,
  `CURRENT_VERSION` varchar(30) DEFAULT NULL,
  `OPERDATA_RICHTEXT` varchar(1200) DEFAULT NULL,
  `CASE_PRI` bigint(20) DEFAULT NULL,
  `REMARK` varchar(1000) DEFAULT NULL,
  `MODULENUM` varchar(60) DEFAULT NULL,
  `TEST_SEQ` int(3) DEFAULT NULL,
  `INIT_SEQ` int(3) DEFAULT NULL,
  `ADUIT_ID` varchar(32) DEFAULT NULL,
  `PREFIXCONDITION` varchar(1000) DEFAULT NULL COMMENT '执行备注',
  `EXE_VERSION` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`TESTCASEID`),
  KEY `TEST_CASE_TASK_IN` (`TASKID`,`MODULEID`) USING BTREE,
  KEY `FKECB3544CC320653D` (`MODULEID`),
  CONSTRAINT `FKECB3544CC320653D` FOREIGN KEY (`MODULEID`) REFERENCES `t_outlineinfo` (`MODULEID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_testcasebaseinfo
-- ----------------------------

-- ----------------------------
-- Table structure for t_testcasepackage
-- ----------------------------
DROP TABLE IF EXISTS `t_testcasepackage`;
CREATE TABLE `t_testcasepackage` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `taskId` varchar(255) DEFAULT NULL COMMENT '项目id',
  `package_name` varchar(255) DEFAULT NULL COMMENT '测试用例包名称',
  `executor` varchar(255) DEFAULT NULL COMMENT '可执行人',
  `exec_environment` varchar(255) DEFAULT NULL COMMENT '执行环境',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `createId` varchar(255) DEFAULT NULL,
  `updateId` varchar(255) DEFAULT NULL,
  `createrId` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_testcasepackage
-- ----------------------------

-- ----------------------------
-- Table structure for t_testcasetype
-- ----------------------------
DROP TABLE IF EXISTS `t_testcasetype`;
CREATE TABLE `t_testcasetype` (
  `TYPE_NAME` varchar(30) DEFAULT NULL,
  `TYPE_ID` bigint(20) NOT NULL,
  `COMP_ID` varchar(32) NOT NULL,
  PRIMARY KEY (`TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_testcasetype
-- ----------------------------

-- ----------------------------
-- Table structure for t_testcase_casepkg
-- ----------------------------
DROP TABLE IF EXISTS `t_testcase_casepkg`;
CREATE TABLE `t_testcase_casepkg` (
  `id` varchar(32) NOT NULL,
  `packageId` varchar(32) DEFAULT NULL,
  `testcaseId` varchar(32) DEFAULT NULL,
  `executorId` varchar(32) DEFAULT NULL,
  `execStatus` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_testcase_casepkg
-- ----------------------------

-- ----------------------------
-- Table structure for t_testcase_library
-- ----------------------------
DROP TABLE IF EXISTS `t_testcase_library`;
CREATE TABLE `t_testcase_library` (
  `library_id` varchar(32) NOT NULL COMMENT '测试用例库唯一id',
  `library_code` varchar(64) NOT NULL COMMENT '编码',
  `testcase_type` varchar(64) DEFAULT NULL COMMENT '用例类别',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父id，用于构造树',
  `create_user_id` varchar(32) DEFAULT NULL COMMENT '用例类别创建人',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`library_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_testcase_library
-- ----------------------------

-- ----------------------------
-- Table structure for t_testcase_library_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_testcase_library_detail`;
CREATE TABLE `t_testcase_library_detail` (
  `TASKID` varchar(32) DEFAULT '',
  `TEST_PHASE` int(1) DEFAULT '0',
  `MODULEID` bigint(20) DEFAULT NULL,
  `TESTCASEID` varchar(32) NOT NULL,
  `CREATERID` varchar(32) DEFAULT '0',
  `TESTCASEDES` varchar(1000) DEFAULT NULL,
  `TESTCASEOPERDATA` varchar(4000) DEFAULT NULL,
  `EXPRESULT` varchar(4000) DEFAULT NULL,
  `VALPROC` varchar(4000) DEFAULT NULL,
  `ISRELEASED` int(1) DEFAULT NULL,
  `CREATVER` varchar(20) DEFAULT NULL,
  `CREATDATE` datetime DEFAULT NULL,
  `ATTCHURL` varchar(200) DEFAULT NULL,
  `INSDATE` datetime DEFAULT NULL,
  `UPDDATE` datetime DEFAULT NULL,
  `STEPID` varchar(32) DEFAULT NULL,
  `STATUS` int(2) DEFAULT NULL,
  `CASETYPE` bigint(20) DEFAULT NULL,
  `WEIGHT` int(3) DEFAULT NULL,
  `EXCEPTRESULT` varchar(4000) DEFAULT NULL,
  `TESTACTOR` varchar(32) DEFAULT NULL,
  `EXE_DATE` datetime DEFAULT NULL,
  `CURRENT_VERSION` varchar(30) DEFAULT NULL,
  `OPERDATA_RICHTEXT` varchar(1200) DEFAULT NULL,
  `CASE_PRI` bigint(20) DEFAULT NULL,
  `REMARK` varchar(1000) DEFAULT NULL,
  `MODULENUM` varchar(60) DEFAULT NULL,
  `TEST_SEQ` int(3) DEFAULT NULL,
  `INIT_SEQ` int(3) DEFAULT NULL,
  `ADUIT_ID` varchar(32) DEFAULT NULL,
  `PREFIXCONDITION` varchar(1000) DEFAULT NULL COMMENT '执行备注',
  `EXE_VERSION` varchar(32) DEFAULT NULL,
  `library_code` varchar(64) DEFAULT NULL COMMENT '编码',
  `library_id` varchar(32) DEFAULT NULL COMMENT '用例所属类别',
  `recommend_user_id` varchar(32) DEFAULT NULL COMMENT '推荐者id',
  `recommend_reason` varchar(255) DEFAULT NULL COMMENT '推荐理由',
  `examine_status` varchar(1) DEFAULT NULL COMMENT '审核状态（0待审核，1审核通过）',
  `examine_user_id` varchar(32) DEFAULT NULL COMMENT '审核人id',
  `number` bigint(20) DEFAULT NULL COMMENT '编号',
  PRIMARY KEY (`TESTCASEID`),
  KEY `TEST_CASE_TASK_IN` (`TASKID`,`MODULEID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_testcase_library_detail
-- ----------------------------

-- ----------------------------
-- Table structure for t_testflowinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_testflowinfo`;
CREATE TABLE `t_testflowinfo` (
  `TASKID` varchar(32) NOT NULL,
  `TEST_PHASE` int(1) NOT NULL DEFAULT '0',
  `TESTFLOWCODE` int(2) NOT NULL DEFAULT '0',
  `TESTFLOWNAME` varchar(50) DEFAULT NULL,
  `INSDATE` date DEFAULT NULL,
  `UPDDATE` date DEFAULT NULL,
  `TESTFLOWID` varchar(32) NOT NULL,
  `ID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`TESTFLOWID`),
  KEY `T_TESTFLOWINFO_IDX4` (`TASKID`) USING BTREE,
  KEY `FKC93F2B79255E9362` (`TASKID`),
  CONSTRAINT `FKC93F2B79255E9362` FOREIGN KEY (`TASKID`) REFERENCES `t_test_task_detail` (`TASKID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_testflowinfo
-- ----------------------------
INSERT INTO `t_testflowinfo` VALUES ('d75078f5c83f443a98c4c73b6378fd0b', '0', '5', null, null, null, '40289f7e88be26240188be2decf10001', null);
INSERT INTO `t_testflowinfo` VALUES ('d75078f5c83f443a98c4c73b6378fd0b', '0', '1', null, null, null, '40289f7e88be26240188be2decf10002', null);
INSERT INTO `t_testflowinfo` VALUES ('d75078f5c83f443a98c4c73b6378fd0b', '0', '8', null, null, null, '40289f7e88be26240188be2decf20003', null);
INSERT INTO `t_testflowinfo` VALUES ('d75078f5c83f443a98c4c73b6378fd0b', '0', '7', null, null, null, '40289f7e88be26240188be2decf20004', null);

-- ----------------------------
-- Table structure for t_testresult
-- ----------------------------
DROP TABLE IF EXISTS `t_testresult`;
CREATE TABLE `t_testresult` (
  `TASKID` varchar(32) NOT NULL,
  `TEST_PHASE` int(1) DEFAULT '0',
  `MODULEID` bigint(20) NOT NULL,
  `TESTCASEID` bigint(20) NOT NULL,
  `TESTVER` varchar(30) NOT NULL DEFAULT '0',
  `TESTRESULT` int(2) DEFAULT '0',
  `TESTRESULTDATE` datetime DEFAULT NULL,
  `TESTACTOR` varchar(32) DEFAULT '0',
  `INSDATE` datetime DEFAULT NULL,
  `UPDDATE` datetime DEFAULT NULL,
  `RESULTID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TESTCONFIGID` bigint(20) DEFAULT NULL,
  `REMARK` varchar(1000) DEFAULT NULL,
  `SEQ` int(3) DEFAULT NULL,
  `PLANT_FORM_ID` int(15) DEFAULT NULL,
  PRIMARY KEY (`RESULTID`),
  KEY `T_TESTRESULT_IDX4` (`TASKID`,`TEST_PHASE`) USING BTREE,
  KEY `FK7F215BFA62A4FE88` (`TESTACTOR`),
  KEY `FK7F215BFA2F3A627` (`TESTCASEID`),
  CONSTRAINT `FK7F215BFA2F3A627` FOREIGN KEY (`TESTCASEID`) REFERENCES `t_testcasebaseinfo` (`TESTCASEID`),
  CONSTRAINT `FK7F215BFA62A4FE88` FOREIGN KEY (`TESTACTOR`) REFERENCES `t_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_testresult
-- ----------------------------

-- ----------------------------
-- Table structure for t_test_cycle_task
-- ----------------------------
DROP TABLE IF EXISTS `t_test_cycle_task`;
CREATE TABLE `t_test_cycle_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) DEFAULT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `task_id` varchar(45) DEFAULT NULL,
  `exe_env` varchar(500) DEFAULT NULL,
  `cycle_type` int(11) DEFAULT NULL,
  `plan_start_date` datetime DEFAULT NULL,
  `plan_end_date` datetime DEFAULT NULL,
  `fact_start_date` datetime DEFAULT NULL,
  `fact_end_date` datetime DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_id` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `result` varchar(200) DEFAULT NULL,
  `update_id` varchar(255) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_test_cycle_task
-- ----------------------------

-- ----------------------------
-- Table structure for t_test_task_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_test_task_detail`;
CREATE TABLE `t_test_task_detail` (
  `TASKID` varchar(32) NOT NULL,
  `TEST_PHASE` int(1) DEFAULT '0',
  `CURRENT_VERSION` varchar(30) DEFAULT '',
  `RELA_TASKID` varchar(32) DEFAULT NULL,
  `REAL_PHASE` int(1) DEFAULT '0',
  `RELTTESTCASEFLAG` int(1) NOT NULL DEFAULT '0',
  `OUTLINESTATE` int(1) DEFAULT '0',
  `MAILBUG` int(1) DEFAULT '0',
  `MAILQUESTION` int(1) DEFAULT '0',
  `MAILEDITION` int(1) DEFAULT '0',
  `MAILCONFIRM` int(1) DEFAULT NULL,
  `UPGRADEFLAG` int(1) DEFAULT NULL,
  `INSDATE` date DEFAULT NULL,
  `UPDDATE` date DEFAULT NULL,
  `PJSIZE` double(20,3) DEFAULT NULL,
  `COMPANYID` varchar(32) NOT NULL,
  `CUSTOM_CASE` int(1) DEFAULT NULL,
  `CUSTOM_BUG` int(1) DEFAULT NULL,
  `CREATE_ID` varchar(32) DEFAULT NULL,
  `TEST_TASK_TATE` int(1) DEFAULT NULL,
  `MAILREPORT` int(1) DEFAULT NULL,
  `PROJECTID` varchar(32) DEFAULT NULL,
  `TEST_SEQ` int(3) DEFAULT NULL,
  PRIMARY KEY (`TASKID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_test_task_detail
-- ----------------------------
INSERT INTO `t_test_task_detail` VALUES ('d75078f5c83f443a98c4c73b6378fd0b', null, null, null, '0', '0', '1', null, null, '0', null, null, null, null, null, '1289630248328', '0', '0', null, '0', null, null, '1');

-- ----------------------------
-- Table structure for t_ticketinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_ticketinfo`;
CREATE TABLE `t_ticketinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bus_num` varchar(255) DEFAULT NULL,
  `bus_start_time` varchar(255) DEFAULT NULL,
  `buy_time` varchar(255) DEFAULT NULL,
  `certi_type` varchar(255) DEFAULT NULL,
  `dest_station_name` varchar(255) DEFAULT NULL,
  `identity_id` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `seat_num` varchar(255) DEFAULT NULL,
  `start_station_name` varchar(255) DEFAULT NULL,
  `station_code` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `ticket_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of t_ticketinfo
-- ----------------------------

-- ----------------------------
-- Table structure for t_ticketstatus
-- ----------------------------
DROP TABLE IF EXISTS `t_ticketstatus`;
CREATE TABLE `t_ticketstatus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `brake_machine` varchar(255) DEFAULT NULL,
  `card_num` varchar(255) DEFAULT NULL,
  `card_type` varchar(255) DEFAULT NULL,
  `operation_state` varchar(255) DEFAULT NULL,
  `operation_time` datetime DEFAULT NULL,
  `ticket_sign` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of t_ticketstatus
-- ----------------------------

-- ----------------------------
-- Table structure for t_typedefine
-- ----------------------------
DROP TABLE IF EXISTS `t_typedefine`;
CREATE TABLE `t_typedefine` (
  `INDENTIFIER` int(2) NOT NULL DEFAULT '0',
  `ENUMID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SIMPNAME` varchar(8) DEFAULT NULL,
  `ENUMNAME` varchar(100) NOT NULL,
  `REMARK` varchar(300) DEFAULT NULL,
  `ISDEFAULT` int(1) DEFAULT '0',
  `INSDATE` date DEFAULT NULL,
  `UPDDATE` date DEFAULT NULL,
  `COMP_ID` varchar(32) DEFAULT NULL,
  `STATUS_FLG` varchar(2) DEFAULT '1',
  `PREFERENCE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ENUMID`),
  UNIQUE KEY `TYPEDEFINE_UQ` (`INDENTIFIER`,`ENUMNAME`,`COMP_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_typedefine
-- ----------------------------
INSERT INTO `t_typedefine` VALUES ('8', '7', null, '高', null, '1', null, '2018-09-28', '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('8', '8', null, '中', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('8', '9', null, '低', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('7', '10', null, '功能', null, '1', null, '2018-09-05', '1', '2', null);
INSERT INTO `t_typedefine` VALUES ('7', '11', null, '性能', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('7', '12', null, '可用性', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('0', '13', null, '功能', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('0', '14', null, '性能', null, '1', null, '2018-09-28', '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('0', '15', null, '可用性', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('10', '16', null, '测试遗漏', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('10', '17', null, '需求变更', null, '1', null, '2011-10-11', '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('10', '18', null, '需求新增', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('10', '19', null, '此前测试策略忽略', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('2', '20', null, '严重影响系统运行', null, '1', null, '2018-09-05', '1', '3', null);
INSERT INTO `t_typedefine` VALUES ('2', '21', null, '影响系统运行', null, '1', null, '2013-08-06', '1', '2', null);
INSERT INTO `t_typedefine` VALUES ('2', '22', null, '不影响运行但必须改', null, '1', null, '2013-08-06', '1', '2', null);
INSERT INTO `t_typedefine` VALUES ('2', '23', null, '所提出的建议', null, '1', null, '2018-09-05', '1', '3', null);
INSERT INTO `t_typedefine` VALUES ('3', '24', null, '修改后出现', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('3', '25', null, '已发现但未修改', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('3', '26', null, '首次出现', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('4', '27', null, '操作即出现', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('4', '28', null, '偶尔出现', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('5', '29', null, '测试人员', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('5', '30', null, '研发人员', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('5', '31', null, '客户', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('6', '32', null, 'Linux+IE7(IE8)', null, '1', null, '2012-08-06', '1', '2', null);
INSERT INTO `t_typedefine` VALUES ('6', '33', null, 'Windows +IE7(FF3.5)', null, '1', null, '2012-08-06', '1', '2', null);
INSERT INTO `t_typedefine` VALUES ('6', '34', null, 'MAC+FF3.5', null, '1', null, '2012-08-06', '1', '2', null);
INSERT INTO `t_typedefine` VALUES ('9', '35', null, '高', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('9', '36', null, '中', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('9', '37', null, '低', null, '1', null, null, '1', '1', null);
INSERT INTO `t_typedefine` VALUES ('1', '38', null, '需求', null, '1', null, '2012-02-20', '1', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '39', null, '系统设计', null, '1', null, '2012-02-20', '1', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '40', null, '概要设计', null, '1', null, '2012-02-20', '1', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '41', null, '详细设计', null, '1', null, '2011-10-12', '1', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '42', null, '编码', null, '1', null, '2012-02-20', '1', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '43', null, '集成测试', null, '1', null, '2012-02-20', '1', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '44', null, '系统测试', null, '1', null, '2012-02-20', '1', '2', null);
INSERT INTO `t_typedefine` VALUES ('6', '45', null, '所有', '', '0', null, '2010-11-13', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('3', '46', null, '首轮测试', '', '0', null, '2012-01-18', '1289630248328', '3', null);
INSERT INTO `t_typedefine` VALUES ('3', '47', null, 'aaa', '', '0', null, '2011-03-23', '1289630248328', '3', null);
INSERT INTO `t_typedefine` VALUES ('10', '48', null, '新特性', '', '0', null, '2012-02-17', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('10', '49', null, '需求不明确', '', '0', null, '2012-01-18', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('2', '50', null, '关键', '', '0', null, '2013-08-06', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('1', '51', null, '系统实施', '', '0', null, '2012-02-20', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '52', null, '维护期', '', '0', null, '2012-02-20', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('6', '53', null, 'windows7+IE8', '', '0', null, '2013-11-19', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('10', '54', null, '未曾测试', '', '0', null, '2012-02-10', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('2', '55', null, '用户要求改的需求', '', '0', null, '2012-02-10', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('4', '56', null, '新需求', '', '0', null, '2012-02-17', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('1', '57', null, '设计疏忽', '', '0', null, '2014-02-10', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '58', null, '语法不熟练', '', '0', null, '2014-02-10', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '59', null, '违反规约', '', '0', null, '2014-02-10', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '60', null, '复用故障', '', '0', null, '2014-02-10', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '61', null, '修改故障', '', '0', null, '2014-02-10', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '62', null, '简单疏忽', '', '0', null, '2014-02-10', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '63', null, '沟通不畅', '', '0', null, '2014-02-10', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '64', null, '配置相关', '', '0', null, '2014-02-10', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '65', null, '安装部署', '', '0', null, '2014-02-10', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '66', null, '性能不佳', '', '0', null, '2014-02-10', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '67', null, '测试脚本', '', '0', null, '2014-02-10', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '68', null, '测试疏忽', '', '0', null, '2014-02-10', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '69', null, '第三方BUG', '', '0', null, '2014-02-10', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('1', '70', null, '其他', '', '0', null, '2014-02-10', '1289630248328', '3', null);
INSERT INTO `t_typedefine` VALUES ('1', '71', null, '新需求', '', '0', null, '2014-02-10', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('2', '72', null, '建议修正', '', '0', null, '2018-09-06', '1289630248328', '3', null);
INSERT INTO `t_typedefine` VALUES ('6', '73', null, 'windows+谷歌8', '', '0', null, '2012-02-27', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('7', '74', null, '场景', '', '0', null, '2012-02-29', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('6', '75', null, 'Win7+360浏览器', '3.14正式版', '0', null, '2013-07-09', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('6', '76', null, 'winXP+IE8', '', '0', null, '2013-11-19', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('6', '77', null, 'Windows7+谷歌9', '', '0', null, '2013-11-19', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('6', '78', null, 'Window7+IE9', '', '0', null, '2012-05-07', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('6', '79', null, 'WinXP+谷歌9', '', '0', null, '2013-11-19', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('6', '80', null, 'winXP+IE6', '', '0', null, '2013-11-19', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('5', '81', null, '文登-徐丹丹', '', '0', null, '2012-07-17', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('5', '82', null, '文登-李艳玲', '', '0', null, '2012-07-17', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('6', '83', null, 'WinXP+Chrome22', '', '0', null, '2013-07-08', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('2', '84', null, '轻微', '', '0', null, '2012-10-22', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('6', '85', null, 'Win7+Chrome19', '', '0', null, '2013-07-08', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('6', '86', null, 'WinXP+搜狗', '', '0', null, '2013-07-09', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('6', '87', null, 'Win7+Chrome27', '', '0', null, '2013-11-19', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('6', '88', null, 'Win7+Firefox22', '', '0', null, '2018-09-05', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('6', '89', null, 'WinXP+Chrome27', '', '0', null, '2013-11-19', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('6', '90', null, 'WinXP+Safari5', '', '0', null, '2013-07-08', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('6', '91', null, 'Win7+safari5', '', '0', null, '2013-12-19', '1289630248328', '3', null);
INSERT INTO `t_typedefine` VALUES ('6', '92', null, 'WinXP+Firefox22', '', '0', null, '2013-12-19', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('2', '93', null, '严重', '', '0', null, '2018-09-05', '1289630248328', '3', null);
INSERT INTO `t_typedefine` VALUES ('2', '94', null, '一般', '', '0', null, '2018-09-05', '1289630248328', '3', null);
INSERT INTO `t_typedefine` VALUES ('6', '95', null, 'WinXP+Chrome28', '', '0', null, '2013-08-08', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('6', '96', null, 'win7+谷歌28', '', '0', null, '2013-08-13', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('0', '97', null, '随机问题', '测试人员提交备注概率和bug级别', '0', null, '2013-08-19', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('6', '98', null, 'WinXP+Chrome29', '', '0', null, '2013-09-02', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('7', '99', null, '界面问题', '', '0', null, '2013-09-03', '1289630248328', '3', null);
INSERT INTO `t_typedefine` VALUES ('0', '100', null, '界面问题', '', '0', null, '2013-09-03', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('6', '101', null, '安卓2.3', '', '0', null, '2013-10-09', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('6', '102', null, 'WinXP+Chrome31', '', '0', null, '2013-11-19', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('5', '103', null, 'created by', 'bug始发者', '0', null, '2014-01-06', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('1', '104', null, '需求分析', '与客户沟通出现问题或需求分析阶段因分析不', '0', null, '2014-02-10', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('1', '105', null, '需求理解', '对需求不熟或分析不透彻导致的bug', '0', null, '2014-02-10', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('1', '106', null, '设计问题', '设计疏忽或设计错误导致的bug', '0', null, '2014-02-10', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('1', '107', null, '框架问题', 'wake平台的bug', '0', null, '2014-02-10', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('1', '108', null, '技术能力', '因开发技术问题，出现的不可避免的bug', '0', null, '2014-02-10', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('1', '109', null, '个人疏忽', '因马虎大意或未充分自测等导致的BUG；重', '0', null, '2014-02-10', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('1', '110', null, '需求变更', '需求新增或需求变更记录', '0', null, '2014-02-10', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('1', '111', null, '环境部署', '系统安装部署差错引出的BUG；测试环境配', '0', null, '2014-02-10', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('1', '112', null, '无效问题', '状态为撤销、重复、无效、非错、关闭/撤销', '0', null, '2014-02-10', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('1', '113', null, '随机问题', '状态为挂起/需要提供更多信息或者需要跟踪', '0', null, '2014-02-10', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('1', '117', null, '其 他', '界面问题，性能问题，开发人员记录问题等', '0', null, '2014-02-10', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('6', '118', null, 'win7+ie8', '', '0', null, '2014-02-24', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('6', '119', null, 'win7+猎豹', '', '0', null, '2014-02-24', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('6', '120', null, 'win7+360', '', '0', null, '2014-02-24', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('6', '121', null, 'win7+ie10', '', '0', null, '2014-02-24', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('6', '122', null, 'win7+ firefox', '', '0', null, '2014-02-24', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('6', '123', null, 'win7+谷歌34', '', '0', null, '2018-09-05', '1289630248328', '3', null);
INSERT INTO `t_typedefine` VALUES ('7', '124', null, '体验', '导入用例自动生成 ', '0', null, '2018-10-08', '1289630248328', '2', null);
INSERT INTO `t_typedefine` VALUES ('6', '125', null, '其他', '', '0', null, '2018-09-05', '1289630248328', '1', null);
INSERT INTO `t_typedefine` VALUES ('8', '134', null, '111', '', '0', null, '2018-08-09', '1289630248328', '3', null);
INSERT INTO `t_typedefine` VALUES ('2', '135', null, '建议修正11', '', '0', null, '2018-09-06', '1289630248328', '3', null);
INSERT INTO `t_typedefine` VALUES ('2', '143', null, 'xiugai', '', '0', null, '2018-09-06', '1289630248328', '3', null);
INSERT INTO `t_typedefine` VALUES ('2', '144', null, '建议更正', '', '0', null, '2018-09-06', '1289630248328', '1', null);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `ID` varchar(32) NOT NULL,
  `COMPANYID` varchar(32) NOT NULL,
  `LOGINNAME` varchar(32) NOT NULL,
  `NAME` varchar(32) NOT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `ISADMIN` int(1) NOT NULL DEFAULT '0',
  `EMPLOYEEID` varchar(32) DEFAULT NULL,
  `STATUS` int(1) DEFAULT '0',
  `ACTIVEFLAG` int(1) DEFAULT '0',
  `EMAIL` varchar(32) NOT NULL,
  `TEL` varchar(20) DEFAULT NULL,
  `QUESTION` varchar(20) DEFAULT NULL,
  `ANSWER` varchar(50) DEFAULT NULL,
  `INSERTDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATEDATE` timestamp NOT NULL DEFAULT '2010-01-11 00:00:00',
  `DELFLAG` bigint(11) unsigned NOT NULL DEFAULT '0',
  `OFFICETEL` varchar(20) DEFAULT NULL,
  `HEADSHIP` varchar(255) DEFAULT NULL,
  `ENTRY_DATE` datetime DEFAULT NULL,
  `JOIN_COMP_DATE` datetime DEFAULT NULL,
  `MY_HOME` varchar(300) DEFAULT NULL,
  `MY_SUPER_HOME` varchar(150) DEFAULT NULL,
  `RELA_DOC_USER_ID` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `T_USER_UQ` (`LOGINNAME`,`DELFLAG`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1289630248984', '1289630248328', 'admin', 'admin', 'd033e22ae348aeb566fc214aec3585c4da997', '1', null, '1', '0', 'liuygneusoft@163.com', null, null, null, '2018-10-19 15:12:38', '2018-10-19 15:41:58', '0', null, null, null, null, '', null, null);

-- ----------------------------
-- Table structure for t_user_group_rela
-- ----------------------------
DROP TABLE IF EXISTS `t_user_group_rela`;
CREATE TABLE `t_user_group_rela` (
  `GROUPID` varchar(32) NOT NULL,
  `USERID` varchar(32) NOT NULL,
  `INSERTDATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATEDATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`GROUPID`,`USERID`),
  KEY `T_USER_GROUP_RELA_FK1` (`USERID`) USING BTREE,
  CONSTRAINT `t_user_group_rela_ibfk_1` FOREIGN KEY (`GROUPID`) REFERENCES `t_group` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `t_user_group_rela_ibfk_2` FOREIGN KEY (`USERID`) REFERENCES `t_user` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 10240 kB; (`GROUPID`) REFER `mypmbugtrace/t_gro';

-- ----------------------------
-- Records of t_user_group_rela
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_other_mission
-- ----------------------------
DROP TABLE IF EXISTS `t_user_other_mission`;
CREATE TABLE `t_user_other_mission` (
  `user_mission_id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL DEFAULT '' COMMENT '其他任务分配人id',
  `mission_id` varchar(32) NOT NULL COMMENT '其他任务id',
  PRIMARY KEY (`user_mission_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_other_mission
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_role_real
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role_real`;
CREATE TABLE `t_user_role_real` (
  `ROLEID` varchar(32) NOT NULL,
  `USERID` varchar(32) NOT NULL,
  PRIMARY KEY (`ROLEID`,`USERID`),
  KEY `FKD6AD839E71BD6AA1` (`ROLEID`),
  KEY `FKD6AD839E7712C00B` (`USERID`),
  KEY `FKD6AD839EEE68C15D` (`USERID`),
  CONSTRAINT `FKD6AD839E71BD6AA1` FOREIGN KEY (`ROLEID`) REFERENCES `t_role` (`ROLEID`),
  CONSTRAINT `FKD6AD839E7712C00B` FOREIGN KEY (`USERID`) REFERENCES `t_user` (`ID`),
  CONSTRAINT `FKD6AD839EEE68C15D` FOREIGN KEY (`USERID`) REFERENCES `t_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_role_real
-- ----------------------------
INSERT INTO `t_user_role_real` VALUES ('402880ea25b62b6a0125b66873290012', '1289630248984');

-- ----------------------------
-- Table structure for t_user_testcasepkg
-- ----------------------------
DROP TABLE IF EXISTS `t_user_testcasepkg`;
CREATE TABLE `t_user_testcasepkg` (
  `id` varchar(32) NOT NULL,
  `packageId` varchar(32) DEFAULT NULL,
  `userId` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `t_user_testcasepkg_ibfk_1` (`packageId`),
  CONSTRAINT `t_user_testcasepkg_ibfk_1` FOREIGN KEY (`packageId`) REFERENCES `t_testcasepackage` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_testcasepkg
-- ----------------------------

-- ----------------------------
-- View structure for v_expbugview
-- ----------------------------
DROP VIEW IF EXISTS `v_expbugview`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER  VIEW `v_expbugview` AS select `t`.`BUGCARDID` AS `bugcardid`,`t`.`BUGDESC` AS `bugDesc`,`t`.`REPRODUCT_TXT` AS `reproduct_txt`,`t`.`CURRENT_STATE` AS `current_state`,`tplevel`.`ENUMNAME` AS `levelName`,`tptype`.`ENUMNAME` AS `typeName`,`tpocca`.`ENUMNAME` AS `occaName`,`urep`.`NAME` AS `reptName`,`t`.`BUGDISVDATE` AS `reptDate`,`vrep`.`VERSION_NUM` AS `disVer`,`ufix`.`NAME` AS `fixName`,`t`.`FIX_DATE` AS `fixDate`,`ucls`.`NAME` AS `clsName`,`t`.`CURRENT_HANDL_DATE` AS `current_handl_date`,`vres`.`VERSION_NUM` AS `reslVer`,`o`.`MODULENAME` AS `modulename`,`o2`.`MODULENAME` AS `superMelName` from ((((((((((`t_bugbaseinfo` `t` join `t_typedefine` `tplevel` on((`tplevel`.`ENUMID` = `t`.`BUGLEVEL`))) join `t_typedefine` `tptype` on((`tptype`.`ENUMID` = `t`.`BUGTYPE`))) join `t_typedefine` `tpocca` on((`tpocca`.`ENUMID` = `t`.`BUGOCCA`))) join `t_user` `urep` on((`t`.`BUGDISVPERID` = `urep`.`ID`))) join `t_software_version` `vrep` on((`vrep`.`VERSION_ID` = `t`.`DISCOVER_VER`))) left join `t_user` `ufix` on((`ufix`.`ID` = `t`.`DEV_OWNER`))) left join `t_user` `ucls` on((`ucls`.`ID` = `t`.`BUG_NUM`))) left join `t_software_version` `vres` on((`vres`.`VERSION_ID` = `t`.`FIX_VERSION`))) join `t_outlineinfo` `o` on((`o`.`MODULEID` = `t`.`MODULEID`))) join `t_outlineinfo` `o2` on((`o2`.`MODULEID` = `o`.`SUPERMODULEID`))) order by `t`.`CURRENT_HANDL_DATE` desc; ;

-- ----------------------------
-- View structure for v_expbugview2
-- ----------------------------
DROP VIEW IF EXISTS `v_expbugview2`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER  VIEW `v_expbugview2` AS select `t`.`BUGCARDID` AS `bugcardid`,`t`.`BUGDESC` AS `bugDesc`,`t`.`REPRODUCT_TXT` AS `reproduct_txt`,`t`.`CURRENT_STATE` AS `current_state`,`tplevel`.`ENUMNAME` AS `levelName`,`tptype`.`ENUMNAME` AS `typeName`,`tpocca`.`ENUMNAME` AS `occaName`,`urep`.`NAME` AS `reptName`,`t`.`BUGDISVDATE` AS `reptDate`,`vrep`.`VERSION_NUM` AS `disVer`,`ufix`.`NAME` AS `fixName`,`t`.`FIX_DATE` AS `fixDate`,`ucls`.`NAME` AS `clsName`,`t`.`CURRENT_HANDL_DATE` AS `current_handl_date`,`vres`.`VERSION_NUM` AS `reslVer`,`o`.`MODULENAME` AS `modulename`,`o2`.`MODULENAME` AS `superMelName` from ((((((((((`t_bugbaseinfo` `t` join `t_typedefine` `tplevel` on((`tplevel`.`ENUMID` = `t`.`BUGLEVEL`))) join `t_typedefine` `tptype` on((`tptype`.`ENUMID` = `t`.`BUGTYPE`))) join `t_typedefine` `tpocca` on((`tpocca`.`ENUMID` = `t`.`BUGOCCA`))) join `t_user` `urep` on((`t`.`BUGDISVPERID` = `urep`.`ID`))) join `t_software_version` `vrep` on((`vrep`.`VERSION_ID` = `t`.`DISCOVER_VER`))) left join `t_user` `ufix` on((`ufix`.`ID` = `t`.`DEV_OWNER`))) left join `t_user` `ucls` on((`ucls`.`ID` = `t`.`CURRENT_HANDLER`))) left join `t_software_version` `vres` on((`vres`.`VERSION_ID` = `t`.`FIX_VERSION`))) join `t_outlineinfo` `o` on((`o`.`MODULEID` = `t`.`MODULEID`))) join `t_outlineinfo` `o2` on((`o2`.`MODULEID` = `o`.`SUPERMODULEID`))) order by `t`.`CURRENT_HANDL_DATE` desc; ;

-- ----------------------------
-- View structure for v_expcaseview
-- ----------------------------
DROP VIEW IF EXISTS `v_expcaseview`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER  VIEW `v_expcaseview` AS select `t`.`TESTCASEID` AS `testcaseid`,`o2`.`MODULENAME` AS `superMelName`,`o`.`MODULENAME` AS `modulename`,`t`.`TESTCASEDES` AS `testcasedes`,`t`.`TESTCASEOPERDATA` AS `testcaseoperdata`,`t`.`EXPRESULT` AS `EXPRESULT`,`t`.`STATUS` AS `status`,`u`.`NAME` AS `author`,`u2`.`NAME` AS `exeName`,`tp`.`ENUMNAME` AS `typeNmae`,`tpri`.`ENUMNAME` AS `priName`,`t`.`UPDDATE` AS `UPDDATE` from ((((((`t_testcasebaseinfo` `t` join `t_outlineinfo` `o` on((`o`.`MODULEID` = `t`.`MODULEID`))) join `t_outlineinfo` `o2` on((`o2`.`MODULEID` = `o`.`SUPERMODULEID`))) join `t_user` `u` on((`t`.`CREATERID` = `u`.`ID`))) left join `t_user` `u2` on((`t`.`ADUIT_ID` = `u2`.`ID`))) join `t_typedefine` `tp` on((`tp`.`ENUMID` = `t`.`CASETYPE`))) join `t_typedefine` `tpri` on((`tpri`.`ENUMID` = `t`.`CASE_PRI`))) order by `t`.`UPDDATE` desc; ;
