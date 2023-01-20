package com.example.projekt_koncowy.user;

import com.example.projekt_koncowy.post.Post;
import com.example.projekt_koncowy.post.PostRepository;
import com.example.projekt_koncowy.post.PostService;
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

    private final PostService postService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,@Lazy PostService postService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.postService = postService;
    }

    //zebranie błędów walidacyjnych do listy
    public List<String> getErrorList(BindingResult bindingResult) {
        List<String> messages = new ArrayList<>();

        if (bindingResult.hasErrors())
            bindingResult.getFieldErrors().forEach(fieldError -> messages.add(fieldError.getDefaultMessage()));

        return messages;
    }
    public List<String> validation(BindingResult bindingResult) {

        return getErrorList(bindingResult);
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
    public void delete(User user){
        List<Post> postList = postService.findAllByUser(user);
        for(Post post : postList){
            postService.delete(post);
        }
        userRepository.delete(user);
    }
    public void changeUserPassword(User user, String password) {
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }



}
