package com.example.springbootrestapi.student.persistance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.springbootrestapi.student.model.Student;
import com.example.springbootrestapi.student.model.StudentExcludingCountry;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{
	
	/*
	 * selects all records from student table, returning fields only from student
	 * table not joining country table - this happens because association with
	 * country table is lazy and returned StudentWithoutCountry type does not
	 * contain birthCountry field which is saved in country table
	 */
	@Query("SELECT s FROM Student s LEFT JOIN FETCH s.details" ) 
	List <StudentExcludingCountry> findAllExcludingCountry();
	
	
	/*
	 * returns records from student table belonging to specified country. Returns
	 * Student fields without associated Country object
	 */
	@Query("SELECT s FROM Student s JOIN s.birthCountry WHERE s.birthCountry.id=:countryId")
	List<StudentExcludingCountry> findStudentsByCountryId(@Param("countryId") Long id);
	
	

	Optional<Student> findStudentByEmail(String email);
	
	
	 //@Query("SELECT s FROM Student s" ) 
	 //List <Student> findAllStudents();
	 
	/*
	 * force fetching lazily associated relations
	 */
	 @Query("SELECT s FROM Student s "
	 		+ "LEFT JOIN FETCH s.birthCountry "
	 		+ "LEFT JOIN FETCH s.details "
	 		+ "LEFT JOIN FETCH s.images "
	 		+ "where s.id=:studentId") 
	 Optional <Student> findStudentAndCountryByStudentId(@Param("studentId") Long id);
	 
	 
	@Modifying
	@Query("delete from Student s where s.id=:studentId")
	int deleteStudentById(@Param("studentId") Long id);

}
