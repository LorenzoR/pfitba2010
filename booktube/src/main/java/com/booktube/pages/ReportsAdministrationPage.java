package com.booktube.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.ResourceModel;

public class ReportsAdministrationPage extends AdministrationPage{
	private static final long serialVersionUID = -1116385333477058852L;
	public Page backPage;

	//public AdministrationPage(final PageParameters parameters) {
	public ReportsAdministrationPage() {
			super();
			final WebMarkupContainer parent = new WebMarkupContainer("reportsContainer");
			parent.setOutputMarkupId(true);
			add(parent);
			
			addBreadcrumb(new BookmarkablePageLink<Object>("link", ReportsAdministrationPage.class), new ResourceModel("reportsPageTitle").getObject());
			
			parent.add(new BookmarkablePageLink<String>("usersEvolutionReport", UsersEvolutionReport.class));
			parent.add(new BookmarkablePageLink<String>("usersDistributionReport", UsersDistributionReport.class));
			parent.add(new BookmarkablePageLink<String>("usersBySexReport", UsersBySexReport.class));
			
			parent.add(new BookmarkablePageLink<String>("worksByCategoryReport", WorksByCategoryReport.class));
			parent.add(new BookmarkablePageLink<String>("messagesBySubjectReport", MessagesBySubjectReport.class));
			parent.add(new BookmarkablePageLink<String>("messagesByCountryReport", MessagesByCountryReport.class));
			
			String newTitle = "Booktube - " + new ResourceModel("reportsPageTitle").getObject(); 
			super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}
