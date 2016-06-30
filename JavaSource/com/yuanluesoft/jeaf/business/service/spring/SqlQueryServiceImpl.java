/*
 * Created on 2004-12-18
 *
 */
package com.yuanluesoft.jeaf.business.service.spring;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.service.SqlQueryService;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.database.SqlResult;
import com.yuanluesoft.jeaf.database.SqlResultList;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class SqlQueryServiceImpl implements SqlQueryService {
	private DatabaseService databaseService; //spring容器加载
	private BusinessDefineService businessDefineService; //业务逻辑定义服务
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.SqlQueryService#listResords(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	public List listResords(String recordName, String sqlSelect, String sqlWhere, String sqlGroupBy, String sqlOrderBy, int offset, int limit) throws ServiceException {
		BusinessObject businessObject = businessDefineService.getBusinessObject(recordName);
		//获取ID字段
		String idColumnName = businessObject.getFieldByParameter("sqlPrimaryKey", "true").getName();
		//如果select子句不为空,添加ID字段
		if(sqlSelect!=null && !sqlSelect.isEmpty() && ("," + sqlSelect.replaceAll("[ \\r\\n]", "") + ",").indexOf("," + idColumnName + ",")==-1) {
			sqlSelect = idColumnName + "," + sqlSelect;
		}
		//生成SQL
		String sql = generateSql(businessObject, sqlSelect, sqlWhere, sqlGroupBy, sqlOrderBy, false);
		List records = databaseService.executeQueryBySql(sql, offset, limit);
		if(records!=null && !records.isEmpty()) {
			for(Iterator iterator=records.iterator(); iterator.hasNext();) {
				SqlResult record = (SqlResult)iterator.next();
				record.put("sqlRecordName", recordName);
				record.put("id", record.get(idColumnName));
			}
		}
		return records;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.SqlQueryService#countResords(java.lang.String, java.lang.String)
	 */
	public int countResords(String recordName, String sqlWhere) throws ServiceException {
		String sql = generateSql(getBusinessDefineService().getBusinessObject(recordName), "count(*) recordCount", sqlWhere, null, null, true);
		List results = databaseService.executeQueryBySql(sql, 0, 0);
		if(results==null || results.isEmpty()) {
			return 0;
		}
		SqlResult result = (SqlResult)results.get(0);
		return result.getInt("recordCount");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.SqlQueryService#getRecord(java.lang.String, java.lang.String)
	 */
	public SqlResult getRecord(String recordName, String recordId) throws ServiceException {
		BusinessObject businessObject = businessDefineService.getBusinessObject(recordName);
		Field primaryKeyField =  businessObject.getFieldByParameter("sqlPrimaryKey", "true");
		String where = primaryKeyField.getName() + "=" + ("number".equals(primaryKeyField.getType()) ? recordId : "'" + recordId + "'");
		//查询主记录
		List records = listResords(recordName, null, where, null, null, 0, 1);
		if(records==null || records.isEmpty()) {
			return null;
		}
		SqlResult record = (SqlResult)records.get(0);
		//获取组成部分列表
		for(Iterator iterator = businessObject.getFields().iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			if(!"components".equals(field.getType())) { //不是组成部分列表
				continue;
			}
			String sqlWhere = StringUtils.fillParameters((String)field.getParameter("sqlWhere"), false, false, true, "utf-8", record, null, null);
			try {
				record.put(field.getName(), listResords((String)field.getParameter("class"), null, sqlWhere, null, null, 0, 0));
			}
			catch(Exception e) {
				Logger.exception(e);
			}
		}
		return record;
	}
	
	/**
	 * 生成SQL
	 * @param businessObject
	 * @param sqlSelect
	 * @param sqlWhere
	 * @param sqlOrderBy
	 * @param countRecord
	 * @return
	 * @throws ServiceException
	 */
	private String generateSql(BusinessObject businessObject, String sqlSelect, String sqlWhere, String sqlGroupBy, String sqlOrderBy, boolean countRecord) throws ServiceException {
		//获取业务逻辑定义
		String sqlFrom = (String)businessObject.getExtendParameter("sqlFrom");
		String alias = sqlFrom.substring(sqlFrom.lastIndexOf(' ') + 1); //表的别名
		
		//处理SELECT子句
		if(sqlSelect==null || sqlSelect.isEmpty()) {
			List selects = new ArrayList();
			generateSqlSelect(selects, businessObject, alias, true);
			sqlSelect = ListUtils.join(selects, ",", false);
		}
		else if(!countRecord) {
			sqlSelect = resetSqlField(sqlSelect, businessObject, alias, "select"); //重置字段名称
		}
		String sql = "select " + sqlSelect + " from " + sqlFrom;
		
		//处理表连接
		for(Iterator iterator = businessObject.getFields().iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			if(!"component".equals(field.getType())) { //不是组成部分
				continue;
			}
			String join = (String)field.getParameter("sqlJoin");
			if(join!=null && !join.isEmpty()) {
				sql += " " + join;
			}
		}
		
		//处理查询条件
		sqlWhere = resetSqlField(sqlWhere, businessObject, alias, "where"); //重置字段名称
		String where = (String)businessObject.getExtendParameter("sqlWhere");
		if(where==null || where.isEmpty()) {
			where = sqlWhere;
		}
		else if(sqlWhere!=null && !sqlWhere.isEmpty()) {
			where = "(" + where + ") and (" + sqlWhere + ")";
		}
		if(where!=null && !where.isEmpty()) {
			sql += " where " + where;
		}
		
		//处理分组
		sqlGroupBy = resetSqlField(sqlGroupBy, businessObject, alias, "group by"); //重置字段名称
		String groupBy = (String)businessObject.getExtendParameter("sqlGroupBy");
		if(groupBy==null || groupBy.isEmpty()) {
			groupBy = sqlGroupBy;
		}
		else if(sqlGroupBy!=null && !sqlGroupBy.isEmpty()) {
			groupBy = sqlGroupBy + ", " + groupBy;
		}
		if(groupBy!=null && !groupBy.isEmpty()) {
			sql += " group by " + groupBy;
		}
		
		//处理排序
		if(!countRecord) {
			sqlOrderBy = resetSqlField(sqlOrderBy, businessObject, alias, "order by"); //重置字段名称
			String orderBy = (String)businessObject.getExtendParameter("sqlOrderBy");
			if(orderBy==null || orderBy.isEmpty()) {
				orderBy = sqlOrderBy;
			}
			else if(sqlOrderBy!=null && !sqlOrderBy.isEmpty()) {
				orderBy = sqlOrderBy + ", " + orderBy;
			}
			if(orderBy!=null && !orderBy.isEmpty()) {
				sql += " order by " + orderBy;
			}
		}
		return sql;
	}
	
	/**
	 * 递归函数:生成SQL选择语句
	 * @param selects
	 * @param businessObject
	 * @param alias
	 * @param mainTable
	 * @throws ServiceException
	 */
	private void generateSqlSelect(List selects, BusinessObject businessObject, String alias, boolean mainTable) throws ServiceException {
		for(Iterator iterator = businessObject.getFields().iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			if(!field.isPersistence() || ",image,attachment,video,components,".indexOf("," + field.getType() + ",")!=-1) { //图片、附件、视频、组成部分列表
				continue;
			}
			if("component".equals(field.getType())) { //组成部分
				if(!mainTable) { //不是主表,不处理
					continue;
				}
				//递归调用
				generateSqlSelect(selects, getBusinessDefineService().getBusinessObject((String)field.getParameter("class")), field.getName(), false);
				continue;
			}
			String fieldName;
			//检查是否有SQL参数
			String sqlParameter = (String)field.getParameter("sql");
			if(sqlParameter!=null && !sqlParameter.isEmpty()) {
				fieldName = "(" + sqlParameter.replaceAll("\\{ALIAS\\}", alias) + ") " + (mainTable ? "" : alias + "_") + field.getName();
			}
			else {
				fieldName = alias + "." + field.getName() + " " + (mainTable ? "" : alias + "_") + field.getName();
			}
			selects.add(fieldName);
		}
	}
	
	/**
	 * 重置SQL子句中的字段名称, 1、替换"T.NAME"为"T.NAME T_NAME" 2、生成子查询, 注:where不允许子查询
	 * @param sql
	 * @param businessObject
	 * @param alias
	 * @param clauseType 子句类型: select/where/order by/group by
	 * @return
	 * @throws ServiceException
	 */
	private String resetSqlField(String sql, BusinessObject businessObject, String alias, String clauseType) throws ServiceException {
		if(sql==null || sql.isEmpty()) {
			return sql;
		}
		String postfix; //字段名称后允许出现的内容
		String prohibition = "("; //字段名称中不允许出现的字符
		String[] keywords; //关键字
		if("where".equals(clauseType)) {
			postfix = "), \r\n\t!=<>+-*/";
			keywords = new String[] {"and", "or", "not", "like", "between", "is", "null"};
		}
		else {
			postfix = ", \r\n\t";
			keywords = new String[]{"desc", "asc"};
		}
		int beginIndex = 0;
		String newSql = "";
		for(int i = beginIndex; i!=-1 && i<sql.length(); i++) {
			if(sql.charAt(i)=='\'') { //单引号
				//向后查找下一个单引号
				if((i = sql.indexOf('\'', i+1))==-1) {
					break;
				}
				continue;
			}
			if(!Character.isLetter(sql.charAt(i))) { //不是字母
				continue;
			}
			//向后查找字段的结尾
			int endIndex = i+1;
			for(; endIndex < sql.length(); endIndex++) {
				if(prohibition.indexOf(sql.charAt(endIndex))!=-1) { //禁止出现的字符
					i = endIndex;
					endIndex = -1;
					break;
				}
				if(postfix.indexOf(sql.charAt(endIndex))!=-1) { //找到结尾
					break;
				}
			}
			if(endIndex==-1) { //出现被禁止的字符
				continue;
			}
			int fieldBegin = i;
			i = endIndex;
			String[] fieldNames = sql.substring(fieldBegin, endIndex).split("\\."); //字段名称
			if(fieldNames.length>2) {
				continue;
			}
			//检查是否关键字
			if(fieldNames.length==1) {
				int j = keywords.length - 1;
				for(; j>=0 && !fieldNames[0].equalsIgnoreCase(keywords[j]); j--);
				if(j>=0) { //是关键字
					continue;
				}
			}
			//获取字段定义
			Field field = businessObject.getField(fieldNames[0]);
			if(field==null) {
				if(fieldNames[0].equals(businessObject.getClassName())) {
					fieldNames = new String[] {fieldNames[1]};
					field = businessObject.getField(fieldNames[0]);
				}
				if(field==null) { //不是数据库字段
					continue;
				}
			}
			if(fieldNames.length==2) {
				field = getBusinessDefineService().getBusinessObject((String)field.getParameter("class")).getField(fieldNames[1]);
				if(field==null) { //不是数据库字段
					continue;
				}
			}
			newSql += sql.substring(beginIndex, fieldBegin);
			beginIndex = endIndex;
			String sqlParameter = (String)field.getParameter("sql");
			if(sqlParameter!=null && !sqlParameter.isEmpty()) {
				newSql += "(" + sqlParameter.replaceAll("\\{ALIAS\\}", fieldNames.length==1 ? alias : fieldNames[0]) + ")";
			}
			else if(fieldNames.length==1) { //主表
				newSql += alias + "." + fieldNames[0];
			}
			else {
				newSql += fieldNames[0] + "." + fieldNames[1];
			}
			if("select".equals(clauseType)) { //select子句
				newSql += " " + fieldNames[0] + (fieldNames.length==1 ? "" : "_" + fieldNames[1]);
			}
		}
		return newSql + sql.substring(beginIndex);
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		String itemsSql = (String)fieldDefine.getParameter("itemsSql");
		if(itemsSql!=null) {
			SqlResultList items = databaseService.executeQueryBySql(itemsSql, 0, 0);
			if(items==null || items.isEmpty()) {
				return null;
			}
			//转换为数组
			for(int i=0; i<items.size(); i++) {
				SqlResult record = (SqlResult)items.get(i);
				Object[] values = (items.getFieldNames().size()==1 ? new Object[]{record.get((String)items.getFieldNames().get(0))} : new Object[]{record.get((String)items.getFieldNames().get(0)), record.get((String)items.getFieldNames().get(1))});
				items.set(i, values);
			}
			return items;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#load(java.lang.Class, long)
	 */
	public Record load(Class recordClass, long id) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#save(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record save(Record record) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#update(com.yuanluesoft.jeaf.database.Record)
	 */
	public Record update(Record record) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#delete(com.yuanluesoft.jeaf.database.Record)
	 */
	public void delete(Record record) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#processExchangedObject(java.lang.Object, java.lang.String)
	 */
	public Serializable processExchangedObject(Object object, String senderName) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#synchUpdate(java.lang.Object, java.lang.String)
	 */
	public void synchUpdate(Object object, String senderName) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#synchDelete(java.lang.Object, java.lang.String)
	 */
	public void synchDelete(Object object, String senderName) throws ServiceException {
		
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#validateBusiness(com.yuanluesoft.jeaf.database.Record)
	 */
	public List validateBusiness(Record record, boolean isNew) throws ServiceException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.BusinessService#validateDataIntegrity(com.yuanluesoft.jeaf.database.Record)
	 */
	public List validateDataIntegrity(Record record, boolean isNew) throws ServiceException {
		return null;
	}

	/**
	 * @return the businessDefineService
	 */
	public BusinessDefineService getBusinessDefineService() {
		return businessDefineService;
	}

	/**
	 * @param businessDefineService the businessDefineService to set
	 */
	public void setBusinessDefineService(BusinessDefineService businessDefineService) {
		this.businessDefineService = businessDefineService;
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