package com.yuanluesoft.appraise.pojo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

import com.yuanluesoft.appraise.model.UnitAppraisesByCategory;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 评议管理:评议(appraise_appraise)
 * @author linchuan
 *
 */
public class Appraise extends Record {
	private String name; //名称
	private long taskId; //评议任务ID
	private String taskName; //评议任务名称
	private int isSpecial; //是否专题评议
	private int appraiserType; //评议员类型,0/基础,1/管理服务对象
	private long areaId; //地区ID
	private String area; //地区名称
	private int appraiseYear; //评议年度
	private int appraiseMonth; //评议月份
	private int voteTimes; //投票人次
	private long creatorId; //发起人ID
	private String creator; //发起人
	private Timestamp created; //发起时间
	private Timestamp endTime; //截止时间

	private Set shortMessages; //短信列表
	private Set unitAppraises; //单位评议列表
	
	/**
	 * 获取本期投票数
	 * @return
	 */
	public int getVoteTotal() {
		return (int)total("voteTotal", false);
	}
	
	/**
	 * 获取本期短信投票数
	 * @return
	 */
	public int getSmsVoteTotal() {
		return (int)total("smsVoteTotal", false);
	}
	
	/**
	 * 获取本期网络投票数
	 * @return
	 */
	public int getInternetVoteTotal() {
		return (int)total("internetVoteTotal", false);
	}
	
	/**
	 * 获取本期综合得分
	 * @return
	 */
	public double getScoreComprehensive() {
		return total("scoreComprehensive", true);
	}
	
	/**
	 * 获取累计投票数,截止到本期
	 * @return
	 */
	public int getYearVoteTotal() {
		return (int)total("yearVoteTotal", false);
	}
	
	/**
	 * 获取累计短信投票数,截止到本期
	 * @return
	 */
	public int getYearSmsVoteTotal() {
		return (int)total("yearSmsVoteTotal", false);
	}
	
	/**
	 * 获取累计网络投票数,截止到本期
	 * @return
	 */
	public int getYearInternetVoteTotal() {
		return (int)total("yearInternetVoteTotal", false);
	}
	
	/**
	 * 获取累计综合得分,截止到本期
	 * @return
	 */
	public double getYearScoreComprehensive() {
		return total("yearScoreComprehensive", true);
	}
	
	/**
	 * 统计
	 * @param propertyName
	 * @return
	 */
	private double total(String propertyName, boolean average) {
		double total = 0;
		try {
			for(Iterator iterator = unitAppraises.iterator(); iterator.hasNext();) {
				UnitAppraise unitAppraise = (UnitAppraise)iterator.next();
				total += ((Number)PropertyUtils.getProperty(unitAppraise, propertyName)).doubleValue();
			}
			if(average) {
				total /= unitAppraises.size();
			}
		}
		catch(Exception e) {

		}
		return total;
	}

	
	/**
	 * 获取选项统计列表
	 * @return
	 */
	public Set getOptionVotes() {
		Set voteTotals = null;
		try {
			for(Iterator iterator = unitAppraises.iterator(); iterator.hasNext();) {
				UnitAppraise unitAppraise = (UnitAppraise)iterator.next();
				if(unitAppraise.getOptionVotes()==null || unitAppraise.getOptionVotes().isEmpty()) {
					continue;
				}
				if(voteTotals==null) {
					voteTotals = new LinkedHashSet();
					for(Iterator iteratorVote = unitAppraise.getOptionVotes().iterator(); iteratorVote.hasNext();) {
						AppraiseOptionVote optionVote = (AppraiseOptionVote)iteratorVote.next();
						AppraiseOptionVote voteTotal = new AppraiseOptionVote();
						PropertyUtils.copyProperties(voteTotal, optionVote);
						voteTotals.add(voteTotal);
					}
					continue;
				}
				for(Iterator iteratorVote = voteTotals.iterator(); iteratorVote.hasNext();) {
					AppraiseOptionVote voteTotal = (AppraiseOptionVote)iteratorVote.next();
					AppraiseOptionVote optionVote = (AppraiseOptionVote)ListUtils.findObjectByProperty(unitAppraise.getOptionVotes(), "optionId", new Long(voteTotal.getOptionId()));
					if(optionVote==null) {
						continue;
					}
					voteTotal.setVoteTotal(voteTotal.getVoteTotal() + optionVote.getVoteTotal()); //投票数
					voteTotal.setSmsVoteTotal(voteTotal.getSmsVoteTotal() + optionVote.getSmsVoteTotal()); //短信投票数
					voteTotal.setInternetVoteTotal(voteTotal.getInternetVoteTotal() + optionVote.getInternetVoteTotal()); //网络投票数
				}
			}
		}
		catch(Exception e) {

		}
		return voteTotals;
	}
	
	/**
	 * 单位评议(按分类)列表
	 * @return
	 */
	public List getUnitAppraisesByCategory() {
		List categories = new ArrayList();
		for(Iterator iterator = unitAppraises.iterator(); iterator.hasNext();) {
			UnitAppraise unitAppraise = (UnitAppraise)iterator.next();
			UnitAppraisesByCategory unitAppraisesByCategory = (UnitAppraisesByCategory)ListUtils.findObjectByProperty(categories, "category", unitAppraise.getUnitCategory());
			if(unitAppraisesByCategory==null) {
				unitAppraisesByCategory = new UnitAppraisesByCategory();
				unitAppraisesByCategory.setCategory(unitAppraise.getUnitCategory());
				unitAppraisesByCategory.setUnitAppraises(new LinkedHashSet());
				categories.add(unitAppraisesByCategory);
			}
			unitAppraisesByCategory.getUnitAppraises().add(unitAppraise);
		}
		return categories;
	}
	
	/**
	 * @return the appraiseMonth
	 */
	public int getAppraiseMonth() {
		return appraiseMonth;
	}
	/**
	 * @param appraiseMonth the appraiseMonth to set
	 */
	public void setAppraiseMonth(int appraiseMonth) {
		this.appraiseMonth = appraiseMonth;
	}
	/**
	 * @return the appraiserType
	 */
	public int getAppraiserType() {
		return appraiserType;
	}
	/**
	 * @param appraiserType the appraiserType to set
	 */
	public void setAppraiserType(int appraiserType) {
		this.appraiserType = appraiserType;
	}
	/**
	 * @return the appraiseYear
	 */
	public int getAppraiseYear() {
		return appraiseYear;
	}
	/**
	 * @param appraiseYear the appraiseYear to set
	 */
	public void setAppraiseYear(int appraiseYear) {
		this.appraiseYear = appraiseYear;
	}
	/**
	 * @return the areaId
	 */
	public long getAreaId() {
		return areaId;
	}
	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	/**
	 * @return the created
	 */
	public Timestamp getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the endTime
	 */
	public Timestamp getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the shortMessages
	 */
	public Set getShortMessages() {
		return shortMessages;
	}
	/**
	 * @param shortMessages the shortMessages to set
	 */
	public void setShortMessages(Set shortMessages) {
		this.shortMessages = shortMessages;
	}
	/**
	 * @return the taskId
	 */
	public long getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName;
	}
	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	/**
	 * @return the unitAppraises
	 */
	public Set getUnitAppraises() {
		return unitAppraises;
	}
	/**
	 * @param unitAppraises the unitAppraises to set
	 */
	public void setUnitAppraises(Set unitAppraises) {
		this.unitAppraises = unitAppraises;
	}
	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}
	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the isSpecial
	 */
	public int getIsSpecial() {
		return isSpecial;
	}

	/**
	 * @param isSpecial the isSpecial to set
	 */
	public void setIsSpecial(int isSpecial) {
		this.isSpecial = isSpecial;
	}

	/**
	 * @return the voteTimes
	 */
	public int getVoteTimes() {
		return voteTimes;
	}

	/**
	 * @param voteTimes the voteTimes to set
	 */
	public void setVoteTimes(int voteTimes) {
		this.voteTimes = voteTimes;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}