package com.devblogrestapi.service;

import com.devblogrestapi.payload.PostDto;
import com.devblogrestapi.payload.PostResponse;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    PostDto getPostById(long id);
    PostDto updatePost(PostDto postDto,long id);
    void deletePostById(long id);
}
