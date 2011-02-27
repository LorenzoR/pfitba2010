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

	public BooksPage(final PageParameters parameters) {

		final WebMarkupContainer parent = new WebMarkupContainer("books");
		parent.setOutputMarkupId(true);
		add(parent);

		String type = parameters.getString("type");
		List<Book> books = null;

		if (type == null) {
			books = bookService.getAllBooks();
		} else if (type.equals("tag")) {
			books = bookService.findBookByTag(parameters.getString("tag"));
		} else if (type.equals("author")) {
			books = bookService.findBookByAuthor(Integer.valueOf(parameters
					.getString("author")));
		} else if (type.equals("title")) {
			books = bookService.findBookByTitle(parameters.getString("title"));
		} else {
			books = bookService.getAllBooks();
		}

		DataView<Book> dataView = bookList("bookList", books);

		parent.add(dataView);
		parent.add(new PagingNavigator("footerPaginator", dataView));

	}

	private DataView<Book> bookList(String label, List<Book> books) {

		IDataProvider<Book> dataProvider = new BookProvider(books);

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
						// item.add(new Label("tagName", tag));
						BookmarkablePageLink<Object> bpl = new BookmarkablePageLink<Object>(
								"tagLink", BooksPage.class, parameters);
						bpl.add(new Label("tagName", tag));
						item.add(bpl);
						// item.add(new BookmarkablePageLink<Object>("tagLink",
						// BooksByTag.class, parameters));
					}
				});

				final PageParameters parameters = new PageParameters();
				parameters.put("book", book.getId().toString());
				item.add(new Label("title", book.getTitle()));
				parameters.put("author", book.getAuthor().getId().toString());
				parameters.put("type", "author");
				BookmarkablePageLink<Object> bpl = new BookmarkablePageLink<Object>(
						"authorLink", BooksPage.class, parameters);
				bpl.add(new Label("authorName", book.getAuthor().getUsername()));
				item.add(bpl);
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

		public BookProvider(List<Book> books) {
			this.books = books;
		}

		public Iterator<Book> iterator(int first, int count) {
			// return bookService.iterator(first, count);
			// return books.iterator(first, count);
			return books.iterator();
		}

		public int size() {
			// return bookService.getCount();
			if (size == null) {
				return books.size();
			} else {
				return size;
			}
		}

		public IModel<Book> model(Book book) {
			return new CompoundPropertyModel<Book>(book);
		}

		public void detach() {
			// TODO Auto-generated method stub

		}
	}

}
