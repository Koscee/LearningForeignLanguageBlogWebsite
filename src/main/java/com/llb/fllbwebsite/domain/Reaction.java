package com.llb.fllbwebsite.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @JsonView(View.Summary.class)
    private Boolean isLiked = false;

    @Transient
    @JsonView(View.Summary.class)
    private String userName;

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
        postName = post.getTitle();
    }

    @PrePersist
    protected void onCreate(){
        this.created_At = new Date();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updated_At = new Date();
    }
}
