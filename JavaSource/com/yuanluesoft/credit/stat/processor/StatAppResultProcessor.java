package com.yuanluesoft.credit.stat.processor;



import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;

public class StatAppResultProcessor extends RecordListProcessor {
	private DatabaseService databaseService;
	private String defaultUnitId;
	private String defaultUnitName;
	private String defaultSiteId;
	protected RecordListData readRecordListData(View view, RecordList recordListModel, List searchConditions, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, WebDirectory webDirectory, WebSite parentSite, SitePage sitePage, HttpServletRequest request) throws ServiceException {
		boolean isBank = false;
		List dateList = new ArrayList();
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
			isBank = true;
		}else{
			hql = "from PersonSubjection PersonSubjection where PersonSubjection.orgId="+id;
			List personSubjections = databaseService.findRecordsByHql(hql);
			userIds = ListUtils.join(personSubjections, "personId", ",", false);//该部门下的所有用户
		}
		//统计应用发布数量
		Datas datas = null;
		Object obj = null;
		if(isBank){
//			金融产品
			hql = "select count(Product.id) from Product Product where Product.creatorId in ("+userIds+")";
			datas = new Datas();
			obj = databaseService.findRecordByHql(hql);
			datas.setNum(obj==null?0:((Number)obj).intValue());
			datas.setStatObj("金融产品");
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			datas.setNewesIssueTime(getNewesCreated("Product",null));
			dateList.add(datas);
			//贷款信息
			hql = "select count(BankLoan.id) from BankLoan BankLoan where BankLoan.creatorId in ("+userIds+")";
			datas = new Datas();
			obj = databaseService.findRecordByHql(hql);
			datas.setNum(obj==null?0:((Number)obj).intValue());
			datas.setStatObj("贷款信息");
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			datas.setNewesIssueTime(getNewesCreated("BankLoan",null));
			dateList.add(datas);
			//排序
			sort(dateList);
			return new RecordListData(dateList);
		}
		//林业局信用记录
		if(unitName.contains("林业")){
			hql = "select count(Agricultural.id) from Agricultural Agricultural where Agricultural.creatorId in ("+userIds+")";
			datas = new Datas();
			obj = databaseService.findRecordByHql(hql);
			datas.setNum(obj==null?0:((Number)obj).intValue());
			datas.setStatObj("林业局信用记录");
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			datas.setNewesIssueTime(getNewesCreated("Agricultural",null));
			
			hql = "select count(ServiceItem.id) from ServiceItem ServiceItem where ServiceItem.creatorId in ("+userIds+")";
			datas = new Datas();
			obj = databaseService.findRecordByHql(hql);
			datas.setNum(obj==null?0:((Number)obj).intValue());
			datas.setStatObj("办事事项");
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			datas.setNewesIssueTime(getNewesCreated("ServiceItem"," where ServiceItem.creatorId in ("+userIds+")"));
			dateList.add(datas);
		}
		if(unitName.contains("法院")){
			//法院信用记录
			hql = "select count(Court.id) from Court Court where Court.creatorId in ("+userIds+")";
			datas = new Datas();
			obj = databaseService.findRecordByHql(hql);
			datas.setNum(obj==null?0:((Number)obj).intValue());
			datas.setStatObj("法院信用记录");
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			datas.setNewesIssueTime(getNewesCreated("Court",null));
			dateList.add(datas);
		}
		if(unitName.contains("林业")){
			//林业局信用记录
			hql = "select count(Forestry.id) from Forestry Forestry where Forestry.creatorId in ("+userIds+")";
			datas = new Datas();
			obj = databaseService.findRecordByHql(hql);
			datas.setNum(obj==null?0:((Number)obj).intValue());
			datas.setStatObj("林业局信用记录");
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			datas.setNewesIssueTime(getNewesCreated("Forestry",null));
			dateList.add(datas);
		}
		if(unitName.contains("安监")){
		//安监局信用记录
		int num = 0;
		Timestamp created = null;
		hql = "select count(BigStandard.id) from BigStandard BigStandard where BigStandard.creatorId in ("+userIds+")";
		obj = databaseService.findRecordByHql(hql);
		num = num +(obj==null?0:((Number)obj).intValue());
		hql = "select count(Punish.id) from Punish Punish where Punish.creatorId in ("+userIds+")";
		obj = databaseService.findRecordByHql(hql);
		num = num +(obj==null?0:((Number)obj).intValue());
		hql = "select count(Permit.id) from Permit Permit where Permit.creatorId in ("+userIds+")";
		obj = databaseService.findRecordByHql(hql);
		num = num +(obj==null?0:((Number)obj).intValue());
		hql = "select count(Handle.id) from Handle Handle where Handle.creatorId in ("+userIds+")";
		obj = databaseService.findRecordByHql(hql);
		num = num +(obj==null?0:((Number)obj).intValue());
		hql = "select count(SmallStandard.id) from SmallStandard SmallStandard where SmallStandard.creatorId in ("+userIds+")";
		obj = databaseService.findRecordByHql(hql);
		num = num +(obj==null?0:((Number)obj).intValue());
		created = getNewesCreated("BigStandard",null);
		Timestamp created2 = getNewesCreated("Punish",null);
		created = after(created, created2);
		created2 = getNewesCreated("Permit",null);
		created = after(created, created2);
		created2 = getNewesCreated("Handle",null);
		created = after(created, created2);
		created2 = getNewesCreated("SmallStandard",null);
		created = after(created, created2);
		datas = new Datas();
		datas.setNum(num);
		datas.setStatObj("安监局信用记录");
		datas.setFlag("app");//应用
		datas.setUnitId(id);
		datas.setUnitName(unitName);
		datas.setNewesIssueTime(created);
		dateList.add(datas);
		}
		if(unitName.contains("国土")){
			
			//国土局信用记录
			hql = "select count(LandResources.id) from LandResources LandResources where LandResources.creatorId in ("+userIds+")";
			datas = new Datas();
			obj = databaseService.findRecordByHql(hql);
			datas.setNum(obj==null?0:((Number)obj).intValue());
			datas.setStatObj("国土局信用记录");
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			datas.setNewesIssueTime(getNewesCreated("LandResources",null));
			dateList.add(datas);
			
			hql = "select count(ServiceItem.id) from ServiceItem ServiceItem where ServiceItem.creatorId in ("+userIds+")";
			datas = new Datas();
			obj = databaseService.findRecordByHql(hql);
			datas.setNum(obj==null?0:((Number)obj).intValue());
			datas.setStatObj("办事事项");
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			datas.setNewesIssueTime(getNewesCreated("ServiceItem"," where ServiceItem.creatorId in ("+userIds+")"));
			dateList.add(datas);
		}
		if(unitName.contains("环保")){
			//环保局信用记录
			int num = 0;
			hql = "select count(License.id) from License License where License.creatorId in ("+userIds+")";
			obj = databaseService.findRecordByHql(hql);
			num = num + (obj==null?0:((Number)obj).intValue());
			hql = "select count(Municipal.id) from Municipal Municipal where Municipal.creatorId in ("+userIds+")";
			obj = databaseService.findRecordByHql(hql);
			num = num + (obj==null?0:((Number)obj).intValue());
			datas = new Datas();
			Timestamp created = getNewesCreated("License",null);
			Timestamp created2 = getNewesCreated("Municipal",null);
			datas.setNewesIssueTime(after(created, created2));
			datas.setNum(num);
			datas.setStatObj("环保局信用记录");
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			dateList.add(datas);
		}
		if(unitName.contains("地税")){
			//地税局信用记录
			int num = 0;
			hql = "select count(LocalTax.id) from LocalTax LocalTax where LocalTax.creatorId in ("+userIds+")";
			obj = databaseService.findRecordByHql(hql);
			num = num + (obj==null?0:((Number)obj).intValue());
			hql = "select count(QuarterTax.id) from QuarterTax QuarterTax where QuarterTax.creatorId in ("+userIds+")";
			obj = databaseService.findRecordByHql(hql);
			num = num + (obj==null?0:((Number)obj).intValue());
			datas = new Datas();
			Timestamp created = getNewesCreated("LocalTax",null);
			Timestamp created2 = getNewesCreated("QuarterTax",null);
			datas.setNewesIssueTime(after(created, created2));
			datas.setNum(num);
			datas.setStatObj("地税局信用记录");
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			dateList.add(datas);
		}
		if(unitName.contains("市场监督")){
			//市场监督管理局信用记录
			hql = "select count(MarketPerson.id) from MarketPerson MarketPerson where MarketPerson.creatorId in ("+userIds+")";
			obj = databaseService.findRecordByHql(hql);
			datas = new Datas();
			int num = obj==null?0:((Number)obj).intValue();
			Timestamp created = getNewesCreated("MarketPerson",null);
			hql = "select count(MarketEnterprise.id) from MarketEnterprise MarketEnterprise where MarketEnterprise.creatorId in ("+userIds+")";
			obj = databaseService.findRecordByHql(hql);
			num = num+(obj==null?0:((Number)obj).intValue());
			Timestamp created2 = getNewesCreated("MarketEnterprise",null);
			datas.setNum(num);
			datas.setNewesIssueTime(after(created, created2));
			datas.setStatObj("市场监督管理局信用记录");
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			dateList.add(datas);
			
			//企业信息管理
			num = 0;
			created = null;
			created2 = null;
			
			hql = "select count(Enterprise.id) from Enterprise Enterprise where Enterprise.creatorId in ("+userIds+")";
			obj = databaseService.findRecordByHql(hql);
			num = num +(obj==null?0:((Number)obj).intValue());
			hql = "select count(EnterpriseIn.id) from EnterpriseIn EnterpriseIn where EnterpriseIn.creatorId in ("+userIds+")";
			obj = databaseService.findRecordByHql(hql);
			num = num +(obj==null?0:((Number)obj).intValue());
			hql = "select count(EnterpriseOut.id) from EnterpriseOut EnterpriseOut where EnterpriseOut.creatorId in ("+userIds+")";
			obj = databaseService.findRecordByHql(hql);
			num = num +(obj==null?0:((Number)obj).intValue());
			created = getNewesCreated("Enterprise"," where Enterprise.creatorId in ("+userIds+")");
			created2 = getNewesCreated("EnterpriseIn"," where EnterpriseIn.creatorId in ("+userIds+")");
			created = after(created, created2);
			created2 = getNewesCreated("EnterpriseOut"," where EnterpriseOut.creatorId in ("+userIds+")");
			created = after(created, created2);
			datas = new Datas();
			datas.setNum(num);
			datas.setNewesIssueTime(created);
			datas.setStatObj("企业信息管理");
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			dateList.add(datas);
		}
		if(unitName.contains("国税")){
			//国家税务局信用记录
			hql = "select count(OweTax.id) from OweTax OweTax where OweTax.creatorId in ("+userIds+")";
			obj = databaseService.findRecordByHql(hql);
			datas = new Datas();
			datas.setNum(obj==null?0:((Number)obj).intValue());
			datas.setNewesIssueTime(getNewesCreated("OweTax",null));
			datas.setStatObj("国家税务局信用记录");
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			dateList.add(datas);
		}
		if(unitName.contains("交通")){
			//交通局信用记录
			hql = "select count(TrafficCredit.id) from TrafficCredit TrafficCredit where TrafficCredit.creatorId in ("+userIds+")";
			datas = new Datas();
			obj = databaseService.findRecordByHql(hql);
			datas.setNum(obj==null?0:((Number)obj).intValue());
			datas.setStatObj("交通局信用记录");
			datas.setNewesIssueTime(getNewesCreated("TrafficCredit",null));
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			dateList.add(datas);
		}
		if(unitName.contains("工商")){
			hql = "select count(ServiceItem.id) from ServiceItem ServiceItem where ServiceItem.creatorId in ("+userIds+")";
			datas = new Datas();
			obj = databaseService.findRecordByHql(hql);
			datas.setNum(obj==null?0:((Number)obj).intValue());
			datas.setStatObj("办事事项");
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			datas.setNewesIssueTime(getNewesCreated("ServiceItem"," where ServiceItem.creatorId in ("+userIds+")"));
			dateList.add(datas);
		}
		if(unitName.contains("房管")){
			hql = "select count(ServiceItem.id) from ServiceItem ServiceItem where ServiceItem.creatorId in ("+userIds+")";
			datas = new Datas();
			obj = databaseService.findRecordByHql(hql);
			datas.setNum(obj==null?0:((Number)obj).intValue());
			datas.setStatObj("办事事项");
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			datas.setNewesIssueTime(getNewesCreated("ServiceItem"," where ServiceItem.creatorId in ("+userIds+")"));
			dateList.add(datas);
		}
		if(unitName.contains("城乡建设")){
			hql = "select count(ServiceItem.id) from ServiceItem ServiceItem where ServiceItem.creatorId in ("+userIds+")";
			datas = new Datas();
			obj = databaseService.findRecordByHql(hql);
			datas.setNum(obj==null?0:((Number)obj).intValue());
			datas.setStatObj("办事事项");
			datas.setFlag("app");//应用
			datas.setUnitId(id);
			datas.setUnitName(unitName);
			datas.setNewesIssueTime(getNewesCreated("ServiceItem"," where ServiceItem.creatorId in ("+userIds+")"));
			dateList.add(datas);
		}
		if(!unitName.contains("镇")&&!unitName.contains("农场")&&!unitName.contains("乡")){
			getCommonCredit(id, unitName, dateList, userIds);//获取通用信用记录
		}
		//排序
		sort(dateList);
		return new RecordListData(dateList);
	}
	
	/**
	 * 降序排序
	 * */
	public void sort(List dateList){
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
	/**
	 * 获取通用信用记录
	 * */
	public List getCommonCredit(long id , String unitName, List dateList,String userIds){
//		通用信用记录
		String hql = "select count(CommonCredit.id) from CommonCredit CommonCredit where CommonCredit.creatorId in ("+userIds+")";
		Datas datas = new Datas();
		Object obj = databaseService.findRecordByHql(hql);
		datas.setNum(obj==null?0:((Number)obj).intValue());
		datas.setStatObj("通用信用记录");
		datas.setFlag("app");//应用
		datas.setUnitId(id);
		datas.setUnitName(unitName);
		dateList.add(datas);
		return dateList;
	}
	
	public Timestamp getNewesCreated(String pojo, String where ){
		 String hql ="";
		 if(where==null){
			 
			 hql = "select max("+pojo+".created) from "+pojo+" "+pojo;
		 }else{
			 hql = "select max("+pojo+".created) from "+pojo+" "+pojo+where;
		 }
		 Object created = databaseService.findRecordByHql(hql);
		 if(created==null){
			 return null;
		 }else{
			 try {
				 return (Timestamp)created;
			} catch (Exception e) {
				return null;
			}
		 }
	}
	public DatabaseService getDatabaseService() {
		return databaseService;
	}
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
	public String getDefaultSiteId() {
		return defaultSiteId;
	}
	public void setDefaultSiteId(String defaultSiteId) {
		this.defaultSiteId = defaultSiteId;
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
	/**
	 * 日起比较返回大的
	 * */
	public Timestamp after(Timestamp t1,Timestamp t2){
		if(t1==null && t2==null){
			return t1;
		}
		if(t1==null || t2==null){
			return t1==null?t2:t1;
		}
		return t1 = t1.after(t2)?t1:t2;
		
	}
	
}
