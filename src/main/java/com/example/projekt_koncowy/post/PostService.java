package com.example.projekt_koncowy.post;

import com.example.projekt_koncowy.user.User;
import com.example.projekt_koncowy.user.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final static String USER_NOT_FOUND_MSG = "Użytkownik nie znaleziony";

    public PostService(PostRepository postRepository,UserService userService ) {
        this.postRepository = postRepository;
        this.userService = userService;
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
        postRepository.delete(post);
    }


}

