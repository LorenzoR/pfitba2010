package com.booktube.pages;

import org.apache.wicket.markup.html.WebMarkupContainer;

public class Error404Page extends BasePage {

	private static final long serialVersionUID = 1L;

	public Error404Page () {
		final WebMarkupContainer parent = new WebMarkupContainer("errorContainer");
		parent.setOutputMarkupId(true);
		add(parent);		
	}
	
	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		
	}

}
