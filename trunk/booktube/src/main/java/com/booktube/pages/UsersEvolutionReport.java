package com.booktube.pages;


import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.general.Dataset;


import com.booktube.pages.utilities.LineReport;
import com.booktube.service.UserService;


public class UsersEvolutionReport extends ReportPage {
	private static final long serialVersionUID = 6051762145219128009L;
	
	protected AgeFilterOption ageFilter;
	protected OriginFilterOption originFilter;
	protected MiscFilterOption customizedMisc;
	DropDownElementPanel genderDropDownElement;
	private String[] labels = new String[]{"Evolución de Usuarios en el tiempo", "Año", "Usuarios"};
	
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
		
		
		
		// En esta clase se agrega el boton submit y el evento onSubmit pues cada Reporte
		// necesitara informacion diferente y ejecutara graficos diferentes
		form.add(new Button("renderReport", new Model<String>("Graficar")) {
			private static final long serialVersionUID = 6743737357599494567L;
			
			@Override
			public void onSubmit() {				
				//List<?> data =  userService.getUserEvolutionByYear(originFilter, ageFilter, customizedMisc);		
//				List<?> data  = getData();
//				final XYSeries serie = new XYSeries("Evolucion de Usuarios en el tiempo");				 
//				for(Object object : data){
//		           Map<?, ?> row = (Map<?, ?>)object;
//		           serie.add(Double.valueOf((String)row.get("year")),Double.valueOf((String)row.get("total")) ); 
//		        }
				    
				
//				final XYSeriesCollection collection = new XYSeriesCollection();
//			    collection.addSeries(serie);
				
//			     
			    int ANCHO_GRAFICA = 600;			    
			    int ALTO_GRAFICA = 450;
			    
			    String filename = "src/main/webapp/img/report.png";
			    
			    try {
			        final LineReport userEvolReport = new LineReport(getData(), labels);			        
			        userEvolReport.saveReportAsPNG(filename, ANCHO_GRAFICA, ALTO_GRAFICA);			        
			    } catch (Exception e) {
			        e.printStackTrace();
			    }		
			    
			    reportImage.setImageResourceReference(new ResourceReference(UsersEvolutionReport.class, "report.png") {
					private static final long serialVersionUID = 7995864723435899261L;

					@Override
					public IResource getResource() {						
						return new ContextRelativeResource("/img/report.png");
					}
				});
			}
		});


	}

	@Override
	public Dataset getData() {
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
