package com.example.springbootrestapi.country;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springbootrestapi.student.Student;

@Service
public class CountryService {
	
	private final CountryRepository contryRepository;
	
	@Autowired
	public CountryService(CountryRepository contryRepository) {
		this.contryRepository = contryRepository;
	}

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

}
