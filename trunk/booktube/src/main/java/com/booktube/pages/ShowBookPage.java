package com.booktube.pages;

import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.rating.RatingPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.facebook.plugins.Comments;
import org.wicketstuff.facebook.plugins.LikeButton;
import org.wicketstuff.facebook.plugins.LikeButton.LikeButtonAction;
import org.wicketstuff.facebook.plugins.LikeButton.LikeButtonLayoutStyle;
import org.wicketstuff.twitter.TweetButton;

import com.booktube.WiaSession;
import com.booktube.model.Book;
import com.booktube.model.Rating;
import com.booktube.model.User;
import com.booktube.model.UserVote;
import com.booktube.service.BookService;
import com.booktube.service.UserService;

public class ShowBookPage extends BasePage {

	@SpringBean
	BookService bookService;

	@SpringBean
	UserService userService;

	/** backwards nav page */
	// private final Page backPage;
	
	private User user = WiaSession.get().getLoggedInUser();
	private Book book;

	public ShowBookPage(PageParameters pageParameters) {

		if ( pageParameters.get("book").isEmpty() ) {
			System.out.println("ERRRORRR");
			System.exit(0);
		}
		
		final int currentPage;
		
		if ( pageParameters.get("currentPage").isEmpty() ) {
			currentPage = 0;
		}
		else {
			currentPage = pageParameters.get("currentPage").toInt();
		}
		
		final Long bookId = pageParameters.get("book").toLong();		
		
		book = bookService.getBook(bookId);
		book.increaseHits();
		bookService.updateBook(book);
		
		addBreadcrumb(new BookmarkablePageLink<Object>("link", BooksPage.class), new ResourceModel("showBookPageTitle").getObject());
		addBreadcrumb(new BookmarkablePageLink<Object>("link", ShowBookPage.class, pageParameters), book.getTitle());
		
		//user = WiaSession.get().getLoggedInUser();

		
		//book = bookModel.getObject();
		
		final WebMarkupContainer parent = new WebMarkupContainer("bookDetails");
		parent.setOutputMarkupId(true);
		add(parent);

		String newTitle = "Booktube - " + book.getTitle() + " " + new ResourceModel("by").getObject() + " " + book.getAuthor(); 
		super.get("pageTitle").setDefaultModelObject(newTitle);
		
		CompoundPropertyModel<Book> model = new CompoundPropertyModel<Book>(book);
		parent.setDefaultModel(model);
		// add(new Label("bookId", book.getId().toString()));
		parent.add(new Label("title"));
		parent.add(new Label("author.username"));
		parent.add(new MultiLineLabel("text"));
		parent.add(new Label("publishDate"));
		parent.add(new Label("hits"));

		final Rating rating1 = book.getRating();
		
		parent.add(new RatingPanel("rating1", new PropertyModel<Integer>(rating1,
				"rating"), new Model<Integer>(5), new PropertyModel<Integer>(rating1, "nrOfVotes"),
				new PropertyModel<Boolean>(this, "hasVoted"), true) {
					private static final long serialVersionUID = 1L;

			@Override
			protected boolean onIsStarActive(int star) {
				return rating1.isActive(star);
			}

			@Override
			protected void onRated(int rating, AjaxRequestTarget target) {
				rating1.addRating(rating);
				if ( user != null) {
					User newUser = userService.getUser(user.getId());
					System.out.println("NEW USER: " + newUser);
					//book.addUserVote(newUser);
					UserVote userVote = new UserVote(user, book);
//                	userVote.setBook(book);
//                	userVote.setUser(user);
					book.addUserVote(userVote);
				}
				bookService.updateBook(book);
			}
		});

//		parent.add(new ResetRatingLink("reset1", new Model<Rating>(rating1), book));

		PageParameters backPageParameters = new PageParameters();
		backPageParameters.set("currentPage", currentPage);
		parent.add(new BookmarkablePageLink<Object>("goBack", BooksPage.class, backPageParameters));
//		parent.add(new Link("goBack") {
//			public void onClick() {
//				setResponsePage(HomePage.class);
//			}
//		});

		//List<Comment> comments = new ArrayList<Comment>(book.getComments());

		//parent.add(commentList("comments", comments));

		//Form<Object> commentForm = commentForm(parent, book, comments);

		//parent.add(commentForm);

		/*Label registerMessage = new Label("registerMessage",
				"Debe registrarse para poder enviar comentarios.");
		parent.add(registerMessage);
*/
		/*if (user == null) {
			commentForm.setVisible(false);
		} else {
			registerMessage.setVisible(false);
		}*/
		
		//final String currentURL = RequestCycle.get().getUrlRenderer().renderFullUrl(
		//	    Url.parse(urlFor(ShowBookPage.class,null).toString()));
		
		final String url = RequestCycle.get().getUrlRenderer().renderFullUrl(
				   Url.parse(urlFor(ShowBookPage.class,pageParameters).toString()));
		
		//final Model<String> url = Model.of(currentURL);
		final Comments facebookComments = new Comments("facebookComments", url);
		parent.add(facebookComments);
		
		final IModel<String> twitterUrl = Model.of("https://github.com/tfreier/wicketstuff-core/tree/master/jdk-1.5-parent/twitter-parent");
		final IModel<String> via = Model.of("tfreier");
		final IModel<String> text = Model.of("just testing #twitter widgets for #wicket.");

		final TweetButton tweetButton = new TweetButton("tweetButton", twitterUrl, text, via);
	
		parent.add(tweetButton);
		
		final LikeButton likeButton = new LikeButton("likeButton", url);
		likeButton.setLayoutStyle(LikeButtonLayoutStyle.BUTTON_COUNT);
		likeButton.setAction(LikeButtonAction.LIKE);
		parent.add(likeButton);
		
		//Label googlePlusOne = new Label("plusone");
		//googlePlusOne.add(new AttributeModifier("href", true, new Model("http://localhost:8080")));
		//parent.add(googlePlusOne);

	}

	/**
	 * Star image for no selected star
	 */
	public static final ResourceReference WICKETSTAR0 = new PackageResourceReference(
			ShowBookPage.class, "WicketStar0.png");

	/**
	 * Star image for selected star
	 */
	public static final ResourceReference WICKETSTAR1 = new PackageResourceReference(
			ShowBookPage.class, "WicketStar1.png");

	/** For serialization. */
	private static final long serialVersionUID = 1L;

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

	/**
	 * Getter for the hasVoted flag.
	 * 
	 * @return <code>true</code> when the user has already voted.
	 */
	public Boolean getHasVoted() {
		return WiaSession.get().getLoggedInUser() == null || this.book.getUserVotes().contains(new UserVote(user, book));
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		
	}

}
