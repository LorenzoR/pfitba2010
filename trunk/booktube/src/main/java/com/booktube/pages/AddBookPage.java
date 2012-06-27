package com.booktube.pages;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.value.ValueMap;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;

import com.booktube.WiaSession;
import com.booktube.model.Book;
import com.booktube.model.BookTag;
import com.booktube.model.User;
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
	private static Long lastInsertedId = null;

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

		AjaxDialogButton ok = new AjaxDialogButton("OK") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				// do your cancel logic here
				System.out.println("BUTTON CLICKED!!");
				setResponsePage(HomePage.class);
				if (lastInsertedId != null) {
					PageParameters pageParameters = new PageParameters();
					pageParameters.set("book", lastInsertedId);
					setResponsePage(ShowBookPage.class, pageParameters);
				}

			}
		};

		dialog = new Dialog("success_dialog");
		dialog.setButtons(ok);
		dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close().render()));

		parent.add(dialog);

	}

	private Form<Book> addBookForm(final WebMarkupContainer parent) {
		Form<Book> form = new Form<Book>("form");

		final Book newBook = new Book();

		CompoundPropertyModel<Book> model = new CompoundPropertyModel<Book>(
				newBook);

		form.setDefaultModel(model);
		
		final TextField<Book> titleField = new TextField<Book>("title");

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
				new SetToStringConverter());

		form.add(tagField);

		final TextArea<Book> editor = new TextArea<Book>("text");
		editor.setOutputMarkupId(true);

		final AutoCompleteTextField<String> category = new AutoCompleteTextField<String>(
				"category", new Model("")) {

			private static final long serialVersionUID = 1L;

			@Override
			protected Iterator<String> getChoices(String input) {
				if (Strings.isEmpty(input)) {
					List<String> emptyList = Collections.emptyList();
					return emptyList.iterator();
				}

				List<String> choices = new ArrayList<String>(10);

				List<String> categories = bookService.getCategories(0,
						Integer.MAX_VALUE);

				for (final String aCategory : categories) {

					if (aCategory.toUpperCase().startsWith(input.toUpperCase())) {
						choices.add(aCategory);
						if (choices.size() == 10) {
							break;
						}
					}
				}

				return choices.iterator();
			}
		};

		form.add(category);

		final AutoCompleteTextField<String> subcategory = new AutoCompleteTextField<String>(
				"subcategory", new Model<String>("")) {

			private static final long serialVersionUID = 1L;

			@Override
			protected Iterator<String> getChoices(String input) {
				if (Strings.isEmpty(input)) {
					List<String> emptyList = Collections.emptyList();
					return emptyList.iterator();
				}

				List<String> choices = new ArrayList<String>(10);

				List<String> subcategories = bookService.getSubcategories(0,
						Integer.MAX_VALUE, null);

				for (final String aSubcategory : subcategories) {
					if (aSubcategory != null) {
						if (aSubcategory.toUpperCase().startsWith(
								input.toUpperCase())) {
							choices.add(aSubcategory);
							if (choices.size() == 10) {
								break;
							}
						}
					}
				}

				return choices.iterator();
			}
		};

		form.add(subcategory);

		form.add(editor);
		form.add(new AjaxSubmitLink("save") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				System.out.println("--- TITULO: " + newBook.getTitle());
				System.out.println("--- CATEGORY: " + newBook.getCategory());
				String text = editor.getDefaultModelObjectAsString();
				String username = user.getUsername();
				String title = titleField.getDefaultModelObjectAsString();
				Set<String> tagSet = (Set<String>) tagField
						.getDefaultModelObject();

				System.out.println("Tags: " + tagSet.toString());

				Book book = new Book(title, text, user);

				Set<BookTag> bookTagSet = new HashSet<BookTag>();

				for (String aTag : tagSet) {
					bookTagSet.add(new BookTag(aTag, book));
				}

				book.setTags(bookTagSet);
				book.setCategory("categoria");
				book.setSubCategory("subcategoria");

				/* Insert book */
				lastInsertedId = bookService.insertBook(book);
				System.out.println("Book inserted.");
				System.out.println("Title: " + title);
				System.out.println("Author: " + username);
				System.out.println("Text: " + text);
				System.out.println("Category: " + book.getCategory());
				System.out.println("SubCategory: " + book.getSubCategory());

				/* Clear values */
				editor.setModel(new Model<Book>());
				titleField.setModel(new Model<Book>());
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
		dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close().render()));

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

		form.add(new Button("button1", new Model<String>("")) {
			private static final long serialVersionUID = 6743737357599494567L;

			@Override
			public void onSubmit() {
				String userString = username.getDefaultModelObjectAsString();
				String passwordString = User.hash(
						password.getDefaultModelObjectAsString(), "SHA-1");

				username.setModel(new Model<String>(""));

				System.out.println("User: " + userString + " Pass: "
						+ passwordString);

				User user = userService.getUser(userString);

				if (user != null && user.getPassword().equals(passwordString)) {
					WiaSession.get().logInUser(user);
				} else {
					// TODO Auto-generated method stub
					/* TERMINAR MENSAJE DE LOGIN INCORRECTO */
					System.out.println("Login failed!");
					info("aaaaaaaaaaaa");
					// loginMsg.setVisible(true);
				}

				if (!continueToOriginalDestination()) {
					setResponsePage(HomePage.class);
				}

				// setResponsePage(BasePage.this);

			}
		});

		return form;
	}

	@Override
	protected void setPageTitle() {
		String newTitle = "Booktube - New Book";
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

	public class CustomTextField extends TextField {

		private final IConverter converter;

		/**
		 * @param id
		 * @param label
		 */
		public CustomTextField(String id, IModel labelModel,
				IConverter converter) {
			super(id, labelModel);
			this.converter = converter;
		}

		@Override
		public IConverter getConverter(Class type) {

			return this.converter;
		}

	}

	public class SetToStringConverter implements IConverter {

		private static final long serialVersionUID = 1L;

		public SetToStringConverter() {
		}

		public void setLocale(Locale locale) {
		}

		public Locale getLocale() {
			return Locale.getDefault();
		}

		public Object convertToObject(String value, Locale locale) {
			String tags[] = value.split(" ");
			Set<String> tagsSet = new HashSet<String>();

			for (String tag : tags) {
				tagsSet.add(tag);
			}

			return tagsSet;
		}

		public String convertToString(Object value, Locale locale) {
			System.out.println("convertToString");
			// TODO Auto-generated method stub
			return null;
		}
	}

}
