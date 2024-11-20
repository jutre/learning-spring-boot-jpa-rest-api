package com.example.springbootrestapi.student.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@Email
	private String email;

	@NotNull
	private LocalDate dob;
	@Transient
	private Integer age;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_country")
	private Country birthCountry;

	@OneToOne(
		mappedBy = "student",
		fetch = FetchType.LAZY,
		cascade = CascadeType.ALL,
		orphanRemoval = true)
	private StudentDetails details;

	@OneToMany(
		mappedBy = "student",
		fetch = FetchType.LAZY,
		cascade = CascadeType.ALL,
		orphanRemoval = true)
	private List<PhotoImage> images = new ArrayList<>();

	public Student() {

	}


	public Student(String firstName, String lastName, String email, LocalDate dob, Country country) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.dob = dob;
		this.birthCountry = country;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String name) {
		this.firstName = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public Integer getAge() {
		return Period.between(this.dob, LocalDate.now()).getYears();
	}

	public void setAge(Integer age) {
		this.age = age;
	}


	public Country getBirthCountry() {
		return birthCountry;
	}

	public void setBirthCountry(Country country) {
		this.birthCountry = country;
	}



	public StudentDetails getDetails() {
		return details;
	}


	/*
	 * as Student and StudentDetails relation is bidirectional when setting details
	 * we must maintain StudentDetails.student reference
	 */
    public void setDetails(StudentDetails details) {
        if (details == null) {
            if (this.details != null) {
                this.details.setStudent(null);
            }
        } else {
        	details.setStudent(this);
        }
        this.details = details;
    }


	public List<PhotoImage> getImages() {
		return images;
	}


	public void setImages(List<PhotoImage> images) {
		this.images = images;
	}

	/*
	 * as Student and PhotoImage relation is bidirectional, when adding
	 * PhotoImage, we must set PhotoImage.student to current student
	 */
    public void addImage(PhotoImage image) {
        images.add(image);
        image.setStudent(this);
    }

    /*
	 * as Student and PhotoImage relation is bidirectional, when removing
	 * PhotoImage, we must remove PhotoImage.student
	 */
    public void removeImage(PhotoImage image) {
        images.remove(image);
        image.setStudent(null);
    }


	@Override
	public String toString() {
		return "Student [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", dob=" + dob + ", age=" + age + ", birthCountry=" + birthCountry + ", details="
				+ details + ", images=" + images + "]";
	}




}
