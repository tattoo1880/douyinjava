```mysql

CREATE TABLE `t_user` (
                          `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                          `uid` VARCHAR(50) NOT NULL COMMENT '用户唯一标识',
                          `short_id` VARCHAR(50) DEFAULT NULL COMMENT '短ID',
                          `nickname` VARCHAR(255) DEFAULT NULL COMMENT '昵称',
                          UNIQUE KEY `uniq_uid` (`uid`),
                          INDEX `idx_short_id` (`short_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```