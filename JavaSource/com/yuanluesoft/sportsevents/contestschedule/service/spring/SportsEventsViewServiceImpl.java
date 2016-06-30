package com.yuanluesoft.sportsevents.contestschedule.service.spring;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl;
import com.yuanluesoft.sportsevents.contestschedule.pojo.Score;

public class SportsEventsViewServiceImpl extends ViewServiceImpl {
/*
 * （非 Javadoc）
 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecordCount(com.yuanluesoft.jeaf.view.model.View, java.lang.String, java.util.List, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
 */
	public int retrieveRecordCount(View view, String currentCategories, List searchConditionList, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		String quickFilter=RequestUtils.getParameterStringValue(request, "viewPackage.quickFilter");
//		快速查询
		String hqlWhere="";
		if(quickFilter!=null&&quickFilter.length()!=0){
			hqlWhere=" where Score.player like '%"+quickFilter+"%' ";
		}
		String viewPackageName=RequestUtils.getParameterStringValue(request, "viewPackageName");
		if(viewPackageName!=null&&viewPackageName.length()!=0){
			String currentViewAction=RequestUtils.getParameterStringValue(request, viewPackageName+".currentViewAction");
			if(currentViewAction!=null&&currentViewAction.equals("finishSearch")){//结束搜索
				hqlWhere="";
			}
		}
		

		List count=getDatabaseService().findRecordsByHql("select Score.player from com.yuanluesoft.sportsevents.contestschedule.pojo.Score Score "+hqlWhere+" group by Score.player");
		if(count==null||count.isEmpty()){
	        return 0;
	    }else{
	    	return count.size();
	    }
	}
/*
 * （非 Javadoc）
 * @see com.yuanluesoft.jeaf.view.service.spring.ViewServiceImpl#retrieveRecords(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, int, int, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
 */
	protected List retrieveRecords(String applicationName, String pojoClassName, String hqlSelect, String hqlJoin, String hqlWhere, String hqlGroupBy, String hqlOrderBy, String filter, List lazyLoadProperties, int offset, int limit, View view, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		String viewName=view.getName();
//		奖牌板，以奖牌总数排序
		if("medalPlate".equals(viewName)){
			List records= getDatabaseService().findRecordsByHql(
           		"select Score.player,sum(Score.score),sum(Score.goldMedalCount),sum(Score.silverMedalCount),sum(Score.bronzeMedalCount)" +
           		" from com.yuanluesoft.sportsevents.contestschedule.pojo.Score Score " +(hqlWhere==null||hqlWhere.length()==0?"":" where "+hqlWhere)+
           		" group by Score.player " +
           		" order by sum(Score.goldMedalCount)+sum(Score.silverMedalCount)+sum(Score.bronzeMedalCount) desc ",offset, limit);
//			   转换填充成Score类
			if(records==null||records.isEmpty()){
		    	return null;
		    }else{
		    	List scores=new ArrayList();
		    	for(Iterator iterator = records.iterator();iterator.hasNext();){
		    		Object[] object=(Object[]) iterator.next();
		    		Score score=new Score();
		    		score.setPlayer(object[0].toString());//参赛团队
		    		score.setScore((object[1])==null?0:((Double)object[1]).doubleValue());//总分数
		    		score.setGoldMedalCount(object[2]==null?0:((Integer)object[2]).intValue());//金牌数
		    		score.setSilverMedalCount(object[3]==null?0:((Integer)object[3]).intValue());//银牌数
		    		score.setBronzeMedalCount(object[4]==null?0:((Integer)object[4]).intValue());//铜牌数
//		    		设置扩展属性，用于统计总奖牌数
		    		score.setExtendPropertyValue("medalCount", Integer.valueOf(score.getGoldMedalCount()+score.getSilverMedalCount()+score.getBronzeMedalCount()));
		    		scores.add(score);
		    	}
		    	return scores;
		    }
		}//分数板	，以分数总数排序
		else if("scorePlate".equals(viewName)){
//			hibernate不需要as重命名列。他会自动as。order by子句要用group by 子句检索出来的列。即用order by sum(Score.score)，不能用order by sum Score.score，后面的order by 会被认为是原表的而不是group出来的
			List records= getDatabaseService().findRecordsByHql("select Score.player,sum(Score.score),sum(Score.goldMedalCount),sum(Score.silverMedalCount),sum(Score.bronzeMedalCount) " +
       		" from com.yuanluesoft.sportsevents.contestschedule.pojo.Score Score " +(hqlWhere==null||hqlWhere.length()==0?"":" where "+hqlWhere)+
       		" group by Score.player  order by sum(Score.score) desc",offset, limit);
//		   转换填充成Score类
			if(records==null||records.isEmpty()){
		    	return null;
		    }else{
		    	List scores=new ArrayList();
		    	for(Iterator iterator = records.iterator();iterator.hasNext();){
		    		Object[] object=(Object[]) iterator.next();
		    		Score score=new Score();
		    		score.setPlayer(object[0].toString());//参赛团队
		    		score.setScore((object[1])==null?0:((Double)object[1]).doubleValue());//总分数
		    		score.setGoldMedalCount(object[2]==null?0:((Integer)object[2]).intValue());//金牌数
		    		score.setSilverMedalCount(object[3]==null?0:((Integer)object[3]).intValue());//银牌数
		    		score.setBronzeMedalCount(object[4]==null?0:((Integer)object[4]).intValue());//铜牌数
		    		scores.add(score);
		    	}
		    	return scores;
		    }
		}
		return null;

	}

		

}
