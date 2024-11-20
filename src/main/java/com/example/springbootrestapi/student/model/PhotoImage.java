package com.example.springbootrestapi.student.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class PhotoImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	String filename;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_student")
	@JsonIgnore //prevents infinite recursion while building JSON as it is bidirectional association
    private Student student;

	public PhotoImage() {
	}

	public PhotoImage(String filename) {
		this.filename = filename;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) 
        	return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass()) 
        	return false;
        return id != null && id.equals(((PhotoImage) o).getId());
    }

	@Override
	public String toString() {
		String imgStr = "PhotoImage [id=" + id + ", filename=" + filename;
		//concatenate student's first, last name, not student field itself to prevent infinite recursion
		//because Student.toString outputs PhotoImage field too
		if(student != null) {
			imgStr += ", Student=[" + student.getFirstName() + "," + student.getLastName() + "]";
		}
		imgStr +=  "]"; 
		return imgStr;
	}
	
}
