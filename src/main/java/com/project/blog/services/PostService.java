package com.project.blog.services;

import java.util.List;

import com.project.blog.entities.Post;
import com.project.blog.payloads.PostDto;
import com.project.blog.payloads.PostResponse;

public interface PostService {

	//create
	PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);
	
	//update
	PostDto updatePost(PostDto postDto, Integer postId); 
	
	//delete
	void deletePost(Integer postId);
	
	//get
	PostDto getPostById(Integer postId);
	
	//getAll
	PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

	//get all post for specific category
	PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize);
	
	//get all post for specific user
	PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize);
	
	//search posts
	List<PostDto> searchPost(String keyword);
}
