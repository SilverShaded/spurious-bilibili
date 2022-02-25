package com.spurious.bilibili.dao;

import com.spurious.bilibili.dao.domain.UserFollowing;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface UserFollowingDao {

    void deleteUserFollowing(Long userId, Long followingId);

    void addUserFollowing(UserFollowing userFollowing);
}
