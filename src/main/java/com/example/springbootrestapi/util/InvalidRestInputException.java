package com.example.springbootrestapi.util;

public class InvalidRestInputException extends RuntimeException{

	public InvalidRestInputException(String message) {
		super(message);
	}

}
