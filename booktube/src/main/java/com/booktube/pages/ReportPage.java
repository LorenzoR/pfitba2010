package com.booktube.pages;

import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.jfree.data.general.Dataset;


public abstract class ReportPage extends AdministrationPage {
	private static final long serialVersionUID = 151437101760469510L;
	protected Form<Void> form;
	protected ReportFilterPanel reportFilter;
	protected TransparentWebMarkupContainer parent; 
	protected Image reportImage;

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
		
	}
	public abstract Dataset getData();
	
}
