package com.project.blog.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.blog.entities.Comment;
import com.project.blog.entities.Post;
import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.payloads.CommentDto;
import com.project.blog.payloads.PostDto;
import com.project.blog.repositories.CommentRepository;
import com.project.blog.repositories.PostRepository;
import com.project.blog.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
		// change commentDto to comment to set post on comment
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		comment.setPost(post);
		comment.setContent(commentDto.getContent());
		// save comment to db
		Comment savedComment = this.commentRepository.save(comment);
		return this.modelMapper.map(savedComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {

		Comment comment = this.commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "Comment Id", commentId));
		this.commentRepository.delete(comment);

	}

}
