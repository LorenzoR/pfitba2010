package com.booktube.pages.validators;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.StringValidator;

import com.booktube.service.UserService;

public class UniqueUsernameValidator extends StringValidator {

	private static final long serialVersionUID = 1L;
	private String currentUsername;

	@SpringBean
	UserService userService;

	public UniqueUsernameValidator() {
	}
	
	public UniqueUsernameValidator(String currentUsername) {
		this.currentUsername = currentUsername;
	}

	@Override
	protected void onValidate(IValidatable<String> validatable) {

		if ( StringUtils.isNotEmpty(currentUsername) ) {
			if ( !currentUsername.equals(validatable.getValue()) && userService.usernameExists(validatable.getValue())) {
				error(validatable);
			}
		}
		else {
			if (userService.usernameExists(validatable.getValue())) {
				error(validatable);
			}
		}

	}

}
