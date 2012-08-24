package com.booktube.pages;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.odlabs.wiquery.ui.datepicker.DatePicker;

import com.booktube.model.Book;
import com.booktube.model.BookTag;
import com.booktube.pages.validators.CustomDateValidator;

public class AdvancedSearchPage extends BasePage {

	private static final long serialVersionUID = 1L;

	private static final int ITEMS_PER_PAGE = 5;
	
	//final private Label feedbackMessage = new Label("feedbackMessage", "<h2>No se encontraron resultados.</h2>");
	final private Label feedbackMessage = new Label("feedbackMessage", new ResourceModel("noResults"));
	final private String dateFormat = "dd/mm/yy";
	final private DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	final private DataView<Book> dataView;
	final private PagingNavigator footerNavigator;
	final private WebMarkupContainer tableContainer;
	
	private String searchAuthor = null;
	private String searchTitle = null;
	private String searchCategory = null;
	private String searchSubcategory = null;
	private String searchTag = null;
	private Long searchBookId = null;
	private Date searchLowPublishDate = null;
	private Date searchHighPublishDate = null;
	private Double searchLowRating = null;
	private Double searchHighRating = null;
	
	public AdvancedSearchPage() {
		final WebMarkupContainer parent = new WebMarkupContainer(
				"advancedSearch");
		parent.setOutputMarkupId(true);
		add(parent);
		
		addBreadcrumb(new BookmarkablePageLink<Object>("link", AdvancedSearchPage.class), new ResourceModel("advancedSearchPageTitle").getObject());
		
		final FeedbackPanel formFeedback = new FeedbackPanel("formFeedback");
		formFeedback.setOutputMarkupId(true);
		parent.add(formFeedback);
		
		feedbackMessage.setEscapeModelStrings(false);
		feedbackMessage.setVisible(false);
		
		final Form<Book> searchBookForm = searchBookForm(parent, formFeedback);
		parent.add(searchBookForm);
		
//		group = new CheckGroup<Book>("group", new ArrayList<Book>());
//		searchBookForm.add(group);
		tableContainer = new WebMarkupContainer("tableContainer");
		
		dataView = bookList("bookList");
		tableContainer.add(dataView);
		
		parent.add(tableContainer);
		
		footerNavigator = new PagingNavigator("footerPaginator", dataView);
		parent.add(footerNavigator);
		
	}

	private Form<Book> searchBookForm(final WebMarkupContainer parent, final FeedbackPanel feedback) {

		Form<Book> form = new Form<Book>("searchBookForm");

		CompoundPropertyModel<Book> model = new CompoundPropertyModel<Book>(
				new Book());

		form.setDefaultModel(model);
		
		form.add(feedbackMessage);

		final TextField<Long> bookId = new TextField<Long>("bookId",
				new Model<Long>());
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

		final DatePicker<Date> lowPublishDate = createDatePicker(
				"lowPublishDate", dateFormat);
		form.add(lowPublishDate);

		final DatePicker<Date> highPublishDate = createDatePicker(
				"highPublishDate", dateFormat);
		form.add(highPublishDate);

		form.add(new CustomDateValidator(lowPublishDate, highPublishDate));

		final TextField<Double> lowRating = new TextField<Double>("lowRating", new Model<Double>(), Double.class);
		form.add(lowRating);
		
		final TextField<Double> highRating = new TextField<Double>("highRating", new Model<Double>(), Double.class);
		form.add(highRating);
		
		form.add(new AjaxSubmitLink("searchBook") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

//				form.add(AttributeModifier.replace("style",
//						"display: block;"));
				// String bookIdString =

				try {
					searchBookId = new Long(bookId
							.getDefaultModelObjectAsString());
				} catch (NumberFormatException ex) {
					searchBookId = null;
				}

				// if
				// (!StringUtils.isBlank(bookId.getDefaultModelObjectAsString()))
				// {
				// searchBookId = Long.valueOf(bookId
				// .getDefaultModelObjectAsString());
				// } else {
				// searchBookId = null;
				// }

				searchTag = new String(tag.getDefaultModelObjectAsString());
				searchAuthor = new String(author
						.getDefaultModelObjectAsString());
				searchTitle = new String(title.getDefaultModelObjectAsString());
				searchCategory = new String(category
						.getDefaultModelObjectAsString());
				searchSubcategory = new String(subcategory
						.getDefaultModelObjectAsString());

				if (!StringUtils.isBlank(lowPublishDate
						.getDefaultModelObjectAsString())) {
					try {
						searchLowPublishDate = (Date) formatter
								.parse(lowPublishDate
										.getDefaultModelObjectAsString());
					} catch (ParseException e) {
						searchLowPublishDate = null;
					}
				} else {
					searchLowPublishDate = null;
				}

				if (!StringUtils.isBlank(highPublishDate
						.getDefaultModelObjectAsString())) {
					try {
						searchHighPublishDate = (Date) formatter
								.parse(highPublishDate
										.getDefaultModelObjectAsString());
					} catch (ParseException e) {
						searchHighPublishDate = null;
					}
				} else {
					searchHighPublishDate = null;
				}

				searchLowRating = lowRating.getConvertedInput();
				searchHighRating = highRating.getConvertedInput();

				if (dataView.getItemCount() <= 0) {
					tableContainer.setVisible(false);
					footerNavigator.setVisible(false);
					feedbackMessage.setVisible(true);
				} else {
					tableContainer.setVisible(true);
					footerNavigator.setVisible(true);
					feedbackMessage.setVisible(false);
				}

				dataView.setCurrentPage(0);
				target.add(parent);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedback);
			}

		});

		return form;

	}

	private DatePicker<Date> createDatePicker(String label, String dateFormat) {
		final DatePicker<Date> datePicker = new DatePicker<Date>(label,
				new Model<Date>(), Date.class);
		datePicker.setAltFormat(dateFormat);
		datePicker.setAltField(dateFormat);
		datePicker.setDateFormat(dateFormat);

		return datePicker;
	}
	
	private DataView<Book> bookList(String label) {

		IDataProvider<Book> dataProvider = new BookProvider();

		DataView<Book> dataView = new DataView<Book>(label, dataProvider,
				ITEMS_PER_PAGE) {

			private static final long serialVersionUID = -869452866439034394L;

			protected void populateItem(Item<Book> item) {
				final Book book = (Book) item.getModelObject();

				CompoundPropertyModel<Book> model = new CompoundPropertyModel<Book>(
						book);

				item.setDefaultModel(model);

				List<BookTag> tagList = null;

				if (book.getTags() != null) {
					tagList = new ArrayList<BookTag>(book.getTags());
				}

				item.add(new PropertyListView<Object>("tagList", tagList) {

					private static final long serialVersionUID = -7951435365391555660L;

					protected void populateItem(ListItem<Object> item) {
						BookTag tag = (BookTag) item.getModelObject();
						final PageParameters parameters = new PageParameters();
						parameters.set("tag", tag);
						parameters.set("type", "tag");

						BookmarkablePageLink<Object> bpl = new BookmarkablePageLink<Object>(
								"tagLink", BooksPage.class, parameters);
						bpl.add(new Label("tagName", tag.getValue()));
						item.add(bpl);

					}
				});

				final PageParameters parameters = new PageParameters();
				parameters.set("book", book.getId().toString());
				// item.add(new Label("title", book.getTitle()));
				// BookmarkablePageLink<Object> bplTitle = new
				// BookmarkablePageLink<Object>(
				// "viewLink", ShowBookPage.class, parameters);

				PageParameters detailsParameter = new PageParameters();
				detailsParameter.set("book", book.getId());
				detailsParameter.set("currentPage", this.getCurrentPage());

				BookmarkablePageLink<Object> titleLink = new BookmarkablePageLink<Object>(
						"viewLink", ShowBookPage.class, detailsParameter);

				// Link<Object> titleLink = new Link("viewLink") {
				// public void onClick() {
				// setResponsePage(new ShowBookPage(book.getId(),
				// BooksPage.this));
				// }
				// };

				titleLink.add(new Label("title", book.getTitle()));

				// bplTitle.add(new Label("title", book.getTitle()));
				// item.add(bplTitle);
				item.add(titleLink);
				parameters.set("author", book.getAuthor().getUsername());
				parameters.set("type", "author");
				BookmarkablePageLink<Object> bplAuthor = new BookmarkablePageLink<Object>(
						"authorLink", BooksPage.class, parameters);
				bplAuthor.add(new Label("authorName", book.getAuthor()
						.getUsername()));
				item.add(bplAuthor);
				item.add(new Label("publishDate", book.getPublishDate()
						.toString()));

				item.add(new BookmarkablePageLink<Object>("detailsLink",
						ShowBookPage.class, detailsParameter));
			}
		};

		return dataView;
	}
	
	@Override
	protected void setPageTitle() {
		String newTitle = "Booktube - " + new ResourceModel("advancedSearchPageTitle").getObject();
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}
	
	class BookProvider implements IDataProvider<Book> {

		private static final long serialVersionUID = 1L;

		public BookProvider() {
		}

		public Iterator<Book> iterator(int first, int count) {
			// return bookService.getAllBooks(first, count).iterator();
			return bookService.getBooks(first, count, searchBookId,
					searchAuthor, searchTitle, searchTag, searchCategory,
					searchSubcategory, searchLowPublishDate,
					searchHighPublishDate, searchLowRating, searchHighRating).iterator();
		}

		public int size() {
			// return bookService.getCount(type, parameter);
			// return this.books.size();
			return bookService.getCount(searchBookId, searchAuthor,
					searchTitle, searchTag, searchCategory, searchSubcategory,
					searchLowPublishDate, searchHighPublishDate, searchLowRating, searchHighRating);
		}

		public IModel<Book> model(Book book) {
			return new CompoundPropertyModel<Book>(book);
		}

		public void detach() {
			// TODO Auto-generated method stub

		}
	}

}