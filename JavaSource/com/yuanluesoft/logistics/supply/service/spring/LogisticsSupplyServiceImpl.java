package com.yuanluesoft.logistics.supply.service.spring;

import com.yuanluesoft.cms.pagebuilder.PageService;
import com.yuanluesoft.cms.pagebuilder.StaticPageBuilder;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.logistics.supply.pojo.LogisticsSupplyDeparture;
import com.yuanluesoft.logistics.supply.pojo.LogisticsSupplyDestination;
import com.yuanluesoft.logistics.supply.service.LogisticsSupplyService;

/**
 * 
 * @author linchuan
 *
 */
public class LogisticsSupplyServiceImpl extends BusinessServiceImpl implements LogisticsSupplyService {
	private PageService pageService; //页面服务

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		record = super.save(record);
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		record = super.update(record);
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_UPDATE);
		return record;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		pageService.rebuildStaticPageForModifiedObject(record, StaticPageBuilder.OBJECT_MODIFY_ACTION_PHYSICAL_DELETE);
		super.delete(record);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.logistics.supply.service.LogisticsSupplyService#updateLogisticsSupply(long, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void updateLogisticsSupply(long vehicleSupplyId, boolean isNew, String departureAreaIds, String departureAreas, String destinationAreaIds, String destinationAreas) throws ServiceException {
		if(departureAreaIds==null || departureAreas==null || destinationAreaIds==null || destinationAreas==null) {
			return;
		}
		if(!isNew) {
			getDatabaseService().deleteRecordsByHql("from LogisticsSupplyDeparture LogisticsSupplyDeparture where LogisticsSupplyDeparture.supplyId=" + vehicleSupplyId);
			getDatabaseService().deleteRecordsByHql("from LogisticsSupplyDestination LogisticsSupplyDestination where LogisticsSupplyDestination.supplyId=" + vehicleSupplyId);
		}
		//保存出发地点
		if(departureAreaIds!=null && !departureAreaIds.equals("")) {
			String[] ids = departureAreaIds.split(",");
			String[] areas = departureAreas.split(",");
			for(int i=0; i<ids.length; i++) {
				LogisticsSupplyDeparture supplyDeparture = new LogisticsSupplyDeparture();
				supplyDeparture.setId(UUIDLongGenerator.generateId()); //ID
				supplyDeparture.setSupplyId(vehicleSupplyId); //车源ID
				supplyDeparture.setDepartureId(Long.parseLong(ids[i])); //出发地点ID
				supplyDeparture.setDeparture(areas[i]); //出发地点
				getDatabaseService().saveRecord(supplyDeparture);
			}
		}
		//保存目的地点
		if(destinationAreaIds!=null && !destinationAreaIds.equals("")) {
			String[] ids = destinationAreaIds.split(",");
			String[] areas = destinationAreas.split(",");
			for(int i=0; i<ids.length; i++) {
				LogisticsSupplyDestination supplyDestination = new LogisticsSupplyDestination();
				supplyDestination.setId(UUIDLongGenerator.generateId()); //ID
				supplyDestination.setSupplyId(vehicleSupplyId); //车源ID
				supplyDestination.setDestinationId(Long.parseLong(ids[i])); //目的地点ID
				supplyDestination.setDestination(areas[i]); //目的地点
				getDatabaseService().saveRecord(supplyDestination);
			}
		}
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