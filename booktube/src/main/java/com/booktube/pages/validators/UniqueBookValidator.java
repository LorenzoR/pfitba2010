package com.booktube.pages.validators;

import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.StringValidator;

import com.booktube.model.Book;
import com.booktube.service.BookService;

public class UniqueBookValidator extends StringValidator {

	private static final long serialVersionUID = 1L;

	@SpringBean
	BookService bookService;

	private String username;

	public UniqueBookValidator(String username) {
		this.username = username;
	}

	@Override
	protected void onValidate(IValidatable<String> validatable) {
//		List<Book> books = bookService.getBooks(0, Integer.MAX_VALUE, null,
//				username, null, null, null, null, null, null, null,
//				null);
		
		List<Book> books = bookService.findBookByTitle(validatable.getValue(), 0, Integer.MAX_VALUE);
		
		for ( Book aBook : books ) {
			if ( aBook.getTitle().equals(validatable.getValue()) && aBook.getAuthor().getUsername().equals(username) ) {
				error(validatable);
			}
		}
	}

}
