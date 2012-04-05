package com.booktube.pages;

import org.apache.wicket.AttributeModifier;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.AbstractReadOnlyModel;


/*
 * Esta clase es un BookmarkablePageLink que agrega la siguiente funcionalidad:
 * Cuando su tag asociado <A> es clickeado y se va a la pagina destino, se 
 * modifica su atribubo a CLASS="selected" de modo que se puede modificar su
 * apariencia usando CSS.
 */
public class MenuLink extends BookmarkablePageLink<Void>{
	static final long serialVersionUID = -6638008415906069431L;	
	Class <? extends WebPage>targetPageClass;
	
	public MenuLink(String wicketId, Class<? extends WebPage> pageClass){	
		super(wicketId, pageClass);		
		targetPageClass = pageClass;
	
		this.add(new AttributeModifier("class", true, new AbstractReadOnlyModel<String>() {           
			
			private static final long serialVersionUID = -6657176903031140068L;
	
			@Override
	        public String getObject() {//				
				return getPage().getPageClass().getSimpleName().equals(targetPageClass.getSimpleName()) ? "selected" : "";
	        }
		}));
	
	}
	
	
}
