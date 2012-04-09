package com.booktube.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

public class WorksAdministrationPage extends AdministrationPage{
	private static final long serialVersionUID = 7512914721917619810L;
	public Page backPage;

	//public AdministrationPage(final PageParameters parameters) {
	public WorksAdministrationPage() {
			super();
			final WebMarkupContainer parent = new WebMarkupContainer("worksContainer");
			parent.setOutputMarkupId(true);
			add(parent);
			
			parent.add( new Label("pageTitle", "Works Administration Page"));
			
			String newTitle = "Booktube - Works Administration"; 
			super.get("pageTitle").setDefaultModelObject(newTitle);
			
	}

}
