package com.bookstore.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.book.model.Bill;

public interface BillRepository extends JpaRepository<Bill, Integer> {
}
