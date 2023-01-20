package com.example.projekt_koncowy.post;

import com.example.projekt_koncowy.comment.Comment;
import com.example.projekt_koncowy.comment.CommentService;
import com.example.projekt_koncowy.user.User;
import com.example.projekt_koncowy.user.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final static String USER_NOT_FOUND_MSG = "Użytkownik nie znaleziony";
    private final CommentService commentService;

    public PostService(PostRepository postRepository,UserService userService, @Lazy CommentService commentService ) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentService = commentService;
    }

    public Post save(Post post){
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(()->new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG)));

        post.setUser(currentLoggedInUser);
        currentLoggedInUser.addPost(post);

        return postRepository.save(post);
    }

    public List<Post> findAllByUser(User user){
        return postRepository.findAllByUser(user);
    }
    public List<Post> findAllByDate(LocalDate date){
        return postRepository.findAllByDate(date);
    }
    public List<String> getErrorList(BindingResult bindingResult) {
        List<String> messages = new ArrayList<>();

        if (bindingResult.hasErrors())
            bindingResult.getFieldErrors().forEach(fieldError -> messages.add(fieldError.getDefaultMessage()));

        return messages;
    }
    public List<String> validation(BindingResult bindingResult) {

        return getErrorList(bindingResult);
    }


    public List<Post> findAll(){
        return postRepository.findAll();
    }

    public Optional<Post> findById(int id){
        return postRepository.findById(id);
    }

    public Post edit(Post oldPost, Post newPost){
        oldPost.setTitle(newPost.getTitle());
        oldPost.setContent(newPost.getContent());

        return postRepository.save(oldPost);
    }
    public void delete (Post post){
        List<Comment> commentList = commentService.findAllByPost(post);
        for(Comment comment : commentList){
            commentService.delete(comment);
        }
        postRepository.delete(post);
    }


}

