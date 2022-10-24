package com.bookstore.book.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Book implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	private String name;
	private String description;
	private String author;
	private Integer type;
	private Double price;
	private String isbn;
	
	@JsonIgnore
	@OneToMany(mappedBy = "book")
    private Set<BillBook> books;


    public Book(Integer id, String name, String description,  String author, Integer type, Double price,String isbn) {
        this.id = id;
        this.name=name;
        this.description=description;
        this.author=author;
        this.type = type;
        this.price = price;
        this.isbn = isbn;
    }

    public Book(String name, String description,  String author, Integer type, Double price,String isbn) {
    	this.name=name;
        this.description=description;
        this.author=author;
        this.type = type;
        this.price = price;
        this.isbn = isbn;
    }

    public Book() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getAuthor() {
		return author;
	}

	public Integer getType() {
		return type;
	}

	public Double getPrice() {
		return price;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Set<BillBook> getBooks() {
		return books;
	}

	public void setBooks(Set<BillBook> books) {
		this.books = books;
	}

	

    

    
    
   
	
	
    
}
