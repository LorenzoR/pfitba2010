package com.booktube.pages;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.service.BookService;

public class CategoryMenu extends WebPage {

	@SpringBean
	BookService bookService;

	public CategoryMenu() {

		List<String> categoryList = bookService.getCategories(0,
				Integer.MAX_VALUE);

		String markup = "";

		for (String aCategory : categoryList) {
			if (aCategory != null) {
				markup += "<li><a href=\"books?category=" + aCategory + "\">"
						+ aCategory + "</a>";

				List<String> subcategoryList = bookService.getSubcategories(0,
						5, aCategory);

				if (subcategoryList.size() > 0) {
					String subCategoryLi = "";
					
					for (String aSubcategory : subcategoryList) {
						
						if (aSubcategory != null) {
							subCategoryLi += "<li><a href=\"books?subcategory="
									+ aSubcategory + "\">" + aSubcategory
									+ "</a></li>";
						}
					}
					
					if ( StringUtils.isNotBlank(subCategoryLi) ) {
						markup += "<ul>" + subCategoryLi + "</ul>";
					}

				}

				markup += "</li>";
			}

		}

		WebMarkupContainer mainUl = new WebMarkupContainer("mainUl");
		add(mainUl);

		Label mainUlContent = new Label("mainUlContent", markup);
		mainUlContent.setEscapeModelStrings(false);

		mainUl.add(mainUlContent);

	}

}
