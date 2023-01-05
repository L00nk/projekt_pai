package com.example.projekt_koncowy.user;

import com.example.projekt_koncowy.helpers.LoginRequest;
import com.example.projekt_koncowy.jwtconf.JwtProvider;
import com.example.projekt_koncowy.jwtconf.JwtResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    private final static String USER_NOT_FOUND_MSG = "User not found";
    private final static String USER_EXISTS = "User already exists";

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@Valid @RequestBody User user, BindingResult bindingResult) {

        if (userService.existsByEmail(user.getEmail()) || userService.existsByLogin(user.getLogin())){
            return new ResponseEntity<>(USER_EXISTS, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(userService.save(user));
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {

        String email = loginRequest.getLogin();
        String password = loginRequest.getPassword();
        Optional<User> optionalUser = userService.findByLogin(email);

        if (optionalUser.isEmpty())
            return new ResponseEntity<>(USER_NOT_FOUND_MSG, HttpStatus.UNAUTHORIZED);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtProvider.generateJwtToken(authentication);
        Date expiryDate = jwtProvider.getExpiryDateFromJwtToken(jwtToken);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwtToken, expiryDate, userDetails.getUsername()));
    }
}
