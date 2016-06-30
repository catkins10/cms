package com.yuanluesoft.cms.onlineservice.interactive.accept.fjwssp.service.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis.encoding.XMLType;
import org.dom4j.Element;

import com.yuanluesoft.cms.onlineservice.interactive.accept.fjwssp.model.Case;
import com.yuanluesoft.cms.onlineservice.interactive.accept.fjwssp.service.FjwsspService;
import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.soap.SoapConnectionPool;
import com.yuanluesoft.jeaf.soap.SoapPassport;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ObjectSerializer;

/**
 * 
 * @author linchuan
 *
 */
public class FjwsspServiceImpl implements FjwsspService {
	private SoapConnectionPool soapConnectionPool; //SOAP连接池
	private SoapPassport soapPassport; //SOAP地址及身份验证信息, 测试地址 http://218.85.73.188:8088/apas/services/, 正式地址 http://wssp.fj.gov.cn/apas/services/
	private String departmentCode; //部门编码,国土厅：003899162
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.interactive.accept.fjwssp.service.FjwsspService#listCases()
	 */
	public List listCases() throws ServiceException {
		List wsspCases = null;
		try{
			wsspCases =(List)ObjectSerializer.deserializeFromFile(getDataFilePath()); //从缓存文件中获取
		}
		catch(Exception e) {
			
		}
		if(wsspCases!=null && !wsspCases.isEmpty()) {
			return wsspCases;
		}
		try {
			return retrieveCases();
		}
		catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.cms.onlineservice.interactive.accept.fjwssp.service.FjwsspService#updateCases()
	 */
	public void updateCases() {
		try {
			retrieveCases();
		}
		catch (Exception e) {
			Logger.exception(e);
		}
	}
	
	/**
	 * 获取办件列表
	 * @return
	 */
	public List retrieveCases() throws Exception {
		if(soapPassport==null || departmentCode==null || departmentCode.isEmpty()) {
			return null;
		}
		if(Logger.isInfoEnabled()) {
			Logger.info("FjwsspService: update data.");
		}
		//调用WEB服务
		String[] argNames = {"deptcode", "starttime", "endTime", "handlestatesing", "count", "sortsign", "times"};
		Object[] args = {departmentCode, "1990-01-01", DateTimeUtils.formatDate(DateTimeUtils.date(), "yyyy-MM-dd"), "3", "50", "2", "1"}; //记录数50条
		QName[] argTypes = {XMLType.XSD_STRING, XMLType.XSD_STRING, XMLType.XSD_STRING, XMLType.XSD_STRING, XMLType.XSD_STRING, XMLType.XSD_STRING, XMLType.XSD_STRING};
		String response = (String)soapConnectionPool.invokeRemoteMethod("getblqg", "getInfoListNew", XMLType.XSD_STRING, null, soapPassport, args, argNames, argTypes, null, null);
	
		//解析返回结果
		int index = response.indexOf("</Result>");
		if(index!=-1) {
			response = response.substring(index + "</Result>".length());//删除result前的数据,原格式不规范
		}
		Element xmlRoot = new XmlParser().parseXmlString(response);
		Element xmlRecords = xmlRoot.element("Records"); //获取根节点下的子节点Records
		if(xmlRecords==null) {
			return null;
		}
		ArrayList wsspCases = new ArrayList();
		for(Iterator iterator = xmlRecords.elementIterator(); iterator.hasNext();) {
			Element xmlRecord = (Element)iterator.next();
			/*<Record>
			 * <ReceiverDept>省国土资源厅</ReceiverDept>
			 * <ProjectName>大田县文江乡文江村陈兴光半坑采石场</ProjectName>
			 * <ProjectID>31209116030</ProjectID>
			 * <ServiceName>采矿权转让登记</ServiceName>
			 * <ServiceCode>003899162A07020</ServiceCode>
			 * <ApplyName>陈兴光</ApplyName>
			 * <CreateTime>2012-09-11 11:19:53</CreateTime>
			 * <PromiseEndDay>2012-10-19</PromiseEndDay>
			 * <HandleState>在办</HandleState>
			 *</Record>
			 **/
			Case wsspCase = new Case();
			wsspCase.setReceiverDept(xmlRecord.elementText("ReceiverDept"));//受理部门
			wsspCase.setProjectName(xmlRecord.elementText("ProjectName"));//受理事项
			wsspCase.setProjectID(xmlRecord.elementText("ProjectID"));//受理申报号
			wsspCase.setServiceName(xmlRecord.elementText("ServiceName"));//审批事项名称
			wsspCase.setServiceCode(xmlRecord.elementText("ServiceCode"));//审批事项编码
			wsspCase.setApplyName(xmlRecord.elementText("ApplyName"));//申报人
			try {
				wsspCase.setCreateTime(DateTimeUtils.parseTimestamp(xmlRecord.elementText("CreateTime"), "yyyy-MM-dd HH:mm:ss"));//申报日期
			}
			catch(Exception e) {
				
			}
			try {
				wsspCase.setPromiseEndDay(DateTimeUtils.parseDate(xmlRecord.elementText("PromiseEndDay"), "yyyy-MM-dd"));//承诺办结日期
			}
			catch(Exception e) {
				
			}
			wsspCase.setHandleState(xmlRecord.elementText("HandleState"));//办件状态
			if(wsspCase.getServiceName()!=null && wsspCase.getApplyName()!=null){ //审批事项名称和申报人不为空
				wsspCases.add(wsspCase);
			}
		}
		//把记录写入文件
		if(!wsspCases.isEmpty()) {
			ObjectSerializer.serializeToFile(wsspCases, getDataFilePath());
		}
		return wsspCases;
	}
	
	/**
	 * 获取数据文件路径
	 * @return
	 */
	private String getDataFilePath() {
		try {
			return FileUtils.createDirectory((String)Environment.getService("sitePagePath")) + "fjwssp.dat";
		}
		catch (ServiceException e) {
			return Environment.getWebinfPath() + "fjwssp.dat";
		}
	}

	/**
	 * @return the departmentCode
	 */
	public String getDepartmentCode() {
		return departmentCode;
	}

	/**
	 * @param departmentCode the departmentCode to set
	 */
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	/**
	 * @return the soapConnectionPool
	 */
	public SoapConnectionPool getSoapConnectionPool() {
		return soapConnectionPool;
	}

	/**
	 * @param soapConnectionPool the soapConnectionPool to set
	 */
	public void setSoapConnectionPool(SoapConnectionPool soapConnectionPool) {
		this.soapConnectionPool = soapConnectionPool;
	}

	/**
	 * @return the soapPassport
	 */
	public SoapPassport getSoapPassport() {
		return soapPassport;
	}

	/**
	 * @param soapPassport the soapPassport to set
	 */
	public void setSoapPassport(SoapPassport soapPassport) {
		this.soapPassport = soapPassport;
	}
}