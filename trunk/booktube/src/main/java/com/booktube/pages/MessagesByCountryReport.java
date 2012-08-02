package com.booktube.pages;

import java.util.List;

import java.util.Map;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DefaultPieDataset;

import com.booktube.pages.utilities.PieReport;
import com.booktube.pages.utilities.Report;
import com.booktube.service.UserService;

public class MessagesByCountryReport extends ReportPage {
	private static final long serialVersionUID = 4452648166847914596L;
	
	@SpringBean
	UserService userService;
	
	private List<String> allGendersList = userService.getAllGenders();
	private List<String> allYearsList = userService.getAllRegistrationYears();
	
	public MessagesByCountryReport(){
		super();
		// Agrego las opciones de filtrado segun que reporte se quiere generar
		addAgeFilterOption();
		addGenderFilterOption(allGendersList);
		addYearFilterOption(allYearsList);
		
		addBreadcrumb(new BookmarkablePageLink<Object>("link", ReportsAdministrationPage.class), new ResourceModel("reportsPageTitle").getObject());
		addBreadcrumb(new BookmarkablePageLink<Object>("link", MessagesByCountryReport.class), new ResourceModel("messagesByCountryReport").getObject());
		
		// Especifico Titulo ( y etiquetas, si corresponde)
		labels = new String[]{new ResourceModel("messagesByCountryReport").getObject()};
		
		String newTitle = "Booktube - " + new ResourceModel("messagesByCountryReport").getObject(); 
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

	@Override
	public Dataset getReportData() {
		List<?> data = userService.getMessagesByCountry(ageFilter, customizedMisc);		
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
		return MessagesByCountryReport.class;
	}

	@Override
	public String getReportTitle() {
		return new ResourceModel("messagesByCountryReport").getObject();
	}

}
