package com.example.projekt_koncowy.comment;

import com.example.projekt_koncowy.post.Post;
import com.example.projekt_koncowy.post.PostService;
import com.example.projekt_koncowy.user.User;
import com.example.projekt_koncowy.user.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final static String USER_NOT_FOUND_MSG = "Użytkownik nie znaleziony";

    public CommentService(CommentRepository commentRepository, UserService userService, PostService postService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    public List<String> getErrorList(String comment) {
        List<String> messages = new ArrayList<>();

        if (comment.length() < 3 || comment.length() > 40)
            messages.add("Komentarz powinien zawierać od 2 do 80 znaków");

        return messages;
    }

    public Comment save(Comment comment, Post post){
        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(()->new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG)));

        comment.setUser(currentLoggedInUser);
        currentLoggedInUser.addComment(comment);

        comment.setPost(post);
        post.addComment(comment);

        return commentRepository.save(comment);
    }

    public List<Comment> findAllByPost(Post post){
        return commentRepository.findAllByPost(post);
    }
    public List<Comment> findAllByUser(User user){
        return commentRepository.findAllByUser(user);
    }

    public Optional<Comment> findById(int id){
        return commentRepository.findById(id);
    }

    public void delete (Comment comment){
        commentRepository.delete(comment);
    }

}
