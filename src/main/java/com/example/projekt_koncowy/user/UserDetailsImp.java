package com.example.projekt_koncowy.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsImp implements UserDetailsService {
    private final UserRepository userRepository;
    private final static String USER_NOT_FOUND_MSG = "User not found";

    public UserDetailsImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,login)));
        return UserPrinciple.build(user);
    }
}
