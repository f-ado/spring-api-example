package com.springapi.security.permissions;

public enum CategoryPermissions {
    CREATE("category.create"),
    READ("category.read"),
    UPDATE("category.update"),
    DELETE("category.delete");

    private String value;

    CategoryPermissions(String permission) {
        this.value = permission;
    }

    public String getValue() {
        return this.value;
    }
}
