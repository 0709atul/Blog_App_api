package com.project.blog.payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.project.blog.entities.Category;
import com.project.blog.entities.Comment;
import com.project.blog.entities.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

	private Integer postId;
	private String title;
	private String content;
	private String imageName;
	private Date date;
	private CategoryDto category;
	private UserDto user; 
	private Set<CommentDto> comments = new HashSet<>();
	 
}
