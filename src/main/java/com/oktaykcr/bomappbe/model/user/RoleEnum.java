package com.oktaykcr.bomappbe.model.user;

import lombok.Getter;
import lombok.Setter;

public enum RoleEnum {
    USER("User"), ADMIN("Admin");

    RoleEnum(String role) {
        this.role = role;
    }

    @Getter
    @Setter
    private String role;
}
