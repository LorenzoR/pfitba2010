package com.booktube.pages.customConverters;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.wicket.util.convert.IConverter;

import com.booktube.model.BookTag;

public class TagSetToString implements IConverter<Set<BookTag>> {

	private static final long serialVersionUID = 1L;

	public TagSetToString() {
	}

	public void setLocale(Locale locale) {
	}

	public Locale getLocale() {
		return Locale.getDefault();
	}

	public Set<BookTag> convertToObject(String value, Locale locale) {
		String tags[] = value.split(" ");
		Set<BookTag> tagsSet = new HashSet<BookTag>();

		for (String aTag : tags) {
			//tagsSet.add(new BookTag(aTag, this.book));
			tagsSet.add(new BookTag(aTag));
		}

		System.out.println("newTagSET: " + tagsSet.toString());

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
