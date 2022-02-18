package com.spurious.bilibili.dao;

import com.spurious.bilibili.dao.domain.User;
import com.spurious.bilibili.dao.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {


    User geyUserByPhone(String phone);

    Integer addUser(User user);

    Integer addUserInfo(UserInfo userInfo);
}
