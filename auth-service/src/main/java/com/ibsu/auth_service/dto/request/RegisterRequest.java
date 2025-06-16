package com.ibsu.auth_service.dto.request;

public record RegisterRequest(String username, String password, String email,
                              String phone, String firstName, String lastName) {
}
