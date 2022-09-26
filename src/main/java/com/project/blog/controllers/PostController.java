package com.project.blog.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.blog.config.AppConstants;
import com.project.blog.payloads.ApiResponse;
import com.project.blog.payloads.PostDto;
import com.project.blog.payloads.PostResponse;
import com.project.blog.services.FileService;
import com.project.blog.services.PostService;

@RestController
@RequestMapping("/api/")
public class PostController {

	@Autowired
	private PostService postServiceImpl;

	@Autowired
	private FileService fileServiceImpl;
	
	@Value("${project.image}")
	private String path;
	
	// create post
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(
			@RequestBody PostDto postDto,
			@PathVariable("userId") Integer userId,
			@PathVariable("categoryId") Integer categoryId){
		PostDto createPost = this. postServiceImpl.createPost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto>(createPost,HttpStatus.CREATED);
	}
	
	//get posts by user
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<PostResponse> getPostsByUser(
			@PathVariable("userId") Integer userId,
			@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize)
	{
		PostResponse postResponse = this.postServiceImpl.getPostsByUser(userId, pageNumber, pageSize);
		return new ResponseEntity<PostResponse>(postResponse,HttpStatus.OK);
	}
	
	//get posts by category
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<PostResponse> getPostsByCategory(
			@PathVariable("categoryId") Integer categoryId,
			@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize)
	{
		PostResponse postResponse = this.postServiceImpl.getPostsByCategory(categoryId,pageNumber,pageSize);
		return new ResponseEntity<PostResponse>(postResponse,HttpStatus.OK);
	}
	
	//get post by post id
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId){
		PostDto postDto = this.postServiceImpl.getPostById(postId);
		return ResponseEntity.ok(postDto);
	}
	
	//get all post
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(name = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir){
		PostResponse pagePostResponse = this.postServiceImpl.getAllPosts(pageNumber,pageSize,sortBy,sortDir);
		return new ResponseEntity<PostResponse>(pagePostResponse,HttpStatus.OK);
	}
	
	//delete post
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable("postId") Integer postId){
		this.postServiceImpl.deletePost(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post is Successfully Deleted!!",true),HttpStatus.OK);
	}
	
	//update post
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable("postId") Integer postId){
		PostDto updatedPostDto = this.postServiceImpl.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatedPostDto,HttpStatus.OK);
	}
	
	//search post by keyword
	@GetMapping("/posts/search/{keywords}")
	public ResponseEntity<List<PostDto>> searchPostByKeyword(
			@PathVariable("keywords") String keywords ){
		 List<PostDto> postDtos = this.postServiceImpl.searchPost(keywords);
		 return new ResponseEntity<List<PostDto>>(postDtos,HttpStatus.OK);
	}
	
	//upload image for perticular image
	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(
			@RequestParam("image") MultipartFile image,
			@PathVariable Integer postId) throws IOException{
		
		PostDto postDto = this.postServiceImpl.getPostById(postId);
		//
		String uploadImageName = this.fileServiceImpl.uploadImage(path, image);
		postDto.setImageName(uploadImageName);
		//update post
		PostDto updatedPost = this.postServiceImpl.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatedPost,HttpStatus.OK);
	}
	
	//method to serve file
	@GetMapping(value = "/post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {
		InputStream resource = this.fileServiceImpl.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
}












