package com.bookstore.book.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.book.exception.BookNotFoundException;
import com.bookstore.book.model.Bill;
import com.bookstore.book.model.BillBook;
import com.bookstore.book.model.Book;
import com.bookstore.book.service.BookService;

@RestController
public class BookApiRestController {
	
	
	@Autowired
	BookService bookService;
	
	
	@PostMapping("/saveBook")
	@ResponseBody
	public ResponseEntity<Book> addBook(@RequestBody Book book){
		book=bookService.save(book);
		return new ResponseEntity<Book>(book,HttpStatus.CREATED);
	}
	
	
	@GetMapping("/findAllBook")
	@ResponseBody
	public ResponseEntity<List<Book>> findAllBook(){
		try {
			List<Book> books=bookService.findAll();
			if(books!=null && books.size()>0)
				return new ResponseEntity<List<Book>>(books,HttpStatus.OK);
			else
				throw new BookNotFoundException();
		} catch (Exception e) {
				throw new BookNotFoundException();
		}
		
	}
	
	@GetMapping("/book/{id}")
	@ResponseBody
	public ResponseEntity<Book> findBookById(@PathVariable String id){
		try {
			Integer bookId=Integer.parseInt(id);
			Book book=bookService.findById(bookId);
			if(book!=null)
				return new ResponseEntity<Book>(book,HttpStatus.OK);
			else
				throw new BookNotFoundException();
		} catch (Exception e) {
				throw new BookNotFoundException();
		}
		
	}
	
	
	@PutMapping("/editBook")
	@ResponseBody
	public ResponseEntity<Book> editBook(@RequestBody Book book){
		book=bookService.save(book);
		return new ResponseEntity<Book>(book,HttpStatus.CREATED);
	}
	
	
	@GetMapping("/deleteBook/{id}")
	@ResponseBody
	public ResponseEntity<Book> editBook(@PathVariable String id){
		Integer bookId=Integer.parseInt(id);
		Book deletedBook=bookService.findById(bookId);
		bookService.deleteBook(bookId);
		return new ResponseEntity<Book>(deletedBook,HttpStatus.OK);
	}
	
	
	
	@PostMapping("/checkout")
	@ResponseBody
	public ResponseEntity<Bill> checkout(@RequestBody Set<BillBook> books){
		Bill bill=bookService.checkoutBooks(books);
		return new ResponseEntity<Bill>(bill,HttpStatus.CREATED);
	}
	
	
	
	
	
	
	
	
	
}
