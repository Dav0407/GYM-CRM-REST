package com.epam.gym_crm.service.impl;

import com.epam.gym_crm.dto.response.UserResponseDTO;
import com.epam.gym_crm.entity.User;
import com.epam.gym_crm.exception.InvalidUserCredentialException;
import com.epam.gym_crm.mapper.UserMapper;
import com.epam.gym_crm.repository.UserRepository;
import com.epam.gym_crm.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public User saveUser(User user) {
        LOG.info("Saving user: {}", user.getUsername());
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public boolean checkUsernameExists(String username) {
        boolean exists = userRepository.findByUsername(username).isPresent();
        LOG.info("Checking if username exists ({}): {}", username, exists);
        return exists;
    }

    @Override
    public User validateCredentials(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    LOG.warn("Username not found: {}", username);
                    return new InvalidUserCredentialException("Username or password is incorrect.");
                });

        if (!user.getPassword().equals(password)) {
            LOG.info("Password validation failed for user: {}", username);
            throw new InvalidUserCredentialException("Username or password is incorrect.");
        }

        return user;
    }

    @Transactional
    @Override
    public UserResponseDTO changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    LOG.warn("User not found: {}", username);
                    return new RuntimeException("User not found.");
                });

        if (!user.getPassword().equals(oldPassword)) {
            LOG.error("Old password does not match for {}", username);
            throw new IllegalArgumentException("Old password is incorrect.");
        }

        user.setPassword(newPassword);
        LOG.info("Password successfully changed for {}", username);

        return userMapper.toUserResponseDTO(user);
    }

    @Transactional
    @Override
    public String generateUsername(String firstName, String lastName) {
        if (!StringUtils.hasText(firstName) || !StringUtils.hasText(lastName)) {
            throw new IllegalArgumentException("First name and last name cannot be empty.");
        }

        LOG.info("Generating username for: {} {}", firstName, lastName);

        String baseUsername = firstName.trim().toLowerCase() + "." + lastName.trim().toLowerCase();
        String username = baseUsername;
        int suffix = 1;

        while (checkUsernameExists(username)) {
            LOG.warn("Username conflict: {} already exists. Trying next.", username);
            username = baseUsername + suffix;
            suffix++;
        }

        LOG.info("Generated unique username: {}", username);
        return username;
    }

    @Override
    public String generateRandomPassword() {
        String password = RANDOM.ints(10, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());

        LOG.info("Generated a random password.");
        return password;
    }

    @Override
    public void updateStatus(String username) {
        try {
            int updatedCount = userRepository.toggleStatus(username);

            if (updatedCount > 0) {
                LOG.info("Successfully toggled status for user: {}", username);
            } else {
                LOG.warn("No user found with username: {}", username);
                throw new EntityNotFoundException("User not found with username: " + username);
            }
        } catch (Exception e) {
            LOG.error("Error toggling status for user: {}", username, e);
            throw new ServiceException("Failed to toggle user status", e);
        }
    }

    @Override
    public void deleteUser(String username) {
        userRepository.findByUsername(username).ifPresentOrElse(user -> {
            userRepository.deleteByUsername(username);
            LOG.info("User deleted successfully: {}", username);
        }, () -> {
            LOG.warn("Attempted to delete non-existent user: {}", username);
            throw new RuntimeException("User not found.");
        });
    }


    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found."));
    }

    @Override
    public UserResponseDTO login(String username, String password) {

        User user = validateCredentials(username, password);

        return userMapper.toUserResponseDTO(user);
    }


}
