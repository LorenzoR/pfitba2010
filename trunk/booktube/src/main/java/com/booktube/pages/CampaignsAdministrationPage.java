package com.booktube.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

public class CampaignsAdministrationPage extends AdministrationPage{	
	private static final long serialVersionUID = 3572068607555159574L;
	public Page backPage;

	//public AdministrationPage(final PageParameters parameters) {
	public CampaignsAdministrationPage() {
			super();
			final WebMarkupContainer parent = new WebMarkupContainer("campaignsContainer");
			parent.setOutputMarkupId(true);
			add(parent);
			
			parent.add( new Label("pageTitle", "Campaigns Administration Page"));
			
	}

}
