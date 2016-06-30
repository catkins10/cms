package com.yuanluesoft.jeaf.view.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import bsh.Interpreter;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.system.exception.SystemUnregistException;
import com.yuanluesoft.jeaf.util.CnToSpell;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.view.calendarview.model.CalendarView;
import com.yuanluesoft.jeaf.view.model.Column;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.search.Condition;
import com.yuanluesoft.jeaf.view.service.ViewService;
import com.yuanluesoft.jeaf.view.statisticview.model.StatisticView;

/**
 * 
 * @author linchuan
 *
 */
public class ViewUtils {
	
	/**
	 * 获取字段值
	 * @param view
	 * @param column
	 * @param record
	 * @param isHtmlPage
	 * @param request
	 * @return
	 */
	public static String getViewFieldValue(View view, Column column, Object record, boolean isHtmlPage, HttpServletRequest request) {
		try {
			//获取字段定义
			Field fieldDefine = FieldUtils.getRecordField(record.getClass().getName(), column.getName(), request);
			int index;
			if(fieldDefine==null && column.getName()!=null && (index=column.getName().indexOf('.'))!=-1) {
				Object component = PropertyUtils.getProperty(record, column.getName().substring(0, index));
				if(component!=null) {
					fieldDefine = FieldUtils.getRecordField(component.getClass().getName(), column.getName().substring(index + 1), request);
				}
			}
			//获取字段值
			Object fieldValue = null;
			if(!Column.COLUMN_TYPE_FORMULA.equals(column.getType())) { //不是公式
				fieldValue = FieldUtils.getFieldValue(record, column.getName(), request);
			}
			else { //公式
				Interpreter interpreter = new Interpreter();  
		        String[] formulaFields = column.getFormulaFields()!=null ? column.getFormulaFields().split(",") : (column.getName()==null ? null : column.getName().split(","));
		        for(int i=0; i<(formulaFields==null ? 0 : formulaFields.length); i++) {
		        	interpreter.set(formulaFields[i], FieldUtils.getFieldValue(record, formulaFields[i], request));
		        }
		        try {
		        	fieldValue = interpreter.eval(column.getFormula());
		        }
		        catch(Exception e) {
		        	
		        }
			}
			//获取前缀
			String prefix = StringUtils.fillParameters(column.getPrefix(), false, false, false, "utf-8", record, request, null);
			//获取后缀
			String postfix = StringUtils.fillParameters(column.getPostfix(), false, false, false, "utf-8", record, request, null);
			//格式化字段值
			boolean filterHtml = fieldDefine!=null && ("string".equals(fieldDefine.getType()) || column.getMaxCharCount()>0);
			String value = FieldUtils.formatFieldValue(fieldValue, fieldDefine, record, true, column.getFormat(), isHtmlPage && !Column.COLUMN_TYPE_FORMULA.equals(column.getType()), filterHtml, false, column.getLength(), column.getSeparator(), column.getEllipsis(), request);
			return (prefix==null ? "" : prefix) + value + (postfix==null ? "" : postfix);
		}
		catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 加亮关键字
	 * @param searchConditionList
	 * @param fieldName
	 * @param fieldValue
	 * @param callback
	 * @return
	 */
	public static boolean writeHighLightKeyWords(List searchConditionList, String fieldName, String fieldValue, HighLightCallback callback) {
		if(searchConditionList==null || searchConditionList.isEmpty()) {
			return false;
		}
		List keys = null;
		boolean searchByKey = false;
		if(searchConditionList.size()==1 && Condition.CONDITION_EXPRESSION_KEY.equals(((Condition)searchConditionList.get(0)).getExpression())) { //快速过滤
			searchByKey = true;
			keys = StringUtils.split(((Condition)searchConditionList.get(0)).getValue1(), ListUtils.generateList(" ,+", ","));
			for(Iterator iterator = keys.iterator(); iterator.hasNext();) {
				String key = (String)iterator.next();
				if(key==null || key.trim().isEmpty() || key.equals("+")) {
					iterator.remove();
				}
			}
		}
		else {
			for(Iterator iterator = searchConditionList.iterator(); iterator.hasNext();) {
				Condition condition = (Condition)iterator.next();
				if(condition.getFieldName()!=null && condition.getFieldName().equals(fieldName) && condition.getValue1()!=null && !condition.getValue1().isEmpty()) {
					keys = ListUtils.generateList(condition.getValue1());
				}
			}
		}
		if(keys==null) {
			return false;
		}
		boolean found = false;
		List values = StringUtils.split(fieldValue, keys);
		for(int i=0; i<(values.size()==1 ? 0 : values.size()); i++) {
			String text = (String)values.get(i);
			if(text==null || text.isEmpty()) {
				continue;
			}
			found = true;
			callback.write(text, i%2!=0);
		}
		if(found) {
			return true;
		}
		//按拼音检查
		if(!searchByKey) { //不是按关键字搜索
			return false;
		}
		String key = ((Condition)searchConditionList.get(0)).getValue1();
		String spell = "";
		int maxCharacters = Math.min(20, fieldValue.length()); //最多处理20个字
		int i = 0;
		for(; i<maxCharacters; i++) {
			spell += CnToSpell.getFullSpell(fieldValue.substring(i, i+1), false);
			if(spell.startsWith(key)) {
				break;
			}
		}
		if(i==maxCharacters) {
			return false;
		}
		callback.write(fieldValue.substring(0, i + 1), true);
		callback.write(fieldValue.substring(i + 1), false);
		return true;
	}
	
	/**
	 * 获取视图服务
	 * @param view
	 * @return
	 * @throws SystemUnregistException
	 */
	public static ViewService getViewService(View view) {
		String serviceName;
		if(view.getViewServiceName()!=null) {
			serviceName = view.getViewServiceName();
		}
		else if(view instanceof StatisticView) {
			serviceName = "statisticViewService";
		}
		else if(view instanceof CalendarView) {
			serviceName = "calendarViewService";
		}
		else {
			serviceName = "viewService";
		}
		try {
			return (ViewService)Environment.getService(serviceName);
		}
		catch (ServiceException e) {
			return null;
		}
	}
	
	/**
	 * 按日获取记录列表
	 * @param records
	 * @param calendarFieldName
	 * @param beginDay
	 * @param endDay
	 * @return
	 */
	public static List findRecordsByDay(List records, String calendarFieldName, int beginDay, int endDay) {
		return findRecordsByDateField(records, calendarFieldName, Calendar.DAY_OF_MONTH, beginDay, endDay);
	}
	
	/**
	 * 按小时获取记录列表
	 * @param records
	 * @param calendarFieldName
	 * @param beginHour
	 * @param endHour
	 * @return
	 */
	public static List findRecordsByHour(List records, String calendarFieldName, int beginHour, int endHour) {
		return findRecordsByDateField(records, calendarFieldName, Calendar.HOUR_OF_DAY, beginHour, endHour);
	}
	
	/**
	 * 获取打开视图的URL
	 * @param view
	 * @param request
	 * @return
	 */
	public static String getViewURL(View view, HttpServletRequest request) {
		String url = StringUtils.fillParameters(view.getUrl(), true, false, false, "utf-8", view, request, null);
		if(url==null || url.isEmpty()) {
			url = Environment.getWebApplicationUrl() + "/jeaf/application/applicationView.shtml?applicationName=" + view.getApplicationName() + "&viewName=" + view.getName();
		}
		else if(url.indexOf("://")==-1) {
			url = Environment.getWebApplicationUrl() + url;
		}
		return url;
	}
	
	/**
	 * 按时间字段获取记录列表
	 * @param records
	 * @param calendarFieldName
	 * @param dateField
	 * @param beginValue
	 * @param endValue
	 * @return
	 */
	private static List findRecordsByDateField(List records, String calendarFieldName, int dateField, int beginValue, int endValue) {
		if(records==null || records.isEmpty()) {
			return null;
		}
		calendarFieldName = calendarFieldName.substring(calendarFieldName.indexOf('.') + 1);
		Calendar calendar = Calendar.getInstance();
		List results = new ArrayList();
		for(Iterator iterator = records.iterator(); iterator.hasNext();) {
			Object record = iterator.next();
			Date date;
			try {
				date = (Date)PropertyUtils.getProperty(record, calendarFieldName);
			}
			catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			if(date==null) {
				continue;
			}
			calendar.setTimeInMillis(date.getTime());
			int value = calendar.get(dateField);
			if(value>=beginValue && value<=endValue) {
				results.add(record);
			}
		}
		return results.isEmpty() ? null : results;
	}
}