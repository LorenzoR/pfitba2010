package com.booktube.persistence.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.booktube.model.Campaign;
import com.booktube.model.CampaignDetail;
import com.booktube.model.User;
import com.booktube.persistence.CampaignDao;

public class CampaignDaoImpl extends AbstractDaoHibernate<Campaign> implements CampaignDao {
	
	protected CampaignDaoImpl() {
		super(Campaign.class);
	}

	public Campaign getCampaign(Long id) {
		return (Campaign) getSession().getNamedQuery("campaign.id")
				.setLong("id", id).setMaxResults(1).uniqueResult();
	}

	public void updateCampaign(Campaign campaign) {
		super.saveOrUpdate(campaign);		
	}

	public void deleteCampaign(Campaign campaign) {
		super.delete(campaign);
	}

	public CampaignDetail getCampaignDetail(Campaign campaign, User receiver) {
		Criteria criteria = getSession().createCriteria(CampaignDetail.class)
				.add(Restrictions.eq("receiver", receiver))
				.add(Restrictions.eq("message", campaign));

		return (CampaignDetail) criteria.setMaxResults(1).uniqueResult();
	}

	public void insertCampaign(Campaign campaign) {
		super.insert(campaign);
	}

	@SuppressWarnings("unchecked")
	public List<Campaign> getAllCampaigns(int first, int count) {
		return (List<Campaign>) getSession().createCriteria(Campaign.class)
				.setFirstResult(first).setMaxResults(count).list();
	}

	public int countCampaigns() {
		Criteria criteria = getSession().createCriteria(Campaign.class);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
