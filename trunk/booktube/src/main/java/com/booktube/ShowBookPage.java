package com.booktube;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.value.ValueMap;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.booktube.model.Book;
import com.booktube.model.Comment;
import com.booktube.model.User;
import com.booktube.service.BookService;
import com.booktube.service.UserService;

public class ShowBookPage extends BasePage {

	@SpringBean
	BookService bookService;

	@SpringBean
	UserService userService;

	/** backwards nav page */
	// private final Page backPage;
	private List<User> users = userService.getAllUsers();

	/*
	 * public ShowBookPage(Page backPage) { // this(backPage, new Book());
	 * this(backPage, new PageParameters()); }
	 */

	/*
	 * public ShowBookPage(final Page backPage, final PageParameters parameters)
	 * {
	 * 
	 * // this.backPage = backPage;
	 * 
	 * Integer bookId = parameters.getAsInteger("book");
	 * 
	 * final Book book = bookService.getBook(bookId);
	 * 
	 * final WebMarkupContainer parent = new WebMarkupContainer("bookDetails");
	 * parent.setOutputMarkupId(true); add(parent);
	 * 
	 * // add(new Label("bookId", book.getId().toString())); parent.add(new
	 * Label("title", book.getTitle())); parent.add(new Label("author",
	 * book.getAuthor().getUsername())); parent.add(new MultiLineLabel("text",
	 * book.getText())); parent.add(new Label("publishDate",
	 * book.getPublishDate().toString()));
	 * 
	 * parent.add(new Link("goBack") { public void onClick() {
	 * setResponsePage(backPage); } });
	 * 
	 * }
	 */

	public ShowBookPage(final PageParameters parameters) {

		// this.backPage = backPage;

		Integer bookId = parameters.getAsInteger("book");

		final Book book = bookService.getBook(bookId);

		final WebMarkupContainer parent = new WebMarkupContainer("bookDetails");
		parent.setOutputMarkupId(true);
		add(parent);

		// add(new Label("bookId", book.getId().toString()));
		parent.add(new Label("title", book.getTitle()));
		parent.add(new Label("author", book.getAuthor().getUsername()));
		parent.add(new MultiLineLabel("text", book.getText()));
		parent.add(new Label("publishDate", book.getPublishDate().toString()));

		parent.add(new Link("goBack") {
			public void onClick() {
				setResponsePage(HomePage.class);
			}
		});

		List<Comment> comments = new ArrayList<Comment>(book.getComments());

		parent.add(commentList("comments", comments));

		parent.add(addCommentForm(parent, book, comments));

	}

	private PropertyListView commentList(String label, List<Comment> comments) {

		PropertyListView commentsPLV = new PropertyListView(label, comments) {

			protected void populateItem(ListItem item) {
				Comment comment = (Comment) item.getModelObject();
				// item.add(new Label("author",
				// comment.getUser().getUsername()));
				// item.add(new Label("author", "este es el autor!!"));
				// item.add(new MultiLineLabel("comment", comment.getText()));
				item.add(new Label("author", comment.getUser().getUsername()));
				item.add(new MultiLineLabel("comment", comment.getText()));
				item.add(new Label("date", comment.getDate().toString()));
			}
		};

		return commentsPLV;

	}

	private Form addCommentForm(final WebMarkupContainer parent,
			final Book book, final List<Comment> comments) {

		Form form = new Form("form");

		final TextArea editor = new TextArea("textArea");
		editor.setOutputMarkupId(true);

		final DropDownChoice ddc = new DropDownChoice("usernameList",
				new Model(users.get(0)), users, new ChoiceRenderer("username",
						"id"));

		ValueMap myParameters = new ValueMap();
		myParameters.put("usernameList", users.get(0));
		form.setModel(new CompoundPropertyModel(myParameters));
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
				// System.out.println("ACA 1");
				String text = editor.getDefaultModelObjectAsString();
				String username = ddc.getDefaultModelObjectAsString();

				// User user =
				// WicketApplication.instance().getUserService().getUser(username);
				// Book book = new Book(title, text, user);

				User user = userService.getUser(username);
				// User user = new User("usuario", "nombre", "apellido");
				System.out.println("user es " + user);
				// Comment comment = new Comment(user, book, text);

				Comment comment = book.addComment(user, text);
				// bookService.insertComment(comment);
				bookService.updateBook(book);
				// setResponsePage(HomePage.class);

				/* Insert comment */
				// WicketApplication.instance().getBookService().insertBook(book);
				System.out.println("Comment inserted.");
				System.out.println("Author: " + username);
				System.out.println("Comment: " + text);

				comments.add(comment);

				/* Clear values */
				editor.setModel(new Model(""));

				target.addComponent(parent);

			}
		});

		return form;

	}

}
