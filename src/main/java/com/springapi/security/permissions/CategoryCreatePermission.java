package com.springapi.security.permissions;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value=RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('category.create')")
public @interface CategoryCreatePermission {
}
