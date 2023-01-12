package com.example.projekt_koncowy.post;

import com.example.projekt_koncowy.user.User;
import com.example.projekt_koncowy.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final static String USER_NOT_FOUND_MSG = "Użytkownik nie znaleziony";

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<?> addPost(@Valid @RequestBody Post post){
        return ResponseEntity.ok(postService.save(post));
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllPosts(){
        return ResponseEntity.ok(postService.findAll());
    }
    @GetMapping("/get-all-user")
    public ResponseEntity<?> getAllUserPosts(){
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(()->new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG)));
        return ResponseEntity.ok(postService.findAllByUser(currentLoggedInUser));
    }


}
