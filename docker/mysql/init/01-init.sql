-- ============================================================
-- Big Market 数据库初始化
-- ============================================================

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`     VARCHAR(32)  NOT NULL                COMMENT '用户ID',
    `username`    VARCHAR(64)  NOT NULL                COMMENT '用户名',
    `password`    VARCHAR(256) NOT NULL                COMMENT '密码',
    `name`        VARCHAR(64)  DEFAULT NULL            COMMENT '姓名',
    `email`       VARCHAR(128) DEFAULT NULL            COMMENT '邮箱',
    `phone`       VARCHAR(32)  DEFAULT NULL            COMMENT '手机号',
    `status`      VARCHAR(16)  NOT NULL DEFAULT 'active' COMMENT '状态',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- 奖品表
CREATE TABLE IF NOT EXISTS `award` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `award_id`    INT          NOT NULL                COMMENT '奖品ID',
    `award_key`   VARCHAR(64)  NOT NULL                COMMENT '奖品key',
    `award_config` TEXT                                 COMMENT '奖品配置',
    `award_desc`  VARCHAR(256) DEFAULT NULL            COMMENT '奖品描述',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_award_id` (`award_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='奖品表';

-- 策略表
CREATE TABLE IF NOT EXISTS `strategy` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `strategy_id`   BIGINT       NOT NULL                COMMENT '策略ID',
    `strategy_desc` VARCHAR(256) NOT NULL                COMMENT '策略描述',
    `rule_models`   VARCHAR(512) DEFAULT NULL            COMMENT '规则模型',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_strategy_id` (`strategy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='策略表';

-- 策略奖品表
CREATE TABLE IF NOT EXISTS `strategy_award` (
    `id`                  BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `strategy_id`         BIGINT        NOT NULL                COMMENT '策略ID',
    `award_id`            INT           NOT NULL                COMMENT '奖品ID',
    `award_title`         VARCHAR(128)  NOT NULL                COMMENT '奖品标题',
    `award_subtitle`      VARCHAR(256)  DEFAULT NULL            COMMENT '奖品副标题',
    `award_count`         INT           NOT NULL DEFAULT 0      COMMENT '奖品总量',
    `award_count_surplus` INT           NOT NULL DEFAULT 0      COMMENT '奖品剩余',
    `award_rate`          DECIMAL(10,4) NOT NULL DEFAULT 0      COMMENT '中奖概率',
    `rule_models`         VARCHAR(512)  DEFAULT NULL            COMMENT '规则模型',
    `sort`                INT           NOT NULL DEFAULT 0      COMMENT '排序',
    `create_time`         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_strategy_id` (`strategy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='策略奖品表';

-- 策略规则表
CREATE TABLE IF NOT EXISTS `strategy_rule` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `strategy_id` BIGINT       NOT NULL                COMMENT '策略ID',
    `award_id`    INT          DEFAULT NULL            COMMENT '奖品ID',
    `rule_type`   INT          NOT NULL                COMMENT '规则类型',
    `rule_model`  VARCHAR(64)  NOT NULL                COMMENT '规则模型',
    `rule_value`  VARCHAR(512) NOT NULL                COMMENT '规则值',
    `rule_desc`   VARCHAR(256) DEFAULT NULL            COMMENT '规则描述',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_strategy_model` (`strategy_id`, `rule_model`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='策略规则表';

-- 规则树
CREATE TABLE IF NOT EXISTS `rule_tree` (
    `id`                 BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `tree_id`            VARCHAR(64)  NOT NULL                COMMENT '规则树ID',
    `tree_name`          VARCHAR(128) NOT NULL                COMMENT '规则树名称',
    `tree_desc`          VARCHAR(256) DEFAULT NULL            COMMENT '规则树描述',
    `tree_root_rule_key` VARCHAR(64)  NOT NULL                COMMENT '根节点key',
    `create_time`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tree_id` (`tree_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='规则树';

-- 规则树节点
CREATE TABLE IF NOT EXISTS `rule_tree_node` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `tree_id`     VARCHAR(64)  NOT NULL                COMMENT '规则树ID',
    `rule_key`    VARCHAR(64)  NOT NULL                COMMENT '规则key',
    `rule_desc`   VARCHAR(256) DEFAULT NULL            COMMENT '规则描述',
    `rule_value`  VARCHAR(512) DEFAULT NULL            COMMENT '规则值',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_tree_id` (`tree_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='规则树节点表';

-- 规则树节点连线
CREATE TABLE IF NOT EXISTS `rule_tree_node_line` (
    `id`               BIGINT      NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `tree_id`          VARCHAR(64) NOT NULL                COMMENT '规则树ID',
    `rule_node_from`   VARCHAR(64) NOT NULL                COMMENT '来源节点',
    `rule_node_to`     VARCHAR(64) NOT NULL                COMMENT '目标节点',
    `rule_limit_type`  VARCHAR(16) NOT NULL                COMMENT '限定类型',
    `rule_limit_value` VARCHAR(64) NOT NULL                COMMENT '限定值',
    `create_time`      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_tree_id` (`tree_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='规则树节点连线表';

-- 抽奖活动
CREATE TABLE IF NOT EXISTS `raffle_activity` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `activity_id`     BIGINT       NOT NULL                COMMENT '活动ID',
    `activity_name`   VARCHAR(128) NOT NULL                COMMENT '活动名称',
    `activity_desc`   VARCHAR(256) DEFAULT NULL            COMMENT '活动描述',
    `begin_date_time` DATETIME     NOT NULL                COMMENT '开始时间',
    `end_date_time`   DATETIME     NOT NULL                COMMENT '结束时间',
    `strategy_id`     BIGINT       NOT NULL                COMMENT '策略ID',
    `state`           VARCHAR(16)  NOT NULL DEFAULT 'open' COMMENT '状态',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_activity_id` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='抽奖活动表';

-- 活动次数配置
CREATE TABLE IF NOT EXISTS `raffle_activity_count` (
    `id`                BIGINT  NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `activity_count_id` BIGINT  NOT NULL                COMMENT '活动次数配置ID',
    `total_count`       INT     NOT NULL DEFAULT 0      COMMENT '总次数',
    `day_count`         INT     NOT NULL DEFAULT 0      COMMENT '日次数',
    `month_count`       INT     NOT NULL DEFAULT 0      COMMENT '月次数',
    `create_time`       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_activity_count_id` (`activity_count_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动次数配置表';

-- 活动 SKU
CREATE TABLE IF NOT EXISTS `raffle_activity_sku` (
    `id`                  VARCHAR(64)   NOT NULL                COMMENT '主键',
    `sku`                 BIGINT        NOT NULL                COMMENT 'SKU',
    `activity_id`         BIGINT        NOT NULL                COMMENT '活动ID',
    `activity_count_id`   BIGINT        NOT NULL                COMMENT '活动次数配置ID',
    `stock_count`         INT           NOT NULL DEFAULT 0      COMMENT '库存总量',
    `stock_count_surplus` INT           NOT NULL DEFAULT 0      COMMENT '剩余库存',
    `product_amount`      DECIMAL(10,2) DEFAULT NULL            COMMENT '商品金额',
    `create_time`         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sku` (`sku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动SKU表';

-- 活动订单
CREATE TABLE IF NOT EXISTS `raffle_activity_order` (
    `id`              BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         VARCHAR(32)   NOT NULL                COMMENT '用户ID',
    `activity_id`     BIGINT        NOT NULL                COMMENT '活动ID',
    `sku`             BIGINT        NOT NULL                COMMENT 'SKU',
    `activity_name`   VARCHAR(128)  NOT NULL                COMMENT '活动名称',
    `strategy_id`     BIGINT        NOT NULL                COMMENT '策略ID',
    `order_id`        VARCHAR(64)   NOT NULL                COMMENT '订单ID',
    `order_time`      DATETIME      NOT NULL                COMMENT '下单时间',
    `total_count`     INT           NOT NULL DEFAULT 0      COMMENT '总次数',
    `day_count`       INT           NOT NULL DEFAULT 0      COMMENT '日次数',
    `month_count`     INT           NOT NULL DEFAULT 0      COMMENT '月次数',
    `state`           VARCHAR(16)   NOT NULL DEFAULT 'create' COMMENT '状态',
    `out_business_no` VARCHAR(64)   DEFAULT NULL            COMMENT '外部业务号',
    `pay_amount`      DECIMAL(10,2) DEFAULT NULL            COMMENT '支付金额',
    `create_time`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`),
    UNIQUE KEY `uk_out_biz_no` (`out_business_no`),
    KEY `idx_user_activity` (`user_id`, `activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动订单表';

-- 活动账户（总）
CREATE TABLE IF NOT EXISTS `raffle_activity_account` (
    `id`                  BIGINT      NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`             VARCHAR(32) NOT NULL                COMMENT '用户ID',
    `activity_id`         BIGINT      NOT NULL                COMMENT '活动ID',
    `total_count`         INT         NOT NULL DEFAULT 0      COMMENT '总次数',
    `total_count_surplus` INT         NOT NULL DEFAULT 0      COMMENT '总剩余',
    `day_count`           INT         NOT NULL DEFAULT 0      COMMENT '日次数',
    `day_count_surplus`   INT         NOT NULL DEFAULT 0      COMMENT '日剩余',
    `month_count`         INT         NOT NULL DEFAULT 0      COMMENT '月次数',
    `month_count_surplus` INT         NOT NULL DEFAULT 0      COMMENT '月剩余',
    `create_time`         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_activity` (`user_id`, `activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动账户表';

-- 活动日账户
CREATE TABLE IF NOT EXISTS `raffle_activity_account_day` (
    `id`                VARCHAR(64)  NOT NULL                COMMENT '主键',
    `user_id`           VARCHAR(32)  NOT NULL                COMMENT '用户ID',
    `activity_id`       BIGINT       NOT NULL                COMMENT '活动ID',
    `day`               VARCHAR(10)  NOT NULL                COMMENT '日期 yyyy-MM-dd',
    `day_count`         INT          NOT NULL DEFAULT 0      COMMENT '日次数',
    `day_count_surplus` INT          NOT NULL DEFAULT 0      COMMENT '日剩余',
    `create_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_activity_day` (`user_id`, `activity_id`, `day`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动日账户表';

-- 活动月账户
CREATE TABLE IF NOT EXISTS `raffle_activity_account_month` (
    `id`                  VARCHAR(64)  NOT NULL                COMMENT '主键',
    `user_id`             VARCHAR(32)  NOT NULL                COMMENT '用户ID',
    `activity_id`         BIGINT       NOT NULL                COMMENT '活动ID',
    `month`               VARCHAR(7)   NOT NULL                COMMENT '月份 yyyy-MM',
    `month_count`         INT          NOT NULL DEFAULT 0      COMMENT '月次数',
    `month_count_surplus` INT          NOT NULL DEFAULT 0      COMMENT '月剩余',
    `create_time`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_activity_month` (`user_id`, `activity_id`, `month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动月账户表';

-- 用户抽奖订单
CREATE TABLE IF NOT EXISTS `user_raffle_order` (
    `id`            VARCHAR(64)  NOT NULL                COMMENT '主键',
    `user_id`       VARCHAR(32)  NOT NULL                COMMENT '用户ID',
    `activity_id`   BIGINT       NOT NULL                COMMENT '活动ID',
    `activity_name` VARCHAR(128) NOT NULL                COMMENT '活动名称',
    `strategy_id`   BIGINT       NOT NULL                COMMENT '策略ID',
    `order_id`      VARCHAR(64)  NOT NULL                COMMENT '订单ID',
    `order_time`    DATETIME     NOT NULL                COMMENT '下单时间',
    `order_state`   VARCHAR(16)  NOT NULL DEFAULT 'create' COMMENT '订单状态',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`),
    KEY `idx_user_activity` (`user_id`, `activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户抽奖订单表';

-- 用户中奖记录
CREATE TABLE IF NOT EXISTS `user_award_record` (
    `id`          VARCHAR(64)  NOT NULL                COMMENT '主键',
    `user_id`     VARCHAR(32)  NOT NULL                COMMENT '用户ID',
    `activity_id` BIGINT       NOT NULL                COMMENT '活动ID',
    `strategy_id` BIGINT       NOT NULL                COMMENT '策略ID',
    `order_id`    VARCHAR(64)  NOT NULL                COMMENT '订单ID',
    `award_id`    INT          NOT NULL                COMMENT '奖品ID',
    `award_title` VARCHAR(128) NOT NULL                COMMENT '奖品标题',
    `award_time`  DATETIME     NOT NULL                COMMENT '中奖时间',
    `award_state` VARCHAR(16)  NOT NULL DEFAULT 'create' COMMENT '状态',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`),
    KEY `idx_user_award` (`user_id`, `activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户中奖记录表';

-- 积分账户
CREATE TABLE IF NOT EXISTS `user_credit_account` (
    `id`               BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`          VARCHAR(32)   NOT NULL                COMMENT '用户ID',
    `total_amount`     DECIMAL(10,2) NOT NULL DEFAULT 0      COMMENT '总积分',
    `available_amount` DECIMAL(10,2) NOT NULL DEFAULT 0      COMMENT '可用积分',
    `account_status`   VARCHAR(16)   NOT NULL DEFAULT 'open' COMMENT '账户状态',
    `create_time`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='积分账户表';

-- 积分流水
CREATE TABLE IF NOT EXISTS `user_credit_order` (
    `id`              BIGINT        NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         VARCHAR(32)   NOT NULL                COMMENT '用户ID',
    `order_id`        VARCHAR(64)   NOT NULL                COMMENT '订单ID',
    `trade_name`      VARCHAR(64)   NOT NULL                COMMENT '交易名称',
    `trade_type`      VARCHAR(16)   NOT NULL                COMMENT '交易类型',
    `trade_amount`    DECIMAL(10,2) NOT NULL                COMMENT '交易金额',
    `out_business_no` VARCHAR(64)   DEFAULT NULL            COMMENT '外部业务号',
    `create_time`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='积分流水表';

-- 任务表（消息补偿）
CREATE TABLE IF NOT EXISTS `task` (
    `id`          VARCHAR(64) NOT NULL                COMMENT '主键',
    `user_id`     VARCHAR(32) NOT NULL                COMMENT '用户ID',
    `message_id`  VARCHAR(64) NOT NULL                COMMENT '消息ID',
    `topic`       VARCHAR(64) NOT NULL                COMMENT 'MQ topic',
    `message`     TEXT        NOT NULL                COMMENT '消息体',
    `state`       VARCHAR(16) NOT NULL DEFAULT 'create' COMMENT '状态',
    `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务表';

-- 日常行为返利配置
CREATE TABLE IF NOT EXISTS `daily_behavior_rebate` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `behavior_type` VARCHAR(32)  NOT NULL                COMMENT '行为类型',
    `rebate_desc`   VARCHAR(256) DEFAULT NULL            COMMENT '返利描述',
    `rebate_type`   VARCHAR(16)  NOT NULL                COMMENT '返利类型',
    `rebate_config` VARCHAR(128) NOT NULL                COMMENT '返利配置',
    `state`         VARCHAR(16)  NOT NULL DEFAULT 'open' COMMENT '状态',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='日常行为返利配置表';

-- 用户行为返利订单
CREATE TABLE IF NOT EXISTS `user_behavior_rebate_order` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`         VARCHAR(32)  NOT NULL                COMMENT '用户ID',
    `order_id`        VARCHAR(64)  NOT NULL                COMMENT '订单ID',
    `out_business_no` VARCHAR(64)  DEFAULT NULL            COMMENT '外部业务号',
    `behavior_type`   VARCHAR(32)  NOT NULL                COMMENT '行为类型',
    `rebate_desc`     VARCHAR(256) DEFAULT NULL            COMMENT '返利描述',
    `rebate_type`     VARCHAR(16)  NOT NULL                COMMENT '返利类型',
    `rebate_config`   VARCHAR(128) NOT NULL                COMMENT '返利配置',
    `biz_id`          VARCHAR(64)  DEFAULT NULL            COMMENT '业务ID',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户行为返利订单表';
