package com.yuanluesoft.land.landcertificate.service.spring;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import com.yuanluesoft.jeaf.attachmentmanage.model.Attachment;
import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;
import com.yuanluesoft.land.landcertificate.pojo.LandCertificate;
import com.yuanluesoft.land.landcertificate.service.LandCertificateService;

/**
 * 
 * @author linchuan
 *
 */
public class LandCertificateServiceImpl implements LandCertificateService {
	private DatabaseService databaseService; //数据库服务
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.land.landcertificate.service.LandCertificateService#importCases(java.util.List)
	 */
	public void importCases(List uploadFiles) throws ServiceException {
		if(uploadFiles==null || uploadFiles.isEmpty()) {
			throw new ServiceException("数据文件未上传");
		}
		String filePath = ((Attachment)uploadFiles.get(0)).getFilePath();
		XmlParser xmlParser = new XmlParser();
		Element element;
		try {
			element = xmlParser.parseXmlFile(filePath);
		} 
		catch(ParseException e) {
			throw new ServiceException("数据文件解析失败");
		}
		/**
		 <NewDataTable>
		  <recordNO>201105241101038</recordNO> 
		  <td_zbh_2>2011</td_zbh_2> 
		  <bp_no>37</bp_no> 
		  <address>芗城区华府豪景2幢205号</address> 
		  <sbpeo>陈淑云</sbpeo> 
		  <sjdate>2011-05-24T16:05:14-04:00</sjdate> 
		  <lbbs>1</lbbs> 
		  <lxdh>2962020</lxdh> 
		  <PNo>350524197201174526</PNo> 
		  <alltdzh>漳国用（2011）第117112号</alltdzh> 
		 </NewDataTable>
		 */
		for(Iterator iterator = element.elementIterator("NewDataTable"); iterator.hasNext();) {
			Element recordElement = (Element)iterator.next();
			String caseNumber = recordElement.elementText("recordNO"); //受理编号
			//检查是否导入过
			LandCertificate landCertificate = (LandCertificate)getDatabaseService().findRecordByHql("from LandCertificate LandCertificate where LandCertificate.caseNumber='" + JdbcUtils.resetQuot(caseNumber) + "'");
			boolean isNew = landCertificate==null;
			if(isNew) {
				landCertificate = new LandCertificate();
				landCertificate.setId(UUIDLongGenerator.generateId()); //ID
				landCertificate.setCaseNumber(caseNumber); //受理编号
			}
			landCertificate.setCaseYear(recordElement.elementText("td_zbh_2")); //年度
			landCertificate.setCaseBatch(recordElement.elementText("bp_no")); //批号
			landCertificate.setCategory(Integer.parseInt(recordElement.elementText("lbbs"))); //收件类别
			String created = recordElement.elementText("sjdate"); //收件时间
			try {
				landCertificate.setCreated(DateTimeUtils.parseTimestamp(created.substring(0, created.lastIndexOf('-')).replace('T', ' '), "yyyy-MM-dd HH:mm:ss"));
			} 
			catch (java.text.ParseException e) {
				
			}
			landCertificate.setApplicant(recordElement.elementText("sbpeo")); //申请人
			landCertificate.setIdCard(recordElement.elementText("PNo")); //身份证号码
			landCertificate.setTel(recordElement.elementText("lxdh")); //联系电话
			landCertificate.setAddress(recordElement.elementText("address")); //土地座落
			landCertificate.setCertificateNumber(recordElement.elementText("alltdzh")); //证号
			if(isNew) {
				getDatabaseService().saveRecord(landCertificate);
			}
			else {
				getDatabaseService().updateRecord(landCertificate);
			}
		}
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
}