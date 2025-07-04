/*
 Navicat Premium Dump SQL

 Source Server         : AjbxyyxSRV
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : 192.168.1.96:3306
 Source Schema         : telegram

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 04/07/2025 05:34:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chat
-- ----------------------------
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat`  (
  `id` bigint NOT NULL COMMENT 'id',
  `uid` bigint NOT NULL COMMENT '用戶id',
  `chat_id` bigint NOT NULL COMMENT '聊天對象的id',
  `last_read_time` datetime NOT NULL COMMENT '最後閲讀時間',
  `mute` tinyint NOT NULL DEFAULT 0 COMMENT '是否通知  0->通知 1不通知',
  `pin` tinyint NOT NULL DEFAULT 0 COMMENT '是否置頂',
  `block` tinyint NOT NULL DEFAULT 0 COMMENT '是否阻止',
  `pinned_messages` json NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_uid`(`uid` ASC) USING BTREE COMMENT 'get chat by uid',
  INDEX `idx_chat_id`(`chat_id` ASC) USING BTREE COMMENT 'get chat by chatId',
  INDEX `idx_uid_chat_id`(`uid` ASC, `chat_id` ASC) USING BTREE COMMENT 'check chat and update match'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for contact
-- ----------------------------
DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact`  (
  `target_uid` bigint NOT NULL COMMENT '聯係人uid',
  `mobile` int NULL DEFAULT NULL COMMENT '聯係人電話',
  `mobile_country` int NULL DEFAULT NULL COMMENT '聯係人電話國家',
  `first_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '聯係人FirstName',
  `last_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '聯係人LastName',
  `uid` bigint NOT NULL COMMENT '本人uid',
  PRIMARY KEY (`target_uid`) USING BTREE,
  INDEX `idx_uid_target_uid`(`target_uid` ASC, `uid` ASC) USING BTREE COMMENT 'locate and update match',
  INDEX `idx_uid`(`uid` ASC) USING BTREE COMMENT 'get one\'s contact list'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for device_record
-- ----------------------------
DROP TABLE IF EXISTS `device_record`;
CREATE TABLE `device_record`  (
  `id` bigint NOT NULL COMMENT 'id',
  `system_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系統',
  `device` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登入設備名稱',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登入ip 可變',
  `latitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '經度',
  `longitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '緯度',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '城市',
  `country` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '國家',
  `uid` bigint NOT NULL COMMENT 'uid',
  `date` datetime NULL DEFAULT NULL COMMENT '登入日期',
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'token',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_uid`(`uid` ASC) USING BTREE COMMENT 'get all record from a uid',
  INDEX `idx_token`(`token` ASC) USING BTREE COMMENT 'get me'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for group_link
-- ----------------------------
DROP TABLE IF EXISTS `group_link`;
CREATE TABLE `group_link`  (
  `id` bigint NOT NULL,
  `limit_time` datetime NULL DEFAULT NULL,
  `limit_number` int NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `request_approval` tinyint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for group_member
-- ----------------------------
DROP TABLE IF EXISTS `group_member`;
CREATE TABLE `group_member`  (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `group_id` bigint NOT NULL,
  `identity` tinyint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for group_table
-- ----------------------------
DROP TABLE IF EXISTS `group_table`;
CREATE TABLE `group_table`  (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '群聊昵稱',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '群聊簡介',
  `members` int NOT NULL DEFAULT 1 COMMENT '群聊人數',
  `member_id` bigint NOT NULL COMMENT '群聊uid集合id',
  `avatar` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '群聊頭像',
  `color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '顔色',
  `pinned_messages` json NULL COMMENT 'pinned message id list',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `id` bigint UNSIGNED NOT NULL,
  `uid` bigint NOT NULL COMMENT '用戶id',
  `position` tinyint NOT NULL COMMENT '用戶職位',
  `mute` date NULL DEFAULT NULL COMMENT '禁言時間',
  `group_id` bigint NOT NULL COMMENT '群組id',
  `last_read_time` datetime NULL DEFAULT NULL COMMENT '最後閲讀時間',
  `pin` tinyint NOT NULL DEFAULT 0 COMMENT '是否置頂',
  `notification` tinyint NOT NULL DEFAULT 0 COMMENT '是否不通知',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` bigint NOT NULL COMMENT '消息id',
  `read` tinyint NOT NULL DEFAULT 0 COMMENT '0未讀 1已讀',
  `sender` bigint NOT NULL COMMENT '消息發送者uid',
  `receive` bigint NULL DEFAULT NULL COMMENT '頻道id',
  `date` datetime NOT NULL COMMENT '消息發送時間',
  `reply` bigint NULL DEFAULT NULL COMMENT '回復的消息id',
  `text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文本消息',
  `link` json NULL COMMENT '链接消息',
  `sticker` json NULL COMMENT '表情消息',
  `photo` json NULL COMMENT '圖片消息',
  `video` json NULL COMMENT '影片消息',
  `music` json NULL COMMENT 'music',
  `file` json NULL COMMENT '文件消息',
  `voice` json NULL COMMENT '音頻訊息',
  `location` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '位置訊息',
  `is_system` tinyint NULL DEFAULT NULL COMMENT '是否是系統訊息',
  `is_file` tinyint NOT NULL DEFAULT 0 COMMENT '是否是文件',
  `is_photo` tinyint NOT NULL DEFAULT 0 COMMENT '是否是照片',
  `is_video` tinyint NOT NULL DEFAULT 0 COMMENT '是否是影片',
  `is_music` tinyint NOT NULL DEFAULT 0 COMMENT 'is music',
  `is_voice` tinyint NOT NULL DEFAULT 0 COMMENT '是否是语音',
  `is_link` tinyint NOT NULL DEFAULT 0 COMMENT '是否是链接',
  `reaction` json NULL COMMENT '交互记录',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_sender_receive_date`(`sender` ASC, `receive` ASC, `date` DESC) USING BTREE COMMENT 'query user\'s unread',
  INDEX `idx_date`(`receive` ASC, `date` DESC) USING BTREE COMMENT 'query group\'s unread'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for message_reaction
-- ----------------------------
DROP TABLE IF EXISTS `message_reaction`;
CREATE TABLE `message_reaction`  (
  `id` bigint NOT NULL,
  `message_id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `react_type` int NOT NULL,
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_message_id_uid`(`message_id` ASC, `uid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL COMMENT '用戶id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用戶名',
  `color` tinyint NOT NULL DEFAULT 0 COMMENT '颜色',
  `avatar` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '用戶頭像',
  `avatar_privacy` int NULL DEFAULT 1 COMMENT '頭像展示範圍',
  `first_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用戶昵稱',
  `last_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` bigint NOT NULL COMMENT '手機號',
  `phone_country` int NOT NULL COMMENT '手機號國家',
  `phone_privacy` int NOT NULL DEFAULT 1 COMMENT '手機號展示範圍',
  `date_of_birth` datetime NOT NULL COMMENT '出生日期',
  `date_of_birth_privacy` int NOT NULL DEFAULT 1 COMMENT '出生日期展示範圍',
  `last_seen` bigint NOT NULL COMMENT '在綫狀態   0->在綫  時間戳->last_seen',
  `last_seen_privacy` int NOT NULL DEFAULT 1 COMMENT '在綫狀態展示範圍',
  `bio` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'bio',
  `bio_privacy` int NOT NULL DEFAULT 1 COMMENT 'bio展示範圍',
  `log_email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登入郵箱',
  `calls_privacy` int UNSIGNED NOT NULL DEFAULT 1 COMMENT '打電話隱私',
  `voice_video_messages_privacy` int NOT NULL DEFAULT 1 COMMENT '音頻&視頻隱私',
  `messages_privacy` int NOT NULL DEFAULT 1 COMMENT '發消息隱私',
  `invites_privacy` int NOT NULL DEFAULT 1 COMMENT '群組邀請權限',
  `avatar_update_time` datetime NULL DEFAULT NULL COMMENT '更新头像时间',
  `official` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_phone_phone_country`(`phone` ASC, `phone_country` ASC) USING BTREE COMMENT 'locate by mobile number',
  INDEX `idx_username`(`username` ASC) USING BTREE COMMENT 'query by username'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
