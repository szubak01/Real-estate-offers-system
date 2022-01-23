package com.example.application.data.enums;

import lombok.Getter;

@Getter
public enum Role {
    USER("user"),
    STUDENT("student"),
    ADMIN("admin");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

}
