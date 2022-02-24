package com.spurious.bilibili.service;


import com.mysql.cj.util.StringUtils;
import com.spurious.bilibili.dao.UserDao;
import com.spurious.bilibili.dao.domain.RefreshTokenDetail;
import com.spurious.bilibili.dao.domain.User;
import com.spurious.bilibili.dao.domain.UserInfo;
import com.spurious.bilibili.dao.domain.constant.UserConstant;
import com.spurious.bilibili.dao.domain.exception.ConditionException;
import com.spurious.bilibili.service.util.MD5Util;
import com.spurious.bilibili.service.util.RSAUtil;
import com.spurious.bilibili.service.util.TokenUtil;
import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {


    final private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    public void addUser(@NotNull User user) {
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("phone number cannot be empty!");
        }
        if (userDao.getUserByEmail(user.getEmail()) != null) {
            throw new ConditionException("email is existed!");
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
        return userDao.getUserByPhone(phone);
    }

    public String login(@NotNull User user) throws Exception {
        User dbUser = null;
        if (!StringUtils.isNullOrEmpty(user.getPhone())) {
            String phone = user.getPhone();
            dbUser = this.getUserByPhone(phone);
            if (dbUser == null) {
                throw new ConditionException("the phone number is not registered!");
            }
        } else if (!StringUtils.isNullOrEmpty(user.getEmail())) {
            String email = user.getEmail();
            dbUser = this.getUserByEmail(email);
            if (dbUser == null) {
                throw new ConditionException("the email is not registered!");
            }
        } else {
            throw new ConditionException("phone number and email are empty!");
        }


        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("Decrypt password failed!");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword,salt,"UTF-8");
        if (!md5Password.equals(dbUser.getPassword())) {
            throw new ConditionException("password error!");
        }
        return TokenUtil.generateToken(dbUser.getId());
    }

    private User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public User getUserInfo(Long userId) {
        User user = userDao.getUserById(userId);
        UserInfo userInfo = userDao.getUserInfoByUserId(userId);
        user.setUserInfo(userInfo);
        return user;
    }

    public void updateUserInfos(UserInfo userInfo) {
    }

    public void updateUsers(@NotNull User user) throws Exception {

        String email = user.getEmail();
        if (!StringUtils.isNullOrEmpty(email)) {
            if (userDao.getUserByEmail(email) != null) {
                throw new ConditionException("email is existed!");
            }
            userDao.updateEmailById(user.getId(),user.getEmail());
        }

        String phone = user.getPhone();
        if (!StringUtils.isNullOrEmpty(phone)) {
            if (userDao.getUserByPhone(phone) != null) {
                throw new ConditionException("phone number is existed!");
            }
            userDao.updatePhoneById(user.getId(),user.getPhone());
        }

        String password = user.getPassword();
        if (!StringUtils.isNullOrEmpty(password))
        {
            User dbUser = userDao.getUserById(user.getId());
            String salt = dbUser.getSalt();
            String md5Password = MD5Util.sign(password,salt,"UTF-8");
            userDao.updatePasswordById(user.getId(),md5Password);
        }
    }

    public String accessToken(String refreshToken) throws Exception {
        RefreshTokenDetail refreshTokenDetail = userDao.getRefreshTokenDetail(refreshToken);
        if (refreshToken == null) {
            throw new ConditionException("401","token expired!");
        }
        Long userId = refreshTokenDetail.getUserId();
        return TokenUtil.generateToken(userId);
    }

    public void logout(String refreshToken, Long userId) {
        userDao.deleteRefreshToken(refreshToken,userId);
    }

    public Map<String, Object> loginForDts(User user) throws Exception{
        String phone = user.getPhone() == null ? "" : user.getPhone();
        String email = user.getEmail() == null ? "" : user.getEmail();
        if (StringUtils.isNullOrEmpty(phone) && StringUtils.isNullOrEmpty(email)) {
            throw new ConditionException("phone number and email are empty !");
        }
        User dbUser = StringUtils.isNullOrEmpty(phone) ?
                userDao.getUserByPhone(email) : userDao.getUserByEmail(phone);
        if (dbUser == null) {
            throw new ConditionException("user does not exist !");
        }
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        }catch (Exception e) {
            throw new ConditionException("Decrypt password failed!");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword,salt,"UTF-8");
        if (!md5Password.equals(dbUser.getPassword())) {
            throw new ConditionException("password error!");
        }
        Long userId = dbUser.getId();
        String accessToken = TokenUtil.generateToken(userId);
        String refreshToken = TokenUtil.generateRefreshToken(userId);
        userDao.deleteRefreshTokenByUserId(userId);
        userDao.addRefreshToken(refreshToken,userId,new Date());
        Map<String, Object> result = new HashMap<>();
        result.put("accessToken",accessToken);
        result.put("refreshToken",refreshToken);
        return result;
    }

    public String getRefreshTokenByUserId(Long userId) {
        return userDao.getRefreshTokenByUserId(userId);
    }
}
