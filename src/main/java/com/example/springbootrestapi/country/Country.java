package com.example.springbootrestapi.country;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.example.springbootrestapi.student.Student;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Country {

	public Country() {

	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	//@JsonManagedReference
	//@JsonBackReference
	@OneToMany(mappedBy = "birthCountry",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true)
	//@Transient	
	@JsonIgnoreProperties("birthCountry")
	private List<Student> students = new ArrayList<Student>();
	
	public Country(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Country(String name) {
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

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + "]";
	}


	
	

}
