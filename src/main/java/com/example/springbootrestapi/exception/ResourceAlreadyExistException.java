package com.example.springbootrestapi.exception;

public class ResourceAlreadyExistException extends RuntimeException{

	public ResourceAlreadyExistException(String message) {
		super(message);
	}

}
