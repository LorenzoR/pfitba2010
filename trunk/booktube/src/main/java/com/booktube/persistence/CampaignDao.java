package com.booktube.persistence;

import java.util.List;

import com.booktube.model.Campaign;
import com.booktube.model.CampaignDetail;
import com.booktube.model.User;

public interface CampaignDao {

	public Campaign getCampaign(Long id);
	public void updateCampaign(Campaign campaign);
    public void deleteCampaign(Campaign campaign);
    public CampaignDetail getCampaignDetail(Campaign campaign, User receiver);
    public void insertCampaign(Campaign campaign);
	public List<Campaign> getAllCampaigns(int first, int count);
	public int countCampaigns();
	
}
