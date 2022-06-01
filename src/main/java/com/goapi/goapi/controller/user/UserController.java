package com.goapi.goapi.controller.user;

import com.goapi.goapi.controller.form.user.UserRegForm;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/reg")
    private ResponseEntity regUser(@Valid @RequestBody UserRegForm userRegForm) {
        Optional<User> userOptional = userService.addNewUser(userRegForm);
        return userOptional.map(user -> ResponseEntity.status(HttpStatus.CREATED).build())
            .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

}
