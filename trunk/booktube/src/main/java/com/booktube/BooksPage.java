package com.booktube;

import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.model.Book;
import com.booktube.model.User;
import com.booktube.service.BookService;
import com.booktube.service.UserService;

public class BooksPage extends BasePage {

	@SpringBean
	BookService bookService;
	
	@SpringBean
	UserService userService;

	//private List<Book> books = WicketApplication.instance().getBookService()
	//		.getAllBooks();

	public BooksPage() {

		List<Book> books = bookService.getAllBooks();
		
		final WebMarkupContainer parent = new WebMarkupContainer("books");
		parent.setOutputMarkupId(true);
		add(parent);

		//parent.add(bookList("bookList", books));
		PageableListView plv = bookList2("bookList", books);
		parent.add(plv);
		parent.add(new PagingNavigator("navigator", plv));
		System.out.println(bookService.findBookByTitle("Little"));

	}

	private PageableListView bookList2(String label, List<Book> books) {
		PageableListView pageableListView = new PageableListView(label, books, 5) {
			protected void populateItem(ListItem item) {
				Book book = (Book) item.getModelObject();
				final PageParameters parameters = new PageParameters();
				parameters.put("book", book.getId().toString());
				item.add(new Label("title", book.getTitle()));
				item.add(new Label("author", book.getAuthor().getUsername()));
				item.add(new Label("publishDate", book.getPublishDate()
						.toString()));
				item.add(new BookmarkablePageLink("editLink",
						EditBookPage.class, parameters));
				item.add(new BookmarkablePageLink("detailsLink",
						ShowBookPage.class, parameters));
				item.add(new Link("deleteLink", item.getModel()) {
					public void onClick() {
						/*
						 * Translate wicket model into the underlying object so
						 * that listener code does not need to deal with the
						 * model
						 */
						// onEditEmployee((Employee) getModelObject());
						Book book = (Book) getModelObject();
						Integer bookId = book.getId();
						// new Model("alert('This is my JS script');");
						// System.out.println("BOOK ID: " + book.getId());
						// System.out.println(getModelObject());

						bookService.deleteBook(book);
						System.out.println("Book " + bookId + " deleted.");
						
						setResponsePage(ShowBookPage.class);

						// System.out.println("Index es " +
						// comments.lastIndexOf(getModelObject()));
						// comments.remove(comments.lastIndexOf(getModelObject()));
					}

				});
			}
		};
		
		return pageableListView;
	}
	
	private PropertyListView bookList(String label, List<Book> books) {

		
		
		PropertyListView bookPLV = new PropertyListView("bookList", books) {

			protected void populateItem(ListItem item) {
				Book book = (Book) item.getModelObject();
				final PageParameters parameters = new PageParameters();
				parameters.put("book", book.getId().toString());
				item.add(new Label("title", book.getTitle()));
				item.add(new Label("author", book.getAuthor().getUsername()));
				item.add(new Label("publishDate", book.getPublishDate()
						.toString()));
				item.add(new BookmarkablePageLink("editLink",
						EditBookPage.class, parameters));
				item.add(new BookmarkablePageLink("detailsLink",
						ShowBookPage.class, parameters));
				item.add(new Link("deleteLink", item.getModel()) {
					public void onClick() {
						/*
						 * Translate wicket model into the underlying object so
						 * that listener code does not need to deal with the
						 * model
						 */
						// onEditEmployee((Employee) getModelObject());
						Book book = (Book) getModelObject();
						Integer bookId = book.getId();
						// new Model("alert('This is my JS script');");
						// System.out.println("BOOK ID: " + book.getId());
						// System.out.println(getModelObject());

						bookService.deleteBook(book);
						System.out.println("Book " + bookId + " deleted.");
						
						setResponsePage(ShowBookPage.class);

						// System.out.println("Index es " +
						// comments.lastIndexOf(getModelObject()));
						// comments.remove(comments.lastIndexOf(getModelObject()));
					}

				});
			}
		};

		return bookPLV;
	}

	protected void onEditBook(Book book) {
		PageParameters parameters = new PageParameters();
		parameters.put("book", book.getId().toString());
		// setResponsePage(new EditBookPage(this, book));
		setResponsePage(new EditBookPage(this, parameters));
	}

	protected void onDetailsBook(Book book) {
		/*PageParameters parameters = new PageParameters();
		parameters.put("book", book.getId().toString());
		// setResponsePage(new EditBookPage(this, book));
		setResponsePage(new ShowBookPage(this, parameters));
	*/}

}
