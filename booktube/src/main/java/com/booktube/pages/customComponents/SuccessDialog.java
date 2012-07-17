package com.booktube.pages.customComponents;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;

public class SuccessDialog<C extends IRequestablePage> extends Dialog {

	private static final long serialVersionUID = 1L;
	private DynamicLabel dynamicLabel;
	private boolean redirect = true;

	public SuccessDialog(String id, String text, final Class<C> webPage,
			final PageParameters pageParameters) {
		super(id);

		dynamicLabel = new DynamicLabel("text", new Model<String>());
		dynamicLabel.setLabel(text);
		this.add(dynamicLabel);

		AjaxDialogButton ok = new AjaxDialogButton("OK") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				// do your cancel logic here
				System.out.println("BUTTON CLICKED!!");
				if (redirect) {
					setResponsePage(webPage, pageParameters);
				} else {
					SuccessDialog.this.close(target);
				}
			}
		};

		this.setButtons(ok);
	}

	public SuccessDialog(String id, String text, final WebPage backPage) {
		super(id);

		dynamicLabel = new DynamicLabel("text", new Model<String>());
		dynamicLabel.setLabel(text);
		this.add(dynamicLabel);

		AjaxDialogButton ok = new AjaxDialogButton("OK") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				// do your cancel logic here
				System.out.println("BUTTON CLICKED!!");
				if (redirect) {
					setResponsePage(backPage);
				} else {
					SuccessDialog.this.close(target);
				}

			}
		};

		this.setButtons(ok);

	}

	public SuccessDialog(String id, String text, final WebMarkupContainer parent) {
		super(id);

		dynamicLabel = new DynamicLabel("text", new Model<String>());
		dynamicLabel.setLabel(text);
		this.add(dynamicLabel);
		// final SuccessDialog<C> dialog = this;

		AjaxDialogButton ok = new AjaxDialogButton("OK") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				// do your cancel logic here
				System.out.println("BUTTON CLICKED!!");
				// dialog.close(target);
				SuccessDialog.this.close(target);
				target.add(parent);
			}
		};

		this.setButtons(ok);

	}

	public DynamicLabel getLabel() {
		return dynamicLabel;
	}

	public void setText(String text) {
		dynamicLabel.setLabel(text);
		// System.out.println("+++++" + dynamicLabel.getLabel());
	}

	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}

}
