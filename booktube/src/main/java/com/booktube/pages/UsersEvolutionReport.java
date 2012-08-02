package com.booktube.pages;


import java.util.List;

import java.util.Map;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.general.Dataset;

import com.booktube.pages.utilities.LineReport;
import com.booktube.pages.utilities.Report;
import com.booktube.service.UserService;


public class UsersEvolutionReport extends ReportPage {
	private static final long serialVersionUID = 6051762145219128009L;
	
	@SpringBean
	UserService userService;
	
	private List<String> allGendersList = userService.getAllGenders();
		
	public UsersEvolutionReport(){
		super();
		// Agrego las opciones de filtrado segun que reporte se quiere generar
		addOriginFilterOption();
		addAgeFilterOption();
		addGenderFilterOption(allGendersList);		
				
		addBreadcrumb(new BookmarkablePageLink<Object>("link", ReportsAdministrationPage.class), new ResourceModel("reportsPageTitle").getObject());
		addBreadcrumb(new BookmarkablePageLink<Object>("link", UsersEvolutionReport.class), new ResourceModel("usersEvolutionReport").getObject());
		
		// Especifico Titulo ( y etiquetas, si corresponde)
		labels = new String[]{new ResourceModel("usersEvolutionReport").getObject(), new ResourceModel("year").getObject(), new ResourceModel("users").getObject()};
		
//		//Agrego las opciones de filtrado segun que reporte se quiere generar
//		originFilter = new OriginFilterOption("component");
//		reportFilter.addFilterOption(originFilter);
//		
//		ageFilter = new AgeFilterOption("component");
//		reportFilter.addFilterOption(ageFilter);
//	
//		customizedMisc = new MiscFilterOption("component");
//		allGendersList.add(0,FilterOption.listFirstOption);
//		genderDropDownElement = new DropDownElementPanel("element", "Sexo", "gender", allGendersList); 
//		customizedMisc.addElement(genderDropDownElement);
//		reportFilter.addFilterOption(customizedMisc);
		
		String newTitle = "Booktube - " + new ResourceModel("usersEvolutionReport").getObject(); 
		super.get("pageTitle").setDefaultModelObject(newTitle);		
		
		

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
		final XYSeries serie = new XYSeries(new ResourceModel("usersEvolutionReport").getObject());				 
		for(Object object : data){
           @SuppressWarnings("unchecked")
           Map<String, Integer> row = (Map<String, Integer>)object;
           serie.add(row.get("year"), row.get("total") ); 
        }
		final XYSeriesCollection collection = new XYSeriesCollection();
	    collection.addSeries(serie);
	    
	    return (Dataset)collection;
	}

	@Override
	public String getReportTitle() {
		return new ResourceModel("usersEvolutionReport").getObject();
	}
	
}
