/*
 * Created on 2005-9-16
 *
 */
package com.yuanluesoft.j2oa.document.service.spring;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.yuanluesoft.j2oa.document.pojo.Keyword;
import com.yuanluesoft.j2oa.document.pojo.KeywordCategory;
import com.yuanluesoft.j2oa.document.service.KeywordService;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.database.Record;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.system.services.InitializeService;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 
 * @author linchuan
 *
 */
public class KeywordServiceImpl extends BusinessServiceImpl implements KeywordService,InitializeService {

	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.system.services.InitializeService#init(java.lang.String, long, java.lang.String)
	 */
	public boolean init(String systemName, long managerId, String managerName) throws ServiceException {
		if(getDatabaseService().findRecordByHql("from Keyword Keyword")!=null) {
			return true;
		}
		//初始化主题词库
		String url = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=" + new File(Environment.getWebinfPath() + "j2oa/document/keywords/keywords.mdb").getAbsolutePath() + ";";
		Connection connection = null;
		Statement statement = null;
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");  
			connection = DriverManager.getConnection(url, "", "");
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from 主题词");
			while(resultSet.next()) {
				KeywordCategory category = new KeywordCategory();
				category.setId(UUIDLongGenerator.generateId());
				category.setDistrict(resultSet.getString("区域分类").trim());
				category.setCategory(resultSet.getString("类别词").trim());
				String[] keywordList = resultSet.getString("主题词").replaceAll("\n", " ").replaceAll(",", " ").replaceAll("，", " ").split(" ");
				Set keywords = new HashSet();
				for(int i=0; i<keywordList.length; i++) {
					keywordList[i] = keywordList[i].trim();
					if(!keywordList[i].equals("")) {
						Keyword keyword = new Keyword();
						keyword.setId(UUIDLongGenerator.generateId()); //ID
						keyword.setCategoryId(category.getId()); //分类ID
						keyword.setKeyword(keywordList[i].trim()); //主题词
						keyword.setKeywordIndex(i); //序号
						keywords.add(keyword);
					}
				}
				category.setKeywords(keywords);
				save(category);
			}
			resultSet.close();
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException(e.getMessage()); 
		}
		finally {
			try {
				statement.close();
			}
			catch(Exception ex) {

			}
			try {
				connection.close();
			}
			catch(Exception ex) {

			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.service.BaseService#save(java.lang.Object)
	 */
	public Record save(Record record) throws ServiceException {
	    KeywordCategory category = (KeywordCategory)record;
	    Record oldRecord;
	    if((oldRecord=(Record)getDatabaseService().findRecordByHql("from KeywordCategory KeywordCategory where KeywordCategory.district='" + JdbcUtils.resetQuot(category.getDistrict()) + "' and KeywordCategory.category='" + JdbcUtils.resetQuot(category.getCategory()) + "'"))!=null) {
	        getDatabaseService().deleteRecord(oldRecord);
	    }
	    super.save(record);
		saveKeywors(((KeywordCategory)record).getKeywords());
		return record;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.service.BaseService#update(java.lang.Object)
	 */
	public Record update(Record record) throws ServiceException {
		KeywordCategory category = (KeywordCategory)record;
		getDatabaseService().deleteRecordsByHql("from Keyword Keyword where Keyword.categoryId=" + category.getId());
		getDatabaseService().flush();
		saveKeywors(category.getKeywords());
		return super.update(record);
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.core.service.BaseService#validateDataIntegrity(java.lang.Object)
	 */
	public List validateDataIntegrity(Record record, boolean isNew) throws ServiceException {
		KeywordCategory category = (KeywordCategory)record;
		DatabaseService databaseService = getDatabaseService();
		List errors = new ArrayList();
		KeywordCategory categoryCheck = (KeywordCategory)databaseService.findRecordByKey(KeywordCategory.class.getName(), "category", category.getCategory());
		if(categoryCheck!=null && categoryCheck.getId()!=category.getId()) {
			errors.add("类别词“" + category.getCategory() + "”已存在，类别词不允许重复");
		}
		for(Iterator iterator = category.getKeywords().iterator(); iterator.hasNext();) {
			Keyword keyword = (Keyword)iterator.next();
			if(ListUtils.getSubListByProperty(category.getKeywords(), "keyword", keyword.getKeyword()).size()>1) {
				errors.add("主题词“" + keyword.getKeyword() + "”重复");
				break;
			}
			else {
				Keyword keywordCheck = (Keyword)databaseService.findRecordByKey(Keyword.class.getName(), "keyword", keyword.getKeyword());
				if(keywordCheck!=null && keywordCheck.getCategoryId()!=category.getId()) {
					errors.add("主题词“" + keyword.getKeyword() + "”已存在，主题词不允许重复");
				}
			}
		}
		return errors.isEmpty() ? null : errors;
	}
	
	/**
	 * 保存主题词
	 * @param memberSet
	 */
	private void saveKeywors(Set keywords) {
		if(keywords==null) {
			return;
		}
		DatabaseService databaseService = getDatabaseService();
		for(Iterator iterator=keywords.iterator(); iterator.hasNext();) {
			Keyword keywordConfig = (Keyword)iterator.next();
			databaseService.saveRecord(keywordConfig);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.business.service.spring.BusinessServiceImpl#listSelectItems(java.lang.String, long, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public List listSelectItems(String itemsName, Object bean, Field fieldDefine, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if("district".equals(itemsName)) {
			return getDatabaseService().findRecordsByHql("select distinct KeywordCategory.district from KeywordCategory KeywordCategory");
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.erp.portal.service.KeywordService#parseKeyword(java.lang.String)
	 */
	public String parseKeyword(String text) throws ServiceException {
		//String hql = "select Keyword.keyword, KeywordCategory.category from KeywordCategory KeywordCategory left join KeywordCategory.keywords Keyword where '" + text.replaceAll("'", "") + "' like '%' || Keyword.keyword) || '%' order by KeywordCategory.category";
		String hql = "select Keyword.keyword, KeywordCategory.category from KeywordCategory KeywordCategory left join KeywordCategory.keywords Keyword where '" + text.replaceAll("'", "") + "' like concat(concat('%' , Keyword.keyword), '%') order by KeywordCategory.category";
		List matchingKeywords = getDatabaseService().findRecordsByHql(hql);
		List sortedCategories = new ArrayList();
		if(matchingKeywords==null) {
			return null;
		}
		for(Iterator iterator = matchingKeywords.iterator(); iterator.hasNext();) {
			Object[] values = (Object[])iterator.next();
			//检查主题词是否出现在别的主题词中
			int k = 0;
			for(; k<matchingKeywords.size() && (((Object[])matchingKeywords.get(k))[0].equals(values[0]) || ((String)((Object[])matchingKeywords.get(k))[0]).indexOf((String)values[0])==-1); k++);
			if(k<matchingKeywords.size()) {
				iterator.remove();
				continue;
			}
			int index = text.indexOf((String)values[0]);
			SortedKeyword keyword = new SortedKeyword();
			keyword.index = index;
			keyword.keyword = (String)values[0];
			int i = 0;
			for(; i<sortedCategories.size() && !((SortedKeywordCategory)sortedCategories.get(i)).category.equals(values[1]); i++);
			SortedKeywordCategory category = (i==sortedCategories.size() ? null : (SortedKeywordCategory)sortedCategories.get(i));
			if(category==null) {
				category = new SortedKeywordCategory();
				category.minIndex = index;
				category.category = (String)values[1];
				category.sortedKeywords.add(keyword);
				//根据index将类别词插入到队列
				for(i=0; i<sortedCategories.size() && ((SortedKeywordCategory)sortedCategories.get(i)).minIndex<=index; i++);
				sortedCategories.add(i, category);
			}
			else if(category.minIndex>index) {
				category.minIndex = index;
				category.sortedKeywords.add(0, keyword);
				//重新调整类别词位置
				sortedCategories.remove(i);
				for(i = 0; i<sortedCategories.size() && ((SortedKeywordCategory)sortedCategories.get(i)).minIndex<=index; i++);
				sortedCategories.add(i, category);
			}
			else {
				//根据index将关键字插入到主题词列表
				for(i=0; i<category.sortedKeywords.size() && ((SortedKeyword)category.sortedKeywords.get(i)).index<=index; i++);
				category.sortedKeywords.add(i, keyword);
			}
		}
		//输出解析结果
		String result = null;
		for(int i=0; i<sortedCategories.size(); i++) {
			SortedKeywordCategory category = (SortedKeywordCategory)sortedCategories.get(i);
			if(i==sortedCategories.size()-1 && category.sortedKeywords.size()==1 && category.category.equals("文秘工作")) {
				result = (result==null ? "" : result + " ") + ((SortedKeyword)category.sortedKeywords.get(0)).keyword.trim();
			}
			else {
				result = (result==null ? "" : result + " ") + category.category.trim();
				for(Iterator iterator = category.sortedKeywords.iterator(); iterator.hasNext();) {
					SortedKeyword keyword = (SortedKeyword)iterator.next();
					if(!keyword.keyword.equals(category.category)) { //主题词和类别词相同时不输出
						result += " " + keyword.keyword.trim();
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 类别词排序
	 * @author linchuan
	 *
	 */
	private class SortedKeywordCategory {
		public String category;
		public int minIndex;
		public List sortedKeywords = new ArrayList();
	}
	
	/**
	 * 主题词排序
	 * @author linchuan
	 *
	 */
	private class SortedKeyword {
		public String keyword;
		public int index;
	}
}
