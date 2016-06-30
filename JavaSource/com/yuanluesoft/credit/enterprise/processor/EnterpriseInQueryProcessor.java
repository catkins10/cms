package com.yuanluesoft.credit.enterprise.processor;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.search.Condition;

public class EnterpriseInQueryProcessor extends RecordListProcessor {



	//	使用自定义组装的搜索条件
	protected List generateSearchConditions(View view, RecordList recordListModel, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {	
		List searchConditions=new ArrayList();
		String name = RequestUtils.getParameterStringValue(request,  "name");//企业名称
		double worthSmall = 0;
		double worthBig = 0;
		try {
			worthSmall = RequestUtils.getParameterDoubleValue(request,  "worthSmall");//注册资本
		} catch (Exception e) {
			Logger.error(e);
		}
		try {
			worthBig = RequestUtils.getParameterDoubleValue(request,  "worthBig");//注册资本
		} catch (Exception e) {
			Logger.error(e);
		}
		String industry = RequestUtils.getParameterStringValue(request,  "industry");//行业分类
		String searchConditionStr = "";
		if(name != null && !name.equals("")){
			searchConditionStr = "EnterpriseIn.name like '%" + name +"%'";
			searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, null, null, Condition.CONDITION_EXPRESSION_HQL,searchConditionStr , null));
		}
		if(worthBig>0){
			searchConditionStr = "EnterpriseIn.worth <="+worthBig;
			searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, null, null, Condition.CONDITION_EXPRESSION_HQL,searchConditionStr , null));
		}
		if(worthSmall>0){
			searchConditionStr = "EnterpriseIn.worth >="+worthSmall;
			searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, null, null, Condition.CONDITION_EXPRESSION_HQL,searchConditionStr , null));
		}
		if(industry != null && !industry.equals("")){
			searchConditionStr = "EnterpriseIn.industry like '%"+industry+"%'";
			searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, null, null, Condition.CONDITION_EXPRESSION_HQL,searchConditionStr , null));
		}
		
        
        return searchConditions;
	}
}
