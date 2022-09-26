package com.project.blog.services;

import com.project.blog.payloads.CommentDto;

public interface CommentService {

	//create
	CommentDto createComment(CommentDto commentDto, Integer postId);
	//delete
	void deleteComment(Integer commentId);
}
