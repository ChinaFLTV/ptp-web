DROP DATABASE IF EXISTS `ptp_web`;
CREATE DATABASE `ptp_web`;
USE `ptp_web`;


CREATE TABLE IF NOT EXISTS `asset`
(

    `id`          BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '用户财产ID',
    `balance`     DOUBLE UNSIGNED   DEFAULT 0 COMMENT '用户当前账户余额',
    `accounts`    CHAR(255)         DEFAULT NULL COMMENT '用户绑定的银行卡',
    `authorities` CHAR(255)         DEFAULT 'drawback,withdraw,view,update_password' COMMENT '当前账户所允许的操作',
    `credit`      DOUBLE UNSIGNED   DEFAULT 100 COMMENT '当前账户的信誉积分',
    `status`      SMALLINT UNSIGNED DEFAULT 300 COMMENT '当前账户状态',
    `create_time` TIMESTAMP         DEFAULT CURRENT_TIMESTAMP COMMENT '资产创建时间',
    `update_time` TIMESTAMP         DEFAULT CURRENT_TIMESTAMP COMMENT '(最后)修改时间' ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`  INT UNSIGNED      DEFAULT 0 COMMENT '资产是否已被删除'

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '用户资产';


CREATE TABLE `role`
(
    `id`          BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '用户角色ID',
    `code`        INT UNSIGNED NOT NULL COMMENT '角色对应的序号',
    `name`        CHAR(255)    NOT NULL COMMENT '角色对应的名称',
    `authorities` VARCHAR(1024)         DEFAULT 'content_list,content_add,content_remove,content_update' COMMENT '当前角色所允许的操作',
    `prohibition` CHAR(255)             DEFAULT 'user_add,user_remove,role_add,role_remove,role_list,role_update' COMMENT '当前角色所禁止的操作',
    `create_time` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '角色创建时间',
    `update_time` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '(最后)修改时间' ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`  INT UNSIGNED          DEFAULT 0 COMMENT '角色是否已被删除',

    UNIQUE KEY ix_code (code),
    UNIQUE KEY ix_name (name)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '角色';


# 2024-3-22  19:33-创建user表
CREATE TABLE IF NOT EXISTS `user`
(
    `id`              BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID', -- 主键，自增长
    `account`         VARCHAR(255) NOT NULL COMMENT '用户账号',                    -- 账号生成
    `password`        VARCHAR(255) NOT NULL COMMENT '用户密码(加密)',              -- 密码加密
    `phone`           VARCHAR(16)           DEFAULT NULL COMMENT '用户绑定的手机号',
    `email`           VARCHAR(255)          DEFAULT NULL COMMENT '用户邮箱',
    `nickname`        VARCHAR(255)          DEFAULT NULL COMMENT '用户昵称',       -- 随机生成昵称
    `realname`        VARCHAR(255)          DEFAULT NULL COMMENT '用户真实姓名(管理员必需)',
    `gender`          TINYINT UNSIGNED      DEFAULT 2 COMMENT '用户性别',
    `idiograph`       VARCHAR(255)          DEFAULT '这个家伙很懒，什么都没写> - <' COMMENT '用户的个性签名',
    `avatar`          TEXT                  DEFAULT NULL COMMENT '用户头像(JSON)', -- 用户个人资料背景图片(JSON)
    `background`      TEXT                  DEFAULT NULL COMMENT '用户个人资料背景图片(JSON)',
    `like_num`        INT UNSIGNED          DEFAULT 0 COMMENT '用户被点赞数量',
    `user_rank`       DOUBLE UNSIGNED       DEFAULT 0 COMMENT '用户等级',
    `birth_date`      TIMESTAMP             DEFAULT NULL COMMENT '用户出生年月',
    `address_info_id` BIGINT UNSIGNED       DEFAULT NULL COMMENT '用户地址信息ID',
    `bind_accounts`   VARCHAR(255)          DEFAULT NULL COMMENT '用户绑定的其他账号',
    `credit`          DOUBLE UNSIGNED       DEFAULT 100 COMMENT '用户信誉积分',
    `status`          SMALLINT UNSIGNED     DEFAULT 300 COMMENT '用户当前状态',
    `meta`            TEXT                  DEFAULT NULL COMMENT '用户其他数据配置(JSON)',
    `role_id`         BIGINT UNSIGNED       DEFAULT NULL COMMENT '用户角色ID',
    `asset_id`        BIGINT UNSIGNED       DEFAULT NULL COMMENT '用户资产ID',
    `create_time`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户注册时间',
    `update_time`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户资料修改时间' ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`      INT UNSIGNED          DEFAULT 0 COMMENT '用户是否已被删除',

    FOREIGN KEY (role_id) REFERENCES role (id),
    FOREIGN KEY (asset_id) REFERENCES asset (id),

    UNIQUE KEY un_idx_account (account),
    UNIQUE KEY un_idx_nickname (nickname),
    INDEX idx_realname (realname),
    UNIQUE KEY un_idx_phone (phone),
    UNIQUE KEY un_idx_email (email)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '用户';


# 2024-3-22  19:16-创建dialogue表
CREATE TABLE IF NOT EXISTS `dialogue`
(
    `id`           BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '对话ID', -- 主键，自增长
    `uid`          BIGINT UNSIGNED NOT NULL COMMENT '发布者ID',                 -- 不能为空
    `title`        VARCHAR(128)    NOT NULL COMMENT '标题',
    `content`      TEXT            NOT NULL COMMENT '内容',
    `accessary`    TEXT                     DEFAULT NULL COMMENT '附加的其他类型的媒体内容(JSON格式)',
    `tags`         VARCHAR(255)             DEFAULT NULL COMMENT '标签',
    `category`     VARCHAR(255)             DEFAULT NULL COMMENT '分类',
    `browse_num`   INT UNSIGNED             DEFAULT 0 COMMENT '浏览量',
    `like_num`     INT UNSIGNED             DEFAULT 0 COMMENT '点赞量',
    `unlike_num`   INT UNSIGNED             DEFAULT 0 COMMENT '倒赞量',
    `comment_num`  INT UNSIGNED             DEFAULT 0 COMMENT '评论量',
    `star_num`     INT UNSIGNED             DEFAULT 0 COMMENT '收藏量',
    `address_info` TEXT                     DEFAULT NULL COMMENT '发布时用户所在的地址信息',
    `status`       SMALLINT UNSIGNED        DEFAULT 100 COMMENT '实例状态',
    `meta`         TEXT                     DEFAULT NULL COMMENT '其他数据配置(JSON)',
    `create_time`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '内容创建时间',
    `update_time`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '(最后)更新时间' ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`   INT UNSIGNED             DEFAULT 0 COMMENT '当前实体是否已被逻辑删除',

    FOREIGN KEY (uid) REFERENCES user (id)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '对话';


# 2024-3-24  16:18-创建announcement表
CREATE TABLE IF NOT EXISTS `announcement`
(
    `id`           BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '公告ID', -- 主键，自增长
    `uid`          BIGINT UNSIGNED NOT NULL COMMENT '发布者ID',                 -- 不能为空
    `title`        VARCHAR(128)    NOT NULL COMMENT '标题',
    `content`      TEXT            NOT NULL COMMENT '内容',
    `accessary`    TEXT                     DEFAULT NULL COMMENT '附加的其他类型的媒体内容(JSON格式)',
    `tags`         VARCHAR(255)             DEFAULT NULL COMMENT '标签',
    `category`     VARCHAR(255)             DEFAULT NULL COMMENT '分类',
    `browse_num`   INT UNSIGNED             DEFAULT 0 COMMENT '浏览量',
    `like_num`     INT UNSIGNED             DEFAULT 0 COMMENT '点赞量',
    `unlike_num`   INT UNSIGNED             DEFAULT 0 COMMENT '倒赞量',
    `comment_num`  INT UNSIGNED             DEFAULT 0 COMMENT '评论量',
    `star_num`     INT UNSIGNED             DEFAULT 0 COMMENT '收藏量',
    `address_info` TEXT                     DEFAULT NULL COMMENT '发布时用户所在的地址信息',
    `status`       SMALLINT UNSIGNED        DEFAULT 100 COMMENT '实例状态',
    `meta`         TEXT                     DEFAULT NULL COMMENT '其他数据配置(JSON)',
    `create_time`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '内容创建时间',
    `update_time`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '(最后)更新时间' ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`   INT UNSIGNED             DEFAULT 0 COMMENT '当前实体是否已被逻辑删除',

    FOREIGN KEY (uid) REFERENCES user (id)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '公告';


# 2024-3-24  16:18-创建passage表
CREATE TABLE IF NOT EXISTS `passage`
(
    `id`           BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '文章ID', -- 主键，自增长
    `uid`          BIGINT UNSIGNED NOT NULL COMMENT '发布者ID',                 -- 不能为空
    `title`        VARCHAR(128)    NOT NULL COMMENT '标题',
    `introduction` CHAR(50)        NOT NULL COMMENT '内容介绍',
    `content`      TEXT            NOT NULL COMMENT '内容',
    `accessary`    TEXT                     DEFAULT NULL COMMENT '附加的其他类型的媒体内容(JSON格式)',
    `tags`         VARCHAR(255)             DEFAULT NULL COMMENT '标签',
    `category`     VARCHAR(255)             DEFAULT NULL COMMENT '分类',
    `browse_num`   INT UNSIGNED             DEFAULT 0 COMMENT '浏览量',
    `like_num`     INT UNSIGNED             DEFAULT 0 COMMENT '点赞量',
    `unlike_num`   INT UNSIGNED             DEFAULT 0 COMMENT '倒赞量',
    `comment_num`  INT UNSIGNED             DEFAULT 0 COMMENT '评论量',
    `star_num`     INT UNSIGNED             DEFAULT 0 COMMENT '收藏量',
    `address_info` TEXT                     DEFAULT NULL COMMENT '发布时用户所在的地址信息',
    `status`       SMALLINT UNSIGNED        DEFAULT 100 COMMENT '实例状态',
    `meta`         TEXT                     DEFAULT NULL COMMENT '其他数据配置(JSON)',
    `create_time`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '内容创建时间',
    `update_time`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '(最后)更新时间' ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`   INT UNSIGNED             DEFAULT 0 COMMENT '当前实体是否已被逻辑删除',

    FOREIGN KEY (uid) REFERENCES user (id)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '文章';


# 2024-3-24  16:26-创建passage_comment表
CREATE TABLE IF NOT EXISTS `passage_comment`
(
    `id`           BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '文章评论ID',
    `passage_id`   BIGINT UNSIGNED NOT NULL COMMENT '评论的文章ID',
    `from_uid`     BIGINT UNSIGNED NOT NULL COMMENT '评论所属用户(发布者)ID',
    `to_uid`       BIGINT UNSIGNED          DEFAULT NULL COMMENT '回复的用户ID(如果是文章的一级评论，则此值为null)',
    `parent_uid`   BIGINT UNSIGNED          DEFAULT NULL COMMENT '父评论ID(如果有的话)',
    `topic_id`     BIGINT UNSIGNED          DEFAULT NULL COMMENT '所属主题ID(用于根据主题进行分库分表以减缓数据库压力)',
    `content`      CHAR(255)       NOT NULL COMMENT '文章评论内容',
    `accessary`    TEXT COMMENT '附加的其他类型的媒体内容(JSON格式)',
    `tags`         CHAR(255) COMMENT '标签',
    `category`     CHAR(255) COMMENT '分类',
    `browse_num`   INT UNSIGNED             DEFAULT 0 COMMENT '浏览量',
    `like_num`     INT UNSIGNED             DEFAULT 0 COMMENT '点赞量',
    `unlike_num`   INT UNSIGNED             DEFAULT 0 COMMENT '倒赞量',
    `comment_num`  INT UNSIGNED             DEFAULT 0 COMMENT '评论量',
    `star_num`     INT UNSIGNED             DEFAULT 0 COMMENT '收藏量',
    `status`       SMALLINT UNSIGNED        DEFAULT 100 COMMENT '当前状态',
    `meta`         TEXT COMMENT '其他数据配置(JSON)',
    `address_info` TEXT COMMENT '记录文章评论发布时的地址信息',
    `create_time`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '内容创建时间',
    `update_time`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '(最后)更新时间' ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`   INT UNSIGNED             DEFAULT 0 COMMENT '文章评论是否已被逻辑删除',

    FOREIGN KEY (passage_id) REFERENCES passage (id),
    FOREIGN KEY (from_uid) REFERENCES user (id),
    FOREIGN KEY (to_uid) REFERENCES user (id),
    FOREIGN KEY (parent_uid) REFERENCES user (id)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '文章评论';


# 2024-5-20  22:00-创建commodity表
CREATE TABLE IF NOT EXISTS `commodity`
(
    `id`           BIGINT UNSIGNED PRIMARY KEY NOT NULL COMMENT '商品唯一标识符',
    `user_id`      BIGINT UNSIGNED             NOT NULL COMMENT '商品卖家ID',
    `name`         VARCHAR(255)                NOT NULL COMMENT '商品名称',
    `description`  TEXT COMMENT '商品详细描述',
    `tags`         CHAR(255) COMMENT '标签',
    `category`     CHAR(255) COMMENT '分类',
    `browse_num`   INT UNSIGNED    DEFAULT 0 COMMENT '浏览量',
    `like_num`     INT UNSIGNED    DEFAULT 0 COMMENT '点赞量',
    `unlike_num`   INT UNSIGNED    DEFAULT 0 COMMENT '倒赞量',
    `comment_num`  INT UNSIGNED    DEFAULT 0 COMMENT '评论量',
    `star_num`     INT UNSIGNED    DEFAULT 0 COMMENT '收藏量',
    `price`        DOUBLE UNSIGNED DEFAULT 0 COMMENT '商品售价',
    `status`       INT UNSIGNED    DEFAULT 1201 COMMENT '商品状态',
    `meta`         TEXT COMMENT '其他数据配置(JSON)',
    `address_info` TEXT COMMENT '记录文章评论发布时的地址信息',
    `create_time`  TIMESTAMP COMMENT '商品发布时间',
    `update_time`  TIMESTAMP COMMENT '商品信息(最后)更新时间',
    `is_deleted`   INT UNSIGNED    DEFAULT 0 COMMENT '当前实体是否已被逻辑删除',

    FOREIGN KEY (user_id) REFERENCES user (id)
        ON UPDATE CASCADE -- 2024-5-21  23:08-级联删除都设置了，这个更新也一起设置得了
        ON DELETE CASCADE -- 2024-5-21  23:08-我们采用级联删除的方式解决需要同时在两个表中删除同一个商品信息的情况，这样做是最简单的，既不需要动用事务，也不需要自己编写触发器、存储过程或者业务逻辑啥的

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT ='商品';


# 2024-5-20  22:05-创建commodity_details表
CREATE TABLE IF NOT EXISTS `commodity_details`
(

    `id`             BIGINT UNSIGNED PRIMARY KEY NOT NULL COMMENT '商品详情唯一标识符',
    `commodity_id`   BIGINT UNSIGNED             NOT NULL COMMENT '商品ID',
    `stock_quantity` INT UNSIGNED COMMENT '商品库存数量',
    `brand`          VARCHAR(255) COMMENT '商品品牌',
    `weight`         DOUBLE UNSIGNED COMMENT '商品重量',
    `size`           VARCHAR(255) COMMENT '商品尺寸',
    `color`          VARCHAR(255) COMMENT '商品颜色',
    `material`       VARCHAR(255) COMMENT '商品材质',
    `origin`         VARCHAR(255) COMMENT '商品产地',
    `image_url`      TEXT COMMENT '商品图片URL',
    `barcode`        VARCHAR(255) COMMENT '商品条形码',

    FOREIGN KEY (commodity_id) REFERENCES commodity (id)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT ='商品详情';