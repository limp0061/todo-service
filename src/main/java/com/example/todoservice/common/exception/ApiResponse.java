package com.example.todoservice.common.exception;

public record ApiResponse<T>(
        boolean success,
        T data,
        String message,
        String code
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "SUCCESS", null);
    }

    public static ApiResponse<Void> error(ErrorCode errorCode) {
        return new ApiResponse<>(false, null, errorCode.getMessage(), errorCode.name());
    }

    public static <T> ApiResponse<T> error(T data, ErrorCode errorCode) {
        return new ApiResponse<>(false, data, errorCode.getMessage(), errorCode.name());
    }
}
