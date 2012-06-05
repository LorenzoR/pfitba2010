package com.booktube.pages;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;



public class UsersEvolutionReport extends ReportPage {
	private static final long serialVersionUID = 6051762145219128009L;
	
	private static final List<String> COUNTRIES = Arrays.asList(new String[] {"Argentina", "Uruguay", "Paraguay" });
	private static final List<String> CITIES = Arrays.asList(new String[] {"Buenos Aires", "Montevideo", "Cordoba", "Asunción", "Rosario" });
	private static final List<String> SEXES = Arrays.asList(new String[] {"Masculino", "Femenino" });
	private static final List<String> MIN_AGES = Arrays.asList(new String[] {"6", "7", "8", "9", "10" });
	private static final List<String> MAX_AGES = Arrays.asList(new String[] {"6", "7", "8", "9", "10" });
	private static final List<String> YEARS = Arrays.asList(new String[] {"2009", "2010", "2011", "2012" });
	
	public UsersEvolutionReport(){
		super();
		final WebMarkupContainer parent = new WebMarkupContainer("usersEvolutionReportContainer");
		parent.setOutputMarkupId(true);
		add(parent);
		
		String newTitle = "Booktube - Users Evolution Report"; 
		super.get("pageTitle").setDefaultModelObject(newTitle);		
		
		parent.add(new Label("countryLabel", "País"));
		parent.add(new DropDownChoice<Object>("country", COUNTRIES));
		
		parent.add(new Label("cityLabel", "Ciudad"));
		parent.add(new DropDownChoice<Object>("city", CITIES));
		
		parent.add(new Label("sexLabel", "Sexo"));
		parent.add(new DropDownChoice<Object>("sex", SEXES));
		
		parent.add(new Label("minLabel", "Min"));
		parent.add(new DropDownChoice<Object>("minAge", MIN_AGES));
		
		parent.add(new Label("maxLabel", "Max"));
		parent.add(new DropDownChoice<Object>("maxAge", MAX_AGES));
		
		parent.add(new Label("yearLabel", "Año"));
		parent.add(new DropDownChoice<Object>("year", YEARS));
		
	}
	
}
