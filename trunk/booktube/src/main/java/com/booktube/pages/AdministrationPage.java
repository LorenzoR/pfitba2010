package com.booktube.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.service.BookService;
import com.booktube.service.UserService;

public class AdministrationPage extends BasePage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8681226479793317569L;

	@SpringBean
	BookService bookService;

	@SpringBean
	UserService userService;

	public static final int BOOKS_PER_PAGE = 5;

	public Page backPage;

	//public AdministrationPage(final PageParameters parameters) {
	public AdministrationPage() {
			super();
			final WebMarkupContainer parent = new WebMarkupContainer("adminContainer"){			
				private static final long serialVersionUID = -3147289158931550124L;

				public boolean isTransparentResolver(){
					return true;
				}
			};
			parent.setOutputMarkupId(true);
			add(parent);
			
			parent.add(new LeftMenuPanel("leftPaneDiv"));
			
			
			//parent.add(new Label("aContent", "Aca va el contenido de la Admin Page."));
			
			
		}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		
	}
		
}
