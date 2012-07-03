package com.booktube.pages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.effects.Effect;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;

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

	protected static final int ITEMS_PER_PAGE = 5;
	
	final protected String dateFormat = "dd/mm/yy";
	final protected DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	public AdministrationPage() {
			super();
			
			final TransparentWebMarkupContainer parent = new TransparentWebMarkupContainer("adminContainer");
			
			parent.setOutputMarkupId(true);
			add(parent);
			
			parent.add(new LeftMenuPanel("leftPaneDiv"));
			
			
		}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		String newTitle = "Booktube - Administracion"; 
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}
	
	protected DatePicker<Date> createDatePicker(String label, String dateFormat) {
		final DatePicker<Date> datePicker = new DatePicker<Date>(label,
				new Model<Date>(), Date.class);
		datePicker.setAltFormat(dateFormat);
		datePicker.setAltField(dateFormat);
		datePicker.setDateFormat(dateFormat);

		return datePicker;
	}

	protected WebMarkupContainer createButtonWithEffect(String buttonId,
			final String textId, final Effect effect) {
		WebMarkupContainer button = new WebMarkupContainer(buttonId);

		button.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			private static final long serialVersionUID = 1L;

			@Override
			public JsScope callback() {
				return JsScope.quickScope(new JsStatement()
						.$(null, "#" + textId).chain(effect).render());
			}
		}));

		return button;
	}
	
//	protected Dialog deleteDialog(Label label, final Class responsePage) {
//
//		Dialog dialog = new Dialog("success_dialog");
//
//		//dialog.add(successDialogLabel);
//		dialog.add(label);
//		
//		AjaxDialogButton ok = new AjaxDialogButton("OK") {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onButtonClicked(AjaxRequestTarget target) {
//				//setResponsePage(WorksAdministrationPage.class);
//				setResponsePage(responsePage);
//			}
//		};
//
//		dialog.setButtons(ok);
//		dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close().render()));
//
//		return dialog;
//
//	}
		
}
