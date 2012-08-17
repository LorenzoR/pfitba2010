package com.booktube.pages.validators;

import org.apache.wicket.markup.html.form.PasswordTextField;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.StringValidator;


public class SamePasswordValidator extends StringValidator{
	private static final long serialVersionUID = 1L;
	private PasswordTextField anotherPasswordField;
	
	
	public SamePasswordValidator(PasswordTextField anotherPasswordField) {
		this.anotherPasswordField = anotherPasswordField;
	}


	@Override
	protected void onValidate(IValidatable<String> validatable) {
		String password = validatable.getValue();		
		String anotherPassword = anotherPasswordField.getValue();
		
		if( password.compareTo(anotherPassword) != 0)
			error(validatable);
		
	}

	
}
