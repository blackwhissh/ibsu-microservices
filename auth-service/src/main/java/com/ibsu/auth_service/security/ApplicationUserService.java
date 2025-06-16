package com.ibsu.auth_service.security;

import com.ibsu.auth_service.model.User;
import com.ibsu.common.exceptions.EntityNotFoundException;
import com.ibsu.common.exceptions.UserIsInactiveException;
import com.ibsu.auth_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;

@Service
public class ApplicationUserService implements UserDetailsService {
    private final UserRepository userRepository;

    public ApplicationUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ApplicationUser loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        if (userRepository.existsByUsername(username)) {
            user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
            if (user != null) {
                if (user.getDeactivationDate() != null && user.getDeactivationDate().plusMonths(1).plusDays(10).isBefore(LocalDate.now())) {
                    throw new UserIsInactiveException();
                }
                if (user.getIsActive()) {
                    return new ApplicationUser(
                            user.getId(), user.getUsername(), user.getPassword(),
                            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                    );
                }
            }
        }
        throw new UsernameNotFoundException(String.format("Username %s not found", username));
    }
}
