package com.devblogrestapi.service.impl;

import com.devblogrestapi.entity.Comment;
import com.devblogrestapi.entity.Post;
import com.devblogrestapi.exception.BlogAPIException;
import com.devblogrestapi.exception.ResourceNotFoundException;
import com.devblogrestapi.payload.CommentDto;
import com.devblogrestapi.repository.CommentRepository;
import com.devblogrestapi.repository.PostRepository;
import com.devblogrestapi.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",postId)
        );
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);
        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
       Post post = postRepository.findById(postId).orElseThrow(
               () -> new ResourceNotFoundException("Post","id",postId)
       );
       Comment comment = commentRepository.findById(commentId).orElseThrow(
               () -> new ResourceNotFoundException("Comment","id",commentId)
       );
       if(!comment.getPost().getId().equals(post.getId())){
           throw new BlogAPIException("Comment does not belong to post", HttpStatus.BAD_REQUEST);
       }
       return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentRequest) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",postId)
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment","id",commentId)
        );

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException("Comment does not belongs to post",HttpStatus.BAD_REQUEST);
        }
        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);
        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",postId)
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment","id",commentId)
        );

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException("Comment does not belongs to post",HttpStatus.BAD_REQUEST);
        }
        commentRepository.delete(comment);
    }

    private CommentDto mapToDTO(Comment comment){
        CommentDto commentDto = mapper.map(comment,CommentDto.class);
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = mapper.map(commentDto,Comment.class);
        return comment;
    }

}
