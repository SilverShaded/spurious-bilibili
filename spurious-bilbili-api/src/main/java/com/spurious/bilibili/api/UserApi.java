package com.spurious.bilibili.api;

import com.spurious.bilibili.api.support.UserSupport;
import com.spurious.bilibili.dao.domain.JsonResponse;
import com.spurious.bilibili.dao.domain.User;
import com.spurious.bilibili.service.UserService;
import com.spurious.bilibili.service.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class UserApi {

    final private UserService userService;

    final private UserSupport userSupport;

    @Autowired
    public UserApi(UserService userService, UserSupport userSupport) {
        this.userService = userService;
        this.userSupport = userSupport;
    }

    @GetMapping("/users")
    public JsonResponse<User> getUser() {
        Long userId = userSupport.getCurrentUserId();
        User user = userService.getUserInfo(userId);
        return new JsonResponse<>(user);
    }


    @GetMapping("/rsa-pks")
    public JsonResponse<String> getRsaPublicKey() {
        String publicKey = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(publicKey);
    }

    @PostMapping("/users")
    public JsonResponse<String> registerUser(@RequestBody User user) {
        userService.addUser(user);
        return JsonResponse.success();
    }
    
    @PostMapping("/user-tokens")
    public JsonResponse<String> login(@RequestBody User user) throws Exception{
        String token = userService.login(user);
        return new JsonResponse<>(token);
    }

    @PostMapping("/user-dts")
    public JsonResponse<Map<String,Object>> loginForDts(@RequestBody User user)
            throws Exception {
        Map<String,Object> map = userService.loginForDts(user);
        return new JsonResponse<>(map);
    }

    @PostMapping("/access-tokens")
    public JsonResponse<String> accessTokens(HttpServletRequest request) throws Exception {
        String refreshToken = request.getHeader("refreshToken");
        String accessToken = userService.accessToken(refreshToken);
        return new JsonResponse<>(accessToken);
    }

    @DeleteMapping("/refresh-tokens")
    public JsonResponse<String> logout(HttpServletRequest request) {
        String refreshToken = request.getHeader("refreshToken");
        Long userId = userSupport.getCurrentUserId();
        userService.logout(refreshToken,userId);
        return JsonResponse.success();
    }

    @PutMapping("/users")
    public JsonResponse<String> updateUsers(@RequestBody User user) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);
        userService.updateUsers(user);
        return JsonResponse.success();
    }

//    @PutMapping("/user-infos")
//    public JsonResponse<String> updateUserInfos(@RequestBody UserInfo userInfo) {
//        Long userId = userSupport.getCurrentUserId();
//        userInfo.setUserId(userId);
//        userService.updateUserInfos(userInfo);
//    }


    
}
