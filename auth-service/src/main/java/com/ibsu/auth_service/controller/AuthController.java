package com.ibsu.auth_service.controller;

import com.ibsu.auth_service.dto.UserDTO;
import com.ibsu.auth_service.dto.request.LoginRequest;
import com.ibsu.auth_service.dto.request.RegisterRequest;
import com.ibsu.auth_service.dto.request.TokenRefreshRequest;
import com.ibsu.auth_service.dto.response.JwtResponse;
import com.ibsu.auth_service.dto.response.MessageResponse;
import com.ibsu.auth_service.dto.response.TokenRefreshResponse;
import com.ibsu.auth_service.model.RefreshToken;
import com.ibsu.auth_service.model.User;
import com.ibsu.auth_service.repository.UserRepository;
import com.ibsu.auth_service.security.jwt.JwtService;
import com.ibsu.auth_service.security.jwt.RefreshTokenService;
import com.ibsu.auth_service.service.AuthService;
import com.ibsu.common.dto.EditUserRequest;
import com.ibsu.common.exceptions.RefreshTokenNotFoundException;
import com.ibsu.common.exceptions.UserNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService, UserRepository userRepository) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request.username(), request.password(), request.email(), request.phone(), request.firstName(), request.lastName()));
    }


    @PostMapping("/edit")
    public ResponseEntity<?> editCurrentUser(@RequestBody EditUserRequest request) {
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return ResponseEntity.ok(authService.editUser(request.firstName(), request.lastName(), request.phone(), request.password(), request.newPassword(), request.repeatPassword(), userId));
    }


    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser() {
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return ResponseEntity.ok(authService.getUser(userId));
    }




    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request.username(), request.password()));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateToken(user.getUsername(), user.getId(), user.getRole());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(RefreshTokenNotFoundException::new);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                .orElseThrow(UserNotFoundException::new);
        Long userId = user.getId();
        refreshTokenService.deleteByUserId(userId);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }

    @PostMapping("/admin/deactivate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deactivateUser(@RequestParam Long userId) {
        authService.deactivateUser(userId);
        return ResponseEntity.ok(new MessageResponse("User deactivated successfully!"));
    }

    @PostMapping("/admin/activate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> activateUser(@RequestParam Long userId) {
        authService.activateUser(userId);
        return ResponseEntity.ok(new MessageResponse("User activated successfully!"));
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(@PageableDefault(page = 0, size = 9, sort = "registrationDate", direction = Sort.Direction.DESC)
                                         Pageable pageable) {
        return ResponseEntity.ok(userRepository.findAll(pageable));
    }

    @GetMapping("/admin/users/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
    }

    @PostMapping("/admin/edit/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> editUserByAdmin(@RequestBody EditUserRequest request, @PathVariable Long userId) {
        return ResponseEntity.ok(authService.editUser(request.firstName(), request.lastName(), request.phone(), request.password(), request.newPassword(), request.repeatPassword(), userId));
    }

    @DeleteMapping("/admin/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@RequestParam Long userId) {
        authService.deleteUser(userId);
        return ResponseEntity.ok().body(new MessageResponse("User deleted successfully!"));
    }
}
