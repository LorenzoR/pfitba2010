package com.booktube.pages;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.model.User;
import com.booktube.service.UserService;

public class WritersPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	UserService userService;

	public static final int WRITERS_PER_PAGE = 5;
	
	private static String searchUsername = null;

	public WritersPage(final PageParameters pageParameters) {

		final WebMarkupContainer parent = new WebMarkupContainer("writers");
		parent.setOutputMarkupId(true);
		add(parent);
		
		final AtomicBoolean writerByLetter = new AtomicBoolean(false);

//		//final DynamicLabel breadcrumbs = new DynamicLabel("breadcrumbs");
//		//breadcrumbs.setLabel("Escritores >");
//		final List<BookmarkablePageLink<Object>> links = new ArrayList<BookmarkablePageLink<Object>>();
//		links.add(new BookmarkablePageLink<Object>("link", HomePage.class));
//		links.add(new BookmarkablePageLink<Object>("link", this.getClass()));
//		final List<String> labels = new ArrayList<String>();
//		labels.add("Inicio");
//		labels.add("Escritores");
//		final ListView<?> breadscrumbsLV = setBreadcrumbs(links, labels);
//		add(breadscrumbsLV);
//		//parent.add(breadcrumbs);
		addBreadcrumb(new BookmarkablePageLink<Object>("link", WritersPage.class), new ResourceModel("writersPageTitle").getObject());
		
		if ( StringUtils.isNotBlank(pageParameters.get("letter").toString()) ) {
			searchUsername = pageParameters.get("letter").toString() + "%";
			if ( writerByLetter.get() ) {
				removeLastBreadcrumb();
			}
			
			addBreadcrumb(new BookmarkablePageLink<Object>("link", WritersPage.class, pageParameters.set("letter", pageParameters.get("letter").toString())), pageParameters.get("letter").toString());
			
			writerByLetter.set(true);
		}
		else {
			searchUsername = null;
		}
		
		List<String> list = Arrays.asList(new String[] { "A", "B", "C", "D",
				"E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
				"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" });
		ListView<String> lettersListView = new ListView<String>("letters", list) {

			private static final long serialVersionUID = 1L;

			protected void populateItem(final ListItem<String> item) {
				// item.add(new Label("label", item.getModel()));
//				BookmarkablePageLink link = new BookmarkablePageLink("link",
//						HomePage.class);
				
				AjaxLink<?> link = new AjaxLink<Void>("link") {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						searchUsername = item.getModelObject() +"%";
						System.out.println("{{{{{{{ SearchUserName: " + searchUsername);
						
						if ( writerByLetter.get() ) {
							removeLastBreadcrumb();
						}
						
						addBreadcrumb(new BookmarkablePageLink<Object>("link", WritersPage.class, pageParameters.set("letter", item.getModelObject())), item.getModelObject());
						//setBreadcrumbs("Escritores > " + item.getModelObject());
						//breadcrumbs.setLabel("Escritores > " + item.getModelObject());
						//target.add(getBreadcrumbsLabel());
//						labels.add(item.getModelObject());
//						links.add(new BookmarkablePageLink<Object>("link", WritersPage.this.getClass()));
						target.add(parent);
						target.add(breadcrumbContainer);
						
						writerByLetter.set(true);
//						target.add(breadscrumbsLV.get(1));
					}
				};
				
				Label label = new Label("label", item.getModel());
				link.add(label);
				item.add(link);
			}
		};
		parent.add(lettersListView);
		// parent.add(listWriters("writerList", users));
		DataView<User> dataView = writerList("writerList");

		parent.add(dataView);
		
		AjaxPagingNavigator footerNavigator = new AjaxPagingNavigator("footerPaginator", dataView) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.appendJavaScript("scrollTo(0, 0)");
				target.add(parent);			
			}
		};
		parent.add(footerNavigator);
		
		final Label feedbackMessage = new Label("feedbackMessage", new ResourceModel("noResults"));
		parent.add(feedbackMessage);
		
		if ( dataView.getItemCount() > 0 ) {
			footerNavigator.setVisible(true);
			lettersListView.setVisible(true);
			feedbackMessage.setVisible(false);
		}
		else {
			footerNavigator.setVisible(false);
			lettersListView.setVisible(false);
			feedbackMessage.setVisible(true);
		}

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
				
				System.out.println("IMAGE: " + user.getImageURL());
				
				Image avatar = new Image("avatar", new Model<String>());
				
				if ( user.getImageURL() == null ) {
					avatar.add(new AttributeModifier("src", new Model<String>("img/defaultAvatar.png")));
				}
				else {
					avatar.add(new AttributeModifier("src", new Model<String>("img/avatar/" + user.getImageURL())));
				}
				
				avatar.add(new AttributeModifier("width", new Model<String>("116px")));
				avatar.add(new AttributeModifier("height", new Model<String>("116px")));

				
				item.add(avatar);
				//item.add(new Image("avatar", new Model<String>("img/defaultAvatar.png")));
				
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
					null, null, null, null, null, null, null).iterator();
		}

		public int size() {
			return userService.getCount(null, searchUsername, null, null, null,
					null, null, null, null);
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
		String newTitle = "Booktube - " + new ResourceModel("writersPageTitle").getObject();
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}
