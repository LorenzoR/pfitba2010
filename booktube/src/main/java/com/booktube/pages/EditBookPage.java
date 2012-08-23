package com.booktube.pages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.WiaSession;
import com.booktube.WicketApplication;
import com.booktube.model.Book;
import com.booktube.model.User;
import com.booktube.model.User.Level;
import com.booktube.pages.customComponents.SuccessDialog;
import com.booktube.pages.customComponents.TagTextField;
import com.booktube.service.BookService;
import com.booktube.service.UserService;

public class EditBookPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	BookService bookService;

	@SpringBean
	UserService userService;

	private SuccessDialog<?> successDialog;
	
	private List<User> users = userService.getAllUsers(0, Integer.MAX_VALUE);
	private final Book book;
	
	private User user;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EditBookPage(PageParameters pageParameters) {

		Long bookId = pageParameters.get("book").toLong();
		int currentPage;

		if (pageParameters.get("currentPage").isEmpty()) {
			currentPage = 0;
		} else {
			currentPage = pageParameters.get("currentPage").toInt();
		}
		
		user = WiaSession.get().getLoggedInUser();

		book = bookService.getBook(bookId);
		
		if ( user == null || ( user.getLevel() != Level.ADMIN && !user.getUsername().equals(book.getAuthor().getUsername()) ) ) {
			throw new AbortWithHttpErrorCodeException(404);
		}
		
		addBreadcrumb(new BookmarkablePageLink<Object>("link", EditBookPage.class, pageParameters), new ResourceModel("edit").getObject() + " " + book.getTitle());

		if (book == null) {
			setResponsePage(HomePage.class);
			return;
		}

		add(new Label("bookId", book.getId().toString()));

		add(editBookForm(book, currentPage));

		pageParameters.set("currentPage", currentPage);
		
		
		Class<?> targetPage = null;		
		if( WiaSession.get().getLoggedInUser().getUsername().compareTo(book.getAuthor().getUsername()) == 0 )
			targetPage = BooksPage.class;
		else
			targetPage = WorksAdministrationPage.class;
			
//		successDialog = new SuccessDialog<BooksPage>("success_dialog", new ResourceModel("editedBook").getObject(), BooksPage.class, pageParameters);
		successDialog = new SuccessDialog<WebPage>("success_dialog", new ResourceModel("editedBook").getObject(), (Class)targetPage, pageParameters);
		add(successDialog);
		
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
		
		final TagTextField tagField = new TagTextField("tags");
		form.add(tagField);

		final TextArea<Book> text = new TextArea<Book>("text");
		text.setOutputMarkupId(true);
		text.setRequired(true);
		form.add(text);

		final DropDownChoice<User> author = new DropDownChoice<User>("author",
				new Model<User>(book.getAuthor()), users);
		author.setRequired(true);
		form.add(author);
		
		final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, getLocale());
		
		final DateTextField publishDate = new DateTextField("publishDate",
				new PropertyModel<Date>(model, "publishDate"),
				new PatternDateConverter(((SimpleDateFormat) dateFormat).toLocalizedPattern(), true));
		publishDate.setRequired(true);
		form.add(publishDate);
		
		form.add(new Label("date_format", WicketApplication.DATE_FORMAT_ES));

		final RequiredTextField<Book> category = new RequiredTextField<Book>("category");
		form.add(category);
		
		final TextField<Book> subcategory = new TextField<Book>("subcategory");
		form.add(subcategory);
		
		final TextField<Book> hits = new TextField<Book>("hits");
		hits.setVisible(false);
		form.add(hits);
		
		//ResetRatingLink resetRatingLink = new ResetRatingLink("reset1", new Model<Rating>(book.getRating()), book);
		//resetRatingLink.setVisible(false);
		
		if ( user.getLevel() == Level.ADMIN ) {
			//resetRatingLink.setVisible(true);
			hits.setVisible(true);
		}
		
		//form.add(resetRatingLink);
		
		form.add(new AjaxSubmitLink("save") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				Logger logger = Logger.getLogger("booktube");
								
				book.setAuthor(author.getModelObject());
				
				bookService.updateBook(book);

				logger.info("Book edited.");
				logger.info("ID: " + book.getId());
				logger.info("Title: " + book.getTitle());
				logger.info("Author: " + book.getAuthor());

				successDialog.open(target);

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
	
//	/**
//	 * Link to reset the ratings.
//	 */
//	private final class ResetRatingLink extends Link<Rating> {
//		/** For serialization. */
//		private static final long serialVersionUID = 1L;
//		private final Book book;
//
//		/**
//		 * Constructor.
//		 * 
//		 * @param id
//		 *            component id
//		 * @param object
//		 *            the model to reset.
//		 */
//		public ResetRatingLink(String id, IModel<Rating> object, Book book) {
//			super(id, object);
//			this.book = book;
//		}
//
//		/**
//		 * @see Link#onClick()
//		 */
//		@Override
//		public void onClick() {
//			Rating rating = getModelObject();
//			rating.setNrOfVotes(0);
//			rating.setRating(0);
//			rating.setSumOfRatings(0);
//			rating.setBook(book);
//		}
//	}

}
