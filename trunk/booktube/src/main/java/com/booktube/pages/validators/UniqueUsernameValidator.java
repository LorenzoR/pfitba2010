package com.booktube.pages.validators;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.StringValidator;

import com.booktube.service.UserService;

public class UniqueUsernameValidator extends StringValidator {

	private static final long serialVersionUID = 1L;

	@SpringBean
	UserService userService;

	@Override
	protected void onValidate(IValidatable<String> validatable) {

		boolean userExists = userService.usernameExists(validatable.getValue());

		if (userExists) {
			error(validatable);
		}

	}

}
