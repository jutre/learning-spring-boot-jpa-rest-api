package com.example.springbootrestapi.student.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.springbootrestapi.student.model.PhotoImage;

public interface PhotoImageRepository extends JpaRepository<PhotoImage, Long> {
	

	int deleteByStudentId(Long studentId);
	
	@Modifying
	@Query("delete from PhotoImage i where i.student.id=:studentId")
	int deleteImagesByStudentId(@Param("studentId") Long id);

}
