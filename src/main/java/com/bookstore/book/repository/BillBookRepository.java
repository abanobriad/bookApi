package com.bookstore.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.book.model.BillBook;

public interface BillBookRepository extends JpaRepository<BillBook, Integer> {
}
