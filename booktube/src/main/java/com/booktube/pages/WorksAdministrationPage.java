package com.booktube.pages;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.odlabs.wiquery.core.effects.sliding.SlideToggle;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.DialogButton;

import com.booktube.model.Book;
import com.booktube.pages.customComponents.DynamicLabel;
import com.booktube.pages.customComponents.SuccessDialog;

public class WorksAdministrationPage extends AdministrationPage {
	private static final long serialVersionUID = 1L;

	private static SuccessDialog<?> successDialog;
	private static Dialog deleteConfirmationDialog;

	//private static Long bookId;
	//private static String deleteBookTitle;

	private static List<Book> removedBooks = null;
	private static Book deleteBook = null;

	private DynamicLabel deleteConfirmationLabel = new DynamicLabel("delete_confirmation_dialog_text", new Model<String>());
	
	private final DataView<Book> dataView;
	private AjaxPagingNavigator footerNavigator;
	
	private final Form<Book> searchBookForm;
	private final WebMarkupContainer searchButton;

	private final CheckGroup<Book> group;

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

	public WorksAdministrationPage() {
		super();
		
		final WebMarkupContainer parent = new WebMarkupContainer(
				"worksContainer");
		parent.setOutputMarkupId(true);
		add(parent);

		addBreadcrumb(new BookmarkablePageLink<Object>("link", WorksAdministrationPage.class), new ResourceModel("worksAdministrationPageTitle").getObject());

		dataView = bookList("bookList");

		group = new CheckGroup<Book>("group", new ArrayList<Book>());
		group.add(dataView);
		group.add(new CheckGroupSelector("groupSelector"));
		
		successDialog = new SuccessDialog<WorksAdministrationPage>("success_dialog",  new ResourceModel("bookDeleted").getObject(), parent);
		parent.add(successDialog);

		deleteConfirmationDialog = deleteConfirmationDialog();
		parent.add(deleteConfirmationDialog);

		searchBookForm = searchBookForm(parent);

		parent.add(searchBookForm);

		searchBookForm.add(group);

		searchButton = createButtonWithEffect(
				"searchBookLink", "searchFields", new SlideToggle());
		parent.add(searchButton);
		
		if (dataView.getItemCount() > 0) {
			feedbackMessage.setVisible(false);
		}
		else {
			feedbackMessage.setVisible(true);
			searchBookForm.setVisible(false);
			footerNavigator.setVisible(false);
			searchButton.setVisible(false);
		}

		//String newTitle = "Booktube - Works Administration";
		//super.get("pageTitle").setDefaultModelObject(newTitle);

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

				item.add(new Check<Book>("checkbox", item.getModel()));

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

				titleLink.add(new Label("title", book.getTitle()));

				item.add(titleLink);
				parameters.set("author", book.getAuthor().getUsername());
				parameters.set("type", "author");
				BookmarkablePageLink<Object> bplAuthor = new BookmarkablePageLink<Object>(
						"authorLink", BooksPage.class, parameters);
				bplAuthor.add(new Label("authorName", book.getAuthor()
						.getUsername()));
				item.add(bplAuthor);
				
				final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, getLocale());
				item.add(new Label("publishDate", dateFormat.format(book.getPublishDate())));

				item.add(new BookmarkablePageLink<Object>("editLink",
						EditBookPage.class, detailsParameter));
				item.add(new BookmarkablePageLink<Object>("detailsLink",
						ShowBookPage.class, detailsParameter));

				item.add(new AjaxLink<Book>("deleteLink", item.getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {

						deleteBook = (Book) getModelObject();

						deleteConfirmationDialog.open(target);

						//deleteBookTitle = deleteBook.getTitle();

						
//						deleteConfirmationLabel.setLabel("Esta seguro que desea eliminar la obra "
//								+ deleteBookTitle + " ?");
						deleteConfirmationLabel.setLabel(new ResourceModel("deleteBookQuestion").getObject());

						target.add(deleteConfirmationLabel);
					}

				});
			}
		};

		return dataView;
	}

	private Dialog deleteConfirmationDialog() {

		final Dialog dialog = new Dialog("delete_confirmation_dialog");

		dialog.add(deleteConfirmationLabel);

		AjaxDialogButton yesButton = new AjaxDialogButton(new ResourceModel("yes").getObject()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {

				System.out.println("Borro Boook");

				System.out.println("USER ES : " + deleteBook);
				
				if ( deleteBook != null ) {
					bookService.deleteBook(deleteBook);
					deleteBook = null;
					successDialog.setText( new ResourceModel("bookDeleted").getObject());
				}
				else if ( removedBooks != null ) {
					successDialog.setText( new ResourceModel("booksDeleted").getObject());
					
					for (Book aBook : removedBooks) {
						bookService.deleteBook(aBook);
					}
					
					removedBooks = null;
					
				}
				
				dialog.close(target);
				target.add(successDialog);

				successDialog.open(target);
				
				showOrHideTable();
				
			}
		};

		DialogButton noButton = new DialogButton(new ResourceModel("no").getObject(),
				JsScope.quickScope(dialog.close().render()));

		dialog.setButtons(noButton, yesButton);

		return dialog;

	}

	private Form<Book> searchBookForm(final WebMarkupContainer parent) {

		Form<Book> form = new Form<Book>("searchBookForm");

		CompoundPropertyModel<Book> model = new CompoundPropertyModel<Book>(
				new Book());

		form.setDefaultModel(model);

		footerNavigator = new AjaxPagingNavigator("footerPaginator", dataView) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(parent);
				
			}
		};
		
		form.add(footerNavigator);
		
		final WebMarkupContainer searchFields = new WebMarkupContainer(
				"searchFields");
		searchFields.add(AttributeModifier.replace("style", "display: none;"));
		form.add(searchFields);

		searchFields.add(feedbackMessage);
		
		final TextField<String> bookId = new TextField<String>("bookId",
				new Model<String>(""));
		searchFields.add(bookId);

		final TextField<String> author = new TextField<String>("author",
				new Model<String>(""));
		searchFields.add(author);

		final TextField<String> title = new TextField<String>("title",
				new Model<String>(""));
		searchFields.add(title);

		final TextField<String> category = new TextField<String>("category",
				new Model<String>(""));
		searchFields.add(category);

		final TextField<String> subcategory = new TextField<String>(
				"subcategory", new Model<String>(""));
		searchFields.add(subcategory);

		final TextField<String> tag = new TextField<String>("tag",
				new Model<String>(""));
		searchFields.add(tag);

		final DatePicker<Date> lowPublishDate = createDatePicker(
				"lowPublishDate", dateFormat);
		searchFields.add(lowPublishDate);

		final DatePicker<Date> highPublishDate = createDatePicker(
				"highPublishDate", dateFormat);
		searchFields.add(highPublishDate);

		final AjaxSubmitLink deleteBook = new AjaxSubmitLink("deleteBook") {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				System.out.println("selected book(s): "
						+ group.getDefaultModelObjectAsString());

				
				deleteConfirmationDialog.open(target);

				deleteConfirmationLabel
						.setLabel(new ResourceModel("deleteBooksQuestion").getObject());

				target.add(deleteConfirmationLabel);
				
				removedBooks = (List<Book>) group.getDefaultModelObject();
				
				showOrHideTable();

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
			}

		};

		form.add(deleteBook);

		searchFields.add(new AjaxSubmitLink("searchBook") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				searchFields.add(AttributeModifier.replace("style",
						"display: block;"));

				try {
					searchBookId = new Long(bookId
							.getDefaultModelObjectAsString());
				} catch (NumberFormatException ex) {
					searchBookId = null;
				}

				searchTag = new String(tag.getDefaultModelObjectAsString());
				searchAuthor = new String(author
						.getDefaultModelObjectAsString());
				searchTitle = new String(title.getDefaultModelObjectAsString());
				searchCategory = new String(category
						.getDefaultModelObjectAsString());
				searchSubcategory = new String(subcategory
						.getDefaultModelObjectAsString());

				// TODO
				// CAMBIAR DATES 
				if (!StringUtils.isBlank(lowPublishDate
						.getDefaultModelObjectAsString())) {
					System.out.println("LowDate: "
							+ lowPublishDate.getDefaultModelObjectAsString());
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

				if (dataView.getItemCount() <= 0) {
					deleteBook.setVisible(false);
					group.setVisible(false);
					footerNavigator.setVisible(false);
					feedbackMessage.setVisible(true);
				} else {
					deleteBook.setVisible(true);
					group.setVisible(true);
					footerNavigator.setVisible(true);
					feedbackMessage.setVisible(false);
				}
				
				dataView.setCurrentPage(0);
				target.add(parent);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
			}

		});

		return form;

	}
	
	private void showOrHideTable() {
		if (dataView.getItemCount() <= 0) {
			searchBookForm.setVisible(false);
			dataView.setVisible(false);
			footerNavigator.setVisible(false);
			searchButton.setVisible(false);
			feedbackMessage.setVisible(true);
		} else {
			searchBookForm.setVisible(true);
			dataView.setVisible(true);
			footerNavigator.setVisible(true);
			searchButton.setVisible(true);
			feedbackMessage.setVisible(false);
		}
	}

	class BookProvider implements IDataProvider<Book> {

		private static final long serialVersionUID = 1L;

		public BookProvider() {
		}

		public Iterator<Book> iterator(int first, int count) {
			return bookService.getBooks(first, count, searchBookId,
					searchAuthor, searchTitle, searchTag, searchCategory,
					searchSubcategory, searchLowPublishDate,
					searchHighPublishDate, searchLowRating, searchHighRating).iterator();
		}

		public int size() {
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
