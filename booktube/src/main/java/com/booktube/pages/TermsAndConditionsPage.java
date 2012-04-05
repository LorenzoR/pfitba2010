package com.booktube.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

public class TermsAndConditionsPage extends SiteBasePage {
	private static final long serialVersionUID = 6008085777255792583L;
	public Page backPage;

	public TermsAndConditionsPage() {
		final WebMarkupContainer parent = new WebMarkupContainer("termsAndConditionsContainer");
		parent.setOutputMarkupId(true);
		add(parent);
		
		parent.add(new Label("aContent", "Aca va el contenido de la Terms and Conditions Page."));
	}
	
}
