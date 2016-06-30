package com.yuanluesoft.cms.siteresource.fjmsa.service;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.siteresource.pojo.SiteResource;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.usermanage.pojo.Org;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.statisticview.service.spring.StatisticViewServiceImpl;

/**
 * 站点资源视图
 * @author linchuan
 *
 */
public class ResourceStatisticViewServiceImpl extends StatisticViewServiceImpl {
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.statisticview.service.spring.StatisticViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		final String rootOrgName = "福建海事局网站管理系统";
		final String keepDepartmentUnitName = "福建海事局";
		List records = super.retrieveRecords(view, currentCategories, searchConditionList, beginRow, request, sessionInfo);
		if(records==null || records.isEmpty()) {
			return records;
		}
		//分支局不显示部门
		for(Iterator iterator = records.iterator(); iterator.hasNext();) {
			SiteResource resource = (SiteResource)iterator.next();
			String statisticFieldNames = (String)resource.getExtendPropertyValue("statisticFieldNames");
			if(statisticFieldNames==null || statisticFieldNames.isEmpty()) { //总计
				continue;
			}
			if("unitName".equals(statisticFieldNames) && !rootOrgName.equals(resource.getUnitName())) { //单位合计,除福建海事局网站管理系统外保留
				continue;
			}
			if(!keepDepartmentUnitName.equals(resource.getUnitName())) {
				iterator.remove();
			}
		}
		//获取“福建海事局”
		Org fjmsa = (Org)getDatabaseService().findRecordByHql("from Org Org where Org.directoryName='" + keepDepartmentUnitName + "'");
		//获取部门列表
		final List departments = getDatabaseService().findRecordsByHql("select Org.directoryName from Org Org where Org.parentDirectoryId=" + fjmsa.getId() + " order by Org.priority DESC, Org.directoryName");
		//获取单位列表
		final List units = getDatabaseService().findRecordsByHql("select Org.directoryName from Org Org where Org.parentDirectoryId=" + fjmsa.getParentDirectoryId() + " order by Org.priority DESC, Org.directoryName");
		//排序
		Collections.sort(records, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				SiteResource resource0 = (SiteResource)arg0;
				SiteResource resource1 = (SiteResource)arg1;
				String statisticFieldNames0 = (String)resource0.getExtendPropertyValue("statisticFieldNames");
				String statisticFieldNames1 = (String)resource1.getExtendPropertyValue("statisticFieldNames");
				if(statisticFieldNames0==null || statisticFieldNames0.isEmpty()) { //总计
					return 1;
				}
				if(statisticFieldNames1==null || statisticFieldNames1.isEmpty()) { //总计
					return -1;
				}
				if("unitName".equals(statisticFieldNames0) || "unitName".equals(statisticFieldNames1)) { //单位
					int index0 = keepDepartmentUnitName.equals(resource0.getUnitName()) ? 1000 : units.indexOf(resource0.getUnitName());
					int index1 = keepDepartmentUnitName.equals(resource1.getUnitName()) ? 1000 : units.indexOf(resource1.getUnitName());
					if((index0==-1 || index1==-1) && index0!=1000 && index1!=1000) {
						return Collator.getInstance(Locale.CHINA).compare(resource0.getUnitName(), resource1.getUnitName());
					}
					return index0==index1 ? (index0==1000 ? ("unitName".equals(statisticFieldNames0) ? -1 : 1) : 0) : (index0<index1 ? -1 : 1);
				}
				else { //部门
					int index0 = departments.indexOf(resource0.getOrgName());
					int index1 = departments.indexOf(resource1.getOrgName());
					if(index0==-1 || index1==-1) {
						return Collator.getInstance(Locale.CHINA).compare(resource0.getOrgName(), resource1.getOrgName());
					}
					return index0==index1 ? 0 : (index0<index1 ? -1 : 1);
				}
			}
		});
		//重新设置行号
		int unitIndex = 0;
		int orgIndex = 0;
		for(Iterator iterator = records.iterator(); iterator.hasNext();) {
			SiteResource resource = (SiteResource)iterator.next();
			String statisticFieldNames = (String)resource.getExtendPropertyValue("statisticFieldNames");
			if(statisticFieldNames==null || statisticFieldNames.isEmpty()) { //总计
				continue;
			}
			else if("unitName".equals(statisticFieldNames)) { //单位
				resource.setExtendPropertyValue("rowIndex", new Integer(unitIndex++));
				orgIndex = 0;
			}
			else { //部门
				resource.setExtendPropertyValue("rowIndex", new Integer(orgIndex++));
			}
		}
		generateStatisticTitle(records, request); //重新生成统计标题
		return records;
	}
}