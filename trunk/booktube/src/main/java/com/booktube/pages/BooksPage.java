package com.booktube.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.PageParameters;
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

import com.booktube.model.Book;
import com.booktube.service.BookService;
import com.booktube.service.UserService;

public class BooksPage extends BasePage {

	@SpringBean
	BookService bookService;

	@SpringBean
	UserService userService;

	public static final int BOOKS_PER_PAGE = 5;

	public String type;

	public BooksPage(final PageParameters parameters) {

		final WebMarkupContainer parent = new WebMarkupContainer("books");
		parent.setOutputMarkupId(true);
		add(parent);

		String type = parameters.getString("type");
		String parameter = null;
		List<Book> books = null;

		if (type != null) {
			parameter = parameters.getString(type);
		}

		this.type = type;

		DataView<Book> dataView = bookList("bookList", type, parameter);

		parent.add(dataView);
		parent.add(new PagingNavigator("footerPaginator", dataView));

	}

	private DataView<Book> bookList(String label, String type, String parameter) {

		IDataProvider<Book> dataProvider = new BookProvider(type, parameter);

		DataView<Book> dataView = new DataView<Book>("bookList", dataProvider,
				BOOKS_PER_PAGE) {

			private static final long serialVersionUID = -869452866439034394L;

			protected void populateItem(Item<Book> item) {
				Book book = (Book) item.getModelObject();

				List<String> tagList = new ArrayList<String>(book.getTags());

				item.add(new PropertyListView<Object>("tagList", tagList) {

					private static final long serialVersionUID = -7951435365391555660L;

					protected void populateItem(ListItem<Object> item) {
						String tag = (String) item.getModelObject();
						final PageParameters parameters = new PageParameters();
						parameters.put("tag", tag);
						parameters.put("type", "tag");

						BookmarkablePageLink<Object> bpl = new BookmarkablePageLink<Object>(
								"tagLink", BooksPage.class, parameters);
						bpl.add(new Label("tagName", tag));
						item.add(bpl);

					}
				});

				final PageParameters parameters = new PageParameters();
				parameters.put("book", book.getId().toString());
				// item.add(new Label("title", book.getTitle()));
				BookmarkablePageLink<Object> bplTitle = new BookmarkablePageLink<Object>(
						"viewLink", ShowBookPage.class, parameters);
				bplTitle.add(new Label("title", book.getTitle()));
				item.add(bplTitle);
				parameters.put("author", book.getAuthor().getUsername());
				parameters.put("type", "author");
				BookmarkablePageLink<Object> bplAuthor = new BookmarkablePageLink<Object>(
						"authorLink", BooksPage.class, parameters);
				bplAuthor.add(new Label("authorName", book.getAuthor()
						.getUsername()));
				item.add(bplAuthor);
				item.add(new Label("publishDate", book.getPublishDate()
						.toString()));
				item.add(new BookmarkablePageLink<Object>("editLink",
						EditBookPage.class, parameters));
				item.add(new BookmarkablePageLink<Object>("detailsLink",
						ShowBookPage.class, parameters));
				item.add(new Link<Book>("deleteLink", item.getModel()) {
					private static final long serialVersionUID = -7155146615720218460L;

					public void onClick() {

						Book book = (Book) getModelObject();
						Integer bookId = book.getId();

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

		private static final long serialVersionUID = 6050730502992812477L;
		private List<Book> books;
		private Integer size;
		private String type;
		private String parameter;

		public BookProvider(String type, String parameter) {
			this.type = type;
			this.parameter = parameter;
			this.size = null;
		}

		public Iterator<Book> iterator(int first, int count) {

			if (type == null) {
				this.books = bookService.getAllBooks(first, count);
				// books = bookService.getAllBooks();
			} else if (type.equals("tag")) {
				this.books = bookService.findBookByTag(parameter, first, count);
				// books =
				// bookService.findBookByTag(parameters.getString("tag"),
				// 0, Integer.MAX_VALUE);
			} else if (type.equals("author")) {
				this.books = bookService.findBookByAuthor(parameter, first,
						count);
				// books = bookService.findBookByAuthor(
				// parameters.getString("author"), 0, Integer.MAX_VALUE);
			} else if (type.equals("title")) {
				this.books = bookService.findBookByTitle(parameter, first,
						count);
				// books = bookService.findBookByTitle(
				// parameters.getString("title"), 0, Integer.MAX_VALUE);
			} else {
				this.books = bookService.getAllBooks(first, count);
				// books = bookService.getAllBooks();
			}

			return this.books.iterator();
			// return bookService.iterator(first, count);
			// return books.iterator();
			// return bookService.findBookByAuthor("eapoe", first,
			// count).iterator();
		}

		public int size() {
			// return bookService.getCount();
			if (size == null) {
				size = bookService.getCount(type, parameter);
			}
			return size;
		}

		public IModel<Book> model(Book book) {
			return new CompoundPropertyModel<Book>(book);
		}

		public void detach() {
			// TODO Auto-generated method stub

		}
	}

}
