package com.booktube.pages;



import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.ResourceModel;
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
		// Agrego las opciones de filtrado segun que reporte se quiere generar
		addOriginFilterOption();
		addAgeFilterOption();
		
		addBreadcrumb(new BookmarkablePageLink<Object>("link", ReportsAdministrationPage.class), new ResourceModel("reportsPageTitle").getObject());
		addBreadcrumb(new BookmarkablePageLink<Object>("link", UsersBySexReport.class), new ResourceModel("usersBySexReport").getObject());
		
		// Especifico Titulo ( y etiquetas, si corresponde)
		labels = new String[]{"Evolución de Usuarios por Género", "Año", "Usuarios"};
		
		String newTitle = "Booktube - " + new ResourceModel("usersBySexReport").getObject(); 
		super.get("pageTitle").setDefaultModelObject(newTitle);
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

	@Override
	public String getReportTitle() {
		return new ResourceModel("usersBySexReport").getObject();
	}
	
}
