package com.booktube.pages;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.dialog.AjaxDialogButton;
import org.odlabs.wiquery.ui.dialog.Dialog;
import org.odlabs.wiquery.ui.dialog.DialogButton;

import com.booktube.model.Campaign;
import com.booktube.model.CampaignDetail;
import com.booktube.pages.MessagesAdministrationPage.MessageProvider;
import com.booktube.service.CampaignService;
import com.booktube.service.MessageService;

public class CampaignsAdministrationPage extends AdministrationPage{	
	private static final long serialVersionUID = 3572068607555159574L;
	
	@SpringBean
	CampaignService campaignService;

	private static Dialog deleteDialog;
	private static Dialog deleteConfirmationDialog;

	public static final int MESSAGES_PER_PAGE = 5;
	
	private static Long campaignId;

	public CampaignsAdministrationPage() {
		super();
		final WebMarkupContainer parent = new WebMarkupContainer(
				"campaignsContainer");
		parent.setOutputMarkupId(true);
		add(parent);

		parent.add(new Label("pageTitle", "Campaigns Administration Page"));

		DataView<Campaign> dataView = campaignList("campaignsList");

		parent.add(dataView);
		parent.add(new PagingNavigator("footerPaginator", dataView));
		
		deleteDialog = deleteDialog();
		parent.add(deleteDialog);
		
		deleteConfirmationDialog = deleteConfirmationDialog();
		parent.add(deleteConfirmationDialog);

		String newTitle = "Booktube - Campaigns Administration";
		super.get("pageTitle").setDefaultModelObject(newTitle);

	}

	private Dialog deleteDialog() {

		Dialog dialog = new Dialog("success_dialog");
		
		dialog.add(new Label("success_dialog_text", "Mensaje eliminado!"));
		
		AjaxDialogButton ok = new AjaxDialogButton("OK") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				setResponsePage(MessagesAdministrationPage.class);

			}
		};

		dialog.setButtons(ok);
		dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close().render()));
		
		return dialog;

	}

	private Dialog deleteConfirmationDialog() {

		final Dialog dialog = new Dialog("delete_confirmation_dialog");

		dialog.add(new Label("delete_confirmation_dialog_text", "Esta seguro que desea eliminar el mensaje?"));
		
		AjaxDialogButton yesButton = new AjaxDialogButton("Si") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onButtonClicked(AjaxRequestTarget target) {
				System.out.println("Borro mensaje");
				Campaign campaign = campaignService.getCampaign(campaignId);
				campaignService.deleteCampaign(campaign);
				// JsScopeUiEvent.quickScope(deleteConfirmationdialog.close().render());
				JsScope.quickScope(dialog.close().render());
				// deleteConfirmationdialog.close(target);
				deleteDialog.open(target);
				// setResponsePage(MessagesAdministrationPage.class);

			}
		};
		
		DialogButton noButton = new DialogButton("No", 
                JsScope.quickScope(dialog.close().render()));

		dialog.setButtons(yesButton, noButton);
		dialog.setCloseEvent(JsScopeUiEvent.quickScope(dialog.close()));

		return dialog;

	}

	private DataView<Campaign> campaignList(String label) {

		IDataProvider<Campaign> dataProvider = new CampaignProvider();

		DataView<Campaign> dataView = new DataView<Campaign>("campaignsList",
				dataProvider, MESSAGES_PER_PAGE) {

			protected void populateItem(Item<Campaign> item) {
				final Campaign campaign = (Campaign) item.getModelObject();
				final String receivers = getReceivers(campaign);
				System.out.println("MESSAGE: " + campaign.getText());
				CompoundPropertyModel<Campaign> model = new CompoundPropertyModel<Campaign>(
						campaign);
				item.setDefaultModel(model);
				final PageParameters parameters = new PageParameters();
				parameters.set("messageId", campaign.getId());
				// item.add(new Label("id"));
				item.add(new Label("subject"));
				item.add(new Label("sender"));
				item.add(new Label("receiver", receivers));
				item.add(new Label("date"));

				item.add(new Link("detailsLink", item.getModel()) {
					public void onClick() {
						setResponsePage(ShowMessagePage.class, parameters);
					}

				});
				item.add(new Link("editLink", item.getModel()) {
					public void onClick() {
						setResponsePage(ShowMessagePage.class, parameters);
						// setResponsePage(new EditWriterPage(user.getId(),
						// MessagePage.this));
					}

				});
				item.add(new AjaxLink<Campaign>("deleteLink", item.getModel()) {

					@Override
					public void onClick(AjaxRequestTarget target) {

						Campaign campaign = (Campaign) getModelObject();
						campaignId = campaign.getId();
						campaignService.deleteCampaign(campaign);
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

	private String getReceivers(Campaign campaign) {
		String receivers = "";
		for (CampaignDetail aCampaignDetail : campaign.getReceiver()) {
			if ( aCampaignDetail.getReceiver() != null ) {
				receivers += aCampaignDetail.getReceiver().getUsername() + ", ";
			}
		}
		return receivers;
	}

	class CampaignProvider implements IDataProvider<Campaign> {

		private List<Campaign> campaigns;

		public CampaignProvider() {
		}

		public Iterator<Campaign> iterator(int first, int count) {

			this.campaigns = campaignService.getAllCampaigns(first, count);
			return this.campaigns.iterator();
		}

		public int size() {
			return messageService.countCampaigns();
		}

		public IModel<Campaign> model(Campaign message) {
			return new CompoundPropertyModel<Campaign>(message);
		}

		public void detach() {
			// TODO Auto-generated method stub

		}
	}
}
