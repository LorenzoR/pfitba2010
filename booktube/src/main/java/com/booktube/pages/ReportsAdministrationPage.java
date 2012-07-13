package com.booktube.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class ReportsAdministrationPage extends AdministrationPage{
	private static final long serialVersionUID = -1116385333477058852L;
	public Page backPage;

	//public AdministrationPage(final PageParameters parameters) {
	public ReportsAdministrationPage() {
			super();
			final WebMarkupContainer parent = new WebMarkupContainer("reportsContainer");
			parent.setOutputMarkupId(true);
			add(parent);
			
			setBreadcrumbs("Administración > Reportes");
			
			parent.add(new BookmarkablePageLink<String>("usersEvolutionReport", UsersEvolutionReport.class));
			parent.add(new BookmarkablePageLink<String>("usersDistributionReport", UsersDistributionReport.class));
			parent.add(new BookmarkablePageLink<String>("usersBySexReport", UsersBySexReport.class));
			
			parent.add(new BookmarkablePageLink<String>("worksByCategoryReport", WorksByCategoryReport.class));
			parent.add(new BookmarkablePageLink<String>("messagesBySubjectReport", MessagesBySubjectReport.class));
			parent.add(new BookmarkablePageLink<String>("messagesByCountryReport", MessagesByCountryReport.class));
			
			String newTitle = "Booktube - Reports Administration"; 
			super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}
