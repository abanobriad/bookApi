package com.bookstore.book.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Bill implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	
	private Date billDate;
	
	private Double totalPrice;
	
	@OneToMany(mappedBy = "bill",cascade = CascadeType.ALL)
	private Set<BillBook> books;

	public Integer getId() {
		return id;
	}

	public Date getBillDate() {
		return billDate;
	}

	public Set<BillBook> getBooks() {
		return books;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public void setBooks(Set<BillBook> books) {
		this.books = books;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	

}
