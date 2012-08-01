package com.booktube.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.ResourceModel;

public class TermsAndConditionsPage extends BasePage {
	private static final long serialVersionUID = 6008085777255792583L;
	public Page backPage;

	public TermsAndConditionsPage() {
		final WebMarkupContainer parent = new WebMarkupContainer("termsAndConditionsContainer");
		parent.setOutputMarkupId(true);
		add(parent);
		
		addBreadcrumb(new BookmarkablePageLink<Object>("link", TermsAndConditionsPage.class), new ResourceModel("termsAndConditions").getObject());
		
		parent.add(new Label("aContent", "Aca va el contenido de la Terms and Conditions Page."));
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		String newTitle = "Booktube - " + new ResourceModel("termsAndConditions").getObject(); 
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}
	
}
