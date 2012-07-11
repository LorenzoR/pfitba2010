package com.booktube.pages.customComponents;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

public class PanelListView extends ListView<Panel> {
	private static final long serialVersionUID = 2086914676227768570L;
	
	public PanelListView(String id, List<? extends Panel> list) {		
		super(id, list);
		setReuseItems(true);
	}

	@Override
	protected void populateItem(ListItem<Panel> item) {
		item.add( (Component)item.getModelObject() );
		
	}

}
