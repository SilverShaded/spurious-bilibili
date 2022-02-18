package com.spurious.bilibili.api;

import com.spurious.bilibili.dao.domain.JsonResponse;
import com.spurious.bilibili.dao.domain.User;
import com.spurious.bilibili.service.UserService;
import com.spurious.bilibili.service.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApi {

    @Autowired
    private UserService userService;

    @GetMapping("/rsa-pk")
    public JsonResponse<String> getRsaPublicKey() {
        String publicKey = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(publicKey);
    }

    @PostMapping("/register")
    public JsonResponse<String> registerUser(@RequestBody User user) {
        userService.addUser(user);
        return JsonResponse.success();
    }
}
