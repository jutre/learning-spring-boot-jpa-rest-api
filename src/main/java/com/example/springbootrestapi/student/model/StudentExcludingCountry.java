package com.example.springbootrestapi.student.model;

import java.time.LocalDate;

public interface StudentExcludingCountry {
	Long getId();
	
	String getFirstName();
	
	String getLastName();
	
	String getEmail();
	
	LocalDate getDob();
	
	Integer getAge();
}
