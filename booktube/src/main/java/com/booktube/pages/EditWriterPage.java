package com.booktube.pages;

import java.util.List;
import java.util.Set;

import org.apache.wicket.AccessStackPageMap;
import org.apache.wicket.AccessStackPageMap.Access;
import org.apache.wicket.Page;
import org.apache.wicket.PageMap;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.collections.ArrayListStack;
import org.apache.wicket.util.value.ValueMap;

import com.booktube.model.Book;
import com.booktube.model.User;
import com.booktube.service.BookService;
import com.booktube.service.UserService;

public class EditWriterPage extends BasePage {

	@SpringBean
	BookService bookService;

	@SpringBean
	UserService userService;
	/** backwards nav page */
	// private final Page backPage;

	private List<User> users = userService.getAllUsers(0, Integer.MAX_VALUE);

	public EditWriterPage(Long userId, final WebPage backPage) {

		// this.backPage = backPage;
		//Integer bookId = book.getId();

		final User user = userService.getUser(userId);

		if (user == null) {
			setResponsePage(HomePage.class);
			return;
		}

		add(new Label("writerId", user.getId().toString()));

		add(editWriterForm(user));

		//setResponsePage(backPage);
		goToLastPage();
	}

	private Form editWriterForm(final User writer) {
		Form<Object> form = new Form<Object>("editWriterForm");

		final TextField<User> usernameField = new TextField<User>("username",
				new Model(writer.getUsername()));
		form.add(usernameField);
		
		final TextField<User> firstnameField = new TextField<User>("firstname",
				new Model(writer.getFirstname()));
		form.add(firstnameField);
		
		final TextField<User> lastnameField = new TextField<User>("lastname",
				new Model(writer.getLastname()));
		form.add(lastnameField);

		/*final TextField<Book> tagsField = new TextField<Book>("tags",
				new Model(book.getTags().toString().substring(1, book.getTags().toString().length() - 1)));
		// titleField.setOutputMarkupId(true);
		// titleField.setMarkupId(getId());
		form.add(tagsField);
		
		final TextArea<String> editor = new TextArea<String>("text", new Model(
				book.getText()));
		editor.setOutputMarkupId(true);

		// final DropDownChoice ddc2 = new DropDownChoice("usernameList",
		// users);

		final DropDownChoice ddc = new DropDownChoice("usernameList",
				new Model(book.getAuthor()), users, new ChoiceRenderer(
						"username", "id"));

		// ValueMap myParameters = new ValueMap();
		// myParameters.put("usernameList", users.get(0));
		// form.setModel(new CompoundPropertyModel(myParameters));
		form.add(ddc);

		form.add(editor);
*/
		form.add(new AjaxSubmitLink("save") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				/*
				// comments.add(new Comment(new
				// User(ddc.getDefaultModelObjectAsString()),
				// editor.getDefaultModelObjectAsString()));
				// editor.setModel(new Model(""));
				// target.addComponent(parent);
				// target.focusComponent(editor);
				String text = editor.getDefaultModelObjectAsString();
				String username = ddc.getDefaultModelObjectAsString();
				String title = titleField.getDefaultModelObjectAsString();

				User user = userService.getUser(username);
				book.setText(text);
				book.setAuthor(user);
				book.setTitle(title);
				// Book newBook = new Book(book.getId(), title, text, user);

				// Edit book
				// bookService.editBook(book.getId(), newBook);
				bookService.updateBook(book);

				System.out.println("Book edited.");
				System.out.println("Title: " + book.getTitle());
				System.out.println("Author: " + book.getAuthor());
				System.out.println("Text: " + book.getText());

				// Previous page
				// setResponsePage(backPage);
				setResponsePage(BooksPage.class);
				*/
			}
		});

		return form;
	}

}
