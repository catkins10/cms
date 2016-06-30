package com.yuanluesoft.traffic.expressway.service.spring;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.NameValuePair;

import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.HttpUtils;
import com.yuanluesoft.jeaf.util.model.HttpResponse;
import com.yuanluesoft.traffic.expressway.pojo.ExpresswayPrice;
import com.yuanluesoft.traffic.expressway.service.ExpresswayService;

/**
 * 
 * @author yuanluesoft
 *
 */
public class ExpresswayServiceImpl implements ExpresswayService {

	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.traffic.expressway.service.ExpresswayService#getPrice(java.lang.String, java.lang.String)
	 */
	public ExpresswayPrice getPrice(String entry, String exit) throws ServiceException {
		try {
			NameValuePair[] pairs = new NameValuePair[3];
			pairs[0] = new NameValuePair("car_type", "");
			pairs[1] = new NameValuePair("from_toll_station_id", entry);
			pairs[2] = new NameValuePair("end_toll_station_id", exit);
			HttpResponse httpResponse = HttpUtils.doPost("http://218.85.65.28/gzcx/cxch/cxcx/TollStandard_gsfyresult.do", null, pairs, null);
			if(httpResponse==null) {
				return null;
			}
			String flag = "<tbody class=\"pn-ltbody\">";
			String response = httpResponse.getResponseBody();
			int index = response.indexOf(flag);
			if(index==-1) {
				return null;
			}
			index += flag.length();
			int indexEnd = response.indexOf("</table>", index);
			response = response.substring(index, indexEnd);
			ExpresswayPrice price = new ExpresswayPrice();
			//解析数据
			Pattern pattern = Pattern.compile("<td [^>]*>((.|\n)*?)</td>");
	        Matcher matcher = pattern.matcher(response);
	        index = 0;
	        int type = 0;//车辆型号
	        float typePrice = 0.0f;//费用
	        while(matcher.find()) {
	        	String value = matcher.group(1).trim().replaceAll("&nbsp;", " ");
	        	switch(index) {
	        	case 2:
	        		type = Integer.parseInt(value); //车辆型号
	        		break;
	        	
	        	case 3:
	        		price.setSpace(Math.round(Float.parseFloat(value) * 100) / 100.0); //收费里程
	        		break;
	        		
	        	case 4:
	        		typePrice = Float.parseFloat(value); //收费
	        		break;
	        		
	        	case 8:
	        		type = Integer.parseInt(value);;
	        		break;
	        		
	        	case 10:
	        		typePrice = Float.parseFloat(value); //车辆二型
	        		break;
	        		
	        	case 14:
	        		type = Integer.parseInt(value);; //车辆三型
	        		break;
	        	
	        	case 16:
	        		typePrice = Float.parseFloat(value); //车辆四型
	        		break;
	        	
	        	case 20:
	        		type = Integer.parseInt(value);; //车辆五型
	        		break;
	        		
	        	case 22:
	        		typePrice = Float.parseFloat(value); //车辆五型
	        		break;
	        		
	        	case 26:
	        		type = Integer.parseInt(value);; //车辆五型
	        		break;
	        		
	        	case 28:
	        		typePrice = Float.parseFloat(value); //车辆五型
	        		break;
	        	}
	        	index++;
	        	//判断是哪个类型的费用
	        	switch(type) {
	        	case 1:
	        		price.setType1Price(typePrice);
	        		break;
	        		
	        	case 2:
	        		price.setType2Price(typePrice);
	        		break;
	        		
	        	case 3:
	        		price.setType3Price(typePrice);
	        		break;
	        		
	        	case 4:
	        		price.setType4Price(typePrice);
	        		break;
	        		
	        	case 5:
	        		price.setType5Price(typePrice);
	        		break;
	        	}
	        }
			return price;
		}
		catch(Exception e) {
			Logger.exception(e);
			return null;
		}
	}
}