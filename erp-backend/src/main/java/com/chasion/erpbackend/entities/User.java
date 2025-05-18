package com.chasion.erpbackend.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "jsh_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String salt;

    @Column(length = 50)
    private String password;

    @Column(name = "leader_flag", length = 1)
    private String leaderFlag = "0";

    private String position;

    private String department;

    private String email;

    private String phonenum;

    @Column(nullable = false)
    private String avatar;

    @Column(name = "ismanager", nullable = false)
    private Integer ismanager = 1;

    @Column(name = "isystem", nullable = false)
    private Integer isystem = 0;

    private Integer status = 0;

    private String description;

    private String remark;

    @Column(name = "weixin_open_id")
    private String weixinOpenId;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "delete_flag", length = 1)
    private String deleteFlag = "0";

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    // 构造方法、Getter和Setter略
    // 为简化代码，这里省略了构造方法、Getter和Setter
    // 实际使用时需要添加完整
}
