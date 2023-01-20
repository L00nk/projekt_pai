package com.example.projekt_koncowy.comment;

import com.example.projekt_koncowy.post.Post;
import com.example.projekt_koncowy.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findAllByPost(Post post);
    List<Comment> findAllByUser(User user);
}
