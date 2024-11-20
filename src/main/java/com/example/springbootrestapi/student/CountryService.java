package com.example.springbootrestapi.student;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springbootrestapi.student.model.Country;
import com.example.springbootrestapi.student.model.StudentExcludingCountry;
import com.example.springbootrestapi.student.persistance.CountryRepository;
import com.example.springbootrestapi.student.persistance.StudentRepository;

@Service
public class CountryService {
	
	@Autowired
	private CountryRepository contryRepository;
	
	@Autowired
	private StudentRepository studentRepository;

	public List<Country> getAllCountries() {
		return contryRepository.findAll();
	}

	public Country getCountryById(Long countryId) {
		Optional<Country> countryById = contryRepository.findById(countryId);
		if(countryById.isEmpty()) {
			throw new IllegalStateException("country with id " + countryId + " does not exist");
		}

		return countryById.get();
	}

	public List<StudentExcludingCountry> getCountriesStudentsByContryId(Long countryId) {
		Optional<Country> countryById = contryRepository.findById(countryId);
		if(countryById.isEmpty()) {
			throw new IllegalStateException("country with id " + countryId + " does not exist");
		}
		List<StudentExcludingCountry> studentsOfCountry = studentRepository.findStudentsByCountryId(countryId);
		return studentsOfCountry;
	}

}
