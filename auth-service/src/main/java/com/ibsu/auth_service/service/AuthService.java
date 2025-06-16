package com.ibsu.auth_service.service;

import com.ibsu.auth_service.dto.UserDTO;
import com.ibsu.auth_service.dto.response.JwtResponse;
import com.ibsu.auth_service.kafka.UserCreatedEventProducer;
import com.ibsu.auth_service.kafka.UserDeactivatedEventProducer;
import com.ibsu.auth_service.kafka.UserEditedEventProducer;
import com.ibsu.auth_service.model.RefreshToken;
import com.ibsu.auth_service.model.User;
import com.ibsu.auth_service.repository.UserRepository;
import com.ibsu.auth_service.security.ApplicationUser;
import com.ibsu.auth_service.security.jwt.JwtService;
import com.ibsu.auth_service.security.jwt.RefreshTokenService;
import com.ibsu.common.enums.RoleEnum;
import com.ibsu.common.event.UserCreatedEvent;
import com.ibsu.common.event.UserDeactivatedEvent;
import com.ibsu.common.event.UserEditedEvent;
import com.ibsu.common.exceptions.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserCreatedEventProducer userCreatedEventProducer;
    private final UserDeactivatedEventProducer userDeactivatedEventProducer;
    private final UserEditedEventProducer userEditedEventProducer;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenService refreshTokenService, PasswordEncoder passwordEncoder, UserCreatedEventProducer userCreatedEventProducer, UserDeactivatedEventProducer userDeactivatedEventProducer, UserEditedEventProducer userEditedEventProducer) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
        this.userCreatedEventProducer = userCreatedEventProducer;
        this.userDeactivatedEventProducer = userDeactivatedEventProducer;
        this.userEditedEventProducer = userEditedEventProducer;
    }

    @Transactional
    public JwtResponse register(String username, String password, String email, String phone, String firstName, String lastName) {
        if (userRepository.existsByUsername(username)) {
            LOGGER.error("User with this username already exists");
            throw new UserAlreadyExistsException();
        }
        if (userRepository.existsByEmail(email)) {
            LOGGER.error("User with this email already exists");
            throw new EmailAlreadyExistsException();
        }

        RoleEnum roleEnum = RoleEnum.USER;

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User(username, hashedPassword, roleEnum, email, phone, firstName, lastName, LocalDate.now(), true);

        User save = userRepository.save(user);

        UserCreatedEvent event = new UserCreatedEvent(
                save.getId(),
                save.getEmail(),
                save.getFirstName(),
                save.getLastName(),
                save.getPhone()
        );

        userCreatedEventProducer.sendUserCreatedEvent(event);

        JwtResponse response = authenticate(username, password, user);
        LOGGER.info("User registered successfully");
        return response;
    }

    @Transactional
    public JwtResponse login(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        refreshTokenService.deleteByUserId(user.getId());
        if (user.getDeactivationDate() != null && user.getDeactivationDate().plusMonths(1).plusDays(10).isBefore(LocalDate.now())) {
            throw new UserIsInactiveException();
        }
        if (!user.getIsActive() && user.getDeactivationDate() == null) {
            throw new UserIsInactiveException();
        }
        return authenticate(username, password, user);
    }

    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setIsActive(false);
        user.setDeactivationDate(LocalDate.now());

        UserDeactivatedEvent event = new UserDeactivatedEvent(userId);
        userDeactivatedEventProducer.sendUserDeactivatedEvent(event);
        userRepository.save(user);
    }

    public UserDTO editUser(Optional<String> firstName, Optional<String> lastName, Optional<String> phone,
                            Optional<String> password, Optional<String> newPassword, Optional<String> repeatPassword, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        firstName.ifPresent(user::setFirstName);
        lastName.ifPresent(user::setLastName);
        phone.ifPresent(user::setPhone);
        if (password.isPresent() && newPassword.isPresent() && repeatPassword.isPresent()) {
            changePassword(user, password.get(), newPassword.get(), repeatPassword.get());
        }
        if (firstName.isPresent() || lastName.isPresent()) {
            UserEditedEvent event = new UserEditedEvent(userId, firstName, lastName);
            userEditedEventProducer.sendUserEditedEvent(event);
        }
        return getUserDTO(userRepository.save(user));
    }

    public UserDTO getUser(Long userId) {
        return getUserDTO(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
    }

    private UserDTO getUserDTO(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getFirstName(),
                user.getLastName()
        );
    }


    private void changePassword(User user, String currentPassword, String newPassword, String repeatNewPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new WrongPasswordException();
        }
        if (!newPassword.equals(repeatNewPassword)) {
            throw new WrongPasswordException();
        }
        user.setPassword(passwordEncoder.encode(newPassword));
    }

    private JwtResponse authenticate(String username, String password, User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ApplicationUser applicationUser = (ApplicationUser) authentication.getPrincipal();

        String jwt = jwtService.generateToken(applicationUser.getUsername(), user.getId(), user.getRole());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return new JwtResponse(jwt, refreshToken.getToken(), applicationUser.getId(), applicationUser.getUsername(), applicationUser.getAuthorities().stream().findFirst().get().toString());
    }



}
