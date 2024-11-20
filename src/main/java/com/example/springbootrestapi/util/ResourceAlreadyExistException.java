package com.example.springbootrestapi.util;

public class ResourceAlreadyExistException extends RuntimeException{

	public ResourceAlreadyExistException(String message) {
		super(message);
	}

}
