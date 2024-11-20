package com.example.springbootrestapi.country;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootrestapi.student.Student;

@RestController
@RequestMapping(path = "api/v1/country")
public class CountryController {

	private final CountryService countryService;
	
	@Autowired
	public CountryController(CountryService countryService) {
		this.countryService = countryService;
	}
	
	@GetMapping
	public List<Country> getCountries(){
		return countryService.getAllCountries();
	}
	
	@GetMapping(path = "{countryId}")
	public Country getCountryById(@PathVariable Long countryId) {
		return countryService.getCountryById(countryId);
	}
	
	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<String> handleIllegalStateException( IllegalStateException exception ) {
	    return ResponseEntity
	        .status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
	        .body("{\"message\":\"" + exception.getMessage() + "\"}");
	}

}
