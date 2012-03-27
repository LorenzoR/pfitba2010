package com.booktube.pages;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Page;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;
import org.wicketstuff.facebook.FacebookSdk;
import org.wicketstuff.facebook.plugins.LikeButton;

import com.booktube.WiaSession;
import com.booktube.model.Book;
import com.booktube.model.User;
import com.booktube.service.BookService;
import com.booktube.service.MessageService;
import com.booktube.service.UserService;

public abstract class BasePage extends WebPage {

	@SpringBean
	UserService userService;

	@SpringBean
	BookService bookService;

	@SpringBean
	MessageService messageService;

	private final int TITLE_SELECTED = 1;
	private final int AUTHOR_SELECTED = 2;
	private final int RATING_SELECTED = 3;
	private final int TAG_SELECTED = 4;

	public BasePage() {

		// WebMarkupContainer group = new WebMarkupContainer("loginContainer");
		if (WiaSession.get().isAuthenticated()) {
			add(new Label("welcome", "Bienvenido "
					+ WiaSession.get().getLoggedInUser().getUsername() + " | "));
		} else {
			add(new Label("welcome"));
		}

		String[][] quoteArray = {
				{
						"The difficulty of literature is not to write, but to write what you mean",
						"Robert Louis Stevenson" },
				{ "Just don't take any class where you have to read BEOWULF.",
						"Woody Allen" },
				{ "Books are humanity in print.", "Barbara W. Tuchman" },

				{
						"Literature adds to reality, it does not simply describe it. It enriches the necessary competencies that daily life requires and provides; and in this respect, it irrigates the deserts that our lives have already become.",
						"C.S. Lewis" },

				{
						"Books are the carriers of civilization. Without books, history is silent, literature dumb, science crippled, thought and speculation at a standstill.",
						"Barbara W. Tuchman" },

				{
						"What is wonderful about great literature is that it transforms the man who reads it towards the condition of the man who wrote.",
						"E. M. Forster" },

				{ "Every man's memory is his private literature.",
						"Aldous Huxley" },

				{
						"The decline of literature indicates the decline of a nation.",
						"Johann Wolfgang von Goethe" },

				{
						"The difficulty of literature is not to write, but to write what you mean; not to affect your reader, but to affect him precisely as you wish.",
						"Robert Louis Stevenson" },

				{
						"The difference between literature and journalism is that journalism is unreadable and literature is not read.",
						"Oscar Wilde" },

				{
						"Literature adds to reality, it does not simply describe it. It enriches the necessary competencies that daily life requires and provides; and in this respect, it irrigates the deserts that our lives have already become.",
						"C. S. Lewis" },

				{
						"Even in literature and art, no man who bothers about originality will ever be original: whereas if you simply try to tell the truth (without caring twopence how often it has been told before) you will, nine times out of ten, become original without ever having noticed it.",
						"C. S. Lewis" },

				{
						"Literature is my Utopia. Here I am not disenfranchised. No barrier of the senses shuts me out from the sweet, gracious discourses of my book friends. They talk to me without embarrassment or awkwardness.",
						"Helen Keller" },

				{
						"All modern American literature comes from one book by Mark Twain called Huckleberry Finn.",
						"Ernest Hemingway" },

				{
						"We know too much, and are convinced of too little. Our literature is a substitute for religion, and so is our religion.",
						"T. S. Eliot" },

				{
						"I am an Anglo-Catholic in religion, a classicist in literature and a royalist in politics.",
						"T. S. Eliot" },

				{
						"Our high respect for a well read person is praise enough for literature.",
						"T. S. Eliot" },

				{
						"The reason that fiction is more interesting than any other form of literature, to those who really like to study people, is that in fiction the author can really tell the truth without humiliating himself.",
						"Jim Rohn" },

				{
						"The decline of literature indicates the decline of a nation.",
						"Johann Wolfgang von Goethe" },

				{
						"He knew everything about literature except how to enjoy it.",
						"Joseph Heller" },

				{
						"The atmosphere of orthodoxy is always damaging to prose, and above all it is completely ruinous to the novel, the most anarchical of all forms of literature.",
						"George Orwell" },

				{ "Every man's memory is his private literature.",
						"Aldous Huxley" },

				{
						"If literature isn't everything, it's not worth a single hour of someone's trouble.",
						"Jean-Paul Sartre" },

				{
						"It is the nature of the artist to mind excessively what is said about him. Literature is strewn with the wreckage of men who have minded beyond reason the opinions of others.",
						"Virginia Woolf" },

				{
						"This is not writing at all. Indeed, I could say that Shakespeare surpasses literature altogether, if I knew what I meant.",
						"Virginia Woolf" },

				{
						"Literature is strewn with the wreckage of men who have minded beyond reason the opinions of others.",
						"Virginia Woolf" },

				{
						"The greatest advances of civilization, whether in architecture or painting, in science and literature, in industry or agriculture, have never come from centralized government.",
						"Milton Friedman" },

				{
						"I hold that a writer who does not passionately believe in the perfectibility of man has no dedication nor any membership in literature.",
						"John Steinbeck" },

				{
						"Develop an interest in life as you see it; the people, things, literature, music - the world is so rich, simply throbbing with rich treasures, beautiful souls and interesting people. Forget yourself.",
						"Henry Miller" } };

		Random generator = new Random(System.currentTimeMillis());

		int randomNumber = generator.nextInt(quoteArray.length);

		Label quote = new Label("randomQuote", "\" "
				+ quoteArray[randomNumber][0] + " \" "
				+ quoteArray[randomNumber][1]);
		add(quote);

		// Label welcomeLabel = new Label("welcome");
		// add(welcomeLabel);
		// welcomeLabel.set
		// group.add(new Label("welcome", "Welcome " +
		// WiaSession.get().getLoggedInUser().getUsername()));
		// add(group);
		// group.setVisible(false);

		add(new FacebookSdk("fbRoot"));

		final IModel<String> url = Model.of("http://localhost:8080");
		add(new LikeButton("likeButton", url));

		add(new Label("footer",
				"Ayuda | Acerca de | Contacto | Términos y Condiciones"));

		add(new BookmarkablePageLink<String>("title", HomePage.class));
		add(new BookmarkablePageLink<String>("addBook", AddBookPage.class));
		add(new BookmarkablePageLink<String>("showBooks", BooksPage.class));
		/*
		 * add(new Link("showBooks") { public void onClick() {
		 * setResponsePage(BooksPage.class); } });
		 */
		add(new BookmarkablePageLink<String>("showWriters", WritersPage.class));
		add(new BookmarkablePageLink<String>("contact", NewContact.class));
		add(new BookmarkablePageLink<String>("loadDataLink", LoadDataPage.class));
		add(new BookmarkablePageLink<String>("messagesLink", MessagesPage.class));
		BookmarkablePageLink<String> registerLink = new BookmarkablePageLink<String>(
				"registerPage", RegisterPage.class);
		add(registerLink);

		if (WiaSession.get().isAuthenticated()) {
			add(new Label("unreadMessages", Integer.toString(messageService
					.countUnreadMessagesTo(WiaSession.get().getLoggedInUser()))));
		} else {
			add(new Label("unreadMessages", " "));
		}

		Form<?> loginForm = loginForm("login");

		Form<?> searchForm = searchBookForm("searchBook");

		Link<?> logoutLink = new Link<Object>("logoutLink") {
			private static final long serialVersionUID = -4042618076562731461L;

			public void onClick() {
				WiaSession.get().logInUser(null);
				setResponsePage(HomePage.class);
			}
		};

		add(loginForm);
		add(searchForm);
		add(logoutLink);

		// loginForm.setVisible(false);

		if (WiaSession.get().isAuthenticated()) {
			// System.out.println("Logueado con username " +
			// WiaSession.get().getUser().getUsername());
			loginForm.setVisible(false);
			registerLink.setVisible(false);

		} else {
			// System.out.println("NO esta logueado");
			// add(new Label("login", "Welcome " +
			// WiaSession.get().getUser().getUsername()));
			logoutLink.setVisible(false);
		}

	}

	private int selectedRadio;

	private Form<Object> searchBookForm(String label) {

		final Form<Object> form = new Form<Object>(label);

		selectedRadio = AUTHOR_SELECTED;

		final RadioGroup<String> radioGroup = new RadioGroup<String>("group",
				new Model<String>(""));
		radioGroup.setOutputMarkupId(true);

		radioGroup.add(new Radio<String>("author", new Model<String>("author"))
				.add(new AjaxEventBehavior("onclick") {
					protected void onEvent(AjaxRequestTarget target) {
						// add your favorite component.
						System.out.println("CLICK EN AUTHOR!!!");
						selectedRadio = AUTHOR_SELECTED;
						System.out.println("selectedRadio es " + selectedRadio);
					}
				}));

		radioGroup.add(new Radio<String>("rating", new Model<String>("rating"))
				.add(new AjaxEventBehavior("onclick") {
					protected void onEvent(AjaxRequestTarget target) {
						// add your favorite component.
						System.out.println("CLICK EN RATING!!!");
						selectedRadio = RATING_SELECTED;
						System.out.println("selectedRadio es " + selectedRadio);
					}
				}));

		radioGroup.add(new Radio<String>("title", new Model<String>("title"))
				.add(new AjaxEventBehavior("onclick") {
					protected void onEvent(AjaxRequestTarget target) {
						// add your favorite component.
						System.out.println("CLICK EN TITLE!!!");
						selectedRadio = TITLE_SELECTED;
						System.out.println("selectedRadio es " + selectedRadio);
					}
				}));

		radioGroup.add(new Radio<String>("tag", new Model<String>("tag"))
				.add(new AjaxEventBehavior("onclick") {
					protected void onEvent(AjaxRequestTarget target) {
						// add your favorite component.
						System.out.println("CLICK EN TAG!!!");
						selectedRadio = TAG_SELECTED;
						System.out.println("selectedRadio es " + selectedRadio);
					}
				}));

		// radioGroup.add(author.add(event));

		/*
		 * radioGroup.add(new Radio<String>("title", new
		 * Model<String>("title"))); radioGroup .add(new Radio<String>("rating",
		 * new Model<String>("rating"))); radioGroup.add(new
		 * Radio<String>("tag", new Model<String>("tag")));
		 */
		form.add(radioGroup);

		final AutoCompleteTextField<String> bookTitle = new AutoCompleteTextField<String>(
				"bookTitle", new Model<String>("")) {

			private static final long serialVersionUID = 2977239698122401133L;

			@Override
			protected Iterator<String> getChoices(String input) {
				if (Strings.isEmpty(input)) {
					List<String> emptyList = Collections.emptyList();
					return emptyList.iterator();
				}

				System.out.println("RadioGroupValue: "
						+ radioGroup.getModelObject());

				List<String> choices = new ArrayList<String>(10);

				/*
				 * if (radioGroup.getValue().equals("author")) { books = null; }
				 * else if (radioGroup.getValue().equals("rating")) { books =
				 * null; } else if (radioGroup.getValue().equals("tag")) { books
				 * = null; } else { books = bookService.getAllBooks(0,
				 * Integer.MAX_VALUE); }
				 */

				// List<User> usersTest = new ArrayList<User>();
				// usersTest.add(new User("test1", "test1", "test1", "test1"));

				if (selectedRadio == AUTHOR_SELECTED) {
					System.out.println("AUTHOR SELECTED!");
					System.out.println("selectedRadio es " + selectedRadio);
					List<User> users = userService.getAllUsers(0,
							Integer.MAX_VALUE);

					for (final User user : users) {
						final String title = user.getUsername();

						if (title.toUpperCase().startsWith(input.toUpperCase())) {
							choices.add(title);
							if (choices.size() == 10) {
								break;
							}
						}
					}
				} else if (selectedRadio == TAG_SELECTED) {
					List<String> tags = bookService.getAllTags();

					for (final String tag : tags) {

						if (tag.toUpperCase().startsWith(input.toUpperCase())) {
							choices.add(tag);
							if (choices.size() == 10) {
								break;
							}
						}
					}

				} else {
					System.out.println("OTRA COSA SELECTED!");
					System.out.println("selectedRadio es " + selectedRadio);
					List<Book> books = bookService.getAllBooks(0,
							Integer.MAX_VALUE);

					for (final Book book : books) {
						final String title = book.getTitle();

						if (title.toUpperCase().startsWith(input.toUpperCase())) {
							choices.add(title);
							if (choices.size() == 10) {
								break;
							}
						}
					}
				}

				return choices.iterator();
			}
		};

		form.add(bookTitle);

		form.add(new Button("find", new Model<String>("")) {

			private static final long serialVersionUID = 7667432284192053894L;

			@Override
			public void onSubmit() {
				String bookTitleString = bookTitle
						.getDefaultModelObjectAsString();

				bookTitle.setModel(new Model<String>(""));

				System.out.println("Book Title: " + bookTitleString);

				System.out.println("Radio Button: " + radioGroup.getValue());

				if (!continueToOriginalDestination()) {
					final PageParameters parameters = new PageParameters();

					if (radioGroup.getValue().equals("author")) {
						parameters.set("type", "author");
						parameters.set("author", bookTitleString);
					} else if (radioGroup.getValue().equals("rating")) {
						parameters.set("type", "rating");
						parameters.set("rating", bookTitleString);
					} else if (radioGroup.getValue().equals("tag")) {
						parameters.set("type", "tag");
						parameters.set("tag", bookTitleString);
					} else {
						System.out.println("Radio Button: "
								+ radioGroup.getValue());
						parameters.set("type", "title");
						parameters.set("title", bookTitleString);
					}

					setResponsePage(BooksPage.class, parameters);
				}

			}
		});

		return form;
	}

	private Form<Object> loginForm(String label) {
		final Form<Object> form = new Form<Object>(label);
		final TextField<String> username = new TextField<String>("username",
				new Model<String>(""));
		final PasswordTextField password = new PasswordTextField("password",
				new Model<String>(""));

		/*
		 * final Label loginMsg = new Label("loginMsg",
		 * "Nombre de usuario o password incorrecto");
		 * loginMsg.setVisible(false); form.add(loginMsg);
		 */

		// Add a FeedbackPanel for displaying our messages
		final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		form.add(feedbackPanel);

		form.add(username);
		form.add(password);

		form.add(new Button("button1", new Model<String>("")) {
			private static final long serialVersionUID = 6743737357599494567L;

			@Override
			public void onSubmit() {
				String userString = username.getDefaultModelObjectAsString();
				String passwordString = User.hash(
						password.getDefaultModelObjectAsString(), "SHA-1");

				username.setModel(new Model<String>(""));

				System.out.println("User: " + userString + " Pass: "
						+ passwordString);

				User user = userService.getUser(userString);

				if (user != null && user.getPassword().equals(passwordString)) {
					WiaSession.get().logInUser(user);
				} else {
					/* TERMINAR MENSAJE DE LOGIN INCORRECTO */
					System.out.println("Login failed!");
					info("aaaaaaaaaaaa");
					// loginMsg.setVisible(true);
				}

				if (!continueToOriginalDestination()) {
					setResponsePage(HomePage.class);
				}

				// setResponsePage(BasePage.this);

			}
		});

		return form;
	}

	// must declare hash map because meta data must be serializable
	private MetaDataKey<HashMap<String, PageIdVersion>> lastPageIdVersionKey = new MetaDataKey<HashMap<String, PageIdVersion>>() {
	};

	@Override
	protected void onBeforeRender() {
		// storeCurrentPage();
		super.onBeforeRender();
	}

	/*
	 * private void storeCurrentPage() { PageIdVersion pageIdVersion = new
	 * PageIdVersion(); pageIdVersion.id = getNumericId(); pageIdVersion.version
	 * = getVersions() - 1;
	 * 
	 * IPageMap pageMap = getPageMap(); HashMap<String, PageIdVersion>
	 * lastPageMap = getSession().getMetaData( lastPageIdVersionKey); if
	 * (lastPageMap == null) { lastPageMap = new HashMap<String,
	 * PageIdVersion>(); getSession().setMetaData(lastPageIdVersionKey,
	 * lastPageMap); } PageIdVersion current =
	 * lastPageMap.get(pageMap.getName());
	 * 
	 * if (current != null) { if (current.equals(pageIdVersion)) { // refresh of
	 * current page return; } pageIdVersion.last = current; current.last = null;
	 * } lastPageMap.put(pageMap.getName(), pageIdVersion); }
	 */
	/*
	 * public void goToLastPage() { HashMap<String, PageIdVersion> lastPageMap =
	 * getSession().getMetaData( lastPageIdVersionKey); PageIdVersion
	 * pageIdVersion = lastPageMap.get(getPageMap().getName()).last; Page page =
	 * getPageMap().get(pageIdVersion.id, pageIdVersion.version);
	 * setResponsePage(page); }
	 */
	class PageIdVersion {
		public int id;
		public int version;
		public PageIdVersion last;
	}

}