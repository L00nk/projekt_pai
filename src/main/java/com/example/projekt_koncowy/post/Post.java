package com.example.projekt_koncowy.post;

import com.example.projekt_koncowy.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, length = 40)
    @Size(min = 3, max = 40, message = "Tytuł powinien składać się z 3-40 znaków")
    @NotBlank(message = "Tytuł nie może być pusty")
    private String title;

    @Column(nullable = false, length = 200)
    @Size(min = 5, max = 200, message = "Post powinien składać się z 5-200 znaków")
    @NotBlank(message = "Zawartość posta nie może być pusta")
    private String content;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", nullable = false, referencedColumnName="id")
    private User user;


}
