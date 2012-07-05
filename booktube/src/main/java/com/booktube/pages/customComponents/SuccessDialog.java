package com.booktube.pages.customComponents;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;

public class SuccessDialog<C extends IRequestablePage> extends Dialog {

	private static final long serialVersionUID = 1L;

	public SuccessDialog(String id, String text, final Class<C> webPage,
			final PageParameters pageParameters) {
		super(id);
		this.add(new Label("text", text));

		AjaxDialogButton ok = new AjaxDialogButton("OK") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				// do your cancel logic here
				System.out.println("BUTTON CLICKED!!");
				setResponsePage(webPage, pageParameters);
			}
		};

		this.setButtons(ok);
	}

	public SuccessDialog(String id, String text, final WebPage backPage) {
		super(id);
		this.add(new Label("text", text));

		AjaxDialogButton ok = new AjaxDialogButton("OK") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				// do your cancel logic here
				System.out.println("BUTTON CLICKED!!");
				setResponsePage(backPage);

			}
		};

		this.setButtons(ok);

	}

}
