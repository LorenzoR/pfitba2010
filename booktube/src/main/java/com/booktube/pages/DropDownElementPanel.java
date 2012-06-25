package com.booktube.pages;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;


public class DropDownElementPanel extends Panel {
	private static final long serialVersionUID = -3071539804676533817L;
	
	private String selectedGender; 
	
	public DropDownElementPanel(String id, String label, List<String>values) {
		super(id);
		selectedGender = values.get(0);
		add(new Label("label", label));
		add(new DropDownChoice<String>("list",new PropertyModel<String>(this,"selectedGender"), values));
	}

	public String getSelectedGender() {
		return selectedGender;
	}
}
