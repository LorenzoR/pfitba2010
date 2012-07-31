package com.booktube.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListView;

public class HelpPage extends BasePage {	
	private static final long serialVersionUID = -5126840475185084538L;
	public Page backPage;

	public HelpPage() {
		final WebMarkupContainer parent = new WebMarkupContainer("helpContainer");
		parent.setOutputMarkupId(true);
		add(parent);
		
		addBreadcrumb(new BookmarkablePageLink<Object>("link", HelpPage.class), "Ayuda");
		
		parent.add(new Label("aContent", "Aca va el contenido de la Help Page."));
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		String newTitle = "Booktube - Help Page"; 
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}
	
}
