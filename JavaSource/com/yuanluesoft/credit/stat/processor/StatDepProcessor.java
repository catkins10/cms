package com.yuanluesoft.credit.stat.processor;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.credit.stat.pojo.CreditStat;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.usermanage.pojo.Person;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.model.View;

public class StatDepProcessor extends RecordListProcessor {
	private String unitIds;//要统计的单位ID
	private String depIds;//部门ID
	private DatabaseService databaseService;
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		// TODO 自动生成方法存根
		String[] ids = unitIds.split(",");
		List recordList = new ArrayList();
		for (int i = 0; i < ids.length; i++) {
			String hql = "from OrgSubjection OrgSubjection where OrgSubjection.parentDirectoryId = "+ids[i]+" and OrgSubjection.directoryId <>"+ids[i];
			
			List orgSubjections = databaseService.findRecordsByHql(hql);
			hql = "from Org Org where Org.id in("+ListUtils.join(orgSubjections, "directoryId", ",", false)+")";
			List orgList = databaseService.findRecordsByHql(hql);
			for (Iterator iter = orgList.iterator(); iter.hasNext();) {
				Org org = (Org) iter.next();
				CreditStat creditStat = new CreditStat();
				creditStat.setId(org.getId());
				creditStat.setUnitName(org.getDirectoryName());
				recordList.add(creditStat);
			}
			
		}
		
		String hql = "from Org Org where Org.id in("+depIds+")";
		List departments = databaseService.findRecordsByHql(hql);
		for (Iterator iter = departments.iterator(); iter.hasNext();) {
			Org org = (Org) iter.next();
			hql = "from PersonSubjection PersonSubjection where PersonSubjection.orgId="+org.getId();
			List personSubjections = databaseService.findRecordsByHql(hql);
			String userIds = ListUtils.join(personSubjections, "personId", ",", false);//该部门下的所有用户
			hql = "from Person Person where Person.id in("+userIds+")";
			List persons = databaseService.findRecordsByHql(hql);
			for (Iterator iterator = persons.iterator(); iterator.hasNext();) {
				Person element = (Person) iterator.next();
				CreditStat creditStat = new CreditStat();
				creditStat.setId(element.getId());
				creditStat.setUnitName(element.getName());
				recordList.add(creditStat);
			}
		}
		
		return new RecordListData(recordList);
	}
	public String getUnitIds() {
		return unitIds;
	}
	public void setUnitIds(String unitIds) {
		this.unitIds = unitIds;
	}
	public String getDepIds() {
		return depIds;
	}
	public void setDepIds(String depIds) {
		this.depIds = depIds;
	}
	public DatabaseService getDatabaseService() {
		return databaseService;
	}
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
	
}
