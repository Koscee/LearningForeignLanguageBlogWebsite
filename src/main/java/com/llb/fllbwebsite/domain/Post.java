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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @NotBlank(message = "Post title is required")
    @Column(unique = true)
    @JsonView(View.Summary.class)
    private String title;

    @NotBlank(message = "Post description is required")
    @JsonView(View.Summary.class)
    private String description;

    @NotBlank(message = "Category of post is required")
    @JsonView(View.Summary.class)
    private String categoryName;

    @NotBlank(message = "Post content is required")
    @Lob
    private String content;

    @NotBlank(message = "Post cover image is required")
    @JsonView(View.Summary.class)
    private String coverImage;

    @Transient
    @JsonView(View.Summary.class)
    private String author;

    @Transient
    @JsonView(View.Summary.class)
    private String authorAvatar;

    //Many-to-One relationship with User
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    //One-to-Many relationship with Comments
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonView(View.Summary.class)
    private List<Comment> comments = new ArrayList<>();

    //One-to-Many relationship with Reaction (Likes)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonView(View.Summary.class)
    private List<Reaction> likes = new ArrayList<>();

    //Many-to-One relationship with Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Category category;



    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonView(View.Summary.class)
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonView(View.Summary.class)
    private Date updatedAt;

    @PostLoad
    protected void onLoad(){
        author = user.getUsername();
        authorAvatar = user.getAvatarImg();
    }

    @PrePersist
    protected void onCreate(){
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = new Date();
    }
}
