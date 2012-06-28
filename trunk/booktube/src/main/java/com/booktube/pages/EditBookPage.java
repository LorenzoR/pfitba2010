package com.booktube.pages;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.model.Book;
import com.booktube.model.User;
import com.booktube.pages.customComponents.CustomTextField;
import com.booktube.pages.customConverters.TagSetToString;
import com.booktube.service.BookService;
import com.booktube.service.UserService;

public class EditBookPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	BookService bookService;

	@SpringBean
	UserService userService;

	private List<User> users = userService.getAllUsers(0, Integer.MAX_VALUE);
	private final Book book;

	public EditBookPage(PageParameters pageParameters) {

		Long bookId = pageParameters.get("book").toLong();
		int currentPage;

		if (pageParameters.get("currentPage").isEmpty()) {
			currentPage = 0;
		} else {
			currentPage = pageParameters.get("currentPage").toInt();
		}

		book = bookService.getBook(bookId);

		if (book == null) {
			setResponsePage(HomePage.class);
			return;
		}

		add(new Label("bookId", book.getId().toString()));

		add(editBookForm(book, currentPage));

		String newTitle = "Booktube - Edit " + book.getTitle();
		super.get("pageTitle").setDefaultModelObject(newTitle);

	}

	private Form<Book> editBookForm(final Book book, final int currentPage) {
		Form<Book> form = new Form<Book>("editBookForm");

		CompoundPropertyModel<Book> model = new CompoundPropertyModel<Book>(
				book);

		form.setDefaultModel(model);

		final TextField<Book> titleField = new TextField<Book>("title");
		form.add(titleField);

//		String tags = book.getTags().toString()
//				.substring(1, book.getTags().toString().length() - 1)
//				.replace(",", "");

		// final TextField<String> tagsField = new TextField<String>("tags",
		// new Model<String>(tags));
		// form.add(tagsField);

		// final CustomTextField tagsField = new CustomTextField("tags",
		// book.getTags(),
		// new TagSetToString());
		// form.add(tagsField);

//		final TextField<Book> tagsField = new TextField<Book>("tags") {
//			private static final long serialVersionUID = 1L;
//
//			@SuppressWarnings("unchecked")
//			@Override
//			public IConverter getConverter(Class type) {
//				return new TagSetToString(book);
//			}
//
//		};
//		form.add(tagsField);
		
		final CustomTextField tagField = new CustomTextField("tags", null,
				new TagSetToString());
		form.add(tagField);

		final TextArea<Book> editor = new TextArea<Book>("text");
		editor.setOutputMarkupId(true);

		// final DropDownChoice ddc2 = new DropDownChoice("usernameList",
		// users);

		final DropDownChoice<User> ddc = new DropDownChoice<User>("author",
				new Model<User>(book.getAuthor()), users);

		// final DropDownChoice<User> ddc = new
		// DropDownChoice<User>("usernameList",
		// new Model<User>(book.getAuthor()), users, new ChoiceRenderer<User>(
		// "username", "id"));

		// ValueMap myParameters = new ValueMap();
		// myParameters.put("usernameList", users.get(0));
		// form.setModel(new CompoundPropertyModel(myParameters));
		form.add(ddc);

		form.add(editor);
		form.add(new AjaxSubmitLink("save") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// comments.add(new Comment(new
				// User(ddc.getDefaultModelObjectAsString()),
				// editor.getDefaultModelObjectAsString()));
				// editor.setModel(new Model(""));
				// target.addComponent(parent);
				// target.focusComponent(editor);
				System.out.println("****** NEW BOOK: " + book.getTitle());
				System.out.println("****** NEW BOOK: " + book.getAuthor());
				System.out.println("****** NEW BOOK: " + book.getTags().toString());


				bookService.updateBook(book);

				System.out.println("Book edited.");
				System.out.println("Title: " + book.getTitle());
				System.out.println("Author: " + book.getAuthor());
				System.out.println("Text: " + book.getText());

				// Previous page
				// setResponsePage(backPage);
				PageParameters pageParameters = new PageParameters();
				pageParameters.set("currentPage", currentPage);
				setResponsePage(BooksPage.class, pageParameters);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}
		});

		return form;
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		// String newTitle = "Booktube - Edit " + book.getTitle();
		// super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}
