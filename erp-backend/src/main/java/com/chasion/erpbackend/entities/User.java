package com.chasion.erpbackend.entities;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class User {

    private Long id;


    private String username;


    private String salt;


    private String password;


    private String leaderFlag = "0";
// 职务
    private String position;

    private String department;

    private String email;

    private String phonenum;


    private String avatar;


    private Integer ismanager = 1;


    private Integer isystem = 0;

    private Integer status = 0;

    private String description;

    private String remark;


    private String weixinOpenId;


    private Long tenantId;


    private String deleteFlag = "0";


    private LocalDateTime updateTime;


    private LocalDateTime createTime;

}
