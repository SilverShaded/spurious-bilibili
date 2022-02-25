package com.spurious.bilibili.dao.domain;

import lombok.Data;

import java.util.Date;

@Data
public class FollowingGroup {

    private Long id;

    private Long userId;

    private String name;

    private String type;

    private Date createTime;

    private Date updateTime;
}
