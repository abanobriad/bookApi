package com.bookstore.book.enumration;

import java.util.HashMap;
import java.util.Map;

public enum BookType {
	PROGRAMING_BOOK (1,0.0), SCIENTIFIC_BOOK (2,0.0), FICTION_BOOK (3,10.0), COMIC_BOOK (4,0.0);
	
	public final int bookType;
	
	public final Double discount;
	
	private static final Map<String, BookType> TYPEMAP = new HashMap<String, BookType>();
    
    static {
        for (BookType bt: values()) {
        	TYPEMAP.put(""+bt.bookType, bt);
        }
    }
	
	private BookType(int type,Double discount) {
		this.bookType=type;
		this.discount=discount;
	}
	
	public static BookType valueByTypeNumber(String bookType) {
        return TYPEMAP.get(bookType);
    }

}
