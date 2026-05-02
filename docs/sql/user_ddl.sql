-- 用户表 DDL
-- 数据库: marketing

CREATE TABLE IF NOT EXISTS `user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`     VARCHAR(32)  NOT NULL                COMMENT '用户ID',
    `username`    VARCHAR(64)  NOT NULL                COMMENT '用户名',
    `password`    VARCHAR(256) NOT NULL                COMMENT '密码（SHA-256加密）',
    `name`        VARCHAR(64)  DEFAULT NULL            COMMENT '用户姓名',
    `email`       VARCHAR(128) DEFAULT NULL            COMMENT '用户邮箱',
    `phone`       VARCHAR(32)  DEFAULT NULL            COMMENT '用户手机号',
    `status`      VARCHAR(16)  NOT NULL DEFAULT 'active' COMMENT '用户状态',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
