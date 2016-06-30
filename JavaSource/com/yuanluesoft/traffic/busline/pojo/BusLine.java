package com.yuanluesoft.traffic.busline.pojo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 公交线路(traffic_busline)
 * @author linchuan
 *
 */
public class BusLine extends Record {
	private String name; //线路名称
	private String ticketPrice; //票价
	private String summerUplinkFirst; //夏季首班车(上行)
	private String summerUplinkLast; //夏季末班车(上行)
	private String winterUplinkFirst; //冬季首班车(上行)
	private String winterUplinkLast; //冬季末班车(上行)
	private String summerDownlinkFirst; //夏季首班车(下行)
	private String summerDownlinkLast; //夏季末班车(下行)
	private String winterDownlinkFirst; //冬季首班车(下行)
	private String winterDownlinkLast; //冬季末班车(下行)
	private double uplinkDistance; //上行线路长度
	private double downlinkDistance; //下行线路长度
	private String uplinkLine; //上行主要线路
	private String downlinkLine; //下行主要线路
	private int busTotal; //配车总数
	private int dutyNumber; //班次
	private int peakFrequency; //高峰发车间隔
	private int commonFrequency; //平峰发车间隔
	private long modifyPersonId; //最后更新人ID
	private String modifyPerson; //最后更新人姓名
	private Timestamp lastModified; //最后更新时间
	private Set stations; //站点列表
	private Set changes; //变更通知列表
	
	/**
	 * 获取下行站点列表
	 * @return
	 */
	public List getDownlinkStations() {
		try {
			List downlinkStations = new ArrayList();
			for(Iterator iterator = stations.iterator(); iterator.hasNext();) {
				BusLineStation station = (BusLineStation)iterator.next();
				if(station.getDirection()=='1' || station.getDirection()=='2') { //0/上行, 1/下行, 2/上下行
					downlinkStations.add(station);
				}
			}
			return downlinkStations;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取上行站点列表
	 * @return
	 */
	public List getUplinkStations() {
		try {
			List uplinkStations = new ArrayList();
			for(Iterator iterator = stations.iterator(); iterator.hasNext();) {
				BusLineStation station = (BusLineStation)iterator.next();
				if(station.getDirection()=='0' || station.getDirection()=='2') { //0/上行, 1/下行, 2/上下行
					uplinkStations.add(station);
				}
			}
			Collections.reverse(uplinkStations);
			return uplinkStations;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * @return the busTotal
	 */
	public int getBusTotal() {
		return busTotal;
	}
	/**
	 * @param busTotal the busTotal to set
	 */
	public void setBusTotal(int busTotal) {
		this.busTotal = busTotal;
	}
	/**
	 * @return the changes
	 */
	public Set getChanges() {
		return changes;
	}
	/**
	 * @param changes the changes to set
	 */
	public void setChanges(Set changes) {
		this.changes = changes;
	}
	/**
	 * @return the commonFrequency
	 */
	public int getCommonFrequency() {
		return commonFrequency;
	}
	/**
	 * @param commonFrequency the commonFrequency to set
	 */
	public void setCommonFrequency(int commonFrequency) {
		this.commonFrequency = commonFrequency;
	}
	/**
	 * @return the downlinkDistance
	 */
	public double getDownlinkDistance() {
		return downlinkDistance;
	}
	/**
	 * @param downlinkDistance the downlinkDistance to set
	 */
	public void setDownlinkDistance(double downlinkDistance) {
		this.downlinkDistance = downlinkDistance;
	}
	/**
	 * @return the downlinkLine
	 */
	public String getDownlinkLine() {
		return downlinkLine;
	}
	/**
	 * @param downlinkLine the downlinkLine to set
	 */
	public void setDownlinkLine(String downlinkLine) {
		this.downlinkLine = downlinkLine;
	}
	/**
	 * @return the dutyNumber
	 */
	public int getDutyNumber() {
		return dutyNumber;
	}
	/**
	 * @param dutyNumber the dutyNumber to set
	 */
	public void setDutyNumber(int dutyNumber) {
		this.dutyNumber = dutyNumber;
	}
	/**
	 * @return the lastModified
	 */
	public Timestamp getLastModified() {
		return lastModified;
	}
	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}
	/**
	 * @return the modifyPerson
	 */
	public String getModifyPerson() {
		return modifyPerson;
	}
	/**
	 * @param modifyPerson the modifyPerson to set
	 */
	public void setModifyPerson(String modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	/**
	 * @return the modifyPersonId
	 */
	public long getModifyPersonId() {
		return modifyPersonId;
	}
	/**
	 * @param modifyPersonId the modifyPersonId to set
	 */
	public void setModifyPersonId(long modifyPersonId) {
		this.modifyPersonId = modifyPersonId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the peakFrequency
	 */
	public int getPeakFrequency() {
		return peakFrequency;
	}
	/**
	 * @param peakFrequency the peakFrequency to set
	 */
	public void setPeakFrequency(int peakFrequency) {
		this.peakFrequency = peakFrequency;
	}
	/**
	 * @return the stations
	 */
	public Set getStations() {
		return stations;
	}
	/**
	 * @param stations the stations to set
	 */
	public void setStations(Set stations) {
		this.stations = stations;
	}
	/**
	 * @return the ticketPrice
	 */
	public String getTicketPrice() {
		return ticketPrice;
	}
	/**
	 * @param ticketPrice the ticketPrice to set
	 */
	public void setTicketPrice(String ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	/**
	 * @return the uplinkDistance
	 */
	public double getUplinkDistance() {
		return uplinkDistance;
	}
	/**
	 * @param uplinkDistance the uplinkDistance to set
	 */
	public void setUplinkDistance(double uplinkDistance) {
		this.uplinkDistance = uplinkDistance;
	}
	/**
	 * @return the uplinkLine
	 */
	public String getUplinkLine() {
		return uplinkLine;
	}
	/**
	 * @param uplinkLine the uplinkLine to set
	 */
	public void setUplinkLine(String uplinkLine) {
		this.uplinkLine = uplinkLine;
	}
	/**
	 * @return the summerDownlinkFirst
	 */
	public String getSummerDownlinkFirst() {
		return summerDownlinkFirst;
	}
	/**
	 * @param summerDownlinkFirst the summerDownlinkFirst to set
	 */
	public void setSummerDownlinkFirst(String summerDownlinkFirst) {
		this.summerDownlinkFirst = summerDownlinkFirst;
	}
	/**
	 * @return the summerDownlinkLast
	 */
	public String getSummerDownlinkLast() {
		return summerDownlinkLast;
	}
	/**
	 * @param summerDownlinkLast the summerDownlinkLast to set
	 */
	public void setSummerDownlinkLast(String summerDownlinkLast) {
		this.summerDownlinkLast = summerDownlinkLast;
	}
	/**
	 * @return the summerUplinkFirst
	 */
	public String getSummerUplinkFirst() {
		return summerUplinkFirst;
	}
	/**
	 * @param summerUplinkFirst the summerUplinkFirst to set
	 */
	public void setSummerUplinkFirst(String summerUplinkFirst) {
		this.summerUplinkFirst = summerUplinkFirst;
	}
	/**
	 * @return the summerUplinkLast
	 */
	public String getSummerUplinkLast() {
		return summerUplinkLast;
	}
	/**
	 * @param summerUplinkLast the summerUplinkLast to set
	 */
	public void setSummerUplinkLast(String summerUplinkLast) {
		this.summerUplinkLast = summerUplinkLast;
	}
	/**
	 * @return the winterDownlinkFirst
	 */
	public String getWinterDownlinkFirst() {
		return winterDownlinkFirst;
	}
	/**
	 * @param winterDownlinkFirst the winterDownlinkFirst to set
	 */
	public void setWinterDownlinkFirst(String winterDownlinkFirst) {
		this.winterDownlinkFirst = winterDownlinkFirst;
	}
	/**
	 * @return the winterDownlinkLast
	 */
	public String getWinterDownlinkLast() {
		return winterDownlinkLast;
	}
	/**
	 * @param winterDownlinkLast the winterDownlinkLast to set
	 */
	public void setWinterDownlinkLast(String winterDownlinkLast) {
		this.winterDownlinkLast = winterDownlinkLast;
	}
	/**
	 * @return the winterUplinkFirst
	 */
	public String getWinterUplinkFirst() {
		return winterUplinkFirst;
	}
	/**
	 * @param winterUplinkFirst the winterUplinkFirst to set
	 */
	public void setWinterUplinkFirst(String winterUplinkFirst) {
		this.winterUplinkFirst = winterUplinkFirst;
	}
	/**
	 * @return the winterUplinkLast
	 */
	public String getWinterUplinkLast() {
		return winterUplinkLast;
	}
	/**
	 * @param winterUplinkLast the winterUplinkLast to set
	 */
	public void setWinterUplinkLast(String winterUplinkLast) {
		this.winterUplinkLast = winterUplinkLast;
	}
}