package com.yuanluesoft.cms.onlineservice.zzbm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.util.BeanUtils;

/**
 * 漳州行政服务中心记录解析器
 * @author linchuan
 *
 */
public class ZzbmRecordParser {
	
	/**
	 * 解析数据文件
	 * @param dataFilePath
	 * @throws ParseException
	 */
	public static void parseDataFile(String dataFilePath, Class recordClass, String[] fieldNames, String[][] fieldMappings, ZzbmRecordParseCallback recordParseCallback) throws ParseException {
		List fields = new ArrayList();
		for(int i=0; i<fieldNames.length; i++) {
			Field field = new Field();
			field.setName(fieldNames[i]);
			field.setDelimiter(i==fieldNames.length-1 ? "\r\n" : "$");
			fields.add(field);
		}
		FileInputStream input = null;
		InputStreamReader fileReader = null;
		BufferedReader reader = null;
		try {
			input = new FileInputStream(dataFilePath);
			fileReader = new InputStreamReader(input, "gbk");
			reader = new BufferedReader(fileReader);
			char[] buffer = new char[4096]; //4k
			int readLen;
			Map propertyTextValues = new LinkedHashMap();
			int fieldIndex = 0;
			String text = "";
			while((readLen=reader.read(buffer))!=-1) {
				text += new String(buffer, 0, readLen);
				for(; fieldIndex<fields.size(); fieldIndex++) {
					Field field = (Field)fields.get(fieldIndex);
					int index = text.indexOf(field.getDelimiter()); //查找字段的结束符
					if(index==-1) { //找不到字段的结束位置
						break;
					}
					propertyTextValues.put(field.getName(), text.substring(0, index).trim());
					text = text.substring(index + field.getDelimiter().length());
					if(fieldIndex==fields.size()-1) { //最后一个字段
						processParsedRecord(propertyTextValues, recordClass, fieldMappings, recordParseCallback);
						propertyTextValues = new LinkedHashMap();
						fieldIndex = -1;
					}
				}
			}
			processParsedRecord(propertyTextValues, recordClass, fieldMappings, recordParseCallback);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		finally {
			try {
				reader.close();
			}
			catch(Exception e) {
				
			}
			try {
				fileReader.close();
			}
			catch(Exception e) {
				
			}
			try {
				input.close();
			}
			catch(Exception e) {
				
			}
		}
	}
		
	/**
	 * 保存申报记录
	 * @param record
	 * @throws ServiceException
	 */
	private static void processParsedRecord(Map propertyTextValues, Class recordClass, String[][] fieldMappings, ZzbmRecordParseCallback recordParseCallback) throws ServiceException {	
		Object record = null;
		try {
			record = recordClass.newInstance();
		}
		catch(Exception e) {
		
		}
		//设置申报属性
		for(int i=0; i<fieldMappings.length; i++) {
			BeanUtils.setPropertyValue(record, fieldMappings[i][1], (String)propertyTextValues.get(fieldMappings[i][0]), fieldMappings[i][2]);
		}
		//回调
		try {
			recordParseCallback.processParsedRecord(propertyTextValues, record);
		}
		catch(Exception e) {
			Logger.exception(e);
		}
	}
	
	/**
	 * 解析字段定义
	 * @param fileName
	 * @return
	 * @throws ServiceException
	 */
	protected List parseFields(String fileName) throws ServiceException {
		//从第三行开始解析
		//格式	10      SQLCHAR       0       1       "$"      10    caseM_append_out                         Chinese_PRC_CI_AS
		//		11      SQLCHAR       0       12      "\r\n"   11    case_statusId                            ""
		FileInputStream input = null;
		InputStreamReader fileReader = null;
		BufferedReader reader = null;
		List fields = new ArrayList();
		try {
			input = new FileInputStream(fileName);
			fileReader = new InputStreamReader(input, "gbk");
			reader = new BufferedReader(fileReader);
			String line;
			int i = 0;
			while((line=reader.readLine())!=null && !line.isEmpty()) {
				if((i++)<2) {
					continue;
				}
				Field field = new Field();
				field.setDelimiter(line.substring(39, line.indexOf("\" ", 40)).replaceAll("\\\\r", "\r").replaceAll("\\\\n", "\n"));
				field.setName(line.substring(53, line.indexOf(" ", 53)));
				fields.add(field);
			}
		}
		catch(Exception e) {
			Logger.exception(e);
		}
		finally {
			try {
				reader.close();
			}
			catch(Exception e) {
				
			}
			try {
				fileReader.close();
			}
			catch(Exception e) {
				
			}
			try {
				input.close();
			}
			catch(Exception e) {
				
			}
		}
		return fields;
	}
	
	/**
	 * 字段定义
	 * @author linchuan
	 *
	 */
	private static class Field {
		private String name; //字段名称
		private String delimiter; //分隔符
		
		/**
		 * @return the delimiter
		 */
		public String getDelimiter() {
			return delimiter;
		}
		/**
		 * @param delimiter the delimiter to set
		 */
		public void setDelimiter(String delimiter) {
			this.delimiter = delimiter;
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
	}
}