﻿SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS  `component`;
CREATE TABLE `component` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uploader` int(11) NOT NULL,
  `info` json NOT NULL,
  `words` json NOT NULL,
  `file` blob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

insert into `component`(`id`,`uploader`,`info`,`words`,`file`) values
(1,1,'{"name": "写出汉语"}','["3"]',0x5675655365747570287B0D0A2020202074656D706C6174653A0D0A2020202020202020603C6469763E0D0A2020202020202020E58699E587BAE6B189E8AFAD3C62722F3E0D0A2020202020203C613E7B7B776F72642E656E676C6973687D7D3C2F613E0D0A2020202020203C656C2D696E70757420762D6D6F64656C3D22696E707574223E3C2F656C2D696E7075743E0D0A2020202020203C656C2D627574746F6E2040636C69636B3D22636C69636B223EE7A1AEE5AE9A3C2F656C2D627574746F6E3E0D0A202020203C2F6469763E602C0D0A2020202070726F70733A205B22776F7264225D2C0D0A20202020646174612829207B0D0A202020202020202072657475726E207B0D0A202020202020202020202020696E7075743A20756E646566696E65640D0A20202020202020207D3B0D0A202020207D2C0D0A202020206D6574686F64733A207B0D0A2020202020202020636C69636B2829207B0D0A202020202020202020202020746869732E24656D69742822726573756C74222C20746869732E696E707574203D3D3D20746869732E776F72642E6368696E657365293B0D0A20202020202020207D0D0A202020207D0D0A7D29),
(2,1,'{"name": "写出英文"}','[1, 2]',0x5675655365747570287B0D0A2020202074656D706C6174653A0D0A2020202020202020603C6469763E0D0A2020202020202020E58699E587BAE88BB1E696873C62722F3E0D0A2020202020203C613E7B7B776F72642E6368696E6573657D7D3C2F613E0D0A2020202020203C656C2D696E70757420762D6D6F64656C3D22696E707574223E3C2F656C2D696E7075743E0D0A2020202020203C656C2D627574746F6E2040636C69636B3D22636C69636B223EE7A1AEE5AE9A3C2F656C2D627574746F6E3E0D0A202020203C2F6469763E602C0D0A2020202070726F70733A205B22776F7264225D2C0D0A20202020646174612829207B0D0A202020202020202072657475726E207B0D0A202020202020202020202020696E7075743A20756E646566696E65640D0A20202020202020207D3B0D0A202020207D2C0D0A202020206D6574686F64733A207B0D0A2020202020202020636C69636B2829207B0D0A202020202020202020202020746869732E24656D69742822726573756C74222C20746869732E696E707574203D3D3D20746869732E776F72642E656E676C697368293B0D0A20202020202020207D0D0A202020207D0D0A7D29);
DROP TABLE IF EXISTS  `lesson`;
CREATE TABLE `lesson` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `data` json NOT NULL,
  `info` json NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

insert into `lesson`(`id`,`data`,`info`) values
(1,'[{"wordId": "1", "component": []}, {"wordId": "2", "component": []}, {"wordId": "3", "component": ["1"]}]','{"name": "测试课程1", "image": "https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png", "message": "课程描述"}'),
(2,'[{"wordId": "3", "component": []}]','{"name": "没图课程2"}'),
(3,'[{"wordId": "2", "component": []}]','{"name": "没图课程3"}'),
(4,'[{"wordId": "1", "component": []}]','{"name": "没图课程4"}');
DROP TABLE IF EXISTS  `lesson_subscriber`;
CREATE TABLE `lesson_subscriber` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `userId` int(11) unsigned NOT NULL,
  `lessonId` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

insert into `lesson_subscriber`(`id`,`userId`,`lessonId`) values
(4,1,2),
(7,1,1);
DROP TABLE IF EXISTS  `role`;
CREATE TABLE `role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

insert into `role`(`id`,`name`) values
(1,'测试分组');
DROP TABLE IF EXISTS  `test_table`;
CREATE TABLE `test_table` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `test` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

insert into `test_table`(`id`,`test`) values
(1,'数据2'),
(2,'数据1');
DROP TABLE IF EXISTS  `user`;
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `salt` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`,`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

insert into `user`(`id`,`username`,`password`,`salt`) values
(1,'测试账号','9f735e0df9a1ddc702bf0a1a7b83033f9f7153a00c29de82cedadc9957289b05','test');
DROP TABLE IF EXISTS  `user_role`;
CREATE TABLE `user_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL,
  `role` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

insert into `user_role`(`id`,`user`,`role`) values
(1,1,1);
DROP TABLE IF EXISTS  `word`;
CREATE TABLE `word` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `data` json NOT NULL,
  `describe` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='单词';

insert into `word`(`id`,`data`,`describe`) values
(1,'{"chinese": "一个", "english": "a"}','a'),
(2,'{"chinese": "单词", "english": "word"}','word'),
(3,'{"chinese": "测试", "english": "test"}','test');
SET FOREIGN_KEY_CHECKS = 1;
