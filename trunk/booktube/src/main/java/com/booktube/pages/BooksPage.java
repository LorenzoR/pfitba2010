package com.booktube.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import com.booktube.model.Book;
import com.booktube.service.BookService;
import com.booktube.service.BookService.SearchType;
import com.booktube.service.UserService;

public class BooksPage extends BasePage {

	@SpringBean
	BookService bookService;

	@SpringBean
	UserService userService;

	public static final int BOOKS_PER_PAGE = 5;

	public BooksPage(final PageParameters parameters) {

		final WebMarkupContainer parent = new WebMarkupContainer("books");
		parent.setOutputMarkupId(true);
		add(parent);

		String typeString = parameters.get("type").toString();
		String parameter = null;
		SearchType type = SearchType.ALL;

		if (typeString != null) {

			parameter = parameters.get(typeString).toString();

			if (typeString.equals("tag")) {
				type = SearchType.TAG;
			} else if (typeString.equals("title")) {
				type = SearchType.TITLE;
			} else if (typeString.equals("author")) {
				type = SearchType.AUTHOR;
			}

		}

		DataView<Book> dataView = bookList("bookList", type, parameter);

		StringValue currentPage = parameters.get("currentPage");
		
		if ( !currentPage.isEmpty() ) {
			dataView.setCurrentPage(currentPage.toInt());
		}
		
		parent.add(dataView);
		parent.add(new PagingNavigator("footerPaginator", dataView));

	}

	private DataView<Book> bookList(String label, SearchType type,
			String parameter) {

		IDataProvider<Book> dataProvider = new BookProvider(type, parameter);

		DataView<Book> dataView = new DataView<Book>("bookList", dataProvider,
				BOOKS_PER_PAGE) {

			private static final long serialVersionUID = -869452866439034394L;

			protected void populateItem(Item<Book> item) {
				final Book book = (Book) item.getModelObject();
				List<String> tagList = null;
				
				if ( book.getTags() != null ) {
					tagList = new ArrayList<String>(book.getTags());
				}
				

				item.add(new PropertyListView<Object>("tagList", tagList) {

					private static final long serialVersionUID = -7951435365391555660L;

					protected void populateItem(ListItem<Object> item) {
						String tag = (String) item.getModelObject();
						final PageParameters parameters = new PageParameters();
						parameters.set("tag", tag);
						parameters.set("type", "tag");

						BookmarkablePageLink<Object> bpl = new BookmarkablePageLink<Object>(
								"tagLink", BooksPage.class, parameters);
						bpl.add(new Label("tagName", tag));
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
				
				BookmarkablePageLink<Object> titleLink = new BookmarkablePageLink<Object>("viewLink", ShowBookPage.class, detailsParameter); 
				
//				Link<Object> titleLink = new Link("viewLink") {
//					public void onClick() {
//						setResponsePage(new ShowBookPage(book.getId(),
//								BooksPage.this));
//					}
//				};

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
				
				item.add(new BookmarkablePageLink<Object>("editLink", EditBookPage.class, detailsParameter));
				item.add(new BookmarkablePageLink<Object>("detailsLink", ShowBookPage.class, detailsParameter));
				
				item.add(new Link<Book>("deleteLink", item.getModel()) {
					private static final long serialVersionUID = -7155146615720218460L;

					public void onClick() {

						Book book = (Book) getModelObject();
						Long bookId = book.getId();

						Book bookDelete = bookService.getBook(bookId);
						
						System.out.println("BOOk es : " + bookDelete);
						
						//bookService.deleteBook(book);
						bookService.deleteBook(bookDelete);
						System.out.println("Book " + bookId + " deleted.");

						setResponsePage(BooksPage.this);
					}

				});
			}
		};

		return dataView;
	}

	class BookProvider implements IDataProvider<Book> {

		private static final long serialVersionUID = 6050730502992812477L;
		private List<Book> books;
		private SearchType type;
		private String parameter;

		public BookProvider(SearchType type, String parameter) {
			this.type = type;
			this.parameter = parameter;
		}

		public Iterator<Book> iterator(int first, int count) {

			switch (type) {
			case ALL:
				this.books = bookService.getAllBooks(first, count);
				break;
			case TAG:
				this.books = bookService.findBookByTag(parameter, first, count);
				break;
			case TITLE:
				this.books = bookService.findBookByTitle(parameter, first,
						count);
				break;
			case AUTHOR:
				this.books = bookService.findBookByAuthor(parameter, first,
						count);
				break;
			default:
				this.books = bookService.getAllBooks(first, count);
			}
			/*
			 * if (type == null) { this.books = bookService.getAllBooks(first,
			 * count); // books = bookService.getAllBooks(); } else if
			 * (type.equals("tag")) { this.books =
			 * bookService.findBookByTag(parameter, first, count); // books = //
			 * bookService.findBookByTag(parameters.getString("tag"), // 0,
			 * Integer.MAX_VALUE); } else if (type.equals("author")) {
			 * this.books = bookService.findBookByAuthor(parameter, first,
			 * count); // books = bookService.findBookByAuthor( //
			 * parameters.getString("author"), 0, Integer.MAX_VALUE); } else if
			 * (type.equals("title")) { this.books =
			 * bookService.findBookByTitle(parameter, first, count); // books =
			 * bookService.findBookByTitle( // parameters.getString("title"), 0,
			 * Integer.MAX_VALUE); } else { this.books =
			 * bookService.getAllBooks(first, count); // books =
			 * bookService.getAllBooks(); }
			 */
			return this.books.iterator();
			// return bookService.iterator(first, count);
			// return books.iterator();
			// return bookService.findBookByAuthor("eapoe", first,
			// count).iterator();
		}

		public int size() {
//			// return bookService.getCount();
//			if (size == null) {
//				/*
//				 * if (type == null) { size =
//				 * bookService.getCount(SearchType.ALL, parameter); } else if
//				 * (type.equals("tag")) { size =
//				 * bookService.getCount(SearchType.TAG, parameter); } else {
//				 * size = bookService.getCount(SearchType.ALL, parameter); }
//				 */
//				size = bookService.getCount(type, parameter);
//				// size = 20;
//			}
//			return size;
			return bookService.getCount(type, parameter);
		}

		public IModel<Book> model(Book book) {
			return new CompoundPropertyModel<Book>(book);
		}

		public void detach() {
			// TODO Auto-generated method stub

		}
	}

}
