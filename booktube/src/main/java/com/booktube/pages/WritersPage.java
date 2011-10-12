package com.booktube.pages;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.model.Book;
import com.booktube.model.User;
import com.booktube.service.UserService;


public class WritersPage extends BasePage {

	@SpringBean
    UserService userService;
	
	public WritersPage() {

		//User user = new User("username", "firstname", "lastname");
		//userService.insertUser(user);
		//WicketApplication.instance().getUserService().insertUser(user);
		//List<User> users = WicketApplication.instance().getUserService()
		List<User> users = userService.getAllUsers(0, Integer.MAX_VALUE);
		final WebMarkupContainer parent = new WebMarkupContainer("books");
		parent.setOutputMarkupId(true);
		add(parent);

		parent.add(listWriters("writerList", users));

	}

	PropertyListView<Object> listWriters(String label, List<User> users) {

		PropertyListView<Object> writersPLV = new PropertyListView<Object>(label, users) {

			protected void populateItem(ListItem<Object> item) {
				User user = (User) item.getModelObject();
				CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(user);
				item.setDefaultModel(model);
				final PageParameters parameters = new PageParameters();
				parameters.put("user", user.getId());
				item.add(new Label("id"));
				item.add(new Label("username"));
				item.add(new Label("firstname"));
				item.add(new Label("lastname"));
				item.add(new Link<Object>("deleteLink", item.getModel()) {
					private static final long serialVersionUID = -7155146615720218460L;

					public void onClick() {

						User user = (User) getModelObject();
						Long userId = user.getId();

						userService.deleteUser(user);
						System.out.println("User " + userId + " deleted.");

						setResponsePage(WritersPage.this);
					}

				});
			}
		};
		
		return writersPLV;

	}
	
	/*class WriterProvider implements IDataProvider<User> {

		private static final long serialVersionUID = 6050730502992812477L;
		private List<User> writers;
		private Integer size;
		private String type;
		private String parameter;

		public UserProvider(String type, String parameter) {
			this.type = type;
			this.parameter = parameter;
			this.size = null;
		}

		public Iterator<User> iterator(int first, int count) {

			if (type == null) {
				this.writers = userService.getAllUsers(first, count);
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
	}*/


}
