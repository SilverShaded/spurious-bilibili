package com.spurious.bilibili.service;

import com.spurious.bilibili.dao.UserDao;
import com.spurious.bilibili.dao.UserFollowingDao;
import com.spurious.bilibili.dao.domain.FollowingGroup;
import com.spurious.bilibili.dao.domain.User;
import com.spurious.bilibili.dao.domain.UserFollowing;
import com.spurious.bilibili.dao.domain.constant.UserConstant;
import com.spurious.bilibili.dao.domain.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserFollowingService {

    final private UserFollowingDao userFollowingDao;

    final private FollowingGroupService followingGroupService;

    final private UserService userService;

    @Autowired
    public UserFollowingService(UserFollowingDao userFollowingDao,
                                FollowingGroupService followingGroupService,
                                UserService userService) {
        this.userFollowingDao = userFollowingDao;
        this.followingGroupService = followingGroupService;
        this.userService = userService;
    }

    @Transactional
    public void addUserFollowings(UserFollowing userFollowing) {
        Long groupId = userFollowing.getGroupId();
        FollowingGroup followingGroup;
        if (groupId == null) {
            followingGroup =
                    followingGroupService.getByType(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT);
            userFollowing.setGroupId(followingGroup.getId());
        }else {
            followingGroup = followingGroupService.getById(groupId);
        }
        if (followingGroup == null) {
            throw new ConditionException("Following group does not exist！");
        }
        Long followingId = userFollowing.getFollowingId();
        User user = userService.getUserById(followingId);
        if (user == null) {
            throw new ConditionException("Following user does not exist!");
        }
        userFollowingDao.deleteUserFollowing(userFollowing.getUserId(),followingId);
        userFollowing.setCreateTime(new Date());
        userFollowingDao.addUserFollowing(userFollowing);



    }
}
