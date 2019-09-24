SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS  `component`;
CREATE TABLE `component` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uploader` int(11) NOT NULL,
  `info` json NOT NULL,
  `words` json NOT NULL,
  `url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

insert into `component`(`id`,`uploader`,`info`,`words`,`url`) values
(1,1,'{"name": "写出汉语"}','[1, 2, 3]','/static/components/1.js'),
(2,1,'{"name": "写出英文"}','[1, 2]','/static/components/2.js');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

insert into `lesson_subscriber`(`id`,`userId`,`lessonId`) values
(2,1,2),
(3,1,1);
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='单词';

insert into `word`(`id`,`data`) values
(1,'{"chinese": "一个", "english": "a"}'),
(2,'{"chinese": "单词", "english": "word"}'),
(3,'{"chinese": "测试", "english": "test"}');
SET FOREIGN_KEY_CHECKS = 1;

