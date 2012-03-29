package com.booktube.pages;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
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
import com.booktube.model.User;
import com.booktube.pages.BooksPage.BookProvider;
import com.booktube.service.UserService;
import com.booktube.service.BookService.SearchType;


public class WritersPage extends BasePage {

	@SpringBean
    UserService userService;
	
	public static final int WRITERS_PER_PAGE = 5;
	
	public WritersPage() {

		//User user = new User("username", "firstname", "lastname");
		//userService.insertUser(user);
		//WicketApplication.instance().getUserService().insertUser(user);
		//List<User> users = WicketApplication.instance().getUserService()
		List<User> users = userService.getAllUsers(0, Integer.MAX_VALUE);
		final WebMarkupContainer parent = new WebMarkupContainer("writers");
		parent.setOutputMarkupId(true);
		add(parent);

		//parent.add(listWriters("writerList", users));
		DataView<User> dataView = writerList("writerList");

		parent.add(dataView);
		parent.add(new PagingNavigator("footerPaginator", dataView));

	}

	private DataView<User> writerList(String label) {

		IDataProvider<User> dataProvider = new WriterProvider();
		
		DataView<User> dataView = new DataView<User>("writerList", dataProvider,
				WRITERS_PER_PAGE) {
			
			protected void populateItem(Item<User> item) {
				final User user = (User) item.getModelObject();
				CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(user);
				item.setDefaultModel(model);
				final PageParameters parameters = new PageParameters();
				parameters.set("user", user.getId());
				item.add(new Label("id"));
				item.add(new Label("username"));
				item.add(new Label("firstname"));
				item.add(new Label("lastname"));
				item.add(new Link("editLink", item.getModel()) {
					public void onClick() {
						setResponsePage(new EditWriterPage(user.getId(),
								WritersPage.this));
					}

				});
				item.add(new Link<User>("deleteLink", item.getModel()) {
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
	
		return dataView;
	}
	
	class WriterProvider implements IDataProvider<User> {

		private List<User> users;

		public WriterProvider() {
		}

		public Iterator<User> iterator(int first, int count) {

			this.users = userService.getAllUsers(first, count);
			
			return this.users.iterator();
		}

		public int size() {
			return userService.getCount();
		}

		public IModel<User> model(User user) {
			return new CompoundPropertyModel<User>(user);
		}

		public void detach() {
			// TODO Auto-generated method stub

		}
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		String newTitle = "Booktube - Writers"; 
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}
