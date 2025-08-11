-- 诊伴伴伴数据库
CREATE DATABASE IF NOT EXISTS `zhenbanban-com` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `zhanbanban-com`;
SET NAMES utf8mb4;

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:  事件表
-- Description: 领域事件, 应用层事件 记录
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `_events`;
CREATE TABLE IF NOT EXISTS `_events`
(
    `id`                 BIGINT UNSIGNED         NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'id',
    `ref_id`             BIGINT UNSIGNED         NOT NULL DEFAULT 0 COMMENT '事件关联ID',
    `event_type`         VARCHAR(75)             NOT NULL DEFAULT '' COMMENT '事件类型',
    `event_id`           VARCHAR(125)            NOT NULL DEFAULT '' COMMENT '事件标识',
    `event_data`         TEXT                    NOT NULL COMMENT '事件数据(仅关键数据)',
    `occurred_at`        BIGINT UNSIGNED         NOT NULL DEFAULT 0 COMMENT '事件发生时间戳',
    `state`              TINYINT(1) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '事件状态(0-待发布 1-已发布 2-发布失败)',
    `created_by`         VARCHAR(255)            NOT NULL DEFAULT '' COMMENT '事件创建人 id:name',
    `created_at`         BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '创建时间',
    `updated_at`         BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '更新时间',
    `version`            BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '乐观锁',
    INDEX `idx_ref_id` (`ref_id`) USING BTREE COMMENT '事件关联ID索引',
    UNIQUE KEY `uk_event_id` (`event_id`) USING BTREE COMMENT '事件标识唯一索引',
    INDEX `idx_event_type` (`event_type`) USING BTREE COMMENT '事件类型索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT '事件表';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:  系统配置表
-- Description:
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `options`;
CREATE TABLE IF NOT EXISTS `options`
(
    `id`                 BIGINT UNSIGNED         NOT NULL PRIMARY KEY COMMENT '配置ID',
    `option_group`       VARCHAR(75)             NOT NULL DEFAULT '' COMMENT '配置分组',
    `option_name`        VARCHAR(75)             NOT NULL UNIQUE COMMENT '配置名',
    `display_name`       VARCHAR(75)             NOT NULL DEFAULT '' COMMENT '配置显示名',
    `option_value`       TEXT                    NOT NULL COMMENT '配置值',
    `description`        VARCHAR(512)            NOT NULL DEFAULT '' COMMENT '配置描述',
    `created_at`         BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '创建时间',
    `updated_at`         BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '更新时间',
    INDEX `idx_option_name` (`option_name`) USING BTREE COMMENT '配置键索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT '系统配置表';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:  互联网医院科室表
-- Description:
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `departments`;
CREATE TABLE IF NOT EXISTS `departments`
(
    `id`                 BIGINT UNSIGNED                                                          NOT NULL PRIMARY KEY COMMENT '科室ID',
    `parent_id`          BIGINT UNSIGNED                                                          NOT NULL DEFAULT 0 COMMENT '上级科室ID',
    `department_type`    ENUM('clinical', 'technology', 'emergency', 'logistics')                 NOT NULL DEFAULT 'logistics' COMMENT '科室类型 clinical:临床科室 technology:医技科室 emergency:急诊与重症科室 logistics:行政及后勤科室',
    `department_name`    VARCHAR(75)                                                              NOT NULL DEFAULT '' COMMENT '科室名称',
    `summary`            VARCHAR(512)                                                             NOT NULL DEFAULT '' COMMENT '科室简介',
    `description`        TEXT                                                                     NOT NULL COMMENT '科室岗位职责',
    `created_at`         BIGINT(11) UNSIGNED                                                      NOT NULL DEFAULT 0 COMMENT '创建时间',
    `updated_at`         BIGINT(11) UNSIGNED                                                      NOT NULL DEFAULT 0 COMMENT '更新时间'
) ENGINE = InnoDB COMMENT '互联网医院科室表';


-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:  疾病诊断表
-- Description: ICD10(国临2.0, 医保2.0, 中医GB/T15657-2021), 含有疾病分类、编码等信息。暂未涉及医保，医保及医保与国临，医保及中医均未入库。
-- Notice     : 描述，章节，病组信息未完善
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `diagnoses`;
CREATE TABLE IF NOT EXISTS `diagnoses`
(
    `id`                                  BIGINT UNSIGNED                                  NOT NULL PRIMARY KEY COMMENT '疾病诊断ID',
    `icd_type`                            TINYINT(1) UNSIGNED                              NOT NULL DEFAULT 0 COMMENT 'ICD类型 1:国临2.0 2:中医GB/T15657-2021 3:医保2.0',
    `icd_code`                            VARCHAR(20)                                      NOT NULL COMMENT 'ICD编码',
    `icd_name`                            VARCHAR(255)                                     NOT NULL COMMENT 'ICD名称',
    `icd_name_pinyin`                     VARCHAR(255)                                     NOT NULL COMMENT 'ICD名称拼音',
    `icd_name_pinyin_abbr`                VARCHAR(255)                                     NOT NULL COMMENT 'ICD名称拼音首字母缩写',
    `icd_optional_name`                   VARCHAR(255)                                     NOT NULL DEFAULT '' COMMENT 'ICD疾病可选用名(仅中医)',
    `icd_optional_name_pinyin`            VARCHAR(255)                                     NOT NULL DEFAULT '' COMMENT 'ICD疾病可选用名(仅中医)拼音',
    `icd_optional_name_pinyin_abbr`       VARCHAR(255)                                     NOT NULL DEFAULT '' COMMENT 'ICD疾病可选用名(仅中医)拼音首字母',
    `icd_alias_name`                      VARCHAR(255)                                     NOT NULL DEFAULT '' COMMENT 'ICD疾病别名, 多个以逗号分隔(仅中医)',
    `icd_alias_name_pinyin`               VARCHAR(255)                                     NOT NULL DEFAULT '' COMMENT 'ICD疾病别名, 多个以逗号分隔(仅中医)拼音',
    `icd_alias_name_pinyin_abbr`          VARCHAR(255)                                     NOT NULL DEFAULT '' COMMENT 'ICD疾病别名, 多个以逗号分隔(仅中医)拼音首字母',
    `description`                         TEXT                                             NOT NULL COMMENT '疾病描述',
    `chapter_code`                        VARCHAR(5)                                       NOT NULL DEFAULT '' COMMENT 'ICD-10章节编码',
    `chapter_name`                        VARCHAR(255)                                     NOT NULL DEFAULT '' COMMENT 'ICD-10章节名称"',
    `block_code`                          VARCHAR(10)                                      NOT NULL DEFAULT '' COMMENT 'ICD-10疾病组编码',
    `block_name`                          VARCHAR(255)                                     NOT NULL DEFAULT '' COMMENT 'ICD-10疾病组名称',
    `created_at`                          BIGINT(11) UNSIGNED                              NOT NULL DEFAULT 0 COMMENT '创建时间',
    `updated_at`                          BIGINT(11) UNSIGNED                              NOT NULL DEFAULT 0 COMMENT '更新时间',
    `deleted_at`                          BIGINT(11) UNSIGNED                              NOT NULL DEFAULT 0 COMMENT '删除时间',
    INDEX `idx_icd_code` (`icd_code`),
    FULLTEXT (`icd_name`, `icd_name_pinyin`, `icd_name_pinyin_abbr`, `icd_optional_name`, `icd_optional_name_pinyin`, `icd_optional_name_pinyin_abbr`, `icd_alias_name`, `icd_alias_name_pinyin`, `icd_alias_name_pinyin_abbr`) WITH PARSER ngram
) ENGINE = InnoDB COMMENT '疾病诊断表';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:   中医证候表
-- Description:
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `syndromes`;
CREATE TABLE IF NOT EXISTS `syndromes`
(
    `id`                           BIGINT UNSIGNED         NOT NULL PRIMARY KEY  COMMENT '证侯ID',
    `syndrome_code`                VARCHAR(20)             NOT NULL DEFAULT '' COMMENT '证侯编码',
    `syndrome_name`                VARCHAR(255)            NOT NULL DEFAULT '' COMMENT '证侯名称',
    `syndrome_name_pinyin`         VARCHAR(255)            NOT NULL DEFAULT '' COMMENT '证侯名称拼音',
    `syndrome_name_pinyin_abbr`    VARCHAR(255)            NOT NULL DEFAULT '' COMMENT '证侯名称拼音首字母缩写',
    `description`                  VARCHAR(512)            NOT NULL DEFAULT '' COMMENT '证侯描述',
    `created_at`                   BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '创建时间',
    `updated_at`                   BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '更新时间',
    `deleted_at`                   BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '删除时间',
    INDEX `idx_syndrome_code` (`syndrome_code`),
    FULLTEXT (`syndrome_name`, `syndrome_name_pinyin`, `syndrome_name_pinyin_abbr`) WITH PARSER ngram
) ENGINE = InnoDB COMMENT '中医证侯表';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:   中医治法
-- Description:  therapeutics 非可数名词，指中医治疗方法或疗法
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `therapeutics`;
CREATE TABLE IF NOT EXISTS `therapeutics`
(
    `id`   BIGINT UNSIGNED             NOT NULL               PRIMARY KEY AUTO_INCREMENT COMMENT '治法ID',
    `therapeutics_code`                VARCHAR(20)            NOT NULL DEFAULT ''    COMMENT '治法编码',
    `therapeutics_name`                VARCHAR(255)           NOT NULL DEFAULT ''    COMMENT '治法名称',
    `therapeutics_name_pinyin`         VARCHAR(255)           NOT NULL DEFAULT '' COMMENT '治法名称拼音',
    `therapeutics_name_pinyin_abbr`    VARCHAR(255)           NOT NULL DEFAULT '' COMMENT '治法名称拼音首字母缩写',
    `description`                      VARCHAR(512)           NOT NULL DEFAULT '' COMMENT '治法描述',
    `created_at`                       BIGINT(11) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '创建时间',
    `updated_at`                       BIGINT(11) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '更新时间',
    `deleted_at`                       BIGINT(11) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '删除时间',
    INDEX `idx_therapeutics_code` (`therapeutics_code`),
    FULLTEXT (`therapeutics_name`, `therapeutics_name_pinyin`, `therapeutics_name_pinyin_abbr`) WITH PARSER ngram
) ENGINE = InnoDB COMMENT '中医治法表';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:   中药饮片字典
-- Description:  编码来源于医保
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `chinese_medicine_pieces`;
CREATE TABLE IF NOT EXISTS `chinese_medicine_pieces`
(
    `id`   BIGINT UNSIGNED       NOT NULL               PRIMARY KEY  COMMENT '饮片ID',
    `piece_code`                 CHAR(10)               NOT NULL COMMENT '饮片编码',
    `piece_name`                 VARCHAR(255)           NOT NULL  COMMENT '饮片名称',
    `piece_name_pinyin`          VARCHAR(255)           NOT NULL COMMENT '饮片名称拼音',
    `piece_name_pinyin_abbr`     VARCHAR(125)           NOT NULL DEFAULT '' COMMENT '饮片名称拼音首字母缩写',
    `piece_alias`                VARCHAR(512)           NOT NULL DEFAULT '' COMMENT '饮片别名(多个以逗号分隔)',
    `piece_alias_pinyin`         VARCHAR(1024)          NOT NULL DEFAULT '' COMMENT '饮片别名拼音(多个以逗号分隔)',
    `piece_alias_pinyin_abbr`    VARCHAR(255)           NOT NULL DEFAULT '' COMMENT '饮片别名拼音首字母缩写(多个以逗号分隔)',
    `nature`                     VARCHAR(50)            NOT NULL DEFAULT '' COMMENT '性味',
    `meridian`                   VARCHAR(50)            NOT NULL DEFAULT '' COMMENT '归经',
    `indications`                VARCHAR(255)           NOT NULL DEFAULT '' COMMENT '功能和主治',
    `dosage`                     VARCHAR(125)           NOT NULL DEFAULT '' COMMENT '用法用量',
    `created_at`                 BIGINT(11) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '创建时间',
    `updated_at`                 BIGINT(11) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '更新时间',
    `deleted_at`                 BIGINT(11) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '删除时间',
    INDEX `idx_piece_code` (`piece_code`),
    FULLTEXT (`piece_name`, `piece_name_pinyin`, `piece_name_pinyin_abbr`, `piece_alias`, `piece_alias_pinyin`, `piece_alias_pinyin_abbr`) WITH PARSER ngram
) ENGINE = InnoDB COMMENT '中药饮片(药材)字典';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:   西药中成药字典
-- Description:  编码来源于医保， 含中成药
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `medicines`;
CREATE TABLE IF NOT EXISTS `medicines`
(
    `id`   BIGINT UNSIGNED          NOT NULL                PRIMARY KEY  COMMENT '药品ID',
    `medicine_code`                 CHAR(24)                NOT NULL COMMENT '药品编码',
    `medicine_name`                 VARCHAR(75)             NOT NULL DEFAULT '' COMMENT '药品商品名称',
    `medicine_name_pinyin`          VARCHAR(288)            NOT NULL DEFAULT '' COMMENT '药品商品名称拼音',
    `medicine_name_pinyin_abbr`     VARCHAR(125)            NOT NULL DEFAULT '' COMMENT '药品商品名称拼音首字母缩写',
    `registered_name`               VARCHAR(75)             NOT NULL COMMENT '药品注册名称',
    `registered_name_pinyin`        VARCHAR(255)            NOT NULL DEFAULT '' COMMENT '药品注册名称拼音',
    `registered_name_pinyin_abbr`   VARCHAR(125)            NOT NULL DEFAULT '' COMMENT '药品注册名称拼音首字母缩写',
    `registered_medicine_model`     VARCHAR(25)             NOT NULL DEFAULT '' COMMENT '药品注册剂型',
    `reality_medicine_model`        VARCHAR(25)             NOT NULL DEFAULT '' COMMENT '药品实际剂型',
    `registered_outlook`            VARCHAR(500)            NOT NULL DEFAULT '' COMMENT '药品注册规格',
    `reality_outlook`               VARCHAR(500)            NOT NULL DEFAULT '' COMMENT '药品实际规格',
    `material_name`                 VARCHAR(255)            NOT NULL DEFAULT '' COMMENT '药品包装材质',
    `factor`                        INT(4) UNSIGNED         NOT NULL DEFAULT 0 COMMENT '药品最小包装数量',
    `unit`                          VARCHAR(15)             NOT NULL DEFAULT '' COMMENT '药品最小包装单位',
    `min_unit`                      VARCHAR(15)             NOT NULL DEFAULT '' COMMENT '药品最小制剂单位',
    `company_name`                  VARCHAR(125)            NOT NULL DEFAULT '' COMMENT '药品生产企业名称',
    `approval_code`                 VARCHAR(100)            NOT NULL DEFAULT '' COMMENT '药品批准文号',
    `standard_code`                 VARCHAR(100)            NOT NULL DEFAULT '' COMMENT '药品本位码',
    `indication`                    TEXT                   NOT NULL COMMENT '药品适应症',
    `description`                   TEXT                   NOT NULL COMMENT '药品描述',
    `otc`                           TINYINT(1) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '是否为非处方药 0:否 1:是',
    `poisonous`                     TINYINT(1) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '是否为毒麻类药品 0:否 1:是',
    `created_at`                    BIGINT(11) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '创建时间',
    `updated_at`                    BIGINT(11) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '更新时间',
    `deleted_at`                    BIGINT(11) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '删除时间',
    UNIQUE INDEX `idx_medicine_code` (`medicine_code`),
    FULLTEXT (`medicine_name`, `medicine_name_pinyin`, `medicine_name_pinyin_abbr`, `registered_name`, `registered_name_pinyin`, `registered_name_pinyin_abbr`) WITH PARSER ngram
) ENGINE = InnoDB COMMENT '西药中成药字典';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:  区域表
-- Description: 五级行政区划表，包含省、市、县、镇、村等信息。
-- ---------------------------------------------------------------------------------------------------------------------
  DROP TABLE IF EXISTS `regions`;
  CREATE TABLE IF NOT EXISTS `regions`
  (
      `id`            BIGINT UNSIGNED        NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '区域ID',
      `parent_id`     BIGINT UNSIGNED        NOT NULL COMMENT '上级区域ID',
      `region_name`   VARCHAR(255)           NOT NULL DEFAULT '' COMMENT '区域名称',
      `pinyin`        VARCHAR(255)           NOT NULL DEFAULT '' COMMENT '区域拼音',
      `pinyin_prefix` VARCHAR(1)             NOT NULL DEFAULT '' COMMENT '区域拼音首字母',
      `level`         TINYINT(2) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '区域级别 1:省 2:市 3:区/县 4:村 5:组/街道',
      INDEX `idx_parent_id` (`parent_id`),
      INDEX `idx_region_name` (`region_name`),
      INDEX `idx_pinyin` (`pinyin`),
      INDEX `idx_pinyin_prefix` (`pinyin_prefix`)
  ) ENGINE = InnoDB
    AUTO_INCREMENT = 1 COMMENT '区域表';


-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:  账号表
-- Description: 全站账号表，账号可以是用户、医生、患者、陪诊等，用于登陆各个终端及系统。
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE IF NOT EXISTS `accounts` (
    `id`            BIGINT UNSIGNED         NOT NULL PRIMARY KEY  COMMENT '主键',
    `scopes`        VARCHAR(255)            NOT NULL DEFAULT 'user' COMMENT '账号作用域',
    `username`      VARCHAR(45)             NOT NULL UNIQUE COMMENT '用户名 >=6 and <= 45',
    `surname`       VARCHAR(75)             NOT NULL DEFAULT '' COMMENT '姓',
    `given_name`    VARCHAR(125)            NOT NULL DEFAULT '' COMMENT '名',
    `gender`        TINYINT(2)              NOT NULL DEFAULT 0 COMMENT '性别 0:未知 1:男 2:女',
    `email`         VARCHAR(255)            NOT NULL DEFAULT '' COMMENT '电子邮箱',
    `phone`         VARCHAR(45)             NOT NULL DEFAULT '' COMMENT '手机号码',
    `password`      VARCHAR(255)            NOT NULL DEFAULT '' COMMENT '密码(加密后)',
    `avatar`        VARCHAR(255)            NOT NULL DEFAULT '' COMMENT '头像',
    `state`         TINYINT(1) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '账号状态 0:禁用 10:激活',
    `version`       BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    `created_at`    BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '创建时间',
    `updated_at`    BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '更新时间',
    INDEX `idx_username` (`username`) USING BTREE COMMENT '用户名索引',
    INDEX `idx_email` (`email`) USING BTREE COMMENT '电子邮箱索引',
    INDEX `idx_phone` (`phone`) USING BTREE COMMENT '手机号码索引',
    INDEX `udx_created_at_id` (`created_at`, `id`) USING BTREE COMMENT '创建时间和ID联合索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT '账号表';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:  账号登陆日志
-- Description:
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `account_login_logs`;
CREATE TABLE IF NOT EXISTS `account_login_logs` (
    `id`                      BIGINT UNSIGNED    NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    `account_id`              BIGINT UNSIGNED    NOT NULL COMMENT '账号ID',
    `username`                VARCHAR(45)        NOT NULL DEFAULT '' COMMENT '用户名',
    `scope`                   VARCHAR(45)        NOT NULL DEFAULT '' COMMENT '账号作用域',
    `kind`                    VARCHAR(125)       NOT NULL DEFAULT '' COMMENT '登录方式(如:web, mobile, app)',
    `platform`                VARCHAR(125)       NOT NULL DEFAULT '' COMMENT '登录平台(如:Windows, macOS, iOS, Android)',
    `client_ip`               VARCHAR(255)       NOT NULL DEFAULT '' COMMENT '登录IP',
    `user_agent`              TEXT               NOT NULL COMMENT '用户代理信息',
    `browser_fingerprint`     TEXT               NOT NULL COMMENT '浏览器指纹信息',
    `token_md5`               VARCHAR(64)        NOT NULL COMMENT '登录令牌MD5值',
    `login_at`                BIGINT UNSIGNED    NOT NULL DEFAULT 0 COMMENT '登录时间戳',
    `logout_at`               BIGINT UNSIGNED    NOT NULL DEFAULT 0 COMMENT '登出时间戳',
    INDEX `idx_account_id` (`account_id`) USING BTREE COMMENT '账号ID索引',
    INDEX `idx_client_ip` (`client_ip`) USING BTREE COMMENT '登陆IP索引'
) ENGINE = InnoDB COMMENT '账号登录日志表';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:  系统管理员表
-- Description:
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `admins`;
CREATE TABLE IF NOT EXISTS `admins` (
    `account_id`       BIGINT UNSIGNED         NOT NULL PRIMARY KEY COMMENT '管理员ID(既账号ID)',
    `last_login_ip`    VARCHAR(45)             NOT NULL DEFAULT '' COMMENT '最后登录IP',
    `last_login_at`    BIGINT UNSIGNED         NOT NULL DEFAULT 0 COMMENT '最后登录时间',
    `created_at`       BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '创建时间',
    `updated_at`       BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '更新时间',
    `deleted_at`       BIGINT UNSIGNED         NOT NULL DEFAULT 0 COMMENT '删除时间'
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT '系统管理员表';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:  系统角色
-- Description:
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles` (
    `id`              BIGINT UNSIGNED         NOT NULL PRIMARY KEY  COMMENT '主键',
    `role_name`       VARCHAR(75)             NOT NULL UNIQUE COMMENT '角色名称',
    `display_name`    VARCHAR(75)             NOT NULL DEFAULT '' COMMENT '角色显示名称',
    `description`     VARCHAR(255)            NOT NULL DEFAULT '' COMMENT '角色描述',
    `created_at`      BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '创建时间',
    `updated_at`      BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '更新时间',
    UNIQUE KEY `uk_role_name` (`role_name`) COMMENT '角色名称唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT '系统角色表';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:  系统权限组
-- Description:
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `permission_groups`;
CREATE TABLE IF NOT EXISTS `permission_groups` (
    `id`               BIGINT UNSIGNED         NOT NULL PRIMARY KEY COMMENT '主键',
    `parent_id`        BIGINT UNSIGNED         NOT NULL DEFAULT 0 COMMENT '父级ID',
    `group_name`       VARCHAR(75)             NOT NULL UNIQUE COMMENT '权限组名称',
    `display_name`     VARCHAR(75)             NOT NULL DEFAULT '' COMMENT '权限组显示名称',
    `description`      VARCHAR(255)            NOT NULL DEFAULT '' COMMENT '权限组描述',
    `created_at`       BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '创建时间',
    `updated_at`       BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '更新时间',
    INDEX `idx_parent_id` (`parent_id`) USING BTREE COMMENT '父级ID索引',
    INDEX `idx_group_name` (`group_name`) USING BTREE COMMENT '权限组名称索引'
) ENGINE = InnoDB COMMENT '系统权限组表';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:  系统权限
-- Description:
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE IF NOT EXISTS `permissions` (
    `id`                 BIGINT UNSIGNED         NOT NULL PRIMARY KEY  COMMENT '主键',
    `group_id`           BIGINT UNSIGNED         NOT NULL DEFAULT 0 COMMENT '权限组ID',
    `permission_name`    VARCHAR(75)             NOT NULL UNIQUE COMMENT '权限名称',
    `display_name`       VARCHAR(75)             NOT NULL DEFAULT '' COMMENT '权限显示名称',
    `description`        VARCHAR(255)            NOT NULL DEFAULT '' COMMENT '全新先描述',
    `created_at`         BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '创建时间',
    `updated_at`         BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '更新时间',
    UNIQUE KEY `uk_permission_name` (`permission_name`) COMMENT '权限名称唯一索引'
) ENGINE = InnoDB COMMENT '系统权限表';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:  系统工作台资源表
-- Description:
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `resources`;
CREATE TABLE IF NOT EXISTS `resources` (
    `id`               BIGINT UNSIGNED                                NOT NULL PRIMARY KEY  COMMENT '主键',
    `parent_id`        BIGINT UNSIGNED                                NOT NULL DEFAULT 0 COMMENT '父级ID',
    `resource_type`    enum('menu', 'button', 'link', 'component')    NOT NULL DEFAULT 'menu' COMMENT '资源类型 menu:菜单 button:按钮 link:链接 component:组件',
    `resource_name`    VARCHAR(75)                                    NOT NULL UNIQUE COMMENT '资源名称',
    `display_name`     VARCHAR(75)                                    NOT NULL DEFAULT '' COMMENT '资源显示名称',
    `description`      VARCHAR(255)                                   NOT NULL DEFAULT '' COMMENT '资源描述',
    `url`              VARCHAR(255)                                   NOT NULL DEFAULT '' COMMENT '资源链接',
    `icon`             VARCHAR(75)                                    NOT NULL DEFAULT '' COMMENT '资源图标',
    `component`        VARCHAR(255)                                   NOT NULL DEFAULT '' COMMENT '前端组件',
    `show_in_menu`     TINYINT(11)                                    NOT NULL DEFAULT 0 COMMENT '是否在菜单中显示 0:否 1:是',
    `sort`             INT(2) UNSIGNED                                NOT NULL DEFAULT 0 COMMENT '排序顺序, 降序排列',
    `created_at`       BIGINT(11) UNSIGNED                            NOT NULL DEFAULT 0 COMMENT '创建时间',
    `updated_at`       BIGINT(11) UNSIGNED                            NOT NULL DEFAULT 0 COMMENT '更新时间',
    INDEX `idx_parent_id` (`parent_id`) USING BTREE COMMENT '父级ID索引',
    INDEX `idx_resource_name` (`resource_name`) USING BTREE COMMENT '资源名称索引'
) ENGINE = InnoDB COMMENT '系统工作台资源表';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:  系统角色与权限关联表
-- Description:
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `role_permissions`;
CREATE TABLE IF NOT EXISTS `role_permissions` (
    `role_id`          BIGINT UNSIGNED         NOT NULL COMMENT '角色ID',
    `permission_id`    BIGINT UNSIGNED         NOT NULL COMMENT '权限ID',
    `created_at`       BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '创建时间',
    PRIMARY key `pk_role_permission` (`role_id`, `permission_id`) COMMENT '角色与权限唯一关联',
    INDEX `idx_role_id` (`role_id`) USING BTREE COMMENT '角色ID索引',
    INDEX `idx_permission_id` (`permission_id`) USING BTREE COMMENT '权限ID索引'
) ENGINE = InnoDB COMMENT '系统角色与权限关联表';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:  系统角色与权限关联表
-- Description:
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `role_resources`;
CREATE TABLE IF NOT EXISTS `role_resources` (
    `role_id`          BIGINT UNSIGNED         NOT NULL COMMENT '角色ID',
    `resource_id`      BIGINT UNSIGNED         NOT NULL COMMENT '资源ID',
    `created_at`       BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '创建时间',
    PRIMARY KEY `uk_role_resource` (`role_id`, `resource_id`) COMMENT '角色与资源唯一关联',
    INDEX `idx_role_id` (`role_id`) USING BTREE COMMENT '角色ID索引',
    INDEX `idx_resource_id` (`resource_id`) USING BTREE COMMENT '资源ID索引'
) ENGINE = InnoDB COMMENT '系统角色与资源关联表';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:  系统管理员与角色关联表
-- Description:
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `admin_roles`;
CREATE TABLE IF NOT EXISTS `admin_roles` (
    `admin_id`         BIGINT UNSIGNED         NOT NULL COMMENT '管理员ID',
    `role_id`          BIGINT UNSIGNED         NOT NULL COMMENT '角色ID',
    `created_at`       BIGINT(11) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '创建时间',
    PRIMARY KEY `pk_admin_role` (`admin_id`, `role_id`) COMMENT '管理员与角色唯一关联',
    INDEX `idx_admin_id` (`admin_id`) USING BTREE COMMENT '管理员ID索引',
    INDEX `idx_role_id` (`role_id`) USING BTREE COMMENT '角色ID索引'
) ENGINE = InnoDB COMMENT '系统管理员与角色关联表';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:   医院表
-- Description:
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `hospitals`;
CREATE TABLE IF NOT EXISTS `hospitals` (
    `id`                             BIGINT UNSIGNED                     NOT NULL PRIMARY KEY COMMENT '医院ID',
    `ownership_type`                 ENUM('PUBLIC', 'PRIVATE', 'OTHER')  NOT NULL DEFAULT 'OTHER' COMMENT '医院所有制类型：PUBLIC=公立 PRIVATE=私立 OTHER=其他',
    `hospital_type`                  ENUM('GENERAL', 'SPECIALTY', 'TRADITIONAL', 'ETHNIC', 'REHABILITATION', 'OTHER')    NOT NULL DEFAULT 'OTHER' COMMENT '医疗机构类型：GENERAL=综合医院 SPECIALTY=专科医院 TRADITIONAL=中医医院 ETHNIC=民族医医院 REHABILITATION=康复医院 OTHER=其他',
    `hospital_level`                 ENUM('LEVEL-3A', 'LEVEL-3B', 'LEVEL-2A', 'LEVEL-2B', 'LEVEL-1', 'OTHER')            NOT NULL DEFAULT 'OTHER' COMMENT '机构等级：LEVEL-3A=三甲、LEVEL-2A=三乙、LEVEL-2A=二甲、LEVEL-2B=二乙、LEVEL-1=一级、OTHER=其他',
    `status`                         ENUM('PENDING', 'ACTIVE', 'INACTIVE')                                               NOT NULL DEFAULT 'PENDING' COMMENT '医院状态：PENDING=待审核 ACTIVE=启用 INACTIVE=禁用',
    `insurance_code`                 CHAR(12)                            NOT NULL DEFAULT '' COMMENT '医保定点机构编码',
    `uscc_code`                      CHAR(18)                            NOT NULL DEFAULT '' COMMENT '统一社会信用代码',
    `hospital_code`                  CHAR(22)                            NOT NULL COMMENT '医院机构登记号',
    `hospital_name`                  VARCHAR(100)                        NOT NULL DEFAULT '' COMMENT '医院名称',
    `province_id`                    BIGINT UNSIGNED                     NOT NULL DEFAULT 0 COMMENT '所在省份编码',
    `city_id`                        BIGINT UNSIGNED                     NOT NULL DEFAULT 0 COMMENT '所在城市编码',
    `county_id`                      BIGINT UNSIGNED                     NOT NULL DEFAULT 0 COMMENT '所在区县编码',
    `address`                        VARCHAR(255)                        NOT NULL DEFAULT '' COMMENT '详细地址',
    `postal_code`                    CHAR(6)                             NOT NULL DEFAULT '' COMMENT '邮政编码',
    `longitude`                      DECIMAL(10, 6)                      DEFAULT NULL COMMENT '经度',
    `latitude`                       DECIMAL(10, 6)                      DEFAULT NULL COMMENT '纬度',
    `map_url`                        VARCHAR(255)                        NOT NULL DEFAULT '' COMMENT '地图链接',
    `contact_phone`                  VARCHAR(20)                         NOT NULL DEFAULT '' COMMENT '联系电话',
    `contact_email`                  VARCHAR(125)                        NOT NULL DEFAULT '' COMMENT '联系邮箱',
    `website`                        VARCHAR(255)                        NOT NULL DEFAULT '' COMMENT '医院官网',
    `companion_diagnosis_enabled`    TINYINT(2)                          NOT NULL DEFAULT 0 COMMENT '是否启用伴诊断服务',
    `meal_service_enabled`           TINYINT(2)                          NOT NULL DEFAULT 0 COMMENT '是否启用配餐服务',
    `testing_delivery_enabled`       TINYINT(2)                          NOT NULL DEFAULT 0 COMMENT '是否启用送检测服务',
    `created_at`                     BIGINT(11) UNSIGNED                 NOT NULL DEFAULT 0 COMMENT '创建时间',
    `updated_at`                     BIGINT(11) UNSIGNED                 NOT NULL DEFAULT 0 COMMENT '更新时间',
    UNIQUE KEY `uk_hospital_code` (`hospital_code`) USING BTREE COMMENT '医院编码索引',
    INDEX `idx_hospital_name` (`hospital_name`) USING BTREE COMMENT '医院名称索
) ENGINE = InnoDB COMMENT '医院表';

-- ---------------------------------------------------------------------------------------------------------------------
-- Table Name:  医生表
-- Description:
-- ---------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `doctors`;
CREATE TABLE IF NOT EXISTS `doctors` (
    `account_id`                              BIGINT UNSIGNED     NOT NULL PRIMARY KEY COMMENT '医生主键ID(账号ID)',
    `doctor_name_pinyin`                      VARCHAR(255)        NOT NULL DEFAULT '' COMMENT '医生姓名拼音',
    `doctor_name_pinyin_abbr`                 VARCHAR(125)        NOT NULL DEFAULT '' COMMENT '医生姓名拼音首字母缩写',
    `photo`                                   VARCHAR(255)        NOT NULL DEFAULT '' COMMENT '医生大头像',
    `portrait_photo`                          VARCHAR(255)        NOT NULL DEFAULT '' COMMENT '医生半身像',
    `title`                                   ENUM('CHIEF', 'DEPUTY-CHIEF', 'ATTENDING', 'RESIDENT', 'OTHER') NOT NULL COMMENT '职称：CHIEF=主任医师、DEPUTY-CHIEF=副主任医师、ATTENDING=主治医师、RESIDENT=住院医师、OTHER=其他',
    `id_number`                               VARCHAR(18)         NOT NULL COMMENT '身份证号码(加密存储)',
    `birthday`                                DATE                DEFAULT NULL COMMENT '出生日期',
    `age`                                     INT(3) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '年龄',
    `license_number`                          CHAR(15)            NOT NULL DEFAULT '' COMMENT '执业证书编号',
    `license_expire_date`                     DATE                DEFAULT NULL COMMENT '执业证书有效期',
    `license`                                 VARCHAR(255)        NOT NULL DEFAULT '' COMMENT '执业证书图片链接',
    `qualification_number`                    CHAR(27)            NOT NULL DEFAULT '' COMMENT '医师资格证书编号',
    `qualification_expire_date`               DATE                DEFAULT NULL COMMENT '医师资格证书有效期',
    `qualification`                           VARCHAR(255)        NOT NULL DEFAULT '' COMMENT '医师资格证书图片链接',
    `practice_start_date`                     DATE                DEFAULT NULL COMMENT '首次执业注册日期',
    `specialty`                               VARCHAR(255)        NOT NULL DEFAULT '' COMMENT '医生擅长专业领域',
    `practice_type`                           ENUM('CLINICAL', 'TRADITIONAL', 'COMBINED') NOT NULL COMMENT '执业类型：CLINICAL=西医、TRADITIONAL=中医、COMBINED=中西医结合',
    `practice_scope`                          VARCHAR(255)        NOT NULL DEFAULT '' COMMENT '执业范围',
    `health_number`                           CHAR(17)            NOT NULL DEFAULT '' COMMENT '健康证编号',
    `health`                                  VARCHAR(255)        NOT NULL DEFAULT '' COMMENT '健康证图片链接',
    `practice_1st_hospital_id`                BIGINT UNSIGNED     NOT NULL DEFAULT 0 COMMENT '第1执业医院ID',
    `practice_1st_hospital_name`              VARCHAR(100)        NOT NULL DEFAULT '' COMMENT '第1执业医院名称',
    `practice_2nd_hospital_id`                BIGINT UNSIGNED     NOT NULL DEFAULT 0 COMMENT '第2执业医院ID',
    `practice_2nd_hospital_name`              VARCHAR(100)        NOT NULL DEFAULT '' COMMENT '第3执业医院名称',
    `practice_3rd_hospital_id`                BIGINT UNSIGNED     NOT NULL DEFAULT 0 COMMENT '第2执业医院ID',
    `practice_3rd_hospital_name`              VARCHAR(100)        NOT NULL DEFAULT '' COMMENT '第3执业医院名称',
    `remark`                                  TEXT                NOT NULL COMMENT '医生备注信息',
    `status`                                  ENUM('PENDING', 'ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL DEFAULT 'PENDING' COMMENT '医生状态：PENDING=待审核、ACTIVE=启用、INACTIVE=禁用、SUSPENDED=暂停',
    `registration_fee_ratio`                  DECIMAL(4, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '挂号费分成比例(百分比)',
    `western_medicine_fee_ratio`              DECIMAL(4, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '西药费分成比例(百分比)',
    `chinese_medicine_piece_fee_ratio`        DECIMAL(4, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '中药饮片费分成比例(百分比)',
    `chinese_medicine_formula_fee_ratio`      DECIMAL(4, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '中医汤剂费分成比例(百分比)',
    `chinese_medicine_particle_fee_ratio`     DECIMAL(4, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '中医颗粒分成比例(百分比)',
    `chinese_medicine_paste_fee_ratio`        DECIMAL(4, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '中医膏方分成比例(百分比)',
    `chinese_medicine_herbal_fee_ratio`       DECIMAL(4, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '中医药膳分成比例(百分比)',
    `created_at`                              BIGINT(11) UNSIGNED          NOT NULL DEFAULT 0 COMMENT '创建时间',
    `updated_at`                              BIGINT(11) UNSIGNED          NOT NULL DEFAULT 0 COMMENT '更新时间',
    INDEX `idx_id_number` (`id_number`) USING BTREE COMMENT '身份证号码索引',
    INDEX `idx_license_number` (`license_number`) USING BTREE COMMENT '执业证书编号索引',
    INDEX `idx_qualification_number` (`qualification_number`) USING BTREE COMMENT '医师资格证书编号索引',
    INDEX `idx_practice_1st_hospital_id` (`practice_1st_hospital_id`) USING BTREE COMMENT '第1执业医院ID索引',
    INDEX `idx_practice_2nd_hospital_id` (`practice_2nd_hospital_id`) USING BTREE COMMENT '第2执业医院ID索引',
    INDEX `idx_practice_3rd_hospital_id` (`practice_3rd_hospital_id`) USING BTREE COMMENT '第3执业医院ID索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT '医生表';


-- 数据
INSERT INTO `accounts` (`id`, `scopes`, `username`, `surname`, `given_name`, `gender`, `email`, `phone`, `password`, `avatar`, `state`, `version`, `created_at`, `updated_at`) VALUES (78071015476822024, 'admin,user', 'admin', '张', '小龙', 0, 'mail@sniu.com', '17810365776', '$2a$10$5r12zl0K.eRKE7R7.ys.w.V5mOHwMXdTHbPW6Mwy8cqj/X8Tba7lG', 'https://avatars.githubusercontent.com/u/7147496', 10, 0, 1754274380580, 0);
INSERT INTO `admins` (`account_id`, `last_login_ip`, `last_login_at`, `created_at`, `updated_at`, `deleted_at`) VALUES (78071015476822024, '', 0, 1754299318014, 0, 0);

INSERT INTO `roles` (`id`, `role_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78224634956218376, 'administrator', '超级管理员', '此角色拥有全站所有操作权限。', 1754311006327, 0);
INSERT INTO `roles` (`id`, `role_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (79320682269245447, 'devops', '运维', '此角色拥有全站运维权限。', 1754572324399, 0);
INSERT INTO `admin_roles` (`admin_id`, `role_id`, `created_at`) VALUES (78071015476822024, 78224634956218376, 1754451256402);

INSERT INTO `permission_groups` (`id`, `parent_id`, `group_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78210881208451078, 0, 'system', '系统管理', '系统功能管理的权限组。', 1754307727171, 0);
INSERT INTO `permission_groups` (`id`, `parent_id`, `group_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78211223186833409, 78210881208451078, 'permission-groups', '权限组', '权限组功能管理的权限组。', 1754307808718, 0);
INSERT INTO `permission_groups` (`id`, `parent_id`, `group_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78211423330631682, 78210881208451078, 'permissions', '权限', '权限功能管理的权限组。', 1754307856424, 0);
INSERT INTO `permission_groups` (`id`, `parent_id`, `group_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78211557451890690, 78210881208451078, 'roles', '角色', '角色功能管理的权限组。', 1754307888400, 0);
INSERT INTO `permission_groups` (`id`, `parent_id`, `group_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78211883647107079, 78210881208451078, 'resouces', '资源', '资源功能管理的权限组。', 1754307966171, 0);
INSERT INTO `permission_groups` (`id`, `parent_id`, `group_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78212020939259913, 78210881208451078, 'metas', '媒体资源', '媒体资源功能管理的权限组。', 1754307998905, 0);
INSERT INTO `permission_groups` (`id`, `parent_id`, `group_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78217268529463296, 0, 'account', '账号', '账号功能的管理的权限组。', 1754309250029, 0);
INSERT INTO `permission_groups` (`id`, `parent_id`, `group_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78217455205351429, 78217268529463296, 'user', '用户', '管理用户的权限组。', 1754309294535, 0);
INSERT INTO `permission_groups` (`id`, `parent_id`, `group_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78217599875284997, 78217268529463296, 'admin', '管理员', '管理管理员的权限组。', 1754309329026, 0);

INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78214987272683522, 78211223186833409, 'permission-group:add', '添加权限组', '拥有此权限可添加权限组。', 1754308706138, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78215233948090373, 78211223186833409, 'permission-group:modify', '修改权限组', '拥有此权限可修改权限组。', 1754308764945, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78215539230507014, 78211223186833409, 'permission-group:delete', '删除权限组', '拥有此权限可删除权限组。', 1754308837731, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78215719585579014, 78211423330631682, 'permission:add', '添加权限', '拥有此权限可添加权限。', 1754308880730, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78216072141996038, 78211423330631682, 'permission:modify', '修改权限', '拥有此权限可修改权限。', 1754308964786, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78216143512272899, 78211423330631682, 'permission:delete', '删除权限', '拥有此权限可删除权限。', 1754308981802, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78216378175193088, 78211557451890690, 'role:add', '添加角色', '拥有此权限可添加角色。', 1754309037750, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78216467299958790, 78211557451890690, 'role:modify', '修改角色', '拥有此权限可修改角色。', 1754309059000, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78216541597859846, 78211557451890690, 'role:delete', '删除角色', '拥有此权限可删除角色。', 1754309076713, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78216707939762177, 78211883647107079, 'resource:add', '添加资源', '拥有此权限可添加资源。', 1754309116371, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78216767863783425, 78211883647107079, 'resource:modify', '修改资源', '拥有此权限可修改资源。', 1754309130659, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78216838344867840, 78211883647107079, 'resource:delete', '删除资源', '拥有此权限可删除资源。', 1754309147464, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78218253352042502, 78217455205351429, 'account:add', '添加用户账号', '拥有此权限可添加用户。', 1754309484827, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78218314223976457, 78217455205351429, 'account:modify', '修改用户账号', '拥有此权限可修改用户。', 1754309499339, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78218417403854852, 78217455205351429, 'account:delete', '删除用户账号', '拥有此权限可删除用户。', 1754309523939, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78218517505114115, 78217455205351429, 'account:activate', '激活用户账号', '拥有此权限可激活用户。', 1754309547807, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78218797772701699, 78212020939259913, 'meta:add', '添加媒体资源', '拥有此权限可添加媒体资源。', 1754309614629, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78218869365276678, 78212020939259913, 'meta:modify', '修改媒体资源', '拥有此权限可修改媒体资源。', 1754309631696, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78218960452976645, 78212020939259913, 'meta:delete', '删除媒体资源', '拥有此权限可删除媒体资源。', 1754309653413, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78219211805032457, 78217599875284997, 'admin:add', '添加管理员', '拥有此权限可添加管理员。', 1754309713339, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78219406101970947, 78217599875284997, 'admin:modify', '修改管理员', '拥有此权限可修改管理员。', 1754309759663, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78219484539650050, 78217599875284997, 'admin:delete', '禁用管理员', '拥有此权限可禁用管理员。', 1754309778365, 0);
INSERT INTO `permissions` (`id`, `group_id`, `permission_name`, `display_name`, `description`, `created_at`, `updated_at`) VALUES (78222061343866888, 78217599875284997, 'admin:activate', '激活管理iyuan', '拥有此权限可激活管理员。', 1754310392724, 0);