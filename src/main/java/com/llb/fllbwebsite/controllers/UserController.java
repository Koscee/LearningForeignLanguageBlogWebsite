package com.llb.fllbwebsite.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.llb.fllbwebsite.domain.User;
import com.llb.fllbwebsite.domain.View;
import com.llb.fllbwebsite.payload.JWTLoginSuccessResponse;
import com.llb.fllbwebsite.payload.LoginRequest;
import com.llb.fllbwebsite.payload.UserUpdateRequest;
import com.llb.fllbwebsite.security.JwtTokenProvider;
import com.llb.fllbwebsite.services.UserService;
import com.llb.fllbwebsite.services.ValidationErrorService;
import com.llb.fllbwebsite.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.security.Principal;

import static com.llb.fllbwebsite.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ValidationErrorService validationErrorService;
    private final UserValidator userValidator;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public UserController(UserService userService, ValidationErrorService validationErrorService, UserValidator userValidator, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.validationErrorService = validationErrorService;
        this.userValidator = userValidator;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    // Create User  [ @route: /api/users/register  @access: private]
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
        // Validate password match
        userValidator.validate(user, result);

        ResponseEntity<?> errorMap = validationErrorService.MapValidationService(result);
        if (errorMap != null) return errorMap;
        User newUser = userService.save(user);
        return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }


    // User Login  [ @route: /api/users/login  @access: private]
    @PostMapping("/login")
    public  ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        ResponseEntity<?> errorMap = validationErrorService.MapValidationService(result);
        if (errorMap != null) return errorMap;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
    }


    // Update User  [ @route: /api/users/update/{id}  @access: private]
    @PatchMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateRequest updateRequest, BindingResult result,
                                        @PathVariable Long userId, Principal principal){
        ResponseEntity<?> errorMap = validationErrorService.MapValidationService(result);
        if (errorMap != null) return errorMap;

        User updatedUser = userService.updateUser(updateRequest, userId, principal.getName());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


    // Get all users  [ @route: /api/users/all  @access: public]
    @JsonView(View.Summary.class)
    @GetMapping("/all")
    public ResponseEntity<Iterable<User>> getAllUsers(){
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    // Get User by Id  [ @route: /api/users/:id  @access: public / private]
    @GetMapping("/id/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.findUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Delete User by Id  [ @route: /api/users/:id  @access: private]
    @DeleteMapping("/id/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return new ResponseEntity<>("User with ID '" + userId + "' was deleted successfully", HttpStatus.OK);
    }

}
