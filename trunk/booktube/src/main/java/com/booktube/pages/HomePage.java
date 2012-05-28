package com.booktube.pages;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wicketstuff.facebook.plugins.LikeBox;

public class HomePage extends BasePage {


	public HomePage() {

		final WebMarkupContainer parent = new WebMarkupContainer("homePage");
		parent.setOutputMarkupId(true);
		add(parent);
		
		parent.add(new Label("content", "Aca va el contenido de la Home Page."));
		
		final LikeBox likeBox = new LikeBox("likeBox", Model.of("https://www.facebook.com/booktube123"));
		parent.add(likeBox);
		
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
	}
	
}
