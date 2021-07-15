package com.llb.fllbwebsite.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserUpdateRequest {

    @NotNull(message = "User id is required")
    private Long id;

    @NotBlank(message = "Field cannot be blank")
    private String firstName;

//    @NotBlank(message = "Field cannot be blank")
    private String lastName;

    @NotBlank(message = "Field cannot be blank")
    private String username;

    @Email(message = "Please input a valid email address")
    @NotBlank(message = "Field cannot be blank")
    private String email;

//    @NotBlank(message = "Password field is required")
//    @Size(min = 8, message = "Password must be more than 8 characters")
//    private String password;

    @NotBlank(message = "Field cannot be blank")
    @Size(min = 13, max = 18, message = "Invalid mobile number")
    private String phoneNumber;

    private String avatarImg;

    @NotBlank(message = "Field cannot be blank")
    private String roleName;
}
