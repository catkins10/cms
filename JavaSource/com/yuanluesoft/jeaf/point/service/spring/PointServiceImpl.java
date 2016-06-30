package com.yuanluesoft.jeaf.point.service.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.point.model.PointItem;
import com.yuanluesoft.jeaf.point.model.PointItemDefinition;
import com.yuanluesoft.jeaf.point.pojo.ItemPoint;
import com.yuanluesoft.jeaf.point.pojo.Point;
import com.yuanluesoft.jeaf.point.service.PointService;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class PointServiceImpl implements PointService {
	private DatabaseService databaseService;
	private Map pointItemDefinitions; //积分项目定义列表

	/* (non-Javadoc)
	 * @see com.yuanluesoft.edu.point.service.PointService#addPoint(java.util.List, long)
	 */
	public int addPoint(List pointItems, long personId) throws ServiceException {
		int pointAdded = 0;
		for(Iterator iterator = pointItems.iterator(); iterator.hasNext();) {
			PointItem pointItem = (PointItem)iterator.next();
			//获取积分项目定义
			PointItemDefinition itemDefinition = (PointItemDefinition)pointItemDefinitions.get(pointItem.getItemName());
			if(itemDefinition==null || itemDefinition.getItemPoint()==0 || pointItem.getItemCount()==0) {
				continue;
			}
			//保存各单项积分
			ItemPoint itemPoint = (ItemPoint)databaseService.findRecordByHql("from ItemPoint ItemPoint where ItemPoint.personId=" + personId + " and ItemPoint.itemName='" + JdbcUtils.resetQuot(pointItem.getItemName()) + "'");
			boolean isNew = (itemPoint==null);
			if(isNew) {
				itemPoint = new ItemPoint();
				itemPoint.setId(UUIDLongGenerator.generateId()); //ID
				itemPoint.setPersonId(personId); //用户ID
				itemPoint.setItemName(pointItem.getItemName()); //项目名称
			}
			itemPoint.setPoint(itemPoint.getPoint() + itemDefinition.getItemPoint() * pointItem.getItemCount()); //积分
			if(isNew) {
				databaseService.saveRecord(itemPoint);
			}
			else {
				databaseService.updateRecord(itemPoint);
			}
			//累积本次增加的积分
			pointAdded += itemDefinition.getItemPoint() * pointItem.getItemCount();
		}
		Point point = (Point)databaseService.findRecordByHql("from Point Point where Point.personId=" + personId);
		boolean isNew = (point==null);
		if(isNew) {
			point = new Point();
			point.setId(UUIDLongGenerator.generateId()); //ID
			point.setPersonId(personId); //用户ID
		}
		point.setPoint(point.getPoint() + pointAdded); //积分
		if(isNew) {
			databaseService.saveRecord(point);
		}
		else {
			databaseService.updateRecord(point);
		}
		return pointAdded;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.point.service.PointService#addPoint(java.lang.String, int, long)
	 */
	public int addPoint(String itemName, int itemCount, long personId) throws ServiceException {
		List pointItems = new ArrayList();
		pointItems.add(new PointItem(itemName, itemCount));
		return addPoint(pointItems, personId);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.edu.point.service.PointService#getPoint(long)
	 */
	public int getPoint(long personId) throws ServiceException {
		Number point = (Number)databaseService.findRecordByHql("select Point.point from Point Point where Point.personId=" + personId);
		return point==null ? 0 : point.intValue();
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
	 * @return the pointItemDefinitions
	 */
	public Map getPointItemDefinitions() {
		return pointItemDefinitions;
	}

	/**
	 * @param pointItemDefinitions the pointItemDefinitions to set
	 */
	public void setPointItemDefinitions(Map pointItemDefinitions) {
		this.pointItemDefinitions = pointItemDefinitions;
	}
}