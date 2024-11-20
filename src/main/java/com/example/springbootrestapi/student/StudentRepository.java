package com.example.springbootrestapi.student;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
	
	//selects all fields only from student table not joining country table - this happens by
	//default because association with country table is lazy. Result is returned as list
	//of StudentWithoutCountry interface because query returns only Student object scalar fields
	@Query("SELECT s FROM Student s" ) 
	List <StudentExcludingCountry> findAllExcludingCountry();
	
	//@Query("SELECT s FROM Student s WHERE s.email=?1")
	Optional<Student> findStudentByEmail(String email);
	
	
	 @Query("SELECT s FROM Student s" ) 
	 List <Student> findAllStudents();
	 
	 //force fetching from associated country table is single query by joining student and country table, 
	 //which fetches also students country
	 @Query("SELECT s FROM Student s LEFT JOIN FETCH s.birthCountry" ) 
	 List <Student> findAllStudentsWithContries();

}
