package com.devblogrestapi.repository;

import com.devblogrestapi.entity.Comment;
import com.devblogrestapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

}
