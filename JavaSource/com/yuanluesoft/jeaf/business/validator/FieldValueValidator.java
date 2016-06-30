package com.yuanluesoft.jeaf.business.validator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.EmailValidator;

import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.sensitive.service.SensitiveWordService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 字段值校验
 * @author linchuan
 *
 */
public class FieldValueValidator {

	/**
	 * 字段校验
	 * @param fieldDefine
	 * @param fieldValue
	 * @param requestParameterValue
	 * @param sensitiveWordCheck 是否做敏感词校验
	 * @return
	 */
	public static List validate(Field fieldDefine, Object fieldValue, String requestParameterValue, boolean sensitiveWordCheck) {
		List errors = new ArrayList();
		if(sensitiveWordCheck &&
		   fieldValue!=null &&
		   (fieldValue instanceof String) &&
		   !"false".equals(fieldDefine.getParameter("sensitiveWordCheck")) && //没有禁止校验
		   ",phone,email,".indexOf("," + fieldDefine.getType() + ",")==-1 && //电话和邮件不校验
		   fieldDefine.getInputMode()!=null && ",hidden,none,time,password,".indexOf("," + fieldDefine.getInputMode() + ",")==-1) {
			try {
				SensitiveWordService sensitiveWordService = (SensitiveWordService)Environment.getService("sensitiveWordService");
				String sensitiveWord = sensitiveWordService.findSensitiveWord((String)fieldValue);
				if(sensitiveWord!=null) {
					errors.add(fieldDefine.getTitle() + "不允许出现：" + sensitiveWord);
					return errors;
				}
				if("html".equals(fieldDefine.getType()) && 
				   (sensitiveWord = sensitiveWordService.findSensitiveWord(StringUtils.filterHtmlElement((String)fieldValue, false)))!=null) {
					errors.add(fieldDefine.getTitle() + "不允许出现：" + sensitiveWord);
					return errors;
				}
			}
			catch(Exception e) {
				errors.add("校验时出错");
				return errors;
			}
		}
		if("radio".equals(fieldDefine.getInputMode())) {
			if(fieldValue==null || fieldValue.toString().isEmpty() || requestParameterValue==null || requestParameterValue.isEmpty() || ((fieldValue instanceof Character) && (((Character)fieldValue).charValue()==' ' || ((Character)fieldValue).charValue()==0))) { //空字段
				errors.add(fieldDefine.getTitle() + "未选择");
				return errors;
			}
		}
		else if(fieldValue==null || fieldValue.toString().isEmpty() || requestParameterValue==null || requestParameterValue.isEmpty()) { //空字段
			if(fieldDefine.isRequired()) { //必填字段
				errors.add(fieldDefine.getTitle() + "不能为空");
				return errors;
			}
			return null;
		}
		if(fieldValue instanceof String[]) { //数组
			if(fieldDefine.isRequired()) { //必填字段
				String[] stringValues = (String[])fieldValue;
				if(stringValues.length<1 ||
				   (stringValues.length==1 && (stringValues[0]==null || "".equals(stringValues[0])))) {
					errors.add(fieldDefine.getTitle() + "不能为空");
					return errors;
				}
			}
			return null; //数组,不做其他校验
		}
		if("number".equals(fieldDefine.getType())) { //数字类型
			if(fieldValue instanceof Number) {
				String error = validateNumber((Number)fieldValue, fieldDefine);
				if(error!=null) {
					errors.add(error);
				}
			}
		}
		else if(fieldDefine.getLength()!=null) { 
			String value = "" + fieldValue;
			if(value.length() - (value.charAt(0)=='-' && (fieldValue instanceof Number) ? 1 : 0)>FieldUtils.getFieldInputLength(fieldDefine)) { //指定了字段长度
				errors.add(fieldDefine.getTitle() + "不能超过" + FieldUtils.getFieldInputLength(fieldDefine) + "个字");
			}
		}
		if("email".equals(fieldDefine.getType())) { //电子邮件地址类型
			if(!EmailValidator.getInstance().isValid((String)fieldValue)) {
				errors.add(fieldDefine.getTitle() + "格式无效");
			}
		}
		else {
			String validateRule = (String)fieldDefine.getParameter("validateRule");
			if(validateRule==null) {
				if("password".equals(fieldDefine.getType())) { //密码
					String password = (String)fieldValue;
					if(!password.startsWith("{") || !password.endsWith("}")) {
						//validateRule = "^[^\\%&amp;\\?\\+]+$|^\\x7b(.*)\\x7d$";
					}
				}
				else if("phone".equals(fieldDefine.getType())) { //电话号码
					validateRule = "[0-9_-]{5,20}$";
				}
			}
			if(validateRule!=null) {
				Pattern pattern = Pattern.compile(validateRule); 
		        Matcher matcher = pattern.matcher(fieldValue.toString()); 
		        if(!matcher.matches()) {
		        	errors.add(fieldDefine.getTitle() + "格式无效");
		        }
			}
		}
		return errors.isEmpty() ? null : errors;
	}
	
	/**
	 * 数字校验
	 * @param fieldValue
	 * @param fieldDefine
	 * @param errors
	 */
	private static String validateNumber(Number fieldValue, Field fieldDefine) {
		if(fieldDefine.getLength()!=null) {
			//检查数值长度
			String numberValue = new DecimalFormat("#.############################").format(fieldValue);
			int index = numberValue.indexOf('.');
			String[] lengthValues = fieldDefine.getLength().split(",");
			if(lengthValues.length==1) { //只能是整数
				if(index!=-1) {
					return fieldDefine.getTitle() + "只能是整数";
				}
				else if(numberValue.length()>Integer.parseInt(lengthValues[0])) {
					return fieldDefine.getTitle() + "不能超过" + fieldDefine.getLength() + "位数";
				}
			}
			else if((index==-1 ? numberValue.length() : index)>(Integer.parseInt(lengthValues[0])-Integer.parseInt(lengthValues[1]))) { //检查整数部分是否超标
				return fieldDefine.getTitle() + "整数部分不能超过" + (Integer.parseInt(lengthValues[0])-Integer.parseInt(lengthValues[1])) + "位数";
			}
			else if(index!=-1 && (numberValue.length()-index-1)>Integer.parseInt(lengthValues[1])) { //检查小数部分是否超标
				return fieldDefine.getTitle() + "小数部分不能超过" + lengthValues[1] + "位数";
			}
		}
		//检查数字的最小值和最大值
		String minValue = (String)fieldDefine.getParameter("minValue");
		String maxValue = (String)fieldDefine.getParameter("maxValue");
		if(minValue!=null || maxValue!=null) {
			double numberValue = ((Number)fieldValue).doubleValue();
			if(minValue!=null && maxValue!=null) {
				if(numberValue<Double.parseDouble(minValue) || numberValue>Double.parseDouble(maxValue)) {
					return fieldDefine.getTitle() + "必须介于" + minValue + "和" + maxValue + "之间";
				}
			}
			else if(minValue!=null) {
				if(numberValue<Double.parseDouble(minValue)) {
					return fieldDefine.getTitle() + "必须大等于" + minValue;
				}
			}
			else {
				if(numberValue>Double.parseDouble(maxValue)) {
					return fieldDefine.getTitle() + "必须小等于" + maxValue;
				}
			}
		}
		return null;
	}
}