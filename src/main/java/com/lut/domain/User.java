package com.lut.domain;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String userId;
    private String name;
    private String username;
    private String password;
    private String sex;
    private String department;
    private Date createTime;
    private Date updateTime;
    private String userType;
    private String delFlag;
}
