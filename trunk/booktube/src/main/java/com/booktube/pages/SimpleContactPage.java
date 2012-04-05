package com.booktube.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

public class SimpleContactPage extends SiteBasePage {	
	private static final long serialVersionUID = 8580310589800311892L;
	public Page backPage;

	public SimpleContactPage() {
		final WebMarkupContainer parent = new WebMarkupContainer("simpleContactContainer");
		parent.setOutputMarkupId(true);
		add(parent);
		
		parent.add(new Label("aContent", "Aca va el contenido de la Simple Contact Page."));
	}
	
}
