package com.booktube.pages;


import java.util.List;

import java.util.Map;

import org.apache.wicket.spring.injection.annot.SpringBean;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.general.Dataset;

import com.booktube.pages.utilities.LineReport;
import com.booktube.pages.utilities.Report;
import com.booktube.service.UserService;


public class UsersEvolutionReport extends ReportPage {
	private static final long serialVersionUID = 6051762145219128009L;
	
	private DropDownElementPanel genderDropDownElement;
	
	@SpringBean
	UserService userService;
	private List<String> allGendersList = userService.getAllGenders();
		
	public UsersEvolutionReport(){
		super();
		// No es necesario WebMarkupContainer pues esta subclase no agrega codigo estatico HTML a la superclase(ReportPage)

		//Agrego las opciones de filtrado segun que reporte se quiere generar
		originFilter = new OriginFilterOption("component");
		reportFilter.addFilterOption(originFilter);
		
		ageFilter = new AgeFilterOption("component");
		reportFilter.addFilterOption(ageFilter);
	
		customizedMisc = new MiscFilterOption("component");
		allGendersList.add(0,FilterOption.listFirstOption);
		genderDropDownElement = new DropDownElementPanel("element", "Sex", "gender", allGendersList); 
		customizedMisc.addElement(genderDropDownElement);
		reportFilter.addFilterOption(customizedMisc);
		
		String newTitle = "Booktube - Users Evolution Report"; 
		super.get("pageTitle").setDefaultModelObject(newTitle);		
		
		labels = new String[]{"Evolución de Usuarios en el tiempo", "Año", "Usuarios"};

	}
	
	public Report getReportType(){
		return  new LineReport(getReportData(), labels);
	}
	
	public Class<?> getReportClass(){
		return UsersEvolutionReport.class;
	}

	@Override
	public Dataset getReportData() {
		List<?> data = userService.getUserEvolutionByYear(originFilter, ageFilter, customizedMisc);		  
		final XYSeries serie = new XYSeries("Evolucion de Usuarios en el tiempo");				 
		for(Object object : data){
           Map<?, ?> row = (Map<?, ?>)object;
           serie.add(Double.valueOf((String)row.get("year")),Double.valueOf((String)row.get("total")) ); 
        }
		final XYSeriesCollection collection = new XYSeriesCollection();
	    collection.addSeries(serie);
	    
	    return (Dataset)collection;
	}
	
}
