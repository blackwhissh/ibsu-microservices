package com.ibsu.common.dto;

import java.util.Optional;

public record EditUserRequest(Optional<String> firstName, Optional<String> lastName, Optional<String> phone,
                              Optional<String> password, Optional<String> newPassword, Optional<String> repeatPassword) {
}
