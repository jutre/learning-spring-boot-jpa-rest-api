package com.example.springbootrestapi.course;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.example.springbootrestapi.student.model.Student;

@Entity
public class Course {

	public Course() {

	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	//@OneToMany(mappedBy = "course")
	//private List<Student> students = new ArrayList<Student>();
	
	public Course(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Course(String name) {
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public List<Student> getStudents() {
//		return students;
//	}
//
//	public void setStudents(List<Student> students) {
//		this.students = students;
//	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + "]";
	}


	
	

}
