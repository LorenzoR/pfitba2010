package com.booktube;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

public class HomePage extends BasePage {


	public HomePage() {

		final WebMarkupContainer parent = new WebMarkupContainer("homePage");
		parent.setOutputMarkupId(true);
		add(parent);
		
		parent.add(new Label("content", "Aca va el contenido de la Home Page."));
		
		
	}
	
}
