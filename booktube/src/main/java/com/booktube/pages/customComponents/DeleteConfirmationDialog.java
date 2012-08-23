package com.booktube.pages.customComponents;

import org.apache.wicket.ajax.AjaxRequestTarget;

import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.component.IRequestablePage;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.DialogButton;



public class DeleteConfirmationDialog<C extends IRequestablePage> extends Dialog {
	private static final long serialVersionUID = 1L;
	private DynamicLabel dynamicLabel;
	private Object object;
	
	@SuppressWarnings("rawtypes")
	public DeleteConfirmationDialog(String id, String text, final Object service, final Object deletObj, final SuccessDialog successDialog) {
		super(id);

		this.object = deletObj;
		
		final DeleteConfirmationDialog dialog = this;
		
		dynamicLabel = new DynamicLabel("text", new Model<String>());
//		dynamicLabel.setLabel(text);
		this.add(dynamicLabel);
		
		AjaxDialogButton yesButton = new AjaxDialogButton(new ResourceModel("yes").getObject()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {

				successDialog.open(target);

				dialog.close(target);
			}
		};
		
		DialogButton noButton = new DialogButton(new ResourceModel("no").getObject(),
				JsScope.quickScope(dialog.close().render()));

		setButtons(noButton, yesButton);
		
	}
	
	public DynamicLabel getLabel() {
		return dynamicLabel;
	}
	
	public void setDeleteObject(Object object) {
		this.object = object;
	}
	
	public void setText(String text) {
		dynamicLabel.setLabel(text);
	}

}
