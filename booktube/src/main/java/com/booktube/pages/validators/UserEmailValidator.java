package com.booktube.pages.validators;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.StringValidator;

import com.booktube.model.User;
import com.booktube.service.UserService;

public class UserEmailValidator extends StringValidator {
	private static final long serialVersionUID = 1L;
	private RequiredTextField<User> emailField;
	
	@SpringBean
	UserService userService;

	public UserEmailValidator(RequiredTextField<User> emailField) {
		this.emailField = emailField;		
	}

	@Override
	protected void onValidate(IValidatable<String> validatable) {

		User user = userService.getUser(validatable.getValue());		
		String submittedEmail = emailField.getValue();
		
		if( submittedEmail.compareToIgnoreCase(user.getEmail()) != 0 ){		
			error(validatable);
		}

	}
}
