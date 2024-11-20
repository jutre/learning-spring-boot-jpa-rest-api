package com.example.springbootrestapi.student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.springbootrestapi.util.InvalidRestInputException;
import com.example.springbootrestapi.util.ResourceAlreadyExistException;
import com.example.springbootrestapi.util.ResourceNotFoundException;

@ControllerAdvice


public class GlobalExceptionHandler{

	@ExceptionHandler( ResourceNotFoundException.class )
	public ResponseEntity<Map<String, String>> onResourceNotFoundException(ResourceNotFoundException exception ) {

		Map<String, String> responseEntity = Map.of(
			"message", exception.getMessage()
		);

		return new ResponseEntity <> (responseEntity, HttpStatus.NOT_FOUND);
	}

	
	@ExceptionHandler(ResourceAlreadyExistException.class)
	public ResponseEntity<Map<String, String>> onResourceAlreadyExistException(ResourceAlreadyExistException exception ) {

		Map<String, String> responseEntity = Map.of(
			"message", exception.getMessage()
		);

		return new ResponseEntity <> (responseEntity, HttpStatus.CONFLICT);
	}
	
	
	@ExceptionHandler(InvalidRestInputException.class)
	public ResponseEntity<Map<String, String>> onInvalidRestInputException(InvalidRestInputException exception ) {

		Map<String, String> responseEntity = Map.of(
			"message", exception.getMessage()
		);

		return new ResponseEntity <> (responseEntity, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> onMethodArgumentNotValidException(MethodArgumentNotValidException exception ) {
		
		List <Map<String, String>> fieldValidationErrors = 
				exception.getBindingResult().getFieldErrors().stream().map(e->{
			
			Map<String, String> errFieldInfo = new HashMap<>();
			errFieldInfo.put(e.getField(), e.getDefaultMessage());
			return errFieldInfo;
			
		}).collect(Collectors.toList());

		Map<String, Object> responseEntity = Map
				.of(

					"message", "invalid field values",
					"validationErrors", fieldValidationErrors

						);

		return new ResponseEntity <> (responseEntity, HttpStatus.BAD_REQUEST);
	}

	/*this could be handler for all other unlisted exceptions but does all of them will be good to be responded
	 * with 400 status code?
	 * @ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String, String>> onOtherException(RuntimeException exception ) {

		Map<String, String> responseEntity = Map.of(
			"message", exception.getMessage()
		);

		return new ResponseEntity <Map<String, String>> (responseEntity, HttpStatus.BAD_REQUEST);
	}*/

}
