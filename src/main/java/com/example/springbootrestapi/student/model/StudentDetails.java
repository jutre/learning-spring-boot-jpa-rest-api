package com.example.springbootrestapi.student.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class StudentDetails {
	
	@Id
    private Long id;
	private String description;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "id") 
	@JsonIgnore //prevents infinite recursion while building JSON as it is bidirectional association
	private Student student;
	 
	
	public StudentDetails() {
	}

	public StudentDetails(String description) {
		this.description = description;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "StudentDetails [id=" + id + ", description=" + description + "]";
	}

	
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	 
	
	

}
