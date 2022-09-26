package com.project.blog.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ResourceNotFoundException extends RuntimeException{

	String resourceName;
	String fieldName;
	long fieldValue;
	String str_fieldValue;
	
	public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue) {
		super(String.format("%s not found with %s : %s", resourceName,fieldName,fieldValue));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
	public ResourceNotFoundException(String resourceName, String fieldName, String str_fieldValue) {
		super(String.format("%s not found with %s : %s", resourceName,fieldName,str_fieldValue));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.str_fieldValue = str_fieldValue;
	}
}
