package com.booktube.pages;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.effects.sliding.SlideToggle;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.DialogButton;

import com.booktube.model.Campaign;
import com.booktube.pages.customComponents.DynamicLabel;
import com.booktube.pages.customComponents.SuccessDialog;
import com.booktube.service.CampaignService;

public class CampaignsAdministrationPage extends AdministrationPage {
	private static final long serialVersionUID = 3572068607555159574L;

	@SpringBean
	CampaignService campaignService;

	private static SuccessDialog<?> successDialog;
	private static Dialog deleteConfirmationDialog;
	
	private DynamicLabel deleteConfirmationLabel = new DynamicLabel("delete_confirmation_dialog_text", new Model<String>());

	private final DataView<Campaign> dataView;
	private AjaxPagingNavigator footerNavigator;
	private final WebMarkupContainer searchButton;
	private final Form<Campaign> searchForm;

	private final CheckGroup<Campaign> group;

	//private static Long campaignId;
	private static Campaign deleteCampaign;
	private static List<Campaign> deleteCampaigns;

	private Long searchCampaignId;
	private String searchSubject;
	private String searchSender;
	private String searchReceiver;
	private Date searchLowCampaignDate;
	private Date searchHighCampaignDate;

	public CampaignsAdministrationPage() {
		super();
		final WebMarkupContainer parent = new WebMarkupContainer(
				"campaignsContainer");
		parent.setOutputMarkupId(true);
		add(parent);

		addBreadcrumb(new BookmarkablePageLink<Object>("link", CampaignsAdministrationPage.class), new ResourceModel("campaignsAdministrationPageTitle").getObject());
		
//		parent.add(new Label("pageTitle", "Campaigns Administration Page"));

		dataView = campaignList("campaignsList");

		group = new CheckGroup<Campaign>("group", new ArrayList<Campaign>());
		group.add(dataView);
		group.add(new CheckGroupSelector("groupSelector"));

		searchForm = searchCampaignForm(parent);
		parent.add(searchForm);

		searchForm.add(group);

		searchButton = createButtonWithEffect(
				"searchCampaignLink", "searchFields", new SlideToggle());
		parent.add(searchButton);
		
		if (dataView.getItemCount() > 0) {
			feedbackMessage.setVisible(false);
		}
		else {
			feedbackMessage.setVisible(true);
			searchForm.setVisible(false);
			footerNavigator.setVisible(false);
			searchButton.setVisible(false);
		}

		successDialog = new SuccessDialog<WorksAdministrationPage>("success_dialog",  new ResourceModel("campaignDeleted").getObject(), parent);
		parent.add(successDialog);

		deleteConfirmationDialog = deleteConfirmationDialog();
		parent.add(deleteConfirmationDialog);

		String newTitle = "Booktube - Campaigns Administration";
		super.get("pageTitle").setDefaultModelObject(newTitle);

	}

	private Dialog deleteConfirmationDialog() {

		final Dialog dialog = new Dialog("delete_confirmation_dialog");

//		dialog.add(new Label("delete_confirmation_dialog_text",
//				new ResourceModel("deleteCampaignQuestion")));
		dialog.add(deleteConfirmationLabel);

		AjaxDialogButton yesButton = new AjaxDialogButton(new ResourceModel("yes").getObject()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				
				if ( deleteCampaign != null ) {
					campaignService.deleteCampaign(deleteCampaign);
					deleteCampaign = null;
					successDialog.setText( new ResourceModel("campaignDeleted").getObject());
				}
				else if ( deleteCampaigns != null ) {
					successDialog.setText( new ResourceModel("campaignsDeleted").getObject());
					
					for ( Campaign aCampaign : deleteCampaigns ) {
						campaignService.deleteCampaign(aCampaign);
					}
					
					deleteCampaigns = null;
					
				}
				
				dialog.close(target);
				target.add(successDialog);

				successDialog.open(target);
				
				showOrHideTable();
				
//				// JsScopeUiEvent.quickScope(deleteConfirmationdialog.close().render());
//				JsScope.quickScope(dialog.close().render());
//				// deleteConfirmationdialog.close(target);
//				deleteDialog.open(target);
//				// setResponsePage(MessagesAdministrationPage.class);
//				dialog.close(target);
//				
//				showOrHideTable();
			}
		};

		DialogButton noButton = new DialogButton(new ResourceModel("no").getObject(),
				JsScope.quickScope(dialog.close().render()));

		dialog.setButtons(noButton, yesButton);
		//dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close()));

		return dialog;

	}

	private DataView<Campaign> campaignList(String label) {

		IDataProvider<Campaign> dataProvider = new CampaignProvider();

		DataView<Campaign> dataView = new DataView<Campaign>("campaignsList",
				dataProvider, ITEMS_PER_PAGE) {

			private static final long serialVersionUID = 1L;

			protected void populateItem(Item<Campaign> item) {
				final Campaign campaign = (Campaign) item.getModelObject();

				CompoundPropertyModel<Campaign> model = new CompoundPropertyModel<Campaign>(
						campaign);
				item.setDefaultModel(model);

				final PageParameters parameters = new PageParameters();
				parameters.set("campaignId", campaign.getId());
				item.add(new Check<Campaign>("checkbox", item.getModel()));
				item.add(new Label("subject"));
				item.add(new Label("sender"));
				
				final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, getLocale());
				item.add(new Label("date", dateFormat.format(campaign.getDate())));

				item.add(new Link<Campaign>("detailsLink", item.getModel()) {
					private static final long serialVersionUID = 1L;

					public void onClick() {
						setResponsePage(ShowCampaignPage.class, parameters);
					}

				});
//				item.add(new Link<Campaign>("editLink", item.getModel()) {
//					private static final long serialVersionUID = 1L;
//
//					public void onClick() {
//						setResponsePage(ShowCampaignPage.class, parameters);
//					}
//
//				});
				item.add(new AjaxLink<Campaign>("deleteLink", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {

						deleteCampaign = (Campaign) getModelObject();

						deleteConfirmationDialog.open(target);
						
						deleteConfirmationLabel.setLabel(new ResourceModel("deleteCampaignQuestion").getObject());

						target.add(deleteConfirmationLabel);
					}

				});
			}

		};

		return dataView;
	}

	private Form<Campaign> searchCampaignForm(final WebMarkupContainer parent) {

		Form<Campaign> form = new Form<Campaign>("searchCampaignForm");

		CompoundPropertyModel<Campaign> model = new CompoundPropertyModel<Campaign>(
				new Campaign());

		form.setDefaultModel(model);

		footerNavigator = new AjaxPagingNavigator("footerPaginator", dataView) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(parent);
			}
		};
		form.add(footerNavigator);
		
		final WebMarkupContainer searchFields = new WebMarkupContainer(
				"searchFields");
		searchFields.add(AttributeModifier.replace("style", "display: none;"));
		form.add(searchFields);
		
		searchFields.add(feedbackMessage);

		final TextField<Campaign> subject = new TextField<Campaign>("subject");
		searchFields.add(subject);

		final TextField<Campaign> campaignId = new TextField<Campaign>("id");
		searchFields.add(campaignId);

		final TextField<String> sender = new TextField<String>("sender", new Model<String>(""));
		searchFields.add(sender);

		final TextField<String> receiver = new TextField<String>("receiver", new Model<String>(""));
		searchFields.add(receiver);

		final DatePicker<Date> lowCampaignDate = createDatePicker(
				"lowCampaignDate", dateFormat);
		searchFields.add(lowCampaignDate);

		final DatePicker<Date> highCampaignDate = createDatePicker(
				"highCampaignDate", dateFormat);
		searchFields.add(highCampaignDate);

		final AjaxSubmitLink deleteCampaign = new AjaxSubmitLink(
				"deleteCampaign") {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				deleteConfirmationDialog.open(target);

				deleteConfirmationLabel
						.setLabel(new ResourceModel("deleteCampaignsQuestion").getObject());

				target.add(deleteConfirmationLabel);
				
				deleteCampaigns = (List<Campaign>) group.getDefaultModelObject();
				
				showOrHideTable();

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

		};

		form.add(deleteCampaign);

		searchFields.add(new AjaxSubmitLink("searchCampaign") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				searchFields.add(AttributeModifier.replace("style",
						"display: block;"));
				// String bookIdString =
				try {
					searchCampaignId = new Long(campaignId
							.getDefaultModelObjectAsString());
				} catch (NumberFormatException ex) {
					searchCampaignId = null;
				}

				searchSubject = new String(subject
						.getDefaultModelObjectAsString());
				searchSender = new String(sender
						.getDefaultModelObjectAsString());
				searchReceiver = new String(receiver
						.getDefaultModelObjectAsString());

				if (!StringUtils.isBlank(lowCampaignDate
						.getDefaultModelObjectAsString())) {
					try {
						searchLowCampaignDate = (Date) formatter
								.parse(lowCampaignDate
										.getDefaultModelObjectAsString());
					} catch (ParseException e) {
						searchLowCampaignDate = null;
					}
				} else {
					searchLowCampaignDate = null;
				}

				if (!StringUtils.isBlank(highCampaignDate
						.getDefaultModelObjectAsString())) {
					try {
						searchHighCampaignDate = (Date) formatter
								.parse(highCampaignDate
										.getDefaultModelObjectAsString());
					} catch (ParseException e) {
						searchHighCampaignDate = null;
					}
				} else {
					searchHighCampaignDate = null;
				}
				
				if (dataView.getItemCount() <= 0) {
					deleteCampaign.setVisible(false);
					group.setVisible(false);
					footerNavigator.setVisible(false);
					feedbackMessage.setVisible(true);
				} else {
					deleteCampaign.setVisible(true);
					group.setVisible(true);
					footerNavigator.setVisible(true);
					feedbackMessage.setVisible(false);
				}

				dataView.setCurrentPage(0);
				target.add(parent);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}

		});

		return form;

	}
	
	private void showOrHideTable() {
		if (dataView.getItemCount() <= 0) {
			searchForm.setVisible(false);
			dataView.setVisible(false);
			footerNavigator.setVisible(false);
			searchButton.setVisible(false);
			feedbackMessage.setVisible(true);
		} else {
			searchForm.setVisible(true);
			dataView.setVisible(true);
			footerNavigator.setVisible(true);
			searchButton.setVisible(true);
			feedbackMessage.setVisible(false);
		}
	}

//	private String getReceivers(Campaign campaign) {
//		String receivers = "";
//		for (CampaignDetail aCampaignDetail : campaign.getReceiver()) {
//			if (aCampaignDetail.getReceiver() != null) {
//				receivers += aCampaignDetail.getReceiver().getUsername() + ", ";
//			}
//		}
//		return receivers;
//	}

	class CampaignProvider implements IDataProvider<Campaign> {

		private static final long serialVersionUID = 1L;

		public CampaignProvider() {
		}

		public Iterator<Campaign> iterator(int first, int count) {
			return campaignService.getCampaigns(first, count, searchCampaignId,
					searchSubject, searchSender, searchReceiver,
					searchLowCampaignDate, searchHighCampaignDate).iterator();
		}

		public int size() {
			return campaignService.getCount(searchCampaignId, searchSubject,
					searchSender, searchReceiver, searchLowCampaignDate,
					searchHighCampaignDate);
		}

		public IModel<Campaign> model(Campaign campaign) {
			return new CompoundPropertyModel<Campaign>(campaign);
		}

		public void detach() {
			// TODO Auto-generated method stub

		}
	}
}
