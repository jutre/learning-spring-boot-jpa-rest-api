package com.example.springbootrestapi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.reactive.function.client.ClientResponse.Headers;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;

import com.example.springbootrestapi.student.model.Country;
import com.example.springbootrestapi.student.model.PhotoImage;
import com.example.springbootrestapi.student.model.Student;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;


@SpringBootTest
@AutoConfigureMockMvc
//@Sql("classpath:data.sql")
class SpringBootRestApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	/* delete data from previous tests and load initial data */
	@BeforeAll
    static void setup(@Autowired DataSource dataSource) throws SQLException {
		Connection conn = dataSource.getConnection();
		ScriptUtils.executeSqlScript(conn, new ClassPathResource("data.sql"));
    }
	
	private Long existingCountryId = 2L;
	
	/* 
	 * sending json with birthCountry=null and
	 */
	@Test
	public void creatingStudentWithNullCountryShouldReturnBadRequest() throws Exception {
		
		System.out.println("----------------------------------------------------------------\n" +
				"creatingStudentWithNullCountry");
		
		Student newStudent = new Student(
			"Juris",
			"Strazdiņš",
			"Andris.Berzins1@gmail.com",
			LocalDate.of(1982, Month.OCTOBER,12),
			null);
		
		String requestBody = objectMapper.writeValueAsString(newStudent);
		
		mockMvc.perform(post("/api/v1/student")
			.contentType("application/json")
			.content(requestBody))
		.andDo(print())
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").value("invalid field values"))
		//validationErrors field contain only one array element - message about birthCountry validation error
		.andExpect(jsonPath("$.validationErrors.size()").value(1))
		.andExpect(jsonPath("$.validationErrors[0].birthCountry").isNotEmpty());
		
		
	}
	
	/* 
	 * sending json with birthCountry set, but birthCountry.id null 
	 */
	@Test
	public void creatingStudentWithNullCountryIdShouldReturnBadRequest() throws Exception {
		
		System.out.println("----------------------------------------------------------------\n" +
			"creatingStudentWithNullCountryId");

		Country country = new Country("Latvia");//country id is null (not initialized)
		Student newStudent = new Student(
			"Juris",
			"Strazdiņš",
			"Andris.Berzins1@gmail.com",
			LocalDate.of(1982, Month.OCTOBER,12),
			country);
		
		String requestBody = objectMapper.writeValueAsString(newStudent);
		
		mockMvc.perform(post("/api/v1/student")
			.contentType("application/json")
			.content(requestBody))
		.andDo(print())
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").value("student birthCoutry.id field value must not be null"));
		
		
	}
	
	/* 
	 * sending json with birthCountry set, but birthCountry value not positive 
	 */
	@Test
	public void creatingStudentWithNonPositiveCountryIdValueShouldReturnBadRequest() throws Exception {
		
		System.out.println("----------------------------------------------------------------\n" +
				"creatingStudentWithNonPositiveCountryIdValue");

		Country country = new Country("Latvia");
		country.setId(0L);
		Student newStudent = new Student(
			"Juris",
			"Strazdiņš",
			"Andris.Berzins1@gmail.com",
			LocalDate.of(1982, Month.OCTOBER,12),
			country);
		
		String requestBody = objectMapper.writeValueAsString(newStudent);
		
		mockMvc.perform(post("/api/v1/student")
			.contentType("application/json")
			.content(requestBody))
		.andDo(print())
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").value("student birthCountry.id field value must be at least 1"));
		
	}


	/* 
	 * sending valid Student json, after that retrieving it and check that email is that what 
	 * was sent  
	 */
	@Test
	public void creatingStudentShouldReturnLocationHeader() throws Exception {
		
		System.out.println("----------------------------------------------------------------\n" +
				"creating valid Student");

		Country country = new Country();
		country.setId(this.existingCountryId);
		
		Student newStudent = new Student(
			"Andris",
			"Berzins",
			"Andris.Berzins@gmail.com",
			LocalDate.of(1982, Month.OCTOBER,12),
			country);
		
		String requestBody = objectMapper.writeValueAsString(newStudent);
		
		MvcResult mvcResult = mockMvc.perform(post("/api/v1/student")
			.contentType("application/json")
			.content(requestBody))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().string(""))
				.andReturn();

		System.out.println("-- getting just created student--");
		
		String newStudentUrl = mvcResult.getResponse().getHeader("Location");

		//get newly created student by location and check students email
		this.mockMvc.perform(get(newStudentUrl))
		.andDo(print())
		.andExpect(status().isOk())
		//expect email the same as in creating POST request
		.andExpect(jsonPath("$.email").value(newStudent.getEmail()))
		//expect birthCountry id the same as in creating POST request
		.andExpect(jsonPath("$.birthCountry.id").value(this.existingCountryId));
		
	}
	
	
	
	
	@Test
	public void creatingStudentWithSameEmailShouldReturnConflictHeader() throws Exception {

		System.out.println("----------------------------------------------------------------\n" +
			"creatingStudentWithSameEmailShouldReturnConflictHeader");
		
		Country country = new Country();
		country.setId(this.existingCountryId);
		
		Student newStudent = new Student(
			"Andris",
			"Berzins2",
			"Andris.Berzins2@gmail.com",
			LocalDate.of(1982, Month.OCTOBER,12),
			country);
		
		String requestBody = objectMapper.writeValueAsString(newStudent);
		
		//create student initially
		mockMvc.perform(post("/api/v1/student")
			.contentType("application/json")
			.content(requestBody))
		.andDo(print())
		.andExpect(status().isCreated());
		
		System.out.println("--creating student with same email again--");
		
		//try creating student with same email again
		mockMvc.perform(post("/api/v1/student")
			.contentType("application/json")
			.content(requestBody))
		.andDo(print())
		.andExpect(status().isConflict())
		.andExpect(jsonPath("$.message").isString());
		
	}
	
	
	/* 
	 * sending valid Student json, after that retrieving it and check that email is that what 
	 * was sent  
	 */
	@Test
	public void testAddingStudentImages() throws Exception {
		
		System.out.println("----------------------------------------------------------------\n" +
			"testAddingStudentImages");

		//add new student
		String newStudentEmail = "Andris.Berzins3@gmail.com";
		Country country = new Country();
		country.setId(this.existingCountryId);
		
		Student newStudent = new Student(
			"Andris",
			"Berzins3",
			newStudentEmail,
			LocalDate.of(1982, Month.OCTOBER,12),
			country);
		
		String requestBody = objectMapper.writeValueAsString(newStudent);
		
		MvcResult mvcResult = mockMvc.perform(post("/api/v1/student")
			.contentType("application/json")
			.content(requestBody))
				.andDo(print())
				.andExpect(status().isCreated())
				.andReturn();

		
		//add image to student
		System.out.println("-- adding image to just created student--");
		
		String newStudentUrl = mvcResult.getResponse().getHeader("Location");
		
		String filename = "image 1";
		PhotoImage photoImage = new PhotoImage (filename);
		requestBody = objectMapper.writeValueAsString(photoImage);
		
		//url for adding images is "/api/v1/student/{student id}/images"
		mockMvc.perform(post(newStudentUrl + "/images")
			.contentType("application/json")
			.content(requestBody))
		.andDo(print())
		.andExpect(status().isCreated());

		
		//check if image posted is present with student we added image to and no more images
		mvcResult = this.mockMvc.perform(get(newStudentUrl))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.images.size()").value(1))
				.andExpect(jsonPath("$.images[0].filename").value(filename))
				.andReturn();
		
		
		//delete previously created image
		String responseJsonStr = mvcResult.getResponse().getContentAsString();
		Student responseStudent = objectMapper.readValue(responseJsonStr, Student.class);
		
		//url for deleting images is  "/api/v1/student/{student id}/images/{image id}"
		String imageUrl = newStudentUrl + "/images/" + responseStudent.getImages().get(0).getId();
		mockMvc.perform(delete(imageUrl))
		.andDo(print())
		.andExpect(status().isOk());
		
		//check there is no images belonging to student after deleting
		//students images resource is located under "/api/v1/student/{student id}/images" so we extend created student url
		this.mockMvc.perform(get(newStudentUrl))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.images.size()").value(0));
		
		//try to delete previously deleted image and expect error message
		mockMvc.perform(delete(imageUrl))
		.andDo(print())
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.message").value("image with id " + responseStudent.getImages().get(0).getId() + 
			" belonging to student with id " + responseStudent.getId() +  " not found"));
	}


	//@Test
	public void allStudentHttpCallEqualsFour() throws Exception {

		/*RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl = "http://localhost:8080/api/v1/student";
		ResponseEntity<String> response	= restTemplate.getForEntity(fooResourceUrl, String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		//System.out.println(response.getBody());
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());*/

		WebClient client = WebClient.builder()
				.baseUrl("http://localhost:8080")
				.build();

		RequestHeadersSpec<?> headersSpec  = client.get().uri("/api/v1/student");

		//List is a workaround to set get status code inside lambda expression body
		List<HttpStatus> statusList = new ArrayList<HttpStatus>();
		statusList.add(null);

		Mono<String> monoStringResponse = headersSpec.exchangeToMono(response -> {
			if (!response.statusCode()
				    .equals(HttpStatus.OK)) {
				fail();
			}
			statusList.set(0, response.statusCode());
			return response.bodyToMono(String.class);
		});

		//call monoStringResponse.block() before following header check as looks like
		//previous lambda expression function is no executed until monoStringResponse.block() is called
		String responseStr = monoStringResponse.block();


		assertThat(statusList.get(0), equalTo(HttpStatus.OK));


		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(responseStr);

		//student list not empty
		assertThat(root.size(), greaterThan(0));


		//		JsonNode id = root.get("id");
		//		JsonNode jsonNode1 = root.get("k1");
		//
		//System.out.println("eeeeeeeeeeeeeeee-" + jsonNode1.textValue() + ", " + (jsonNode1.textValue() == null) + ", " + (jsonNode1 == null));
		//assertThat(jsonNode1, nullValue());
		//assertThat(id, nullValue());System.out.println("is nul " + (id == null));
		//		RestTemplate restTemplate = new RestTemplate();
		//		String fooResourceUrl = "http://localhost:8080/api/v1/student";
		//		Student student = restTemplate.getForObject(fooResourceUrl + "/1", Student.class);
		//		assertThat(student.getFirstName(), notNullValue());
		//		assertThat(student.getId(), is(1L));
		//
		//		ResponseEntity<Student[]> studList = restTemplate.getForObject(fooResourceUrl , Student[].class);
		//		assertThat((Long)studList.length, 4L);
		//		assertThat(student.getId(), is(1L));
	}

	//@Test
	public void addNewStudent() throws Exception {
		WebClient client = WebClient.builder()
				.baseUrl("http://localhost:8080")
				.build();

		UriSpec<RequestBodySpec> uriSpec = client.post();
		RequestBodySpec bodySpec = uriSpec.uri("/api/v1/student");
		RequestHeadersSpec<?> headersSpec = bodySpec
				.bodyValue("{\"firstName\":\"Andris 4\","
						+ "\"lastName\":\"Berzins 4\","
						+ "\"email\":\"Andris4.Berzins41@gmail.com\","
						+ "\"dob\":\"1995-12-17\","
						+ "\"birthCountry\":{\"id\":2}}")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		/*
		 * after first Student creating http status code must be 201, "Location"
		 * header must be set, response body must be empty
		 */

		//List is a workaround to set get headers, status code inside lambda expression body
		List<HttpStatus> statusList = new ArrayList<HttpStatus>();
		statusList.add(null);
		List<Headers> headersList = new ArrayList<Headers>();
		headersList.add(null);
		Mono<String> monoStringResponse = headersSpec.exchangeToMono(response -> {
			statusList.set(0, response.statusCode());
			headersList.set(0, response.headers());
			return response.bodyToMono(String.class);
		});

		//call monoStringResponse.block() before following header check as looks like
		//previous lambda expression function is no executed until monoStringResponse.block() is called
		String responseStr = monoStringResponse.block();

		assertThat(responseStr, nullValue());


		assertThat(statusList.get(0), equalTo(HttpStatus.CREATED));


		assertThat(headersList.get(0).header("Location"), notNullValue());



		/*
		 * when trying to add Student with same email again, http status code must be 409 
		 * response body must contain error message, 
		 */

		statusList.set(0, null);//clear before update
		monoStringResponse = headersSpec.exchangeToMono(response -> {
			statusList.set(0, response.statusCode());
			return response.bodyToMono(String.class);
		});


		//error message description should be returned as response body; 
		//call monoStringResponse.block() before following header check as looks like previous
		//lambda expression function is no executed until monoStringResponse.block() is called 
		responseStr = monoStringResponse.block();

		assertThat(responseStr, notNullValue());


		assertThat(statusList.get(0), equalTo(HttpStatus.CONFLICT));

	}

}
