package com.booktube.pages;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.booktube.pages.utilities.JFreeChartBarReport;
import com.booktube.pages.utilities.JFreeChartLineReport;
import com.booktube.service.UserService;


public class UsersBySexReport extends ReportPage {
	private static final long serialVersionUID = -4994588390864291547L;
	
	protected AgeFilterOption ageFilter;
	protected OriginFilterOption originFilter;
	protected MiscFilterOption customizedMisc;
	DropDownElementPanel genderDropDownElement;
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
				//List<?> data  = getData();
				
				
				
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				dataset.setValue(5, "jalados", "José");
				dataset.setValue(5, "jalados", "Ronny");
				dataset.setValue(4, "jalados", "Frank");
				dataset.setValue(2, "jalados", "Sumire");
				dataset.setValue(0, "jalados", "Maribel");
				dataset.setValue(-1, "jalados", "Ian");
				dataset.setValue(10, "aprobados", "José");
				dataset.setValue(9, "aprobados", "Ronny");
				dataset.setValue(12, "aprobados", "Frank");
				dataset.setValue(13, "aprobados", "Sumire");
				dataset.setValue(15, "aprobados", "Maribel");
				dataset.setValue(12, "aprobados", "Ian");
				
				
//				final XYSeries serie = new XYSeries("Evolucion de Usuario en el tiempo");
//				for(Object object : data){
//		           Map<?, ?> row = (Map<?, ?>)object;
//		           serie.add(Double.valueOf((String)row.get("year")),Double.valueOf((String)row.get("total")) ); 
//		        }
//				    
				
				 
			    int ANCHO_GRAFICA = 600;			    
			    int ALTO_GRAFICA = 450;
			    
			    String filename = "src/main/webapp/img/report.png";
			    
			    try {
			        final JFreeChartBarReport usersBySexReport = new JFreeChartBarReport();
			        final JFreeChart grafica = usersBySexReport.crearGrafica(dataset);
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
		return  userService.getUserEvolutionByYear(originFilter, ageFilter, customizedMisc);
	}
	
}
