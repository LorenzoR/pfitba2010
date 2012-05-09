package com.booktube.service;

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

}
