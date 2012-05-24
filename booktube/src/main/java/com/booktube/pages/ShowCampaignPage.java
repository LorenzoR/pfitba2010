package com.booktube.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.WiaSession;
import com.booktube.model.Book;
import com.booktube.model.Campaign;
import com.booktube.model.CampaignDetail;
import com.booktube.model.User;
import com.booktube.service.CampaignService;

public class ShowCampaignPage extends BasePage {

	@SpringBean
	CampaignService campaignService;

	private User user;
	private final Campaign campaign;

	public ShowCampaignPage(final PageParameters pageParameters) {

		user = WiaSession.get().getLoggedInUser();
		Long campaignId = pageParameters.get("campaignId").toLong();

		campaign = campaignService.getCampaign(campaignId);

		final WebMarkupContainer parent = new WebMarkupContainer(
				"campaignDetails");
		parent.setOutputMarkupId(true);
		add(parent);

		String newTitle = "Booktube - Campaign " + campaign.getSubject();
		super.get("pageTitle").setDefaultModelObject(newTitle);
		
		//List<Campaign> campaignList = campaign.getAllAnswers();
		List<Campaign> campaignList = new ArrayList<Campaign>();
		campaignList.add(campaign);
		
		//List<Message> messageList = getAnswers(message);
		
		//Collections.sort(messageList, Message.getDateComparator());
		
		ListView<Campaign> listview = new ListView<Campaign>("campaignList", campaignList) {
			protected void populateItem(ListItem<Campaign> item) {
				final Campaign campaign = (Campaign) item.getModelObject();
				CompoundPropertyModel<Campaign> model = new CompoundPropertyModel<Campaign>(campaign);
				item.setDefaultModel(model);
				
				item.add(new Label("subject"));
				item.add(new Label("sender"));
				item.add(new Label("date"));
				item.add(new Label("text"));
			}
		};
		
		parent.add(listview);
		
		campaignService.getCampaignDetail(campaign, user).setRead(true);
		campaignService.updateCampaign(campaign);
		
		//campaign.setRead(true);
		//messageService.updateMessage(message);
		

	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
	}

}
