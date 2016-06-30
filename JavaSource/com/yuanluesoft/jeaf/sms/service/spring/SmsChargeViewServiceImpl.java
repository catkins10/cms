package com.yuanluesoft.jeaf.sms.service.spring;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.sms.pojo.SmsSend;
import com.yuanluesoft.jeaf.sms.pojo.SmsUnitBusiness;
import com.yuanluesoft.jeaf.sms.pojo.SmsUnitConfig;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.statisticview.model.Statistic;
import com.yuanluesoft.jeaf.view.statisticview.model.StatisticView;
import com.yuanluesoft.jeaf.view.statisticview.service.spring.StatisticViewServiceImpl;

/**
 * 
 * @author linchuan
 *
 */
public class SmsChargeViewServiceImpl extends StatisticViewServiceImpl {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.statisticview.service.spring.StatisticViewServiceImpl#retrieveRecords(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, int, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		//根据用户所在单位获取短信单位配置
		String hql = "select SmsUnitConfig" +
					 " from SmsUnitConfig SmsUnitConfig, OrgSubjection OrgSubjection" +
					 " where SmsUnitConfig.unitId=OrgSubjection.directoryId" +
					 " and OrgSubjection.parentDirectoryId=" + sessionInfo.getUnitId() +
					 " order by SmsUnitConfig.unitName";
		List units = getDatabaseService().findRecordsByHql(hql, ListUtils.generateList("businessConfigs"), beginRow, view.getPageRows() + 1);
		if(units==null || units.isEmpty()) {
			return null;
		}
		boolean lastPage = units.size()<=view.getPageRows();
		if(!lastPage) {
			units.remove(units.size()-1); //删除多获取的记录
		}
		String where = view.getWhere();
		StatisticView statisticView = (StatisticView)view;
		int pageRows = view.getPageRows();
		view.setPageRows(units.size() * 100); //获取记录，最多支持100项业务
		view.addWhere("SmsSend.senderUnitId in (" + ListUtils.join(units, "unitId", ",", false) + ")");
		List records = super.retrieveRecords(view, currentCategories, searchConditionList, 0, request, sessionInfo);
		view.setPageRows(pageRows);
		SmsSend smsSendTotal = new SmsSend(); //合计
		smsSendTotal.setExtendPropertyValue("sendCount", new Integer(0)); //发送数量
		smsSendTotal.setExtendPropertyValue("charge", new Double(0)); //费用
		smsSendTotal.setExtendPropertyValue("statisticTitleFormat", ((Statistic)statisticView.getStatistics().get(2)).getTitle()); //统计标题格式
		for(int i=units.size()-1; i>=0; i--) {
			SmsUnitConfig unit = (SmsUnitConfig)units.get(i);
			//转换为SmsSend
			SmsSend unitSend = new SmsSend();
			unitSend.setSenderUnit(unit.getUnitName()); //单位名称
			unitSend.setExtendPropertyValue("rowIndex", new Integer(i + beginRow)); //行号
			unitSend.setExtendPropertyValue("statisticTitleFormat", ((Statistic)statisticView.getStatistics().get(1)).getTitle()); //统计标题格式
			unitSend.setExtendPropertyValue("statisticFieldNames", "senderUnitId"); //统计字段名称
			unitSend.setExtendPropertyValue("sendCount", new Integer(0)); //发送数量
			unitSend.setExtendPropertyValue("charge", new Double(0)); //费用
			units.set(i, unitSend);
			//分项业务统计
			int j = 0;
			for(Iterator iterator = unit.getBusinessConfigs()==null ? null : unit.getBusinessConfigs().iterator(); iterator!=null && iterator.hasNext(); j++) {
				SmsUnitBusiness unitBusiness = (SmsUnitBusiness)iterator.next();
				//转换为SmsSend
				SmsSend smsSend = new SmsSend();
				smsSend.setSenderUnit(unit.getUnitName()); //单位名称
				smsSend.setBusinessName(unitBusiness.getBusinessName()); //业务名称
				smsSend.setExtendPropertyValue("rowIndex", new Integer(j)); //行号
				smsSend.setExtendPropertyValue("statisticTitleFormat", ((Statistic)statisticView.getStatistics().get(0)).getTitle()); //统计标题格式
				smsSend.setExtendPropertyValue("price", new Double(unitBusiness.getPrice())); //单价
				smsSend.setExtendPropertyValue("discount", new Double(unitBusiness.getDiscount())); //折扣
				//查找统计结果，获取短信发送数量
				Number sendCount = null;
				for(Iterator iteratorStat = records==null ? null : records.iterator(); iteratorStat!=null && iteratorStat.hasNext();) {
					SmsSend stat = (SmsSend)iteratorStat.next();
					if("senderUnitId,businessName".equals(stat.getExtendPropertyValue("statisticFieldNames")) &&  //单项业务统计
					   unit.getUnitId()==stat.getSenderUnitId() &&
					   unitBusiness.getBusinessName().equals(stat.getBusinessName())) {
						sendCount = (Number)stat.getExtendPropertyValue("sendCount"); //发送数量
						break;
					}
				}
				//计算费用
				smsSend.setExtendPropertyValue("sendCount", new Integer(sendCount==null ? 0 : sendCount.intValue())); //发送数量
				if(sendCount!=null) {
					unitSend.setExtendPropertyValue("sendCount", new Integer(((Number)unitSend.getExtendPropertyValue("sendCount")).intValue() + sendCount.intValue())); //发送数量
					smsSendTotal.setExtendPropertyValue("sendCount", new Integer(((Number)smsSendTotal.getExtendPropertyValue("sendCount")).intValue() + sendCount.intValue())); //发送数量
				}
				double charge = unitBusiness.getPrice() * (unitBusiness.getDiscount()<=0 ? 1 : unitBusiness.getDiscount()/10.0) * (unitBusiness.getChargeMode()==1 ? 1 : (sendCount==null ? 0 : sendCount.intValue())); //固定费用|1\0按条计费|0
				smsSend.setExtendPropertyValue("charge", new Double(charge)); //费用
				unitSend.setExtendPropertyValue("charge", new Double(((Number)unitSend.getExtendPropertyValue("charge")).doubleValue() + charge)); //费用
				smsSendTotal.setExtendPropertyValue("charge", new Double(((Number)smsSendTotal.getExtendPropertyValue("charge")).doubleValue() + charge)); //费用
				units.add(i+j+1, smsSend);
			}
		}
		//如果是最后一页,添加合计
		if(lastPage) {
			//获取其他单位的统计数据
			for(int i=0; i<beginRow; i+=view.getPageRows()) {
				view.setWhere(where);
				records = retrieveRecords(view, currentCategories, searchConditionList, i, request, sessionInfo);
				for(Iterator iteratorStat = records==null ? null : records.iterator(); iteratorStat!=null && iteratorStat.hasNext();) {
					SmsSend stat = (SmsSend)iteratorStat.next();
					if("senderUnitId".equals(stat.getExtendPropertyValue("statisticFieldNames"))) {  //单位统计
						smsSendTotal.setExtendPropertyValue("sendCount", new Integer(((Integer)smsSendTotal.getExtendPropertyValue("sendCount")).intValue() + ((Integer)stat.getExtendPropertyValue("sendCount")).intValue())); //短信数量
						smsSendTotal.setExtendPropertyValue("charge", new Double(((Double)smsSendTotal.getExtendPropertyValue("charge")).doubleValue() + ((Double)stat.getExtendPropertyValue("charge")).doubleValue())); //费用
					}
				}
			}
			units.add(smsSendTotal);
		}
		generateStatisticTitle(units, request);
		return units;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.statisticview.service.spring.StatisticViewServiceImpl#retrieveRecordCount(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	protected int retrieveRecordCount(View view, String currentCategories, List searchConditionList, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		String hql = "select count(SmsUnitConfig.id)" +
					 " from SmsUnitConfig SmsUnitConfig, OrgSubjection OrgSubjection" +
					 " where SmsUnitConfig.unitId=OrgSubjection.directoryId" +
					 " and OrgSubjection.parentDirectoryId=" + sessionInfo.getUnitId();
		Number unitCount = (Number)getDatabaseService().findRecordByHql(hql);
		return unitCount==null ? 0 : unitCount.intValue();
	}
}