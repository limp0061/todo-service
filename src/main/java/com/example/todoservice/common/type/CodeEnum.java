package com.example.todoservice.common.type;

public interface CodeEnum {

    String getCode();

    default String getDesc() {
        return "";
    }
}
