package com.booktube.pages;

import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.jfree.data.general.Dataset;

import com.booktube.pages.utilities.Report;


public abstract class ReportPage extends AdministrationPage {
	private static final long serialVersionUID = 151437101760469510L;
	
	private Form<Void> form;	
	private TransparentWebMarkupContainer parent; 
	private Image reportImage;
	
	private final int ANCHO_GRAFICA = 600;			    
    private final int ALTO_GRAFICA = 450;
	
	// Cada reporte puede usar los siguientes filtros
	protected ReportFilterPanel reportFilter;
	protected AgeFilterOption ageFilter;
	protected OriginFilterOption originFilter;
	protected MiscFilterOption customizedMisc;
	
	//Titulos del reporte: Titulo Princial, Etiqueta Eje X, Etiqueta Eje Y
	//Debe tener al menos el titulo principal (entonces, tiene tamaño variable)
	protected String[] labels;
	
	

	public ReportPage(){
		super();
		parent = new TransparentWebMarkupContainer("reportContainer");		
		parent.setOutputMarkupId(true);
		add(parent);
		
		form = new Form<Void>("filterForm");
		parent.add(form);
		
		reportFilter = new ReportFilterPanel("reportFilterPanel");
		form.add(reportFilter);
				
		reportImage = new Image("reportImage", new ContextRelativeResource("/img/blankReport.png"));		
		parent.add(reportImage);		
		
		form.add(new Button("renderReport", new Model<String>("Graficar")) {
			private static final long serialVersionUID = 6743737357599494567L;
			
			@Override
			public void onSubmit() {
			    String filename = "src/main/webapp/img/report.png";
			    
			    try {
			    	final Report report = getReportType();
			        report.saveReportAsPNG(filename, ANCHO_GRAFICA, ALTO_GRAFICA);			        
			    } catch (Exception e) {
			        e.printStackTrace();
			    }		
			    
			    reportImage.setImageResourceReference(new ResourceReference(getReportClass(), "report.png") {
					private static final long serialVersionUID = 7995864723435899261L;

					@Override
					public IResource getResource() {						
						return new ContextRelativeResource("/img/report.png");
					}
				});
			}
		});

		
	}
	
	// Cada reporte especifico debe implementar estos metodos
	
	// Genera el conjunto de datos especifico del reporte
	public abstract Dataset getReportData();
	
	// Obtiene el tipo de Grafico para el reporte
	public abstract Report getReportType();
	
	// Obtiene la clase que correponde al reporte a ser generado. 
	public abstract Class<?> getReportClass();
	
}
