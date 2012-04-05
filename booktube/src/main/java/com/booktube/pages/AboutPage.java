package com.booktube.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

public class AboutPage extends BasePage {	
	private static final long serialVersionUID = -9057533012223851372L;
	public Page backPage;

	public AboutPage() {
		final WebMarkupContainer parent = new WebMarkupContainer("aboutContainer");
		parent.setOutputMarkupId(true);
		add(parent);
		
		parent.add(new Label("aContent", "Aca va el contenido de la About Page. acción ACCIÓN"));
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		
	}
	
}
