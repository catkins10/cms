package com.yuanluesoft.jeaf.database.parser.dom4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import com.yuanluesoft.jeaf.base.parser.dom4j.XmlParser;
import com.yuanluesoft.jeaf.database.model.Table;
import com.yuanluesoft.jeaf.database.model.TableColumn;
import com.yuanluesoft.jeaf.database.model.TableIndex;
import com.yuanluesoft.jeaf.database.parser.DatabaseDefineParser;
import com.yuanluesoft.jeaf.exception.ParseException;

/**
 * 
 * @author linchuan
 *
 */
public class DatabaseDefineParserImpl extends XmlParser implements DatabaseDefineParser {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.database.parser.DatabaseDefineParser#parseTables(java.lang.String)
	 */
	public List parseTables(String defineFileName) throws ParseException {
		List tables = new ArrayList();
		//解析table-config.xml文件
		Element xmlRoot = parseXmlFile(defineFileName);
		for(Iterator iterator = xmlRoot.elementIterator("table"); iterator.hasNext();) {
			Element xmlTable = (Element)iterator.next();
			tables.add(parseTable(xmlTable));
		}
		return tables;
	}
	
	/**
	 * 解析表
	 * @param xmlTable
	 * @return
	 * @throws ParseException
	 */
	private Table parseTable(Element xmlTable) throws ParseException {
		Table table = new Table();
		table.setTableName(xmlTable.attributeValue("name")); //表名称
		table.setDescription(xmlTable.attributeValue("description")); //描述
		table.setPrimaryKey("id"); //主键
		table.setColumns(new ArrayList()); //列列表
		for(Iterator iterator = xmlTable.elementIterator("column"); iterator.hasNext();) {
			Element xmlColumn = (Element)iterator.next();
			TableColumn tableColumn = new TableColumn();
			tableColumn.setName(xmlColumn.attributeValue("name")); //名称
			tableColumn.setPojoPropertyName(null); //对应的POJO属性名称,为空时和名称相同
			tableColumn.setDescription(xmlColumn.attributeValue("description")); //描述
			tableColumn.setType(xmlColumn.attributeValue("type")); //类型,包括:varchar/text/char/number/date/timestamp
			tableColumn.setLength(xmlColumn.attributeValue("length")); //长度
			tableColumn.setReferenceTable(xmlColumn.attributeValue("referenceTable")); //关联的表
			table.getColumns().add(tableColumn);
		}
		List indexes = new ArrayList(); //索引列表
		for(Iterator iterator = xmlTable.elementIterator("index"); iterator!=null && iterator.hasNext();) {
			Element xmlIndex = (Element)iterator.next();
			TableIndex tableIndex = new TableIndex();
			tableIndex.setIndexName(xmlIndex.attributeValue("name")); //名称
			tableIndex.setIndexColumns(xmlIndex.attributeValue("columns")); //列
			indexes.add(tableIndex);
		}
		table.setIndexes(indexes.isEmpty() ? null : indexes);
		return table;
	}
}