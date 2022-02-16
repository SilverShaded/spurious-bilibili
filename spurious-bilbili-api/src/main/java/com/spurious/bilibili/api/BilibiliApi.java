package com.spurious.bilibili.api;

import com.spurious.bilibili.service.BilibiliService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BilibiliApi {

    @Autowired
    private BilibiliService bilibiliService;

    @GetMapping("/query")
    public Map<String,Object> query(Long id) {
        return bilibiliService.query(id);
    }


}
