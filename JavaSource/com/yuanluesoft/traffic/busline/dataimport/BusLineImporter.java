package com.yuanluesoft.traffic.busline.dataimport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yuanluesoft.cms.capture.model.CapturedRecord;
import com.yuanluesoft.cms.capture.model.CapturedRecordList;
import com.yuanluesoft.cms.capture.pojo.CmsCaptureTask;
import com.yuanluesoft.cms.capture.service.CaptureService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.traffic.busline.pojo.BusLine;
import com.yuanluesoft.traffic.busline.service.BusLineService;


/**
 * 
 * @author linchuan
 *
 */
public class BusLineImporter {
	//抓取规则
	private String captureRule = "rO0ABXNyAC9jb20ueXVhbmx1ZXNvZnQuY21zLmNhcHR1cmUucG9qby5DbXNDYXB0dXJlVGFzawf+LciU+lAXAgASSQAJYmVnaW5QYWdlQwASYm9keUluZGV4RGlyZWN0aW9uSgAJY3JlYXRvcklkSQAHZW5hYmxlZEMAEmxpc3RJbmRleERpcmVjdGlvbkMAEW5leHRQYWdlRGlyZWN0aW9uQwAWbmV4dFBhZ2VJbmRleERpcmVjdGlvbkMAFnJlY29yZFBhZ2VVUkxEaXJlY3Rpb25MABFidXNpbmVzc0NsYXNzTmFtZXQAEkxqYXZhL2xhbmcvU3RyaW5nO0wADWJ1c2luZXNzVGl0bGVxAH4AAUwACmNhcHR1cmVVUkxxAH4AAUwAB2NyZWF0ZWR0ABRMamF2YS9zcWwvVGltZXN0YW1wO0wAB2NyZWF0b3JxAH4AAUwAC2Rlc2NyaXB0aW9ucQB+AAFMABJleHRlbmRlZFBhcmFtZXRlcnNxAH4AAUwABmZpZWxkc3QAD0xqYXZhL3V0aWwvU2V0O0wAC25leHRQYWdlVVJMcQB+AAFMAA1yZWNvcmRQYWdlVVJMcQB+AAF4cgAkY29tLnl1YW5sdWVzb2Z0LmplYWYuZGF0YWJhc2UuUmVjb3Jk+R9r9r7rgeMCAAFKAAJpZHhwBhv39GWz+hAAAAAAADAAAAl+BtghyAAAAAEAMAAxADAAMHQALGNvbS55dWFubHVlc29mdC50cmFmZmljLmJ1c2xpbmUucG9qby5CdXNMaW5ldAAM5YWs5Lqk57q/6LevdABIaHR0cDovL2Z1emhvdS44Njg0LmNuL2dfJUI4JUEzJUQ2JUREJUMzJUY2JUQ0JUNCJUI5JUFCJUJEJUJCJUI5JUFCJUNCJUJFc3IAEmphdmEuc3FsLlRpbWVzdGFtcCYY1cgBU79lAgABSQAFbmFub3N4cgAOamF2YS51dGlsLkRhdGVoaoEBS1l0GQMAAHhwdwgAAAExgA7JcHgYFI0AdAAM6LaF57qn566h55CGdAAM5YWs5Lqk57q/6LevdAAAc3IAH25ldC5zZi5oaWJlcm5hdGUuY29sbGVjdGlvbi5TZXS+tWviOIuz4gIAAUwAA3NldHEAfgADeHIAKm5ldC5zZi5oaWJlcm5hdGUuY29sbGVjdGlvbi5PRE1HQ29sbGVjdGlvbnNG3XWEiaaIAgAAeHIAMG5ldC5zZi5oaWJlcm5hdGUuY29sbGVjdGlvbi5QZXJzaXN0ZW50Q29sbGVjdGlvbncEHXKlwrR1AgACWgALaW5pdGlhbGl6ZWRMABJjb2xsZWN0aW9uU25hcHNob3R0ACxMbmV0L3NmL2hpYmVybmF0ZS9lbmdpbmUvQ29sbGVjdGlvblNuYXBzaG90O3hwAXNyADFuZXQuc2YuaGliZXJuYXRlLmltcGwuU2Vzc2lvbkltcGwkQ29sbGVjdGlvbkVudHJ54TZsiUMkNPMCAAVaAAVkaXJ0eVoAC2luaXRpYWxpemVkTAAJbG9hZGVkS2V5dAAWTGphdmEvaW8vU2VyaWFsaXphYmxlO0wABHJvbGVxAH4AAUwACHNuYXBzaG90cQB+ABV4cAABc3IADmphdmEubGFuZy5Mb25nO4vkkMyPI98CAAFKAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVyhqyVHQuU4IsCAAB4cAYb9/Rls/oQdAA2Y29tLnl1YW5sdWVzb2Z0LmNtcy5jYXB0dXJlLnBvam8uQ21zQ2FwdHVyZVRhc2suZmllbGRzc3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAADHcIAAAAEAAAAAdzcgAwY29tLnl1YW5sdWVzb2Z0LmNtcy5jYXB0dXJlLnBvam8uQ21zQ2FwdHVyZUZpZWxkohRhGc6TOzgCAAhJAAppc0xpc3RQYWdlRAAIcHJpb3JpdHlKAAZ0YXNrSWRMAApmaWVsZEJlZ2lucQB+AAFMAAtmaWVsZERlZmluZXQAK0xjb20veXVhbmx1ZXNvZnQvamVhZi9idXNpbmVzcy9tb2RlbC9GaWVsZDtMAAhmaWVsZEVuZHEAfgABTAALZmllbGRGb3JtYXRxAH4AAUwACWZpZWxkTmFtZXEAfgABeHEAfgAEBhv39JXdFBUAAAAAP/AAAAAAAAAGG/f0ZbP6EHQAEjxwPuWOu+eoi++8mjxzcGFuPnB0AA48L3NwYW4+PC9wPjxwPnB0AAxkb3dubGlua0xpbmVxAH4AH3NxAH4AHQYb9/SV3RQSAAAAAUAAAAAAAAAABhv39GWz+hB0AAhhIGhyZWY9InB0AAIiPnB0ABFjYXB0dXJlUmVjb3JkTGlua3EAfgAjc3EAfgAdBhv39JXdFBAAAAABAAAAAAAAAAAGG/f0ZbP6EHQAFGRpdiBjbGFzcz0ibGEiPjx1bD4KcHQAGDwvZGl2PgogICA8ZGl2IGlkPSJmdGFkInB0AAtjYXB0dXJlTGlzdHEAfgAnc3EAfgAdBhv39JXdFBYAAAAAQAAAAAAAAAAGG/f0ZbP6EHQAD+Wbnueoi++8mjxzcGFuPnB0AAs8L3NwYW4+PC9wPnB0AAp1cGxpbmtMaW5lcQB+ACtzcQB+AB0GG/f0ld0UEQAAAAE/8AAAAAAAAAYb9/Rls/oQdAAEPGxpPnB0AAU8L2xpPnB0AA1jYXB0dXJlUmVjb3JkcQB+AC9zcQB+AB0GG/f0ld0UEwAAAAFACAAAAAAAAAYb9/Rls/oQdAABPnB0AAQ8L2E+cHQABG5hbWVxAH4AM3NxAH4AHQYb9/SV3RQUAAAAAAAAAAAAAAAABhv39GWz+hB0AA08L2E+PC9oMj48bGk+cHQACjxhIGhyZWY9Ii9wdAARc3VtbWVyVXBsaW5rRmlyc3RxAH4AN3hzcgAXamF2YS51dGlsLkxpbmtlZEhhc2hTZXTYbNdald0qHgIAAHhyABFqYXZhLnV0aWwuSGFzaFNldLpEhZWWuLc0AwAAeHB3DAAAABA/QAAAAAAAB3EAfgAncQB+ADdxAH4AL3EAfgAfcQB+ACNxAH4AK3EAfgAzeHQAAHQAAA==";
	private CaptureService captureService; //抓取服务
	private DatabaseService databaseService; //数据库服务
	private BusLineService busLineService; //公交线路服务

	public void importBusLine() throws Exception {
		//福州市公共交通集团有限责任公司
		importCompany("http://fuzhou.8684.cn/g_%B8%A3%D6%DD%CA%D0%B9%AB%B9%B2%BD%BB%CD%A8%BC%AF%CD%C5%D3%D0%CF%DE%D4%F0%C8%CE%B9%AB%CB%BE");
		//福州康弛新巴士有限责任公司
		importCompany("http://fuzhou.8684.cn/g_%B8%A3%D6%DD%BF%B5%B3%DA%D0%C2%B0%CD%CA%BF%D3%D0%CF%DE%D4%F0%C8%CE%B9%AB%CB%BE");
		importCompany("http://fuzhou.8684.cn/g_%B8%A3%D6%DD%BF%B5%B3%DB%D0%C2%B0%CD%CA%BF%D3%D0%CF%DE%D4%F0%C8%CE%B9%AB%CB%BE");
		//福州闽运公交公司
		importCompany("http://fuzhou.8684.cn/g_%B8%A3%D6%DD%C3%F6%D4%CB%B9%AB%BD%BB%B9%AB%CB%BE");
		//福州公交集团责任有限公司
		importCompany("http://fuzhou.8684.cn/g_%B8%A3%D6%DD%B9%AB%BD%BB%BC%AF%CD%C5%D4%F0%C8%CE%D3%D0%CF%DE%B9%AB%CB%BE");
		//福州营达公交责任有限公司
		importCompany("http://fuzhou.8684.cn/g_%B8%A3%D6%DD%D3%AA%B4%EF%B9%AB%BD%BB%D4%F0%C8%CE%D3%D0%CF%DE%B9%AB%CB%BE");
		//福州市公共交通集团闽侯惠民巴士有限公司
		importCompany("http://fuzhou.8684.cn/g_%B8%A3%D6%DD%CA%D0%B9%AB%B9%B2%BD%BB%CD%A8%BC%AF%CD%C5%C3%F6%BA%EE%BB%DD%C3%F1%B0%CD%CA%BF%D3%D0%CF%DE%B9%AB%CB%BE");
	}
	
	/**
	 * 按公司导入
	 * @param url
	 * @throws Exception
	 */
	private void importCompany(String url) throws Exception {
		CmsCaptureTask task = captureService.decodeCaptureTask(captureRule);
		CapturedRecordList busLinesPage = captureService.captureListPage(task, url, 0);
		for(Iterator iterator = busLinesPage.getRecords().iterator(); iterator.hasNext();) {
			CapturedRecord captureRecord = (CapturedRecord)iterator.next();
			CapturedRecord detailPage = captureService.captureContentPage(task, captureRecord.getUrl());
			BusLine busLineDetail = (BusLine)detailPage.getRecord();
			BusLine busLine = (BusLine)captureRecord.getRecord();
			System.out.println("import:" + busLine.getName());
			//删除原来的记录
			databaseService.deleteRecordsByHql("from BusLine BusLine where BusLine.name='" + JdbcUtils.resetQuot(busLine.getName()) + "'");
			//保存新记录
			busLine.setId(UUIDLongGenerator.generateId()); //ID
			String text = busLineDetail.getSummerUplinkFirst();
			//解析票价
			String[] values = text.replaceAll(" ", ",").split(",");
			int beginIndex = -1;
			int i = 0;
			for(; i<values.length; i++) {
				if(beginIndex==-1 && values[i].indexOf("元")!=-1 && values[i].indexOf(":")==-1) {
					beginIndex = i;
				}
				if(values[i].indexOf("查8公")!=-1 || values[i].indexOf("八六")!=-1 || values[i].indexOf("8684")!=-1 || values[i].indexOf("查公交")!=-1 || values[i].indexOf("专业公交查询")!=-1) {
					break;
				}
			}
			if(beginIndex!=-1) {
				String ticketPrice = values[beginIndex];
				for(beginIndex=beginIndex+1; beginIndex<i; beginIndex++) {
					ticketPrice += "," + values[beginIndex];
				}
				busLine.setTicketPrice(ticketPrice); //票价
			}
			//解析首末班时间
			values = text.replaceAll("：", ":").split(":");
			List times = new ArrayList(); 
			for(i=0; i<values.length-1; i++) {
				String hour = "";
				System.out.println("values[i]:" + values[i]);
				for(int j=values[i].length()-1; j>values[i].length()-3; j--) {
					if(j>=0 && values[i].charAt(j)>='0' && values[i].charAt(j)<='9') {
						hour = values[i].charAt(j) + hour;
					}
				}
				if(hour.isEmpty()) {
					continue;
				}
				if(hour.length()==1) {
					hour = "0" + hour;
				}
				String minute = "";
				for(int j=0; j<2 && j<values[i+1].length(); j++) {
					if(values[i+1].charAt(j)>='0' && values[i+1].charAt(j)<='9') {
						minute += values[i+1].charAt(j);
					}
				}
				if(minute.length()==1) {
					minute = "0" + minute;
				}
				times.add(hour + ":" + minute);
			}
			System.out.println("times: " + times);
			if(times.size()==2) {
				busLine.setSummerDownlinkFirst((String)times.get(0)); //夏季首班车(下行)
				busLine.setSummerDownlinkLast((String)times.get(1)); //夏季末班车(下行)
				busLine.setWinterDownlinkFirst((String)times.get(0)); //冬季首班车(下行)
				busLine.setWinterDownlinkLast((String)times.get(1)); //冬季末班车(下行)
				busLine.setSummerUplinkFirst((String)times.get(0)); //夏季首班车(上行)
				busLine.setSummerUplinkLast((String)times.get(1)); //夏季末班车(上行)
				busLine.setWinterUplinkFirst((String)times.get(0)); //冬季首班车(上行)
				busLine.setWinterUplinkLast((String)times.get(1)); //冬季末班车(上行)
			}
			else if(times.size()==3) {
				busLine.setSummerDownlinkFirst((String)times.get(0)); //夏季首班车(下行)
				busLine.setSummerDownlinkLast((String)times.get(1)); //夏季末班车(下行)
				busLine.setWinterDownlinkFirst((String)times.get(0)); //冬季首班车(下行)
				busLine.setWinterDownlinkLast((String)times.get(2)); //冬季末班车(下行)
				busLine.setSummerUplinkFirst((String)times.get(0)); //夏季首班车(上行)
				busLine.setSummerUplinkLast((String)times.get(1)); //夏季末班车(上行)
				busLine.setWinterUplinkFirst((String)times.get(0)); //冬季首班车(上行)
				busLine.setWinterUplinkLast((String)times.get(2)); //冬季末班车(上行)
			}
			else if(times.size()==4 && text.indexOf("夏")!=-1 && text.indexOf("冬")!=-1) {
				busLine.setSummerDownlinkFirst((String)times.get(0)); //夏季首班车(下行)
				busLine.setSummerDownlinkLast((String)times.get(1)); //夏季末班车(下行)
				busLine.setWinterDownlinkFirst((String)times.get(2)); //冬季首班车(下行)
				busLine.setWinterDownlinkLast((String)times.get(3)); //冬季末班车(下行)
				busLine.setSummerUplinkFirst((String)times.get(0)); //夏季首班车(上行)
				busLine.setSummerUplinkLast((String)times.get(1)); //夏季末班车(上行)
				busLine.setWinterUplinkFirst((String)times.get(2)); //冬季首班车(上行)
				busLine.setWinterUplinkLast((String)times.get(3)); //冬季末班车(上行)
			}
			else if(times.size()==4) {
				busLine.setSummerDownlinkFirst((String)times.get(0)); //夏季首班车(下行)
				busLine.setSummerDownlinkLast((String)times.get(1)); //夏季末班车(下行)
				busLine.setWinterDownlinkFirst((String)times.get(0)); //冬季首班车(下行)
				busLine.setWinterDownlinkLast((String)times.get(1)); //冬季末班车(下行)
				busLine.setSummerUplinkFirst((String)times.get(2)); //夏季首班车(上行)
				busLine.setSummerUplinkLast((String)times.get(3)); //夏季末班车(上行)
				busLine.setWinterUplinkFirst((String)times.get(2)); //冬季首班车(上行)
				busLine.setWinterUplinkLast((String)times.get(3)); //冬季末班车(上行)
			}
			else if(times.size()==6) {
				busLine.setSummerDownlinkFirst((String)times.get(0)); //夏季首班车(下行)
				busLine.setSummerDownlinkLast((String)times.get(1)); //夏季末班车(下行)
				busLine.setWinterDownlinkFirst((String)times.get(0)); //冬季首班车(下行)
				busLine.setWinterDownlinkLast((String)times.get(2)); //冬季末班车(下行)
				busLine.setSummerUplinkFirst((String)times.get(3)); //夏季首班车(上行)
				busLine.setSummerUplinkLast((String)times.get(4)); //夏季末班车(上行)
				busLine.setWinterUplinkFirst((String)times.get(3)); //冬季首班车(上行)
				busLine.setWinterUplinkLast((String)times.get(5)); //冬季末班车(上行)
			}
			else if(times.size()==8) {
				busLine.setSummerDownlinkFirst((String)times.get(0)); //夏季首班车(下行)
				busLine.setSummerDownlinkLast((String)times.get(1)); //夏季末班车(下行)
				busLine.setWinterDownlinkFirst((String)times.get(2)); //冬季首班车(下行)
				busLine.setWinterDownlinkLast((String)times.get(3)); //冬季末班车(下行)
				busLine.setSummerUplinkFirst((String)times.get(4)); //夏季首班车(上行)
				busLine.setSummerUplinkLast((String)times.get(5)); //夏季末班车(上行)
				busLine.setWinterUplinkFirst((String)times.get(6)); //冬季首班车(上行)
				busLine.setWinterUplinkLast((String)times.get(7)); //冬季末班车(上行)
			}
			//busLine.setUplinkDistance(uplinkDistance); //上行线路长度
			//busLine.setDownlinkDistance(downlinkDistance); //下行线路长度
			if(busLineDetail.getDownlinkLine()==null || busLineDetail.getDownlinkLine().isEmpty() ||
			   busLineDetail.getUplinkLine()==null || busLineDetail.getUplinkLine().isEmpty()) {
				continue;
			}
			//解析公交站点
			String[] downlinkStations = busLineDetail.getDownlinkLine().split(" - ");
			String[] uplinkStations = busLineDetail.getUplinkLine().split(" - ");
			int index = downlinkStations[downlinkStations.length-1].lastIndexOf(" (");
			if(index==-1) {
				continue;
			}
			downlinkStations[downlinkStations.length-1] = downlinkStations[downlinkStations.length-1].substring(0, index);
			index = uplinkStations[uplinkStations.length-1] .lastIndexOf(" (");
			if(index==-1) {
				continue;
			}
			uplinkStations[uplinkStations.length-1] = uplinkStations[uplinkStations.length-1].substring(0,  index);
			
			busLine.setDownlinkLine(downlinkStations[0] + "~" + downlinkStations[downlinkStations.length-1]); //下行主要线路
			busLine.setUplinkLine(uplinkStations[0] + "~" + uplinkStations[uplinkStations.length-1]); //上行主要线路
			//busLine.setBusTotal(busTotal); //配车总数
			//busLine.setDutyNumber(dutyNumber); //班次
			//busLine.setPeakFrequency(peakFrequency); //高峰发车间隔
			//busLine.setCommonFrequency(commonFrequency); //平峰发车间隔
			busLine.setModifyPersonId(0); //最后更新人ID
			busLine.setModifyPerson(null); //最后更新人姓名
			busLine.setLastModified(DateTimeUtils.now()); //最后更新时间
			//private Set stations; //站点列表
			//private Set changes; //变更通知列表
			//保存主记录
			databaseService.saveRecord(busLine);
			//保存站点列表
			busLineService.updateBusLineStations(busLine, ListUtils.join(downlinkStations, ",", false), ListUtils.join(uplinkStations, ",", false));
		}
	}

	/**
	 * @return the captureRule
	 */
	public String getCaptureRule() {
		return captureRule;
	}

	/**
	 * @param captureRule the captureRule to set
	 */
	public void setCaptureRule(String captureRule) {
		this.captureRule = captureRule;
	}

	/**
	 * @return the captureService
	 */
	public CaptureService getCaptureService() {
		return captureService;
	}

	/**
	 * @param captureService the captureService to set
	 */
	public void setCaptureService(CaptureService captureService) {
		this.captureService = captureService;
	}

	/**
	 * @return the databaseService
	 */
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	/**
	 * @return the busLineService
	 */
	public BusLineService getBusLineService() {
		return busLineService;
	}

	/**
	 * @param busLineService the busLineService to set
	 */
	public void setBusLineService(BusLineService busLineService) {
		this.busLineService = busLineService;
	}
}