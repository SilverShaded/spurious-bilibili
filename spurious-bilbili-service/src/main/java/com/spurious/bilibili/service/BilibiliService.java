package com.spurious.bilibili.service;

import com.spurious.bilibili.dao.BilibiliDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BilibiliService {

    @Autowired(required = false)
    private BilibiliDao bilibiliDao;

    public Map<String,Object> query(Long id) {
        return bilibiliDao.query(id);
    }
}
