package com.example.projekt_koncowy.post;

import com.example.projekt_koncowy.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {
    List<Post> findAllByUser(User user);
    List<Post> findAll();

}
