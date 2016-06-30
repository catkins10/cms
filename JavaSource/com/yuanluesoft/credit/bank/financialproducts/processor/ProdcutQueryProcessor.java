package com.yuanluesoft.credit.bank.financialproducts.processor;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.search.Condition;

public class ProdcutQueryProcessor extends RecordListProcessor {



	//	使用自定义组装的搜索条件
	protected List generateSearchConditions(View view, RecordList recordListModel, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {	
		List searchConditions=new ArrayList();
		//金融产品名称
		String name = RequestUtils.getParameterStringValue(request,  "name");
		String creator = RequestUtils.getParameterStringValue(request,  "key");
		if(name!=null && !name.equals("")){
			String searchConditionStr=new String("Product.name like '%"+ name +"%' ");//条件语句
			searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, null, null, Condition.CONDITION_EXPRESSION_HQL,searchConditionStr , null));
		}
		if(creator!=null && !creator.equals("")){
			String searchConditionStr=new String("Product.creator like '%"+ name +"%' ");//条件语句
			searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, null, null, Condition.CONDITION_EXPRESSION_HQL,searchConditionStr , null));
		}
        return searchConditions;
	}
}
