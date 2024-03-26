USE ptp_web;

# 2024-3-24  19:17-往asset表中插入模拟数据
INSERT INTO `asset`(`id`, `balance`, `accounts`)
VALUES (1, 1000.00, '7758521,1212121'),
       (2, 2500.00, '7474741,5201314,27252762'),
       (3, 5000.00, '32427422'),
       (4, 100000.00, '88888888'),
       (5, 5632.35, '2135467'),
       (6, 6025.00, '7758521,1212121'),
       (7, 8400.00, '7474741,5201314,27222788'),
       (8, 96000.00, '89252527'),
       (9, 120000.00, '88887777,252542452,278272'),
       (10, 8848.63, '213527525467');


# 2024-3-25  9:18-往role表中插入模拟数据
INSERT INTO `role` (`code`, `name`, `authorities`, `prohibition`)
VALUES (801, 'administrator',
        'content_list,content_add,content_remove,content_update,user_add,user_remove,role_add,role_remove,role_list,role_update',
        ''),
       (802, 'manager', 'content_list,content_add,content_remove,user_add,user_remove',
        'role_add,role_remove,role_list,role_update'),
       (803, 'normal_user', 'content_list,content_add,content_update',
        'user_add,user_remove,role_add,role_remove,role_list,role_update'),
       (804, 'limit_user', 'content_list',
        'content_remove,content_update,user_add,user_remove,role_add,role_remove,role_list,role_update'),
       (805, 'blocked_user', '',
        'content_add,content_remove,content_update,user_add,user_remove,role_add,role_remove,role_list,role_update'),
       (806, 'publisher', 'content_list,content_add',
        'user_add,user_remove,role_add,role_remove,role_list,role_update'),
       (807, 'monitor', 'content_list,content_remove',
        'user_add,user_remove,role_add,role_remove,role_list,role_update'),
       (808, 'developer', 'user_add,user_remove',
        'content_list,content_update,role_add,role_remove,role_list,role_update'),
       (809, 'DevOps', 'role_add,role_remove,role_list,role_update', 'user_add,user_remove,content_add,content_update'),
       (810, 'else', 'content_remove,content_update',
        'user_add,user_remove,role_add,role_remove,role_list,role_update');


# 2024-3-25  10:49-往user表中插入模拟数据
INSERT INTO `user` (`account`, `password`, `phone`, `email`, `nickname`, `realname`, `idiograph`, `avatar`,
                    `background`, `like_num`,
                    `user_rank`,
                    `birth_date`, `credit`, `role_id`, `asset_id`)
VALUES ('3599758685', '22851316', '13537484671',
        'l.shhieoo@gsv.at', 'Lisa Moore', '易霞', '都是风景，幸会', '{
    "type": "url",
    "uri": "https://img.52tu.com/2021/09/01/5ad2100845d130a794d1b4c5d3e0e470ef2f8199.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/03/45/0aQwB9cbps.jpg!/fh/350"
}', '71049', '2', '2020-06-17 22:02:05', '63',
        '5', '7')
     , ('3503619143', '07814566', '13287644133',
        't.idbdscth@lshsiu.cy', 'Linda Thomas',
        '贺秀兰', '没有期待的日子反而顺顺利利', '{
    "type": "url",
    "uri": "https://img.52tu.com/2021/09/01/b2266bc20fb899954fdd83b52a4bc974945b0d8a.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/17/18/v6cXZ0iGp7.jpg!/fh/350"
}', '46508', '0', '2006-02-09 20:15:06',
        '67', '7', '5')
     , ('3516168164', '53874642', '13583563823',
        'y.tulrjlfkk@vicetx.org.cn', 'Ruth Johnson',
        '罗军', '山行野宿，孤身万里', '{
    "type": "url",
    "uri": "https://img.52tu.com/2021/09/01/3bd034c17c4cd37d8227500fe65ba9697c14bb9e.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/03/71/LtH3VlTOqf.jpg!/fh/350"
}', '94245', '4', '2000-03-31 04:16:50',
        '82', '2', '6')
     , ('513246526', '55436628', '18912719149',
        't.bipmfy@gthgyhn.bi', 'Laura Allen', '龚艳',
        '慢慢体会我的极端与浪漫吧', '{
    "type": "url",
    "uri": "https://img.52tu.com/2021/09/01/c09ca8d3d114b49bbea211c3963f31cafd53c44e.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/06/62/qGrtrfyBtQ.jpg!/fh/350"
}', '49784', '1', '1975-08-13 05:32:43', '67',
        '7', '6')
     , ('215696542', '66361897', '18974214504',
        's.gnqqpm@ptbbx.kg', 'Elizabeth Martinez',
        '孟伟', '白天有说有笑，晚上睡个好觉', '{
    "type": "url",
    "uri": "https://img.52tu.com/2021/09/01/6dab3ef8c66c22ba5f42f2ae1c1962746a034e7c.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/03/35/zcLyqiXIpY.jpg!/fh/350"
}', '93138', '0', '2003-12-04 09:43:51', '72', '1', '5')
     , ('8939546763', '93719365', '18992484290',
        'p.ptsb@femfev.sy', 'Amy Williams', '乔洋', '我出售故事，谋杀，艳情 ，小道消息', '{
    "type": "url",
    "uri": "https://img.52tu.com/2021/09/01/dd21fad2a0e3d6fcc408e1259eecbfa62e8150b2.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/04/99/qel1H6beV0.jpg!/fh/350"
}', '40361', '6', '2015-01-29 01:42:38', '86', '10', '6')
     , ('3521156476', '52802884', '18955647333',
        'q.rmjw@agep.sj', 'Patricia Lewis', '吕丽', '把自己流放到世界上的某个角落', '{
    "type": "url",
    "uri": "https://img.52tu.com/2021/09/01/93426bb7f4da9edb3fd21a5a5e4e4b645615a789.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/12/38/nYHRr2MgWc.jpg!/fh/350"
}', '42529', '4', '1973-01-02 08:51:20', '65', '8', '8')
     , ('3528123755', '32553084', '18982578235',
        'z.amkn@hqzoo.al', 'Anthony Miller', '高涛', '放松点，不用和每个人都好，也不用被每个人喜欢', '{
    "type": "url",
    "uri": "https://img.52tu.com/2021/09/01/013350272e6da3ba20f1132c15d469d81c8b5627.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/09/41/qOxDlfbzDo.jpg!/fh/350"
}', '33603', '0', '1994-12-23 08:53:52', '62', '2', '7')
     , ('8933864275', '68433956', '13262114721',
        'v.wvakehp@julprwh.kp', 'Timothy Rodriguez',
        '罗强', '这座城市每个角落，都填满若有所思的生活', '{
    "type": "url",
    "uri": "https://img.52tu.com/2021/09/01/4d8d5a0c509e3a38b3199cf05cc2421c27e17eca.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/07/24/bG7xs5TbEb.jpg!/fh/350"
}', '7477', '3', '2005-01-19 04:49:08', '85', '9', '3')
     , ('3590511491', '01583241', '18987740346', 'j.qkeq@xykngv.cl', 'Kimberly Jones', '方平',
        '一定要周而复始的快快乐乐', '{
    "type": "url",
    "uri": "https://img.52tu.com/2021/09/01/0c2b9beba63bca2bf225a5db7c6824010a0d5ad1.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/05/30/TnG3M9TXyi.jpg!/fh/350"
}', '38165', '1', '1984-03-13 14:12:53', '73', '9', '6');
