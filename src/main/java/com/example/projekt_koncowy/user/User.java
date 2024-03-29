package com.example.projekt_koncowy.user;

import com.example.projekt_koncowy.comment.Comment;
import com.example.projekt_koncowy.post.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true, nullable = false, length = 45)
    @Size(min = 4, max = 40, message = "Login powinien składać się z 4-40 znaków")
    @NotBlank(message = "Login nie może być pusty")
    private String login;

    @Column(unique = true, nullable = false, length = 50)
    @Size(max = 50, message = "Email powinien mieć maksymalnie 50 znaków")
    @NotBlank
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 1000)
    @NotBlank(message = "Hasło nie może  być puste")
    private String password;

    @Transient
    private String roles = "ROLE_USER";

    @Transient
    @JsonIgnore
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private Set<Post> postSet = new HashSet<>();

    public void addPost(Post post) {
        postSet.add(post);
        post.setUser(this);
    }

    @Transient
    @JsonIgnore
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private Set<Comment> commentSet = new HashSet<>();

    public void addComment(Comment comment) {
        commentSet.add(comment);
        comment.setUser(this);
    }
}
