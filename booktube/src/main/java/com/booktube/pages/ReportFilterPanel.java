package com.booktube.pages;


import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;

public class ReportFilterPanel extends Panel {
	private static final long serialVersionUID = 2581370639576897790L;
	List<FilterOption> components;
	
	public ReportFilterPanel(String id) {
		super(id);
		
		//add( new OriginFilterOption("originOption"));
		//add( new AgeFilterOption("ageOption"));
		//add( new MiscFilterOption("miscOption"));
		
		components = new ArrayList<FilterOption>();
		//components.add( new OriginFilterOption("component"));
		//components.add( new AgeFilterOption("component"));
		//components.add( new MiscFilterOption("component"));
		add( new PanelListView("components", components));
		
	}
	public void addFilterOption( FilterOption option){
		components.add(option);
	}

	

}
