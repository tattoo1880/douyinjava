```mysql

CREATE TABLE `t_user` (
                          `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                          `uid` VARCHAR(50) NOT NULL COMMENT '用户唯一标识',
                          `short_id` VARCHAR(50) DEFAULT NULL COMMENT '短ID',
                          `nickname` VARCHAR(255) DEFAULT NULL COMMENT '昵称',
                          UNIQUE KEY `uniq_uid` (`uid`),
                          INDEX `idx_short_id` (`short_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';




CREATE TABLE `t_content`(
                            `id` varchar(50) PRIMARY KEY COMMENT '主键ID',
                            `room_id` varchar(50) NOT NULL COMMENT '房间ID',
                            `uid` varchar(50) NOT NULL COMMENT '用户ID',
                            `nickname` varchar(255) NOT NULL COMMENT '昵称',
                            `content` TEXT COMMENT '内容',
                            `create_time` DATETIME COMMENT '创建时间',
                            INDEX `idx_room_id` (`room_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容表';


```