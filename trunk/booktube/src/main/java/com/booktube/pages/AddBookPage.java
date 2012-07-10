package com.booktube.pages;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;

import com.booktube.WiaSession;
import com.booktube.model.Book;
import com.booktube.model.User;
import com.booktube.pages.customComponents.AbstractAutoCompleteTextField;
import com.booktube.pages.customComponents.CustomTextField;
import com.booktube.pages.customComponents.SuccessDialog;
import com.booktube.pages.customConverters.TagSetToString;
import com.booktube.service.BookService;
import com.booktube.service.UserService;

public class AddBookPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	UserService userService;

	@SpringBean
	BookService bookService;

	private User user;
	private static Dialog dialog;
	private static PageParameters pageParameters = new PageParameters();

	public AddBookPage() {

		final WebMarkupContainer parent = new WebMarkupContainer("books");
		parent.setOutputMarkupId(true);
		add(parent);

		user = WiaSession.get().getLoggedInUser();

		// Label registerMessage = new Label("registerMessage",
		// "Debe registrarse para poder publicar.");
		// parent.add(registerMessage);

		final Dialog loginDialog = loginDialog();

		parent.add(loginDialog);

		WebMarkupContainer registerMessage = new WebMarkupContainer(
				"registerMessage");
		registerMessage.add(new BookmarkablePageLink<String>("registerLink",
				RegisterPage.class));
		AjaxLink<Void> loginLink = new AjaxLink<Void>("loginLink") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				loginDialog.open(target);
			}

		};
		registerMessage.add(loginLink);
		parent.add(registerMessage);

		Form<Book> form = addBookForm(parent);
		parent.add(form);

		if (user == null) {
			registerMessage.setVisible(true);
			form.setVisible(false);
		} else {
			form.setVisible(true);
			registerMessage.setVisible(false);
		}

//		AjaxDialogButton ok = new AjaxDialogButton("OK") {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onButtonClicked(AjaxRequestTarget target) {
//				// do your cancel logic here
//				System.out.println("BUTTON CLICKED!!");
//				setResponsePage(HomePage.class);
//				if (lastInsertedId != null) {
//					PageParameters pageParameters = new PageParameters();
//					pageParameters.set("book", lastInsertedId);
//					setResponsePage(ShowBookPage.class, pageParameters);
//				}
//
//			}
//		};
//
//		dialog = new Dialog("success_dialog");
//		dialog.setButtons(ok);
//		dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close().render()));
		
		dialog = new SuccessDialog<ShowBookPage>("success_dialog", "Obra agragada con éxito!", ShowBookPage.class, pageParameters);
		parent.add(dialog);

	}

	private Form<Book> addBookForm(final WebMarkupContainer parent) {
		Form<Book> form = new Form<Book>("form");

		final Book newBook = new Book(user);

		CompoundPropertyModel<Book> model = new CompoundPropertyModel<Book>(
				newBook);

		form.setDefaultModel(model);

		final RequiredTextField<Book> titleField = new RequiredTextField<Book>("title");

		form.add(titleField);

		// final TextField<Book> tagField = new TextField<Book>("tags");

		// final TextField<Book> tagField = new TextField<Book>("tags") {
		// private static final long serialVersionUID = 1L;
		//
		// @SuppressWarnings("unchecked")
		// @Override
		// public IConverter getConverter(Class type) {
		// return new SetToStringConverter();
		// }
		//
		// };
		final CustomTextField tagField = new CustomTextField("tags", null,
				new TagSetToString());

		form.add(tagField);

		final TextArea<Book> editor = new TextArea<Book>("text");
		editor.setOutputMarkupId(true);
		editor.setRequired(true);
		form.add(editor);

		final AbstractAutoCompleteTextField<String> category = new AbstractAutoCompleteTextField<String>(
				"category", new PropertyModel<Book>(newBook, "category")) {
			private static final long serialVersionUID = 1L;

			protected final List<String> getChoiceList(final String input) {
				return bookService.getCategories(0, Integer.MAX_VALUE);
			}

			protected final String getChoiceValue(final String choice)
					throws Throwable {
				return choice;
			}
		};
		category.setRequired(true);
		form.add(category);

		final AbstractAutoCompleteTextField<String> subcategory = new AbstractAutoCompleteTextField<String>(
				"subcategory", new PropertyModel<Book>(newBook, "subcategory")) {
			private static final long serialVersionUID = 1L;

			protected final List<String> getChoiceList(final String input) {
				return bookService.getSubcategories(0, Integer.MAX_VALUE, null);
			}

			protected final String getChoiceValue(final String choice)
					throws Throwable {
				return choice;
			}
		};

		form.add(subcategory);

		form.add(new AjaxSubmitLink("save") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				System.out.println("--- TITULO: " + newBook.getTitle());
				System.out.println("--- CATEGORY: " + newBook.getCategory());
				System.out.println("--- SUBCATEGORY: "
						+ newBook.getSubCategory());
				System.out.println("--- TAGS: " + newBook.getTags().toString());
//				String text = editor.getDefaultModelObjectAsString();
//				String username = user.getUsername();
//				String title = titleField.getDefaultModelObjectAsString();
//				Set<BookTag> tagSet = (Set<BookTag>) tagField
//						.getDefaultModelObject();
//				String subcategoryString = subcategory
//						.getDefaultModelObjectAsString();
//				String categoryString = category
//						.getDefaultModelObjectAsString();
//
//				System.out.println("Tags: " + tagSet.toString());
//
//				Book book = new Book(title, text, user);
//
//				Set<String> bookTagSet = new HashSet<String>();
//
//				// for (String aTag : tagSet) {
//				// //aTag.setBook(book);
//				// bookTagSet.add(aTag);
//				// }
//
//				// book.setTags(bookTagSet);
//				book.setCategory(categoryString);
//				book.setSubCategory(subcategoryString);
				
				/* Insert book */
				Long lastInsertedId = bookService.insertBook(newBook);
				pageParameters.set("book", lastInsertedId);
				System.out.println("Book inserted.");
				System.out.println("Title: " + newBook.getTitle());
				System.out.println("Author: " + newBook.getAuthor());
				System.out.println("Text: " + newBook.getText());
				System.out.println("Category: " + newBook.getCategory());
				System.out.println("SubCategory: " + newBook.getSubCategory());

				user.addBook(newBook);
				
				///* Clear values */
				//editor.setModel(new Model<Book>());
				//titleField.setModel(new Model<Book>());
				target.add(parent);

				dialog.open(target);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
			}
		});

		return form;
	}

	private Dialog loginDialog() {

		Dialog dialog = new Dialog("login_dialog");

		final Form<Object> form = loginForm("login_dialog_form");

		dialog.add(form);

		AjaxDialogButton ok = new AjaxDialogButton("Login") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				setResponsePage(MessagesAdministrationPage.class);

			}
		};

		dialog.setButtons(ok);
		//dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close().render()));

		return dialog;

	}

	private Form<Object> loginForm(String label) {
		final Form<Object> form = new Form<Object>(label);
		final TextField<String> username = new TextField<String>("username",
				new Model<String>(""));
		final PasswordTextField password = new PasswordTextField("password",
				new Model<String>(""));

		username.setRequired(true);

		/*
		 * final Label loginMsg = new Label("loginMsg",
		 * "Nombre de usuario o password incorrecto");
		 * loginMsg.setVisible(false); form.add(loginMsg);
		 */

		// Add a FeedbackPanel for displaying our messages
		// final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
		// feedbackPanel.setOutputMarkupId(true);
		// form.add(feedbackPanel);

		form.add(username);
		form.add(password);

//		form.add(new Button("button1", new Model<String>("")) {
//			private static final long serialVersionUID = 6743737357599494567L;
//
//			@Override
//			public void onSubmit() {
//				String userString = username.getDefaultModelObjectAsString();
//				String passwordString = User.hash(
//						password.getDefaultModelObjectAsString(), "SHA-1");
//
//				username.setModel(new Model<String>(""));
//
//				System.out.println("User: " + userString + " Pass: "
//						+ passwordString);
//
//				User user = userService.getUser(userString);
//
//				if (user != null && user.getPassword().equals(passwordString)) {
//					WiaSession.get().logInUser(user);
//				} else {
//					// TODO Auto-generated method stub
//					/* TERMINAR MENSAJE DE LOGIN INCORRECTO */
//					System.out.println("Login failed!");
//					info("aaaaaaaaaaaa");
//					// loginMsg.setVisible(true);
//				}
//
//				if (!continueToOriginalDestination()) {
//					setResponsePage(HomePage.class);
//				}
//
//				// setResponsePage(BasePage.this);
//
//			}
//		});

		return form;
	}

	@Override
	protected void setPageTitle() {
		String newTitle = "Booktube - New Book";
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}
