package com.spurious.bilibili.dao;

import com.spurious.bilibili.dao.domain.FollowingGroup;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface FollowingGroupDao {

    FollowingGroup getByType(String type);

    FollowingGroup getById(Long id);
}
