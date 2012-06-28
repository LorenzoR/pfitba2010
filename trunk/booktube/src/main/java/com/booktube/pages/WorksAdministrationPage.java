package com.booktube.pages;

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
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.odlabs.wiquery.core.effects.sliding.SlideToggle;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.DialogButton;

import com.booktube.model.Book;
import com.booktube.model.BookTag;

public class WorksAdministrationPage extends AdministrationPage {
	private static final long serialVersionUID = 7512914721917619810L;

	private static Dialog deleteDialog;
	private static Dialog deleteConfirmationDialog;

	private static Long bookId;

	private static Book deleteBook;

	private static String deleteBookTitle;

//	private Label deleteConfirmationLabel = new Label(
//			"delete_confirmation_dialog_text", new PropertyModel<String>(this,
//					"deleteConfirmationText")) {
//
//		private static final long serialVersionUID = 1L;
//
//		{
//			setOutputMarkupId(true);
//		}
//	};

	Model<String> deleteConfirmationModel = new Model<String>() {

		private static final long serialVersionUID = 1L;
		
		private String text;
		
		public String getObject() {
			return text;
		}
		
		public void setObject(String value) {
			this.text = value;
		}
	};
	
	private Label deleteConfirmationLabel = new Label(
			"delete_confirmation_dialog_text", deleteConfirmationModel);

	Model<String> successDialogModel = new Model<String>() {

		private static final long serialVersionUID = 1L;
		
		private String text;
		
		public String getObject() {
			return text;
		}
		
		public void setObject(String value) {
			this.text = value;
		}
	};
	
	private Label successDialogLabel = new Label("success_dialog_text", successDialogModel);
	
//	private Label successDialogLabel = new Label("success_dialog_text",
//			new PropertyModel<String>(this, "successDialogText")) {
//		private static final long serialVersionUID = 1L;
//
//		{
//			setOutputMarkupId(true);
//		}
//	};

//	private String successDialogText;

	private final DataView<Book> dataView;
	private final PagingNavigator footerNavigator;

	private final CheckGroup<Book> group;

	private String searchAuthor = null;
	private String searchTitle = null;
	private String searchCategory = null;
	private String searchSubcategory = null;
	private String searchTag = null;
	private Long searchBookId = null;
	private Date searchLowPublishDate = null;
	private Date searchHighPublishDate = null;

	final LoadableDetachableModel<List<Book>> resultsModel = new LoadableDetachableModel<List<Book>>() {
		private static final long serialVersionUID = 1L;

		protected List<Book> load() {
			return null;
		}
	};

	public WorksAdministrationPage() {
		super();
		
		final WebMarkupContainer parent = new WebMarkupContainer(
				"worksContainer");
		parent.setOutputMarkupId(true);
		add(parent);

		deleteConfirmationLabel.setOutputMarkupId(true);
		successDialogLabel.setOutputMarkupId(true);
		
		parent.add(new Label("pageTitle", "Works Administration Page"));

		group = new CheckGroup<Book>("group", new ArrayList<Book>());

		dataView = bookList("bookList");

		group.add(dataView);

		footerNavigator = new PagingNavigator("footerPaginator", dataView);
		parent.add(footerNavigator);

		deleteDialog = deleteDialog();
		parent.add(deleteDialog);

		deleteConfirmationDialog = deleteConfirmationDialog();
		parent.add(deleteConfirmationDialog);

		final Form<Book> searchBookForm = searchBookForm(parent);

		parent.add(searchBookForm);

		searchBookForm.add(group);

		WebMarkupContainer searchButton = createButtonWithEffect(
				"searchBookLink", "searchFields", new SlideToggle());
		parent.add(searchButton);

		String newTitle = "Booktube - Works Administration";
		super.get("pageTitle").setDefaultModelObject(newTitle);

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

				item.add(new BookmarkablePageLink<Object>("editLink",
						EditBookPage.class, detailsParameter));
				item.add(new BookmarkablePageLink<Object>("detailsLink",
						ShowBookPage.class, detailsParameter));

				item.add(new AjaxLink<Book>("deleteLink", item.getModel()) {
					private static final long serialVersionUID = -7155146615720218460L;

					@Override
					public void onClick(AjaxRequestTarget target) {

						deleteBook = (Book) getModelObject();

						bookId = book.getId();
						// deleteBook = bookService.getBook(bookId);
						System.out.println("Book ID " + bookId);
						System.out.println("BOOK ES : " + deleteBook);

						deleteConfirmationDialog.open(target);

						deleteBookTitle = deleteBook.getTitle();

						//deleteConfirmationText = "Esta seguro que desea eliminar la obra "
						//		+ deleteBookTitle + " ?";
						deleteConfirmationModel.setObject("Esta seguro que desea eliminar la obra "
								+ deleteBookTitle + " ?");

						target.add(deleteConfirmationLabel);
					}

				});
			}
		};

		return dataView;
	}

	private Dialog deleteDialog() {

		Dialog dialog = new Dialog("success_dialog");

		dialog.add(successDialogLabel);

		AjaxDialogButton ok = new AjaxDialogButton("OK") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				setResponsePage(WorksAdministrationPage.class);

			}
		};

		dialog.setButtons(ok);
		dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close().render()));

		return dialog;

	}

	private Dialog deleteConfirmationDialog() {

		final Dialog dialog = new Dialog("delete_confirmation_dialog");

		// labelText = "original2";
		// dialog.add(new Label("delete_confirmation_dialog_text",
		// "Esta seguro que desea eliminar el usuario?"));
		dialog.add(deleteConfirmationLabel);
		System.out.println("USERID " + bookId);
		// System.out.println("USER: " + user);
		// labelText = "original3";

		AjaxDialogButton yesButton = new AjaxDialogButton("Si") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {

				System.out.println("Borro Boook");

				System.out.println("USER ES : " + deleteBook);
				bookService.deleteBook(deleteBook);

				//successDialogText = "Obra " + deleteBookTitle + " eliminada.";
				successDialogModel.setObject("Obra " + deleteBookTitle + " eliminada.");
				target.add(successDialogLabel);
				// JsScopeUiEvent.quickScope(deleteConfirmationdialog.close().render());
				JsScope.quickScope(dialog.close().render());
				// deleteConfirmationdialog.close(target);
				deleteDialog.open(target);
				// setResponsePage(MessagesAdministrationPage.class);

			}
		};

		DialogButton noButton = new DialogButton("No",
				JsScope.quickScope(dialog.close().render()));

		dialog.setButtons(noButton, yesButton);
		dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close()));

		return dialog;

	}

	private Form<Book> searchBookForm(final WebMarkupContainer parent) {

		Form<Book> form = new Form<Book>("searchBookForm");

		CompoundPropertyModel<Book> model = new CompoundPropertyModel<Book>(
				new Book());

		form.setDefaultModel(model);

		final WebMarkupContainer searchFields = new WebMarkupContainer(
				"searchFields");
		searchFields.add(AttributeModifier.replace("style", "display: none;"));
		form.add(searchFields);

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

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				System.out.println("selected book(s): "
						+ group.getDefaultModelObjectAsString());

				@SuppressWarnings("unchecked")
				List<Book> removedBooks = (List<Book>) group
						.getDefaultModelObject();

				for (Book aBook : removedBooks) {
					bookService.deleteBook(aBook);
				}

				// removedBooks.remove(books.size()-1);
				// List<Book> books = bookService.getBooks(0, Integer.MAX_VALUE,
				// authorString, titleString, tagString, categoryString,
				// subcategoryString, lowPublishDateString,
				// highPublishDateString);

				// if (books.size() > 0) {
				// resultsModel.setObject(books);
				// } else {
				// resultsModel.setObject(null);
				// group.setVisible(false);
				// }
				if (dataView.getItemCount() <= 0) {
					this.setVisible(false);
					footerNavigator.setVisible(false);
				} else {
					this.setVisible(true);
					footerNavigator.setVisible(true);
				}

				target.add(parent);

				// System.out.println("BOOKS: " + books);

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

				// System.out.println(bookService.getBook(Long
				// .valueOf(bookIdString)));

				// List<Book> books = bookService.getBooks(0, Integer.MAX_VALUE,
				// searchAuthor, searchTitle, searchTag, searchCategory,
				// searchSubcategory, searchLowPublishDate,
				// searchHighPublishDate);

				/*
				 * if (books.size() > 0) {
				 * 
				 * resultsModel.setObject(books);
				 * System.out.println("DETACHABLE: " +
				 * resultsModel.getObject().toString()); group.setVisible(true);
				 * checkGroupSelector.setVisible(true);
				 * bookResultListTable.setVisible(true);
				 * 
				 * target.add(parent); } else { resultsModel.setObject(null);
				 * group.setVisible(false);
				 * checkGroupSelector.setVisible(false);
				 * bookResultListTable.setVisible(false);
				 * System.out.println("NO HAY NADA"); }
				 */

				if (dataView.getItemCount() <= 0) {
					deleteBook.setVisible(false);
					footerNavigator.setVisible(false);
				} else {
					deleteBook.setVisible(true);
					footerNavigator.setVisible(true);
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

	class BookProvider implements IDataProvider<Book> {

		private static final long serialVersionUID = 1L;

		public BookProvider() {
		}

		public Iterator<Book> iterator(int first, int count) {
			// return bookService.getAllBooks(first, count).iterator();
			return bookService.getBooks(first, count, searchBookId,
					searchAuthor, searchTitle, searchTag, searchCategory,
					searchSubcategory, searchLowPublishDate,
					searchHighPublishDate).iterator();
		}

		public int size() {
			// return bookService.getCount(type, parameter);
			// return this.books.size();
			return bookService.getCount(searchBookId, searchAuthor,
					searchTitle, searchTag, searchCategory, searchSubcategory,
					searchLowPublishDate, searchHighPublishDate);
		}

		public IModel<Book> model(Book book) {
			return new CompoundPropertyModel<Book>(book);
		}

		public void detach() {
			// TODO Auto-generated method stub

		}
	}

}
