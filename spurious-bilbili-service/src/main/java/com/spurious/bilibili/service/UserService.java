package com.spurious.bilibili.service;


import com.mysql.cj.util.StringUtils;
import com.spurious.bilibili.dao.UserDao;
import com.spurious.bilibili.dao.domain.User;
import com.spurious.bilibili.dao.domain.UserInfo;
import com.spurious.bilibili.dao.domain.constant.UserConstant;
import com.spurious.bilibili.dao.domain.exception.ConditionException;
import com.spurious.bilibili.service.util.MD5Util;
import com.spurious.bilibili.service.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired(required = false)
    private UserDao userDao;

    public void addUser(User user) {
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("phone number cannot be empty!");
        }
        User dbUser = this.getUserByPhone(phone);
        if (dbUser != null) {
            throw new ConditionException("the phone number has been registered!");
        }
        Date now = new Date();
        String salt = String.valueOf(now.getTime());
        //Password is encrypted by RSA
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("Decrypt password failed!");
        }
        String md5Password = MD5Util.sign(rawPassword,salt,"UTF-8");
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setCreateTime(now);
        userDao.addUser(user);
        //add user_info
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userInfo.setCreateTime(now);
        userDao.addUserInfo(userInfo);
    }

    public User getUserByPhone(String phone) {
        return userDao.geyUserByPhone(phone);
    }
}
