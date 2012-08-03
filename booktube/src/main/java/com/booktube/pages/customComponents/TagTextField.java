package com.booktube.pages.customComponents;

import java.util.Set;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.convert.IConverter;

import com.booktube.model.BookTag;
import com.booktube.pages.customConverters.TagSetToStringConverter;

public class TagTextField extends TextField<String> {

	private static final long serialVersionUID = 1L;

	private final IConverter<Set<BookTag>> converter;

	public TagTextField(String id) {
		super(id);
		this.converter = new TagSetToStringConverter();
	}
	
//	public CustomTextField(String id, IModel labelModel, IConverter converter) {
//		super(id, labelModel);
//		this.converter = converter;
//	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public IConverter<Set<BookTag>> getConverter(Class type) {
		return this.converter;
	}

}
