USE `ptp_web`;

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
INSERT INTO `role` (`id`, `code`, `name`, `authorities`, `prohibition`)
VALUES (1, 801, 'administrator',
        '["all","content:list","content:add","content:remove","content:update","content:user:add", "content:user:remove", "content:user:list", "content:user:update","role:add","role:remove","role:list","role:update"]',
        '[]'),
       (2, 802, 'manager', '["content:list","content:add","content:remove","content:user:add","content:user:remove"]',
        '["role:add","role:remove","role:list","role:update"]'),
       (3, 803, 'normal:user', '["content:list","content:add","content:update"]',
        '["user:add","user:remove","role:add","role:remove","role:list","role:update"]'),
       (4, 804, 'limit:user', '["content:list"]',
        '["content:remove","content:update","user:add","content:user:remove","role:add","role:remove","role:list","role:update"]'),
       (5, 805, 'blocked:user', '[]',
        '["content:add","content:remove","content:update","content:user:add","content:user:remove","role:add","role:remove","role:list","role:update"]'),
       (6, 806, 'publisher', '["content:list","content:add"]',
        '["user:add","content:user:remove","role:add","role:remove","role:list","role:update"]'),
       (7, 807, 'monitor', '["content:list","content:remove"]',
        '["user:add","content:user:remove","role:add","role:remove","role:list","role:update"]'),
       (8, 808, 'developer', '["content:user:add","content:user:remove"]',
        '["content:list","content:update","role:add","role:remove","role:list","role:update"]'),
       (9, 809, 'DevOps', '["role:add","role:remove","role:list","role:update"]',
        '["user:add","content:user:remove","content:add","content:update"]'),
       (10, 810, 'else', '["content:remove","content:update"]',
        '["content:user:add","content:user:remove","role:add","role:remove","role:list","role:update"]')
on duplicate key update code        = values(code),
                        name        = values(name),
                        authorities = values(authorities),
                        prohibition = values(prohibition);


# 2024-3-25  10:49-往user表中插入模拟数据
INSERT INTO `user` (`account`, `password`, `phone`, `email`, `nickname`, `realname`, `gender`, `idiograph`, `avatar`,
                    `background`, `like_num`, `share_num`,
                    `user_rank`,
                    `birth_date`, `address`, `credit`, `role_id`, `asset_id`)
VALUES ('3599758685', '22851316', '13537484671',
        'l.shhieoo@gsv.at', '春物叙事曲', '易霞', 1, '都是风景，幸会', '{
    "type": "url",
    "uri": "https://ptp-user-1309498949.cos.ap-nanjing.myqcloud.com/user-0816202307233954-avatar.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/03/45/0aQwB9cbps.jpg!/fh/350"
}', '71049', 1515, '2', '2020-06-17 22:02:05', '["山东省","临沂市","蒙阴县"]', '63',
        '5', '7')
     , ('3503619143', '07814566', '13287644133',
        't.idbdscth@lshsiu.cy', '焦糖布丁',
        '贺秀兰', 1, '没有期待的日子反而顺顺利利', '{
    "type": "url",
    "uri": "https://ptp-user-1309498949.cos.ap-nanjing.myqcloud.com/user-0846202307236889-background.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/17/18/v6cXZ0iGp7.jpg!/fh/350"
}', '46508', 154, '0', '2006-02-09 20:15:06', '["北京市","北京市","东城区"]',
        '67', '7', '5')
     , ('3516168164', '53874642', '18315661882',
        '3242742226@qq.com', '爱吃香芋派',
        '罗军', 0, '山行野宿，孤身万里', '{
    "type": "url",
    "uri": "https://img.52tu.com/2021/09/01/3bd034c17c4cd37d8227500fe65ba9697c14bb9e.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/03/71/LtH3VlTOqf.jpg!/fh/350"
}', '94245', 6616, '4', '2000-03-31 04:16:50', '["广东省","广州市","天河区"]',
        '82', '2', '6')
     , ('513246526', '55436628', '18912719149',
        't.bipmfy@gthgyhn.bi', 'Laura Allen', '龚艳', 1,
        '慢慢体会我的极端与浪漫吧', '{
    "type": "url",
    "uri": "https://ptp-user-1309498949.cos.ap-nanjing.myqcloud.com/user-1850202302121496-avatar.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/06/62/qGrtrfyBtQ.jpg!/fh/350"
}', '49784', 451, '1', '1975-08-13 05:32:43', '["江苏省","南京市","鼓楼区"]', '67',
        '7', '6')
     , ('215696542', '66361897', '18689841823',
        '236010069@qq.com', '小鱼偶偶泡',
        '孟伟', 0, '白天有说有笑，晚上睡个好觉', '{
    "type": "url",
    "uri": "https://ptp-user-1309498949.cos.ap-nanjing.myqcloud.com/user-9767202303161187-avatar.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/03/35/zcLyqiXIpY.jpg!/fh/350"
}', '93138', 864, '0', '2003-12-04 09:43:51', '["浙江省","杭州市","西湖区"]', '72', '1', '5')
     , ('8939546763', '93719365', '18992484290',
        'dage3242742226@gmail.com', '树上有只熊', '乔洋', 2, '我出售故事，谋杀，艳情 ，小道消息', '{
    "type": "url",
    "uri": "https://ptp-user-1309498949.cos.ap-nanjing.myqcloud.com/user-1850202302121496-background.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/04/99/qel1H6beV0.jpg!/fh/350"
}', '40361', 231, '6', '2015-01-29 01:42:38', '["山东省","青岛市","市南区"]', '86', '10', '6')
     , ('3521156476', '52802884', '18955647333',
        'q.rmjw@agep.sj', '银河投递员', '吕丽', 1, '把自己流放到世界上的某个角落', '{
    "type": "url",
    "uri": "https://img.52tu.com/2021/09/01/93426bb7f4da9edb3fd21a5a5e4e4b645615a789.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/12/38/nYHRr2MgWc.jpg!/fh/350"
}', '42529', 784, '4', '1973-01-02 08:51:20', '["四川省","成都市","锦江区"]', '65', '8', '8')
     , ('3528123755', '32553084', '18982578235',
        'z.amkn@hqzoo.al', '春天禁止入内', '高涛', 0, '放松点，不用和每个人都好，也不用被每个人喜欢', '{
    "type": "url",
    "uri": "https://ptp-user-1309498949.cos.ap-nanjing.myqcloud.com/user-3125202308061853-avatar.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/09/41/qOxDlfbzDo.jpg!/fh/350"
}', '33603', 186, '0', '1994-12-23 08:53:52', '["陕西省","西安市","雁塔区"]', '62', '2', '7')
     , ('8933864275', '68433956', '13262114721',
        'v.wvakehp@julprwh.kp', '海盐幻想',
        '罗强', 0, '这座城市每个角落，都填满若有所思的生活', '{
    "type": "url",
    "uri": "https://ptp-user-1309498949.cos.ap-nanjing.myqcloud.com/user-3125202308061853-background.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/07/24/bG7xs5TbEb.jpg!/fh/350"
}', '7477', 223, '3', '2005-01-19 04:49:08', '["湖北省","武汉市","洪山区"]', '85', '9', '3')
     , ('3590511491', '01583241', '18987740346', 'j.qkeq@xykngv.cl', '落日飛機', '方平', 0,
        '一定要周而复始的快快乐乐', '{
    "type": "url",
    "uri": "https://ptp-user-1309498949.cos.ap-nanjing.myqcloud.com/user-9240202309053651-avatar.jpg"
}', '{
    "type": "url",
    "uri": "https://img.tukuppt.com/bg_grid/00/05/30/TnG3M9TXyi.jpg!/fh/350"
}', '38165', 135, '1', '1984-03-13 14:12:53', '["福建省","福州市","鼓楼区"]', '73', '9', '6');


# 2024-3-27  20:17-往announcement表中插入数据
INSERT INTO `announcement` (`id`, `uid`, `title`, `content`, `tags`, `category`, `browse_num`, `like_num`, `unlike_num`,
                            `comment_num`, `star_num`, `share_num`, `address_info`)
VALUES (1, 5, '新产品发布', '欢迎体验我们的新产品，希望能够为您带来更好的体验。', '["新产品", "体验"]', '["产品发布"]',
        6238, 5123, 2487, 7836, 9432, 666,
        '{"altitude": 100.25, "longitude": 121.4797, "latitude": 31.2354, "country": "中国", "countryID": "CN", "province": "上海", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "黄河路123号"}'),
       (2, 9, '节日促销', '感恩节即将到来，我们将举办大型促销活动，各种商品一律7折优惠。', '["促销", "节日"]',
        '["促销活动"]', 8327, 4392, 6410, 1895, 5491, 725,
        '{"altitude": 56.75, "longitude": 116.4074, "latitude": 39.9042, "country": "中国", "countryID": "CN", "province": "北京", "provinceID": "110000", "city": "北京市", "cityID": "110100", "county": "东城区", "countyID": "110101", "detailedLocation": "东单大街456号"}'),
       (3, 2, '系统升级通知', '尊敬的用户，为了提供更稳定的服务，我们将于本周末进行系统升级，请留意相关通知。',
        '["系统升级", "通知"]', '["系统维护"]', 4862, 9274, 5713, 3271, 2457, 332,
        '{"altitude": 14.37, "longitude": 113.2806, "latitude": 23.1252, "country": "中国", "countryID": "CN", "province": "广东", "provinceID": "440000", "city": "广州市", "cityID": "440100", "county": "越秀区", "countyID": "440104", "detailedLocation": "中山一路789号"}'),
       (4, 6, '招聘公告', '欢迎优秀人才加入我们，目前有多个岗位空缺，期待您的加入。', '["招聘", "人才"]', '["人才招聘"]',
        7114, 3698, 9256, 5456, 3125, 577,
        '{"altitude": 239.84, "longitude": 114.0661, "latitude": 22.5485, "country": "中国", "countryID": "CN", "province": "广东", "provinceID": "440000", "city": "深圳市", "cityID": "440300", "county": "南山区", "countyID": "440305", "detailedLocation": "科技南路678号"}'),
       (5, 8, '产品升级公告', '我们将于下个月推出产品新版本，届时将为用户带来更多功能和体验优化。',
        '["产品升级", "新版本"]', '["产品更新"]', 5943, 8257, 4389, 6945, 1774, 798,
        '{"altitude": 39.91, "longitude": 113.263, "latitude": 23.1291, "country": "中国", "countryID": "CN", "province": "广东", "provinceID": "440000", "city": "广州市", "cityID": "440100", "county": "天河区", "countyID": "440106", "detailedLocation": "天河北路6789号"}'),
       (6, 1, '年终盛典预告', '本年度年终盛典即将到来，敬请期待精彩节目和丰厚奖品。', '["年终盛典", "预告"]',
        '["活动通知"]', 8961, 7512, 2623, 9182, 4096, 255,
        '{"altitude": 204.67, "longitude": 116.4074, "latitude": 39.9042, "country": "中国", "countryID": "CN", "province": "北京", "provinceID": "110000", "city": "北京市", "cityID": "110100", "county": "西城区", "countyID": "110102", "detailedLocation": "西直门大街123号"}'),
       (7, 7, '重要通知', '尊敬的用户，我们将于明天进行服务器维护，届时可能会影响部分服务，请谅解。',
        '["重要通知", "服务器维护"]', '["系统维护"]', 4231, 9674, 5392, 2615, 7489, 638,
        '{"altitude": 62.5, "longitude": 121.4737, "latitude": 31.2304, "country": "中国", "countryID": "CN", "province": "上海", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "徐汇区", "countyID": "310104", "detailedLocation": "肇嘉浜路1234号"}'),
       (9, 3, '产品推荐', '推荐我们的最新产品，全新上市，品质保证，赶快抢购吧！', '["产品推荐", "新品"]', '["产品推广"]',
        6925, 1498, 8234, 5871, 3869, 521,
        '{"altitude": 305.5, "longitude": 113.2806, "latitude": 23.1252, "country": "中国", "countryID": "CN", "province": "广东", "provinceID": "440000", "city": "广州市", "cityID": "440100", "county": "越秀区", "countyID": "440104", "detailedLocation": "广州大道123号"}'),
       (10, 7, '特价优惠通知', '特价商品抢购通知，各类商品低价促销中，赶快选购您需要的商品吧！', '["特价", "优惠"]',
        '["促销活动"]', 4265, 9243, 1378, 7524, 9852, 702,
        '{"altitude": 97.2, "longitude": 121.4737, "latitude": 31.2304, "country": "中国", "countryID": "CN", "province": "上海", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "徐汇区", "countyID": "310104", "detailedLocation": "龙华西路789号"}'),
       (11, 6, '会员福利公告', '针对会员用户推出多项福利，包括积分兑换、生日礼品等，详情请查看会员中心。',
        '["会员福利", "积分兑换"]', '["会员服务"]', 5789, 6423, 3859, 2198, 9546, 174,
        '{"altitude": 124.7, "longitude": 116.4074, "latitude": 39.9042, "country": "中国", "countryID": "CN", "province": "北京", "provinceID": "110000", "city": "北京市", "cityID": "110100", "county": "朝阳区", "countyID": "110105", "detailedLocation": "朝阳路456号"}'),
       (12, 9, '线上讲座预告', '本周六将举办线上讲座，主题为“创业经验分享”，欢迎大家踊跃参与。',
        '["线上讲座", "创业经验"]', '["活动预告"]', 7582, 3691, 5982, 7461, 8253, 465,
        '{"altitude": 56.1, "longitude": 116.4074, "latitude": 39.9042, "country": "中国", "countryID": "CN", "province": "北京", "provinceID": "110000", "city": "北京市", "cityID": "110100", "county": "海淀区", "countyID": "110108", "detailedLocation": "中关村大街789号"}'),
       (13, 2, '新品发布会', '精品新品即将发布，敬请期待新品发布会，更多惊喜等您发现。', '["新品发布", "惊喜"]',
        '["产品发布"]', 6742, 9214, 3578, 6281, 1943, 161,
        '{"altitude": 92.5, "longitude": 113.263, "latitude": 23.1291, "country": "中国", "countryID": "CN", "province": "广东", "provinceID": "440000", "city": "广州市", "cityID": "440100", "county": "越秀区", "countyID": "440104", "detailedLocation": "中山大道6789号"}'),
       (14, 8, '员工活动通知', '公司将组织员工团建活动，欢迎员工踊跃参加，增进团队凝聚力。', '["员工活动", "团建"]',
        '["团队活动"]', 3598, 6745, 9352, 5183, 2964, 562,
        '{"altitude": 80.3, "longitude": 113.2806, "latitude": 23.1252, "country": "中国", "countryID": "CN", "province": "广东", "provinceID": "440000", "city": "广州市", "cityID": "440100", "county": "天河区", "countyID": "440106", "detailedLocation": "天河北路1234号"}'),
       (15, 4, '节日放假通知', '公司放假通知，本周五开始放假，假期愉快！', '["放假通知", "节假日"]', '["公司通知"]', 2478,
        3695, 7589, 9362, 4157, 50,
        '{"altitude": 32.4, "longitude": 114.0661, "latitude": 22.5485, "country": "中国", "countryID": "CN", "province": "广东", "provinceID": "440000", "city": "深圳市", "cityID": "440300", "county": "南山区", "countyID": "440305", "detailedLocation": "科技南路123号"}'),
       (17, 5, '客户满意度调查', '我们重视您的意见，诚邀参与客户满意度调查，共同改善服务质量。',
        '["满意度调查", "客户反馈"]', '["服务调查"]', 7849, 2156, 8324, 6947, 9512, 23,
        '{"altitude": 56.7, "longitude": 121.4797, "latitude": 31.2354, "country": "中国", "countryID": "CN", "province": "上海", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "中山东一路123号"}'),
       (18, 10, '公司年会通知', '公司年会将于本月底举行，预祝年会圆满成功，欢迎大家踊跃参加。', '["年会通知", "圆满成功"]',
        '["公司活动"]', 3926, 9251, 3784, 8541, 1397, 221,
        '{"altitude": 91.2, "longitude": 121.4737, "latitude": 31.2304, "country": "中国", "countryID": "CN", "province": "上海", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "徐汇区", "countyID": "310104", "detailedLocation": "南丹路456号"}'),
       (19, 1, '特价清仓优惠', '特价清仓优惠，底价处理，数量有限，先到先得，赶快选购吧！', '["特价清仓", "底价处理"]',
        '["促销活动"]', 6384, 9473, 5217, 3659, 2486, 250,
        '{"altitude": 307.3, "longitude": 121.4797, "latitude": 31.2354, "country": "中国", "countryID": "CN", "province": "上海", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "南京东路789号"}'),
       (20, 2, '品牌推广活动', '我们的品牌推广活动即将启动，全场商品低至5折，敬请关注。', '["品牌推广", "5折优惠"]',
        '["品牌活动"]', 4792, 6258, 4938, 8613, 2794, 173,
        '{"altitude": 103.5, "longitude": 113.2806, "latitude": 23.1252, "country": "中国", "countryID": "CN", "province": "广东", "provinceID": "440000", "city": "广州市", "cityID": "440100", "county": "天河区", "countyID": "440106", "detailedLocation": "广州大道123号"}'),
       (21, 9, '新产品发布', '欢迎体验我们的新产品，希望能够为您带来更好的体验。', '["新产品", "体验"]', '["产品发布"]',
        6238, 5123, 2487, 7836, 9432, 361,
        '{"altitude": 100.25, "longitude": 121.4797, "latitude": 31.2354, "country": "中国", "countryID": "CN", "province": "上海", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "黄河路123号"}'),
       (22, 5, '节日促销', '感恩节即将到来，我们将举办大型促销活动，各种商品一律7折优惠。', '["促销", "节日"]',
        '["促销活动"]', 8327, 4392, 6410, 1895, 5491, 25,
        '{"altitude": 56.75, "longitude": 116.4074, "latitude": 39.9042, "country": "中国", "countryID": "CN", "province": "北京", "provinceID": "110000", "city": "北京市", "cityID": "110100", "county": "东城区", "countyID": "110101", "detailedLocation": "东单大街456号"}'),
       (23, 10, '系统升级通知', '尊敬的用户，为了提供更稳定的服务，我们将于本周末进行系统升级，请留意相关通知。',
        '["系统升级", "通知"]', '["系统维护"]', 4862, 9274, 5713, 3271, 2457, 7,
        '{"altitude": 14.37, "longitude": 113.2806, "latitude": 23.1252, "country": "中国", "countryID": "CN", "province": "广东", "provinceID": "440000", "city": "广州市", "cityID": "440100", "county": "越秀区", "countyID": "440104", "detailedLocation": "中山一路789号"}'),
       (25, 8, '产品升级公告', '我们将于下个月推出产品新版本，届时将为用户带来更多功能和体验优化。',
        '["产品升级", "新版本"]', '["产品更新"]', 5943, 8257, 4389, 6945, 1774, 20,
        '{"altitude": 39.91, "longitude": 113.263, "latitude": 23.1291, "country": "中国", "countryID": "CN", "province": "广东", "provinceID": "440000", "city": "广州市", "cityID": "440100", "county": "天河区", "countyID": "440106", "detailedLocation": "天河北路6789号"}'),
       (26, 1, '年终盛典预告', '本年度年终盛典即将到来，敬请期待精彩节目和丰厚奖品。', '["年终盛典", "预告"]',
        '["活动通知"]', 8961, 7512, 2623, 9182, 4096, 91,
        '{"altitude": 204.67, "longitude": 116.4074, "latitude": 39.9042, "country": "中国", "countryID": "CN", "province": "北京", "provinceID": "110000", "city": "北京市", "cityID": "110100", "county": "西城区", "countyID": "110102", "detailedLocation": "西直门大街123号"}'),
       (27, 4, '重要通知', '尊敬的用户，我们将于明天进行服务器维护，届时可能会影响部分服务，请谅解。',
        '["重要通知", "服务器维护"]', '["系统维护"]', 4231, 9674, 5392, 2615, 7489, 1270,
        '{"altitude": 62.5, "longitude": 121.4737, "latitude": 31.2304, "country": "中国", "countryID": "CN", "province": "上海", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "徐汇区", "countyID": "310104", "detailedLocation": "肇嘉浜路1234号"}'),
       (28, 7, '新店开业通知', '我们的新店即将开业，欢迎大家前来参观品尝，有', '["新店开业", "参观品尝"]', '["开业通知"]',
        7821, 2543, 6432, 8941, 3285, 431,
        '{"altitude": 239.84, "longitude": 114.0661, "latitude": 22.5485, "country": "中国", "countryID": "CN", "province": "广东", "provinceID": "440000", "city": "深圳市", "cityID": "440300", "county": "南山区", "countyID": "440305", "detailedLocation": "科技南路678号"}'),
       (29, 10, '节日活动预告', '元旦即将到来，公司将组织丰富多彩的节日活动，敬请期待。', '["节日活动", "元旦"]',
        '["节日庆祝"]', 7159, 9632, 4281, 5763, 3198, 271,
        '{"altitude": 91.2, "longitude": 121.4737, "latitude": 31.2304, "country": "中国", "countryID": "CN", "province": "上海", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "徐汇区", "countyID": "310104", "detailedLocation": "南丹路456号"}'),
       (30, 8, '产品推荐', '欢迎体验我们的优质产品，全场特价，数量有限，赶紧抢购吧！', '["产品推荐", "特价"]',
        '["产品推广"]', 6348, 8426, 1759, 4672, 8952, 174,
        '{"altitude": 103.5, "longitude": 113.2806, "latitude": 23.1252, "country": "中国", "countryID": "CN", "province": "广东", "provinceID": "440000", "city": "广州市", "cityID": "440100", "county": "天河区", "countyID": "440106", "detailedLocation": "广州大道123号"}'),
       (31, 6, '会员优惠活动', '会员专享优惠活动，各类商品低至6折，快来参加吧！', '["会员优惠", "专享活动"]',
        '["会员福利"]', 9754, 3269, 8471, 5421, 3692, 13,
        '{"altitude": 56.7, "longitude": 121.4797, "latitude": 31.2354, "country": "中国", "countryID": "CN", "province": "上海", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "中山东一路123号"}'),
       (33, 9, '教育讲座通知', '本周六将有教育专家举办公益讲座，主题为“家庭教育心得”，欢迎踊跃参加。',
        '["教育讲座", "公益活动"]', '["公益活动"]', 5921, 7243, 3928, 8175, 6254, 975,
        '{"altitude": 92.5, "longitude": 113.2806, "latitude": 23.1252, "country": "中国", "countryID": "CN", "province": "广东", "provinceID": "440000", "city": "广州市", "cityID": "440100", "county": "天河区", "countyID": "440106", "detailedLocation": "广州大道123号"}'),
       (34, 5, '健康生活分享', '健康生活分享会，将邀请营养专家为大家分享健康饮食秘诀，敬请期待。',
        '["健康生活", "饮食秘诀"]', '["健康讲座"]', 4897, 8362, 1675, 5732, 4126, 263,
        '{"altitude": 56.75, "longitude": 116.4074, "latitude": 39.9042, "country": "中国", "countryID": "CN", "province": "北京", "provinceID": "110000", "city": "北京市", "cityID": "110100", "county": "东城区", "countyID": "110101", "detailedLocation": "东单大街456号"}'),
       (35, 1, '节假日放假通知', '节假日放假通知，公司放假安排如下：1月1日至1月3日放假，祝您新年快乐！',
        '["放假通知", "新年快乐"]', '["公司通知"]', 7325, 9652, 3289, 7425, 6189, 479,
        '{"altitude": 32.4, "longitude": 114.0661, "latitude": 22.5485, "country": "中国", "countryID": "CN", "province": "广东", "provinceID": "440000", "city": "深圳市", "cityID": "440300", "county": "南山区", "countyID": "440305", "detailedLocation": "科技南路123号"}'),
       (36, 10, '品牌日活动', '本月品牌日活动，购物满额送好礼，详情请关注官方公众号。', '["品牌日", "好礼"]',
        '["品牌活动"]', 8254, 4792, 6531, 1843, 2976, 526,
        '{"altitude": 91.2, "longitude": 121.4737, "latitude": 31.2304, "country": "中国", "countryID": "CN", "province": "上海", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "徐汇区", "countyID": "310104", "detailedLocation": "南丹路456号"}'),
       (37, 2, '习近平在金砖国家领导人第十六次会晤上的讲话（全文）', '尊敬的普京总统，
各位同事：

祝贺峰会成功召开，感谢普京总统及东道主俄罗斯的周到安排和热情接待。

我想借此机会再次欢迎新成员加入金砖大家庭。扩员是金砖发展史上的重要里程碑，也是国际格局演变的标志性事件。这次峰会我们又决定邀请多个国家成为金砖伙伴国。这是金砖发展过程中的又一个重要进展。中国人常讲：“君子处事，于义合者为利。”金砖国家走到一起，是基于共同追求，顺应世界和平和发展大势。我们要利用好这次峰会，保持好金砖发展势头，谋划好全局性、方向性、战略性问题，同心同德，勇毅前行，推动金砖国家集体再出发。

当前，世界进入新的动荡变革期，面临关键抉择。是任由世界动荡不安，还是推动其重回和平发展的正道？我想到俄罗斯作家车尔尼雪夫斯基的著作《怎么办？》，书中主人公的坚强意志和奋斗激情，正是当前我们所需要的精神力量。时代的风浪越大，我们越要勇立潮头，以坚韧不拔之志、敢为人先之勇、识变应变之谋，把金砖打造成促进“全球南方”团结合作的主要渠道、推动全球治理变革的先锋力量。

——我们要建设“和平金砖”，做共同安全的维护者。人类是不可分割的安全共同体。只有践行共同、综合、合作、可持续的安全观，才能走出一条普遍安全之路。乌克兰危机还在延宕。中国和巴西会同有关“全球南方”国家发起了乌克兰危机“和平之友”小组，旨在汇集更多致力于和平的声音。我们要坚持“战场不外溢、战事不升级、各方不拱火”三原则，推动局势尽快缓和。加沙地区的人道主义形势持续恶化，黎巴嫩战火又起，相关各方间的冲突还在进一步升级。我们要推动尽快停火、停止杀戮，为全面、公正、持久解决巴勒斯坦问题不懈努力。

——我们要建设“创新金砖”，做高质量发展的先行者。新一轮科技革命和产业变革迅猛发展。我们要紧跟时代步伐，培育新质生产力。中方新近成立中国－金砖国家人工智能发展与合作中心，愿同各方深化创新合作，释放人工智能能量。中方将建立金砖国家深海资源国际研究中心、金砖国家特殊经济区中国合作中心、金砖国家工业能力中国中心、金砖国家数字产业生态合作网络。欢迎各方积极参与，推动金砖合作提质升级。

——我们要建设“绿色金砖”，做可持续发展的践行者。绿色是这个时代的底色，金砖国家要主动融入全球绿色低碳转型洪流。中国电动汽车、锂电池、光伏产品等优质产能，为世界绿色发展提供了重要助力。中方愿发挥自身优势，同金砖国家拓展绿色产业、清洁能源以及绿色矿产合作，推动全产业链“绿色化”发展，充实合作“含绿量”，提升发展“含金量”。

——我们要建设“公正金砖”，做全球治理体系改革的引领者。国际力量对比正在深刻演变，但全球治理体系改革长期滞后。我们要践行真正的多边主义，坚持共商共建共享的全球治理观，以公平正义、开放包容为理念引领全球治理改革。我们要顺应“全球南方”崛起大势，积极回应各国加入金砖合作机制的呼声，推进扩员和设置伙伴国进程，提升发展中国家在全球治理中的代表性和发言权。

当前形势下，国际金融架构改革紧迫性突出。金砖国家要发挥引领作用，深化财金合作，促进金融基础设施互联互通，维护高水平金融安全，做大做强新开发银行，推动国际金融体系更好反映世界经济格局变化。

——我们要建设“人文金砖”，做文明和合共生的倡导者。金砖国家汇聚了深厚的历史和璀璨的文化。我们要积极倡导不同文明包容共存，加强治国理政经验交流，挖掘教育、体育、艺术等领域合作潜力，让不同文明交相辉映，照亮金砖前行之路。去年，我提出金砖数字教育合作倡议，很高兴看到这一机制已经落地。中方将实施金砖数字教育能力建设计划，未来5年在金砖国家设立10个海外学习中心，为1000名教育管理人员和师生提供培训机会，助力金砖人文交流走深走实。

各位同事！

中方愿同金砖各国一道，开创“大金砖合作”高质量发展新局面，携手更多“全球南方”国家共同推动构建人类命运共同体！

谢谢大家！', '["外交","俄罗斯","金砖国家"]', '["中国","和平"]', 8615, 265, 0, 5955, 56153, 1555, '{"altitude": 345.67, "longitude": 113.8765, "latitude": 34.5678, "country": "中国", "countryID": "CN", "province": "俄罗斯", "provinceID": "41", "city": "喀山", "cityID": "4101", "county": "金水区", "countyID": "410105", "detailedLocation": "经三路123号"}');


# 2024-3-27  20:22-往passage表中插入数据
INSERT
INTO `passage` (`id`, `uid`, `publisher_nickname`, `publisher_avatar_url`, `title`, `introduction`, `cover_img_url`, `content`, `tags`, `category`, `browse_num`, `like_num`,
                `unlike_num`, `comment_num`, `star_num`, `share_num`, `address_info`)
VALUES (1, 3, '瑾凉', 'https://p4.itc.cn/q_70/images03/20201001/2ae0e7e1c63f4332b875de25d6f7375e.jpeg', '科技创新与发展', '本文探讨科技创新对社会发展的重要性，以及未来科技的发展方向。', 'https://fc.3dmgame.com/uploads/39/img/1.jpg',
        '随着科技的不断进步，我们迎来了一个全新的时代。', '["科技", "创新"]', '["科技发展"]', 2336, 8592, 132, 4967, 7183, 165,
        '{"altitude": 123.45, "longitude": 120.1234, "latitude": 30.5678, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "33", "city": "杭州市", "cityID": "3301", "county": "西湖区", "countyID": "330106", "detailedLocation": "西湖大道123号"}'),
       (2, 9, '捞月亮的人', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSlcAi2yjDGgCvLi1HUroaWRtz_AU5arUTC4A&s', '古代文明探秘', '通过对古代文明的考古发掘和研究，揭示了古代人类的生活和思想。', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS24ZxXkO1YZl86Y9FU_YZVhjpFOt3KLKZPSA&s',
        '古代文明是人类历史的宝贵遗产，我们应该珍惜和传承。', '["古代文明", "考古"]', '["文化历史"]', 5271, 3245, 75,
        1892, 4236, 516,
        '{"altitude": 456.78, "longitude": 110.9876, "latitude": 25.4321, "country": "中国", "countryID": "CN", "province": "陕西省", "provinceID": "61", "city": "西安市", "cityID": "6101", "county": "雁塔区", "countyID": "610113", "detailedLocation": "大雁塔南路456号"}'),
       (3, 8, '蔚洁玉', 'https://lh4.googleusercontent.com/proxy/tt0WK9Cxkk15ZWmEm1uvgOZKXHvt7b_D-uHaODeqMI4RLNiEfS2OM1u6dJ2vlLb2rf8QJAejNz0moEsQkygOYFc5IcznC5CfoefTysBIH_NFALaspzY', '音乐作品解读', '本文分析了一部经典音乐作品的内涵和意义，探讨了音乐对人们情感的影响。', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRV-p5UcRG7gYwRsDDbMXVFpyiiDktVvFf_Lw&s',
        '音乐是人类灵魂的表达，每一首作品都蕴含着深刻的情感。', '["音乐作品", "解读"]', '["音乐赏析"]', 7824, 6823, 249,
        3921, 8547, 16,
        '{"altitude": 789.12, "longitude": 115.6789, "latitude": 35.8765, "country": "中国", "countryID": "CN", "province": "山东省", "provinceID": "37", "city": "青岛市", "cityID": "3702", "county": "市北区", "countyID": "370203", "detailedLocation": "香港中路789号"}'),
       (4, 1, '叶蔷薇', 'https://img.5km.net/touxiang/2024/2024012619226092.jpg', '娱乐圈八卦揭秘', '揭秘娱乐圈内的八卦新闻和热点话题，了解明星生活的另一面。', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSBYJ8XwEItXgIeZC1rh6AqiY3eOqpgUXYiDA&s',
        '娱乐圈是充满故事和传奇的地方，背后隐藏着许多不为人知的秘密。', '["娱乐圈", "八卦"]', '["娱乐新闻"]', 3985, 1569,
        65, 2789, 6324, 489,
        '{"altitude": 456.78, "longitude": 116.5432, "latitude": 39.1234, "country": "中国", "countryID": "CN", "province": "北京市", "provinceID": "11", "city": "北京市", "cityID": "1101", "county": "东城区", "countyID": "110101", "detailedLocation": "王府井大街456号"}'),
       (5, 7, '慕雪桑', 'https://a.520gexing.com/uploads/allimg/2020031310/r5uyhqmo5ln.jpeg', '智能科技应用', '探讨智能科技在生活中的应用，以及未来智能科技的发展趋势。', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRacXF56MkFQpPCIMuX4uKZ34CndoCU2OsAfw&s',
        '智能科技改变了我们的生活方式，让我们的生活更加便捷和高效。', '["智能科技", "应用"]', '["科技发展"]', 6542, 9842,
        349, 5213, 9325, 166,
        '{"altitude": 234.56, "longitude": 118.7654, "latitude": 32.1098, "country": "中国", "countryID": "CN", "province": "江苏省", "provinceID": "32", "city": "南京市", "cityID": "3201", "county": "玄武区", "countyID": "320102", "detailedLocation": "中山路789号"}'),
       (6, 4, '女汉子也会卖萌', 'https://p7.itc.cn/q_70/images03/20201014/c816871b1c1f441a80267e542ad2720b.jpeg', '历史名人故事', '讲述历史上著名人物的故事和成就，探索历史文化的魅力。', 'https://gd-hbimg.huaban.com/9a1ff0d2d24bd6f37eda556d457f411b6f19f3351d0aea-aqNEeD_fw658',
        '历史名人是人类文明的见证者，他们的故事激励着后人不断前行。', '["历史名人", "故事"]', '["文化历史"]', 3124, 7854,
        189, 4216, 5491, 4974,
        '{"altitude": 345.67, "longitude": 113.8765, "latitude": 34.5678, "country": "中国", "countryID": "CN", "province": "河南省", "provinceID": "41", "city": "郑州市", "cityID": "4101", "county": "金水区", "countyID": "410105", "detailedLocation": "经三路123号"}'),
       (8, 7, '曾经再美也敌不过时间', 'https://lh4.googleusercontent.com/proxy/aQN31oeDvSbNopaX2iLF8cvXe6XOCBFAShQE8NOSRFwzYW5bWmRdzAvxBN83P7Spk7Rz7UIcl-WF_iB-yI3Wd8BsNY1yoLlYRx8UCHASEykby-t2f4fV', '音乐剧欣赏', '分享经典音乐剧的观后感和欣赏点，探讨音乐剧对艺术的贡献和影响。', 'https://s.panlai.com/upload/2023/11/bizhihui_com_20231112165804169977948428828.png-arthumbs',
        '音乐剧是音乐和戏剧的完美结合，让观众沉浸在音乐的魅力中。', '["音乐剧", "欣赏"]', '["音乐赏析"]', 4567, 2456, 98,
        3567, 7894, 135,
        '{"altitude": 567.89, "longitude": 114.5432, "latitude": 30.9876, "country": "中国", "countryID": "CN", "province": "湖北省", "provinceID": "42", "city": "武汉市", "cityID": "4201", "county": "江岸区", "countyID": "420102", "detailedLocation": "解放大道123号"}'),
       (9, 2, '等风来', 'https://img.taotu.cn/ssd/ssd3/1/2023-05-19/1_a48dcc9551bd11ffb2f1f1756bec7f12.jpg', '科技创新与未来', '探讨科技创新对未来社会的影响和发展趋势，展望未来科技带来的可能性。', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR9qqlbFep_i1Qc6tsq5J6dm9jkVJBc34xXhQ&s',
        '未来科技将引领人类走向更加美好的未来，开创新的生活方式。', '["科技创新", "未来"]', '["科技发展"]', 9874, 6321,
        124, 7854, 6543, 158,
        '{"altitude": 345.67, "longitude": 121.2345, "latitude": 31.5432, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "31", "city": "上海市", "cityID": "3101", "county": "黄浦区", "countyID": "310101", "detailedLocation": "南京东路456号"}'),
       (10, 5, '你眼中太温柔', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQtcU39WtnO5NJBO8n-o7g0tMY2FOlP-jydPQ&s', '古代文明遗址', '介绍古代文明遗址的发现和保护工作，探讨古代文明对现代社会的启示。', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqqvX9lo9zjphMWH8g6yKw6mw-JcIHwpjzxg&s',
        '古代文明遗址是历史的见证，也是我们对过去文明的探寻。', '["古代文明", "遗址"]', '["文化历史"]', 7854, 2365, 45,
        4589, 3654, 533,
        '{"altitude": 567.89, "longitude": 119.8765, "latitude": 32.9876, "country": "中国", "countryID": "CN", "province": "江苏省", "provinceID": "32", "city": "南京市", "cityID": "3201", "county": "秦淮区", "countyID": "320104", "detailedLocation": "夫子庙大街123号"}'),
       (11, 6, '兔牙小甜心', 'https://p9.itc.cn/q_70/images03/20200609/68c9c2f9dc15467a9a36cccde1832029.jpeg', '经典音乐推荐', '推荐经典音乐作品，分享音乐对情感的表达和启发。', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSTMWRnvCZ_Re_vbXSLRRtRGktTDPH128-9gA&s',
        '音乐是心灵的语言，每首经典作品都有着独特的魅力和内涵。', '["经典音乐", "推荐"]', '["音乐赏析"]', 6321, 9854, 231,
        5748, 7489, 861,
        '{"altitude": 456.78, "longitude": 113.5432, "latitude": 23.9876, "country": "中国", "countryID": "CN", "province": "广东省", "provinceID": "44", "city": "广州市", "cityID": "4401", "county": "越秀区", "countyID": "440104", "detailedLocation": "广州大道123号"}'),
       (12, 3, '心碎葬海', 'https://pic3.zhimg.com/v2-ca368c220fe34918fe0a22516c549bc2_b.jpg', '历史人物传记', '介绍历史上重要人物的传记和成就，探讨其影响和价值。', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTshEiwkpEIkXEM8xiXHr9QB48M1APoVO_7aw&s',
        '历史人物是时代的精英，他们的成就和思想影响着后世。', '["历史人物", "传记"]', '["文化历史"]', 2548, 3658, 98,
        4578, 6985, 512,
        '{"altitude": 123.45, "longitude": 120.9876, "latitude": 30.5432, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "33", "city": "宁波市", "cityID": "3302", "county": "江东区", "countyID": "330206", "detailedLocation": "海曙路789号"}'),
       (13, 9, '朕要去幼儿园深造了', 'https://gd-hbimg.huaban.com/47593527598317b52ef01190728196c631b81565f38f-66Sh0y_fw658', '科技与环境', '探讨科技对环境的影响和可持续发展的重要性，倡导绿色科技。', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSVSl_Bl3gb7z_7Hvyd3aa4PVevF77s4oPVNQ&s',
        '科技与环境是密不可分的，我们需要用科技保护好我们的地球家园。', '["科技", "环境"]', '["科技发展"]', 7854, 6321,
        124, 7854, 6543, 496,
        '{"altitude": 234.56, "longitude": 116.9876, "latitude": 40.5432, "country": "中国", "countryID": "CN", "province": "北京市", "provinceID": "11", "city": "北京市", "cityID": "1101", "county": "海淀区", "countyID": "110108", "detailedLocation": "西四环北路123号"}'),
       (15, 8, '席城', 'https://lh3.googleusercontent.com/proxy/41RBeDbSn7RLenNASYBXiMnNtnMFGBefElyWAIEa72CGcPUwdswuxYWbVkxL_TJQmGuxTaKgPw', '世界文学名著解读', '解读世界文学名著中的经典之作，分析其文学价值和影响。', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRcmDMgV0snEhdiJYb6z-osgH4PtfqMoeEyLA&s',
        '世界文学名著是人类智慧的结晶，每一部作品都有着深刻的内涵。', '["世界文学", "名著"]', '["文学赏析"]', 3658, 9874,
        258, 6325, 7458, 161,
        '{"altitude": 123.45, "longitude": 121.3456, "latitude": 31.7890, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "31", "city": "上海市", "cityID": "3101", "county": "徐汇区", "countyID": "310104", "detailedLocation": "漕溪路123号"}'),
       (16, 4, '可以哭但决不认输', 'https://www.maomeitu.com/cjpic/frombd/0/253/3927684668/789999124.jpg', '古代建筑艺术', '探讨古代建筑的艺术特点和文化价值，了解古代人类的智慧。', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT52J1dStrAqWk7k0a-16H4Hor48qSfx-evaA&s',
        '古代建筑是文化的载体，承载着历史和记忆。', '["古代建筑", "艺术"]', '["文化历史"]', 4785, 3256, 87, 2156, 6948, 861,
        '{"altitude": 345.67, "longitude": 113.8765, "latitude": 34.5678, "country": "中国", "countryID": "CN", "province": "河南省", "provinceID": "41", "city": "郑州市", "cityID": "4101", "county": "金水区", "countyID": "410105", "detailedLocation": "经三路456号"}'),
       (17, 1, '时间划痕', 'https://a.520gexing.com/uploads/allimg/2021030508/ua44ans5s3f.jpg', '流行音乐赏析', '赏析流行音乐的魅力和特点，探讨音乐对年轻人的影响和价值。', 'https://img3.huamaocdn.com/upload/bizhi/images/1000w680h/2023/10/202310261147394928.jpg',
        '流行音乐是青年人的主流文化，代表着时代的潮流和情感。', '["流行音乐", "赏析"]', '["音乐赏析"]', 3652, 8952, 324,
        3654, 7895, 489,
        '{"altitude": 567.89, "longitude": 114.5432, "latitude": 30.9876, "country": "中国", "countryID": "CN", "province": "湖北省", "provinceID": "42", "city": "武汉市", "cityID": "4201", "county": "江岸区", "countyID": "420102", "detailedLocation": "解放大道456号"}'),
       (18, 10, '白天嘻嘻哈哈晚上崩溃想哭', 'https://img.youjidi.net/uploadimg/image/20200107/20200107175307_87926.jpg', '现代科技应用', '探讨现代科技在各个领域的应用和发展趋势，展望科技带来的未来。', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqqvX9lo9zjphMWH8g6yKw6mw-JcIHwpjzxg&s',
        '现代科技是社会发展的推动力，引领着我们走向智能化时代。', '["现代科技", "应用"]', '["科技发展"]', 7854, 6321, 124,
        7854, 6543, 205,
        '{"altitude": 234.56, "longitude": 116.9876, "latitude": 40.5432, "country": "中国", "countryID": "CN", "province": "北京市", "provinceID": "11", "city": "北京市", "cityID": "1101", "county": "海淀区", "countyID": "110108", "detailedLocation": "西四环南路123号"}'),
       (19, 2, '花开不见花', 'https://img.52tiemo.com/uploads/allimg/2020052714/f541cjonfgs.jpeg', '古代传奇故事', '讲述古代传奇故事中的英雄和传奇经历，探讨传奇对人们的启示。', 'https://img.tukuppt.com/bg_grid/02/73/48/d7pTXRFrvu.jpg!/fh/350',
        '传奇故事是人类想象的产物，也是对历史的一种诠释和传承。', '["古代传奇", "故事"]', '["文化历史"]', 6325, 9652, 145,
        7852, 6542, 48,
        '{"altitude": 456.78, "longitude": 113.5432, "latitude": 23.9876, "country": "中国", "countryID": "CN", "province": "广东省", "provinceID": "44", "city": "广州市", "cityID": "4401", "county": "越秀区", "countyID": "440104", "detailedLocation": "广州大道456号"}'),
       (20, 5, '半成烟沙忆空城', 'https://www.ishuoshuo.com/uploads/allimg/230819/1-230Q911343Q61.jpg', '摄影艺术分享', '分享摄影艺术的魅力和技巧，探讨摄影对生活的记录和表达。', 'https://file.moyublog.com/free_wallpapers_files/b3bpwurp223.jpg',
        '摄影是艺术的一种表现形式，通过镜头展示生活的美好和瞬间。', '["摄影艺术", "分享"]', '["艺术分享"]', 8521, 3654,
        96, 7854, 6325, 4865,
        '{"altitude": 345.67, "longitude": 120.5432, "latitude": 31.9876, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "33", "city": "杭州市", "cityID": "3301", "county": "西湖区", "countyID": "330106", "detailedLocation": "西湖大道456号"}'),
       (22, 6, '失我者永失', 'https://i.juweishe.com/20240127/b087034908cb1c3c9c5b89fa29ba3899.jpg', '古代战争纪实', '记录古代战争的历史过程和战略战术，探讨战争对社会的影响。', 'https://file.moyublog.com/d/file/2021-08-20/kh5ub5wuoga.jpg',
        '战争是人类历史中的重要篇章，也是文明与野蛮的交织之地。', '["古代战争", "纪实"]', '["历史战争"]', 3652, 8952, 324,
        3654, 7895, 208,
        '{"altitude": 567.89, "longitude": 114.5432, "latitude": 30.9876, "country": "中国", "countryID": "CN", "province": "湖北省", "provinceID": "42", "city": "武汉市", "cityID": "4201", "county": "江岸区", "countyID": "420102", "detailedLocation": "解放大道456号"}'),
       (23, 1, '墨晔', 'https://img.keaitupian.cn/newupload/02/1644388362764627.jpg', '当代艺术作品', '探讨当代艺术作品的风格和意义，分析艺术对当代社会的影响。', 'https://lh5.googleusercontent.com/proxy/WjJX04CNNHe4OdujXI9ahF-zxp24wWC-Fqx5skvDTTwHuvvPMALvYPfg57zXzn49jUA6tTsb9agNqG62sg17cAxBMGmsZAgiy4rz_7qyx2JelGvjYnCd',
        '当代艺术是审美的表达，也是现代社会文化的一部分。', '["当代艺术", "作品"]', '["艺术分析"]', 7854, 6321, 124, 7854,
        654, 5546,
        '{"altitude": 234.56, "longitude": 116.9876, "latitude": 40.5432, "country": "中国", "countryID": "CN", "province": "北京市", "provinceID": "11", "city": "北京市", "cityID": "1101", "county": "海淀区", "countyID": "110108", "detailedLocation": "西四环南路456号"}'),
       (24, 2, '我寄生于荆棘', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSWmZyUJNUZg3-tMS-pGRJWU7ql_NoBu8DFuQ&s', '文化差异与交流', '探讨不同文化之间的差异和交流，分析文化交融的影响和意义。', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTg1NVXMCySTd_0vYm45dN6kyvaMo9MOqvPGw&s',
        '文化是人类的共同财富，文化交流可以促进世界和平与发展。', '["文化差异", "交流"]', '["文化交流"]', 6325, 9652, 145,
        7852, 6542, 941,
        '{"altitude": 456.78, "longitude": 113.5432, "latitude": 23.9876, "country": "中国", "countryID": "CN", "province": "广东省", "provinceID": "44", "city": "广州市", "cityID": "4401", "county": "越秀区", "countyID": "440104", "detailedLocation": "广州大道456号"}'),
       (25, 3, '拂晓晨曦', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKyv4GU65-Dt1XTYUcYpb_3eoRpPEWzbEGAw&s', '现代社会问题', '分析现代社会面临的各种问题和挑战，探讨解决问题的路径和方法。', 'https://pic.netbian.com/uploads/allimg/240709/163712-17205142322df4.jpg',
        '现代社会问题是复杂多样的，需要全社会共同努力才能解决。', '["现代社会", "问题"]', '["社会分析"]', 2548, 3658, 98,
        4578, 6985, 419,
        '{"altitude": 123.45, "longitude": 120.9876, "latitude": 30.5432, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "33", "city": "宁波市", "cityID": "3302", "county": "江东区", "countyID": "330206", "detailedLocation": "海曙路789号"}'),
       (26, 9, '回首留不住光阴', 'https://pit1.topit.pro/forum/202202/381112035916166.jpg', '全球经济发展', '分析全球经济的发展趋势和影响因素，探讨经济全球化的挑战和机遇。', 'https://pic.netbian.com/uploads/allimg/240312/012812-1710178092fbd7.jpg',
        '全球经济是相互关联的，经济发展需要各国携手合作共同发展。', '["全球经济", "发展"]', '["经济分析"]', 7854, 6321,
        124, 7854, 6543, 247,
        '{"altitude": 234.56, "longitude": 116.9876, "latitude": 40.5432, "country": "中国", "countryID": "CN", "province": "北京市", "provinceID": "11", "city": "北京市", "cityID": "1101", "county": "海淀区", "countyID": "110108", "detailedLocation": "西四环北路456号"}'),
       (27, 10, '继以夏澈于心', 'https://i.juweishe.com/20240127/de5fdf245216a8974a6c08a073f64680.jpg', '未来城市规划', '展望未来城市的规划和发展方向，探讨城市化对生活的影响和挑战。', 'https://pic.netbian.com/uploads/allimg/240902/151248-172526116849b4.jpg',
        '未来城市规划需要考虑可持续发展和人文关怀，建设宜居城市。', '["未来城市", "规划"]', '["城市发展"]', 8521, 3654,
        96, 7854, 6325, 158,
        '{"altitude": 345.67, "longitude": 120.5432, "latitude": 31.9876, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "33", "city": "杭州市", "cityID": "3301", "county": "西湖区", "countyID": "330106", "detailedLocation": "西湖大道789号"}'),
       (29, 4, '南巷暖栀', 'https://lh5.googleusercontent.com/proxy/52BNnke-BQ5fY8qd4VXifa4lwI18k5fSerFoD5xQyCnfilP86_qFpyadYf15yaNite_Dhch0ESC9bx10Uhiw4zWlTCRQvRFDUetpNiOwou80jrrUq1r6', '天文与宇宙探索', '探索天文学与宇宙学的奥秘和发现，分析宇宙对人类的启示。', 'https://pic.netbian.com/uploads/allimg/220530/193120-1653910280105d.jpg',
        '宇宙是无限的宝藏，我们需要不断探索和认识宇宙的奥秘。', '["天文学", "宇宙探索"]', '["科学探索"]', 6325, 9652, 145,
        7852, 6542, 1035,
        '{"altitude": 456.78, "longitude": 113.5432, "latitude": 23.9876, "country": "中国", "countryID": "CN", "province": "广东省", "provinceID": "44", "city": "广州市", "cityID": "4401", "county": "越秀区", "countyID": "440104", "detailedLocation": "广州大道789号"}'),
       (30, 8, '给我三分之一阳光', 'https://www.maomeitu.com/cjpic/frombd/1/253/3408815439/1585338270.jpg', '儿童教育与成长', '探讨儿童教育的重要性和方法，关注儿童成长过程中的问题和挑战。', 'https://cdn.pixabay.com/photo/2022/02/23/17/08/planets-7031048_960_720.jpg',
        '儿童教育是社会的未来，我们需要关注儿童的身心健康和全面发展。', '["儿童教育", "成长"]', '["教育发展"]', 2548,
        3658, 98, 4578, 6985, 351,
        '{"altitude": 123.45, "longitude": 120.9876, "latitude": 30.5432, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "33", "city": "宁波市", "cityID": "3302", "county": "江东区", "countyID": "330206", "detailedLocation": "海曙路789号"}'),
       (31, 1, '爱能有多精彩', 'https://p9.itc.cn/q_70/images03/20201014/3c15a6259e574c89bb877d9fc85412dc.jpeg', '科技与人文结合', '探讨科技与人文的融合与发展，分析科技对人文的影响和改变。', 'https://cdn.pixabay.com/photo/2021/11/13/23/06/tree-6792528_960_720.jpg',
        '科技与人文是相辅相成的，科技发展需要更多人文关怀和智慧。', '["科技", "人文结合"]', '["科技发展", "人文关怀"]',
        7854, 6321, 124, 7854, 6543, 894,
        '{"altitude": 234.56, "longitude": 116.9876, "latitude": 40.5432, "country": "中国", "countryID": "CN", "province": "北京市", "provinceID": "11", "city": "北京市", "cityID": "1101", "county": "海淀区", "countyID": "110108", "detailedLocation": "西四环南路456号"}'),
       (32, 7, '夏风吹乱了发', 'https://img.k2r2.com/uploads/frombd/0/253/2399762584/3202520005.jpg!190pic', '人类基因与进化', '探讨人类基因的奥秘和进化过程，分析基因对人类的影响和未来发展。', 'https://cdn.pixabay.com/photo/2024/03/09/14/54/sea-8622735_960_720.jpg',
        '人类基因是生命的密码，基因进化对人类社会和文明有着深远的影响。', '["人类基因", "进化"]', '["生命科学"]', 3652,
        8952, 324, 3654, 7895, 1348,
        '{"altitude": 567.89, "longitude": 114.5432, "latitude": 30.9876, "country": "中国", "countryID": "CN", "province": "湖北省", "provinceID": "42", "city": "武汉市", "cityID": "4201", "county": "江岸区", "countyID": "420102", "detailedLocation": "解放大道456号"}'),
       (33, 3, '依若萱', 'https://www.fq9.net/public/uploads/pic/tx/1445228333.jpg', '音乐与情感表达', '探讨音乐对情感的表达和启发，分析音乐对人类情感的影响和治疗作用。', 'https://haowallpaper.com/link/common/file/previewFileImg/32654717b8cda2c284cc01b72b58e85c32654717b8cda2c284cc01b72b58e85c',
        '音乐是心灵的良药，它可以带给我们情感的共鸣和宣泄。', '["音乐", "情感表达"]', '["音乐治疗"]', 7854, 6321, 124,
        7854, 6543, 231,
        '{"altitude": 234.56, "longitude": 116.9876, "latitude": 40.5432, "country": "中国", "countryID": "CN", "province": "北京市", "provinceID": "11", "city": "北京市", "cityID": "1101", "county": "海淀区", "countyID": "110108", "detailedLocation": "西四环北路456号"}'),
       (34, 10, '我的离开也是爱', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTI4OAtvTg5hfpCw9IdIQHbSdvTftlDAllkiQ&s', '旅游与文化体验', '分享旅游与文化体验的乐趣和意义，探讨旅游对人的启发和教育作用。', 'https://haowallpaper.com/link/common/file/previewFileImg/a6522fa33ce35953bcc2373375cdecc9a6522fa33ce35953bcc2373375cdecc9',
        '旅游是开阔眼界的好方式，它可以让我们更加了解不同的文化和风土人情。', '["旅游", "文化体验"]', '["旅游教育"]',
        8521, 3654, 96, 7854, 6325, 255,
        '{"altitude": 345.67, "longitude": 120.5432, "latitude": 31.9876, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "33", "city": "杭州市", "cityID": "3301", "county": "西湖区", "countyID": "330106", "detailedLocation": "西湖大道789号"}'),
       (35, 4, '能不能给我一首歌的时间?', 'https://today-obs.line-scdn.net/0h2KrBWqVhbWRPO3jAvN8SM3dtYRV8XXdtbV4hB2JrO1diFy87J18-Bz06Y0hrXyxib1QnATo-M1JhC3hmIw/w1200', '鞠婧祎简介', '鞠婧祎（1994年6月18日—），出生于四川省遂宁市，中国女演员、歌手及舞者。所属经纪公司为丝芭传媒[1]，前为大型女子偶像团体SNH48及小分队塞纳河组合的成员。', 'https://pic.3gbizhi.com/uploadmark/20230808/80f99d63fdbb9947e4022d03ce9ea925.jpg',
        '活动与经历
SNH48女团时期
2013年8月18日，通过SNH48二期生审查，成为34名合格者之一[2]。11月11日，正式成为SNH48Team NII成员[3]。[4]11月16日，Team NII参与了“中国移动“咪咕和Ta的朋友”～这些年，我们一起追过的SNH48～ SNH48广州万人演唱会”[5]。12月31日，Team NII参加东方卫视跨年晚会[6][7]。

2014年1月18日Team NII参与了“SNH48红白歌会”，鞠婧祎以微弱优势战胜S队歌姬徐晨辰，成为当晚红白对抗赛N队唯一的胜利[8]。7月26日，于SNH48第一届总选举，获得第4名，最终获得10,145票，入选第一届总选举选拔组[9][10]。9月30日，代言SONY旗下Hi-Res AUDIO系列。10月11日达成剧场百场纪念公演。11月上旬，由于被称为“4000年第一偶像”，而在日本网络上引发话题[11]。后转载至中国时，媒体误译为“4000年第一美女”，但也因此广为人知[12][13]。12月12日为剧场【人气女神】答谢公演，31日Team NII参加东方卫视2015年跨年盛典。

2015年1月31日，参加了“SNH48年度金曲大赏演唱会”[14]，现场演唱代言手机游戏《魔天记》主题曲《缘尽世间》[15]。2月17日，参与安徽卫视和江苏卫视的春节晚会[16][17]。3月6日，为代言百事可乐主唱主题曲《羊咩咩》[18]4月8日，出席美图手机发布会[19]。15日在《乐弹乐有料》担任嘉宾，首谈4000年事件对她的影响。4月30日，出演《魔天记》微电影《魔天劫》上映，饰演珈蓝一角[20]。5月21日，出席TCL么么哒手机发布会[21]。6月上旬，代言海俪恩桃花秀隐形眼镜[22]。6月13日为剧场生日公演。7月25日于SNH48第二届总选举，获得第2名，最终获得64,785.5票，入选第二届总选举选拔组[23]。9月13日，以塞纳河组合成员于SNH48星梦剧院首次正式亮相演出及发布[24]。同时为剧场2周年公演日榜冠军。9月7日，出席代言腾讯游戏《上古世纪》发布会[25]。9月23日，加盟湖南卫视的《全员加速中》[26]。9月25日，出席代言echo回声App活动[27]。11月20日，被人民网舆论监察室评为10月明星好评率排行榜第一位[28]。12月2日，出演芒果TV网络剧《史料不及》[29]。12月26日，参与了“SNH48第二届年度金曲大赏BEST 30”[30]。12月31日，参加Team NII东方卫视2016跨年晚会演出活动。

2016年1月1日，参加《我是歌手 谁来踢馆》[31]。1月8日，参与江苏卫视节目《最强大脑》[32]2月22日，参加湖南卫视2016湖南卫视元宵喜乐会[33]。6月19日，参与江苏卫视音乐节目《盖世音雄》[34]。6月30日，受邀为《麻辣变形计》电视剧主题曲《Fighting Day》献声[35]。2016年7月15日，为《九州·天空城》电视剧插曲《醉飞霜》献唱[36]。7月20日，出演由企鹅影业、上影寰亚联合出品的《九州·天空城》，饰演雪飞霜一角[37]，这次为鞠婧祎以偶像身份首次进入电视剧萤幕，并在艺人媒体指数排行榜上升至13位[38]。7月30日，于SNH48第三届总决选，获得第1名，成功登顶，最终获得230,752.7票，成为第三届总选举选拔组Center，又成为SNH48迄今为止票数首位超过20万的成员[39]。8月份随组合前往西班牙拍摄汇报单MV《公主披风》。10月27日，发行首张个人EP《每一天》。11月11日，SNH48出席天猫双十一全球狂欢夜晚会。12月10日，鞠婧祎参加腾讯视频星光大赏，凭借《九州·天空城》中的雪飞霜一角获得腾讯视频星光大赏“年度新锐电视剧女演员荣誉”并演唱《醉飞霜》。12月18日，参加2016网易有态度人物盛典获得了“年度最有态度元气新星”。12月底带领SNH48部分成员登上安徽卫视国剧盛典演出《公主披风》并个人Solo演唱《醉飞霜》。

2017年1月7日，参与了“SNH48第三届年度金曲大赏BEST 50”[40]，与Team NII成员曾艳芬、赵粤合作之三人曲《Don''t Touch》为BEST 1，作为第一名歌曲的中心位获得了额外的MV拍摄机会，远赴美国纽约的布鲁克林、曼哈顿、唐人街等地进行录制。2月15日，出演由优酷视频，盟将威影视，嗨乐娱乐及世纪影游联合出品的《热血长安》，饰演上官紫苏一角。5月4日，鞠婧祎代表SNH48获得中央“五四优秀青年”称号。6月8日，发行第2张个人EP《等不到你》。6月18日，于万代南梦宫文化中心举办生日Fan Meeting。7月10日，作为第三届总选奖励，鞠婧祎出演丝芭影视首部自制电视剧《芸汐传》，饰演女主角韩芸汐。7月29日，于SNH48第四届总决选，获得了第1名，最终获得277,781.3票，成为第四届总选举选拔组Center，为SNH48首位于总选举连霸的成员。12月15日，丝芭传媒正式宣布鞠婧祎晋升SNH48 Group明星殿堂，成立个人工作室单飞发展，日后将以个人名义进行活动，鞠婧祎成为SNH48 Group第一个晋升明星殿堂的成员[41]。12月16日，出席第十一届音乐盛典咪咕汇，以《想你了》获得年度十大金曲。12月18日，鞠婧祎单飞后新曲《分裂时差》首发[42]。12月27日，鞠婧祎个人工作室正式发布了鞠婧祎个人记录片先导版本－《StarLight·星梦之光》[43][44]。

单飞后：个人发展
2018年1月16日，参加“微博之夜”颁奖典礼，获得“微博年度进取艺人”的荣誉[45][46]。3月4日回到SNH48星梦剧院参演Team NII 【忆往昔】暨江真仪生日公演，此为鞠婧祎最后一次在剧场演出。26日，赴上海参加第25届东方风云榜音乐盛典，获颁东方风云榜“年度跨界艺人奖”[47]。5月20日，赴苏州参加果本15周年盛典。6月24日，出席《快乐中国毕业歌会》。7月28日，参与综艺《快乐大本营》播出[48]。10月18日，正式加盟爱奇艺《国风美少年》，担任节目召集人。11月3日，出席网易云国风极乐夜，演唱歌曲《叹云兮》，并获得“国风新锐女歌手奖”。11月16日参与青年艺人合唱之单曲“未来已来”。12月1日出席爱奇艺尖叫之夜，获“年度戏剧潜力艺人”奖。隔日参加SNH48成员万丽娜的生日冷餐会。

2019年1月1日出席《由你音乐榜》，宣传歌曲《Don''t Touch》。2月19日同时登上央视CCTV-1《喜气洋洋闹元宵》与湖南卫视《元宵喜乐会》。2月25日赴加拿大参与首届中加电视节，以《芸汐传》中的韩芸汐一角获得“国际影视传播作品​网剧最佳女演员奖”。3月25日于第26届东方风云榜音乐盛典，获颁东方风云榜“年度风尚歌手奖”并演唱《叹云兮》[49]。4月3日网剧《新白娘子传奇》首播，鞠婧祎主演并演唱主题曲《千年等一回》。6月18日远于日本拍摄MV的新曲《孤独与诗》首发。7月9日网剧《请赐我一双翅膀》首播，鞠婧祎主演并演唱片尾曲《一双翅膀》。9月11日，作为女主角参演的《如意芳霏》开机，饰演傅容一角[50]。9月26日，参与由网易云音乐、中国青年报、团中央网络影视中心联合出品的《我和我的祖国》青春版MV上线，鞠婧祎在其中饰演一名青年志愿者，前往敬老院为长者们演奏小提琴。与来自各行各业的青年，共同绘成中国青年追梦奋斗的缩影[51]。10月31日，EMPHASIS艾斐诗官方微博宣布鞠婧祎为EMPHASIS艾斐诗HARMONY“合”系列代言人。11月8日歌曲《恋爱告急》、《虹》首发[52]。10日出席《天猫双11狂欢夜》随即演唱新曲《恋爱告急》[53]。12月22日在CCTV综艺节目《一堂好课》中担任音乐课课代表[54]，同日佩戴全新珠宝系列FORM“形”惊艳亮相上海港汇恒隆广场精品店的EMPHASIS璀璨圣诞活动。

2020年1月24日，参演《2020年中央广播电视总台春节联欢晚会》小品《喜欢你喜欢我》[55]。 2月15日，在央视CCTV-6《佳片有约》推荐电影《伯德小姐》[56]。 3月6日，发布与SNH48成员万丽娜、陈琳、陈思、姜杉合作的翻唱跳舞台《冬日》PV[57]。 3月14日，鞠婧祎芭莎男士3月电子刊发售[58]。 3月28日，参与综艺《快乐大本营》播出[59]。 3月29日，发布与SNH48成员陈琳合作的翻唱跳舞台《爱未央》PV[60]。 4月1-9日，参与《朋友请听好》系列综艺播出[61]。 4月13日，参与访谈综艺《甜蜜的任务》播出[62]。 5月3日，在湖南卫视五四晚会演唱《阳光总在风雨后》并与彭昱畅合唱《我的未来不是梦》[63]。 6月1日，在央视儿童节特别节目演出《熊猫和小鼹鼠》主题歌[64]。 6月2-9日，参与爱奇艺网络综艺我要这样生活播出[65]。 6月16日，南方人物周刊鞠婧祎特辑发售[66]。 6月17日，在湖南卫视618超拼夜演唱《氧气》，并与张翰合唱《温柔》[67]。 6月18日，作为生日惊喜发布《恋爱告急》舞台版PV。 6月28日，鞠婧祎作为费加罗MODE7月刊封面人物的杂志发售[68]。 7月11日，为动画恋与制作人献声的片尾曲《梦的旅航》发布[69]。 7月19日，出席第27届东方风云榜荣获最佳舞台演绎与年度最具突破艺人，与霍尊合唱《平凡与伟大》开场并单独演唱《古画》宣传《如意芳霏》[70]。 7月23日，作为女主角参演的《慕南枝》开机，饰演姜保宁一角[71]。 同日，与宋威龙搭档的古装书院爱情喜剧《漂亮书生》在爱奇艺播出[72]。 7月4日-8月1日，参与综艺青春环游记2第5、6、9集分别播出[73]。 7月27日，鞠婧祎作为时尚健康2020 ISSUE 08封面人物的杂志发售[74]。 8月2日，与曾舜晞代表《慕南枝》剧组出席2020腾讯视频年度发布会[75]。 9月7日，出席YSL高订时妆发布会[76]。 10月21日，与张哲瀚再次搭档的古装爱情网络剧《如意芳霏》在爱奇艺播出[77]。 10月31日，在浙江卫视苏宁超级秀上演唱《芙蓉》[78]。 11月中前往内蒙古呼伦贝尔市拍摄MV。 11月29日，作为女主角参演的《满月之下请相爱》开机，饰演雷初夏一角[79]。 12月31日，在浙江卫视跨年晚会演出《恋爱告急》[80]。

2021年1月5日，发行冬季单曲《过去完成时》，12小时内销量突破20万张，并于 1月7日发布该单曲MV[81]。 2月12日，在北京卫视春晚参与《相约北京》《少年》合唱[82]。 2月28日，出席2020微博之夜，获得微博年度进取艺人奖[83]。 4月10日，作为助演学姐参加综艺创造营2021的第三次公演，与米卡、高卿尘、吴宇恒、薛八一、张星特、曾涵江合作演出《输入法打可爱按第五》[84]。 5月4日，参与新华社青春拾来五四青春歌会，翻唱并演出《青春舞曲》[85]。 5月13日，为电视剧《风暴舞》献唱的插曲《倒流》上线[86]。 5月18日，为博物馆主题专辑《国宝传音》献唱的南京博物馆文物推广曲《琢光曲》发布[87]。 6月5日，参与综艺《快乐大本营》播出[88]。 6月7日，与曾舜晞一起出席腾讯视频拾光盛典宣传《嘉南传》，并演出《只对你有感觉》[89]。 6月10日，与曾舜晞一起出席第27届上海电视节白玉兰奖宣传《嘉南传》[90]。 6月16日，在湖南卫视天猫开心夜与侯明昊合作演出《你的眼睛像星星》[91]。 6月27日，作为女主角参演的《花戎》开机，分饰魏枝/司马忘月/魔后三角[92]。 6月29日，由QQ音乐发起合作，与姜云升共同创作的单曲《今晚夜色真美》发布[93]。 8月6日，为动画天官赐福献声，演唱灵文角色曲《天下清》[94]。 8月26日，与郑业成搭档主演的满月之下请相爱在爱奇艺播出[95]。 10月17日，与曾舜晞搭档主演的古装爱情剧《嘉南传》在腾讯视频及爱奇艺播出[96]。 12月31日，在北京卫视环球跨年冰雪盛典上与朱正廷合唱《入海》[97]。

2022年1月7日，前往山东济宁曲阜，作为大运河推广官参与公益直播[98]。 1月10日，在湖南卫视芒果年货节与陈立农合唱《花海》[99]。 2月16日，作为女主角参演的古装仙侠修练剧《仙剑四》开机，原作为游戏仙剑奇侠传四，在剧中饰演韩菱纱一角[100]。 2月22日，发行单曲《0.2s》[101]。 2月27日参加快手一千零一夜百大主播晚会演出《0.2s》《假面舞会》[102]。 8月12日，友情出演金融反诈警匪片《金钱堡垒》，在剧中饰演韩小美一角[103]。 8月25日，作为女主角参演相声社团德云社的喜剧电影《粉墨江湖》在天津德云社内开机，饰演澹台台一角[104]。 10月13日发布翻唱曲《以十洲的名义》，为SNH48十周年曲目[105]。 11月9号，于河南卫视国潮盛典压轴演出《灯火流光》，展现剪纸、风筝、刺绣等非遗技艺[106]。 11月13日，公布出道九周年纪念实体专辑《IX》发行预告，鞠婧祎首次以总监制的身份参与了专辑的创作，本次专辑共有2款发行版本，分别为精装版及平装版[107]。 11月17日，发行出道九周年纪念实体专《IX》的收录曲《今日晴》[108]。 11月22日，与QQ音乐合作发行出道【九周年典藏星光卡】主题卡池[109]。 11月25日，于第29届东方风云榜荣获年度人气歌手，并演出《恋爱告急》[110]。 12月15日，发行出道九周年纪念实体专《IX》的主打曲《Be My POI》，并发布官方PV[111]。 12月31日，在浙江卫视美好跨年夜上演出《千年等一回》《恋爱告急》[112]。',
        '["明星","介绍"]', '["自传"]', 165, 9586, 756, 896, 425, 265, '{"altitude": 31.2304, "longitude": 121.4737, "latitude": 31.2304, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "SH", "city": "上海", "cityID": "1796236", "county": "", "countyID": "", "detailedLocation": "上海市上海市黄浦区黄浦南路"}');


# 2024-3-27  20:29-往dialogue表中插入数据
INSERT
INTO `dialogue` (`id`, `uid`, `title`, `content`, `tags`, `category`, `browse_num`, `like_num`, `unlike_num`,
                 `comment_num`, `star_num`, `share_num`, `address_info`)
VALUES (1, 3, '娱乐圈八卦大揭秘', '最近有关娱乐圈的八卦消息不断，小道消息称某明星与某富豪热恋中，引起广泛关注。',
        '["娱乐圈", "八卦"]', '["娱乐新闻"]', 8834, 7152, 189, 2315, 5793, 1651,
        '{"altitude": 35.6895, "longitude": 139.6917, "latitude": 35.6895, "country": "日本", "countryID": "JP", "province": "东京都", "provinceID": "13", "city": "东京", "cityID": "1850147", "county": "", "countyID": "", "detailedLocation": "东京都东京市千代田区千代田1-1"}'),
       (2, 4, '某综艺节目吐槽大会', '近期某综艺节目吐槽环节笑料百出，网友纷纷表示过足了幽默瘾，调侃节目更具互动性。',
        '["综艺节目", "吐槽"]', '["娱乐综艺"]', 7542, 987, 543, 1578, 4021, 56,
        '{"altitude": 40.7128, "longitude": 74.0060, "latitude": 40.7128, "country": "美国", "countryID": "US", "province": "纽约州", "provinceID": "NY", "city": "纽约", "cityID": "5128581", "county": "", "countyID": "", "detailedLocation": "纽约州纽约市曼哈顿区曼哈顿"}'),
       (3, 9, '生活小贴士：防止电脑眼疲劳',
        '长时间对着电脑容易导致眼疲劳，建议每小时休息5分钟，眼保健操和远眺绿色有助缓解。', '["生活小贴士", "眼保健"]',
        '["健康生活"]', 3985, 1523, 94, 872, 2876, 859,
        '{"altitude": 31.2304, "longitude": 121.4737, "latitude": 31.2304, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "SH", "city": "上海", "cityID": "1796236", "county": "", "countyID": "", "detailedLocation": "上海市上海市黄浦区黄浦南路"}'),
       (4, 8, '今天遇到的倒霉事大揭秘', '今天出门遇到大雨，车子被淋湿，还差点迟到，真是倒霉透顶，希望明天运气能好些。',
        '["倒霉事", "遭遇"]', '["生活分享"]', 5147, 295, 467, 1089, 1910, 894,
        '{"altitude": 39.9042, "longitude": 116.4074, "latitude": 39.9042, "country": "中国", "countryID": "CN", "province": "北京市", "provinceID": "BJ", "city": "北京", "cityID": "1816670", "county": "", "countyID": "", "detailedLocation": "北京市北京市东城区东城南路"}'),
       (5, 1, '我最近很喜悦！', '最近收到了一份意外的礼物，心情特别好，分享给大家！', '["喜悦", "礼物"]', '["生活分享"]',
        6321, 8492, 63, 2387, 4215, 216,
        '{"altitude": 51.5074, "longitude": 0.1278, "latitude": 51.5074, "country": "英国", "countryID": "GB", "province": "伦敦市", "provinceID": "ENG", "city": "伦敦", "cityID": "2643743", "county": "", "countyID": "", "detailedLocation": "英国伦敦市伦敦市威斯敏斯特区"}'),
       (6, 2, '某综艺节目爆笑片段', '某综艺节目播放了一段搞笑视频，笑到肚子疼，赶紧分享给大家一起乐呵乐呵！',
        '["综艺节目", "爆笑"]', '["娱乐分享"]', 7984, 536, 201, 1872, 3589, 165,
        '{"altitude": 48.8566, "longitude": 2.3522, "latitude": 48.8566, "country": "法国", "countryID": "FR", "province": "法兰西岛", "provinceID": "IDF", "city": "巴黎", "cityID": "2968815", "county": "", "countyID": "", "detailedLocation": "法国法兰西岛巴黎市巴黎市巴黎"}'),
       (21, 5, '最新热门电影推荐', '最新热门电影推荐：某电影票房口碑双丰收，影迷期待已久，强烈推荐！',
        '["电影推荐", "热门"]', '["娱乐推荐"]', 4156, 8654, 129, 3825, 2461, 167,
        '{"altitude": 34.0522, "longitude": 118.2437, "latitude": 34.0522, "country": "美国", "countryID": "US", "province": "加利福尼亚州", "provinceID": "CA", "city": "洛杉矶", "cityID": "5368361", "county": "", "countyID": "", "detailedLocation": "加利福尼亚州洛杉矶市洛杉矶市中心"}'),
       (22, 7, '某明星代言新品发布', '某明星代言的新品即将发布，预告片已公开，引发粉丝热议和期待。',
        '["明星代言", "新品发布"]', '["产品推广"]', 9582, 3269, 148, 2596, 7153, 2568,
        '{"altitude": 22.3193, "longitude": 114.1694, "latitude": 22.3193, "country": "中国", "countryID": "CN", "province": "香港", "provinceID": "HK", "city": "香港", "cityID": "1819729", "county": "", "countyID": "", "detailedLocation": "香港香港岛中西区中环"}'),
       (23, 6, '旅行心得分享', '最近的一次旅行收获满满，分享给大家我的旅行心得和美景照片！', '["旅行心得", "美景分享"]',
        '["生活旅行"]', 6274, 2486, 52, 1734, 4029, 782,
        '{"altitude": 37.7749, "longitude": 122.4194, "latitude": 37.7749, "country": "美国", "countryID": "US", "province": "加利福尼亚州", "provinceID": "CA", "city": "旧金山", "cityID": "5391959", "county": "", "countyID": "", "detailedLocation": "加利福尼亚州旧金山市旧金山市中心"}'),
       (24, 10, '科技新闻：某公司发布新产品', '某知名科技公司发布新产品，引领科技潮流，吸引众多关注。',
        '["科技新闻", "新产品发布"]', '["科技资讯"]', 7985, 6235, 182, 3489, 5172, 892,
        '{"altitude": 51.5074, "longitude": 0.1278, "latitude": 51.5074, "country": "英国", "countryID": "GB", "province": "伦敦市", "provinceID": "ENG", "city": "伦敦", "cityID": "2643743", "county": "", "countyID": "", "detailedLocation": "英国伦敦市伦敦市威斯敏斯特区"}'),
       (25, 2, '某明星参加慈善活动', '某明星近日参加了一场大型慈善活动，为社会公益事业贡献一份力量。',
        '["明星慈善", "公益活动"]', '["社会公益"]', 9853, 1573, 268, 4196, 6347, 684,
        '{"altitude": 40.7128, "longitude": 74.0060, "latitude": 40.7128, "country": "美国", "countryID": "US", "province": "纽约州", "provinceID": "NY", "city": "纽约", "cityID": "5128581", "county": "", "countyID": "", "detailedLocation": "纽约州纽约市曼哈顿区曼哈顿"}'),
       (26, 9, '某剧集最新剧情猛料', '某热播剧集近期剧情猛料曝光，引发网友热议和剧迷期待。', '["热播剧集", "剧情猛料"]',
        '["剧集资讯"]', 7234, 4856, 187, 2981, 4219, 49,
        '{"altitude": 31.2304, "longitude": 121.4737, "latitude": 31.2304, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "SH", "city": "上海", "cityID": "1796236", "county": "", "countyID": "", "detailedLocation": "上海市上海市黄浦区黄浦南路"}'),
       (28, 3, '某综艺节目最新热议话题', '某综艺节目最新热议话题是关于家庭教育的讨论，吸引众多观众关注。',
        '["综艺节目", "热议话题"]', '["娱乐资讯"]', 5269, 9354, 346, 4789, 5421, 564,
        '{"altitude": 35.6895, "longitude": 139.6917, "latitude": 35.6895, "country": "日本", "countryID": "JP", "province": "东京都", "provinceID": "13", "city": "东京", "cityID": "1850147", "county": "", "countyID": "", "detailedLocation": "东京都东京市千代田区千代田1-1"}'),
       (29, 7, '某知名音乐人新专辑发布', '某知名音乐人即将发布全新专辑，音乐风格多样，备受期待和好评。',
        '["音乐人", "新专辑发布"]', '["音乐资讯"]', 7524, 4863, 97, 2974, 4285, 483,
        '{"altitude": 22.3193, "longitude": 114.1694, "latitude": 22.3193, "country": "中国", "countryID": "CN", "province": "香港", "provinceID": "HK", "city": "香港", "cityID": "1819729", "county": "", "countyID": "", "detailedLocation": "香港香港岛中西区中环"}'),
       (30, 10, '健康养生小贴士分享', '分享一些健康养生小贴士，希望大家能够有一个健康的生活方式。',
        '["健康养生", "小贴士"]', '["健康生活"]', 3921, 2875, 56, 1392, 2498, 4812,
        '{"altitude": 51.5074, "longitude": 0.1278, "latitude": 51.5074, "country": "英国", "countryID": "GB", "province": "伦敦市", "provinceID": "ENG", "city": "伦敦", "cityID": "2643743", "county": "", "countyID": "", "detailedLocation": "英国伦敦市伦敦市威斯敏斯特区"}'),
       (31, 9, '某品牌新款上市', '某时尚品牌最新款式即将上市，预告图已发布，引发潮流追随者热议。',
        '["时尚品牌", "新款上市"]', '["时尚资讯"]', 6132, 4387, 112, 2567, 3824, 485,
        '{"altitude": 31.2304, "longitude": 121.4737, "latitude": 31.2304, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "SH", "city": "上海", "cityID": "1796236", "county": "", "countyID": "", "detailedLocation": "上海市上海市黄浦区黄浦南路"}'),
       (32, 4, '某健身教练分享运动技巧', '某知名健身教练分享最新的运动技巧，帮助健身爱好者更科学有效地锻炼身体。',
        '["健身教练", "运动技巧"]', '["健康运动"]', 8547, 6349, 276, 3895, 5974, 813,
        '{"altitude": 39.9042, "longitude": 116.4074, "latitude": 39.9042, "country": "中国", "countryID": "CN", "province": "北京市", "provinceID": "BJ", "city": "北京", "cityID": "1816670", "county": "", "countyID": "", "detailedLocation": "北京市北京市东城区东城南路"}'),
       (33, 6, '某大学举办国际学术会议', '某知名大学即将举办国际学术会议，邀请国内外专家学者探讨前沿科研话题。',
        '["学术会议", "国际学术"]', '["学术交流"]', 7123, 5476, 189, 2984, 4289, 8681,
        '{"altitude": 37.7749, "longitude": 122.4194, "latitude": 37.7749, "country": "美国", "countryID": "US", "province": "加利福尼亚州", "provinceID": "CA", "city": "旧金山", "cityID": "5391959", "county": "", "countyID": "", "detailedLocation": "加利福尼亚州旧金山市旧金山市中心"}'),
       (35, 1, '某健康品牌新品发布', '某知名健康品牌即将发布全新产品，宣称有助于提高免疫力和改善睡眠质量。',
        '["健康品牌", "新品发布"]', '["健康资讯"]', 5824, 3875, 82, 1592, 2764, 458,
        '{"altitude": 40.7128, "longitude": 74.0060, "latitude": 40.7128, "country": "美国", "countryID": "US", "province": "纽约州", "provinceID": "NY", "city": "纽约", "cityID": "5128581", "county": "", "countyID": "", "detailedLocation": "纽约州纽约市曼哈顿区曼哈顿"}'),
       (36, 3, '某明星出席时尚发布会', '某知名明星近期出席了一场时尚发布会，穿着大气，引发时尚圈热议。',
        '["明星时尚", "发布会"]', '["时尚资讯"]', 4765, 2987, 135, 2287, 3541, 264,
        '{"altitude": 35.6895, "longitude": 139.6917, "latitude": 35.6895, "country": "日本", "countryID": "JP", "province": "东京都", "provinceID": "13", "city": "东京", "cityID": "1850147", "county": "", "countyID": "", "detailedLocation": "东京都东京市千代田区千代田1-1"}'),
       (37, 6, '某旅游目的地推荐', '最近去了一个很美的旅游目的地，推荐给大家，特别适合度假放松。', '["旅游推荐", "度假"]',
        '["旅游资讯"]', 7284, 5486, 267, 3562, 4693, 584,
        '{"altitude": 37.7749, "longitude": 122.4194, "latitude": 37.7749, "country": "美国", "countryID": "US", "province": "加利福尼亚州", "provinceID": "CA", "city": "旧金山", "cityID": "5391959", "county": "", "countyID": "", "detailedLocation": "加利福尼亚州旧金山市旧金山市中心"}'),
       (38, 4, '某书籍推荐：心理学经典', '最近读了一本心理学经典书籍，内容十分精彩，推荐给对心理学感兴趣的朋友们。',
        '["心理学书籍", "经典推荐"]', '["心理学分享"]', 8146, 6953, 187, 3275, 5821, 723,
        '{"altitude": 39.9042, "longitude": 116.4074, "latitude": 39.9042, "country": "中国", "countryID": "CN", "province": "北京市", "provinceID": "BJ", "city": "北京", "cityID": "1816670", "county": "", "countyID": "", "detailedLocation": "北京市北京市东城区东城南路"}'),
       (39, 9, '某品牌新品服饰发布', '某知名时尚品牌发布了全新的服饰产品，风格独特，备受关注和喜爱。',
        '["时尚品牌", "新品发布", "服饰"]', '["时尚资讯"]', 6954, 4378, 153, 2754, 3965, 269,
        '{"altitude": 31.2304, "longitude": 121.4737, "latitude": 31.2304, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "SH", "city": "上海", "cityID": "1796236", "county": "", "countyID": "", "detailedLocation": "上海市上海市黄浦区黄浦南路"}'),
       (40, 5, '某体育赛事精彩瞬间', '某体育赛事最近的一场比赛中出现了许多精彩瞬间，让人过足了体育瘾。',
        '["体育赛事", "精彩瞬间"]', '["体育资讯"]', 8735, 6874, 204, 3987, 6243, 496,
        '{"altitude": 34.0522, "longitude": 118.2437, "latitude": 34.0522, "country": "美国", "countryID": "US", "province": "加利福尼亚州", "provinceID": "CA", "city": "洛杉矶", "cityID": "5368361", "county": "", "countyID": "", "detailedLocation": "加利福尼亚州洛杉矶市洛杉矶市中心"}'),
       (42, 8, '某品牌限时优惠活动', '某知名品牌推出限时优惠活动，各种商品打折促销，速来抢购！',
        '["品牌优惠", "限时活动"]', '["购物促销"]', 5126, 4256, 86, 1975, 3287, 272,
        '{"altitude": 48.8566, "longitude": 2.3522, "latitude": 48.8566, "country": "法国", "countryID": "FR", "province": "法兰西岛", "provinceID": "11", "city": "巴黎", "cityID": "2988507", "county": "", "countyID": "", "detailedLocation": "法兰西岛巴黎市巴黎市中心"}'),
       (43, 3, '某游戏最新版本上线', '某知名游戏最新版本已经上线，新增了多项内容和功能，快来体验吧！',
        '["游戏更新", "版本上线"]', '["游戏资讯"]', 6475, 4975, 125, 3258, 4732, 591,
        '{"altitude": 35.6895, "longitude": 139.6917, "latitude": 35.6895, "country": "日本", "countryID": "JP", "province": "东京都", "provinceID": "13", "city": "东京", "cityID": "1850147", "county": "", "countyID": "", "detailedLocation": "东京都东京市千代田区千代田1-1"}'),
       (44, 7, '某新科技产品发布', '某科技公司最新研发的产品即将发布，预告片已经曝光，引发广泛关注。',
        '["新科技产品", "产品发布"]', '["科技资讯"]', 7824, 6256, 189, 3987, 5471, 158,
        '{"altitude": 22.3193, "longitude": 114.1694, "latitude": 22.3193, "country": "中国", "countryID": "CN", "province": "香港", "provinceID": "HK", "city": "香港", "cityID": "1819729", "county": "", "countyID": "", "detailedLocation": "香港香港岛中西区中环"}'),
       (45, 5, '某健康食品推荐', '最近发现一款非常好吃的健康食品，分享给大家，健康又美味！', '["健康食品", "美食推荐"]',
        '["健康生活"]', 9432, 5278, 152, 3582, 4965, 489,
        '{"altitude": 34.0522, "longitude": 118.2437, "latitude": 34.0522, "country": "美国", "countryID": "US", "province": "加利福尼亚州", "provinceID": "CA", "city": "洛杉矶", "cityID": "5368361", "county": "", "countyID": "", "detailedLocation": "加利福尼亚州洛杉矶市洛杉矶市中心"}'),
       (46, 10, '某艺术展览即将开幕', '某艺术展览即将开幕，汇集了众多艺术家的作品，展现多元文化魅力。',
        '["艺术展览", "文化活动"]', '["艺术文化"]', 6387, 4856, 167, 2987, 4156, 5648,
        '{"altitude": 51.5074, "longitude": 0.1278, "latitude": 51.5074, "country": "英国", "countryID": "GB", "province": "伦敦市", "provinceID": "ENG", "city": "伦敦", "cityID": "2643743", "county": "", "countyID": "", "detailedLocation": "英国伦敦市伦敦市威斯敏斯特区"}'),
       (47, 2, '某时尚品牌春季新品发布', '某知名时尚品牌推出了春季新品系列，时尚感十足，让人眼前一亮！',
        '["时尚品牌", "春季新品"]', '["时尚资讯"]', 8721, 6482, 178, 3875, 5394, 86,
        '{"altitude": 40.7128, "longitude": 74.0060, "latitude": 40.7128, "country": "美国", "countryID": "US", "province": "纽约州", "provinceID": "NY", "city": "纽约", "cityID": "5128581", "county": "", "countyID": "", "detailedLocation": "纽约州纽约市曼哈顿区曼哈顿"}'),
       (48, 3, '李知恩歌手简介', '李知恩[b]（韩语：이지은 I Ji Eun，1993年5月16日—），艺名IU（韩语：아이유 A I Yu），是一位韩国K-pop创作歌手、偶像明星和韩剧演员。她的艺名“IU”是“I”和“You”的组合，意味着通过音乐我们彼此相连。在全世界很流行的K-pop韩国流行音乐中，IU是最畅销的创作歌手之一。[1]

至今，IU已发行了5张录音室专辑和5张EP，以及2张怀旧歌曲翻唱专辑。她在Gaon榜上有2张专辑和14首歌曲曾经获得第一名。自2012年起，IU一直是《福布斯》杂志年度韩国名人权力榜40强之一，并在2012年达到第3名的最高排名。[2][3][4]《公告牌》认可IU为韩国流行音乐百强榜的先锋，她是拥有最多冠军歌曲且维持第一位置周数最长的艺人之一。[5]她在2014年和2017年被评为Gallup韩国年度最佳歌手。[6][7]

在IU早期从事歌唱工作时，她以带有青春偶像特质的独唱歌手形象为人熟知。随着职业生涯的发展，IU逐渐转变为一位多才多艺的音乐工作者，并因对流行文化的影响与贡献被视为艺术家。除了成为知名度、歌曲销量和奖项兼具的天后级歌手外，她也是受到认可的词曲作家和音乐制作人。她还在许多电视剧中出演，包括《我的大叔》、《德鲁纳酒店》等代表作品。[8]

早年生活
IU于1993年5月16日生于韩国，自小在首尔长大，与其父母和弟弟住在一起，家里曾开名为宝物岛的文具店。IU上小学，家庭经济状况恶化[9]，父母因为为他人作担保而欠下了巨债[10]，一家人被迫分开生活，与IU的奶奶和表姐在一间公寓中度过了一年多贫困的生活[11][12]。这一段时间，IU与她的父母接触不多，因而很感激奶奶的出现[12]。

在中学时，IU发现了她对唱歌的热情。在校内运动会演唱并赢得了老师及同学的掌声后，IU萌生了成为歌手的想法[13]。她曾先后参加过20次选拔但均被拒绝，并且曾被伪造的娱乐公司欺骗[14]。2007年10月，IU通过当时经纪公司LOEN娱乐的试镜，并成为练习生[15]。她在训练10个月后便随即发行她的首张个人专辑[16]。因为曾经艰苦的生活，IU说她“喜欢在练习室”，在那里她可以吃她想吃的东西，并且有睡觉的地方[12]。在出道之前，LOEN娱乐将其艺名命名为“IU”。艺名从“我和你”（I and You）中引申而出，象征着音乐在人与人之间所产生的凝聚力[16]。

职业生涯
在2007年，IU与Kakao M（前LEON娱乐）签约成为练习生[17]，15岁时推出了首张迷你专辑《Lost and Found》，开启了她的歌手生涯。随后，她的知名度逐渐上升[18]，特别是在2010年发布的专辑《Real》中的主打歌《好日子》大获成功，连续五周位居韩国Gaon数位榜榜首。2011年，随着专辑《Real+》和《Last Fantasy》的成功[19]，IU在韩国音乐排行榜上确立了强势地位，被称为“国民妹妹”，现在更被称为“国民甜心”[20]。她开始涉足歌曲创作，并在2013年发行的专辑《Modern Times》中展现出成熟风格[21]。随后的专辑也保持了她在音乐排行榜上的主导地位，即使风格有所改变，如2014年的怀旧歌曲翻唱专辑《花书签》和2015年的第四张迷你专辑《CHAT-SHIRE》。[22][23]

音乐成就
2008年9月18日，IU以演唱《迷儿》在《M! Countdown》[24]正式出道，随后推出首张迷你专辑《Lost and Found》。2009年4月23日，发行首张正规专辑《Growing Up》[25]，主打歌《Boo》在各大音乐节目及线上音源排行榜取得佳绩，使她逐渐受到注目。11月12日，IU推出第二张迷你专辑《IU...IM》。[26][27]

2010年，IU和2AM成员瑟雍为MBC综艺节目《我们结婚了》合唱主题曲《唠叨》[28]。7月2日在KBS的音乐节目《音乐银行》获得一位。12月9日，IU发布了第三张迷你专辑《Real》，其中的主打歌《好日子》大获成功，带来了轰动效应。这首歌在SBS《人气歌谣》和KBS《音乐银行》均获得了出道后首个三连冠，并在Mnet的音乐节目《M! Countdown》中首次夺得一位。[29][30]


《好日子》
时长：16秒。0:16
《Real》主打歌曲《好日子》的16秒片段。IU的“三段高音”受到了广泛关注。歌曲的流行归因于它的歌词和高声调的高潮。[31][32]
播放此文件有问题？请参见媒体帮助。
2011年3月20日，IU成为SBS音乐节目《人气歌谣》的主持之一[33]。5月，IU为电视剧《最佳爱情》献唱《抓住我的手》[34]。11月29日，IU的第二张正规专辑《Last Fantasy》甫发行便横扫韩国各大音乐榜单，专辑的全部13首歌曲迅速占据了各大音乐网站榜单的1至13位[35][36]。2月17日推出了专辑《Real+》，其中额外收录了3首新曲。[37][38]',
        '["歌手","维基百科"]', '["真实","娱乐圈"]', 685, 7135, 19, 62, 681, 561, '{"altitude": 100.8, "longitude": 113.280637, "latitude": 23.125178, "country": "中国", "countryID": "CN", "province": "广东省", "provinceID": "440000", "city": "广州市", "cityID": "440100", "county": "天河区", "countyID": "440106", "detailedLocation": "天河公园"}');


# 2024-3-27  20:33-往comment表中插入数据
INSERT
INTO `comment` (`id`, `content_id`, `belong_type`, `from_uid`, `from_nickname`, `from_avatar_url`, `to_uid`, `topic_id`, `content`, `tags`,
                `category`, `browse_num`, `like_num`, `unlike_num`, `comment_num`, `star_num`, `share_num`,
                `address_info`)
VALUES (1, 3, 2101, 10, '清风徐来', 'https://q.qlogo.cn/g?b=qq&nk=123456789&s=640', 5, 2, '这篇帖子内容非常丰富，对我的工作帮助很大，谢谢作者的分享！', '["工作", "分享"]',
        '["职场", "学习"]', 8000, 5000, 200, 1500, 3000, 521,
        '{"altitude": 100.5, "longitude": 120.12345, "latitude": 30.98765, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "330000", "city": "杭州市", "cityID": "330100", "county": "西湖区", "countyID": "330106", "detailedLocation": "华星路123号"}'),
       (2, 12, 2101, 4, '梦回江南', 'https://q.qlogo.cn/g?b=qq&nk=987654321&s=640', 8, 5, '这篇帖子有点过时了，希望作者能够更新一下最新的资讯，让我们得到更多的信息。',
        '["过时", "更新"]', '["资讯", "学习"]', 6000, 3000, 1000, 800, 2000, 486,
        '{"altitude": 90.8, "longitude": 116.395645, "latitude": 39.929986, "country": "中国", "countryID": "CN", "province": "北京市", "provinceID": "110000", "city": "北京市", "cityID": "110100", "county": "东城区", "countyID": "110101", "detailedLocation": "天安门广场"}'),
       (3, 5, 2101, 2, '星空下的约定', 'https://pic.qqans.com/up/2024-6/17189512656033766.jpg', 1, 3, '对这篇帖子的看法颇有分歧，我认为作者的观点有待商榷，需要更多证据支持。', '["分歧", "商榷"]',
        '["观点", "讨论"]', 7000, 4000, 1500, 1000, 2500, 168,
        '{"altitude": 110.2, "longitude": 104.065735, "latitude": 30.659462, "country": "中国", "countryID": "CN", "province": "四川省", "provinceID": "510000", "city": "成都市", "cityID": "510100", "county": "锦江区", "countyID": "510104", "detailedLocation": "天府广场"}'),
       (4, 8, 2101, 6, '漫步云端', 'https://pic.qqans.com/up/2024-6/17189512652642286.jpg', 10, 1, '这篇帖子让我对这个话题有了更深入的了解，作者的观点很有启发性。', '["了解", "观点"]',
        '["话题", "学习"]', 9000, 6000, 250, 2000, 3500, 89,
        '{"altitude": 92.3, "longitude": 121.487899, "latitude": 31.249162, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "人民广场"}'),
       (5, 2, 2101, 10, '听风者', 'https://pic.qqans.com/up/2024-6/17189512652081434.jpg', 7, 4, '这篇帖子写得很好，对我有很大的启发，谢谢作者的分享。', '["启发", "分享"]',
        '["感悟", "学习"]', 8500, 5500, 500, 1800, 3200, 489,
        '{"altitude": 100.8, "longitude": 113.280637, "latitude": 23.125178, "country": "中国", "countryID": "CN", "province": "广东省", "provinceID": "440000", "city": "广州市", "cityID": "440100", "county": "天河区", "countyID": "440106", "detailedLocation": "天河公园"}'),
       (6, 9, 2102, 9, '晨曦微露', 'https://pic.qqans.com/up/2024-6/17189512654377576.jpg', 6, 1, '这篇文章内容深刻，对我有很大的启发，值得一读。', '["深刻", "启发"]', '["思考", "学习"]',
        7500, 4500, 1000, 1500, 2800, 145,
        '{"altitude": 102.5, "longitude": 117.190182, "latitude": 39.125596, "country": "中国", "countryID": "CN", "province": "天津市", "provinceID": "120000", "city": "天津市", "cityID": "120100", "county": "和平区", "countyID": "120101", "detailedLocation": "解放北路"}'),
       (7, 10, 2102, 8, '竹林深处', 'https://pic.qqans.com/up/2024-6/17189512656060133.jpg', 9, 3, '这篇文章有一定的价值，但是有些观点我并不完全认同。', '["价值", "观点"]',
        '["评价", "讨论"]', 7200, 4200, 900, 1200, 2600, 4894,
        '{"altitude": 103.6, "longitude": 106.530635, "latitude": 29.544606, "country": "中国", "countryID": "CN", "province": "重庆市", "provinceID": "500000", "city": "重庆市", "cityID": "500100", "county": "渝中区", "countyID": "500103", "detailedLocation": "解放碑步行街"}'),
       (9, 6, 2102, 8, '梦之城', 'https://pic.qqans.com/up/2024-6/17189512658303541.jpg', 2, 4, '这篇文章内容独特，对我启发很大，希望能够看到更多类似的作品。', '["独特", "启发"]',
        '["观点", "学习"]', 7800, 4700, 800, 1300, 2700, 56,
        '{"altitude": 95.4, "longitude": 118.767413, "latitude": 32.041544, "country": "中国", "countryID": "CN", "province": "江苏省", "provinceID": "320000", "city": "南京市", "cityID": "320100", "county": "鼓楼区", "countyID": "320106", "detailedLocation": "中山路"}'),
       (10, 5, 2102, 1, '风之谷', 'https://pic.qqans.com/up/2024-6/17189512651546949.jpg', 7, 2, '这篇文章给我带来了很多思考，对我的工作和学习都有所启发。', '["思考", "启发"]',
        '["工作", "学习"]', 8200, 4900, 1000, 1600, 2900, 489,
        '{"altitude": 108.7, "longitude": 121.618622, "latitude": 38.91459, "country": "中国", "countryID": "CN", "province": "山东省", "provinceID": "370000", "city": "青岛市", "cityID": "370200", "county": "市南区", "countyID": "370202", "detailedLocation": "中山路"}'),
       (11, 3, 2102, 3, '时光旅人', 'https://pic.qqans.com/up/2024-6/17189512667874525.jpg', 9, 1, '这篇文章让我看到了作者深刻的见解，对我影响很大，值得推荐给更多人。', '["深刻", "见解"]',
        '["推荐", "学习"]', 7300, 4400, 700, 1100, 2500, 694,
        '{"altitude": 98.2, "longitude": 114.066112, "latitude": 22.548515, "country": "中国", "countryID": "CN", "province": "广东省", "provinceID": "440000", "city": "深圳市", "cityID": "440300", "county": "福田区", "countyID": "440304", "detailedLocation": "华强北路"}'),
       (12, 8, 2102, 2, '蓝色幻想', 'https://pic.qqans.com/up/2024-6/17189512669557081.jpg', 8, 3, '这篇文章对我影响很大，作者的观点很新颖，希望能够看到更多相关内容。', '["影响", "新颖"]',
        '["观点", "学习"]', 7700, 4600, 750, 1250, 2600, 161,
        '{"altitude": 100.0, "longitude": 113.264434, "latitude": 23.129162, "country": "中国", "countryID": "CN", "province": "广东省", "provinceID": "440000", "city": "广州市", "cityID": "440100", "county": "天河区", "countyID": "440106", "detailedLocation": "天河城"}'),
       (13, 1, 2103, 4, '樱花落', 'https://pic.qqans.com/up/2024-6/17189512668996229.jpg', 10, 5, '这个评论对我的启发很大，作者的思路很清晰，值得仔细阅读。', '["启发", "思路"]',
        '["阅读", "学习"]', 7900, 4800, 850, 1350, 2800, 49,
        '{"altitude": 93.8, "longitude": 116.405285, "latitude": 39.904989, "country": "中国", "countryID": "CN", "province": "北京市", "provinceID": "110000", "city": "北京市", "cityID": "110100", "county": "东城区", "countyID": "110101", "detailedLocation": "王府井大街"}'),
       (14, 10, 2103, 9, '月下独酌', 'https://pic.qqans.com/up/2024-6/17189512661678785.jpg', 1, 2, '这个评论写得很有深度，对我的研究很有帮助，感谢作者的分享。', '["深度", "帮助"]',
        '["研究", "学习"]', 7400, 4300, 650, 1050, 2400, 684,
        '{"altitude": 110.6, "longitude": 113.547122, "latitude": 22.224979, "country": "中国", "countryID": "CN", "province": "广东省", "provinceID": "440000", "city": "深圳市", "cityID": "440300", "county": "南山区", "countyID": "440305", "detailedLocation": "科技园"}'),
       (15, 17, 2101, 6, '风中追风', 'https://img.duoziwang.com/2021/06/388899110323815522.jpg', 5, 4, '这篇帖子内容很丰富，对我的工作有很大的帮助，谢谢作者的辛勤劳动。', '["丰富", "帮助"]',
        '["工作", "学习"]', 8100, 4700, 700, 1200, 2700, 487,
        '{"altitude": 96.3, "longitude": 120.15507, "latitude": 30.274085, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "330000", "city": "杭州市", "cityID": "330100", "county": "西湖区", "countyID": "330106", "detailedLocation": "西湖"}'),
       (17, 2, 2104, 10, '梦之翼', 'https://img.duoziwang.com/2021/06/538899112195328801.jpg', 3, 1, '这个用户很有启发性，对我的研究产生了很大的帮助，期待作者更多的作品。',
        '["启发性", "帮助"]', '["研究", "学习"]', 7900, 4600, 700, 1250, 2600, 616,
        '{"altitude": 94.5, "longitude": 121.473701, "latitude": 31.230416, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "外滩"}'),
       (18, 4, 2103, 3, '星辰大海', 'https://img.duoziwang.com/2021/06/q101802363837080.jpg', 10, 5, '这个用户的观点独特，对我的思考产生了很大的影响，值得一读。', '["独特", "思考"]',
        '["观点", "学习"]', 7500, 4400, 650, 1100, 2500, 89,
        '{"altitude": 91.7, "longitude": 116.407394, "latitude": 39.904211, "country": "中国", "countryID": "CN", "province": "北京市", "provinceID": "110000", "city": "北京市", "cityID": "110100", "county": "东城区", "countyID": "110101", "detailedLocation": "故宫"}'),
       (19, 11, 2102, 5, '时光隧道', 'https://img.duoziwang.com/2021/06/348899110413416682.jpg', 2, 2, '这篇文章内容很有深度，对我的研究产生了很大的帮助，感谢作者的分享。', '["深度", "帮助"]',
        '["研究", "学习"]', 7700, 4500, 700, 1200, 2700, 56,
        '{"altitude": 97.2, "longitude": 120.15507, "latitude": 30.274085, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "330000", "city": "杭州市", "cityID": "330100", "county": "西湖区", "countyID": "330106", "detailedLocation": "西湖"}'),
       (20, 1, 2102, 6, '风之语', 'https://img.duoziwang.com/2021/06/508899111065019819.jpg', 9, 4, '这篇文章内容很有启发性，对我的学习产生了很大的帮助，期待作者更多的作品。',
        '["启发性", "帮助"]', '["学习", "分享"]', 7900, 4600, 700, 1250, 2600, 188,
        '{"altitude": 94.5, "longitude": 121.473701, "latitude": 31.230416, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "外滩"}'),
       (21, 3, 2101, 7, '梦回吹角', 'https://img.duoziwang.com/2021/06/528899110155213237.jpg', 1, 1, '这个帖子对我的启发很大，作者的思路很清晰，值得仔细阅读。', '["启发", "思路"]',
        '["阅读", "学习"]', 7800, 4700, 800, 1300, 2700, 68,
        '{"altitude": 95.4, "longitude": 118.767413, "latitude": 32.041544, "country": "中国", "countryID": "CN", "province": "江苏省", "provinceID": "320000", "city": "南京市", "cityID": "320100", "county": "鼓楼区", "countyID": "320106", "detailedLocation": "中山路"}'),
       (22, 5, 2101, 8, '晨光熹微', 'https://img.duoziwang.com/2021/06/q10261326075793.jpg', 4, 3, '这篇帖子内容很丰富，对我的工作有很大的帮助，谢谢作者的辛勤劳动。', '["丰富", "帮助"]',
        '["工作", "学习"]', 8100, 4700, 700, 1200, 2700, 589,
        '{"altitude": 96.3, "longitude": 120.15507, "latitude": 30.274085, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "330000", "city": "杭州市", "cityID": "330100", "county": "西湖区", "countyID": "330106", "detailedLocation": "西湖"}'),
       (23, 2, 2103, 9, '蓝色忧郁', 'https://img.duoziwang.com/2021/06/q101801083723120.jpg', 5, 2, '这个评论的内容很有启发性，对我的学习产生了很大的帮助，期待作者更多的作品。',
        '["启发性", "帮助"]', '["学习", "分享"]', 7900, 4600, 700, 1250, 2600, 561,
        '{"altitude": 94.5, "longitude": 121.473701, "latitude": 31.230416, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "外滩"}'),
       (25, 6, 2101, 4, '樱花树下', 'https://img.duoziwang.com/2021/05/10111437216392.jpg', 8, 1, '这篇帖子写得很有深度，对我的研究有很大的帮助，期待作者更多的作品。', '["深度", "帮助"]',
        '["研究", "学习"]', 7700, 4500, 700, 1200, 2700, 325,
        '{"altitude": 97.2, "longitude": 120.15507, "latitude": 30.274085, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "330000", "city": "杭州市", "cityID": "330100", "county": "西湖区", "countyID": "330106", "detailedLocation": "西湖"}'),
       (26, 9, 2102, 2, '月夜风高', 'https://img.duoziwang.com/2021/06/q101800293516674.jpg', 5, 4, '这篇文章内容很有启发性，对我的学习产生了很大的帮助，谢谢作者的分享。', '["启发性", "帮助"]',
        '["学习", "分享"]', 7900, 4600, 700, 1250, 2600, 468,
        '{"altitude": 94.5, "longitude": 121.473701, "latitude": 31.230416, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "外滩"}'),
       (27, 4, 2101, 7, '风华正茂', 'https://img.duoziwang.com/2021/05/10111437214711.jpg', 3, 5, '这篇帖子内容很丰富，对我的工作有很大的帮助，感谢作者的辛勤劳动。', '["丰富", "帮助"]',
        '["工作", "学习"]', 8100, 4700, 700, 1200, 2700, 832,
        '{"altitude": 96.3, "longitude": 120.15507, "latitude": 30.274085, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "330000", "city": "杭州市", "cityID": "330100", "county": "西湖区", "countyID": "330106", "detailedLocation": "西湖"}'),
       (28, 1, 2102, 5, '梦幻泡影', 'https://img.duoziwang.com/2021/04/08260845111360.jpg', 9, 2, '这篇文章对我的启发很大，作者的思路很清晰，值得仔细阅读。', '["启发", "思路"]',
        '["阅读", "学习"]', 7800, 4700, 800, 1300, 2700, 487,
        '{"altitude": 95.4, "longitude": 118.767413, "latitude": 32.041544, "country": "中国", "countryID": "CN", "province": "江苏省", "provinceID": "320000", "city": "南京市", "cityID": "320100", "county": "鼓楼区", "countyID": "320106", "detailedLocation": "中山路"}'),
       (29, 2, 2101, 8, '星辰之海', 'https://img.duoziwang.com/2021/05/10111437207367.jpg', 1, 1, '这篇帖子内容很有启发性，对我的学习产生了很大的帮助，期待作者更多的作品。',
        '["启发性", "帮助"]', '["学习", "分享"]', 7900, 4600, 700, 1250, 2600, 298,
        '{"altitude": 94.5, "longitude": 121.473701, "latitude": 31.230416, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "外滩"}'),
       (30, 8, 2101, 9, '时光之轮', 'https://img.duoziwang.com/2021/06/28899110220214098.jpg', 2, 5, '这篇帖子内容很丰富，对我的工作有很大的帮助，谢谢作者的辛勤劳动。', '["丰富", "帮助"]',
        '["工作", "学习"]', 8100, 4700, 700, 1200, 2700, 89,
        '{"altitude": 96.3, "longitude": 120.15507, "latitude": 30.274085, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "330000", "city": "杭州市", "cityID": "330100", "county": "西湖区", "countyID": "330106", "detailedLocation": "西湖"}'),
       (31, 6, 2103, 6, '风中奇缘', 'https://img.duoziwang.com/2021/06/528899111095220219.jpg', 10, 1, '这个评论写得很有深度，对我的研究有很大的帮助，期待作者更多的作品。', '["深度", "帮助"]',
        '["研究", "学习"]', 7700, 4500, 700, 1200, 2700, 647,
        '{"altitude": 97.2, "longitude": 120.15507, "latitude": 30.274085, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "330000", "city": "杭州市", "cityID": "330100", "county": "西湖区", "countyID": "330106", "detailedLocation": "西湖"}'),
       (33, 5, 2101, 10, '梦之城', 'https://img.duoziwang.com/2021/06/10250835810323.jpg', 8, 2, '这篇帖子内容很有启发性，对我的学习产生了很大的帮助，谢谢作者的分享。',
        '["启发性", "帮助"]', '["学习", "分享"]', 7900, 4600, 700, 1250, 2600, 489,
        '{"altitude": 94.5, "longitude": 121.473701, "latitude": 31.230416, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "外滩"}'),
       (34, 8, 2102, 9, '晨曦初照', 'https://m.qqkw.com/d/tx/titlepic/83e38aedcc0cd2c1031e62c9e4f46beb.jpg', 6, 5, '这篇文章内容很丰富，对我的工作有很大的帮助，感谢作者的辛勤劳动。', '["丰富", "帮助"]',
        '["工作", "学习"]', 8100, 4700, 700, 1200, 2700, 481,
        '{"altitude": 96.3, "longitude": 120.15507, "latitude": 30.274085, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "330000", "city": "杭州市", "cityID": "330100", "county": "西湖区", "countyID": "330106", "detailedLocation": "西湖"}'),
       (35, 2, 2102, 5, '蓝色天际', 'https://m.qqkw.com/d/tx/titlepic/91130ac08cb5d199eb427d036d21e134.jpg', 3, 1, '这篇文章对我的启发很大，作者的思路很清晰，值得仔细阅读。', '["启发", "思路"]',
        '["阅读", "学习"]', 7800, 4700, 800, 1300, 2700, 587,
        '{"altitude": 95.4, "longitude": 118.767413, "latitude": 32.041544, "country": "中国", "countryID": "CN", "province": "江苏省", "provinceID": "320000", "city": "南京市", "cityID": "320100", "county": "鼓楼区", "countyID": "320106", "detailedLocation": "中山路"}'),
       (36, 3, 2101, 2, '樱花飞舞', 'https://m.qqkw.com/d/tx/titlepic/bc872e8b7d9176a77692950b6a28ff79.jpg', 1, 1, '这篇帖子内容很有启发性，对我的学习产生了很大的帮助，期待作者更多的作品。',
        '["启发性", "帮助"]', '["学习", "分享"]', 7900, 4600, 700, 1250, 2600, 218,
        '{"altitude": 94.5, "longitude": 121.473701, "latitude": 31.230416, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "外滩"}'),
       (37, 9, 2103, 7, '月下老人', 'https://m.qqkw.com/d/tx/titlepic/6724e5d6699b143a2b31958db4689659.jpg', 8, 4, '这个评论写得很有深度，对我的研究有很大的帮助，期待作者更多的作品。', '["深度", "帮助"]',
        '["研究", "学习"]', 7700, 4500, 700, 1200, 2700, 187,
        '{"altitude": 97.2, "longitude": 120.15507, "latitude": 30.274085, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "330000", "city": "杭州市", "cityID": "330100", "county": "西湖区", "countyID": "330106", "detailedLocation": "西湖"}'),
       (38, 19, 2101, 6, '风中追风', 'https://m.qqkw.com/d/tx/titlepic/bd0c259252f3e1723c66060b1715bef2.jpg', 2, 2, '这篇帖子内容很有启发性，对我的学习产生了很大的帮助，期待作者更多的作品。',
        '["启发性", "帮助"]', '["学习", "分享"]', 7900, 4600, 700, 1250, 2600, 5898,
        '{"altitude": 94.5, "longitude": 121.473701, "latitude": 31.230416, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "外滩"}'),
       (39, 5, 2103, 4, '梦幻岛', 'https://m.qqkw.com/d/tx/titlepic/4d9baa3935a29061af611d1eec897e78.jpg', 10, 2, '这个评论内容很有启发性，对我的学习产生了很大的帮助，谢谢作者的分享。',
        '["启发性", "帮助"]', '["学习", "分享"]', 7900, 4600, 700, 1250, 2600, 751,
        '{"altitude": 94.5, "longitude": 121.473701, "latitude": 31.230416, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "外滩"}'),
       (41, 2, 2104, 3, '时光旅行者', 'https://m.qqkw.com/d/tx/titlepic/c6ae6c034eb5e8c9b62bf0c93d409d0a.jpg', 5, 1, '这个用户对我的启发很大，作者的思路很清晰，值得仔细阅读。', '["启发", "思路"]',
        '["阅读", "学习"]', 7800, 4700, 800, 1300, 2700, 894,
        '{"altitude": 95.4, "longitude": 118.767413, "latitude": 32.041544, "country": "中国", "countryID": "CN", "province": "江苏省", "provinceID": "320000", "city": "南京市", "cityID": "320100", "county": "鼓楼区", "countyID": "320106", "detailedLocation": "中山路"}'),
       (42, 9, 2102, 1, '风之低语', 'https://m.qqkw.com/d/tx/titlepic/2c973d887d1bd14b54221ab4fffb1fef.jpg', 8, 4, '这篇文章内容很有启发性，对我的学习产生了很大的帮助，感谢作者的分享。', '["启发性", "帮助"]',
        '["学习", "分享"]', 7900, 4600, 700, 1250, 2600, 859,
        '{"altitude": 94.5, "longitude": 121.473701, "latitude": 31.230416, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "外滩"}'),
       (43, 4, 2101, 7, '梦之城', 'https://m.qqkw.com/d/tx/titlepic/0edc8af91bdac9158a9786ec90242e6a.jpg', 6, 5, '这篇帖子内容很丰富，对我的工作有很大的帮助，期待作者更多的作品。', '["丰富", "帮助"]',
        '["工作", "学习"]', 8100, 4700, 700, 1200, 2700, 56,
        '{"altitude": 96.3, "longitude": 120.15507, "latitude": 30.274085, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "330000", "city": "杭州市", "cityID": "330100", "county": "西湖区", "countyID": "330106", "detailedLocation": "西湖"}'),
       (44, 3, 2101, 2, '晨光微露', 'https://m.qqkw.com/d/tx/titlepic/4f0e03223f439947e999ce7dd42883f0.jpg', 9, 1, '这篇帖子内容很有启发性，对我的学习产生了很大的帮助，期待作者更多的作品。',
        '["启发性", "帮助"]', '["学习", "分享"]', 7900, 4600, 700, 1250, 2600, 895,
        '{"altitude": 94.5, "longitude": 121.473701, "latitude": 31.230416, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "外滩"}'),
       (45, 24, 2102, 6, '紫夜轻歌', 'https://m.qqkw.com/d/tx/titlepic/7e2c3bfb57871399a88ce5f9f3522e03.jpg', 5, 2, '这篇文章内容很有启发性，对我的学习产生了很大的帮助，谢谢作者的分享。',
        '["启发性", "帮助"]',
        '["学习", "分享"]', 7900, 4600, 700, 1250, 2600, 298,
        '{"altitude": 94.5, "longitude": 121.473701, "latitude": 31.230416, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "外滩"}'),
       (46, 5, 2101, 4, '碧海潮生', 'https://m.qqkw.com/d/tx/titlepic/58c0255af491aa0c2a67a01df7da5c93.jpg', 8, 2, '这篇帖子内容很有启发性，对我的学习产生了很大的帮助，期待作者更多的作品。',
        '["启发性", "帮助"]', '["学习", "分享"]', 7900, 4600, 700, 1250, 2600, 667,
        '{"altitude": 94.5, "longitude": 121.473701, "latitude": 31.230416, "country": "中国", "countryID": "CN", "province": "上海市", "provinceID": "310000", "city": "上海市", "cityID": "310100", "county": "黄浦区", "countyID": "310101", "detailedLocation": "外滩"}'),
       (47, 3, 2103, 8, '雪域冰心', 'https://m.qqkw.com/d/tx/titlepic/70a6e75ce1a506f2003bc06dfcc1240c.jpg', 7, 5, '这个评论的内容很丰富，对我的工作有很大的帮助，感谢作者的辛勤劳动。', '["丰富", "帮助"]',
        '["工作", "学习"]', 8100, 4700, 700, 1200, 2700, 132,
        '{"altitude": 96.3, "longitude": 120.15507, "latitude": 30.274085, "country": "中国", "countryID": "CN", "province": "浙江省", "provinceID": "330000", "city": "杭州市", "cityID": "330100", "county": "西湖区", "countyID": "330106", "detailedLocation": "西湖"}');


# 2024-5-20  22:25-往commodity表中插入数据
INSERT INTO `commodity` (`id`,
                         `user_id`,
                         `name`,
                         `description`,
                         `tags`,
                         `category`,
                         `browse_num`,
                         `like_num`,
                         `unlike_num`,
                         `comment_num`,
                         `star_num`,
                         `share_num`,
                         `price`,
                         `status`,
                         `address_info`,
                         `create_time`,
                         `update_time`)
VALUES (1, 8, 'iPhone 13 Pro Max (256GB)', 'Apple新款旗舰手机，A15仿生芯片，超强性能，后置三摄，拍照更清晰，续航更持久。',
        '["iPhone", "手机", "苹果", "电子产品"]', '["手机", "智能手机", "苹果手机"]', 100000,
        2000, 500, 1000, 5000, 289, 8999.00, 1201,
        '{"province": "上海市", "city": "浦东新区", "detailedLocation": "浦东大道1号"}', '2024-05-20 10:00:00',
        '2024-05-20 10:00:00'),
       (2, 5, 'Redmi K50 Pro (12GB+256GB)', '骁龙8 Gen 1旗舰芯片，120Hz高刷屏，67W快充，游戏性能强悍。',
        '["手机", "智能手机", "小米"]', '["手机", "智能手机", "游戏手机"]', 50000, 1500, 300, 500, 1000, 589, 3299.00,
        1201, '{"province": "北京市", "city": "朝阳区", "detailedLocation": "国贸三里屯"}',
        '2024-05-20 10:00:00', '2024-05-20 10:00:00'),
       (3, 9, 'Tesla Model 3 (Long Range)', '纯电动轿车，续航里程可达663km，百公里加速3.1秒，科技感十足。',
        '["电动车", "汽车", "特斯拉", "新能源"]', '["汽车", "轿车", "新能源汽车"]', 10000, 500, 200, 100, 50, 415, 599900.00,
        1201, '{"province": "上海市", "city": "浦东新区", "detailedLocation": "浦东大道1号"}',
        '2024-05-20 10:00:00',
        '2024-05-20 10:00:00'),
       (4, 7, 'BYD Han EV (创世版)', '纯电动轿车，续航里程可达712km，百公里加速3.7秒，空间宽敞舒适。',
        '["电动车", "汽车", "比亚迪", "新能源"]', '["汽车", "轿车", "新能源汽车"]', 5000, 300, 150, 50, 20, 8948, 289800.00,
        1201, '{"province": "北京市", "city": "朝阳区", "detailedLocation": "国贸三里屯"}', '2024-05-20 10:00:00',
        '2024-05-20 10:00:00'),
       (5, 5, 'iPad Air 5 (2022款)', '学习娱乐两不误，M1芯片强劲性能，Liquid Retina显示屏，支持Apple Pencil。',
        '["学习用品", "电子产品", "平板电脑"]', '["电子产品", "平板电脑", "学习平板"]', 5000, 2000, 1000, 500, 200, 29,
        5999.00, 1201, '{"province": "上海市", "city": "浦东新区", "detailedLocation": "浦东大道1号"}',
        '2024-05-20 10:00:00', '2024-05-20 10:00:00'),
       (6, 2, 'MacBook Pro 14-inch (M1 Pro, 16GB RAM, 1TB SSD)',
        'Apple新款高端笔记本，M1 Pro芯片强劲性能，14.2英寸Liquid Retina XDR显示屏，续航长达17小时。',
        '["笔记本", "电脑", "苹果", "电子产品"]', '["电脑", "笔记本", "Macbook"]', 20000, 1000, 500, 200, 100, 598, 24999.00,
        1201, '{"province": "上海市", "city": "浦东新区", "detailedLocation": "浦东大道1号"}',
        '2024-05-20 10:00:00', '2024-05-20 10:00:00'),
       (7, 9, 'ThinkPad X1 Carbon Gen 10 (14英寸)',
        '联想ThinkPad经典商务笔记本，轻薄便携，性能强悍，续航长达20小时。', '["笔记本", "电脑", "联想", "商务"]',
        '["电脑", "笔记本", "商务本"]', 15000, 800, 400, 150, 50, 478, 13999.00,
        1201, '{"province": "北京市", "city": "朝阳区", "detailedLocation": "国贸三里屯"}',
        '2024-05-20 10:00:00', '2024-05-20 10:00:00'),
       (8, 1, 'ROG Zephyrus G14 (2022款)',
        'ROG玩家国度游戏本，AMD锐龙9 6900HS处理器，NVIDIA GeForce RTX 3060显卡，高性能游戏无压力。',
        '["笔记本", "电脑", "华硕", "游戏"]', '["电脑", "笔记本", "游戏本"]', 10000, 500, 300, 100, 30, 28, 11999.00,
        1201, '{"province": "上海市", "city": "浦东新区", "detailedLocation": "浦东大道1号"}',
        '2024-05-20 10:00:00', '2024-05-20 10:00:00'),
       (9, 6, 'iPad Air 5 (2022款)', '学习娱乐两不误，M1芯片强劲性能，Liquid Retina显示屏，支持Apple Pencil。',
        '["学习用品", "电子产品", "平板电脑"]', '["电子产品", "平板电脑", "学习平板"]', 5000, 2000, 1000, 500, 200, 89,
        5999.00, 1201, '{"province": "上海市", "city": "浦东新区", "detailedLocation": "浦东大道1号"}',
        '2024-05-20 10:00:00', '2024-05-20 10:00:00'),
       (10, 4, '优衣库 无印良品 简约T恤', '纯棉材质，舒适透气，百搭百配。', '["衣服", "男装", "女装", "T恤"]',
        '["衣服", "T恤", "休闲"]', 1000, 10000, 500, 200, 100, 598, 99.00, 1201,
        '{"province": "上海市", "city": "浦东新区", "detailedLocation": "浦东大道1号"}',
        '2024-05-20 10:00:00', '2024-05-20 10:00:00'),
       (11, 3, '耐克 运动休闲短裤', '速干透气，舒适运动，百搭潮流。', '["衣服", "男装", "女装", "短裤"]',
        '["衣服", "短裤", "运动"]', 500, 5000, 300, 100, 50, 49, 199.00, 1201,
        '{"province": "北京市", "city": "朝阳区", "detailedLocation": "国贸三里屯"}',
        '2024-05-20 10:00:00', '2024-05-20 10:00:00'),
       (12, 10, '阿迪达斯 时尚运动风衣', '防风防水，保暖透气，时尚百搭。', '["衣服", "男装", "女装", "风衣"]',
        '["衣服", "风衣", "休闲"]', 300, 2000, 200, 50, 20, 589, 399.00, 1201,
        '{"province": "上海市", "city": "浦东新区", "detailedLocation": "浦东大道1号"}',
        '2024-05-20 10:00:00', '2024-05-20 10:00:00'),
       (13, 8, '晨光简约中性笔', '书写流畅，出墨均匀，性价比高。', '["文具", "学习用品", "钢笔"]',
        '["文具", "钢笔", "中性笔"]', 10000, 5000, 300, 100, 50, 784, 2.99, 1201,
        '{"province": "上海市", "city": "浦东新区", "detailedLocation": "浦东大道1号"}',
        '2024-05-21 10:00:00', '2024-05-21 10:00:00'),
       (14, 9, '派通小学生书包', '轻便耐用，减负护脊，多种款式可选。', '["文具", "学习用品", "书包"]',
        '["文具", "书包", "小学生书包"]', 5000, 3000, 200, 50, 20, 71, 199.00, 1201,
        '{"province": "北京市", "city": "朝阳区", "detailedLocation": "国贸三里屯"}', '2024-05-21 10:00:00',
        '2024-05-21 10:00:00'),
       (15, 1, '真彩水彩笔套装', '36色水彩笔，颜色丰富，易上色，适合绘画初学者。', '["文具", "学习用品", "彩笔"]',
        '["文具", "彩笔", "水彩笔"]', 3000, 2000, 100, 30, 10, 157, 39.90, 1201,
        '{"province": "上海市", "city": "浦东新区", "detailedLocation": "浦东大道1号"}',
        '2024-05-21 10:00:00', '2024-05-21 10:00:00'),
       (16, 5, '卡地亚 LOVE 系列戒指', '经典钉螺设计，简约时尚，永不过时。', '["珠宝首饰", "戒指", "卡地亚"]',
        '["珠宝首饰", "戒指", "情侣戒指"]', 2000, 1000, 50, 20, 10, 489, 1999.00, 1201,
        '{"province": "上海市", "city": "浦东新区", "detailedLocation": "浦东大道1号"}', '2024-05-21 10:00:00',
        '2024-05-21 10:00:00'),
       (17, 3, '蒂芙尼 T Smile 系列项链', '简约T形设计，优雅百搭，适合日常佩戴。', '["珠宝首饰", "项链", "蒂芙尼"]',
        '["珠宝首饰", "项链", "女士项链"]', 3000, 2000, 100, 30, 10, 89, 2999.00, 1201,
        '{"province": "北京市", "city": "朝阳区", "detailedLocation": "国贸三里屯"}', '2024-05-21 10:00:00',
        '2024-05-21 10:00:00'),
       (18, 7, '梵克雅宝 四叶草系列耳钉', '幸运四叶草图案，精致小巧，点亮你的耳畔。',
        '["珠宝首饰", "耳钉", "梵克雅宝"]', '["珠宝首饰", "耳钉", "女士耳钉"]', 1500, 1000, 50, 20, 10, 781, 999.00, 1201,
        '{"province": "上海市", "city": "浦东新区", "detailedLocation": "浦东大道1号"}',
        '2024-05-21 10:00:00', '2024-05-21 10:00:00'),
       (19, 4, '故宫博物院文创 故宫猫书签套装', '故宫猫书签套装，萌趣可爱，实用又美观。',
        '["文创周边", "书签", "故宫博物院"]', '["文创周边", "书签", "卡通书签"]', 1000, 500, 20, 10, 5, 842, 19.90, 1201,
        '{"province": "上海市", "city": "浦东新区", "detailedLocation": "浦东大道1号"}',
        '2024-05-21 10:00:00', '2024-05-21 10:00:00'),
       (20, 7, '北京冬奥会纪念徽章', '北京冬奥会纪念徽章，珍藏冬奥记忆。', '["文创周边", "徽章", "北京冬奥会"]',
        '["文创周边", "徽章", "运动徽章"]', 500, 300, 100, 20, 10, 172, 9.90, 1201,
        '{"province": "北京市", "city": "朝阳区", "detailedLocation": "国贸三里屯"}',
        '2024-05-21 10:00:00', '2024-05-21 10:00:00'),
       (21, 10, '周杰伦 新专辑 最伟大的作品 CD', '周杰伦新专辑《最伟大的作品》，致敬经典，重温感动。',
        '["音乐", "专辑", "周杰伦"]', '["音乐", "专辑", "华语流行"]', 2000, 1000, 50, 20, 10, 55, 69.90, 1201,
        '{"province": "上海市", "city": "浦东新区", "detailedLocation": "浦东大道1号"}',
        '2024-05-21 10:00:00', '2024-05-21 10:00:00');


# 2024-5-20  22:26-往commodity_details表中插入数据
INSERT
INTO `commodity_details`(`id`,
                         `commodity_id`,
                         `stock_quantity`,
                         `brand`,
                         `weight`,
                         `size`,
                         `color`,
                         `material`,
                         `origin`,
                         `image_url`,
                         `barcode`)
values (101, 1, 100, 'Apple', 0.226, '160.8 x 78.1 x 7.65 mm', '深空灰色', '铝合金', '中国',
        'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg', '6943123456789')
        ,
       (102, 2, 200, 'Redmi', 0.202, '163.2 x 76.8 x 8.5 mm', '天ui蓝', '玻璃', '中国',
        'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg', '6943123456780'),
       (103, 3, 20, 'Tesla', 1848.00, '4694 x 1850 x 1440 mm', '珍珠白', '金属', '美国',
        'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg', '69431234567890'),
       (104, 4, 15, 'BYD', 2180.00, '4995 x 1910 x 1500 mm', '曜石黑', '金属', '中国',
        'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg', '69431234567810'),
       (105, 5, 100, 'Apple', 0.60, '257 x 174 x 6.1 mm', '星光白', '铝合金', '美国',
        'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg', '69431234567893'),
       (106, 6, 50, 'Apple', 1.60, '360.4 x 240.5 x 16.3 mm', '深空灰色', '铝合金', '美国',
        'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg', '69431234567891'),
       (107, 7, 30, 'Lenovo', 1.13, '328.1 x 223.5 x 14.2 mm', '黑色', '碳纤维', '中国',
        'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg', '69431234567820'),
       (108, 8, 20, 'ASUS', 1.60, '324 x 226 x 19.9 mm', '月光白', '铝合金', '中国',
        'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg', '69431234567830'),
       (109, 9, 100, 'Apple', 0.60, '257 x 174 x 6.1 mm', '星光白', '铝合金', '美国',
        'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg', '69431234567893'),
       (110, 10, 5000, '优衣库', 0.20, 'M', '白色', '棉', '中国',
        'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg', '69431234567892'),
       (111, 11, 2000, 'Nike', 0.30, 'L', '黑色', '聚酯纤维',
        '美国', 'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg',
        '69431234567840'),
       (112, 12, 1000, 'Adidas', 0.50, 'XL', '灰色', '尼龙', '德国',
        'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg', '69431234567850'),
       (113, 13, 10000, '晨光', 0.01, '标准', '黑色', '塑料',
        '中国', 'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg',
        '69431234567893'),
       (114, 14, 5000, '派通', 0.80, '30 x 28 x 15 cm',
        '蓝色', '尼龙', '中国', 'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg',
        '69431234567841'),
       (115, 15, 2000, '真彩', 0.25, '套装', '多色', '塑料',
        '中国', 'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg',
        '69431234567851'),
       (116, 16, 500, '卡地亚', 0.01, '无', '玫瑰金',
        '18K金', '法国', 'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg',
        '69431234567812'),
       (117, 17, 1000, '蒂芙尼', 0.02, '60cm', '玫瑰金', '18K金', '美国',
        'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg',
        '69431234567821'),
       (118, 18, 500, '梵克雅宝', 0.01, '无', '白色', '珍珠', '法国',
        'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg', '69431234567831'),
       (119, 19, 1000, '故宫博物院文创', 0.05, '10cm x 5cm', '多色', '纸', '中国',
        'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg', '69431234567861'),
       (120, 20, 500, '北京冬奥会', 0.02, '5cm', '多色', '金属',
        '中国', 'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg',
        '69431234567871'),
       (121, 21, 1000, '杰威尔音乐', 0.10, '12cm x 12cm x 1cm', '无', '塑料', '中国',
        'https://img.alicdn.com/tfs/img01.alicdn.com/imcp/v1/O1CN01/21z0Q7y1174nY55745v9.jpg', '69431234567881');


# 2024-10-20  1:40-往banner表中插入数据
INSERT INTO `banner`(`id`, `uid`, `title`, `img_url`)
VALUES (1, 5, '能不能给我一首歌的时间', 'https://d35kvm5iuwjt9t.cloudfront.net/dbimages/sfx293837.jpg'),
       (2, 8, '鞠婧祎 么么叽', 'https://img.win3000.com/m00/e1/57/0272abb760114f889b3e79c3042ace54.jpg'),
       (3, 1, '李知恩I Love You', 'https://cdn.i-scmp.com/sites/default/files/d8/images/canvas/2023/09/20/7bbf6337-8997-46f2-9616-ca6c4da9a112_70fdfdcb.jpg'),
       (4, 2, 'StarShip第五次测试取得圆满成功', 'https://www.ascend.events/wp-content/uploads/2024/03/SpaceX-Launch-14Mar2024-banner.jpg');


# 2024-10-21  18:16-往subscriber_ship表中插入数据
INSERT INTO `subscriber_ship`(`id`, `uid`, `follower_id`, `followee_id`)
VALUES (1, 1, 1, 2),
       (2, 2, 2, 3),
       (3, 3, 3, 4),
       (4, 4, 4, 5),
       (5, 5, 5, 6),
       (6, 6, 6, 7),
       (7, 7, 7, 8),
       (8, 8, 8, 9),
       (9, 9, 9, 10),
       (10, 10, 10, 1),
       (11, 1, 1, 3),
       (12, 2, 2, 4),
       (13, 3, 3, 5),
       (14, 4, 4, 6),
       (15, 5, 5, 7),
       (16, 6, 6, 8),
       (17, 7, 7, 9),
       (18, 8, 8, 10),
       (19, 9, 9, 1),
       (20, 10, 10, 2),
       (21, 1, 1, 4),
       (22, 2, 2, 5),
       (23, 3, 3, 6),
       (24, 4, 4, 7),
       (25, 5, 5, 8),
       (26, 6, 6, 9),
       (27, 7, 7, 10),
       (28, 8, 8, 1),
       (29, 9, 9, 2),
       (30, 10, 10, 3);