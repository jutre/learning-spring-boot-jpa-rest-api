package com.example.springbootrestapi.student.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Country {

	public Country() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;



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

	/*
	 * public List<Student> getStudents() { return students; }
	 *
	 * public void setStudents(List<Student> students) { this.students = students; }
	 */

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + "]";
	}





}
