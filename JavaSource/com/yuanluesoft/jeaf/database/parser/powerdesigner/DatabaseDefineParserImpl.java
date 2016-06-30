package com.yuanluesoft.jeaf.database.parser.powerdesigner;

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
		//解析pdm文件
		Element xmlRoot = parseXmlFile(defineFileName);
		for(Iterator iterator = xmlRoot.element("RootObject").element("Children").element("Model").element("Tables").elementIterator("Table"); iterator.hasNext();) {
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
		table.setTableName(xmlTable.elementText("Code")); //表名称
		table.setDescription(xmlTable.elementText("Name")); //描述
		//table.setPojoClassName(pojoClassName); //POJO类名称
		table.setPrimaryKey("id"); //主键
		table.setIndexes(new ArrayList()); //索引列表
		for(Iterator iterator = xmlTable.element("Indexes")==null ? null : xmlTable.element("Indexes").elementIterator(); iterator!=null && iterator.hasNext();) {
			Element xmlIndex = (Element)iterator.next();
			if(xmlIndex.element("IndexColumns")==null) {
				continue;
			}
			TableIndex tableIndex = new TableIndex();
			tableIndex.setIndexName(xmlIndex.elementText("Code")); //名称
			String indexColumns = null;
			for(Iterator iteratorIndexColumn = xmlIndex.element("IndexColumns").elementIterator(); iteratorIndexColumn.hasNext();) {
				Element xmlIndexColumn = (Element)iteratorIndexColumn.next();
				String columnId = xmlIndexColumn.element("Column").element("Column").attributeValue("Ref");
				indexColumns = (indexColumns==null ? "" : indexColumns + ", ") + "{" + columnId + "}" + ("0".equals(xmlIndexColumn.elementText("Ascending")) ? " DESC" : "");
			}
			if(indexColumns!=null) {
				tableIndex.setIndexColumns(indexColumns); //列
				table.getIndexes().add(tableIndex);
			}
		}
		table.setColumns(new ArrayList()); //列列表
		for(Iterator iterator = xmlTable.element("Columns").elementIterator(); iterator.hasNext();) {
			Element xmlColumn = (Element)iterator.next();
			TableColumn tableColumn = new TableColumn();
			tableColumn.setName(xmlColumn.elementText("Code")); //名称
			tableColumn.setPojoPropertyName(null); //对应的POJO属性名称,为空时和名称相同
			String comment = xmlColumn.elementText("Comment");
			tableColumn.setDescription(xmlColumn.elementText("Name") + (comment==null ? "" : "," + comment)); //描述
			String dataType = xmlColumn.elementText("DataType").toLowerCase();
			if(dataType.startsWith("varchar")) {
				tableColumn.setType(TableColumn.COLUMN_TYPE_VARCHAR); //类型,包括:varchar/text/char/number/date/timestamp
			}
			else if(dataType.startsWith("char")) {
				tableColumn.setType(TableColumn.COLUMN_TYPE_CHAR); //类型,包括:varchar/text/char/number/date/timestamp
			}
			else if(dataType.indexOf("text")!=-1 || dataType.startsWith("clob")) {
				tableColumn.setType(TableColumn.COLUMN_TYPE_TEXT); //类型,包括:varchar/text/char/number/date/timestamp
			}
			else if(dataType.startsWith("number") || dataType.startsWith("numeric")) {
				tableColumn.setType(TableColumn.COLUMN_TYPE_NUMBER); //类型,包括:varchar/text/char/number/date/timestamp
			}
			else if(dataType.startsWith("date")) {
				tableColumn.setType(TableColumn.COLUMN_TYPE_DATE); //类型,包括:varchar/text/char/number/date/timestamp
			}
			else if(dataType.startsWith("timestamp")) {
				tableColumn.setType(TableColumn.COLUMN_TYPE_TIMESTAMP); //类型,包括:varchar/text/char/number/date/timestamp
			}
			int index = dataType.indexOf('(');
			tableColumn.setLength(index==-1 ? null : dataType.substring(index + 1, dataType.lastIndexOf(')'))); //长度
			table.getColumns().add(tableColumn);
			//更新索引字段名称
			for(Iterator iteratorIndex = table.getIndexes().iterator(); iteratorIndex.hasNext();) {
				TableIndex tableIndex = (TableIndex)iteratorIndex.next();
				tableIndex.setIndexColumns(tableIndex.getIndexColumns().replaceAll("\\{" + xmlColumn.attributeValue("Id") + "\\}", tableColumn.getName()));
			}
		}
		if(table.getIndexes().isEmpty()) {
			table.setIndexes(null);
		}
		return table;
	}
}