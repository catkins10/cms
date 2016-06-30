package com.yuanluesoft.credit.regist.service.spring;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.cms.pagebuilder.model.page.SitePage;
import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.cms.pagebuilder.processor.spring.RecordListProcessor;
import com.yuanluesoft.cms.sitemanage.pojo.WebDirectory;
import com.yuanluesoft.cms.sitemanage.pojo.WebSite;
import com.yuanluesoft.credit.regist.pojo.admin.CreditUser;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.search.Condition;

public class RegistQueryProcessor extends RecordListProcessor {
	private RegistServiceImpl registServiceImpl;
	private DatabaseService databaseService;


	//	使用自定义组装的搜索条件
	protected List generateSearchConditions(View view, RecordList recordListModel, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {	
		String where = "";
		List searchConditions=new ArrayList();
		String loginName = RequestUtils.getParameterStringValue(request,  "loginName");
		String password = RequestUtils.getParameterStringValue(request,  "password");
		CreditUser user = (CreditUser)databaseService.findRecordByHql("from com.yuanluesoft.songxi.regist.pojo.admin.User User where User.loginName='"+loginName+"'");
		if(user==null){
			where ="1=2";
		}else{
			if(password!=null){
				password = registServiceImpl.encryptPersonPassword(user.getId(), loginName, password);
			}
			where = " CreditUser.loginName = '"+loginName+"' and CreditUser.password = '" + password + "' ";
		}
		String searchConditionStr=new String(where);//条件语句
        searchConditions.add(new Condition(Condition.CONDITION_LINK_MODE_AND, null, null, Condition.CONDITION_EXPRESSION_HQL,searchConditionStr , null));

        return searchConditions;
	}


	public DatabaseService getDatabaseService() {
		return databaseService;
	}


	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}


	public RegistServiceImpl getRegistServiceImpl() {
		return registServiceImpl;
	}


	public void setRegistServiceImpl(RegistServiceImpl registServiceImpl) {
		this.registServiceImpl = registServiceImpl;
	}


	
	
}
