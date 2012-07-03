package com.booktube.pages.customComponents;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public class DynamicLabel extends Label {

	private static final long serialVersionUID = 1L;

	private static Model<String> model = new Model<String>() {

		private static final long serialVersionUID = 1L;

		private String text;

		public String getObject() {
			return text;
		}

		public void setObject(String value) {
			this.text = value;
		}
	};

	public DynamicLabel(String id) {
		super(id, model);
		this.setOutputMarkupId(true);
	}

	public void setLabel(String text) {
		DynamicLabel.model.setObject(text);
		//DynamicLabel.model.getObject();
	}

	public String getLabel() {
		return DynamicLabel.model.getObject();
	}

}
