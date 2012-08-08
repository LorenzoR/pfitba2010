package com.booktube.pages.validators;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.StringValidator;

import com.booktube.service.UserService;

public class UniqueEmailValidator extends StringValidator {

	private static final long serialVersionUID = 1L;

	@SpringBean
	UserService userService;

	public UniqueEmailValidator() {
	}

	@Override
	protected void onValidate(IValidatable<String> validatable) {

		if (userService.emailExists(validatable.getValue())) {
			error(validatable);
		}

	}

}
