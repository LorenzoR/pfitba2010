package com.booktube.pages;

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
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.effects.sliding.SlideToggle;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.datepicker.DatePicker;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.DialogButton;

import com.booktube.model.Campaign;
import com.booktube.model.CampaignDetail;
import com.booktube.service.CampaignService;

public class CampaignsAdministrationPage extends AdministrationPage {
	private static final long serialVersionUID = 3572068607555159574L;

	@SpringBean
	CampaignService campaignService;

	private static Dialog deleteDialog;
	private static Dialog deleteConfirmationDialog;

	private final DataView<Campaign> dataView;
	private final PagingNavigator footerNavigator;

	private final CheckGroup<Campaign> group;

	private static Long campaignId;

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

		setBreadcrumbs("Administración > Campañas");
		
//		parent.add(new Label("pageTitle", "Campaigns Administration Page"));

		dataView = campaignList("campaignsList");

		group = new CheckGroup<Campaign>("group", new ArrayList<Campaign>());
		group.add(dataView);
		group.add(new CheckGroupSelector("groupSelector"));

		footerNavigator = new PagingNavigator("footerPaginator", dataView);
		parent.add(footerNavigator);

		Form<Campaign> searchForm = searchCampaignForm(parent);
		parent.add(searchForm);

		searchForm.add(group);

		WebMarkupContainer searchButton = createButtonWithEffect(
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

		deleteDialog = deleteDialog(parent);
		parent.add(deleteDialog);

		deleteConfirmationDialog = deleteConfirmationDialog();
		parent.add(deleteConfirmationDialog);

		String newTitle = "Booktube - Campaigns Administration";
		super.get("pageTitle").setDefaultModelObject(newTitle);

	}

	private Dialog deleteDialog(final WebMarkupContainer parent) {

		final Dialog dialog = new Dialog("success_dialog");

		dialog.add(new Label("success_dialog_text", "Campaña eliminada!"));

		AjaxDialogButton ok = new AjaxDialogButton("OK") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				//setResponsePage(CampaignsAdministrationPage.class);
				dialog.close(target);
				target.add(parent);
			}
		};

		dialog.setButtons(ok);
		//dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close().render()));

		return dialog;

	}

	private Dialog deleteConfirmationDialog() {

		final Dialog dialog = new Dialog("delete_confirmation_dialog");

		dialog.add(new Label("delete_confirmation_dialog_text",
				"Esta seguro que desea eliminar la campaña?"));

		AjaxDialogButton yesButton = new AjaxDialogButton("Si") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				System.out.println("Borro mensaje");
				Campaign campaign = campaignService.getCampaign(campaignId);
				System.out.println("CAMPAIGN ES : " + campaign);
				campaignService.deleteCampaign(campaign);
				// JsScopeUiEvent.quickScope(deleteConfirmationdialog.close().render());
				JsScope.quickScope(dialog.close().render());
				// deleteConfirmationdialog.close(target);
				deleteDialog.open(target);
				// setResponsePage(MessagesAdministrationPage.class);
				dialog.close(target);
			}
		};

		DialogButton noButton = new DialogButton("No",
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
				//final String receivers = getReceivers(campaign);
				System.out.println("MESSAGE: " + campaign.getText());
				CompoundPropertyModel<Campaign> model = new CompoundPropertyModel<Campaign>(
						campaign);
				item.setDefaultModel(model);

				final PageParameters parameters = new PageParameters();
				parameters.set("messageId", campaign.getId());
				// item.add(new Label("id"));
				item.add(new Check<Campaign>("checkbox", item.getModel()));
//				item.add(new Label("id"));
				item.add(new Label("subject"));
				item.add(new Label("sender"));
				//item.add(new Label("receiver", receivers));
				item.add(new Label("date"));

				item.add(new Link<Campaign>("detailsLink", item.getModel()) {
					private static final long serialVersionUID = 1L;

					public void onClick() {
						setResponsePage(ShowMessagePage.class, parameters);
					}

				});
				item.add(new Link<Campaign>("editLink", item.getModel()) {
					private static final long serialVersionUID = 1L;

					public void onClick() {
						setResponsePage(ShowMessagePage.class, parameters);
						// setResponsePage(new EditWriterPage(user.getId(),
						// MessagePage.this));
					}

				});
				item.add(new AjaxLink<Campaign>("deleteLink", item.getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {

						Campaign campaign = (Campaign) getModelObject();
						campaignId = campaign.getId();
						// campaignService.deleteCampaign(campaign);
						// userService.deleteUser(message);
						// System.out.println("User " + messageId +
						// " deleted.");

						// setResponsePage(MessagesAdministrationPage.class);
						// dialog.open(target);
						deleteConfirmationDialog.open(target);
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

		final WebMarkupContainer searchFields = new WebMarkupContainer(
				"searchFields");
		searchFields.add(AttributeModifier.replace("style", "display: none;"));
		form.add(searchFields);
		
		searchFields.add(feedbackMessage);

		final TextField<Campaign> subject = new TextField<Campaign>("subject");
		searchFields.add(subject);

		final TextField<Campaign> campaignId = new TextField<Campaign>("id");
		searchFields.add(campaignId);

		final TextField<Campaign> sender = new TextField<Campaign>("sender");
		searchFields.add(sender);

		final TextField<Campaign> receiver = new TextField<Campaign>("receiver");
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

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				System.out.println("selected Campaign(s): "
						+ group.getDefaultModelObjectAsString());

				@SuppressWarnings("unchecked")
				List<Campaign> removedCampaigns = (List<Campaign>) group.getDefaultModelObject();

				for (Campaign aCampaign : removedCampaigns) {
					campaignService.deleteCampaign(aCampaign);
				}

				if (dataView.getItemCount() <= 0) {
					this.setVisible(false);
					footerNavigator.setVisible(false);
					feedbackMessage.setVisible(true);
				} else {
					this.setVisible(true);
					footerNavigator.setVisible(true);
					feedbackMessage.setVisible(false);
				}

				target.add(parent);

				// System.out.println("BOOKS: " + books);

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
					System.out.println("lowCampaignDate: "
							+ lowCampaignDate.getDefaultModelObjectAsString());
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

	private String getReceivers(Campaign campaign) {
		String receivers = "";
		for (CampaignDetail aCampaignDetail : campaign.getReceiver()) {
			if (aCampaignDetail.getReceiver() != null) {
				receivers += aCampaignDetail.getReceiver().getUsername() + ", ";
			}
		}
		return receivers;
	}

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
