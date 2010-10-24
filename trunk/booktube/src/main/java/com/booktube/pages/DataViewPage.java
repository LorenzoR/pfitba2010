package com.booktube.pages;

import java.util.Iterator;

import org.apache.wicket.Application;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.model.User;
import com.booktube.service.UserService;

public class DataViewPage extends WebPage {

	@SpringBean
	UserService userService;

	public DataViewPage() {
		IDataProvider dataProvider = new UsersProvider();

		DataView dataView = new DataView("rows", dataProvider, 3) {
			public void populateItem(final Item item) {
				final User user = (User) item.getModelObject();
				item.add(new Label("id", user.getId().toString()));
			}
		};

		add(dataView);

		add(new PagingNavigator("footerPaginator", dataView));

	}

	class UsersProvider implements IDataProvider {

		public Iterator<User> iterator(int first, int count) {
			return userService.iterator(first, count);
		}

		public int size() {
			return userService.getCount();
		}

		public IModel<User> model(Object object) {
			return new CompoundPropertyModel<User>(object);
		}

		public void detach() {
			// TODO Auto-generated method stub

		}
	}

}
