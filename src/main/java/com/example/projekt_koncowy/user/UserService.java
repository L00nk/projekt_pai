package com.example.projekt_koncowy.user;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final static String USER_NOT_FOUND_MSG = "User not found";

    public UserService(UserRepository userRepository, @Lazy BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //zebranie błędów walidacyjnych do listy
    public List<String> getErrorList(BindingResult bindingResult) {
        List<String> messages = new ArrayList<>();

        if (bindingResult.hasErrors())
            bindingResult.getFieldErrors().forEach(fieldError -> messages.add(fieldError.getDefaultMessage()));

        return messages;
    }

    public Optional<User> findCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserLogin = authentication.getName();

        return userRepository.findByLogin(currentUserLogin);
    }
    public Boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }
    public Boolean existsByEmail(String email) {
        return userRepository.existsByLogin(email);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

}
