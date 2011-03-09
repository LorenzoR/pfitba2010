package com.booktube.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.rating.RatingPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.value.ValueMap;

import com.booktube.WiaSession;
import com.booktube.model.Book;
import com.booktube.model.Comment;
import com.booktube.model.Rating;
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
	
	private User user;

	public ShowBookPage(final PageParameters parameters) {

		user = WiaSession.get().getLoggedInUser();
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

		final Rating rating1 = book.getRating();
		
		parent.add(new RatingPanel("rating1", new PropertyModel<Integer>(rating1,
				"rating"), new Model<Integer>(5), new PropertyModel<Integer>(rating1, "nrOfVotes"),
				new PropertyModel<Boolean>(this, "hasVoted"), true) {
			@Override
			protected boolean onIsStarActive(int star) {
				return rating1.isActive(star);
			}

			@Override
			protected void onRated(int rating, AjaxRequestTarget target) {
				rating1.addRating(rating);
				bookService.updateBook(book);
			}
		});

		parent.add(new ResetRatingLink("reset1", new Model<Rating>(rating1)));

		parent.add(new Link("goBack") {
			public void onClick() {
				setResponsePage(HomePage.class);
			}
		});

		List<Comment> comments = new ArrayList<Comment>(book.getComments());

		parent.add(commentList("comments", comments));

		Form<Object> commentForm = commentForm(parent, book, comments);

		parent.add(commentForm);

		Label registerMessage = new Label("registerMessage",
				"Debe registrarse para poder enviar comentarios.");
		parent.add(registerMessage);

		if (user == null) {
			commentForm.setVisible(false);
		} else {
			registerMessage.setVisible(false);
		}

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

	private Form<Object> commentForm(final WebMarkupContainer parent,
			final Book book, final List<Comment> comments) {

		Form<Object> form = new Form<Object>("form");

		final TextArea<Object> editor = new TextArea<Object>("textArea");
		editor.setOutputMarkupId(true);

		ValueMap myParameters = new ValueMap();
		myParameters.put("usernameList", users.get(0));
		form.setModel(new CompoundPropertyModel(myParameters));

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
				// String username = user.getUsername();

				// User user =
				// WicketApplication.instance().getUserService().getUser(username);
				// Book book = new Book(title, text, user);

				// User user = userService.getUser(username);
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
				System.out.println("Author: " + user.getUsername());
				System.out.println("Comment: " + text);

				comments.add(comment);

				/* Clear values */
				editor.setModel(new Model(""));

				target.addComponent(parent);

			}
		});

		return form;

	}

	/**
	 * Star image for no selected star
	 */
	public static final ResourceReference WICKETSTAR0 = new ResourceReference(
			ShowBookPage.class, "WicketStar0.png");

	/**
	 * Star image for selected star
	 */
	public static final ResourceReference WICKETSTAR1 = new ResourceReference(
			ShowBookPage.class, "WicketStar1.png");

	/** For serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Link to reset the ratings.
	 */
	private final class ResetRatingLink extends Link<Rating> {
		/** For serialization. */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor.
		 * 
		 * @param id
		 *            component id
		 * @param object
		 *            the model to reset.
		 */
		public ResetRatingLink(String id, IModel<Rating> object) {
			super(id, object);
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
		}
	}
	
	/**
	 * keeps track whether the user has already voted on this page, comes
	 * typically from the database, or is stored in a cookie on the client side.
	 */
	private Boolean hasVoted = Boolean.FALSE;

	/**
	 * Getter for the hasVoted flag.
	 * 
	 * @return <code>true</code> when the user has already voted.
	 */
	public Boolean getHasVoted() {
		return hasVoted;
	}

}
