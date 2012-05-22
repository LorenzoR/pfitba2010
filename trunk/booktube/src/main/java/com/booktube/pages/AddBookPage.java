package com.booktube.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
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

//		Label registerMessage = new Label("registerMessage",
//				"Debe registrarse para poder publicar.");
//		parent.add(registerMessage);
		
		final Dialog loginDialog = loginDialog();
				
		parent.add(loginDialog);
		
		WebMarkupContainer registerMessage = new WebMarkupContainer("registerMessage");
		registerMessage.add(new BookmarkablePageLink<String>("registerLink", RegisterPage.class));
		AjaxLink loginLink = new AjaxLink("loginLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				loginDialog.open(target);
			}
			
		};
		registerMessage.add(loginLink);
		parent.add(registerMessage);

		Form<?> form = addBookForm(parent);
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

	private Form<?> addBookForm(final WebMarkupContainer parent) {
		Form<?> form = new Form("form");

		final TextField titleField = new TextField("title", new Model(""));

		form.add(titleField);

		final TextField tagField = new TextField("tag", new Model(""));

		form.add(tagField);

		final TextArea editor = new TextArea("textArea");
		editor.setOutputMarkupId(true);

		final AutoCompleteTextField<String> category = new AutoCompleteTextField<String>(
				"category", new Model<String>("")) {

			private static final long serialVersionUID = 2977239698122401133L;

			@Override
			protected Iterator<String> getChoices(String input) {
				if (Strings.isEmpty(input)) {
					List<String> emptyList = Collections.emptyList();
					return emptyList.iterator();
				}

				List<String> choices = new ArrayList<String>(10);

				List<String> categories = bookService.getCategories(0, Integer.MAX_VALUE);

				for (final String aCategory : categories ) {

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

			private static final long serialVersionUID = 2977239698122401133L;

			@Override
			protected Iterator<String> getChoices(String input) {
				if (Strings.isEmpty(input)) {
					List<String> emptyList = Collections.emptyList();
					return emptyList.iterator();
				}

				List<String> choices = new ArrayList<String>(10);

				List<String> subcategories = bookService.getSubcategories(0, Integer.MAX_VALUE);

				for (final String aSubcategory : subcategories ) {

					if (aSubcategory.toUpperCase().startsWith(input.toUpperCase())) {
						choices.add(aSubcategory);
						if (choices.size() == 10) {
							break;
						}
					}
				}

				return choices.iterator();
			}
		};
		
		form.add(subcategory);

		ValueMap myParameters = new ValueMap();

		form.setModel(new CompoundPropertyModel(myParameters));

		form.add(editor);
		form.add(new AjaxSubmitLink("save") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// comments.add(new Comment(new
				// User(ddc.getDefaultModelObjectAsString()),
				// editor.getDefaultModelObjectAsString()));
				// editor.setModel(new Model(""));
				// target.addComponent(parent);
				// target.focusComponent(editor);
				// System.out.println("ACA 1");
				String text = editor.getDefaultModelObjectAsString();
				String username = user.getUsername();
				String title = titleField.getDefaultModelObjectAsString();
				String tagString = tagField.getDefaultModelObjectAsString();
				String tags[] = tagString.split(" ");
				System.out.println("Tags: " + tags.toString());

				// User user = userService.getUser(username);
				Book book = new Book(title, text, user);

				Set<BookTag> tagsSet = new HashSet<BookTag>();

				for (String tag : tags) {
					System.out.println("Tag: " + tag);
					// book.addTag(tag);
					tagsSet.add(new BookTag(tag, book));
				}

				book.setTags(tagsSet);
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
				editor.setModel(new Model(""));
				titleField.setModel(new Model(""));
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
		
		dialog.add(new Label("username_text", "Username: "));
		dialog.add(new Label("password_text", "Password: "));
		
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

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		String newTitle = "Booktube - New Book";
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}
