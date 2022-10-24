package com.bookstore.book;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.bookstore.book.enumration.BookType;
import com.bookstore.book.model.Bill;
import com.bookstore.book.model.BillBook;
import com.bookstore.book.model.Book;
import com.bookstore.book.repository.BillRepository;
import com.bookstore.book.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookApiRestControllerTest {
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final ObjectMapper om = new ObjectMapper();
	@Autowired
	BookRepository bookRepository;

	@Autowired
	BillRepository billRepository;

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void setup() {
		bookRepository.deleteAll();
		billRepository.deleteAll();
		om.setDateFormat(simpleDateFormat);
	}

	//Test save service
	@Test
	public void testSaveService() throws Exception {
		Book expectedRecord = getTestData().get("java");
		Book actualRecord = om.readValue(mockMvc
				.perform(post("/saveBook").contentType("application/json")
						.content(om.writeValueAsString(getTestData().get("java"))))
				.andDo(print()).andExpect(jsonPath("$.id", greaterThan(0))).andExpect(status().isCreated()).andReturn()
				.getResponse().getContentAsString(), Book.class);

		Assert.assertTrue(new ReflectionEquals(expectedRecord, "id").matches(actualRecord));
		assertEquals(true, bookRepository.findById(actualRecord.getId()).isPresent());
	}

	//Test Get List Service
	@Test
	public void testGETListService() throws Exception {
		Map<String, Book> data = getTestData();
		data.remove("java");
		List<Book> expectedRecords = new ArrayList<>();

		for (Map.Entry<String, Book> kv : data.entrySet()) {
			expectedRecords
					.add(om.readValue(
							mockMvc.perform(post("/saveBook").contentType("application/json")
									.content(om.writeValueAsString(kv.getValue()))).andDo(print())
									.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(),
							Book.class));
		}
		Collections.sort(expectedRecords, Comparator.comparing(Book::getId));

		List<Book> actualRecords = om.readValue(
				mockMvc.perform(get("/findAllBook")).andDo(print()).andExpect(jsonPath("$.*", isA(ArrayList.class)))
						.andExpect(jsonPath("$.*", hasSize(expectedRecords.size()))).andExpect(status().isOk())
						.andReturn().getResponse().getContentAsString(),
				new TypeReference<List<Book>>() {
				});

		for (int i = 0; i < expectedRecords.size(); i++) {
			Assert.assertTrue(new ReflectionEquals(expectedRecords.get(i)).matches(actualRecords.get(i)));
		}
	}

	//Test get book by id
	@Test
	public void testGETByIdService() throws Exception {
		Map<String, Book> data = getTestData();
		List<Book> records = new ArrayList<>();

		for (Map.Entry<String, Book> kv : data.entrySet()) {
			records.add(
					om.readValue(
							mockMvc.perform(post("/saveBook").contentType("application/json")
									.content(om.writeValueAsString(kv.getValue()))).andDo(print())
									.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(),
							Book.class));
		}
		Collections.sort(records, Comparator.comparing(Book::getId));
		Book expectedRecord = records.get(1);

		Book actualRecord = om.readValue(mockMvc.perform(get("/book/" + expectedRecord.getId())).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<Book>() {
				});

		Assert.assertTrue(new ReflectionEquals(expectedRecord).matches(actualRecord));

	}

	//test edit service
	@Test
	public void testEditService() throws Exception {
		Map<String, Book> data = getTestData();
		List<Book> records = new ArrayList<>();

		for (Map.Entry<String, Book> kv : data.entrySet()) {
			records.add(
					om.readValue(
							mockMvc.perform(post("/saveBook").contentType("application/json")
									.content(om.writeValueAsString(kv.getValue()))).andDo(print())
									.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(),
							Book.class));
		}
		Collections.sort(records, Comparator.comparing(Book::getId));
		Book updatedRecord = records.get(0);

		Book newRecord = new Book("Java11", "Learning latest version of java programing launguage", "Mark",
				BookType.PROGRAMING_BOOK.bookType, 490.99, "10111");
		newRecord.setId(updatedRecord.getId());

		Book actualRecord = om.readValue(mockMvc
				.perform(put("/editBook").contentType("application/json").content(om.writeValueAsString(newRecord)))
				.andDo(print()).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(),
				Book.class);

		Assert.assertTrue(new ReflectionEquals(newRecord).matches(actualRecord));

	}

	//test delete service
	@Test
	public void testDeleteService() throws Exception {
		Map<String, Book> data = getTestData();
		List<Book> records = new ArrayList<>();

		for (Map.Entry<String, Book> kv : data.entrySet()) {
			records.add(
					om.readValue(
							mockMvc.perform(post("/saveBook").contentType("application/json")
									.content(om.writeValueAsString(kv.getValue()))).andDo(print())
									.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(),
							Book.class));
		}
		Collections.sort(records, Comparator.comparing(Book::getId));

		Book deletedRecord = records.get(1);

		deletedRecord = om.readValue(mockMvc.perform(get("/deleteBook/" + deletedRecord.getId())).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<Book>() {
				});

		List<Book> allRecordsAfterDelete = om.readValue(
				mockMvc.perform(get("/findAllBook")).andDo(print()).andExpect(jsonPath("$.*", isA(ArrayList.class)))
						.andExpect(jsonPath("$.*", hasSize(records.size() - 1))).andExpect(status().isOk()).andReturn()
						.getResponse().getContentAsString(),
				new TypeReference<List<Book>>() {
				});

		for (int i = 0; i < allRecordsAfterDelete.size(); i++) {
			Assert.assertFalse(new ReflectionEquals(deletedRecord).matches(allRecordsAfterDelete.get(i)));
		}

	}

	//test checkout service with book that have discount and book that have not discount
	@Test
	public void testCheckoutService() throws Exception {
		Map<String, Book> data = getTestData();
		List<Book> records = new ArrayList<>();

		for (Map.Entry<String, Book> kv : data.entrySet()) {
			records.add(
					om.readValue(
							mockMvc.perform(post("/saveBook").contentType("application/json")
									.content(om.writeValueAsString(kv.getValue()))).andDo(print())
									.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(),
							Book.class));
		}
		Collections.sort(records, Comparator.comparing(Book::getId));

		Bill bill = om.readValue(mockMvc
				.perform(post("/checkout").contentType("application/json")
						.content(om.writeValueAsString(getCheckoutTestData())))
				.andDo(print()).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(),
				Bill.class);

		List<BillBook> bookList = new ArrayList<BillBook>(bill.getBooks());

		for (BillBook billBook : bookList) {
			// fiction book that have discount 10%
			if (billBook.getBook().getType() == BookType.FICTION_BOOK.bookType) {
				assertEquals(
						(billBook.getBook().getPrice() * billBook.getCount())
						- ((billBook.getBook().getPrice() * BookType.FICTION_BOOK.discount / 100)
								* billBook.getCount()),
						billBook.getTotalPrice().doubleValue(), 0.5);
			} else {// other book that have discount 0%
				assertEquals((billBook.getBook().getPrice() * billBook.getCount()),
						billBook.getTotalPrice().doubleValue(), 0.5);
			}
		}

	}

	// get test data

	private Map<String, Book> getTestData() throws ParseException {
		Map<String, Book> data = new LinkedHashMap<>();

		Book javaBook = new Book("Java", "Learning java programing launguage", "Mark",
				BookType.PROGRAMING_BOOK.bookType, 300.99, "10111");
		data.put("java", javaBook);

		Book anatomyBook = new Book("human_anatomy ", "contain all details about anatomy", "peter",
				BookType.SCIENTIFIC_BOOK.bookType, 1200.99, "11101");
		data.put("anatomy", anatomyBook);

		Book sqlBook = new Book("fiction_story", "fiction story", "ahmed", BookType.FICTION_BOOK.bookType, 970.99,
				"11001");
		data.put("fiction", sqlBook);

		Book mickyStory = new Book("mickyStory ", "story", "jack", BookType.COMIC_BOOK.bookType, 200.99, "10001");
		data.put("mickyStory", mickyStory);

		return data;
	}

	// get checkout test data
	private Set<BillBook> getCheckoutTestData() throws ParseException {
		Set<BillBook> selectedBooks = new HashSet<BillBook>();
		// first book
		BillBook book1 = new BillBook();
		book1.setBook(bookRepository.findByName("Java"));
		book1.setCount(3);
		selectedBooks.add(book1);

		// second book
		BillBook book2 = new BillBook();
		book2.setBook(bookRepository.findByName("fiction_story"));
		book2.setCount(5);
		selectedBooks.add(book2);
		return selectedBooks;
	}
}
