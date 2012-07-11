package com.booktube.pages;

import java.util.ArrayList;
import java.util.List;

import com.booktube.pages.customComponents.DropDownElementPanel;

public class MiscFilterOption extends FilterOption {
	private static final long serialVersionUID = 8701603490904340060L;	
	private List<DropDownElementPanel> elements;
	
	public MiscFilterOption(String id) {
		super(id);
		elements = new ArrayList<DropDownElementPanel>();		
		add( new PanelListView("elements", elements));		
	}
	
	public void addElement( DropDownElementPanel element ){
		elements.add(element);
	}
	public List<DropDownElementPanel> getElements(){
		return elements;
	}

	

}
