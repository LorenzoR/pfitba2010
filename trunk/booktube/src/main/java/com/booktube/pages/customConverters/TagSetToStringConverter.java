package com.booktube.pages.customConverters;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.util.convert.IConverter;

import com.booktube.model.BookTag;

public class TagSetToStringConverter implements IConverter<Set<BookTag>> {

	private static final long serialVersionUID = 1L;

	public TagSetToStringConverter() {
	}

	public Set<BookTag> convertToObject(String value, Locale locale) {
		
		if ( StringUtils.isEmpty(value) ) {
			return null;
		}
		
		String tags[] = value.split(" ");
		Set<BookTag> tagsSet = new HashSet<BookTag>();

		for (String aTag : tags) {
			tagsSet.add(new BookTag(aTag));
		}

		return tagsSet;
	}

	public String convertToString(Set<BookTag> value, Locale locale) {

		Set<BookTag> tagSet = (Set<BookTag>) value;
		if (tagSet.size() > 0) {
			String tags = "";

			for (BookTag aTag : tagSet) {
				tags += aTag.getValue() + " ";
			}

			return tags.substring(0, tags.length() - 1);
		} else {
			return null;
		}

	}
}
