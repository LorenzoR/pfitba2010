package com.booktube.pages;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.WiaSession;
import com.booktube.model.Campaign;
import com.booktube.model.CampaignDetail;
import com.booktube.model.User;
import com.booktube.model.User.Level;
import com.booktube.service.CampaignService;

public class ShowCampaignPage extends BasePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	CampaignService campaignService;

	private final User user;
	private final Campaign campaign;

	public ShowCampaignPage(final PageParameters pageParameters) {

		Long campaignId = null;
		
		user = WiaSession.get().getLoggedInUser();
		
		if ( user == null || pageParameters.get("campaignId") == null ) {
			throw new AbortWithHttpErrorCodeException(404);
		}
		else {
			campaignId = pageParameters.get("campaignId").toLong();
		}

		campaign = campaignService.getCampaign(campaignId);

		
		if ( user.getLevel() == Level.ADMIN ) {
			addBreadcrumb(new BookmarkablePageLink<Object>("link", AdministrationPage.class), new ResourceModel("administrationPageTitle").getObject());
			addBreadcrumb(new BookmarkablePageLink<Object>("link", CampaignsAdministrationPage.class), new ResourceModel("campaigns").getObject());
		}
		else {
			addBreadcrumb(new BookmarkablePageLink<Object>("link", CampaignsPage.class, pageParameters), new ResourceModel("campaigns").getObject());
		}
		
		addBreadcrumb(new BookmarkablePageLink<Object>("link", ShowCampaignPage.class, pageParameters), campaign.getSubject());
		
		final WebMarkupContainer parent = new WebMarkupContainer(
				"campaignDetails");
		parent.setOutputMarkupId(true);
		add(parent);

		String newTitle = "Booktube - " + new ResourceModel("campaign").getObject() + " " + campaign.getSubject();
		super.get("pageTitle").setDefaultModelObject(newTitle);

		// List<Campaign> campaignList = campaign.getAllAnswers();
		List<Campaign> campaignList = new ArrayList<Campaign>();
		campaignList.add(campaign);

		// List<Message> messageList = getAnswers(message);

		// Collections.sort(messageList, Message.getDateComparator());

		ListView<Campaign> listview = new ListView<Campaign>("campaignList",
				campaignList) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(ListItem<Campaign> item) {
				final Campaign campaign = (Campaign) item.getModelObject();
				CompoundPropertyModel<Campaign> model = new CompoundPropertyModel<Campaign>(
						campaign);
				item.setDefaultModel(model);
				
				final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, getLocale());

				item.add(new Label("subject"));
				item.add(new Label("sender"));
				item.add(new Label("date", dateFormat.format(campaign.getDate())));
				item.add(new Label("text"));
				
				List<CampaignDetail> campaignDetailList = new ArrayList<CampaignDetail>(campaign.getReceiver());
				
				ListView<CampaignDetail> listview = new ListView<CampaignDetail>("receiverList", campaignDetailList) {
					private static final long serialVersionUID = 1L;

					protected void populateItem(ListItem<CampaignDetail> item) {
						final CampaignDetail campaignDetail = (CampaignDetail) item.getModelObject();
						CompoundPropertyModel<CampaignDetail> model = new CompoundPropertyModel<CampaignDetail>(
								campaignDetail);
						item.setDefaultModel(model);
						
						item.add(new Label("username", campaignDetail.getReceiver().getUsername()));

					}
				};
				
				item.add(listview);
			}
		};

		parent.add(listview);

		if (user != null && user.getLevel() != Level.ADMIN && user.getLevel() != Level.OPERATOR) {
			campaignService.getCampaignDetail(campaign, user).setRead(true);
			campaignService.updateCampaign(campaign);
		}

		// campaign.setRead(true);
		// messageService.updateMessage(message);

	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
	}

}
