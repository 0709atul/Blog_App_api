package com.project.blog.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.blog.entities.Category;
import com.project.blog.entities.Post;
import com.project.blog.entities.User;
import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.payloads.CategoryDto;
import com.project.blog.payloads.PostDto;
import com.project.blog.payloads.PostResponse;
import com.project.blog.repositories.CategoryRepository;
import com.project.blog.repositories.PostRepository;
import com.project.blog.repositories.UserRepository;
import com.project.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {

		// find category
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", categoryId));
		// find user
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));

		// convert dto to entity
		Post post = this.modelMapper.map(postDto, Post.class);
		// set Datt on post entity
		post.setImageName("default.png");
		post.setDate(new Date());
		post.setCategory(category);
		post.setUser(user);

		// save post into db
		Post savedPost = this.postRepository.save(post);
		// return postDto
		return this.modelMapper.map(savedPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());

		Post updatedPost = this.postRepository.save(post);
		return this.modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
		this.postRepository.delete(post);
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
		return this.modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		
		Sort sort = null;
		
		if(sortDir.equalsIgnoreCase("asc")) 
			sort = Sort.by(sortBy).ascending();
		else
			sort = Sort.by(sortBy).descending();
		
		// create pageable object with sorting
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		// find single page of posts and it has all required information about page
		Page<Post> pagePost = this.postRepository.findAll(pageable);
		// find all post (list of all post)
		List<Post> posts = pagePost.getContent();
		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		// create postresponse object and set data on it.
		PostResponse postResponse = new PostResponse();

		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setTotalElements(pagePost.getNumberOfElements());
		postResponse.setLastPage(pagePost.isLast());
		// now return post Response
		return postResponse;
	}

	@Override
	public PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize) {
		// create pageable object
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		// find category by CategoryRepository
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		// find single page of posts and it has all required information about page
		Page<Post> pagePost = this.postRepository.findByCategory(category, pageable);
		// find all post(list of posts)
		List<Post> posts = pagePost.getContent();
		// convert list of posts into list of dtos
		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		// create postresponse object and set data on it.
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setTotalElements(pagePost.getNumberOfElements());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize) {
		// create pageable object to get page
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		// find user by UserRepository
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User Id", userId));
		// find single page of posts and it has all required information about page
		Page<Post> pagePost = this.postRepository.findByUser(user, pageable);
		//// find all post(list of posts)
		List<Post> posts = pagePost.getContent();
		// convert list of post into a list of postDtos
		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		// create postresponse object and set data on it.
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setTotalElements(pagePost.getNumberOfElements());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}

	@Override
	public List<PostDto> searchPost(String keyword) {
		List<Post> posts = this.postRepository.findByTitleContaining(keyword);
		List<PostDto> postDtos = posts.stream()
				.map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		return postDtos;
	}

}
