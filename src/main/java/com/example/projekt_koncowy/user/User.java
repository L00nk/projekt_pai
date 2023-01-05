package com.example.projekt_koncowy.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    @Size(min = 5, max = 40, message = "Login powinien składać się z 5-40 znaków")
    @NotBlank(message = "Login nie może być pusty")
    private String login;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 60)
    @NotBlank(message = "Hasło nie może  być puste")
    private String password;

    @Transient
    private String roles = "ROLE_USER";
}
