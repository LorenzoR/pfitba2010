package com.booktube.pages;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.ResourceModel;

public class TermsAndConditionsPage extends BasePage {
	private static final long serialVersionUID = 6008085777255792583L;
	public Page backPage;

	public TermsAndConditionsPage() {
		final WebMarkupContainer parent = new WebMarkupContainer(
				"termsAndConditionsContainer");
		parent.setOutputMarkupId(true);
		add(parent);

		addBreadcrumb(new BookmarkablePageLink<Object>("link",
				TermsAndConditionsPage.class), new ResourceModel(
				"termsAndConditions").getObject());

		String content = "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla vel orci sit amet lacus facilisis dignissim. Etiam id ante non quam porta bibendum vel eu leo. Duis scelerisque tellus at eros faucibus quis placerat lacus posuere. Aliquam interdum tempus dignissim. Fusce pellentesque adipiscing ipsum, sed tincidunt diam hendrerit sed. Cras pharetra facilisis nunc sit amet mattis. Sed volutpat gravida risus, ut lacinia tortor posuere eu. Vestibulum quis dapibus tortor. Aliquam nisi ipsum, vestibulum sed viverra sit amet, sodales quis nulla. Etiam enim turpis, molestie vel volutpat vehicula, pretium a mauris. Quisque purus nunc, placerat at consequat nec, rhoncus at nisi. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eleifend, nisi id semper facilisis, tellus neque faucibus tellus, sed ultrices sapien metus in libero. Nunc volutpat tempus mauris a vulputate. Suspendisse volutpat sodales fringilla. Nunc ac sapien sit amet arcu vestibulum cursus.</p>"
				+ "<p>Praesent nec ligula neque, id sollicitudin ante. Aliquam rhoncus congue mauris id pellentesque. Donec non metus nulla, et rutrum velit. Nulla ac nibh eget arcu molestie pellentesque. Donec sed sem in nibh egestas mollis. Donec ornare lobortis suscipit. Vestibulum lacinia lobortis varius. Suspendisse ac luctus turpis. Suspendisse eget turpis tortor. Maecenas at aliquet velit. Nam lobortis massa at justo mattis tristique. Vestibulum consequat enim a est blandit a luctus neque aliquet. Morbi sed volutpat enim. Vivamus eget neque sit amet justo condimentum tincidunt non id lacus. Mauris et ipsum ac tortor malesuada ornare vulputate pharetra nunc. Vivamus sollicitudin euismod pellentesque.</p>"
				+ "<p>Pellentesque eu mauris volutpat sem lacinia ultrices non ut ante. Donec pretium adipiscing magna sed tempor. Curabitur in diam massa, ac malesuada dui. Aenean ac auctor ipsum. Phasellus condimentum semper erat rutrum lobortis. Nullam fermentum mattis leo vitae cursus. Integer nec massa a purus tempus euismod. Aenean ut velit metus. Nam non quam eget sem posuere commodo. Donec mattis malesuada faucibus. Donec vestibulum euismod pellentesque. Fusce commodo lobortis augue. Nulla imperdiet, metus non auctor fermentum, velit nulla eleifend ligula, eget fringilla purus dolor sed quam.</p>"
				+ "<p>Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Integer posuere iaculis turpis, vitae adipiscing felis aliquam et. Nulla quis placerat ipsum. Aliquam et erat quis lectus fermentum pulvinar. Nunc nec lectus purus, a fermentum nulla. Vestibulum aliquet facilisis justo, ac pharetra urna aliquam in. Proin eget mauris a odio tincidunt ornare a vitae neque. Nunc vehicula auctor nunc eu laoreet. Donec pretium mollis nisi, et bibendum odio facilisis ut.</p>"
				+ "<p>Vestibulum sit amet consectetur mauris. Sed fringilla iaculis interdum. Maecenas ultrices elementum congue. Morbi id velit tellus, adipiscing convallis est. Phasellus eros augue, eleifend et suscipit ut, mattis non lacus. Quisque orci eros, iaculis sit amet volutpat ut, euismod adipiscing leo. Sed eu quam ac libero tincidunt elementum nec et nunc. Maecenas nisi eros, consectetur et vulputate sed, feugiat volutpat metus. Phasellus vitae justo velit. Quisque tincidunt libero id purus facilisis gravida iaculis mauris tempor. Morbi at augue eget nisi condimentum lobortis a sit amet massa. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam iaculis, elit sed suscipit bibendum, eros sapien tempor quam, ut ullamcorper justo metus in lorem.</p>";
		
		parent.add(new Label("aContent", content).setEscapeModelStrings(false));
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		String newTitle = "Booktube - "
				+ new ResourceModel("termsAndConditions").getObject();
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}

}
