package com.booktube.pages.customComponents;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

public class CustomTextField extends TextField {

	private static final long serialVersionUID = 1L;

	private final IConverter converter;

	/**
	 * @param id
	 * @param label
	 */
	public CustomTextField(String id, IModel labelModel, IConverter converter) {
		super(id, labelModel);
		this.converter = converter;
	}

	@Override
	public IConverter getConverter(Class type) {
		return this.converter;
	}

}
