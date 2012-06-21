package com.booktube.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.service.BookService;

public class MenuExample extends WebPage {

	@SpringBean
	BookService bookService;
	
	public MenuExample() {

		// create a list with sublists
		List<Object> l1 = new ArrayList<Object>();
		l1.add("test 1.1");
		l1.add("test 1.2");
		List<Object> l2 = new ArrayList<Object>();
		l2.add("test 2.1");
		l2.add("test 2.2");
		l2.add("test 2.3");
		List<String> l3 = new ArrayList<String>();
		l3.add("test 3.1");
		l2.add(l3);
		l2.add("test 2.4");
		l1.add(l2);
		l1.add("test 1.3");
		
		List<Object> categoriesLi = new ArrayList<Object>();
		
		List<String> categoryList = bookService.getCategories(0, Integer.MAX_VALUE);
		
		for ( String aCategory : categoryList ) {
			categoriesLi.add(aCategory);
			List<String> subcategoryList = bookService.getSubcategories(0, Integer.MAX_VALUE, aCategory);
			
			List<String> subcategoryLi = new ArrayList<String>();
			
			for ( String aSubcategory : subcategoryList ) {
				subcategoryLi.add(aSubcategory);
			}
			
			categoriesLi.add(subcategoryLi);
		}
		
		// construct the panel
		//add(new RecursivePanel("panels", categoriesLi));
		
		String url = RequestCycle.get().getUrlRenderer().renderFullUrl(
				   Url.parse(urlFor(CategoryMenu.class, null).toString()));

		final String[] splitStr = url.split("/");
		
		System.out.println(splitStr[0]);
		System.out.println(splitStr[1]);
		System.out.println(splitStr[2]);
		System.out.println(splitStr[3]);
		
		StringBuffer buf = new StringBuffer(url);
		buf.insert(url.indexOf("/com.") + 1, "wicket/bookmarkable/");
		
		url = buf.toString();
		
		System.out.println(bookService.getSubcategories(0, 1000, "category1"));
		
		final String newUrl = RequestCycle.get().getUrlRenderer().renderFullUrl(
				   Url.parse(urlFor(CategoryMenu.class, null).toString()));
		
		Label myScript = new Label("myScript", "url = '"
				+ newUrl + "';");
		myScript.setEscapeModelStrings(false);
		add(myScript);

		// List<String> categories = new ArrayList<String>();
		// categories.add("Categoria 1");
		// categories.add("Categoria 2");
		// categories.add("Categoria 3");
		// categories.add("Categoria 4");
		// categories.add("Categoria 5");
		// categories.add("Categoria 6");
		//
		// List<String> subcategories = new ArrayList<String>();
		// subcategories.add("Subcategoria 1");
		// subcategories.add("Subcategoria 2");
		// subcategories.add("Subcategoria 3");
		// subcategories.add("Subcategoria 4");
		// subcategories.add("Subcategoria 5");
		// subcategories.add("Subcategoria 6");
		// subcategories.add("Subcategoria 7");
		// subcategories.add("Subcategoria 8");
		//
		// WebMarkupContainer ulMain = new WebMarkupContainer("ulMain");
		//
		// RepeatingView view = new RepeatingView("repeater");
		//
		// for ( String aCategory : categories ) {
		// view.add(new Label(view.newChildId(), aCategory));
		// }
		//
		// ulMain.add(view);
		// add(ulMain);

	}

}
