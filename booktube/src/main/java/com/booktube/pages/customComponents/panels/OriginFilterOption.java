package com.booktube.pages.customComponents.panels;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.service.UserService;

public class OriginFilterOption extends FilterOption {
	private static final long serialVersionUID = 7159761887147390608L;
	
	@SpringBean
	UserService userService;
	
	private List<String> allCountriesList;
	private String selectedCountry;
	
	private List<String> allCitiesList;
	private String selectedCity;
	
	
	public OriginFilterOption(String id) {
		super(id);
		
		allCountriesList = userService.getAllCountries();
//		allCountriesList.add(0, FilterOption.listFirstOption);
//		selectedCountry = allCountriesList.get(0);
		selectedCountry = null;
		
		allCitiesList = userService.getAllCities();
//		allCitiesList.add(0, FilterOption.listFirstOption);
//		selectedCity = allCitiesList.get(0);
		selectedCountry = null;
				
		add(new Label("countryLabel", "País"));		
		DropDownChoice<String> countryDropCombo = new DropDownChoice<String>("country", new PropertyModel<String>(this, "selectedCountry"),allCountriesList);
		countryDropCombo.setNullValid(true);
		add(countryDropCombo);
		
		
		add(new Label("cityLabel", "Ciudad"));
		DropDownChoice<String> cityDropCombo = new DropDownChoice<String>("city",  new PropertyModel<String>(this, "selectedCity"), allCitiesList); 
		cityDropCombo.setNullValid(true);
		add( cityDropCombo );
	}

	public String getSelectedCountry() {
//		if( selectedCountry == FilterOption.listFirstOption )
//			return null;
//		else
//			return selectedCountry;
		return selectedCountry;
	}

	public String getSelectedCity() {
//		if( selectedCity == FilterOption.listFirstOption )
//			return null;
//		else
//			return selectedCity;
		return selectedCity;
	}

}
