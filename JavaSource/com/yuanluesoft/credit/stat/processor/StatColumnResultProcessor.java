package com.yuanluesoft.credit.stat.processor;



import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordListData;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.credit.stat.pojo.Datas;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.usermanage.service.OrgService;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;

public class StatColumnResultProcessor extends RecordListProcessor {
	private DatabaseService databaseService;
	private OrgService orgService ;
	private String defaultUnitId;
	private String defaultUnitName;
	private String defaultSiteId;
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		List dateList = new ArrayList();
		String orgIds = "";
		long id = RequestUtils.getParameterLongValue(request, "id");
		String unitName = RequestUtils.getParameterStringValue(request, "unitName");
		long siteId = RequestUtils.getParameterLongValue(request, "siteId");
		if(id==0){
			id = Long.valueOf(defaultUnitId).longValue();
		}
		if(unitName==null || unitName.equals("")){
			unitName = defaultUnitName;
		}
		if(siteId==0){
			siteId = Long.valueOf(defaultUnitId).longValue();
		}
		String hql = "from Person Person where Person.id = "+id;
		String userIds = "";
		if(databaseService.findRecordsByHql(hql)!=null){//id是用户ID
			userIds = id+"";
			orgIds = orgService.listOrgIdsOfPerson("" + userIds, true);
		}else{
			hql = "from PersonSubjection PersonSubjection where PersonSubjection.orgId="+id;
			List personSubjections = databaseService.findRecordsByHql(hql);
			userIds = ListUtils.join(personSubjections, "personId", ",", false);//该部门下的所有用户
			orgIds = orgService.listOrgIdsOfPerson("" + userIds, true);
		}
		hql = "from WebDirectory WebDirectory where WebDirectory.id in(select WebDirectorySubjection.directoryId from WebDirectorySubjection WebDirectorySubjection where WebDirectorySubjection.parentDirectoryId="+siteId+" and WebDirectorySubjection.directoryId <> "+siteId+") and WebDirectory.id in (select WebDirectoryPopedom.directoryId from WebDirectoryPopedom WebDirectoryPopedom where WebDirectoryPopedom.userId in("+userIds+") or WebDirectoryPopedom.userId  in("+orgIds+"))";
		
		List webDirectorys = databaseService.findRecordsByHql(hql);
		if(webDirectorys!=null){
			for (Iterator iter = webDirectorys.iterator(); iter.hasNext();) {
				WebDirectory element = (WebDirectory) iter.next();
				hql = " select count(SiteResource.id) from SiteResource SiteResource where SiteResource.editorId in("+userIds+") and SiteResource.id in (select SiteResourceSubjection.resourceId from SiteResourceSubjection SiteResourceSubjection where SiteResourceSubjection.siteId ="+element.getId()+")";
				Datas datas = new Datas();
				datas.setNum(((Number)databaseService.findRecordByHql(hql)).intValue());
				datas.setStatObj(element.getDirectoryName());
				datas.setFlag("column");//栏目
				datas.setUnitId(id);
				datas.setUnitName(unitName);
				hql = " select max(SiteResource.created) from SiteResource SiteResource where SiteResource.editorId in("+userIds+") and SiteResource.id in (select SiteResourceSubjection.resourceId from SiteResourceSubjection SiteResourceSubjection where SiteResourceSubjection.siteId ="+element.getId()+")";
				try {
					datas.setNewesIssueTime((Timestamp)databaseService.findRecordByHql(hql));
				} catch (Exception e) {
					datas.setNewesIssueTime(null);
				}
				dateList.add(datas);
			}
	//		排序
			Collections.sort(dateList, 
					 new Comparator() {
						public int compare(Object o1, Object o2) {
							try {
								Datas obj1 = (Datas)o1;
								Datas obj2 = (Datas)o2;
								int result = Integer.valueOf(obj2.getNum()).intValue()-Integer.valueOf(obj1.getNum()).intValue() ;
								return result;
							} catch (Exception e) {
								return 0;
							}
						}
					});
		}
		return new RecordListData(dateList);
	}
	public DatabaseService getDatabaseService() {
		return databaseService;
	}
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
	public String getDefaultUnitId() {
		return defaultUnitId;
	}
	public void setDefaultUnitId(String defaultUnitId) {
		this.defaultUnitId = defaultUnitId;
	}
	public String getDefaultUnitName() {
		return defaultUnitName;
	}
	public void setDefaultUnitName(String defaultUnitName) {
		this.defaultUnitName = defaultUnitName;
	}
	public String getDefaultSiteId() {
		return defaultSiteId;
	}
	public void setDefaultSiteId(String defaultSiteId) {
		this.defaultSiteId = defaultSiteId;
	}
	public OrgService getOrgService() {
		return orgService;
	}
	public void setOrgService(OrgService orgService) {
		this.orgService = orgService;
	}
	
	
}
