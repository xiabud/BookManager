package com.xiecode.entity;


import lombok.Data;

@Data
public class AuthUser {
    private String username;
    private String password;
    private String role;
}
