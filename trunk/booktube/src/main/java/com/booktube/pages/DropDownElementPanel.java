package com.booktube.pages;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;


public class DropDownElementPanel extends Panel {
	private static final long serialVersionUID = -3071539804676533817L;
	
	private String selectedValue; 
	private String tableFieldName;
	
	public DropDownElementPanel(String id, String label, String tableFieldName, List<String>values) {
		super(id);
		selectedValue = values.get(0);
		this.tableFieldName = tableFieldName;
		add(new Label("label", label));
		add(new DropDownChoice<String>("list",new PropertyModel<String>(this,"selectedValue"), values));
	}

	public String getTableFieldName(){
		return tableFieldName;
	}
	public String getSelectedValue() {
		return selectedValue;
	}
}
