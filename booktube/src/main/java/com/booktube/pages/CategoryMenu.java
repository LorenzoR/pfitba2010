package com.booktube.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.service.BookService;

public class CategoryMenu extends WebPage {

	@SpringBean
	BookService bookService;

	public CategoryMenu() {

		List<Object> categoriesLi = new ArrayList<Object>();

		List<String> categoryList = bookService.getCategories(0,
				Integer.MAX_VALUE);

		String markup = "";
		
		for (String aCategory : categoryList) {
			//categoriesLi.add(aCategory);
			markup += "<li><a href=\"#\">" + aCategory + "</a>";
			
			List<String> subcategoryList = bookService.getSubcategories(0,
					5, aCategory);
			
			if ( subcategoryList.size() > 0 ) {
				markup += "<ul>";

				for (String aSubcategory : subcategoryList) {
					markup += "<li><a href=\"#\">" + aSubcategory + "</a></li>";
				}
				
				markup += "</ul>";
				
			}
			
			markup += "</li>";

			//categoriesLi.add(subcategoryLi);
		}
		
//		for ( Object anObject : categoriesLi ) {
//			if (anObject instanceof List) {
//				List<String> subcategoryList = (List<String>) anObject;
//				System.out.println("<ul>");
//				markup += "<ul>";
//				for ( String subCategory : subcategoryList ) {
//					System.out.println("<li>" + subCategory.toString() + "</li>");
//					markup += "<li>" + subCategory.toString() + "</li>";
//				}
//				System.out.println("</ul>");
//				markup += "</ul>";
//			}
//			else {
//				System.out.println("<li>" + anObject.toString() + "</li>");
//				markup += "<li>" + anObject.toString() + "</li>";
//			}
//			
//		}
		
		WebMarkupContainer mainUl = new WebMarkupContainer("mainUl");
		add(mainUl);
		
		Label mainUlContent = new Label("mainUlContent", markup);
		mainUlContent.setEscapeModelStrings(false);
		
		mainUl.add(mainUlContent);

//		WebMarkupContainer ulRepeater = new WebMarkupContainer("ulRepeater");
//		add(ulRepeater);
//
//		RepeatingView view = new RepeatingView("repeater");
//		view.add(new Label(view.newChildId(), "hello"));
//		view.add(new Label(view.newChildId(), "goodbye"));
//		view.add(new Label(view.newChildId(), "good morning"));
//		ulRepeater.add(view);

		// construct the panel
		//add(new RecursivePanel("panels", categoriesLi));

	}

}
