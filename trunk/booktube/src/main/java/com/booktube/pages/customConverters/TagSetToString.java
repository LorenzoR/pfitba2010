package com.booktube.pages.customConverters;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.wicket.util.convert.IConverter;

import com.booktube.model.Book;
import com.booktube.model.BookTag;

public class TagSetToString implements IConverter<Set<String>> {

	private static final long serialVersionUID = 1L;
	private final Book book;

	public TagSetToString(Book book) {
		this.book = book;
	}

	public void setLocale(Locale locale) {
	}

	public Locale getLocale() {
		return Locale.getDefault();
	}

	public Set<String> convertToObject(String value, Locale locale) {
		String tags[] = value.split(" ");
		Set<String> tagsSet = new HashSet<String>();

		for (String aTag : tags) {
			//tagsSet.add(new BookTag(aTag, this.book));
			tagsSet.add(aTag);
		}

		System.out.println("newTagSET: " + tagsSet.toString());

		return tagsSet;
	}

	public String convertToString(Set<String> value, Locale locale) {

		Set<String> tagSet = (Set<String>) value;
		if (tagSet.size() > 0) {
			String tags = "";

			for (String aTag : tagSet) {
				tags += aTag + " ";
			}

			return tags.substring(0, tags.length() - 1);
		} else {
			return null;
		}

	}
}
