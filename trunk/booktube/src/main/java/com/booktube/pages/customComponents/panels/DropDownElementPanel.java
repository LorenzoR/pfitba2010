package com.booktube.pages.customComponents.panels;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;


public class DropDownElementPanel extends Panel {
	private static final long serialVersionUID = -3071539804676533817L;
	
	private String selectedValue; 
	private String tableFieldName;
	private String label;
	
	public DropDownElementPanel(String id, String label, String tableFieldName, List<String>values) {
		super(id);
		selectedValue = null;
		this.tableFieldName = tableFieldName;
		this.label = label;
		add(new Label("label", label));
		DropDownChoice<String> dropCombo = new DropDownChoice<String>("list",new PropertyModel<String>(this,"selectedValue"), values);
		dropCombo.setNullValid(true);
		add(dropCombo);
	}

	public String getTableFieldName(){
		return tableFieldName;
	}
	public String getLabel(){
		return label;
	}
	
	public String getSelectedValue() {
			return selectedValue;
	}
}
