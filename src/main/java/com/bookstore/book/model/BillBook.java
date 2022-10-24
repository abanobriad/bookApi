package com.bookstore.book.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class BillBook implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	
	@ManyToOne
    @JoinColumn(name = "bookId")
    private Book book;

	@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "billId")
    private Bill bill;
    
    private Integer count;
    
    private Double totalPrice;
    
    private Double totalDiscount;
    
    private Double discountPercentage;

	public Integer getId() {
		return id;
	}

	public Book getBook() {
		return book;
	}

	public Bill getBill() {
		return bill;
	}

	public Integer getCount() {
		return count;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
    
    
    
    

}
