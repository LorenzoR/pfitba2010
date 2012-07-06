package com.booktube.pages;



import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

import com.booktube.pages.utilities.BarReport;

import com.booktube.service.UserService;


public class UsersBySexReport extends ReportPage {
	private static final long serialVersionUID = -4994588390864291547L;
	
	protected AgeFilterOption ageFilter;
	protected OriginFilterOption originFilter;
	protected MiscFilterOption customizedMisc;
	DropDownElementPanel genderDropDownElement;
	private String[] labels = new String[]{"Evolución de Usuarios por Género", "Año", "Usuarios"};
	
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
		
		
		// En esta clase se agrega el boton submit y el evento onSubmit pues cada Reporte
		// necesitara informacion diferente y ejecutara graficos diferentes
		form.add(new Button("renderReport", new Model<String>("Graficar")) {
			private static final long serialVersionUID = 6743737357599494567L;
			
			@Override
			public void onSubmit() {				
				//List<?> data =  userService.getUserEvolutionByYear(originFilter, ageFilter, customizedMisc);
				
//				List<?> data  = getData();
//				DefaultCategoryDataset dataset = new DefaultCategoryDataset();				
//				for(Object object : data){
//		           Map<?, ?> row = (Map<?, ?>)object;
//		           String year = (String)row.get("year");
//		           dataset.setValue((Integer)row.get("female"), "mujeres", year); 
//		           dataset.setValue((Integer)row.get("male"), "hombres", year); 
//		        }				

				 
			    int ANCHO_GRAFICA = 600;			    
			    int ALTO_GRAFICA = 450;
			    
			    String filename = "src/main/webapp/img/report.png";
			    
			    try {
			        final BarReport usersBySexReport = new BarReport(getData(), labels);
			        usersBySexReport.saveReportAsPNG(filename, ANCHO_GRAFICA, ALTO_GRAFICA);			        			        
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
	
}
