package com.example.springbootrestapi.student;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springbootrestapi.student.model.PhotoImage;
import com.example.springbootrestapi.student.model.Student;
import com.example.springbootrestapi.student.model.StudentExcludingCountry;
import com.example.springbootrestapi.student.persistance.CountryRepository;
import com.example.springbootrestapi.student.persistance.PhotoImageRepository;
import com.example.springbootrestapi.student.persistance.StudentRepository;
import com.example.springbootrestapi.util.ResourceAlreadyExistException;
import com.example.springbootrestapi.util.ResourceNotFoundException;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private PhotoImageRepository photoImageRepository;


	public List<StudentExcludingCountry> getStudents() {
		return studentRepository.findAllExcludingCountry();
	}


	public Student getStudentById(Long studentId) {

		Student student = studentRepository.findStudentAndCountryByStudentId(studentId)
				.orElseThrow(()-> new ResourceNotFoundException(
					"student with id " + studentId + " does not exist"));

		return student;
	}


	public Student addNewStudent(Student student) {
		
		Optional<Student> studentByEmail = studentRepository.findStudentByEmails(student.getEmail());
		if(studentByEmail.isPresent()) {
			throw new ResourceAlreadyExistException("student with email " + student.getEmail() + " already exists");
		}
		
		boolean countryExists = countryRepository.existsById(student.getBirthCountry().getId());
		if(!countryExists) {
			throw new ResourceNotFoundException("country with id " + student.getBirthCountry().getId() + " does not exist");
		}
						

		/*
		 * it is possible to set Student.id in request; if Student.id is not null, repository save()
		 *  method queries for student record with given id and updates it if record exists
		 */
		student.setId(null);

		/*
		 * when request json is deserialized to Student object Student.image field is populated with list of
		 * PhotoImage objects, while PhotoImage.student field is populated with null value; it must be set to Student
		 * object - it is needed for jpa to persist reference to Student object as PhotoImage.student
		 * is the owning side of manyToOne relationship.
		 * Also it is possible to set PhotoImage.id in request; it must be null to prevent updating any existing
		 * PhotoImage record's fields
		 */
		student.getImages().forEach((img)->{
			img.setStudent(student);
			img.setId(null);
		});

		return studentRepository.save(student);
	}


	public void deleteStudent(Long studentId) {
//		boolean exists = studentRepository.existsById(studentId);
//		if(!exists) {
//			throw new IllegalStateException("student with id " + studentId + " does not exist");
//		}
//		studentRepository.deleteById(studentId);
		//select student data joined with all associated tables to prevent individual select to associated tables
		studentRepository.findStudentAndCountryByStudentId(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("student with id " + studentId + " not found"));
		
		studentRepository.deleteById(studentId);
		//this method can be improved by deleting from PhotoImage table by single query (by default jpa issues individual
		//delete query for each associated PhotoImage)
	}

	@Transactional
	public void updateStudent(Long studentId, String firstName, String email) {

		//checking if student with supplied id exists in database
		Student existingStudent = studentRepository.findById(studentId)
				.orElseThrow(()-> new ResourceNotFoundException(
					"student with id " + studentId + " does not exist"));


		//get new name if supplied
		if(firstName != null &&
				firstName.length() != 0 &&
				!existingStudent.getFirstName().equals(firstName)) {
			existingStudent.setFirstName(firstName);
		}

		if(email != null 
				&& email.length() != 0 
				&& !existingStudent.getEmail().equals(email)) {
			Optional<Student> otherStudentWithSuppliedEmail = studentRepository.findStudentByEmails(email);
			if(otherStudentWithSuppliedEmail.isPresent()) {
				throw new IllegalStateException("email taken");
			}
			existingStudent.setEmail(email);
		}


	}



	public void updateStudentByRequestBody(Student student) {
		if (student.getId() == null) {
			 throw new IllegalStateException("student id must be not null");
		}

		if (student.getId() == 0 || !studentRepository.existsById(student.getId())) {
			 throw new ResourceNotFoundException("student with id " + student.getId() +  " does not exist");
		}

		//when request json is deserialized to Student object, Student.image field is populated with PhotoImage objects list
		//meanwhile PhotoImage.student object is not set; we must set is as student.image.student is the owning
		//side of manyToOne relationship and hibernate uses PhotoImage.student object to store reference in database
		//Warning - if in JSON there is image object with image belonging to other student that image is associated with current student
		//we must check is every image from request belongs to current student
		student.getImages().forEach((img)->{
			img.setStudent(student);

		});

		studentRepository.save(student);
	}


	public void addImage(Long studentId, PhotoImage image) {
		//if student exists add image to student
		Student existingStudent = studentRepository.findById(studentId)
				.orElseThrow(()-> new ResourceNotFoundException(
					"student with id " + studentId + " does not exist"));

		image.setStudent(existingStudent);
		photoImageRepository.save(image);
	}


	@Transactional
	public void deleteImage(Long studentId, Long imageId) {
		//fetch student with all his images
		Student existingStudent = studentRepository.findStudentAndCountryByStudentId(studentId).orElseThrow(
			()-> new ResourceNotFoundException("student with id " + studentId + " not found"));

		//delete image only if it belongs to student
		PhotoImage img = new PhotoImage();
		img.setId(imageId);
		
		if( !existingStudent.getImages().contains(img)) {
        	throw new ResourceNotFoundException("image with id " + imageId + " belonging to student with id " +
        			studentId +  " not found");
        }
		
		existingStudent.removeImage(img);
	}

}
