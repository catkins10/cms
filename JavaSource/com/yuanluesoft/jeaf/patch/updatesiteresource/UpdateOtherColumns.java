package com.yuanluesoft.jeaf.patch.updatesiteresource;

import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.cms.sitemanage.service.SiteService;
import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.cms.siteresource.pojo.SiteResourceSubjection;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 更新其他栏目列表
 * @author linchuan
 *
 */
public class UpdateOtherColumns {
	
	/**
	 * 更新其他栏目列表
	 * @throws Exception
	 */
	public void updateOtherColumns() throws Exception {
		DatabaseService databaseService = (DatabaseService)Environment.getService("databaseService");
		SiteService siteService = (SiteService)Environment.getService("siteService");
		String hql = "select SiteResource" +
					 " from SiteResource SiteResource" +
					 " where SiteResource.otherColumnIds is null" +
					 " and (select count(SiteResourceSubjection.id) from SiteResourceSubjection SiteResourceSubjection where SiteResourceSubjection.resourceId=SiteResource.id)>1";
		for(int i=0; i<200000 ; i+=200) {
			List resources = databaseService.findRecordsByHql(hql, ListUtils.generateList("subjections"), 0, 200);
			if(resources==null || resources.isEmpty()) {
				break;
			}
			for(Iterator iterator = resources.iterator(); iterator.hasNext();) {
				SiteResource resource = (SiteResource)iterator.next();
				Iterator iteratorColumn = resource.getSubjections().iterator();
				iteratorColumn.next();
				String otherColumnIds = null;
				String otherColumnNames = null;
				while(iteratorColumn.hasNext()) {
					SiteResourceSubjection subjection = (SiteResourceSubjection)iteratorColumn.next();
					String columnName = null;
					try {
						columnName = siteService.getDirectoryFullName(subjection.getSiteId(), "/", "site");
					}
					catch(Exception e) {
						
					}
					if(columnName==null) {
						databaseService.deleteRecord(subjection);
					}
					else {
						otherColumnIds = (otherColumnIds==null ? "" : otherColumnIds + ",") + subjection.getSiteId();
						otherColumnNames = (otherColumnNames==null ? "" : otherColumnNames + ",") + columnName;
					}
				}
				resource.setOtherColumnIds(otherColumnIds);
				resource.setOtherColumnNames(otherColumnNames);
				databaseService.updateRecord(resource);
			}
			if(resources.size()<200) {
				break;
			}
		}
	}
}