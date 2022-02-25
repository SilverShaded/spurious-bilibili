package com.spurious.bilibili.service;

import com.spurious.bilibili.dao.FollowingGroupDao;
import com.spurious.bilibili.dao.domain.FollowingGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowingGroupService {

    final private FollowingGroupDao followingGroupDao;

    @Autowired
    public FollowingGroupService(FollowingGroupDao followingGroupDao) {
        this.followingGroupDao = followingGroupDao;
    }

    public FollowingGroup getByType(String type) {
        return followingGroupDao.getByType(type);
    }

    public FollowingGroup getById(Long id) {
        return followingGroupDao.getById(id);
    }
}
