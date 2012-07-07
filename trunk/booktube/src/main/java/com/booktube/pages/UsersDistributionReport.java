package com.booktube.pages;

import java.util.List;


import java.util.Map;

import org.apache.wicket.spring.injection.annot.SpringBean;

import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;

import com.booktube.pages.utilities.PieReport;
import com.booktube.pages.utilities.Report;
import com.booktube.service.UserService;

public class UsersDistributionReport extends ReportPage {
	private static final long serialVersionUID = 254209194148266371L;
	
	private DropDownElementPanel genderDropDownElement;
	private DropDownElementPanel yearsDropDownElement;
	
	
	@SpringBean
	UserService userService;
	private List<String> allGendersList = userService.getAllGenders();
	private List<String> allYearsList = userService.getAllRegistrationYears();
		
	public UsersDistributionReport(){
		super();
		// No es necesario WebMarkupContainer pues esta subclase no agrega codigo estatico HTML a la superclase(ReportPage)

		//Agrego las opciones de filtrado segun que reporte se quiere generar
		ageFilter = new AgeFilterOption("component");
		reportFilter.addFilterOption(ageFilter);
	
		customizedMisc = new MiscFilterOption("component");
		
		allGendersList.add(0,FilterOption.listFirstOption);
		genderDropDownElement = new DropDownElementPanel("element", "Sexo", "gender", allGendersList);			
		customizedMisc.addElement(genderDropDownElement);			
		allYearsList.add(0,FilterOption.listFirstOption);
		yearsDropDownElement = new DropDownElementPanel("element", "Año", "registration_date", allYearsList);			
		customizedMisc.addElement(yearsDropDownElement);
		
		reportFilter.addFilterOption(customizedMisc);
		
		String newTitle = "Booktube - Users Distribution Report"; 
		super.get("pageTitle").setDefaultModelObject(newTitle);	
		
		
		labels = new String[]{"Distribución de Usuarios por país"};
		
	}
	
	@Override
	public Dataset getReportData() {
		List<?> data = userService.getUserDistributionByCountry(ageFilter, customizedMisc);		
		DefaultPieDataset result = new DefaultPieDataset();
		for(Object object : data){
           Map<?, ?> row = (Map<?, ?>)object;
		 	result.setValue((String)row.get("country"), (Number)row.get("total"));		           
        }	
		return (Dataset)result;

	}

	@Override
	public Report getReportType() {
		return new PieReport(getReportData(), labels);
	}

	@Override
	public Class<?> getReportClass() {
		return UsersEvolutionReport.class;
	}

}
