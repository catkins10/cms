package com.yuanluesoft.traffic.expenses.service.spring;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.NameValuePair;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.util.model.HttpResponse;
import com.yuanluesoft.traffic.expenses.pojo.Expenses;
import com.yuanluesoft.traffic.expenses.service.ExpensesService;

/**
 * 
 * @author yuanluesoft
 *
 */
public class ExpensesServiceImpl implements ExpensesService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.traffic.expenses.service.ExpensesService#getExpenses(java.lang.String, java.lang.String, java.lang.String)
	 */
	public Expenses getExpenses(String plateNumberPrefix, String plateNumber, String vehicleType) throws ServiceException {
		try {
			//http://www.fjjt.gov.cn/Expenses/expenses.asp?key1=%C3%F6A&key2=1f196&key3=%C0%B6
			NameValuePair[] pairs = new NameValuePair[3];
			pairs[0] = new NameValuePair("key1", new String(plateNumberPrefix.getBytes(),"iso-8859-1"));
			pairs[1] = new NameValuePair("key2", new String(plateNumber.getBytes(),"iso-8859-1"));
			pairs[2] = new NameValuePair("key3", new String(vehicleType.getBytes(),"iso-8859-1"));
			HttpResponse httpResponse = HttpUtils.doPost("http://www.fjjt.gov.cn/Expenses/expenses.asp", null, pairs, null);
			if(httpResponse==null) {
				return null;
			}
			String response = httpResponse.getResponseBody();
			int index = response.indexOf("数据最后更新日期为:");
			if(index==-1) {
				return null;
			}
			index += "数据最后更新日期为:".length();
			//解析更新日期
			int indexEnd = response.indexOf("<", index);
			Expenses expenses = new Expenses();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-d H:m:s");
			expenses.setLastUpdated(new Timestamp(formatter.parse(response.substring(index, indexEnd).trim()).getTime()));
			index = response.indexOf("所属稽征所</td>", indexEnd + 1);
			if(index==-1) {
				return null;
			}
			index += "所属稽征所</td>".length();
			indexEnd = response.indexOf("../images/dian3.jpg", index);
			if(indexEnd==-1) {
				return null;
			}
			response = response.substring(index, indexEnd);
			//解析数据更新日期
			Pattern pattern = Pattern.compile("<td [^>]*>((.|\n)*?)</td>");
	        Matcher matcher = pattern.matcher(response);
	        index = 0;
	        while(matcher.find()) {
	        	String value = matcher.group(1).trim().replaceAll("&nbsp;", " ");
	        	if(value==null) {
	        		continue;
	        	}
	        	int indexScript = value.indexOf("<script");
	        	if(indexScript!=-1) {
	        		value = value.substring(0, indexScript);
	        	}
	        	switch(index) {
	        	case 0:
	        		expenses.setFullPlateNumber(value); //车号
	        		break;
	        		
	        	case 1:
	        		expenses.setType(value); //厂牌型号
	        		break;
	        		
	        	case 2:
	        		expenses.setHalt(value); //是否报停
	        		break;
	        		
	        	case 3:
	        		formatter = new SimpleDateFormat("yyyyMMdd");
	        		expenses.setExpensesDate(new Date(formatter.parse(value).getTime())); //缴费有效期限
	        		break;
	        		
	        	case 4:
	        		expenses.setCollection(value); //所属稽征所
	        		break;
	        	}
	        	index++;
	        }
			return expenses;
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.traffic.expenses.service.ExpensesService#getLastUpdated()
	 */
	public Timestamp getLastUpdated() throws ServiceException {
		
		return null;
	}
}
