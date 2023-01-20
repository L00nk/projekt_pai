package com.example.projekt_koncowy.comment;

import com.example.projekt_koncowy.post.Post;
import com.example.projekt_koncowy.post.PostService;
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

@RestController
@AllArgsConstructor
@RequestMapping(path = "/comment")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final PostService postService;
    private final static String POST_NOT_FOUND_MSG = "Post nie znaleziony";
    private final static String COMMENT_NOT_FOUND_MSG = "Komentarz nie znaleziony";
    private final static String USER_NOT_OWNER_MSG = "Nie jesteś twórcą komentarza";
    private final static String USER_NOT_FOUND_MSG = "Użytkownik nie znaleziony";

    @Transactional
    @PostMapping("/add/{post}")
    public ResponseEntity<?> addComment(@Valid @RequestBody Comment comment, @PathVariable int post, BindingResult bindingResult ){
        Post currentPost = postService.findById(post).orElseThrow(()->new UsernameNotFoundException(String.format(POST_NOT_FOUND_MSG)));
        return ResponseEntity.ok(commentService.save(comment,currentPost));
    }
    @GetMapping("/get-all-comments/{post}")
    public ResponseEntity<?> getAllComments(@PathVariable int post){
        Post currentPost = postService.findById(post).orElseThrow(()->new UsernameNotFoundException(String.format(POST_NOT_FOUND_MSG)));
        return ResponseEntity.ok(commentService.findAllByPost(currentPost));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable int id){
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(()->new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG)));
        Comment deleteComment = commentService.findById(id).orElseThrow(()->new RuntimeException(String.format(COMMENT_NOT_FOUND_MSG)));
        if(deleteComment.getUser() != currentLoggedInUser){
            return new ResponseEntity<>(USER_NOT_OWNER_MSG, HttpStatus.UNAUTHORIZED);
        }
        else
        {
            commentService.delete(deleteComment);
            return ResponseEntity.ok("Komentarz o id "+id+" usunięty");
        }
    }
}
