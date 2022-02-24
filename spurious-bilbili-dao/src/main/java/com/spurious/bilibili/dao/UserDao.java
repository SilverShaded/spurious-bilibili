package com.spurious.bilibili.dao;

import com.spurious.bilibili.dao.domain.RefreshTokenDetail;
import com.spurious.bilibili.dao.domain.User;
import com.spurious.bilibili.dao.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Mapper
public interface UserDao {


    User getUserByPhone(String phone);

    Integer addUser(User user);

    Integer addUserInfo(UserInfo userInfo);

    User getUserById(Long id);

    UserInfo getUserInfoByUserId(Long userId);

    void updatePhoneById(Long id, String phone);

    void updateEmailById(Long id, String email);

    void updatePasswordById(Long id, String password);

    User getUserByEmail(String email);

    RefreshTokenDetail getRefreshTokenDetail(String refreshToken);

    void deleteRefreshToken(String refreshToken, Long userId);

    void deleteRefreshTokenByUserId(Long userId);

    void addRefreshToken(String refreshToken, Long userId, Date createTime);

    String getRefreshTokenByUserId(Long userId);
}
