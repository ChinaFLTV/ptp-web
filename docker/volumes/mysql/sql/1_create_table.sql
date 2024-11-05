DROP DATABASE IF EXISTS `ptp_web`;
CREATE DATABASE `ptp_web`;
USE `ptp_web`;


CREATE TABLE IF NOT EXISTS `asset`
(

    `id`          BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '用户财产ID',
    `balance`     DECIMAL(65, 6) UNSIGNED DEFAULT 0 COMMENT '用户当前账户余额', -- 2024-6-23  16:48-采用DECIMAL数据类型存储用户余额 , 因为DECIMAL类型不存在精度损失 , 备注 : DECIMAL(M,D)占用16B , 其中M范围为1~65(默认为10) , 表示数字的总位数 , D范围为0~30(默认为0,值不得大于M) , 表示小数位数
    `accounts`    CHAR(255)               DEFAULT NULL COMMENT '用户绑定的银行卡',
    `authorities` CHAR(255)               DEFAULT 'drawback,withdraw,view,update_password' COMMENT '当前账户所允许的操作',
    `credit`      DOUBLE UNSIGNED         DEFAULT 100 COMMENT '当前账户的信誉积分',
    `status`      SMALLINT UNSIGNED       DEFAULT 300 COMMENT '当前账户状态',
    `create_time` TIMESTAMP               DEFAULT CURRENT_TIMESTAMP COMMENT '资产创建时间',
    `update_time` TIMESTAMP               DEFAULT CURRENT_TIMESTAMP COMMENT '(最后)修改时间' ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`  INT UNSIGNED            DEFAULT 0 COMMENT '资产是否已被删除',
    `version`     INT UNSIGNED            DEFAULT 1 COMMENT '当前资产实体的版本(用于辅助实现乐观锁)'

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '用户资产';


CREATE TABLE `role`
(
    `id`          BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '用户角色ID',
    `code`        INT UNSIGNED NOT NULL COMMENT '角色对应的序号',
    `name`        CHAR(255)    NOT NULL COMMENT '角色对应的名称',
    `authorities` VARCHAR(1024) DEFAULT 'content_list,content_add,content_remove,content_update' COMMENT '当前角色所允许的操作',
    `prohibition` CHAR(255)     DEFAULT 'user_add,user_remove,role_add,role_remove,role_list,role_update' COMMENT '当前角色所禁止的操作',
    `create_time` TIMESTAMP     DEFAULT CURRENT_TIMESTAMP COMMENT '角色创建时间',
    `update_time` TIMESTAMP     DEFAULT CURRENT_TIMESTAMP COMMENT '(最后)修改时间' ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`  INT UNSIGNED  DEFAULT 0 COMMENT '角色是否已被删除',
    `version`     INT UNSIGNED  DEFAULT 1 COMMENT '当前角色实体的版本(用于辅助实现乐观锁)',

    UNIQUE KEY ix_code (code),
    UNIQUE KEY ix_name (name)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '角色';


# 2024-3-22  19:33-创建user表
CREATE TABLE IF NOT EXISTS `user`
(
    `id`            BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID', -- 主键，自增长
    `account`       VARCHAR(255) NOT NULL COMMENT '用户账号',                    -- 账号生成
    `password`      VARCHAR(255) NOT NULL COMMENT '用户密码(加密)',              -- 密码加密
    `phone`         VARCHAR(16)       DEFAULT NULL COMMENT '用户绑定的手机号',
    `email`         VARCHAR(255)      DEFAULT NULL COMMENT '用户邮箱',
    `nickname`      VARCHAR(255)      DEFAULT NULL COMMENT '用户昵称',           -- 随机生成昵称
    `realname`      VARCHAR(255)      DEFAULT NULL COMMENT '用户真实姓名(管理员必需)',
    `gender`        TINYINT UNSIGNED  DEFAULT 2 COMMENT '用户性别',
    `age`           INT UNSIGNED      DEFAULT NULL COMMENT '用户年龄',
    `idiograph`     VARCHAR(255)      DEFAULT '这个家伙很懒，什么都没写> - <' COMMENT '用户的个性签名',
    `avatar`        TEXT              DEFAULT NULL COMMENT '用户头像(JSON)',     -- 用户个人资料背景图片(JSON)
    `background`    TEXT              DEFAULT NULL COMMENT '用户个人资料背景图片(JSON)',
    `like_num`      INT UNSIGNED      DEFAULT 0 COMMENT '用户被点赞数量',
    `share_num`     INT UNSIGNED      DEFAULT 0 COMMENT '用户被转发的数量',
    `user_rank`     DOUBLE UNSIGNED   DEFAULT 0 COMMENT '用户等级',
    `birth_date`    TIMESTAMP         DEFAULT NULL COMMENT '用户出生年月',
    `address`       VARCHAR(255)      DEFAULT '[]' COMMENT '用户家庭地址信息',
    `bind_accounts` VARCHAR(255)      DEFAULT NULL COMMENT '用户绑定的其他账号',
    `credit`        DOUBLE UNSIGNED   DEFAULT 100 COMMENT '用户信誉积分',
    `status`        SMALLINT UNSIGNED DEFAULT 300 COMMENT '用户当前状态',
    `meta`          TEXT              DEFAULT NULL COMMENT '用户其他数据配置(JSON)',
    `role_id`       BIGINT UNSIGNED   DEFAULT NULL COMMENT '用户角色ID',
    `asset_id`      BIGINT UNSIGNED   DEFAULT NULL COMMENT '用户资产ID',
    `create_time`   TIMESTAMP         DEFAULT CURRENT_TIMESTAMP COMMENT '用户注册时间',
    `update_time`   TIMESTAMP         DEFAULT CURRENT_TIMESTAMP COMMENT '用户资料修改时间' ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`    INT UNSIGNED      DEFAULT 0 COMMENT '用户是否已被删除',
    `version`       INT UNSIGNED      DEFAULT 1 COMMENT '当前用户实体的版本(用于辅助实现乐观锁)',

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
    `id`             BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '对话ID', -- 主键，自增长
    `uid`            BIGINT UNSIGNED NOT NULL COMMENT '发布者ID',                 -- 不能为空
    `title`          VARCHAR(128)    NOT NULL COMMENT '标题',
    `content`        TEXT            NOT NULL COMMENT '内容',
    `content_format` INT UNSIGNED      DEFAULT 2501 COMMENT '内容形式',
    `accessary`      TEXT              DEFAULT NULL COMMENT '附加的其他类型的媒体内容(JSON格式)',
    `tags`           VARCHAR(255)      DEFAULT NULL COMMENT '标签',
    `category`       VARCHAR(255)      DEFAULT NULL COMMENT '分类',
    `browse_num`     INT UNSIGNED      DEFAULT 0 COMMENT '浏览量',
    `like_num`       INT UNSIGNED      DEFAULT 0 COMMENT '点赞量',
    `unlike_num`     INT UNSIGNED      DEFAULT 0 COMMENT '倒赞量',
    `comment_num`    INT UNSIGNED      DEFAULT 0 COMMENT '评论量',
    `star_num`       INT UNSIGNED      DEFAULT 0 COMMENT '收藏量',
    `share_num`      INT UNSIGNED      DEFAULT 0 COMMENT '转发量',
    `address_info`   TEXT              DEFAULT NULL COMMENT '发布时用户所在的地址信息',
    `status`         SMALLINT UNSIGNED DEFAULT 100 COMMENT '实例状态',
    `meta`           TEXT              DEFAULT NULL COMMENT '其他数据配置(JSON)',
    `create_time`    TIMESTAMP         DEFAULT CURRENT_TIMESTAMP COMMENT '内容创建时间',
    `update_time`    TIMESTAMP         DEFAULT CURRENT_TIMESTAMP COMMENT '(最后)更新时间' ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`     INT UNSIGNED      DEFAULT 0 COMMENT '当前实体是否已被逻辑删除',
    `version`        INT UNSIGNED      DEFAULT 1 COMMENT '当前对话实体的版本(用于辅助实现乐观锁)',

    FOREIGN KEY (uid) REFERENCES user (id)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '对话';


# 2024-3-24  16:18-创建announcement表
CREATE TABLE IF NOT EXISTS `announcement`
(
    `id`             BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '公告ID', -- 主键，自增长
    `uid`            BIGINT UNSIGNED NOT NULL COMMENT '发布者ID',                 -- 不能为空
    `title`          VARCHAR(128)    NOT NULL COMMENT '标题',
    `content`        TEXT            NOT NULL COMMENT '内容',
    `content_format` INT UNSIGNED      DEFAULT 2501 COMMENT '内容形式',
    `accessary`      TEXT              DEFAULT NULL COMMENT '附加的其他类型的媒体内容(JSON格式)',
    `tags`           VARCHAR(255)      DEFAULT NULL COMMENT '标签',
    `category`       VARCHAR(255)      DEFAULT NULL COMMENT '分类',
    `browse_num`     INT UNSIGNED      DEFAULT 0 COMMENT '浏览量',
    `like_num`       INT UNSIGNED      DEFAULT 0 COMMENT '点赞量',
    `unlike_num`     INT UNSIGNED      DEFAULT 0 COMMENT '倒赞量',
    `comment_num`    INT UNSIGNED      DEFAULT 0 COMMENT '评论量',
    `star_num`       INT UNSIGNED      DEFAULT 0 COMMENT '收藏量',
    `share_num`      INT UNSIGNED      DEFAULT 0 COMMENT '转发量',
    `address_info`   TEXT              DEFAULT NULL COMMENT '发布时用户所在的地址信息',
    `status`         SMALLINT UNSIGNED DEFAULT 100 COMMENT '实例状态',
    `meta`           TEXT              DEFAULT NULL COMMENT '其他数据配置(JSON)',
    `create_time`    TIMESTAMP         DEFAULT CURRENT_TIMESTAMP COMMENT '内容创建时间',
    `update_time`    TIMESTAMP         DEFAULT CURRENT_TIMESTAMP COMMENT '(最后)更新时间' ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`     INT UNSIGNED      DEFAULT 0 COMMENT '当前实体是否已被逻辑删除',
    `version`        INT UNSIGNED      DEFAULT 1 COMMENT '当前公告实体的版本(用于辅助实现乐观锁)',

    FOREIGN KEY (uid) REFERENCES user (id)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '公告';


# 2024-10-25  20:40-创建实体评分表
CREATE TABLE IF NOT EXISTS `rate`
(
    `id`                  BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT 'ID',
    `uid`                 BIGINT UNSIGNED                            NOT NULL COMMENT '评分者ID',
    `content_type`        INT UNSIGNED                               NOT NULL COMMENT '内容实体类型',
    `content_id`          BIGINT UNSIGNED DEFAULT NULL COMMENT '内容实体ID',
    `content_title`       VARCHAR(128)    DEFAULT NULL COMMENT '内容实体标题(如果有的话)',
    `content_tags`        TEXT            DEFAULT NULL COMMENT '内容实体的评分标签(JSON)',
    `status`              INT UNSIGNED    DEFAULT 200 COMMENT '实例状态',
    `average_score`       DOUBLE          DEFAULT -1 COMMENT '平均评分',
    `max_score`           DOUBLE          DEFAULT -1 COMMENT '最高评分',
    `min_score`           DOUBLE          DEFAULT -1 COMMENT '最低评分',
    `rate_user_count`     BIGINT UNSIGNED DEFAULT 0 COMMENT '评分人数',
    `rate_user_count_map` TEXT            DEFAULT NULL COMMENT '评分人数分布映射(JSON)',
    `rate_map`            TEXT            DEFAULT NULL COMMENT '具体的评分详情(JSON)',
    `rate_type`           INT UNSIGNED    DEFAULT 2302 COMMENT '评分记录的类型',
    `statistic_rate_id`   BIGINT UNSIGNED DEFAULT NULL COMMENT '对应内容实体的评分统计记录的ID',
    `meta`                TEXT            DEFAULT NULL COMMENT '其他数据配置(JSON)',
    `create_time`         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP COMMENT '内容创建时间',
    `update_time`         TIMESTAMP       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '(最后)更新时间',
    `is_deleted`          INT UNSIGNED    DEFAULT 0 COMMENT '当前实体是否已被逻辑删除',
    `version`             INT UNSIGNED    DEFAULT 1 COMMENT '当前实体的版本(用于辅助实现乐观锁)',

    FOREIGN KEY (uid) REFERENCES user (`id`)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT ='内容评分实体(PO实体类)';


# 2024-3-24  16:18-创建passage表
CREATE TABLE IF NOT EXISTS `passage`
(
    `id`                   BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '文章ID',                                          -- 主键，自增长
    `uid`                  BIGINT UNSIGNED NOT NULL COMMENT '发布者ID',                                                          -- 不能为空
    `rate_id`              BIGINT UNSIGNED NOT NULL COMMENT '评分记录统计ID(建议在创建文章记录时 , 同步新增对应的评分统计记录)', -- 不能为空
    `publisher_nickname`   CHAR(255)         DEFAULT '' COMMENT '文章发布者昵称',
    `publisher_avatar_url` CHAR(255)         DEFAULT '' COMMENT '文章发布者头像URL',
    `title`                VARCHAR(128)    NOT NULL COMMENT '标题',
    `introduction`         CHAR(255)       NOT NULL COMMENT '内容介绍',
    `cover_img_url`        CHAR(255)         DEFAULT NULL COMMENT '文章封面图片资源URL',
    `content`              TEXT            NOT NULL COMMENT '内容',
    `content_format`       INT UNSIGNED      DEFAULT 2501 COMMENT '内容形式',
    `accessary`            TEXT              DEFAULT NULL COMMENT '附加的其他类型的媒体内容(JSON格式)',
    `tags`                 VARCHAR(255)      DEFAULT NULL COMMENT '标签',
    `category`             VARCHAR(255)      DEFAULT NULL COMMENT '分类',
    `browse_num`           INT UNSIGNED      DEFAULT 0 COMMENT '浏览量',
    `like_num`             INT UNSIGNED      DEFAULT 0 COMMENT '点赞量',
    `unlike_num`           INT UNSIGNED      DEFAULT 0 COMMENT '倒赞量',
    `comment_num`          INT UNSIGNED      DEFAULT 0 COMMENT '评论量',
    `star_num`             INT UNSIGNED      DEFAULT 0 COMMENT '收藏量',
    `share_num`            INT UNSIGNED      DEFAULT 0 COMMENT '转发量',
    `address_info`         TEXT              DEFAULT NULL COMMENT '发布时用户所在的地址信息',
    `status`               SMALLINT UNSIGNED DEFAULT 100 COMMENT '实例状态',
    `meta`                 TEXT              DEFAULT NULL COMMENT '其他数据配置(JSON)',
    `create_time`          TIMESTAMP         DEFAULT CURRENT_TIMESTAMP COMMENT '内容创建时间',
    `update_time`          TIMESTAMP         DEFAULT CURRENT_TIMESTAMP COMMENT '(最后)更新时间' ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`           INT UNSIGNED      DEFAULT 0 COMMENT '当前实体是否已被逻辑删除',
    `version`              INT UNSIGNED      DEFAULT 1 COMMENT '当前文章实体的版本(用于辅助实现乐观锁)',

    FOREIGN KEY (uid) REFERENCES user (id),
    FOREIGN KEY (rate_id) REFERENCES rate (id)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '文章';


# 2024-3-24  16:26-创建comment表
CREATE TABLE IF NOT EXISTS `comment`
(
    `id`              BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '文章评论ID',
    `content_id`      BIGINT UNSIGNED NOT NULL COMMENT '评论所归属的内容的ID',
    `belong_type`     INT UNSIGNED    NOT NULL COMMENT '评论所附属的内容类型',
    `from_uid`        BIGINT UNSIGNED NOT NULL COMMENT '评论所属用户(发布者)ID',
    `from_nickname`   CHAR(255)         DEFAULT NULL COMMENT '评论所属用户(发布者)昵称',
    `from_avatar_url` CHAR(255)         DEFAULT NULL COMMENT '评论所属用户(发布者)头像URL',
    `to_uid`          BIGINT UNSIGNED   DEFAULT NULL COMMENT '回复的用户ID(如果是文章的一级评论，则此值为null)',
    `to_nickname`     CHAR(255)         DEFAULT NULL COMMENT '回复的用户昵称',
    `to_avatar_url`   CHAR(255)         DEFAULT NULL COMMENT '回复的用户头像URL',
    `parent_id`       BIGINT UNSIGNED   DEFAULT NULL COMMENT '父评论ID(如果有的话)',
    `topic_id`        BIGINT UNSIGNED   DEFAULT NULL COMMENT '所属主题ID(用于根据主题进行分库分表以减缓数据库压力)',
    `content`         CHAR(255)       NOT NULL COMMENT '文章评论内容',
    `content_format`  INT UNSIGNED      DEFAULT 2501 COMMENT '内容形式',
    `accessary`       TEXT COMMENT '附加的其他类型的媒体内容(JSON格式)',
    `tags`            CHAR(255)         DEFAULT NULL COMMENT '标签',
    `category`        CHAR(255)         DEFAULT NULL COMMENT '分类',
    `browse_num`      INT UNSIGNED      DEFAULT 0 COMMENT '浏览量',
    `like_num`        INT UNSIGNED      DEFAULT 0 COMMENT '点赞量',
    `unlike_num`      INT UNSIGNED      DEFAULT 0 COMMENT '倒赞量',
    `comment_num`     INT UNSIGNED      DEFAULT 0 COMMENT '评论量',
    `star_num`        INT UNSIGNED      DEFAULT 0 COMMENT '收藏量',
    `share_num`       INT UNSIGNED      DEFAULT 0 COMMENT '转发量',
    `status`          SMALLINT UNSIGNED DEFAULT 100 COMMENT '当前状态',
    `meta`            TEXT COMMENT '其他数据配置(JSON)',
    `address_info`    TEXT COMMENT '记录文章评论发布时的地址信息',
    `create_time`     TIMESTAMP         DEFAULT CURRENT_TIMESTAMP COMMENT '内容创建时间',
    `update_time`     TIMESTAMP         DEFAULT CURRENT_TIMESTAMP COMMENT '(最后)更新时间' ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`      INT UNSIGNED      DEFAULT 0 COMMENT '文章评论是否已被逻辑删除',
    `version`         INT UNSIGNED      DEFAULT 1 COMMENT '当前文章评论实体的版本(用于辅助实现乐观锁)',

    # FOREIGN KEY (passage_id) REFERENCES passage (id),
    FOREIGN KEY (from_uid) REFERENCES user (id),
    FOREIGN KEY (to_uid) REFERENCES user (id),
    FOREIGN KEY (parent_id) REFERENCES comment (id)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '内容评论';


# 2024-5-20  22:00-创建commodity表
CREATE TABLE IF NOT EXISTS `commodity`
(
    `id`           BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT '商品唯一标识符',
    `user_id`      BIGINT UNSIGNED                            NOT NULL COMMENT '商品卖家ID',
    `name`         VARCHAR(255)                               NOT NULL COMMENT '商品名称',
    `description`  TEXT                    DEFAULT NULL COMMENT '商品详细描述',
    `tags`         CHAR(255)               DEFAULT NULL COMMENT '标签',
    `category`     CHAR(255)               DEFAULT NULL COMMENT '分类',
    `browse_num`   INT UNSIGNED            DEFAULT 0 COMMENT '浏览量',
    `like_num`     INT UNSIGNED            DEFAULT 0 COMMENT '点赞量',
    `unlike_num`   INT UNSIGNED            DEFAULT 0 COMMENT '倒赞量',
    `comment_num`  INT UNSIGNED            DEFAULT 0 COMMENT '评论量',
    `star_num`     INT UNSIGNED            DEFAULT 0 COMMENT '收藏量',
    `share_num`    INT UNSIGNED            DEFAULT 0 COMMENT '转发量',
    `price`        DECIMAL(65, 6) UNSIGNED DEFAULT 0 COMMENT '商品售价',
    `status`       INT UNSIGNED            DEFAULT 1201 COMMENT '商品状态',
    `meta`         TEXT                    DEFAULT NULL COMMENT '其他数据配置(JSON)',
    `address_info` TEXT                    DEFAULT NULL COMMENT '记录文章评论发布时的地址信息',
    `create_time`  TIMESTAMP               DEFAULT CURRENT_TIMESTAMP COMMENT '商品发布时间',
    `update_time`  TIMESTAMP               DEFAULT CURRENT_TIMESTAMP COMMENT '商品信息(最后)更新时间',
    `is_deleted`   INT UNSIGNED            DEFAULT 0 COMMENT '当前实体是否已被逻辑删除',
    `version`      INT UNSIGNED            DEFAULT 1 COMMENT '当前商品实体的版本(用于辅助实现乐观锁)',

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

    `id`             BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT '商品详情唯一标识符', -- // 2024-5-23  21:13-对于商品详情ID，我们默认采用数据块自动主键类型，这样做不会产生副作用，因为商品详情ID不会流入到Java实体类和业务逻辑这里，因此也就不存在主键ID所出现的传统问题和风险
    `commodity_id`   BIGINT UNSIGNED                            NOT NULL COMMENT '商品ID',
    `stock_quantity` INT UNSIGNED COMMENT '商品库存数量',
    `brand`          VARCHAR(255) COMMENT '商品品牌',
    `weight`         DOUBLE UNSIGNED COMMENT '商品重量',
    `size`           VARCHAR(255) COMMENT '商品尺寸',
    `color`          VARCHAR(255) COMMENT '商品颜色',
    `material`       VARCHAR(255) COMMENT '商品材质',
    `origin`         VARCHAR(255) COMMENT '商品产地',
    `image_url`      TEXT COMMENT '商品图片URL',
    `barcode`        VARCHAR(255) COMMENT '商品条形码',
    `version`        INT UNSIGNED DEFAULT 1 COMMENT '当前商品详情实体的版本(用于辅助实现乐观锁)',

    FOREIGN KEY (commodity_id) REFERENCES commodity (id)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT ='商品详情';


# 2024-5-25  22:45-创建transaction_record表
CREATE TABLE `transaction_record`
(
    `id`           BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT 'ID',
    `uid`          BIGINT UNSIGNED                            NOT NULL COMMENT '交易者ID',
    `commodity_id` BIGINT UNSIGNED                            NOT NULL COMMENT '商品ID',
    `remark`       TEXT            DEFAULT NULL COMMENT '交易备注',
    `count`        INT(11)         DEFAULT 0                  NOT NULL COMMENT '下单数量',
    `total_price`  DOUBLE UNSIGNED DEFAULT 0                  NOT NULL COMMENT '总价',
    `discount`     DOUBLE UNSIGNED DEFAULT 0                  NOT NULL COMMENT '折扣',
    `payment_mode` TEXT                                       NOT NULL COMMENT '支付方式',
    `tags`         TEXT            DEFAULT NULL COMMENT '标签',
    `category`     TEXT            DEFAULT NULL COMMENT '分类',
    `browse_num`   INT UNSIGNED    DEFAULT 0 DEFAULT NULL COMMENT '浏览量',
    `like_num`     INT UNSIGNED    DEFAULT 0 DEFAULT NULL COMMENT '点赞量',
    `unlike_num`   INT UNSIGNED    DEFAULT 0 DEFAULT NULL COMMENT '倒赞量',
    `comment_num`  INT UNSIGNED    DEFAULT 0 DEFAULT NULL COMMENT '评论量',
    `star_num`     INT UNSIGNED    DEFAULT 0 DEFAULT NULL COMMENT '收藏量',
    `share_num`    INT UNSIGNED    DEFAULT 0 DEFAULT NULL COMMENT '转发量',
    `processors`   TEXT            DEFAULT NULL COMMENT '中间处理该订单的人员ID',
    `address_info` TEXT            DEFAULT NULL COMMENT '发布时用户所在的地址信息',
    `status`       INT UNSIGNED    DEFAULT 1301 DEFAULT NULL COMMENT '实例状态',
    `meta`         TEXT COMMENT '其他数据配置(JSON)',
    `create_time`  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP COMMENT '流水创建时间',
    `update_time`  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP COMMENT '（最后）更新时间',
    `is_deleted`   INT UNSIGNED    DEFAULT 0 COMMENT '当前实体是否已被逻辑删除',
    `version`      INT UNSIGNED    DEFAULT 1 COMMENT '当前商品交易记录实体的版本(用于辅助实现乐观锁)',

    FOREIGN KEY (uid) REFERENCES user (id),
    FOREIGN KEY (commodity_id) REFERENCES commodity (id)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT ='商品交易订单(PO实体类)';


# 2024-6-28  21:47-创建seata框架所需的undo-log表
CREATE TABLE `undo_log`
(

    `id`            BIGINT                                                        NOT NULL AUTO_INCREMENT,
    `branch_id`     BIGINT                                                        NOT NULL,
    `xid`           VARCHAR(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
    `context`       VARCHAR(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
    `rollback_info` LONGBLOB                                                      NOT NULL,
    `log_status`    INT                                                           NOT NULL,
    `log_created`   DATETIME                                                      NOT NULL,
    `log_modified`  DATETIME                                                      NOT NULL,

    PRIMARY KEY (id) USING BTREE,
    UNIQUE KEY `ux_undo_log` (xid, branch_id) USING BTREE

) ENGINE = InnoDB
  AUTO_INCREMENT = 15
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = DYNAMIC;


# 2024-10-17  20:09-创建实体举报表
CREATE TABLE IF NOT EXISTS `report`
(
    `id`             BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT 'ID',
    `uid`            BIGINT UNSIGNED                            NOT NULL COMMENT '举报者ID',
    `title`          VARCHAR(128) DEFAULT NULL COMMENT '标题',
    `content`        TEXT                                       NOT NULL COMMENT '举报内容',
    `content_type`   INT UNSIGNED                               NOT NULL COMMENT '内容实体类型',
    `content_format` INT UNSIGNED DEFAULT 2501 COMMENT '内容形式',
    `content_id`     BIGINT UNSIGNED                            NOT NULL COMMENT '内容实体ID',
    `nickname`       VARCHAR(255) DEFAULT NULL COMMENT '举报者昵称',
    `avatar_url`     VARCHAR(255) DEFAULT NULL COMMENT '举报者头像URL',
    `accessary`      TEXT         DEFAULT NULL COMMENT '附加的其他类型的媒体内容(JSON格式)',
    `tags`           CHAR(255)    DEFAULT NULL COMMENT '标签',
    `category`       CHAR(255)    DEFAULT NULL COMMENT '分类',
    `browse_num`     INT UNSIGNED DEFAULT 0 COMMENT '浏览量',
    `like_num`       INT UNSIGNED DEFAULT 0 COMMENT '点赞量',
    `unlike_num`     INT UNSIGNED DEFAULT 0 COMMENT '倒赞量',
    `comment_num`    INT UNSIGNED DEFAULT 0 COMMENT '评论量',
    `star_num`       INT UNSIGNED DEFAULT 0 COMMENT '收藏量',
    `share_num`      INT UNSIGNED DEFAULT 0 COMMENT '转发量',
    `status`         INT UNSIGNED DEFAULT 100 COMMENT '实例状态',
    `meta`           TEXT         DEFAULT NULL COMMENT '其他数据配置(JSON)',
    `address_info`   TEXT         DEFAULT NULL COMMENT '发布时用户所在的地址信息',
    `create_time`    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '内容创建时间',
    `update_time`    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '(最后)更新时间',
    `is_deleted`     INT UNSIGNED DEFAULT 0 COMMENT '当前实体是否已被逻辑删除',
    `version`        INT UNSIGNED DEFAULT 1 COMMENT '当前实体的版本(用于辅助实现乐观锁)',

    FOREIGN KEY (uid) REFERENCES user (`id`)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT ='内容举报实体(PO实体类)';


# 2024-10-19  1:34-创建轮播表
CREATE TABLE IF NOT EXISTS `banner`
(
    `id`             BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT 'ID',
    `uid`            BIGINT UNSIGNED                            NOT NULL COMMENT '发布者ID',
    `title`          VARCHAR(128)                               NOT NULL COMMENT '轮播标题',
    `content`        TEXT         DEFAULT NULL COMMENT '轮播内容',
    `content_format` INT UNSIGNED DEFAULT 2501 COMMENT '内容形式',
    `img_url`        CHAR(255)                                  NOT NULL COMMENT '轮播插图的云端资源直链(建议比例为>16:9)',
    `accessary`      TEXT         DEFAULT NULL COMMENT '附加的其他类型的媒体内容(JSON格式)',
    `tags`           CHAR(255)    DEFAULT NULL COMMENT '标签',
    `category`       CHAR(255)    DEFAULT NULL COMMENT '分类',
    `browse_num`     INT UNSIGNED DEFAULT 0 COMMENT '浏览量',
    `like_num`       INT UNSIGNED DEFAULT 0 COMMENT '点赞量',
    `unlike_num`     INT UNSIGNED DEFAULT 0 COMMENT '倒赞量',
    `comment_num`    INT UNSIGNED DEFAULT 0 COMMENT '评论量',
    `star_num`       INT UNSIGNED DEFAULT 0 COMMENT '收藏量',
    `share_num`      INT UNSIGNED DEFAULT 0 COMMENT '转发量',
    `status`         INT UNSIGNED DEFAULT 100 COMMENT '实例状态',
    `meta`           TEXT         DEFAULT NULL COMMENT '其他数据配置(JSON)',
    `address_info`   TEXT         DEFAULT NULL COMMENT '发布时用户所在的地址信息',
    `create_time`    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '内容创建时间',
    `update_time`    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '(最后)更新时间',
    `is_deleted`     INT UNSIGNED DEFAULT 0 COMMENT '当前实体是否已被逻辑删除',
    `version`        INT UNSIGNED DEFAULT 1 COMMENT '当前实体的版本(用于辅助实现乐观锁)',

    FOREIGN KEY (uid) REFERENCES user (`id`)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT ='轮播实体(PO实体类)';


# 2024-10-21  17:07-创建订阅关系表
CREATE TABLE IF NOT EXISTS `subscriber_ship`
(
    `id`          BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT 'ID',
    `uid`         BIGINT UNSIGNED                            NOT NULL COMMENT '订阅关系创建者ID',
    `follower_id` BIGINT UNSIGNED                            NOT NULL COMMENT '订阅者的ID(也即订阅操作发起者)',
    `followee_id` BIGINT UNSIGNED                            NOT NULL COMMENT '被订阅者的ID',
    `status`      INT UNSIGNED DEFAULT 200 COMMENT '实例状态',
    `meta`        TEXT         DEFAULT NULL COMMENT '其他数据配置(JSON)',
    `create_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '内容创建时间',
    `update_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '(最后)更新时间',
    `is_deleted`  INT UNSIGNED DEFAULT 0 COMMENT '当前实体是否已被逻辑删除',
    `version`     INT UNSIGNED DEFAULT 1 COMMENT '当前实体的版本(用于辅助实现乐观锁)',

    FOREIGN KEY (uid) REFERENCES user (`id`),
    FOREIGN KEY (follower_id) REFERENCES user (`id`),
    FOREIGN KEY (followee_id) REFERENCES user (`id`)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT ='订阅关系数据实体(PO实体类)';


# 2024-10-29  1:48-创建事件记录表
CREATE TABLE IF NOT EXISTS `event_record`
(
    `id`            BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT 'ID',
    `uid`           BIGINT UNSIGNED                            NOT NULL COMMENT '事件记录产生者ID',
    `nickname`      VARCHAR(255) DEFAULT NULL COMMENT '事件记录产生者昵称',
    `avatar_url`    VARCHAR(255) DEFAULT NULL COMMENT '事件记录产生者头像URL',
    `content_type`  INT UNSIGNED                               NOT NULL COMMENT '内容实体类型',
    `content_id`    BIGINT UNSIGNED                            NOT NULL COMMENT '内容实体ID',
    `content_title` VARCHAR(128) DEFAULT NULL COMMENT '内容实体标题(如果有的话)',
    `event_type`    INT UNSIGNED DEFAULT NULL COMMENT '事件类型',
    `remark`        TEXT         DEFAULT NULL COMMENT '事件备注',
    `status`        INT UNSIGNED DEFAULT 200 COMMENT '实例状态',
    `meta`          TEXT         DEFAULT NULL COMMENT '其他数据配置(JSON)',
    `create_time`   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '内容创建时间',
    `update_time`   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '(最后)更新时间',
    `is_deleted`    INT UNSIGNED DEFAULT 0 COMMENT '当前实体是否已被逻辑删除',
    `version`       INT UNSIGNED DEFAULT 1 COMMENT '当前实体的版本(用于辅助实现乐观锁)',

    FOREIGN KEY (uid) REFERENCES user (`id`)

)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT ='事件记录数据实体(PO实体类)';