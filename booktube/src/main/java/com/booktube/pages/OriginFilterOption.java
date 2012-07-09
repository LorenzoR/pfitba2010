package com.booktube.pages;

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
		allCountriesList.add(0, listFirstOption);
		selectedCountry = allCountriesList.get(0);
		
		allCitiesList = userService.getAllCities();
		allCitiesList.add(0, listFirstOption);
		selectedCity = allCitiesList.get(0);
				
		add(new Label("countryLabel", "País"));				
		add(new DropDownChoice<String>("country", new PropertyModel<String>(this, "selectedCountry"),allCountriesList));
		
		
		add(new Label("cityLabel", "Ciudad"));
		add(new DropDownChoice<String>("city",  new PropertyModel<String>(this, "selectedCity"), allCitiesList) );
	}

	public String getSelectedCountry() {
		return selectedCountry;
	}

	public String getSelectedCity() {
		return selectedCity;
	}

}
