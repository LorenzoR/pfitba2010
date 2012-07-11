package com.booktube.pages.customComponents.panels;


import org.apache.wicket.markup.html.panel.Panel;

import com.booktube.pages.CampaignsAdministrationPage;
import com.booktube.pages.MessagesAdministrationPage;
import com.booktube.pages.ReportsAdministrationPage;
import com.booktube.pages.UsersAdministrationPage;
import com.booktube.pages.WorksAdministrationPage;
import com.booktube.pages.customComponents.MenuLink;

public class LeftMenuPanel extends Panel{	
	private static final long serialVersionUID = 6320824482508912135L;

	public LeftMenuPanel(String id){
		super(id);
		
		
		//add(new Label("panelTitle","Example PanelZZZ"));
		add( new MenuLink("reportsAdministrationLink", ReportsAdministrationPage.class));
		add( new MenuLink("campaignsAdministrationLink", CampaignsAdministrationPage.class));
		add( new MenuLink("messagesAdministrationLink", MessagesAdministrationPage.class));
		add( new MenuLink("worksAdministrationLink", WorksAdministrationPage.class));
		add( new MenuLink("usersAdministrationLink", UsersAdministrationPage.class));
		
		//campañas
		
	}
}
