-- ----------------------------
-- Table structure for jsh_user
-- ----------------------------
DROP TABLE IF EXISTS `jsh_user`;
CREATE TABLE `jsh_user` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                            `username` varchar(255) NOT NULL COMMENT '用户姓名--例如张三',
                            `salt` varchar(255) NOT NULL COMMENT '密码加盐',
                            `password` varchar(50) DEFAULT NULL COMMENT '登陆密码',
                            `leader_flag` varchar(1) DEFAULT '0' COMMENT '是否经理，0否，1是',
                            `position` varchar(200) DEFAULT NULL COMMENT '职位',
                            `department` varchar(255) DEFAULT NULL COMMENT '所属部门',
                            `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
                            `phonenum` varchar(100) DEFAULT NULL COMMENT '手机号码',
                            `avatar` varchar(255) NOT NULL COMMENT '头像地址',
                            `ismanager` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否为管理者 0==管理者 1==员工',
                            `isystem` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否系统自带数据 ',
                            `status` tinyint(4) DEFAULT '0' COMMENT '状态，0正常，2封禁',
                            `description` varchar(500) DEFAULT NULL COMMENT '用户描述信息',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            `weixin_open_id` varchar(100) DEFAULT NULL COMMENT '微信绑定',
                            `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户id',
                            `delete_flag` varchar(1) DEFAULT '0' COMMENT '删除标记，0未删除，1删除',
                            `update_time` DATE NOT NULL COMMENT '更新时间',
                            `create_time` DATE NOT NULL COMMENT '创建时间',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of jsh_user
-- ----------------------------
INSERT INTO `jsh_user`
(`username`, `salt`, `password`, `leader_flag`, `position`, `department`, `email`, `phonenum`, `avatar`, `ismanager`, `isystem`, `status`, `description`, `remark`, `weixin_open_id`, `tenant_id`, `delete_flag`, `update_time`, `create_time`)
VALUES
    ('张三', 'abc123', 'e10adc3949ba59abbe56e057f20f883e', '0', '软件工程师', '技术部', 'zhangsan@example.com', '13800138000', '/avatar/zhangsan.png', 1, 0, 0, '专注于后端开发', '优秀员工', 'wx123456', 1, '0', '2023-05-18', '2023-05-18'),
    ('李四', 'def456', 'e10adc3949ba59abbe56e057f20f883e', '1', '项目经理', '技术部', 'lisi@example.com', '13900139000', '/avatar/lisi.png', 0, 0, 0, '负责项目管理与协调', '经验丰富', 'wx234567', 1, '0', '2023-05-18', '2023-05-18'),
    ('王五', 'ghi789', 'e10adc3949ba59abbe56e057f20f883e', '0', 'UI设计师', '设计部', 'wangwu@example.com', '13700137000', '/avatar/wangwu.png', 1, 0, 0, '负责产品UI设计', '创意丰富', 'wx345678', 1, '0', '2023-05-18', '2023-05-18');

