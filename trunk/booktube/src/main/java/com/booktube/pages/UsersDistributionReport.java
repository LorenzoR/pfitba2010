package com.booktube.pages;

import java.util.List;
import java.io.File;
import java.util.Map;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;


import com.booktube.pages.utilities.JFreeChartPieReport;
import com.booktube.service.UserService;

public class UsersDistributionReport extends ReportPage {
	private static final long serialVersionUID = 254209194148266371L;		
		protected AgeFilterOption ageFilter;
		protected OriginFilterOption originFilter;
		protected MiscFilterOption customizedMisc;
		DropDownElementPanel genderDropDownElement;
		DropDownElementPanel yearsDropDownElement;
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
			
			
			// En esta clase se agrega el boton submit y el evento onSubmit pues cada Reporte
			// necesitara informacion diferente y ejecutara graficos diferentes
			form.add(new Button("renderReport", new Model<String>("Graficar")) {
				private static final long serialVersionUID = 6743737357599494567L;
				
				@Override
				public void onSubmit() {									
					List<?> data  = getData();
			
					DefaultPieDataset result = new DefaultPieDataset();
					for(Object object : data){
			           Map<?, ?> row = (Map<?, ?>)object;
					 	result.setValue((String)row.get("country"), (Number)row.get("total"));		           
			        }	
					 
				    int ANCHO_GRAFICA = 600;			    
				    int ALTO_GRAFICA = 450;
				    
				    String filename = "src/main/webapp/img/report.png";
				    
				    try {
				        final JFreeChartPieReport userDistribReport = new JFreeChartPieReport();
				        final JFreeChart grafica = userDistribReport.crearGrafica(result, "Distribución de Usuarios por país");
				        ChartUtilities.saveChartAsPNG(new File(filename), grafica, ANCHO_GRAFICA, ALTO_GRAFICA);			        
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
	public List<?> getData() {
		return  userService.getUserDistributionByCountry(ageFilter, customizedMisc);
	}

}
