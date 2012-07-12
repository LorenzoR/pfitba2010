package com.booktube.pages;

import java.util.Iterator;

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

import com.booktube.WiaSession;
import com.booktube.model.Campaign;
import com.booktube.model.User;
import com.booktube.service.CampaignService;

public class CampaignsPage extends BasePage {
	
	private static final long serialVersionUID = 1L;

	@SpringBean
	CampaignService campaignService;
	
	private User user;
	
	public static final int MESSAGES_PER_PAGE = 5;
	
	public CampaignsPage() {
		user = WiaSession.get().getLoggedInUser();
		System.out.println("User : " + user);
		//User user = new User("username", "firstname", "lastname");
		//userService.insertUser(user);
		//WicketApplication.instance().getUserService().insertUser(user);
		//List<User> users = WicketApplication.instance().getUserService()
		//List<User> users = userService.getAllUsers(0, Integer.MAX_VALUE);
		final WebMarkupContainer parent = new WebMarkupContainer("campaigns");
		parent.setOutputMarkupId(true);
		add(parent);

		//parent.add(listWriters("writerList", users));
		DataView<Campaign> dataView = campaignList("campaignList");

		parent.add(dataView);
		parent.add(new PagingNavigator("footerPaginator", dataView));

	}

	private DataView<Campaign> campaignList(String label) {

		IDataProvider<Campaign> dataProvider = new CampaignProvider();
		
		DataView<Campaign> dataView = new DataView<Campaign>(label, dataProvider,
				MESSAGES_PER_PAGE) {
			
					private static final long serialVersionUID = 1L;

			protected void populateItem(Item<Campaign> item) {
				final Campaign campaign = (Campaign) item.getModelObject();
				System.out.println("MESSAGE: " + campaign.getText());
				CompoundPropertyModel<Campaign> model = new CompoundPropertyModel<Campaign>(campaign);
				item.setDefaultModel(model);
				final PageParameters parameters = new PageParameters();
				parameters.set("campaignId", campaign.getId());
				//item.add(new Label("id"));
				if ( campaignService.getCampaignDetail(campaign, user).isRead() ) {
					item.add(new Label("subject"));
					item.add(new Label("sender"));
					item.add(new Label("date"));
				}
				else {
					item.add(new Label("subject", "<b>" + campaign.getSubject() + "</b>").setEscapeModelStrings(false));
					item.add(new Label("sender", "<b>" + campaign.getSender() + "</b>").setEscapeModelStrings(false));
					item.add(new Label("date", "<b>" + campaign.getDate() + "</b>").setEscapeModelStrings(false));
				}
				
				item.add(new Link<Campaign>("detailsLink", item.getModel()) {
					private static final long serialVersionUID = 1L;

					public void onClick() {
						setResponsePage(ShowCampaignPage.class, parameters);
					}

				});
				item.add(new Link<Campaign>("deleteLink", item.getModel()) {
					private static final long serialVersionUID = -7155146615720218460L;

					public void onClick() {

						Campaign campaign = (Campaign) getModelObject();
						//Long campaignId = campaign.getId();

						campaignService.deleteCampaign(campaign);
						//System.out.println("User " + messageId + " deleted.");

						setResponsePage(CampaignsPage.this);
					}

				});
			}
			
		};
	
		return dataView;
	}
	
	class CampaignProvider implements IDataProvider<Campaign> {

		private static final long serialVersionUID = 1L;

		public CampaignProvider() {
		}

		public Iterator<Campaign> iterator(int first, int count) {
			return campaignService.getAllCampaignsTo(user, first, count).iterator();
		}

		public int size() {
			return campaignService.countCampaignsTo(user);
		}

		public IModel<Campaign> model(Campaign campaign) {
			return new CompoundPropertyModel<Campaign>(campaign);
		}

		public void detach() {
			// TODO Auto-generated method stub

		}
	}

	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub
		String newTitle = "Booktube - Campaigns"; 
		super.get("pageTitle").setDefaultModelObject(newTitle);
	}


}
