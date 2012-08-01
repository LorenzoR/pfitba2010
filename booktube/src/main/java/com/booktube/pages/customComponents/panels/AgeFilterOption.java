package com.booktube.pages.customComponents.panels;

import java.util.List;


import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.service.UserService;

public class AgeFilterOption extends FilterOption {
	private static final long serialVersionUID = 2291617367903205980L;
	
	@SpringBean
	UserService userService;
	
	private List<String> allAgesList = (List<String>)userService.getAllAges();
//	private String selectedMinAge = allAgesList.get(0);
//	private String selectedMaxAge = selectedMinAge;
	
	private String selectedMinAge;
	private String selectedMaxAge;
	public AgeFilterOption(String id) {		
		super(id);
	
		allAgesList.add(0, listFirstOption);
		selectedMinAge = allAgesList.get(0);
		selectedMaxAge = selectedMinAge;
		
		add(new Label("minLabel", "Min"));
		add(new DropDownChoice<String>("minAge", new PropertyModel<String>(this,"selectedMinAge"),allAgesList));
		add(new Label("maxLabel", "Max"));
		add(new DropDownChoice<String>("maxAge", new PropertyModel<String>(this,"selectedMaxAge"),allAgesList));
	}

	public String getSelectedMinAge() {
		return selectedMinAge;
	}
	public String getSelectedMaxAge() {
		return selectedMaxAge;
	}

}