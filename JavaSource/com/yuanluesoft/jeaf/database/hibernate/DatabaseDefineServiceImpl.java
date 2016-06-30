package com.yuanluesoft.jeaf.database.hibernate;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultDocumentType;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.cache.Cache;
import com.yuanluesoft.jeaf.database.DatabaseDefineService;
import com.yuanluesoft.jeaf.database.model.Table;
import com.yuanluesoft.jeaf.database.model.TableColumn;
import com.yuanluesoft.jeaf.database.model.TableManyToOneColumn;
import com.yuanluesoft.jeaf.database.model.TableOneToManyColumn;
import com.yuanluesoft.jeaf.database.parser.DatabaseDefineParser;
import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.FileUtils;
import com.yuanluesoft.jeaf.util.ListUtils;

/**
 * 
 * @author linchuan
 *
 */
public class DatabaseDefineServiceImpl implements DatabaseDefineService {
	private DatabaseDefineParser databaseDefineParser; //数据库定义解析器
	private Cache cache; //缓存

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseDefineService#listTables(java.lang.String)
	 */
	public List listTables(String applicationName) throws ServiceException {
		try {
			String cacheKey = "table_" + applicationName;
			List tables = (List)cache.get(cacheKey);
			if(tables==null) {
				tables = databaseDefineParser.parseTables(Environment.getWebinfPath() + applicationName + "/table-config.xml");
				if(tables!=null) {
					cache.put(cacheKey, tables);
				}
			}
			return tables;
		}
		catch(Exception e) {
			throw new ServiceException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseDefineService#saveDatabaseDefine(java.lang.String, java.util.List)
	 */
	public void saveDatabaseDefine(String applicationName, List tables) throws ServiceException {
		Document xmlDocument = DocumentHelper.createDocument();
		xmlDocument.setDocType(new DefaultDocumentType("hibernate-mapping", "-//Hibernate/Hibernate Mapping DTD 2.0//EN", "http://hibernate.sourceforge.net/hibernate-mapping-2.0.dtd"));
		Element xmlHibernateMappings = xmlDocument.addElement("hibernate-mapping");
		for(Iterator iterator = tables==null ? null : tables.iterator(); iterator!=null && iterator.hasNext();) {
			Table table = (Table)iterator.next();
			generateTableXML(table, xmlHibernateMappings);
		}
		//保存XML文件
		XmlParser xmlParser = new XmlParser(); //XML解析器
		try {
			xmlParser.saveXmlFile(xmlDocument, FileUtils.createDirectory(Environment.getWebinfPath() + applicationName) + "hibernate-config.xml");
		}
		catch (ParseException e) {
			throw new ServiceException(e);
		}
		registApplication(applicationName); //注册
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.DatabaseDefineService#registApplication(java.lang.String)
	 */
	public void registApplication(String applicationName) throws ServiceException {
		//添加到WEB-INF/hibernate-config.xml
		String hibernateConfig = FileUtils.readStringFromFile(Environment.getWebinfPath() + "hibernate-config.xml", "utf-8");
		if(hibernateConfig.indexOf(applicationName)==-1) {
			hibernateConfig = hibernateConfig.replaceFirst("(<property name=\"mappingResources\">[\\s\\S]*?<list>)", "$1\n\t\t\t\t<value>../" + applicationName + "/hibernate-config.xml</value>");
			try {
				FileUtils.saveDataToFile(Environment.getWebinfPath() + "hibernate-config.xml", hibernateConfig.getBytes("utf-8"));
			}
			catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}
		}
	}

	/**
	 * 生成表XML
	 * @param table
	 * @param parentElement
	 * @throws ServiceException
	 */
	private void generateTableXML(Table table, Element parentElement) throws ServiceException {
		if(table.getDescription()!=null && !table.getDescription().isEmpty()) {
			parentElement.addComment(table.getDescription()); //描述
		}
		Element xmlTable = parentElement.addElement("class");
		xmlTable.addAttribute("name", table.getPojoClassName()); //类名称
		xmlTable.addAttribute("table", table.getTableName()); //表名称
		xmlTable.addAttribute(" polymorphism", "explicit"); //多态性
		//ID字段
		if(table.getPrimaryKey()==null) {
			table.setPrimaryKey("id");
		}
		TableColumn idColumn = (TableColumn)ListUtils.findObjectByProperty(table.getColumns(), "name", table.getPrimaryKey());
		Element xmlId = xmlTable.addElement("id");
		xmlId.addAttribute("name", idColumn.getName());
		xmlId.addAttribute("column", idColumn.getName());
		xmlId.addAttribute("type", "long");
		xmlId.addElement("generator").addAttribute("class", "assigned");
		//添加其他字段
		for(Iterator iterator = table.getColumns().iterator(); iterator.hasNext();) {
			TableColumn column = (TableColumn)iterator.next();
			if(table.getPrimaryKey().equals(column.getName())) { //主键,不再处理
				continue;
			}
			if(column.getDescription()!=null && !column.getDescription().isEmpty()) {
				xmlTable.addComment(column.getDescription());
			}
			if(column instanceof TableOneToManyColumn) { //一对多
				TableOneToManyColumn oneToManyColumn = (TableOneToManyColumn)column;
				Element xmlOneToMany = xmlTable.addElement("set");
				xmlOneToMany.addAttribute("name", oneToManyColumn.getName());
				xmlOneToMany.addAttribute("table", oneToManyColumn.getTableName());
				xmlOneToMany.addAttribute("lazy", oneToManyColumn.isLazy() + "");
				xmlOneToMany.addAttribute("inverse", "true");
				xmlOneToMany.addAttribute("cascade", "delete");
				xmlOneToMany.addAttribute("order-by", oneToManyColumn.getOrderBy());
				xmlOneToMany.addElement("key").addAttribute("column", oneToManyColumn.getForeignKey());
				xmlOneToMany.addElement("one-to-many").addAttribute("class", oneToManyColumn.getPojoClassName());
			}
			else if(column instanceof TableManyToOneColumn) { //多对一
				TableManyToOneColumn manyToOneColumn = (TableManyToOneColumn)column;
				Element xmlManyToOne = xmlTable.addElement("many-to-one");
				xmlManyToOne.addAttribute("name", manyToOneColumn.getName());
				xmlManyToOne.addAttribute("class", manyToOneColumn.getPojoClassName());
				xmlManyToOne.addAttribute("column", manyToOneColumn.getColumnName());
				xmlManyToOne.addAttribute("update", "false");
				xmlManyToOne.addAttribute("insert", "false");
			}
			else {
				Element xmlColumn = xmlTable.addElement("property");
				xmlColumn.addAttribute("name", column.getPojoPropertyName()==null ? column.getName() : column.getPojoPropertyName());
				if(column.getPojoPropertyName()!=null) {
					xmlColumn.addAttribute("column", column.getName());
				}
				if(TableColumn.COLUMN_TYPE_TEXT.equals(column.getType())) {
					xmlColumn.addAttribute("type", "com.yuanluesoft.jeaf.database.hibernate.StringClobType");
				}
			}
		}
	}

	/**
	 * @return the databaseDefineParser
	 */
	public DatabaseDefineParser getDatabaseDefineParser() {
		return databaseDefineParser;
	}

	/**
	 * @param databaseDefineParser the databaseDefineParser to set
	 */
	public void setDatabaseDefineParser(DatabaseDefineParser databaseDefineParser) {
		this.databaseDefineParser = databaseDefineParser;
	}

	/**
	 * @return the cache
	 */
	public Cache getCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}
}