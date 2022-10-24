package com.bookstore.book.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.book.enumration.BookType;
import com.bookstore.book.model.Bill;
import com.bookstore.book.model.BillBook;
import com.bookstore.book.model.Book;
import com.bookstore.book.repository.BillBookRepository;
import com.bookstore.book.repository.BillRepository;
import com.bookstore.book.repository.BookRepository;
import com.bookstore.book.service.BookService;

@Service
public class BookServiceImpl implements BookService{
	
	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	BillRepository billRepository;
	
	@Autowired
	BillBookRepository billBookRepository;
	
	@Autowired
	EntityManager em;
	
	
	@Override
	public Book save(Book book) {
		book=bookRepository.save(book);
		return book;
	}


	@Override
	public List<Book> searchBook(Book book,String sort) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Book> query = builder.createQuery(Book.class);
		Root<Book> root = query.from(Book.class);
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		//id
	    if(book.getId()!=null && book.getId()!=0)
	    {
	    	Predicate predicate = builder.equal(root.get("id"), book.getId());
	    	predicates.add(predicate);
	    }
	    //name
	    if(book.getName()!=null && !book.getName().equals(""))
	    {
	    	Predicate predicate = builder.like(root.get("name"), "%"+book.getName()+"%");
	    	predicates.add(predicate);
	    }
	    
	    
	  //description
	    if(book.getDescription()!=null && !book.getDescription().equals(""))
	    {
	    	Predicate predicate = builder.like(root.get("description"), "%"+book.getDescription()+"%");
	    	predicates.add(predicate);
	    }
	   
	    
	  //author
	    if(book.getAuthor()!=null && !book.getAuthor().equals(""))
	    {
	    	Predicate predicate = builder.like(root.get("author"), "%"+book.getAuthor()+"%");
	    	predicates.add(predicate);
	    }
	    
	  //type
	    if(book.getType()!=null && book.getType()!=0)
	    {
	    	Predicate predicate = builder.equal(root.get("type"), book.getType());
	    	predicates.add(predicate);
	    }
	    
	  //isbn
	    if(book.getIsbn()!=null && !book.getIsbn().equals(""))
	    {
	    	Predicate predicate = builder.like(root.get("isbn"), "%"+book.getIsbn()+"%");
	    	predicates.add(predicate);
	    }
	  
	    
	    query.select(root).where((Predicate[])predicates.toArray(new Predicate[predicates.size()]));
	    
	    query.orderBy(builder.asc(root.get("id")));
	    return this.em.createQuery(query).getResultList();
	}


	@Override
	public Book findById(Integer id) {
		return bookRepository.findById(id).get();
	}


	@Override
	public void deleteBook(Integer id) {
		bookRepository.deleteById(id);
	}


	@Override
	public List<Book> findAll() {
		return bookRepository.findAll();
	}


	@Override
	public Bill checkoutBooks(Set<BillBook> billBooks) {
		Bill newBill=new Bill();
		Set<BillBook> updatedBillBooks=new HashSet<BillBook>();
		newBill.setBillDate(new Date());
		//apply discount on books
		Double totalBillPrice=0.0;
		for(BillBook billBook:billBooks) {
				BookType bookType=BookType.valueByTypeNumber(""+billBook.getBook().getType());
				billBook.setDiscountPercentage(bookType.discount);
				if( billBook.getCount()!=null && billBook.getCount() > 0 ) {
					Double totalDiscount=((billBook.getDiscountPercentage()/100.00)*billBook.getBook().getPrice())*billBook.getCount();
					billBook.setTotalDiscount(totalDiscount);
					billBook.setTotalPrice((billBook.getBook().getPrice()*billBook.getCount())-totalDiscount);
				}
				
				billBook.setBill(newBill);
				
				totalBillPrice+=billBook.getTotalPrice();
				
				updatedBillBooks.add(billBook);
		}
		
		newBill.setTotalPrice(totalBillPrice);
		newBill.setBooks(updatedBillBooks);
		
		return billRepository.save(newBill);
	}



}
