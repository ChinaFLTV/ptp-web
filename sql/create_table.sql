create database if not exists ptp_web;

use ptp_web;

# 2024-3-22  19:16-创建dialogue表
CREATE TABLE if not exists dialogue
(
    `id`           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID', -- 主键，自增长
    `uid`          BIGINT       NOT NULL COMMENT '发布者ID',       -- 不能为空
    `create_time`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '发布时间',
    `update_time`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '(最后)更新时间',
    `title`        VARCHAR(128) NOT NULL COMMENT '标题',
    `content`      TEXT         NOT NULL COMMENT '内容',
    `accessary`    TEXT                  DEFAULT NULL COMMENT '附加的其他类型的媒体内容(JSON格式)',
    `tags`         VARCHAR(255)          DEFAULT NULL COMMENT '标签',
    `category`     VARCHAR(255)          DEFAULT NULL COMMENT '分类',
    `browse_num`   INT                   DEFAULT 0 COMMENT '浏览量',
    `like_num`     INT                   DEFAULT 0 COMMENT '点赞量',
    `unlike_num`   INT                   DEFAULT 0 COMMENT '倒赞量',
    `comment_num`  INT                   DEFAULT 0 COMMENT '评论量',
    `star_num`     INT                   DEFAULT 0 COMMENT '收藏量',
    `address_info` TEXT                  DEFAULT NULL COMMENT '发布时用户所在的地址信息',
    `status`       CHAR(16)              DEFAULT 'NORMAL' COMMENT '实例状态',
    `meta`         TEXT                  DEFAULT NULL COMMENT '其他数据配置(JSON)',
    `is_deleted`   INT                   DEFAULT 0 COMMENT '当前实体是否已被逻辑删除'
    # INDEX idx_uid (uid),                                        -- 为uid字段创建索引
    # INDEX idx_status (status),                                  -- 为status字段创建索引
    # INDEX idx_is_deleted (isDeleted)                            -- 为isDeleted字段创建索引
);


# 2024-3-22  19:33-创建user表
CREATE TABLE user
(
    `id`                 BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID号', -- 主键，自增长
    `account`            VARCHAR(255) NOT NULL COMMENT '用户账号',             -- 账号生成
    `password`           VARCHAR(255) NOT NULL COMMENT '用户密码(加密)',       -- 密码加密
    `phone`              VARCHAR(16)  DEFAULT NULL COMMENT '用户绑定的手机号',
    `email`              VARCHAR(255) DEFAULT NULL COMMENT '用户邮箱',
    `nickname`           VARCHAR(255) DEFAULT NULL COMMENT '用户昵称',         -- 随机生成昵称
    `realname`           VARCHAR(255) DEFAULT NULL COMMENT '用户真实姓名(管理员必需)',
    `gender`             VARCHAR(16)  DEFAULT 'SECRET' COMMENT '用户性别',
    `idiograph`          VARCHAR(255) DEFAULT '这个家伙很懒，什么都没写> - <' COMMENT '用户的个性签名',
    `avatar`             TEXT         DEFAULT NULL COMMENT '用户头像(JSON)',   -- 用户个人资料背景图片(JSON)
    `background_picture` TEXT         DEFAULT NULL COMMENT '用户个人资料背景图片(JSON)',
    `like_num`           INT          DEFAULT 0 COMMENT '用户被点赞数量',
    `user_rank`          DOUBLE       DEFAULT 0 COMMENT '用户等级',
    `register_date`      TIMESTAMP COMMENT '用户注册时间' ON UPDATE CURRENT_TIMESTAMP,
    `birth_date`         TIMESTAMP    DEFAULT NULL COMMENT '用户出生年月',
    `address_info`       TEXT         DEFAULT NULL COMMENT '用户地址信息',
    `bind_accounts`      VARCHAR(255) DEFAULT NULL COMMENT '用户绑定的其他账号',
    `credit`             DOUBLE       DEFAULT 100 COMMENT '用户信誉积分',
    `status`             VARCHAR(16)  DEFAULT 'NORMAL' COMMENT '用户当前状态',
    `meta`               TEXT         DEFAULT NULL COMMENT '用户其他数据配置(JSON)',
    `role`               VARCHAR(16)  DEFAULT 'USER' COMMENT '用户角色',
    `asset`              TEXT         DEFAULT NULL COMMENT '用户资产'
);
