package com.project.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.blog.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
