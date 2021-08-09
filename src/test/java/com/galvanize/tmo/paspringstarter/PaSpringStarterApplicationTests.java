package com.galvanize.tmo.paspringstarter;

import com.galvanize.tmo.paspringstarter.helper.JsonMarshalHelper;
import com.galvanize.tmo.paspringstarter.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaSpringStarterApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Test
	void isHealthy() throws Exception {
		mockMvc.perform(get("/api/health"))
				.andExpect(status().isOk());
	}

	@Test
	void getBookList_Empty() throws Exception {
		String uri = "/api/books";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		List<Book> libraryBooks = JsonMarshalHelper.mapFromJson(content, List.class);
		assertTrue(libraryBooks.size() == 0);
	}

	@Test
	void addBooks_ExpectListInSortedOrderOfTitle() throws Exception {
		String uri = "/api/books";

		// Create Book 1
		Book bookOne = new Book();
		bookOne.setTitle("The Hitchhiker's Guide to the Galaxy");
		bookOne.setAuthor("Douglas Adams");
		bookOne.setYearPublished(1979);
		String inputJson = JsonMarshalHelper.mapToJson(bookOne);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
		String content = mvcResult.getResponse().getContentAsString();
		Book createdBook = JsonMarshalHelper.mapFromJson(content, Book.class);
		assertNotNull(createdBook.getId());

		// Create Book 2
		Book bookTwo = new Book();
		bookTwo.setTitle("Do Androids Dream of Electric Sheep?");
		bookTwo.setAuthor("Philip K. Dick");
		bookTwo.setYearPublished(1968);
		inputJson = JsonMarshalHelper.mapToJson(bookTwo);

		mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson)).andReturn();
		status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
		content = mvcResult.getResponse().getContentAsString();
		createdBook = JsonMarshalHelper.mapFromJson(content, Book.class);
		assertNotNull(createdBook.getId());

		// Create Book 3
		Book bookThree = new Book();
		bookThree.setTitle("Neuromancer");
		bookThree.setAuthor("William Gibson");
		bookThree.setYearPublished(1984);
		inputJson = JsonMarshalHelper.mapToJson(bookThree);

		mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson)).andReturn();
		status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
		content = mvcResult.getResponse().getContentAsString();
		createdBook = JsonMarshalHelper.mapFromJson(content, Book.class);
		assertNotNull(createdBook.getId());

		// GET all books in sorted order of title
		mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		content = mvcResult.getResponse().getContentAsString();
		List<Book> libraryBooks = JsonMarshalHelper.mapFromJsonToList(content, Book[].class);
		assertTrue(libraryBooks.size() == 3);
		// sorted by title test
		assertEquals(libraryBooks.get(0).getTitle(), bookTwo.getTitle());
		assertEquals(libraryBooks.get(1).getTitle(), bookThree.getTitle());
		assertEquals(libraryBooks.get(2).getTitle(), bookOne.getTitle());


		// DELETE all books and expect 204 status and empty body
		mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson)).andReturn();
		status = mvcResult.getResponse().getStatus();
		assertEquals(204, status);
		assertEquals(mvcResult.getResponse().getContentAsString(), "");

		// Expect 0 count since all books were deleted
		mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		content = mvcResult.getResponse().getContentAsString();
		libraryBooks = JsonMarshalHelper.mapFromJsonToList(content, Book[].class);
		assertTrue(libraryBooks.size() == 0);
	}
}
