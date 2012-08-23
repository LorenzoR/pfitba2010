package com.booktube.persistence.hibernate;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.booktube.model.Campaign;
import com.booktube.model.CampaignDetail;
import com.booktube.model.User;
import com.booktube.model.User.Level;
import com.booktube.persistence.CampaignDao;

public class CampaignDaoImpl extends AbstractDaoHibernate<Campaign> implements
		CampaignDao {

	protected CampaignDaoImpl() {
		super(Campaign.class);
	}

	public Campaign getCampaign(Long id) {
		return (Campaign) getSession().getNamedQuery("campaign.id")
				.setLong("id", id).setMaxResults(1).uniqueResult();
	}

	public void updateCampaign(Campaign campaign) {
		// super.saveOrUpdate(campaign);
		super.update(campaign);
		Logger.getLogger("booktube").info("Campaign " + campaign.getId() + " updated.");
	}

	public void deleteCampaign(Campaign campaign) {
		Long id = campaign.getId();
		super.delete(campaign);
		Logger.getLogger("booktube").info("Campaign " + id + " deleted.");
	}

	public CampaignDetail getCampaignDetail(Campaign campaign, User receiver) {
		Criteria criteria = getSession().createCriteria(CampaignDetail.class)
				.add(Restrictions.eq("receiver", receiver))
				.add(Restrictions.eq("campaign", campaign));

		return (CampaignDetail) criteria.setMaxResults(1).uniqueResult();
	}

	public void insertCampaign(Campaign campaign) {
		super.insert(campaign);
		Logger.getLogger("booktube").info("Campaign " + campaign.getId() + " inserted.");
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

	@SuppressWarnings("unchecked")
	public List<Campaign> getAllCampaignsTo(User user, int first, int count) {
		return (List<Campaign>) getSession().createCriteria(Campaign.class)
				.createCriteria("receiver")
				.add(Restrictions.eq("receiver", user)).setFirstResult(first)
				.setMaxResults(count).list();
	}

	public int countCampaignsTo(User user) {
		Criteria criteria = getSession().createCriteria(Campaign.class)
				.createCriteria("receiver")
				.add(Restrictions.eq("receiver", user));
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	public int countUnreadCampaignsTo(User user) {
		Criteria criteria = getSession().createCriteria(Campaign.class)
				.createCriteria("receiver")
				.add(Restrictions.eq("receiver", user))
				.add(Restrictions.eq("isRead", false));
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	public void sendCampaign(Campaign campaign, List<User> receivers) {
		for (User aUser : receivers) {
			if (aUser.getLevel() == Level.USER) {
				campaign.addReceiver(new CampaignDetail(aUser, campaign));
			}
		}
		
		if (campaign.getReceiver().size() > 0) {
			insertCampaign(campaign);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Campaign> getCampaings(int first, int count, Long campaignId,
			String subject, String sender, String receiver, Date lowDate,
			Date highDate) {
		return (List<Campaign>) createCriteria(campaignId, subject, sender,
				receiver, lowDate, highDate).setFirstResult(first)
				.setMaxResults(count).list();
	}

	public int getCount(Long campaignId, String subject, String sender,
			String receiver, Date lowDate, Date highDate) {
		Criteria criteria = createCriteria(campaignId, subject, sender,
				receiver, lowDate, highDate);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	private Criteria createCriteria(Long campaignId, String subject,
			String sender, String receiver, Date lowDate, Date highDate) {
		Criteria criteria = getSession().createCriteria(Campaign.class);

		if (campaignId != null) {
			criteria.add(Restrictions.eq("id", campaignId));
		}

		if (!StringUtils.isBlank(subject)) {
			criteria.add(Restrictions.ilike("subject", "%" + subject + "%"));
		}

		if (StringUtils.isNotBlank(sender)) {
			criteria.createCriteria("sender").add(
					Restrictions.eq("username", sender));
		}

		if (StringUtils.isNotBlank(receiver)) {
			criteria.createCriteria("receiver").createCriteria("receiver")
					.add(Restrictions.eq("username", receiver));
		}

		return criteria;
	}

}
