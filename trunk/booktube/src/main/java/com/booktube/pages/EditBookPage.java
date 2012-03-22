package com.booktube.pages;

import java.util.List;
import java.util.Set;

import org.apache.wicket.Page;
import org.apache.wicket.request.mapper.parameter.PageParameters;
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

public class EditBookPage extends BasePage {

	@SpringBean
	BookService bookService;

	@SpringBean
	UserService userService;
	/** backwards nav page */
	// private final Page backPage;

	private List<User> users = userService.getAllUsers(0, Integer.MAX_VALUE);

	public EditBookPage(Long bookId, final WebPage backPage) {

		// this.backPage = backPage;
		//Integer bookId = book.getId();

		final Book book = bookService.getBook(bookId);

		if (book == null) {
			setResponsePage(HomePage.class);
			return;
		}

		add(new Label("bookId", book.getId().toString()));

		add(editBookForm(book));

		//setResponsePage(backPage);
		//goToLastPage();
	}

	/*
	private Page getPreviousPage() {
		PageMap defaultPageMap = (PageMap) getSession().getDefaultPageMap();
		ArrayListStack accessStack = ((AccessStackPageMap) defaultPageMap).getAccessStack();
		Access access = (Access) accessStack
				.get(accessStack.size() - 2);
		Page page = defaultPageMap.getEntry(access.getId()).getPage();
		return page;
	}
	*/

	/*
	 * public EditBookPage(Page backPage) { //this(backPage, new Book());
	 * this(backPage, new PageParameters()); }
	 */
	/*
	 * public EditBookPage(final PageParameters parameters) {
	 * 
	 * //this.backPage = backPage; Integer bookId =
	 * parameters.getAsInteger("book");
	 * 
	 * final Book book = bookService.getBook(bookId);
	 * 
	 * if ( book == null ) { setResponsePage(HomePage.class); return ; }
	 * 
	 * add(new Label("bookId", book.getId().toString()));
	 * 
	 * add(editBookForm(book));
	 * 
	 * }
	 */

	/*
	 * public EditBookPage(final Page backPage, final PageParameters parameters)
	 * {
	 * 
	 * //this.backPage = backPage; System.out.println("************* ACA 3");
	 * Integer bookId = parameters.getAsInteger("book");
	 * 
	 * final Book book = bookService.getBook(bookId);
	 * 
	 * add(new Label("bookId", book.getId().toString()));
	 * 
	 * Form<Object> form = new Form<Object>("editBookForm");
	 * 
	 * final TextField<Book> titleField = new TextField<Book>("title", new
	 * Model(book.getTitle())); // titleField.setOutputMarkupId(true); //
	 * titleField.setMarkupId(getId()); form.add(titleField);
	 * 
	 * final TextArea<String> editor = new TextArea<String>("text", new Model(
	 * book.getText())); editor.setOutputMarkupId(true);
	 * 
	 * // final DropDownChoice ddc2 = new DropDownChoice("usernameList", //
	 * users);
	 * 
	 * final DropDownChoice ddc = new DropDownChoice("usernameList", new
	 * Model(book.getAuthor()), users, new ChoiceRenderer( "username", "id"));
	 * 
	 * // ValueMap myParameters = new ValueMap(); //
	 * myParameters.put("usernameList", users.get(0)); // form.setModel(new
	 * CompoundPropertyModel(myParameters)); form.add(ddc);
	 * 
	 * form.add(editor); form.add(new AjaxSubmitLink("save") {
	 * 
	 * @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form)
	 * { // comments.add(new Comment(new //
	 * User(ddc.getDefaultModelObjectAsString()), //
	 * editor.getDefaultModelObjectAsString())); // editor.setModel(new
	 * Model("")); // target.addComponent(parent); //
	 * target.focusComponent(editor); String text =
	 * editor.getDefaultModelObjectAsString(); String username =
	 * ddc.getDefaultModelObjectAsString(); String title =
	 * titleField.getDefaultModelObjectAsString();
	 * 
	 * User user = userService.getUser(username); Book newBook = new
	 * Book(book.getId(), title, text, user); book.setText(text);
	 * book.setTitle(title); book.setAuthor(user);
	 * 
	 * // Edit book bookService.updateBook(book);
	 * //bookService.editBook(book.getId(), newBook);
	 * 
	 * System.out.println("Book edited."); System.out.println("Title: " +
	 * title); System.out.println("Author: " + username);
	 * System.out.println("Text: " + text);
	 * 
	 * // Previous page //setResponsePage(backPage);
	 * setResponsePage(BooksPage.class);
	 * 
	 * } });
	 * 
	 * add(form);
	 * 
	 * }
	 */

	/*
	 * public EditBookPage(final Page backPage, final Book book) {
	 * 
	 * this.backPage = backPage;
	 * 
	 * add(new Label("bookId", book.getId().toString()));
	 * 
	 * Form<Object> form = new Form<Object>("editBookForm");
	 * 
	 * final TextField<Book> titleField = new TextField<Book>("title", new
	 * Model(book.getTitle())); // titleField.setOutputMarkupId(true); //
	 * titleField.setMarkupId(getId()); form.add(titleField);
	 * 
	 * final TextArea<String> editor = new TextArea<String>("text", new Model(
	 * book.getText())); editor.setOutputMarkupId(true);
	 * 
	 * // final DropDownChoice ddc2 = new DropDownChoice("usernameList", //
	 * users);
	 * 
	 * final DropDownChoice ddc = new DropDownChoice("usernameList", new
	 * Model(book.getAuthor()), users, new ChoiceRenderer( "username", "id"));
	 * 
	 * // ValueMap myParameters = new ValueMap(); //
	 * myParameters.put("usernameList", users.get(0)); // form.setModel(new
	 * CompoundPropertyModel(myParameters)); form.add(ddc);
	 * 
	 * form.add(editor); form.add(new AjaxSubmitLink("save") {
	 * 
	 * @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form)
	 * { // comments.add(new Comment(new //
	 * User(ddc.getDefaultModelObjectAsString()), //
	 * editor.getDefaultModelObjectAsString())); // editor.setModel(new
	 * Model("")); // target.addComponent(parent); //
	 * target.focusComponent(editor); String text =
	 * editor.getDefaultModelObjectAsString(); String username =
	 * ddc.getDefaultModelObjectAsString(); String title =
	 * titleField.getDefaultModelObjectAsString();
	 * 
	 * User user = WicketApplication.instance().getUserService()
	 * .getUser(username); Book newBook = new Book(book.getId(), title, text,
	 * user);
	 * 
	 * // Edit book WicketApplication.instance().getBookService()
	 * .editBook(book.getId(), newBook); System.out.println("Book edited.");
	 * System.out.println("Title: " + title); System.out.println("Author: " +
	 * username); System.out.println("Text: " + text);
	 * 
	 * // Previous page setResponsePage(backPage);
	 * 
	 * } });
	 * 
	 * add(form);
	 * 
	 * 
	 * }
	 */

	private Form editBookForm(final Book book) {
		Form<Object> form = new Form<Object>("editBookForm");

		final TextField<Book> titleField = new TextField<Book>("title",
				new Model(book.getTitle()));
		// titleField.setOutputMarkupId(true);
		// titleField.setMarkupId(getId());
		form.add(titleField);

		final TextField<Book> tagsField = new TextField<Book>("tags",
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
		form.add(new AjaxSubmitLink("save") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
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

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				
			}
		});

		return form;
	}

}
