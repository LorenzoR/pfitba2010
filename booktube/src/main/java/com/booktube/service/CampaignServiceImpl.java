package com.booktube.service;

import java.util.Date;
import java.util.List;

import com.booktube.model.Campaign;
import com.booktube.model.CampaignDetail;
import com.booktube.model.User;
import com.booktube.persistence.CampaignDao;

public class CampaignServiceImpl implements CampaignService {

	private CampaignDao itemDao;
	
	public CampaignServiceImpl() {
		
	}
	
	public CampaignDao getItemDao() {
		return itemDao;
	}
	
	public void setItemDao(CampaignDao itemDao) {
		this.itemDao = itemDao;
	}
	
	public Campaign getCampaign(Long id) {
		return itemDao.getCampaign(id);
	}

	public void updateCampaign(Campaign campaign) {
		itemDao.updateCampaign(campaign);
	}

	public void deleteCampaign(Campaign campaign) {
		itemDao.deleteCampaign(campaign);
	}

	public CampaignDetail getCampaignDetail(Campaign campaign, User receiver) {
		return itemDao.getCampaignDetail(campaign, receiver);
	}

	public void insertCampaign(Campaign campaign) {
		itemDao.insertCampaign(campaign);
	}

	public List<Campaign> getAllCampaigns(int first, int count) {
		return itemDao.getAllCampaigns(first, count);
	}

	public int countCampaigns() {
		return itemDao.countCampaigns();
	}

	public List<Campaign> getAllCampaignsTo(User user, int first, int count) {
		return itemDao.getAllCampaignsTo(user, first, count);
	}

	public int countCampaignsTo(User user) {
		return itemDao.countCampaignsTo(user);
	}

	public int countUnreadCampaignsTo(User user) {
		return itemDao.countUnreadCampaignsTo(user);
	}

	public void sendCampaign(Campaign campaign, List<User> receivers) {
		itemDao.sendCampaign(campaign, receivers);
	}

	public List<Campaign> getCampaigns(int first, int count, String subject,
			User sender, User receiver, Date lowDate, Date highDate) {
		return itemDao.getCampaings(first, count, subject, sender, receiver, lowDate, highDate);
	}

}
