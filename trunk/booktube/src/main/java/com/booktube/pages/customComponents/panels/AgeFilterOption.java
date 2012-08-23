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
	
//		allAgesList.add(0, FilterOption.listFirstOption);
//		selectedMinAge = allAgesList.get(0);
//		selectedMaxAge = selectedMinAge;
		selectedMaxAge = null;
		selectedMinAge = null;
		
		add(new Label("minLabel", "Min"));
		DropDownChoice<String> minAgeDropCombo = new DropDownChoice<String>("minAge", new PropertyModel<String>(this,"selectedMinAge"),allAgesList);
		minAgeDropCombo.setNullValid(true);
		add(minAgeDropCombo);
		
		add(new Label("maxLabel", "Max"));
		DropDownChoice<String> maxAgeDropCombo = new DropDownChoice<String>("maxAge", new PropertyModel<String>(this,"selectedMaxAge"),allAgesList);
		maxAgeDropCombo.setNullValid(true);
		add(maxAgeDropCombo);
	}

	public String getSelectedMinAge() {
//		if( selectedMinAge == FilterOption.listFirstOption )
//			return null;
//		else
//			return selectedMinAge;
		return selectedMinAge;
	}
	public String getSelectedMaxAge() {
//		if( selectedMaxAge == FilterOption.listFirstOption )
//			return null;
//		else
//			return selectedMaxAge;
		return selectedMaxAge;
	}

}
