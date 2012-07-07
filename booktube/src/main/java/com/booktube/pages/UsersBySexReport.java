package com.booktube.pages;



import java.util.List;
import java.util.Map;

import org.apache.wicket.spring.injection.annot.SpringBean;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

import com.booktube.pages.utilities.BarReport;
import com.booktube.pages.utilities.Report;
import com.booktube.service.UserService;


public class UsersBySexReport extends ReportPage {
	private static final long serialVersionUID = -4994588390864291547L;
	
	@SpringBean
	UserService userService;	
		
	public UsersBySexReport(){
		super();
		// No es necesario WebMarkupContainer pues esta subclase no agrega codigo estatico HTML a la superclase(ReportPage)

		//Agrego las opciones de filtrado segun que reporte se quiere generar
		originFilter = new OriginFilterOption("component");
		reportFilter.addFilterOption(originFilter);
		
		ageFilter = new AgeFilterOption("component");
		reportFilter.addFilterOption(ageFilter);	
		
		String newTitle = "Booktube - Users By Sex Report"; 
		super.get("pageTitle").setDefaultModelObject(newTitle);	
		
		labels = new String[]{"Evolución de Usuarios por Género", "Año", "Usuarios"};	

	}

	@Override
	public Dataset getReportData() {
		List<?> data = userService.getUserEvolutionBySex(originFilter, ageFilter);
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();				
		for(Object object : data){
           Map<?, ?> row = (Map<?, ?>)object;
           String year = (String)row.get("year");
           dataset.setValue((Integer)row.get("female"), "mujeres", year); 
           dataset.setValue((Integer)row.get("male"), "hombres", year); 
        }
		return (Dataset)dataset;

	}

	@Override
	public Report getReportType() {
		return new BarReport(getReportData(), labels);
	}

	@Override
	public Class<?> getReportClass() {
		return UsersBySexReport.class;
	}
	
}
