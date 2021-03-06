package com.spurious.bilibili.dao.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class JsonResponse<T> {

    private String code;

    private String msg;

    private T data;

    public JsonResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResponse(T data) {
        this.data = data;
        msg = "success!";
        code = "0";
    }

    public static JsonResponse<String> success() {
        return new JsonResponse<>(null);
    }

    public static JsonResponse<String> success(String data) {
        return new JsonResponse<>(data);
    }

    public static JsonResponse<String> fail() {
        return new JsonResponse<>("1","fail!");
    }

    public static JsonResponse<String> fail(String code, String msg) {
        return new JsonResponse<>(code,msg);
    }
}
