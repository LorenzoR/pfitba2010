package com.booktube.pages;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import com.booktube.WiaSession;
import com.booktube.model.Book;
import com.booktube.model.BookTag;
import com.booktube.model.User;
import com.booktube.model.User.Level;
import com.booktube.service.BookService;
import com.booktube.service.UserService;

public class BooksPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	BookService bookService;

	@SpringBean
	UserService userService;

	public static final int BOOKS_PER_PAGE = 5;

	private final AjaxPagingNavigator footerNavigator;
	
	private User user;
	
	private String author = null;
	private String title = null;
	private String category = null;
	private String subcategory = null;
	private String tag = null;
	private Long bookId = null;
	private Date lowPublishDate = null;
	private Date highPublishDate = null;
	private Double lowRating = null;
	private Double highRating = null;
	
	final LoadableDetachableModel<List<Book>> resultsModel = new LoadableDetachableModel<List<Book>>() {

		private static final long serialVersionUID = 1L;

		protected List<Book> load() {
			return null;
		}
	};

	public BooksPage(final PageParameters parameters) {

		addBreadcrumb(new BookmarkablePageLink<Object>("link", BooksPage.class), new ResourceModel("booksPageTitle").getObject());
		
		user = WiaSession.get().getLoggedInUser();
		
		if (StringUtils.isNotBlank(parameters.get("author").toString())) {
			author = parameters.get("author").toString();
			parameters.set("author", author);
			addBreadcrumb(new BookmarkablePageLink<Object>("link", BooksPage.class, parameters), "Autor " + author);
		}

		if (StringUtils.isNotBlank(parameters.get("tag").toString())) {
			tag = parameters.get("tag").toString();
			parameters.set("tag", tag);
			addBreadcrumb(new BookmarkablePageLink<Object>("link", BooksPage.class, parameters), "Tag " + tag);
		}

		if (StringUtils.isNotBlank(parameters.get("title").toString())) {
			title = parameters.get("title").toString();
		}
		
		if (StringUtils.isNotBlank(parameters.get("category").toString())) {
			category = parameters.get("category").toString();
			parameters.set("category", category);
			addBreadcrumb(new BookmarkablePageLink<Object>("link", BooksPage.class, parameters), "Categoria " + category);
		}
		
		if (StringUtils.isNotBlank(parameters.get("subcategory").toString())) {
			subcategory = parameters.get("subcategory").toString();
			parameters.set("subcategory", subcategory);
			addBreadcrumb(new BookmarkablePageLink<Object>("link", BooksPage.class, parameters), "Subcategoria " + subcategory);
		}
		
		final WebMarkupContainer parent = new WebMarkupContainer("books");
		parent.setOutputMarkupId(true);
		add(parent);

		final WebMarkupContainer categoryMenu = new WebMarkupContainer("categoryButton");
		add(categoryMenu);
		
		final String newUrl = RequestCycle.get().getUrlRenderer().renderFullUrl(
				   Url.parse(urlFor(CategoryMenu.class, null).toString()));
		Label myScript = new Label("myScript", "url = '"
				+ newUrl + "';");
		myScript.setEscapeModelStrings(false);
		add(myScript);

		DataView<Book> dataView = bookList("bookList");

		StringValue currentPage = parameters.get("currentPage");

		if (!currentPage.isEmpty()) {
			dataView.setCurrentPage(currentPage.toInt());
		}

		parent.add(dataView);
		
		footerNavigator = new AjaxPagingNavigator("footerPaginator", dataView) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.appendJavaScript("scrollTo(0, 0)");
				target.add(parent);
			}
		};
		parent.add(footerNavigator);
		
		final Label feedbackMessage = new Label("feedbackMessage", new ResourceModel("noResults"));
//		Label feedbackMessage = new Label("feedbackMessage", "No se encontraron resultados.");
		parent.add(feedbackMessage);
		
		if ( dataView.getItemCount() <= 0 ) {
			footerNavigator.setVisible(false);
			categoryMenu.setVisible(true);
			feedbackMessage.setVisible(true);
		}
		else {
			footerNavigator.setVisible(true);
			categoryMenu.setVisible(true);
			feedbackMessage.setVisible(false);
		}
		
	}

	private DataView<Book> bookList(String label) {

		IDataProvider<Book> dataProvider = new BookProvider();

		DataView<Book> dataView = new DataView<Book>("bookList", dataProvider,
				BOOKS_PER_PAGE) {

			private static final long serialVersionUID = -869452866439034394L;

			protected void populateItem(Item<Book> item) {
				final Book book = (Book) item.getModelObject();
				List<BookTag> tagList = null;

				if (book.getTags() != null) {
					tagList = new ArrayList<BookTag>(book.getTags());
				}

				item.add(new PropertyListView<Object>("tagList", tagList) {

					private static final long serialVersionUID = 1L;

					protected void populateItem(ListItem<Object> item) {
						BookTag tag = (BookTag) item.getModelObject();
						final PageParameters parameters = new PageParameters();
						parameters.set("tag", tag);
						parameters.set("type", "tag");

						BookmarkablePageLink<Object> bpl = new BookmarkablePageLink<Object>(
								"tagLink", BooksPage.class, parameters);
						bpl.add(new Label("tagName", tag.getValue()));
						item.add(bpl);

					}
				});

				final PageParameters parameters = new PageParameters();
				parameters.set("book", book.getId().toString());
				// item.add(new Label("title", book.getTitle()));
				// BookmarkablePageLink<Object> bplTitle = new
				// BookmarkablePageLink<Object>(
				// "viewLink", ShowBookPage.class, parameters);

				PageParameters detailsParameter = new PageParameters();
				detailsParameter.set("book", book.getId());
				detailsParameter.set("currentPage", this.getCurrentPage());

				BookmarkablePageLink<Object> titleLink = new BookmarkablePageLink<Object>(
						"viewLink", ShowBookPage.class, detailsParameter);

				// Link<Object> titleLink = new Link("viewLink") {
				// public void onClick() {
				// setResponsePage(new ShowBookPage(book.getId(),
				// BooksPage.this));
				// }
				// };

				titleLink.add(new Label("title", book.getTitle()));

				// bplTitle.add(new Label("title", book.getTitle()));
				// item.add(bplTitle);
				item.add(titleLink);
				parameters.set("author", book.getAuthor().getUsername());
				parameters.set("type", "author");
				BookmarkablePageLink<Object> bplAuthor = new BookmarkablePageLink<Object>(
						"authorLink", BooksPage.class, parameters);
				bplAuthor.add(new Label("authorName", book.getAuthor()
						.getUsername()));
				item.add(bplAuthor);
				
				final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, getLocale());
				
				item.add(new Label("publishDate", dateFormat.format(book.getPublishDate())));

				WebMarkupContainer editLinkContainer = new WebMarkupContainer("editLinkContainer");
				BookmarkablePageLink<Object> editLink = new BookmarkablePageLink<Object>("editLink",
						EditBookPage.class, detailsParameter);
				editLinkContainer.add(editLink);
				editLinkContainer.setVisible(false);
				
				item.add(editLinkContainer);
				item.add(new BookmarkablePageLink<Object>("detailsLink",
						ShowBookPage.class, detailsParameter));

				
				WebMarkupContainer deleteLinkContainer = new WebMarkupContainer("deleteLinkContainer");
				
				Link<Book> deleteLink = new Link<Book>("deleteLink", item.getModel()) {

					private static final long serialVersionUID = 1L;

					public void onClick() {

						Book book = (Book) getModelObject();

						bookService.deleteBook(book);

						setResponsePage(BooksPage.this);
					}

				};
				deleteLinkContainer.add(deleteLink);
				deleteLinkContainer.setVisible(false);
				
				item.add(deleteLinkContainer);
				
				if ( user != null && ( user.getLevel() == Level.ADMIN || user.getUsername().equals(book.getAuthor().getUsername()) ) ) {
					deleteLinkContainer.setVisible(true);
					editLinkContainer.setVisible(true);
				}
				
			}
		};

		return dataView;
	}

	class BookProvider implements IDataProvider<Book> {

		private static final long serialVersionUID = 1L;

		public BookProvider() {
		}

		public Iterator<Book> iterator(int first, int count) {
			return bookService
					.getBooks(first, count, bookId, author, title, tag,
							category, subcategory, lowPublishDate,
							highPublishDate, lowRating, highRating).iterator();
		}

		public int size() {
			// // return bookService.getCount();
			// if (size == null) {
			// /*
			// * if (type == null) { size =
			// * bookService.getCount(SearchType.ALL, parameter); } else if
			// * (type.equals("tag")) { size =
			// * bookService.getCount(SearchType.TAG, parameter); } else {
			// * size = bookService.getCount(SearchType.ALL, parameter); }
			// */
			// size = bookService.getCount(type, parameter);
			// // size = 20;
			// }
			// return size;
			// return bookService.getCount(type, parameter);
			return bookService.getCount(bookId, author, title, tag, category,
					subcategory, lowPublishDate, highPublishDate, lowRating, highRating);
		}

		public IModel<Book> model(Book book) {
			return new CompoundPropertyModel<Book>(book);
		}

		public void detach() {
			// TODO Auto-generated method stub

		}
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		String newTitle = "Booktube - " + new ResourceModel("booksPageTitle").getObject();
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}
