package com.booktube.pages;

import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.WicketApplication;
import com.booktube.model.Book;
import com.booktube.model.Rating;
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
		
		addBreadcrumb(new BookmarkablePageLink<Object>("link", EditBookPage.class, pageParameters), new ResourceModel("edit").getObject() + " " + book.getTitle());

		if (book == null) {
			setResponsePage(HomePage.class);
			return;
		}

		add(new Label("bookId", book.getId().toString()));

		add(editBookForm(book, currentPage));

		String newTitle = "Booktube - " + new ResourceModel("edit").getObject() + " " + book.getTitle();
		super.get("pageTitle").setDefaultModelObject(newTitle);

	}

	private Form<Book> editBookForm(final Book book, final int currentPage) {
		Form<Book> form = new Form<Book>("editBookForm");

		CompoundPropertyModel<Book> model = new CompoundPropertyModel<Book>(
				book);

		form.setDefaultModel(model);

		final RequiredTextField<Book> titleField = new RequiredTextField<Book>("title");
		form.add(titleField);
		
		final CustomTextField tagField = new CustomTextField("tags", null,
				new TagSetToString());
		form.add(tagField);

		final TextArea<Book> text = new TextArea<Book>("text");
		text.setOutputMarkupId(true);
		text.setRequired(true);
		form.add(text);

		final DropDownChoice<User> author = new DropDownChoice<User>("author",
				new Model<User>(book.getAuthor()), users);
		author.setRequired(true);
		form.add(author);
		
		final DateTextField publishDate = new DateTextField("publishDate",
				new PropertyModel<Date>(model, "publishDate"),
				new PatternDateConverter(WicketApplication.DATE_FORMAT, true));
		publishDate.setRequired(true);
		form.add(publishDate);
		
		form.add(new Label("date_format", WicketApplication.DATE_FORMAT_ES));

		final RequiredTextField<Book> category = new RequiredTextField<Book>("category");
		form.add(category);
		
		final TextField<Book> subcategory = new TextField<Book>("subcategory");
		form.add(subcategory);
		
		final TextField<Book> hits = new TextField<Book>("hits");
		form.add(hits);
		
		form.add(new ResetRatingLink("reset1", new Model<Rating>(book.getRating()), book));
		
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
	}
	
	/**
	 * Link to reset the ratings.
	 */
	private final class ResetRatingLink extends Link<Rating> {
		/** For serialization. */
		private static final long serialVersionUID = 1L;
		private final Book book;

		/**
		 * Constructor.
		 * 
		 * @param id
		 *            component id
		 * @param object
		 *            the model to reset.
		 */
		public ResetRatingLink(String id, IModel<Rating> object, Book book) {
			super(id, object);
			this.book = book;
		}

		/**
		 * @see Link#onClick()
		 */
		@Override
		public void onClick() {
			Rating rating = getModelObject();
			rating.setNrOfVotes(0);
			rating.setRating(0);
			rating.setSumOfRatings(0);
			rating.setBook(book);
		}
	}

}
