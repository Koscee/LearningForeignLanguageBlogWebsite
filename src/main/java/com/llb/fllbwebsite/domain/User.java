package com.llb.fllbwebsite.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.llb.fllbwebsite.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @NotBlank(message = "First name is required")
    @JsonView(View.Summary.class)
    private String firstName;

//    @NotBlank(message = "Last name is required")
    @JsonView(View.Summary.class)
    private String lastName;

    @Transient
    @JsonView(View.Summary.class)
    private String fullName;

    @Enumerated(value = EnumType.STRING)
    @JsonView(View.Summary.class)
    private Gender gender;

    @NotBlank(message = "Username is required")
    @Column(unique = true)
    @JsonView(View.Summary.class)
    private String username;

    @Email(message = "Please input a valid email address")
    @NotBlank(message = "Email address is required")
    @Column(unique = true)
    @JsonView(View.Summary.class)
    private String email;

    @NotBlank(message = "Password field is required")
    @Size(min = 8, message = "Password must be more than 8 characters")
    private String password;

    @Transient
    private String confirmPassword;

    @NotBlank(message = "Phone number is required")
    @Size(min = 13, max = 18, message = "Invalid mobile number")
    @JsonView(View.Summary.class)
    private String phoneNumber;

    @JsonView(View.Summary.class)
    private String avatarImg;

    @Transient
    @JsonView(View.Summary.class)
    private String roleName = "";

    //One-to-Many relationship with Post
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();

    //One-to-Many relationship with Comments
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    //One-to-Many relationship with Reaction (Likes)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Reaction> likes = new ArrayList<>();

    //Many-to-One relationship with Role
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Role role;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonView(View.Summary.class)
    private Date registered_At;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonView(View.Summary.class)
    private Date updated_At;


    @PostLoad
    protected void onLoad(){
        this.fullName = getFirstName() + " " + getLastName();
        this.roleName = this.role.getName();
    }

    @PrePersist
    public void onRegister(){
        this.registered_At = new Date();
    }

    @PreUpdate
    public void onUpdate(){
        this.updated_At = new Date();
    }

    @PostPersist
    protected void afterRegister(){
        this.onLoad();
    }

    @PostUpdate
    protected void afterUpdate(){
        this.onLoad();
    }


    /*
    UserDetails interface methods
     */

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.getRole().getName()));
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
