package com.booktube.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListView;

public class AboutPage extends BasePage {	
	private static final long serialVersionUID = -9057533012223851372L;
	public Page backPage;

	public AboutPage() {
		final WebMarkupContainer parent = new WebMarkupContainer("aboutContainer");
		parent.setOutputMarkupId(true);
		add(parent);
		
//		List<BookmarkablePageLink> linkList = breadcrumbsModel.getObject();
//		BookmarkablePageLink link = new BookmarkablePageLink<Object>("link", this.getClass());
//		link.add(new Label("textLink", "Acerca de"));
//		linkList.add(link);
//		breadcrumbsModel.setObject(linkList);
		addBreadcrumb(new BookmarkablePageLink<Object>("link", AboutPage.class), "Acerca de");
		//add(createBreadcrumbs());
		
		parent.add(new Label("aContent", "Aca va el contenido de la About Page. acción ACCIÓN"));
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		
	}
	
}
