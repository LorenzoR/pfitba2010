package com.booktube.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;

import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;

import org.jfree.data.general.Dataset;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;

import com.booktube.pages.customComponents.AJAXDownload;
import com.booktube.pages.utilities.Report;


public abstract class ReportPage extends AdministrationPage {
	private static final long serialVersionUID = 151437101760469510L;
	
	private Form<Void> form;	
	private TransparentWebMarkupContainer parent; 
	private Image reportImage;
	private Report report; 
	
	private int ANCHO_GRAFICA = 600;			    
    private int ALTO_GRAFICA = 450;
	
	// Cada reporte puede usar los siguientes filtros
	protected ReportFilterPanel reportFilter;
	protected AgeFilterOption ageFilter;
	protected OriginFilterOption originFilter;
	protected MiscFilterOption customizedMisc;
	
	//Titulos del reporte: Titulo Princial, Etiqueta Eje X, Etiqueta Eje Y
	//Debe tener al menos el titulo principal (entonces, tiene tamaño variable)
	protected String[] labels;
	
	// Si se hace click en el link download y no se generó todavía un grafico,
	// entonces se presenta un mensaje de error al usuario
	private boolean chartCreated;
	private Dialog downloadErrorDialog;

	public ReportPage(){
		super();
		
		// Agregamos el componente que se usara para mensaje de error
		chartCreated = false;		
		downloadErrorDialog = downloadErrorDialog();
		add(downloadErrorDialog);		

		// Agregamos los componentes propios de la pagina
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
			    chartCreated = true;
			    try {
			    	report = getReportType();
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
		
		
		final AJAXDownload download = new AJAXDownload(){
			private static final long serialVersionUID = -6686805855143115318L;

			@Override
			protected IResourceStream getResourceStream()
			{
				IResourceStream resourceStream = new AbstractResourceStreamWriter() {
					private static final long serialVersionUID = 2132022243218093750L;
					@Override
				    public String getContentType() {
				    	  return "application/pdf";
				    }
					
					public void write(Response output) {					
						try {
					          report.writeChartAsPdf(output.getOutputStream(), ANCHO_GRAFICA, ALTO_GRAFICA);
					        } catch (Exception e) {
					        	e.printStackTrace();
					        }
					}
			    };
				return resourceStream;
			}
			 @Override 
             protected String getFileName() { 
                     return  "report.pdf";
             } 
		};
		form.add(download);

		form.add(new AjaxFallbackLink<Void>("downloadLink") {
			private static final long serialVersionUID = 4657893854790752619L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				if( !chartCreated){					
					downloadErrorDialog.open(target);
					return;
				}
				if (target != null) {				
					download.initiate(target);			    
				}				
			}
		});
		
// Esta version funciona pero no es un evento AJAX		
//		form.add(new Link<Void>("downloadLink") {
//			private static final long serialVersionUID = -7156242893015466304L;
//
//			@Override
//			public void onClick() {
//				
//			    IResourceStream resourceStream = new AbstractResourceStreamWriter() {
//					private static final long serialVersionUID = 2132022243218093750L;
//					@Override
//				    public String getContentType() {
//				    	  return "application/pdf";
//				    }
//					
//					public void write(Response output) {					
//						try {
//					          report.writeChartAsPdf(output.getOutputStream(), ANCHO_GRAFICA, ALTO_GRAFICA);
//					        } catch (Exception e) {
//					        	e.printStackTrace();
//					        }
//					}
//			    };
//
//			    getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(resourceStream, "report.pdf"));			    
//			    
//			}
//		});
		
	}
	
	// Cada reporte especifico debe implementar estos metodos
	
	// Genera el conjunto de datos especifico del reporte
	public abstract Dataset getReportData();
	
	// Obtiene el tipo de Grafico para el reporte
	public abstract Report getReportType();
	
	// Obtiene la clase que correponde al reporte a ser generado. 
	public abstract Class<?> getReportClass();
	
	
	private Dialog downloadErrorDialog() {

		final Dialog dialog = new Dialog("download_error_dialog");

		dialog.setTitle("<span class=\"ui-icon ui-icon-alert\" style=\"float: left;\"></span> &nbsp; Error");
		
		
		AjaxDialogButton ok = new AjaxDialogButton("OK") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				dialog.close(target);
			}
		};

		dialog.setButtons(ok);
		return dialog;
	}

	
}
