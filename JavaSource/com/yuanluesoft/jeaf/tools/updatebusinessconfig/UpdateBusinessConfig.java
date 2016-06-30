package com.yuanluesoft.jeaf.tools.updatebusinessconfig;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.parser.dom4j.BusinessDefineParserImpl;
import com.yuanluesoft.jeaf.database.model.Table;
import com.yuanluesoft.jeaf.database.model.TableColumn;
import com.yuanluesoft.jeaf.database.parser.powerdesigner.DatabaseDefineParserImpl;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.util.callback.FileSearchCallback;

/**
 * 更新buisiness-config.xml
 * @author linchuan
 *
 */
public class UpdateBusinessConfig {

	public static void main(String[] args) throws Exception {
		//解析hibernate-config.xml
		final Map mapping = new HashMap(); 
		FileUtils.fileSearch(Environment.getWebinfPath(), "hibernate-config.xml", new FileSearchCallback() {
			public void onFileFound(File file) {
				Pattern pattern = Pattern.compile("class name=\"([^\"]*)\"[^>]*table=\"([^\"]*)\"");
    	        Matcher matcher = pattern.matcher(FileUtils.readStringFromFile(file.getPath(), "utf-8"));
                while(matcher.find()) {
                	mapping.put(matcher.group(1), matcher.group(2));
    	        }
			}
		});
		
		//解析pdm
		final List tables = new ArrayList(); 
		File[] files = new File("D:\\workspace\\cms\\documentation\\开发文档").listFiles();
		for(int i=0; i<files.length; i++) {
			if(!files[i].getName().endsWith(".pdm")) {
				continue;
			}
			tables.addAll(new DatabaseDefineParserImpl().parseTables(files[i].getPath()));
		}
		
		final int[] found = {0};
		FileUtils.fileSearch(Environment.getWebinfPath(), "business-config.xml", new FileSearchCallback() {
			public void onFileFound(File file) {
				String applicationName = file.getPath().replace('\\', '/');
				int index = applicationName.toLowerCase().lastIndexOf("web-inf/");
				applicationName = applicationName.substring(index + "web-inf/".length(), applicationName.lastIndexOf('/'));
				try {
					boolean changed = false;
					List businessObjects = new BusinessDefineParserImpl().parse(applicationName, file.getPath());
					for(Iterator iterator = businessObjects.iterator(); iterator.hasNext();) {
						BusinessObject businessObject = (BusinessObject)iterator.next();
						//获取对应的表
						String tableName = (String)mapping.get(businessObject.getClassName());
						if(tableName==null) {
							continue;
						}
						Table table = (Table)ListUtils.findObjectByProperty(tables, "tableName", tableName);
						if(table==null) {
							continue;
						}
						//检查字段长度
						for(Iterator iteratorColumn = table.getColumns().iterator(); iteratorColumn.hasNext();) {
							TableColumn column = (TableColumn)iteratorColumn.next();
							Field field = businessObject.getField(column.getName());
							if(field==null) {
								continue;
							}
							if(TableColumn.COLUMN_TYPE_CHAR.equals(column.getType())) { //字符
								changed = true;
								field.setType("char");
								field.setLength("1");
								//System.out.println("*************char:" + column.getName() + "," + column.getLength() + "," + field.getType() + "," + field.getLength());
							}
							else if(TableColumn.COLUMN_TYPE_VARCHAR.equals(column.getType())) { //字符串
								if(",string,password,email,phone,html,imageName,videoName,attachmentName,".indexOf("," + field.getType() + ",")==-1) {
									//System.out.println("*************string:" + column.getName() + "," + column.getLength() + "," + field.getType() + "," + field.getLength() + "," + applicationName);
								}
								if(StringUtils.isEquals(column.getLength(), field.getLength())) { //长度相同
									changed = true;
									field.setParameter("singleByteCharacters", "true"); //设置为单字节
								}
								else if(field.getLength()==null) {
									changed = true;
									field.setLength(column.getLength());
								}
								else  if(Integer.parseInt(field.getLength())<=Integer.parseInt(column.getLength())/2) {
									System.out.println("*************string1:" + (++found[0]) +  "," + table.getTableName() + "," + column.getName() + "," + column.getLength() + "," + field.getType() + "," + field.getLength() + "," + applicationName + "," + businessObject.getClassName());
									changed = true;
									field.setLength(column.getLength());
								}
							}
						}
					}
					if(changed) {
						System.out.println((++found[0]) + "、" + file.getPath() + " updated.");
						//new BusinessDefineParserImpl().saveBusinessDefine(businessObjects, file.getPath());
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}