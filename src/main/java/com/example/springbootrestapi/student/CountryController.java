package com.example.springbootrestapi.student;

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

import com.example.springbootrestapi.student.model.Country;
import com.example.springbootrestapi.student.model.StudentExcludingCountry;

@RestController
@RequestMapping(path = "api/v1/student/countries")
public class CountryController {

	@Autowired
	private CountryService countryService;
	
	
	//returns all country list
	@GetMapping
	public List<Country> getCountries(){
		return countryService.getAllCountries();
	}
	
	//returns id, name of specified country
	@GetMapping(path = "{countryId}")
	public Country getCountryById(@PathVariable Long countryId) {
		return countryService.getCountryById(countryId);
	}
	
	//returns all students of specified country
	@GetMapping(path = "{countryId}/students")
	public List<StudentExcludingCountry> getCountriesStudents(@PathVariable("countryId") Long countryId) {
		return countryService.getCountriesStudentsByContryId(countryId);
	}
	
	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<String> handleIllegalStateException( IllegalStateException exception ) {
	    return ResponseEntity
	        .status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
	        .body("{\"message\":\"" + exception.getMessage() + "\"}");
	}

}
