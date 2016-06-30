package com.yuanluesoft.traffic.busline.service.spring;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.traffic.busline.pojo.BusLine;
import com.yuanluesoft.traffic.busline.pojo.BusLineChange;
import com.yuanluesoft.traffic.busline.pojo.BusLineStation;
import com.yuanluesoft.traffic.busline.service.BusLineService;

/**
 * 
 * @author lmiky
 *
 */
public class BusLineServiceImpl extends BusinessServiceImpl implements BusLineService {
	private PageService pageService; //页面服务,用于重建静态页面

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		record = super.save(record);
		if(record instanceof BusLine) {
			if(pageService!=null) {
				pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}
		}
		else if(record instanceof BusLineChange) { //新建变更通知
			//删除一年前的通知,避免记录过多
			String hql = "from BusLineChange BusLineChange" +
						 " where BusLineChange.created<TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.add(DateTimeUtils.now(), Calendar.YEAR, -1), null) + ")";
			getDatabaseService().deleteRecordsByHql(hql);
		}
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		record = super.update(record);
		if((record instanceof BusLine) || (record instanceof BusLineChange)) {
			if(pageService!=null) {
				pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
			}
		}
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		if((record instanceof BusLine) || (record instanceof BusLineChange)) {
			if(pageService!=null) {
				pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
			}
		}
		super.delete(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.traffic.busline.service.BusLineService#getBusLineByName(java.lang.String)
	 */
	public BusLine getBusLineByName(String name) throws ServiceException {
		String hql = "from BusLine BusLine" +
					 " where BusLine.name='" + JdbcUtils.resetQuot(name) + "'" +
					 " or BusLine.name='" + JdbcUtils.resetQuot(name) + "路'";
		return (BusLine)getDatabaseService().findRecordByHql(hql, listLazyLoadProperties(BusLine.class));
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.traffic.busline.service.BusLineService#resetBusLineByInterimChange(com.yuanluesoft.traffic.busline.pojo.BusLine)
	 */
	public Date resetBusLineByInterimChange(BusLine busLine) throws ServiceException {
		BusLineChange interimChange = null;
		try {
			busLine.getChanges().isEmpty();
			if(busLine.getChanges()!=null && !busLine.getChanges().isEmpty()) {
				interimChange = (BusLineChange)busLine.getChanges().iterator().next(); //获取最近的一个变更通知
			}
		}
		catch(Exception e) {
			String hql = "from BusLineChange BusLineChange" +
						 " where BusLineChange.busLineId=" + busLine.getId() +
						 " order by BusLineChange.beginDate DESC";
			interimChange = (BusLineChange)getDatabaseService().findRecordByHql(hql);
		}
		if(interimChange!=null && 
		   (interimChange.getInterim()!='1' || (interimChange.getEndDate()!=null && interimChange.getEndDate().before(DateTimeUtils.date())))) { //不是临时变更、或者已经结束
			interimChange = null;
		}
		if(interimChange!=null) { //有临时变更通知
			return interimChange.getEndDate();
		}
		//没有临时变更通知,删除所有的临时站点,状态,0/正常,1/临时增加,2/临时取消
		for(Iterator iteratorStation = (busLine.getStations()==null ? null : busLine.getStations().iterator()); iteratorStation!=null && iteratorStation.hasNext();) {
			BusLineStation station = (BusLineStation)iteratorStation.next();
			if(station.getStatus()=='1') { //临时增加
				iteratorStation.remove();
			}
			else if(station.getStatus()=='2') { //临时取消
				station.setStatus('0');
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#validateDataIntegrity(com.yuanluesoft.jeaf.database.Record)
	 */
	public List validateDataIntegrity(com.yuanluesoft.jeaf.database.Record record, boolean isNew) throws ServiceException {
		if(record instanceof BusLine) {
			String hql = "select BusLine.id" +
						 " from BusLine BusLine" +
						 " where BusLine.name='" + JdbcUtils.resetQuot(((BusLine)record).getName()) + "'" +
						 " and BusLine.id!=" + record.getId();
			if(getDatabaseService().findRecordByHql(hql)!=null) {
				return ListUtils.generateList("公交线路已经存在");
			}
		}
		return super.validateDataIntegrity(record, isNew);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.traffic.busline.service.BusLineService#updateBusLineStations(java.lang.String, java.lang.String)
	 */
	public void updateBusLineStations(BusLine busLine, String downlinkStationNames, String uplinkStationNames) throws ServiceException {
		String[] downlinkStations = downlinkStationNames.replaceAll("[\\r\\n 。]", "").replaceAll("[;；，]", ",").split(",");
		List stations = new ArrayList();
		for(int i=0; i<downlinkStations.length; i++) {
			BusLineStation station = new BusLineStation();
			station.setBusLineId(busLine.getId()); //公交线路ID
			station.setName(downlinkStations[i]); //站点名称
			station.setDirection('1'); //上行/下行,0/上行, 1/下行, 2/上下行
			station.setStatus('0'); //状态,0/正常,1/临时增加,2/临时取消
			stations.add(station);
		}
		String[] uplinkStations = uplinkStationNames.replaceAll("[\\r\\n 。]", "").replaceAll("[;；，]", ",").split(",");
		for(int i=uplinkStations.length-1; i>=0; i--) {
			BusLineStation station = new BusLineStation();
			station.setBusLineId(busLine.getId()); //公交线路ID
			station.setName(uplinkStations[i]); //站点名称
			station.setDirection('0'); //上行/下行,0/上行, 1/下行, 2/上下行
			station.setStatus('0'); //状态,0/正常,1/临时增加,2/临时取消
			stations.add(station);
		}
		//删除原来的站点
		for(Iterator iteratorStation = busLine.getStations()==null ? null : busLine.getStations().iterator(); iteratorStation!=null && iteratorStation.hasNext();) {
			BusLineStation station = (BusLineStation)iteratorStation.next();
			getDatabaseService().deleteRecord(station);
		}
		//保存站点
		for(Iterator iteratorStation = stations.iterator(); iteratorStation.hasNext();) {
			BusLineStation station = (BusLineStation)iteratorStation.next();
			station.setId(UUIDLongGenerator.generateId());
			getDatabaseService().saveRecord(station);
		}
		busLine.setStations(new LinkedHashSet(stations));
	}

	/**
	 * @return the pageService
	 */
	public PageService getPageService() {
		return pageService;
	}

	/**
	 * @param pageService the pageService to set
	 */
	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}
}