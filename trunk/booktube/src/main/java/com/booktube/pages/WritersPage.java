package com.booktube.pages;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.model.User;
import com.booktube.pages.customComponents.DynamicLabel;
import com.booktube.service.UserService;

public class WritersPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	UserService userService;

	public static final int WRITERS_PER_PAGE = 5;
	
	private static String searchUsername = null;

	public WritersPage() {

		final WebMarkupContainer parent = new WebMarkupContainer("writers");
		parent.setOutputMarkupId(true);
		add(parent);

		//final DynamicLabel breadcrumbs = new DynamicLabel("breadcrumbs");
		//breadcrumbs.setLabel("Escritores >");
		setBreadcrumbs("Escritores >");
		//parent.add(breadcrumbs);
		
		searchUsername = null;
		
		List<String> list = Arrays.asList(new String[] { "A", "B", "C", "D",
				"E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
				"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" });
		ListView<String> listview = new ListView<String>("listview", list) {

			private static final long serialVersionUID = 1L;

			protected void populateItem(final ListItem<String> item) {
				// item.add(new Label("label", item.getModel()));
//				BookmarkablePageLink link = new BookmarkablePageLink("link",
//						HomePage.class);
				
				AjaxLink<?> link = new AjaxLink<Void>("link") {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						searchUsername = item.getModelObject() +"%s";
						System.out.println("{{{{{{{ SearchUserName: " + searchUsername);
						setBreadcrumbs("Escritores > " + item.getModelObject());
						//breadcrumbs.setLabel("Escritores > " + item.getModelObject());
						target.add(getBreadcrumbsLabel());
						target.add(parent);
					}
				};
				
				Label label = new Label("label", item.getModel());
				link.add(label);
				item.add(link);
			}
		};
		parent.add(listview);
		// parent.add(listWriters("writerList", users));
		DataView<User> dataView = writerList("writerList");

		parent.add(dataView);
		parent.add(new PagingNavigator("footerPaginator", dataView));

	}

	private DataView<User> writerList(String label) {

		IDataProvider<User> dataProvider = new WriterProvider();

		DataView<User> dataView = new DataView<User>("writerList",
				dataProvider, WRITERS_PER_PAGE) {

			private static final long serialVersionUID = 1L;

			protected void populateItem(Item<User> item) {
				final User user = (User) item.getModelObject();
				final CompoundPropertyModel<User> model = new CompoundPropertyModel<User>(
						user);
				item.setDefaultModel(model);
				final PageParameters parameters = new PageParameters();
				parameters.set("userId", user.getId());
				item.add(new Label("id"));
				item.add(new Label("username"));
				item.add(new Label("firstname"));
				item.add(new Label("lastname"));
				
				PageParameters worksParameter = new PageParameters();
				worksParameter.set("author", user.getUsername());
				worksParameter.set("type", "author");
				
				item.add(new BookmarkablePageLink<Object>(
						"worksLink", BooksPage.class, worksParameter));
				
				item.add(new BookmarkablePageLink<Object>("detailsLink",
						ShowUserPage.class, parameters));
				/*
				 * item.add(new Link("detailsLink", item.getModel()) { public
				 * void onClick() { setResponsePage(ShowUserPage.class,
				 * parameters); }
				 * 
				 * });
				 */
				item.add(new Link<User>("editLink", item.getModel()) {
					private static final long serialVersionUID = 1L;

					public void onClick() {
						// setResponsePage(new EditWriterPage(user.getId(),
						// WritersPage.this));
						setResponsePage(new EditWriterPage(model,
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
			// this.users = userService.getAllUsers(first, count);
			// return this.users.iterator();
			return userService.getUsers(first, count, null, searchUsername,
					null, null, null, null, null, null).iterator();
		}

		public int size() {
			return userService.getCount(null, searchUsername, null, null, null,
					null, null, null);
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
