package com.llb.fllbwebsite.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Category name is required")
    @JsonView(View.Summary.class)
    private String name;

    //One-to-Many relationship with Post
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonView(View.Summary.class)
    private List<Post> posts = new ArrayList<>();


    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonView(View.Summary.class)
    private Date created_At;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonView(View.Summary.class)
    private Date updated_At;

    @PrePersist
    public void onCreate(){
        this.created_At = new Date();
    }

    @PreUpdate
    public void onUpdate(){
        this.updated_At = new Date();
    }
}
