package com.booktube.pages.validators;

import java.util.Date;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

public class CustomDateValidator extends AbstractFormValidator {

	private static final long serialVersionUID = 1L;

	/** form components to be checked. */
	private final FormComponent<Date>[] components;

	/**
	 * Construct.
	 * 
	 * @param formComponent1
	 *            a form component
	 * @param formComponent2
	 *            a form component
	 */
	@SuppressWarnings("unchecked")
	public CustomDateValidator(FormComponent<Date> dateFrom,
			FormComponent<Date> dateTo) {
		components = new FormComponent[] { dateFrom, dateTo };
	}

	public FormComponent<Date>[] getDependentFormComponents() {
		return components;
	}

	public void validate(Form<?> form) {
		// we have a choice to validate the type converted values or the raw
		// input values, we validate the raw input		
		Date dateFrom = (Date) components[0].getConvertedInput();
        Date dateTo = (Date) components[1].getConvertedInput();
 
        if (dateTo != null && dateFrom != null && dateTo.before(dateFrom)){
        	error(components[0]);
        }

	}

}
