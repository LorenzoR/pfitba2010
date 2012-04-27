package com.booktube.pages;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.wicket.Page;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.collections.ArrayListStack;
import org.apache.wicket.util.value.ValueMap;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;

import com.booktube.model.Book;
import com.booktube.model.User;
import com.booktube.model.User.Level;
import com.booktube.service.BookService;
import com.booktube.service.UserService;

public class EditWriterPage extends BasePage {

	@SpringBean
	BookService bookService;

	@SpringBean
	UserService userService;
	/** backwards nav page */
	// private final Page backPage;

	private List<User> users = userService.getAllUsers(0, Integer.MAX_VALUE);
	private final User user;
	private final Long userId;

	private static Dialog dialog;

	public EditWriterPage(Long userId, final WebPage backPage) {

		// this.backPage = backPage;
		// Integer bookId = book.getId();
		this.userId = userId;
		user = userService.getUser(userId);

		if (user == null) {
			setResponsePage(HomePage.class);
			return;
		}

		add(new Label("writerId", user.getId().toString()));

		add(editWriterForm(user));

		AjaxDialogButton ok = new AjaxDialogButton("OK") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				// do your cancel logic here
				System.out.println("BUTTON CLICKED!!");
				setResponsePage(UsersAdministrationPage.class);

			}
		};

		dialog = new Dialog("success_dialog");
		dialog.setButtons(ok);
		dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close().render()));

		add(dialog);

		// setResponsePage(backPage);
		// goToLastPage();
		String newTitle = "Booktube - Edit " + user.getUsername();
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

	private Form editWriterForm(final User writer) {
		Form<Object> form = new Form<Object>("editWriterForm");

		final TextField<User> usernameField = new TextField<User>("username",
				new Model(writer.getUsername()));
		form.add(usernameField);

		final TextField<User> firstnameField = new TextField<User>("firstname",
				new Model(writer.getFirstname()));
		form.add(firstnameField);

		final TextField<User> lastnameField = new TextField<User>("lastname",
				new Model(writer.getLastname()));
		form.add(lastnameField);

		final List<String> levels = Arrays.asList(new String[] {
				"Administrador", "Usuario" });
		final String selected = writer.getLevel() == User.Level.ADMIN ? "Administrador"
				: "Usuario";
		final DropDownChoice<String> levelDDC = new DropDownChoice("level",
				new Model(selected), levels);
		form.add(levelDDC);

		/*
		 * final TextField<Book> tagsField = new TextField<Book>("tags", new
		 * Model(book.getTags().toString().substring(1,
		 * book.getTags().toString().length() - 1))); //
		 * titleField.setOutputMarkupId(true); //
		 * titleField.setMarkupId(getId()); form.add(tagsField);
		 * 
		 * final TextArea<String> editor = new TextArea<String>("text", new
		 * Model( book.getText())); editor.setOutputMarkupId(true);
		 * 
		 * // final DropDownChoice ddc2 = new DropDownChoice("usernameList", //
		 * users);
		 * 
		 * final DropDownChoice ddc = new DropDownChoice("usernameList", new
		 * Model(book.getAuthor()), users, new ChoiceRenderer( "username",
		 * "id"));
		 * 
		 * // ValueMap myParameters = new ValueMap(); //
		 * myParameters.put("usernameList", users.get(0)); // form.setModel(new
		 * CompoundPropertyModel(myParameters)); form.add(ddc);
		 * 
		 * form.add(editor);
		 */
		form.add(new AjaxSubmitLink("save") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				/*
				 * // comments.add(new Comment(new //
				 * User(ddc.getDefaultModelObjectAsString()), //
				 * editor.getDefaultModelObjectAsString())); //
				 * editor.setModel(new Model("")); //
				 * target.addComponent(parent); //
				 * target.focusComponent(editor); String username =
				 * username.getDefaultModelObjectAsString(); String name =
				 * name.getDefaultModelObjectAsString(); String lastname =
				 * lastname.getDefaultModelObjectAsString();
				 * 
				 * User user = userService.getUser(username);
				 * book.setText(text); book.setAuthor(user);
				 * book.setTitle(title); // Book newBook = new
				 * Book(book.getId(), title, text, user);
				 * 
				 * // Edit book // bookService.editBook(book.getId(), newBook);
				 * bookService.updateBook(book);
				 * 
				 * System.out.println("Book edited.");
				 * System.out.println("Title: " + book.getTitle());
				 * System.out.println("Author: " + book.getAuthor());
				 * System.out.println("Text: " + book.getText());
				 * 
				 * // Previous page // setResponsePage(backPage);
				 * setResponsePage(BooksPage.class);
				 */

				String username = usernameField.getDefaultModelObjectAsString();
				String firstname = firstnameField
						.getDefaultModelObjectAsString();
				String lastname = lastnameField.getDefaultModelObjectAsString();
				Level level = levelDDC.getDefaultModelObjectAsString()
						.compareTo("Administrador") == 0 ? User.Level.ADMIN
						: User.Level.USER;

				User editUser = new User(userId, username, user.getPassword(),
						lastname, firstname, level);

				userService.updateUser(editUser);

				System.out.println("user editado");
				
				dialog.open(target);
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
