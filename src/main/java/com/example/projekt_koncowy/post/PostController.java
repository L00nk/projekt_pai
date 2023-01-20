package com.example.projekt_koncowy.post;

import com.example.projekt_koncowy.user.User;
import com.example.projekt_koncowy.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final static String USER_NOT_FOUND_MSG = "Użytkownik nie znaleziony";
    private final static String POST_NOT_FOUND_MSG = "Post o podanym id nie istnieje";
    private final static String USER_NOT_OWNER_MSG = "Nie jesteś twórcą posta";

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<?> addPost(@Valid @RequestBody Post post,BindingResult bindingResult ){
        if (postService.getErrorList(bindingResult).size() != 0)
            return new ResponseEntity<>(postService.getErrorList(bindingResult), HttpStatus.BAD_REQUEST);
        LocalDate date = LocalDate.now();
        post.setDate(date);
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
    @GetMapping("/filter-date/{date}")
    public ResponseEntity<?> getAllPostsByDate(@PathVariable String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate newDate = LocalDate.parse(date,formatter);
        return ResponseEntity.ok(postService.findAllByDate(newDate));
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editPost(@PathVariable int id, @RequestBody Post post, BindingResult bindingResult){
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(()->new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG)));
        Post editPost = postService.findById(id).orElseThrow(()->new RuntimeException(String.format(POST_NOT_FOUND_MSG)));

        if(editPost.getUser() != currentLoggedInUser)
            return new ResponseEntity<>(USER_NOT_OWNER_MSG, HttpStatus.UNAUTHORIZED);

        if (postService.getErrorList(bindingResult).size() != 0)
            return new ResponseEntity<>(postService.getErrorList(bindingResult), HttpStatus.BAD_REQUEST);

        return ResponseEntity.ok(postService.edit(editPost, post));

    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id){
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(()->new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG)));
        Post deletePost = postService.findById(id).orElseThrow(()->new RuntimeException(String.format(POST_NOT_FOUND_MSG)));
        if(deletePost.getUser() != currentLoggedInUser){
            return new ResponseEntity<>(USER_NOT_OWNER_MSG, HttpStatus.UNAUTHORIZED);
        }
        else
        {
            postService.delete(deletePost);
            return ResponseEntity.ok("Post with id "+id+" deleted succesfully");
        }
    }


}
