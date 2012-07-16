package com.booktube.pages.customComponents;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.DialogButton;

import com.booktube.persistence.hibernate.AbstractDaoHibernate;

public class DeleteConfirmationDialog<C extends IRequestablePage> extends Dialog {

	private DynamicLabel dynamicLabel;
	private Object object;
	
	public DeleteConfirmationDialog(String id, String text, final Object service, final Object deletObj, final SuccessDialog successDialog) {
		super(id);

		this.object = deletObj;
		
		final DeleteConfirmationDialog dialog = this;
		
		dynamicLabel = new DynamicLabel("text", new Model<String>());
//		dynamicLabel.setLabel(text);
		this.add(dynamicLabel);
		
		AjaxDialogButton yesButton = new AjaxDialogButton("Si") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {

				System.out.println("Borro Boook");

				
				System.out.println("BOOK ES : " + object.toString());
				//dao.delete(object);
				//service.delete(object);


//				target.add(successDialog);
				//successDialog.setText("ESTE SI CAMBIA");
				//target.add(successDialog);
				successDialog.open(target);

				dialog.close(target);
			}
		};
		
		DialogButton noButton = new DialogButton("No",
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
