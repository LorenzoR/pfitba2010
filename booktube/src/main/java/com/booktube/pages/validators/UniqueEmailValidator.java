package com.booktube.pages.validators;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.StringValidator;

import com.booktube.service.UserService;

public class UniqueEmailValidator extends StringValidator {

	private static final long serialVersionUID = 1L;
	private String currentEmail;

	@SpringBean
	UserService userService;

	public UniqueEmailValidator() {
	}
	
	public UniqueEmailValidator(String currentEmail) {
		this.currentEmail = currentEmail;
	}

	@Override
	protected void onValidate(IValidatable<String> validatable) {

		if ( StringUtils.isNotEmpty(currentEmail) ) {
			if ( !currentEmail.equals(validatable.getValue()) && userService.emailExists(validatable.getValue())) {
				error(validatable);
			}
		}
		else {
			if (userService.emailExists(validatable.getValue())) {
				error(validatable);
			}
		}

	}

}
