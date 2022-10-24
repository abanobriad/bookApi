package com.bookstore.book.service;

import java.util.List;
import java.util.Set;

import com.bookstore.book.model.Bill;
import com.bookstore.book.model.BillBook;
import com.bookstore.book.model.Book;

public interface BookService {
	Book save(Book book);
	void deleteBook(Integer id);
	List<Book> findAll();
	List<Book> searchBook(Book book,String sort);
	Book findById(Integer id);
	Bill checkoutBooks(Set<BillBook> billBooks);
}
