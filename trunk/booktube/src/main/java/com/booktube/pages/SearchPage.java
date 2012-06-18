package com.booktube.pages;

import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.ui.datepicker.DatePicker;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.odlabs.wiquery.core.effects.Effect;
import org.odlabs.wiquery.core.effects.EffectBehavior;
import org.odlabs.wiquery.core.effects.basic.Hide;
import org.odlabs.wiquery.core.effects.basic.Show;
import org.odlabs.wiquery.core.effects.basic.Toggle;
import org.odlabs.wiquery.core.effects.sliding.SlideToggle;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;

import com.booktube.WiaSession;
import com.booktube.model.Book;
import com.booktube.model.Campaign;
import com.booktube.model.Message;
import com.booktube.model.User;
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

	private String authorString = null;
	private String titleString = null;
	private String categoryString = null;
	private String subcategoryString = null;
	private String tagString = null;
	private Long bookIdSearch = null;
	private Date lowPublishDateString = null;
	private Date highPublishDateString = null;

	private Long campaignId = null;
	private String campaignSubject = null;
	private User campaignSender = null;
	private User campaignReceiver = null;
	private Date lowCampaignDate = null;
	private Date highCampaignDate = null;

	DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	final private String dateFormat = "dd/mm/yy";

	public SearchPage() {

		final WebMarkupContainer parent = new WebMarkupContainer("searchPage");
		parent.setOutputMarkupId(true);
		add(parent);

		parent.add(searchUserForm(parent));
		parent.add(searchBookForm(parent));
		parent.add(searchMessageForm(parent));
		parent.add(searchCampaignForm(parent));

		WebMarkupContainer userButton = createButtonWithEffect(
				"searchUserLink", "searchUserFormId", new SlideToggle());
		parent.add(userButton);

		WebMarkupContainer bookButton = createButtonWithEffect(
				"searchBookLink", "searchBookFormId", new SlideToggle());
		parent.add(bookButton);

		WebMarkupContainer messageButton = createButtonWithEffect(
				"searchMessageLink", "searchMessageFormId", new SlideToggle());
		parent.add(messageButton);

		WebMarkupContainer campaignButton = createButtonWithEffect(
				"searchCampaignLink", "searchCampaignFormId", new SlideToggle());
		parent.add(campaignButton);

	}

	private Form<?> searchCampaignForm(final WebMarkupContainer parent) {

		Form<?> form = new Form<Object>("searchCampaignForm");
		form.add(AttributeModifier.replace("style", "display: none;"));

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

		final DatePicker<Date> lowCampaignDateField = createDatePicker(
				"lowCampaignDate", dateFormat);
		form.add(lowCampaignDateField);

		final DatePicker<Date> highCampaignDateField = createDatePicker(
				"highCampaignDate", dateFormat);
		form.add(highCampaignDateField);

		final LoadableDetachableModel<List<Campaign>> resultsModel = new LoadableDetachableModel<List<Campaign>>() {
			protected List<Campaign> load() {
				return null;
			}
		};

		final CheckGroup group = new CheckGroup("group", new ArrayList());
		group.setVisible(false);
		form.add(group);

		final CheckGroupSelector checkGroupSelector = new CheckGroupSelector(
				"groupSelector");
		checkGroupSelector.setVisible(false);
		group.add(checkGroupSelector);

		final WebMarkupContainer campaignResultListTable = new WebMarkupContainer(
				"campaignResultListTable");
		campaignResultListTable.setVisible(false);

		group.add(campaignResultListTable);

		ListView<Campaign> campaignLV = new ListView<Campaign>(
				"campaignResultList", resultsModel) {

			@Override
			protected void populateItem(ListItem<Campaign> item) {
				final Campaign campaign = (Campaign) item.getModelObject();
				CompoundPropertyModel<Campaign> model = new CompoundPropertyModel<Campaign>(
						campaign);

				item.setDefaultModel(model);
				item.add(new Check("checkbox", item.getModel()));
				item.add(new Label("id"));
				item.add(new Label("subject"));
				item.add(new Label("sender"));
			}

		};

		campaignResultListTable.add(campaignLV);

		form.add(new AjaxSubmitLink("searchCampaign") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				form.add(AttributeModifier.replace("style", "display: block;"));

				campaignSubject = new String(subject
						.getDefaultModelObjectAsString());

				if (userService.getUser(sender.getDefaultModelObjectAsString()) != null) {
					campaignSender = userService.getUser(sender
							.getDefaultModelObjectAsString());
				} else {
					campaignSender = null;
				}

				if (userService.getUser(receiver
						.getDefaultModelObjectAsString()) != null) {
					campaignReceiver = userService.getUser(receiver
							.getDefaultModelObjectAsString());
				} else {
					campaignReceiver = null;
				}

				if (!StringUtils.isBlank(lowCampaignDateField
						.getDefaultModelObjectAsString())) {
					try {
						lowCampaignDate = (Date) formatter
								.parse(lowCampaignDateField
										.getDefaultModelObjectAsString());
					} catch (ParseException e) {
						lowCampaignDate = null;
					}
				} else {
					lowCampaignDate = null;
				}

				if (!StringUtils.isBlank(highCampaignDateField
						.getDefaultModelObjectAsString())) {
					try {
						highCampaignDate = (Date) formatter
								.parse(highCampaignDateField
										.getDefaultModelObjectAsString());
					} catch (ParseException e) {
						highCampaignDate = null;
					}
				} else {
					highCampaignDate = null;
				}

				List<Campaign> campaigns = campaignService.getCampaigns(0,
						Integer.MAX_VALUE, campaignSubject, campaignSender,
						campaignReceiver, lowCampaignDate, highCampaignDate);

				if (campaigns.size() > 0) {

					resultsModel.setObject(campaigns);
					System.out.println("DETACHABLE: "
							+ resultsModel.getObject().toString());
					group.setVisible(true);
					campaignResultListTable.setVisible(true);

					target.add(parent);
				} else {
					resultsModel.setObject(null);
					group.setVisible(false);
					campaignResultListTable.setVisible(false);
				}

				target.add(parent);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

		});

		AjaxSubmitLink deleteBook = new AjaxSubmitLink("deleteCampaign") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// System.out.println("selected book(s): "
				// + group.getDefaultModelObjectAsString());
				// List<Book> removedBooks = (List<Book>) group
				// .getDefaultModelObject();
				//
				// for (Book aBook : removedBooks) {
				// bookService.deleteBook(aBook);
				// }
				//
				// // removedBooks.remove(books.size()-1);
				// List<Book> books = bookService.getBooks(0, Integer.MAX_VALUE,
				// authorString, titleString, tagString, categoryString,
				// subcategoryString, lowPublishDateString,
				// highPublishDateString);
				//
				// if (books.size() > 0) {
				// resultsModel.setObject(books);
				// } else {
				// resultsModel.setObject(null);
				// group.setVisible(false);
				// }
				//
				// target.add(parent);
				//
				// // System.out.println("BOOKS: " + books);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

		};

		group.add(deleteBook);

		return form;

	}

	private Form<?> searchMessageForm(final WebMarkupContainer parent) {

		Form<?> form = new Form<Object>("searchMessageForm");
		form.add(AttributeModifier.replace("style", "display: none;"));

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

		final DatePicker<Date> lowMessageDate = createDatePicker(
				"lowMessageDate", dateFormat);
		form.add(lowMessageDate);

		final DatePicker<Date> highMessageDate = createDatePicker(
				"highMessageDate", dateFormat);
		form.add(highMessageDate);

		form.add(new AjaxSubmitLink("searchMessage") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				form.add(AttributeModifier.replace("style", "display: block;"));

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
		form.add(AttributeModifier.replace("style", "display: none;"));

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

		/*
		 * final DatePicker<Date> lowPublishDate = new DatePicker<Date>(
		 * "lowPublishDate", new Model<Date>(), Date.class);
		 * lowPublishDate.setAltFormat("dd/mm/yy");
		 * lowPublishDate.setAltField("dd/mm/yy");
		 * lowPublishDate.setDateFormat("dd/mm/yy"); form.add(lowPublishDate);
		 */
		final DatePicker<Date> lowPublishDate = createDatePicker(
				"lowPublishDate", dateFormat);
		form.add(lowPublishDate);
		// final DatePicker<Date> lowPublishDate = new
		// org.apache.wicket.extensions.yui.calendar.DatePicker<Date>()

		// final DatePicker<Date> highPublishDate = new DatePicker<Date>(
		// "highPublishDate", new Model<Date>(), Date.class);
		// highPublishDate.setAltFormat("dd/mm/yy");
		// highPublishDate.setAltField("dd/mm/yy");
		// highPublishDate.setDateFormat("dd/mm/yy");
		// form.add(highPublishDate);
		final DatePicker<Date> highPublishDate = createDatePicker(
				"highPublishDate", dateFormat);
		form.add(highPublishDate);

		final LoadableDetachableModel<List<Book>> resultsModel = new LoadableDetachableModel<List<Book>>() {
			protected List<Book> load() {
				return null;
			}
		};

		final CheckGroup group = new CheckGroup("group", new ArrayList());
		group.setVisible(false);
		form.add(group);

		final CheckGroupSelector checkGroupSelector = new CheckGroupSelector(
				"groupSelector");
		checkGroupSelector.setVisible(false);
		group.add(checkGroupSelector);

		final WebMarkupContainer bookResultListTable = new WebMarkupContainer(
				"bookResultListTable");
		bookResultListTable.setVisible(false);
		
		group.add(bookResultListTable);

		ListView<Book> booksLV = new ListView<Book>("bookResultList",
				resultsModel) {

			@Override
			protected void populateItem(ListItem<Book> item) {
				final Book book = (Book) item.getModelObject();
				CompoundPropertyModel<Book> model = new CompoundPropertyModel<Book>(
						book);

				PageParameters detailsParameter = new PageParameters();
				detailsParameter.set("book", book.getId());

				item.setDefaultModel(model);
				item.add(new Check("checkbox", item.getModel()));
				item.add(new Label("id"));
				item.add(new Label("title"));
				item.add(new Label("author"));

				item.add(new BookmarkablePageLink<Book>("editLink",
						EditBookPage.class, detailsParameter));
				item.add(new BookmarkablePageLink<Book>("detailsLink",
						ShowBookPage.class, detailsParameter));

				item.add(new AjaxLink<Book>("deleteLink", item.getModel()) {

					@Override
					public void onClick(AjaxRequestTarget target) {
						System.out.println("BOOk es : " + book);

						// bookService.deleteBook(book);
						bookService.deleteBook(book);
						System.out.println("Book " + bookId + " deleted.");

						List<Book> books = bookService.getBooks(0,
								Integer.MAX_VALUE, authorString, titleString,
								tagString, categoryString, subcategoryString,
								lowPublishDateString, highPublishDateString);

						if (books.size() > 0) {

							resultsModel.setObject(books);
							System.out.println("DETACHABLE: "
									+ resultsModel.getObject().toString());
							group.setVisible(true);
							checkGroupSelector.setVisible(true);
							bookResultListTable.setVisible(true);

							target.add(parent);
						} else {
							resultsModel.setObject(null);
							group.setVisible(false);
							checkGroupSelector.setVisible(false);
							bookResultListTable.setVisible(false);
							System.out.println("NO HAY NADA");
						}

						target.add(parent);
					}

				});
			}

		};

		bookResultListTable.add(booksLV);

		form.add(new AjaxSubmitLink("searchBook") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				form.add(AttributeModifier.replace("style", "display: block;"));
				// String bookIdString = bookId.getDefaultModelObjectAsString();

				if (!StringUtils.isBlank(bookId.getDefaultModelObjectAsString())) {
					bookIdSearch = Long.valueOf(bookId
							.getDefaultModelObjectAsString());
				} else {
					bookIdSearch = null;
				}

				tagString = new String(tag.getDefaultModelObjectAsString());
				authorString = new String(author
						.getDefaultModelObjectAsString());
				titleString = new String(title.getDefaultModelObjectAsString());
				categoryString = new String(category
						.getDefaultModelObjectAsString());
				subcategoryString = new String(subcategory
						.getDefaultModelObjectAsString());

				if (!StringUtils.isBlank(lowPublishDate
						.getDefaultModelObjectAsString())) {
					System.out.println("LowDate: "
							+ lowPublishDate.getDefaultModelObjectAsString());
					try {
						lowPublishDateString = (Date) formatter
								.parse(lowPublishDate
										.getDefaultModelObjectAsString());
					} catch (ParseException e) {
						lowPublishDateString = null;
					}
				} else {
					lowPublishDateString = null;
				}

				if (!StringUtils.isBlank(highPublishDate
						.getDefaultModelObjectAsString())) {
					try {
						highPublishDateString = (Date) formatter
								.parse(highPublishDate
										.getDefaultModelObjectAsString());
					} catch (ParseException e) {
						highPublishDateString = null;
					}
				} else {
					highPublishDateString = null;
				}

				// System.out.println(bookService.getBook(Long
				// .valueOf(bookIdString)));

				List<Book> books = bookService.getBooks(0, Integer.MAX_VALUE,
						authorString, titleString, tagString, categoryString,
						subcategoryString, lowPublishDateString,
						highPublishDateString);

				if (books.size() > 0) {

					resultsModel.setObject(books);
					System.out.println("DETACHABLE: "
							+ resultsModel.getObject().toString());
					group.setVisible(true);
					checkGroupSelector.setVisible(true);
					bookResultListTable.setVisible(true);

					target.add(parent);
				} else {
					resultsModel.setObject(null);
					group.setVisible(false);
					checkGroupSelector.setVisible(false);
					bookResultListTable.setVisible(false);
					System.out.println("NO HAY NADA");
				}

				target.add(parent);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

		});

		AjaxSubmitLink deleteBook = new AjaxSubmitLink("deleteBook") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				System.out.println("selected book(s): "
						+ group.getDefaultModelObjectAsString());
				List<Book> removedBooks = (List<Book>) group
						.getDefaultModelObject();

				for (Book aBook : removedBooks) {
					bookService.deleteBook(aBook);
				}

				// removedBooks.remove(books.size()-1);
				List<Book> books = bookService.getBooks(0, Integer.MAX_VALUE,
						authorString, titleString, tagString, categoryString,
						subcategoryString, lowPublishDateString,
						highPublishDateString);

				if (books.size() > 0) {
					resultsModel.setObject(books);
				} else {
					resultsModel.setObject(null);
					group.setVisible(false);
				}

				target.add(parent);

				// System.out.println("BOOKS: " + books);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

		};

		group.add(deleteBook);

		return form;

	}

	private Form<?> searchUserForm(final WebMarkupContainer parent) {

		Form<?> form = new Form<Object>("searchUserForm");
		form.add(AttributeModifier.replace("style", "display: none;"));

		final TextField<String> userId = new TextField<String>("userId",
				new Model<String>(""));
		form.add(userId);

		final TextField<String> username = new TextField<String>("username",
				new Model<String>(""));
		form.add(username);

		List<String> genders = Arrays.asList(new String[] { "Todos",
				"Masculino", "Femenino" });

		final DropDownChoice<String> genderSelect = new DropDownChoice<String>(
				"gender", new Model<String>(), genders);

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
				"country", new Model<String>(), countryList);

		form.add(countrySelect);

		form.add(new AjaxSubmitLink("searchUser") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				form.add(AttributeModifier.replace("style", "display: block;"));

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

		});

		return form;

	}

	private WebMarkupContainer createButtonWithEffect(String buttonId,
			final String textId, final Effect effect) {
		WebMarkupContainer button = new WebMarkupContainer(buttonId);

		button.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			private static final long serialVersionUID = 1L;

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.odlabs.wiquery.core.events.Event#callback()
			 */
			@Override
			public JsScope callback() {
				return JsScope.quickScope(new JsStatement()
						.$(null, "#" + textId).chain(effect).render());
			}
		}));

		// add(button);

		return button;
	}

	private DatePicker<Date> createDatePicker(String label, String dateFormat) {
		final DatePicker<Date> datePicker = new DatePicker<Date>(label,
				new Model<Date>(), Date.class);
		datePicker.setAltFormat(dateFormat);
		datePicker.setAltField(dateFormat);
		datePicker.setDateFormat(dateFormat);

		return datePicker;
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub

	}

}
