package com.booktube.service;

import java.util.Date;
import java.util.List;

import com.booktube.model.Campaign;
import com.booktube.model.CampaignDetail;
import com.booktube.model.User;

public interface CampaignService {

	public Campaign getCampaign(Long id);
	public void updateCampaign(Campaign campaign);
    public void deleteCampaign(Campaign campaign);
    public CampaignDetail getCampaignDetail(Campaign campaign, User receiver);
    public void insertCampaign(Campaign campaign);
	public List<Campaign> getAllCampaigns(int first, int count);
	public List<Campaign> getAllCampaignsTo(User user, int first, int count);
	public List<Campaign> getCampaigns(int first, int count, Long campaignId, String subject, String sender, String receiver, Date lowDate, Date highDate);
	public int countCampaigns();
	public int countCampaignsTo(User user);
	public int countUnreadCampaignsTo(User user);
	public int getCount(Long campaignId, String subject, String sender, String receiver, Date lowDate, Date highDate);
	public void sendCampaign(Campaign campaign, List<User> receivers);
}
