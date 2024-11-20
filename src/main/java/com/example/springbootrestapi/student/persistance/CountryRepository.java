package com.example.springbootrestapi.student.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springbootrestapi.student.model.Country;

@Repository
public interface CountryRepository  extends JpaRepository<Country, Long> {

}
