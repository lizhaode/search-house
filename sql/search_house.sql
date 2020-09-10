CREATE TABLE `house_info` (
	`id` int NOT NULL AUTO_INCREMENT,
	`house_name` varchar(20) NOT NULL COMMENT '小区名字',
	`price` varchar(8) DEFAULT NULL COMMENT '均价',
	`total_price` varchar(8) DEFAULT NULL COMMENT '起始总价',
	`max_area` varchar(5) NOT NULL COMMENT '小区户型最大面积',
	`min_area` varchar(5) NOT NULL COMMENT '小区户型最小面积',
	`room_scope` varchar(10) NOT NULL COMMENT '居室数量',
	`address` varchar(100) NOT NULL COMMENT '比较详细的地址',
	`street` varchar(30) NOT NULL COMMENT '行政街道',
	`district` varchar(20) NOT NULL COMMENT '行政区',
	`dev_company` varchar(100) DEFAULT NULL COMMENT '开发商, 可能会存在多个, 逗号区隔',
	`decoration` varchar(5) DEFAULT NULL COMMENT '装修类型',
	`sale_status` varchar(5) COMMENT '销售状态',
	`url` text DEFAULT NULL COMMENT '楼盘链接',
	`create_timestamp` timestamp NOT NULL DEFAULT current_timestamp,
	`update_timestamp` timestamp NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
	PRIMARY KEY (`id`),
	KEY `house_name` (`house_name`),
	KEY `district` (`district`),
	KEY `street` (`street`),
	KEY `price` (`price`),
	KEY `min_area` (`min_area`),
	KEY `sale_status` (`sale_status`)
) ENGINE = InnoDB CHARSET = utf8mb4;


CREATE TABLE `house_info_history` (
	`id` int NOT NULL AUTO_INCREMENT,
	`house_name` varchar(20) NOT NULL COMMENT '小区名字',
	`change_string` text DEFAULT NULL COMMENT '有更新的字段，json 格式',
	`create_timestamp` timestamp NOT NULL DEFAULT current_timestamp,
	`update_timestamp` timestamp NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
	PRIMARY KEY (`id`),
	KEY `house_name` (`house_name`)
) ENGINE = InnoDB CHARSET = utf8mb4;