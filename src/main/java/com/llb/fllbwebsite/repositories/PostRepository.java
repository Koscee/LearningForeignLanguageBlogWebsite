package com.llb.fllbwebsite.repositories;

import com.llb.fllbwebsite.domain.Post;
import com.llb.fllbwebsite.domain.User;
import com.llb.fllbwebsite.dto.PostDto;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

//    @Override
//    Optional<Post> findById(Long postId);

    Post getById(Long postId);

    Post findByTitle(String postTitle);

    Iterable<Post> findAllByUserOrderByIdDesc(User user);

//    @Query("select NEW com.llb.fllbwebsite.dto.PostDto (p.id, p.title, p.description, p.coverImage, p.categoryName, p.createdAt, p.updatedAt) from Post p")
//    List<PostDto> getAll();

   @Query("select NEW com.llb.fllbwebsite.dto.PostDto (p.id, p.title, p.description, p.coverImage, p.categoryName, p.createdAt, p.updatedAt) from Post p")
   Iterable<PostDto> getAll(Sort createdAt);

//    @Query("select NEW com.llb.fllbwebsite.dto.PostDto (p.id, p.title, p.categoryName, p.coverImage, p.description, p.createdAt, p.updatedAt) " +
//            "from Post p " +
//            "where p.id <> ?1 and p.categoryName = ?2 " +
//            "order by p.createdAt desc"
//    )
    Iterable<Post> findTop3ByIdNotAndCategoryNameOrderByCreatedAtDesc(Long postId, String categoryName);

    @Query("select p from Post p where lower(p.content) like %?1% or lower(p.title) like %?1% order by p.createdAt desc ")
    List<Post> findPostByContentOrTitleIgnoreLetterCase(String searchText);

}
