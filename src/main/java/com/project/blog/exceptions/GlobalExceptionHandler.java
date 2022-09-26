package com.project.blog.exceptions;

import java.io.ObjectInputStream.GetField;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.project.blog.payloads.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler  {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExcetionHandler(ResourceNotFoundException ex) {
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> methodArgsNotValidExceptionHandler(MethodArgumentNotValidException ex) {
		Map<String, String> rsp = new HashMap<>();
		// now we are using exception object to get fieldname due to excetion occur and
		// message!!
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			rsp.put(fieldName, message);
		});

		return new ResponseEntity<Map<String, String>>(rsp, HttpStatus.BAD_REQUEST);
	}

}
