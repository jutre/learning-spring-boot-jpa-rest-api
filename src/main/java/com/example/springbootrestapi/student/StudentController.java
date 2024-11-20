package com.example.springbootrestapi.student;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.springbootrestapi.student.model.PhotoImage;
import com.example.springbootrestapi.student.model.Student;
import com.example.springbootrestapi.student.model.StudentExcludingCountry;
import com.example.springbootrestapi.util.InvalidRestInputException;
import com.example.springbootrestapi.util.ResourceAlreadyExistException;

@RestController
//@Validated
@RequestMapping(path = "api/v1/student")
public class StudentController {

	@Autowired
	private StudentService studentService;

	/*
	 * get all students
	 */
	@GetMapping
	public List<StudentExcludingCountry> getStudents() {
		return studentService.getStudents();
	}

	/*
	 * get single student
	 */
	@GetMapping(path = "{studentId}")
	public Student getStudent(@PathVariable Long studentId) {
		return studentService.getStudentById(studentId);
	}


	/*
	 * creating a new student
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)//send status code 201 after creating student
	public void registerNewStudent(@Valid @RequestBody Student student, HttpServletResponse response) {

		if(student.getBirthCountry().getId() == null) {
			throw new InvalidRestInputException("student birthCoutry.id field value must not be null");
		}
		
		if(student.getBirthCountry().getId() < 1) {
			throw new InvalidRestInputException("student birthCountry.id field value must be at least 1");
		}
		
		Student createdStudent = studentService.addNewStudent(student);

		//set "Location" header to created student's url
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
	            .path("/{id}")
	            .buildAndExpand(createdStudent.getId())
	            .toUri();
		response.setHeader("Location", uri.toString());
	}


	/*
	 * update student
	 */
	@PutMapping
	public void updateStudent(@RequestBody Student student) {
		studentService.updateStudentByRequestBody(student);
	}

	//update student first name, email
	/*@PutMapping(path = "{studentId}")
	public void updateStudent(
			@PathVariable("studentId") Long studentId,
			@RequestParam(required = false) String firstName,
			@RequestParam(required = false) String email){
		studentService.updateStudent(studentId, firstName, email);
	}*/


	/*
	 * delete student
	 */
	@DeleteMapping(path = "{studentId}")
	public void deleteStudent(@PathVariable("studentId") Long studentId) {
		studentService.deleteStudent(studentId);
	}


	/*
	 * add image to student
	 */
	@PostMapping(path = "{studentId}/images")
	@ResponseStatus(HttpStatus.CREATED)
	public void addImage(
			@PathVariable("studentId") Long studentId,
			@RequestBody PhotoImage image) {
		studentService.addImage(studentId, image);
	}


	/*
	 * delete student's image
	 */
	@DeleteMapping(path = "{studentId}/images/{imageId}")
	public void deleteImage(
			@PathVariable("studentId") Long studentId,
			@PathVariable("imageId") Long imageId) {
		studentService.deleteImage(studentId, imageId);
	}

}
