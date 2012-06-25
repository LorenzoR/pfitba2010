package com.booktube.pages;

import java.util.Arrays;
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
	private static final List<String> CITIES = Arrays.asList(new String[] {"Buenos Aires", "Montevideo", "Cordoba", "Asunción", "Rosario" });
	
	private List<String> allCountriesList = userService.getAllCountries();
	private String selectedCountry = allCountriesList.get(0);
	
	//private List<String> allCitiesList = userService.getAllCities();
	//private String selectedCity = allCitiesList.get(0);
	private String selectedCity = CITIES.get(0);
	
	
	public OriginFilterOption(String id) {
		super(id);
		add(new Label("countryLabel", "País"));				
		add(new DropDownChoice<String>("country", new PropertyModel<String>(this, "selectedCountry"),allCountriesList));
		
		
		add(new Label("cityLabel", "Ciudad"));
		//add(new DropDownChoice<String>("city",  new PropertyModel<String>(this, "selectedCity"), allCitiesList) );
		add(new DropDownChoice<String>("city", new PropertyModel<String>(this, "selectedCity"), CITIES) );
	}


	public String getSelectedCountry() {
		return selectedCountry;
	}


	public String getSelectedCity() {
		return selectedCity;
	}

}
