package com.online.yugaofan.domain.base;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author:  dave
 * @date: 2021/6/9
 * 
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class BaseResponse<T> {

    private Integer code;

    private String message;

    private T data;

    public BaseResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    @NonNull
    public static <T> BaseResponse<T> ok(@Nullable String message, @Nullable T data) {
        return new BaseResponse<>(HttpStatus.OK.value(), message, data);
    }


    @NonNull
    public static <T> BaseResponse<T> ok(@Nullable String message) {
        return ok(message, null);
    }


    public static <T> BaseResponse<T> ok(@Nullable T data) {
        return new BaseResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    @NonNull
    public static <T> BaseResponse<T> error(@Nullable String message, @Nullable T data) {
        return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, data);
    }

    @NonNull
    public static <T> BaseResponse<T> error(@Nullable String message) {
        return error(message, null);
    }


    public static <T> BaseResponse<T> error(@Nullable T data) {
        return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

}
