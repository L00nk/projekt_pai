package com.example.projekt_koncowy.user;

import com.example.projekt_koncowy.helpers.EditUserRequest;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final static String USER_NOT_FOUND_MSG = "Użytkownik o podanych danych nie istnieje";
    private final static String USER_EXISTS = "Użytkownik o podanym loginie/mailu już istnieje";

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@Valid @RequestBody User user, BindingResult bindingResult) {

        if (userService.validation(bindingResult).size() != 0)
            return new ResponseEntity<>(userService.getErrorList(bindingResult), HttpStatus.BAD_REQUEST);

        if (userService.existsByEmail(user.getEmail()) || userService.existsByLogin(user.getLogin())){
            return new ResponseEntity<>(USER_EXISTS, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(userService.save(user));
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {

        String login = loginRequest.getLogin();
        String password = loginRequest.getPassword();
        Optional<User> optionalUser = userService.findByLogin(login);

        if (userService.getErrorList(bindingResult).size() != 0)
            return new ResponseEntity<>(userService.getErrorList(bindingResult), HttpStatus.BAD_REQUEST);

        if (optionalUser.isEmpty())
            return new ResponseEntity<>(USER_NOT_FOUND_MSG, HttpStatus.UNAUTHORIZED);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtProvider.generateJwtToken(authentication);
        Date expiryDate = jwtProvider.getExpiryDateFromJwtToken(jwtToken);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwtToken, expiryDate, userDetails.getUsername()));
    }
    @PutMapping("/user/edit")
    public ResponseEntity<?> changePassword(@Valid @RequestBody EditUserRequest editUserRequest,
                                            BindingResult bindingResult) {
        String password = editUserRequest.getPassword();

        if (userService.validation(bindingResult).size() != 0)
            return new ResponseEntity<>(userService.validation(bindingResult).size() != 0, HttpStatus.BAD_REQUEST);

        User user = userService.findCurrentLoggedInUser().orElseThrow(()->new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG)));

        userService.changeUserPassword(user, password);

        return new ResponseEntity<>("Hasło zostało zmienione", HttpStatus.OK);
    }
    @GetMapping("/user/get-self")
    public ResponseEntity<?> getUser() {

        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(()->new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG)));

        return new ResponseEntity<>(currentLoggedInUser, HttpStatus.OK);

    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteUser() {

        User currentLoggedInUser = userService.findCurrentLoggedInUser().orElseThrow(()->new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG)));
        int id = currentLoggedInUser.getId();

        userService.delete(currentLoggedInUser);

        return new ResponseEntity<>("Użytkownik o id "+ id + " został usunięty", HttpStatus.OK);

    }


}
