package com.spurious.bilibili.api.support;

import com.spurious.bilibili.dao.domain.exception.ConditionException;
import com.spurious.bilibili.service.UserService;
import com.spurious.bilibili.service.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Component
public class UserSupport {

    final private UserService userService;

    @Autowired
    public UserSupport(UserService userService) {
        this.userService = userService;
    }

    public Long getCurrentUserId() {

        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = requestAttributes.getRequest().getHeader("token");
        Long userId = TokenUtil.verifyToken(token);
        if (userId < 0) {
            throw new ConditionException("Illegal user");
        }
        this.verifyRefreshToken(userId);
        return userId;
    }

    private void verifyRefreshToken(Long userId) {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String refreshToken = requestAttributes.getRequest().getHeader("refreshToken");
        String dbRefreshToken = userService.getRefreshTokenByUserId(userId);
        if (!dbRefreshToken.equals(refreshToken)) {
            throw new ConditionException("refresh token error!");
        }
    }
}
