package com.booktube;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.value.ValueMap;

import com.booktube.model.Book;
import com.booktube.model.User;
import com.booktube.service.BookService;
import com.booktube.service.UserService;

public class AddBookPage extends BasePage {

	@SpringBean
	UserService userService;
	
	@SpringBean
	BookService bookService;
	
	private List<User> users = userService.getAllUsers();
	
	public AddBookPage() {

		final WebMarkupContainer parent = new WebMarkupContainer("books");
		parent.setOutputMarkupId(true);
		add(parent);
		
		Form form = addBookForm(parent);
		
		parent.add(form);
		
	}
	
	private Form addBookForm(final WebMarkupContainer parent) {
		Form form = new Form("form");

		final TextField titleField = new TextField("title", new Model(""));
		//titleField.setOutputMarkupId(true);
		//titleField.setMarkupId(getId());
		form.add(titleField);
		
		final TextArea editor = new TextArea("textArea");
		editor.setOutputMarkupId(true);
		
		final DropDownChoice ddc = new DropDownChoice("usernameList", new Model(users.get(0)), users, new ChoiceRenderer("username", "id"));
		
		ValueMap myParameters = new ValueMap();
		myParameters.put("usernameList", users.get(0)); 
		form.setModel(new CompoundPropertyModel(myParameters)); 
		form.add(ddc);
		
		form.add(editor);
		form.add(new AjaxSubmitLink("save") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				//comments.add(new Comment(new User(ddc.getDefaultModelObjectAsString()), editor.getDefaultModelObjectAsString()));
				//editor.setModel(new Model(""));
				//target.addComponent(parent);
				//target.focusComponent(editor);
				//System.out.println("ACA 1");
				String text = editor.getDefaultModelObjectAsString();
				String username = ddc.getDefaultModelObjectAsString();
				String title = titleField.getDefaultModelObjectAsString();
				
				User user = userService.getUser(username);
				Book book = new Book(title, text, user);
				
				/* Insert book */
				bookService.insertBook(book);
				System.out.println("Book inserted.");
				System.out.println("Title: " + title);
				System.out.println("Author: " + username);
				System.out.println("Text: " + text);
				
				/* Clear values */
				editor.setModel(new Model(""));
				titleField.setModel(new Model(""));
				target.addComponent(parent);
				setResponsePage(HomePage.class);
				
			}
		});
		
		return form;
	}
	

	
}
