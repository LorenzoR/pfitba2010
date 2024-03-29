package com.booktube.pages;

import java.util.List;

import javax.servlet.ServletContext;



import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;

import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
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
import com.booktube.pages.customComponents.panels.AgeFilterOption;
import com.booktube.pages.customComponents.panels.DropDownElementPanel;
import com.booktube.pages.customComponents.panels.MiscFilterOption;
import com.booktube.pages.customComponents.panels.OriginFilterOption;
import com.booktube.pages.customComponents.panels.ReportFilterPanel;
import com.booktube.pages.utilities.Report;


public abstract class ReportPage extends AdministrationPage {
	private static final long serialVersionUID = 151437101760469510L;
	
	private Form<Void> form;	
	private TransparentWebMarkupContainer parent; 
	private Image reportImage;
	private Report report; 
	
	private int CHART_WIDTH = 600;			    
    private int CHART_HEIGHT = 450;
	
	// Cada reporte puede usar los siguientes filtros
	protected ReportFilterPanel reportFilter = null;
	protected AgeFilterOption ageFilter = null;
	protected OriginFilterOption originFilter = null;
	protected MiscFilterOption customizedMisc = null;
	
	//Customized Filter puede incluir dos tipos de condiciones
	protected DropDownElementPanel genderDropDownElement;
	protected DropDownElementPanel yearsDropDownElement;
	
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
		
		parent.add(new Label("pageTitle", getReportTitle()));
		
		form = new Form<Void>("filterForm");
		parent.add(form);
		
		reportFilter = new ReportFilterPanel("reportFilterPanel");
		form.add(reportFilter);

//		reportImage = new Image("reportImage", new ContextRelativeResource("img/report.png"));
		reportImage = new Image("reportImage", new ContextRelativeResource("img/blankReport.png"));
		reportImage.setVisible(true);
		reportImage.setOutputMarkupId(true);		
		parent.add(reportImage);		

		
		form.add(new AjaxButton("renderReport",	new Model<String>("Graficar")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				String filename = getRootContext("img/report.png");		    
			    chartCreated = true;
			    
			    try {
			    	report = getReportType();
			        report.saveReportAsPNG(filename, CHART_WIDTH, CHART_HEIGHT);
			    } catch (Exception e) {
			        e.printStackTrace();
			    }		
			    
		    reportImage.setImageResourceReference(new ResourceReference(getReportClass(), "img/report.png") {
				private static final long serialVersionUID = 7995864723435899261L;

				@Override
				public IResource getResource() {						
					return new ContextRelativeResource("img/report.png");
				}
			});
		    
			reportImage.add(new AttributeModifier("class", new Model<String>("reportImage")));
			reportImage.setVisible(true);
			target.add(reportImage);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				Logger.getLogger("ReportPage.Form.onSubmit()").error("Se produjo un error al ejecutarse onSubmit() del AjaxButton.");
			}
		});
				
				
				
//		form.add(new Button("renderReport", new Model<String>("Graficar")) {
//			private static final long serialVersionUID = 6743737357599494567L;
//			
//			@Override
//			public void onSubmit() {
////			    String filename = "src/main/webapp/img/report.png";
//			    String filename = getRootContext("img/report.png");		    
//			    chartCreated = true;
//			    
//			    try {
//			    	report = getReportType();
//			        report.saveReportAsPNG(filename, CHART_WIDTH, CHART_HEIGHT);
//			    } catch (Exception e) {
//			        e.printStackTrace();
//			    }		
//			    
////			    reportImage.setImageResourceReference(new ResourceReference(getReportClass(), "img/report.png") {
////					private static final long serialVersionUID = 7995864723435899261L;
////
////					@Override
////					public IResource getResource() {						
////						return new ContextRelativeResource("img/report.png");
////					}
////				});
//			    reportImage.add(new AttributeModifier("class", new Model<String>("reportImage")));
//			   
////			    reportImage.setVisible(true);
//			    
//			}
//		});
		
		
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
								String details = getFilterDetails(ageFilter, originFilter, customizedMisc);
					          report.writeChartAsPdf(output.getOutputStream(), CHART_WIDTH, CHART_HEIGHT, details);
					        } catch (Exception e) {
					        	e.printStackTrace();
					        }
					}

					// FALTA INTERNACIONALIZAR
					private String getFilterDetails(AgeFilterOption ageFilter,
							OriginFilterOption originFilter,
							MiscFilterOption customizedMisc) {
						StringBuffer resp = new StringBuffer("");						
						
						if( ageFilter != null){
							String min = ageFilter.getSelectedMinAge();
							String max = ageFilter.getSelectedMaxAge();	
							
							if( min != null )
								resp.append(getSeparator(resp)+ageFilter.getMinAgeLabel()+" = "+min+" " + new ResourceModel("years").getObject());
							if( max != null )
								resp.append(getSeparator(resp)+ageFilter.getMaxAgeLabel()+" = "+max+" " + new ResourceModel("years").getObject());							
						}
						if( originFilter != null ){
							String city = originFilter.getSelectedCity();
							String country = originFilter.getSelectedCountry();
							
							if( country != null)
								resp.append(getSeparator(resp)+originFilter.getCountryLabel()+" = "+country);
							if( city != null )
								resp.append(getSeparator(resp)+originFilter.getCityLabel()+" = "+city);
						}
						if( customizedMisc != null ){
							List<DropDownElementPanel> list = customizedMisc.getElements();
							for( DropDownElementPanel elem : list ){								
								String value = elem.getSelectedValue();
								if( value  != null )
									resp.append(getSeparator(resp)+elem.getLabel()+" = "+elem.getSelectedValue()+"  ");
							}								
						}
						if( resp.toString().isEmpty() )
							resp.append("No se especificaron criterios.");
						return resp.toString(); 
						
					}

					private String getSeparator(StringBuffer buff) {
						String cad = "";
						if ( buff.length() > 0 )
							cad = "  |  ";
						return cad;
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
		
	}
	
	/*
	 *  CADA REPORTE ESPECIFICO DEBE IMPLEMENTAR ESTOS METODOS
	 *  ======================================================
	 */
	
	// Genera el conjunto de datos especifico del reporte
	public abstract Dataset getReportData();
	
	// Obtiene el tipo de Grafico para el reporte
	public abstract Report getReportType();
	
	// Obtiene la clase que correponde al reporte a ser generado. 
	public abstract Class<?> getReportClass();
	
	public abstract String getReportTitle();
	
	/*
	 *  METODOS PARA AGREGAR LAS OPCIONES DEL FILTRO
	 *  ============================================
	 *  Cada Reporte agrega solo las que necesita
	 */	
	protected void addOriginFilterOption(){
		originFilter = new OriginFilterOption("component");
		reportFilter.addFilterOption(originFilter);
	}
	
	protected void addAgeFilterOption(){
		ageFilter = new AgeFilterOption("component");
		reportFilter.addFilterOption(ageFilter);	
	}
	
	protected void addGenderFilterOption(List<String> allGendersList){
		createMiscFilterOption();		
//		allGendersList.add(0,FilterOption.listFirstOption);
		genderDropDownElement = new DropDownElementPanel("element", new ResourceModel("gender").getObject(), "gender", allGendersList);			
		customizedMisc.addElement(genderDropDownElement);		
	}
	
	protected void addYearFilterOption(List<String> allYearsList){
		createMiscFilterOption();
//		allYearsList.add(0,FilterOption.listFirstOption);
		yearsDropDownElement = new DropDownElementPanel("element", new ResourceModel("year").getObject(), "registration_date", allYearsList);			
		customizedMisc.addElement(yearsDropDownElement);
	}
	
	/*
	 *  METODOS PRIVADOS
	 *  ================
	 */
	private void createMiscFilterOption(){
		if( customizedMisc == null ){
			customizedMisc = new MiscFilterOption("component");
			reportFilter.addFilterOption(customizedMisc);
		}
	}
	
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
	public String getRootContext(String resource){
		 
		String rootContext = "";
 
		WebApplication webApplication = WebApplication.get();
		if(webApplication!=null){
			ServletContext servletContext = webApplication.getServletContext();
			
			if(servletContext!=null){
				rootContext = servletContext.getRealPath(resource);
//				rootContext = servletContext.getServletContextName();
			}else{
				//do nothing
			}
		}else{
			//do nothing
		}
 
		return rootContext;
 
}
	
}
