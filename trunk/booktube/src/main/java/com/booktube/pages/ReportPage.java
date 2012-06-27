package com.booktube.pages;

import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;

public class ReportPage extends AdministrationPage {
	private static final long serialVersionUID = 151437101760469510L;
	protected Form<Void> form;
	protected ReportFilterPanel reportFilter;

	public ReportPage(){
		super();
		final TransparentWebMarkupContainer parent = new TransparentWebMarkupContainer("reportContainer");		
		parent.setOutputMarkupId(true);
		add(parent);
		
		form = new Form<Void>("filterForm");
		parent.add(form);
		
		reportFilter = new ReportFilterPanel("reportFilterPanel");
		form.add(reportFilter);
		
	}
	
}
