package com.booktube.pages;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.datepicker.DatePicker;

import com.booktube.WiaSession;
import com.booktube.model.Book;
import com.booktube.model.Message;
import com.booktube.service.BookService;
import com.booktube.service.CampaignService;
import com.booktube.service.MessageService;
import com.booktube.service.UserService;

public class SearchPage extends BasePage {

	@SpringBean
	MessageService messageService;

	@SpringBean
	UserService userService;

	@SpringBean
	BookService bookService;

	@SpringBean
	CampaignService campaignService;

	public SearchPage() {

		final WebMarkupContainer parent = new WebMarkupContainer("searchPage");
		parent.setOutputMarkupId(true);
		add(parent);

		parent.add(searchUserForm());
		parent.add(searchBookForm(parent));
		parent.add(searchMessageForm());
		parent.add(searchCampaignForm());

	}

	private Form<?> searchCampaignForm() {

		Form<?> form = new Form<Object>("searchCampaignForm");

		final TextField<String> campaignId = new TextField<String>(
				"campaignId", new Model<String>(""));
		form.add(campaignId);

		final TextField<String> subject = new TextField<String>("subject",
				new Model<String>(""));
		form.add(subject);

		final TextField<String> sender = new TextField<String>("sender",
				new Model<String>(""));
		form.add(sender);

		final TextField<String> receiver = new TextField<String>("receiver",
				new Model<String>(""));
		form.add(receiver);

		form.add(new AjaxSubmitLink("searchCampaign") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

		});

		return form;

	}

	private Form<?> searchMessageForm() {

		Form<?> form = new Form<Object>("searchMessageForm");

		final TextField<String> messageId = new TextField<String>("messageId",
				new Model<String>(""));
		form.add(messageId);

		final TextField<String> subject = new TextField<String>("subject",
				new Model<String>(""));
		form.add(subject);

		final TextField<String> sender = new TextField<String>("sender",
				new Model<String>(""));
		form.add(sender);

		final TextField<String> receiver = new TextField<String>("receiver",
				new Model<String>(""));
		form.add(receiver);

		form.add(new AjaxSubmitLink("searchMessage") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

		});

		return form;

	}

	private Form<?> searchBookForm(final WebMarkupContainer parent) {

		Form<?> form = new Form<Object>("searchBookForm");

		final TextField<String> bookId = new TextField<String>("bookId",
				new Model<String>(""));
		form.add(bookId);

		final TextField<String> author = new TextField<String>("author",
				new Model<String>(""));
		form.add(author);

		final TextField<String> title = new TextField<String>("title",
				new Model<String>(""));
		form.add(title);

		final TextField<String> category = new TextField<String>("category",
				new Model<String>(""));
		form.add(category);

		final TextField<String> subcategory = new TextField<String>(
				"subcategory", new Model<String>(""));
		form.add(subcategory);

		final TextField<String> tag = new TextField<String>("tag",
				new Model<String>(""));
		form.add(tag);

		final DatePicker<Date> lowPublishDate = new DatePicker<Date>(
				"lowPublishDate", new Model<Date>(), Date.class);
		form.add(lowPublishDate);

		final DatePicker<Date> highPublishDate = new DatePicker<Date>(
				"highPublishDate", new Model<Date>(), Date.class);
		form.add(highPublishDate);

		final LoadableDetachableModel resultsModel = new LoadableDetachableModel() {
			protected Object load() {
				return bookService.getAllBooks(0, 10);
			}
		};
		
		resultsModel.setObject(bookService.getAllBooks(0,20));

		ListView lv = new ListView("bookResultList", resultsModel) {

			@Override
			protected void populateItem(ListItem item) {
				final Book book = (Book) item.getModelObject();
				CompoundPropertyModel<Book> model = new CompoundPropertyModel<Book>(
						book);

				item.setDefaultModel(model);

				item.add(new Label("id"));
				item.add(new Label("title"));
			}

		};
	
		resultsModel.setObject(bookService.getAllBooks(0,30));
		
		parent.add(lv);
		
		resultsModel.setObject(bookService.getAllBooks(0,40));

		// List<Book> books = bookService.getAllBooks(0, 10);
		//
		// PropertyListView<Book> propertyListView = new PropertyListView<Book>(
		// "bookResultList", books) {
		// protected void populateItem(ListItem<Book> item) {
		// final Book book = (Book) item.getModelObject();
		// CompoundPropertyModel<Book> model = new CompoundPropertyModel<Book>(
		// book);
		//
		// item.setDefaultModel(model);
		//
		// item.add(new Label("id"));
		// item.add(new Label("title"));
		//
		//
		// }
		// };
		//
		// parent.add(propertyListView);

		form.add(new AjaxSubmitLink("searchBook") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				String bookIdString = bookId.getDefaultModelObjectAsString();
				String tagString = tag.getDefaultModelObjectAsString();

				System.out.println(bookService.getBook(Long
						.valueOf(bookIdString)));

				List<Book> books = bookService
						.findBookByTag(tagString, 0, 1000);
				
				resultsModel.setObject(books);
				
				target.add(parent);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

		});

		return form;

	}

	private Form<?> searchUserForm() {

		Form<?> form = new Form<Object>("searchUserForm");

		final TextField<String> userId = new TextField<String>("userId",
				new Model<String>(""));
		form.add(userId);

		final TextField<String> username = new TextField<String>("username",
				new Model<String>(""));
		form.add(username);

		List<String> genders = Arrays.asList(new String[] { "Todos",
				"Masculino", "Femenino" });

		final DropDownChoice<String> genderSelect = new DropDownChoice<String>(
				"gender", new PropertyModel<String>(this, ""), genders);

		form.add(genderSelect);

		final TextField<String> lowAgeField = new TextField<String>("lowAge",
				new Model<String>(""));

		form.add(lowAgeField);

		final TextField<String> highAgeField = new TextField<String>("highAge",
				new Model<String>(""));

		form.add(highAgeField);

		final DatePicker<Date> lowRegistrationDateField = new DatePicker<Date>(
				"lowRegistrationDate", new Model<Date>(), Date.class);
		form.add(lowRegistrationDateField);

		final DatePicker<Date> highRegistrationDateField = new DatePicker<Date>(
				"highRegistrationDate", new Model<Date>(), Date.class);
		form.add(highRegistrationDateField);

		List<String> countryList = userService.getAllCountries();

		final DropDownChoice<String> countrySelect = new DropDownChoice<String>(
				"country", new PropertyModel<String>(this, ""), countryList);

		form.add(countrySelect);

		form.add(new AjaxSubmitLink("searchUser") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

		});

		return form;

	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub

	}

}
