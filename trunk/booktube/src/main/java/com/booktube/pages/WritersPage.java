package com.booktube.pages;

import java.util.Iterator;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.model.User;
import com.booktube.service.UserService;


public class WritersPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
    UserService userService;
	
	public static final int WRITERS_PER_PAGE = 5;
	
	public WritersPage() {

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
			
					private static final long serialVersionUID = 1L;

			protected void populateItem(Item<User> item) {
				final User user = (User) item.getModelObject();
				CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(user);
				item.setDefaultModel(model);
				final PageParameters parameters = new PageParameters();
				parameters.set("userId", user.getId());
				item.add(new Label("id"));
				item.add(new Label("username"));
				item.add(new Label("firstname"));
				item.add(new Label("lastname"));
				item.add(new BookmarkablePageLink<Object>("detailsLink", ShowUserPage.class, parameters));
				/*item.add(new Link("detailsLink", item.getModel()) {
					public void onClick() {
						setResponsePage(ShowUserPage.class, parameters);
					}

				});*/
				item.add(new Link<User>("editLink", item.getModel()) {
					private static final long serialVersionUID = 1L;

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

		private static final long serialVersionUID = 1L;

		public WriterProvider() {
		}

		public Iterator<User> iterator(int first, int count) {
			
			return userService.getAllUsers(first, count).iterator();
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
