package com.llb.fllbwebsite.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @NotBlank(message = "Comment field cannot be blank")
    @JsonView(View.Summary.class)
    private String content;

    @Transient
    @JsonView(View.Summary.class)
    private String userName;

    @Transient
    @JsonView(View.Summary.class)
    private String userAvatar;

    @Transient
    @JsonView(View.Summary.class)
    private String postName;


    //Many-to-One relationship with User
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    //Many-to-One relationship with Post
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Post post;

    //Likes for comment will be implemented in future version

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonView(View.Summary.class)
    private Date created_At;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonView(View.Summary.class)
    private Date updated_At;

    @PostLoad
    protected void onLoad(){
        userName = user.getUsername();
        userAvatar = user.getAvatarImg();
        postName = post.getTitle();
    }

    @PrePersist
    public void onCreate(){
        this.created_At = new Date();
    }

    @PreUpdate
    public  void onUpdate(){
        this.updated_At = new Date();
    }
}
