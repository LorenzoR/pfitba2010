package com.booktube.pages.validators;

import java.util.Date;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.DateValidator;

public class BirthdayValidator extends DateValidator {

	private static final long serialVersionUID = 1L;

	@Override
	protected void onValidate(IValidatable<Date> validatable) {
		if ( validatable == null || new Date().before(validatable.getValue()) ) {
			error(validatable);
		}
	}

}
