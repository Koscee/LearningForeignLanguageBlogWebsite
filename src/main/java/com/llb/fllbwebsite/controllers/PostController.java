package com.llb.fllbwebsite.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.llb.fllbwebsite.domain.Post;
import com.llb.fllbwebsite.domain.View;
import com.llb.fllbwebsite.dto.PostDto;
import com.llb.fllbwebsite.services.PostService;
import com.llb.fllbwebsite.services.ValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final ValidationErrorService validationErrorService;

    @Autowired
    public PostController(PostService postService, ValidationErrorService validationErrorService) {
        this.postService = postService;
        this.validationErrorService = validationErrorService;
    }


    // Create Post  [ @route: /api/posts  @access: private]
    @PostMapping("")
    public ResponseEntity<?> createPost(@Valid @RequestBody Post post, BindingResult result, Principal principal){

        ResponseEntity<?> errorMap = validationErrorService.MapValidationService(result);
        if (errorMap != null) return errorMap;
        Post newPost = postService.saveOrUpdatePost(post, principal.getName());

        return new ResponseEntity<Post>(newPost, HttpStatus.CREATED);
    }

    // Get all Posts  [ @route: /api/posts/all  @access: public]
    @JsonView(View.Summary.class)
    @GetMapping("/all")
    public ResponseEntity<Iterable<Post>> getAllPosts(){
        return new ResponseEntity<Iterable<Post>>(postService.findAllPosts(), HttpStatus.OK);
    }

    // Get all Posts info without including their content  [ @route: /api/posts/all-filtered @access: public]
    @GetMapping("/all-filtered")
    public ResponseEntity<Iterable<PostDto>> getAllFilteredPosts(){
        return new ResponseEntity<Iterable<PostDto>>(postService.getAllPostsAndFilterPostContents(), HttpStatus.OK);
    }

    // Get all Posts of a User  [ @route: /api/posts/all/user  @access: public]
    @JsonView(View.Summary.class)
    @GetMapping("/all/user")
    public ResponseEntity<Iterable<Post>> getAllPostsByUser(Principal principal){
        return new ResponseEntity<Iterable<Post>>(postService.findAllPostsByUser(principal.getName()), HttpStatus.OK);
    }

    // Get Post by Id  [ @route: /api/posts/id/:postId  @access: public / private]
    @GetMapping("/id/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId){
        Post post = postService.findPostById(postId);
        return new ResponseEntity<Post>(post, HttpStatus.OK);
    }

    // Get Posts by categoryName  [ @route: /api/posts/all/{postId}/{categoryName}  @access: public]
    @JsonView(View.Summary.class)
    @GetMapping("/all/{postId}/{categoryName}")
    public ResponseEntity<?> getTop3RelatedPosts(@PathVariable Long postId, @PathVariable String categoryName){
        Iterable<Post> posts = postService.findTop3RelatedPostByCategoryName(postId, categoryName);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // Delete Post by Id  [ @route: /api/posts/id/:postId  @access: private]
    @DeleteMapping("/id/{postId}")
    public ResponseEntity<?> deletePostById(@PathVariable Long postId, Principal principal){
        postService.deletePostById(postId, principal.getName());
        return new ResponseEntity<String>("Post with ID '" + postId + "' was deleted successfully", HttpStatus.OK);
    }


    // Get Post by title or content  [ @route: /api/posts/search?searchText=value  @access: public / private]
    @JsonView(View.Summary.class)
    @GetMapping("/search")
    public ResponseEntity<?> searchPostByTitleOrContent(@RequestParam(value = "searchText") String searchText){
        List<Post> foundPosts = postService.findPostByContentOrTitleIgnoreLetterCase(searchText);
        return new ResponseEntity<>(foundPosts, HttpStatus.OK);
    }






    // In the frontend use lodash to remove spaces from the String passed as the PathVariable
    // Get Post by title  [ @route: /api/posts/:title  @access: public / private]
//    @GetMapping("/title/{postTitle}")
//    public ResponseEntity<?> getPostByTitle(@PathVariable String postTitle){
//        postTitle = postTitle.replace("-", " ");
//        Post post = postService.findPostByTitle(postTitle);
//        return new ResponseEntity<Post>(post, HttpStatus.OK);
//    }

}
