package com.booktube.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import com.booktube.model.Book;
import com.booktube.model.BookTag;
import com.booktube.service.BookService;
import com.booktube.service.UserService;

public class BooksPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	BookService bookService;

	@SpringBean
	UserService userService;

	public static final int BOOKS_PER_PAGE = 5;

	private final PagingNavigator footerNavigator;
	
	private String author = null;
	private String title = null;
	private String category = null;
	private String subcategory = null;
	private String tag = null;
	private Long bookId = null;
	private Date lowPublishDate = null;
	private Date highPublishDate = null;
	
	final LoadableDetachableModel<List<Book>> resultsModel = new LoadableDetachableModel<List<Book>>() {

		private static final long serialVersionUID = 1L;

		protected List<Book> load() {
			return null;
		}
	};

	public BooksPage(final PageParameters parameters) {

		final WebMarkupContainer parent = new WebMarkupContainer("books");
		parent.setOutputMarkupId(true);
		add(parent);

		if (StringUtils.isNotBlank(parameters.get("author").toString())) {
			author = parameters.get("author").toString();
		}

		if (StringUtils.isNotBlank(parameters.get("rating").toString())) {
			//rating = parameters.get("rating").toString();
		}

		if (StringUtils.isNotBlank(parameters.get("tag").toString())) {
			tag = parameters.get("tag").toString();
		}

		if (StringUtils.isNotBlank(parameters.get("title").toString())) {
			title = parameters.get("title").toString();
		}
		
		if (StringUtils.isNotBlank(parameters.get("category").toString())) {
			category = parameters.get("category").toString();
		}
		
		if (StringUtils.isNotBlank(parameters.get("subcategory").toString())) {
			subcategory = parameters.get("subcategory").toString();
		}

		DataView<Book> dataView = bookList("bookList");

		StringValue currentPage = parameters.get("currentPage");

		if (!currentPage.isEmpty()) {
			dataView.setCurrentPage(currentPage.toInt());
		}

		parent.add(dataView);
		
		footerNavigator = new PagingNavigator("footerPaginator", dataView);
		parent.add(footerNavigator);
		
		Label feedbackMessage = new Label("feedbackMessage", "No se encontraron resultados.");
		parent.add(feedbackMessage);
		
		if ( dataView.getItemCount() <= 0 ) {
			footerNavigator.setVisible(false);
			feedbackMessage.setVisible(true);
		}
		else {
			footerNavigator.setVisible(true);
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
						parameters.set("tag", tag.getValue());
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
				item.add(new Label("publishDate", book.getPublishDate()
						.toString()));

				item.add(new BookmarkablePageLink<Object>("editLink",
						EditBookPage.class, detailsParameter));
				item.add(new BookmarkablePageLink<Object>("detailsLink",
						ShowBookPage.class, detailsParameter));

				item.add(new Link<Book>("deleteLink", item.getModel()) {

					private static final long serialVersionUID = 1L;

					public void onClick() {

						Book book = (Book) getModelObject();
						Long bookId = book.getId();

						// Book bookDelete = bookService.getBook(bookId);

						System.out.println("BOOk es : " + book);

						// bookService.deleteBook(book);
						bookService.deleteBook(book);
						System.out.println("Book " + bookId + " deleted.");

						setResponsePage(BooksPage.this);
					}

				});
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
							highPublishDate).iterator();
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
					subcategory, lowPublishDate, highPublishDate);
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
		String newTitle = "Booktube - Books";
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}