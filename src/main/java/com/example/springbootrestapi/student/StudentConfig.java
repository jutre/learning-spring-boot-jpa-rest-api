package com.example.springbootrestapi.student;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springbootrestapi.student.model.Country;
import com.example.springbootrestapi.student.model.PhotoImage;
import com.example.springbootrestapi.student.model.Student;
import com.example.springbootrestapi.student.model.StudentDetails;
import com.example.springbootrestapi.student.persistance.CountryRepository;
import com.example.springbootrestapi.student.persistance.StudentRepository;

@Configuration
public class StudentConfig {

	@Bean
	CommandLineRunner commandLineRunner(StudentRepository repository, CountryRepository countryRepository) {
		return args -> {

			Country country = new Country("Latvia");
			countryRepository.save(country);
			
			Student juris = new Student(
				"Juris",
				"Strazdiņš",
				"Juris.Strazdins@gmail.com",
				LocalDate.of(1982, Month.OCTOBER,12),
				country);
			
			
			juris.addImage(new PhotoImage("Image1"));
			juris.addImage(new PhotoImage("Image2"));

			country = new Country("Russia");
			countryRepository.save(country);
			
			Student janis = new Student(
				"Janis",
				"Bērziņš",
				"Janis.Berzins@gmail.com",
				LocalDate.of(1960, Month.MARCH, 29),
				country);
			
			StudentDetails studDetails = new StudentDetails("Details of Janis");
			janis.setDetails(studDetails);
			
			Student aivars = new Student(
				"Aivars",
				"Zvidris",
				"Aivars.Zvidris@gmail.com",
				LocalDate.of(1981, Month.DECEMBER, 3),
				country);
			
			Student ivars = new Student(
				"Ivars",
				"Kalniņš",
				"Ivars.Kalnins@gmail.com",
				LocalDate.of(1934, Month.NOVEMBER, 21),
				country);
			
			repository.saveAll(List.of(juris, janis, aivars, ivars));

		};
	}

}
