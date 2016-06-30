/*
 * Created on 2006-3-25
 *
 */
package com.yuanluesoft.jeaf.view.service.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.config.ActionConfig;

import com.yuanluesoft.eai.client.EAIClient;
import com.yuanluesoft.jeaf.business.model.BusinessObject;
import com.yuanluesoft.jeaf.business.model.Field;
import com.yuanluesoft.jeaf.business.service.BusinessDefineService;
import com.yuanluesoft.jeaf.business.util.FieldUtils;
import com.yuanluesoft.jeaf.database.DatabaseService;
import com.yuanluesoft.jeaf.exception.PrivilegeException;
import com.yuanluesoft.jeaf.exception.ServiceException;
import com.yuanluesoft.jeaf.logger.Logger;
import com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo;
import com.yuanluesoft.jeaf.util.DateTimeUtils;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.JdbcUtils;
import com.yuanluesoft.jeaf.util.JsonUtils;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.util.StringUtils;
import com.yuanluesoft.jeaf.view.model.Category;
import com.yuanluesoft.jeaf.view.model.Column;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.ViewPackage;
import com.yuanluesoft.jeaf.view.model.search.Condition;
import com.yuanluesoft.jeaf.view.model.viewaction.NewWorkflowInstanceAction;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewActionGroup;
import com.yuanluesoft.jeaf.view.service.ViewService;
import com.yuanluesoft.jeaf.view.viewcustomize.service.ViewCustomizeService;
import com.yuanluesoft.jeaf.word.service.WordService;
import com.yuanluesoft.jeaf.workflow.service.WorkflowExploitService;
import com.yuanluesoft.workflow.client.exception.WorkflowException;

/**
 * 
 * @author linchuan
 * 
 */
public class ViewServiceImpl implements ViewService {
	private EAIClient eaiClient; //EAI消费者
	private ViewCustomizeService viewCustomizeService; //自定义服务
	private WorkflowExploitService workflowExploitService; //工作流利用服务
	private DatabaseService databaseService; //数据库访问
	private BusinessDefineService businessDefineService; //业务逻辑定义服务
	
    /*
     * (non-Javadoc)
     * @see com.yuanluesoft.jeaf.view.service.ViewService#retrieveViewPackage(com.yuanluesoft.jeaf.view.model.ViewPackage, com.yuanluesoft.jeaf.view.model.View, int, boolean, boolean, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
     */
	public void retrieveViewPackage(ViewPackage viewPackage, View view, int beginRow, boolean retrieveDataOnly, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException, PrivilegeException {
		viewPackage.setView(view); //设置视图
		if(!retrieveDataOnly) {
			//构造定制后视图
			viewCustomizeService.customizeView(view, sessionInfo.getUserId());
			//根据权限调整视图操作
			retrieveViewActions(view, request, sessionInfo);
	    	//重设视图列
	    	resetViewColumns(view, viewPackage.getViewMode(), request, sessionInfo);
	    	//设置排序列
	    	setSortColumn(viewPackage);
	    	//设置当前位置
	    	setLocation(viewPackage);
		}
    	//处理分类
    	initViewCategory(viewPackage, request, sessionInfo);
    	//获取数据
 		if(!View.VIEW_DISPLAY_MODE_CONDITION.equals(viewPackage.getViewMode())) { //不是条件输入模式
 			if(beginRow<=0) {
 				beginRow = Math.max(0, (viewPackage.getCurPage()-1) * viewPackage.getView().getPageRows());
 			}
 			retrieveViewData(viewPackage, beginRow, readRecordsOnly, countRecordsOnly, request, sessionInfo);
 			if(viewPackage.getPageCount()<=0) {
 				viewPackage.setPageCount(1);
 			}
	    	//设置记录行号
	    	viewPackage.setRowNum((viewPackage.getCurPage() - 1) * viewPackage.getView().getPageRows() + 1);
 		}
	}
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.ViewService#listChildCategories(com.yuanluesoft.jeaf.view.model.View, java.lang.String, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo, int)
	 */
	public List listChildCategories(View view, String currentCategories, HttpServletRequest request, SessionInfo sessionInfo, int limit) throws ServiceException {
		List categoryDefine = view.getCategories();
		String[] categories = currentCategories.split(",");
		int level = categories.length; //分类级别,根为1
		
		//初始化查询选项
		int size = categoryDefine.size();
		if(level>size && !view.isCategoryLoop()) {
			return null;
		}
		Category category = (Category)categoryDefine.get(Math.min(size-1, level-1));
		String hqlWhere = category.getWhere();
		if(hqlWhere!=null) {
			//替换{PARENTVALUE}
			if(level>1) {
				hqlWhere = hqlWhere.replaceAll("\\x7bPARENTVALUE\\x7d", getCategoryValue(categories, ((Category)categoryDefine.get(Math.min(size-1, level-2))).getValueType(), level-1));
			}
			//替换各级分类值{CATEGORY1VALUE},{CATEGORY2VALUE},...
			for(int i=1; i<level; i++) {
				hqlWhere = hqlWhere.replaceAll("\\x7bCATEGORY" + i + "VALUE\\x7d", getCategoryValue(categories,((Category)categoryDefine.get(Math.min(size-1,i-1))).getValueType(), i));
			}
			hqlWhere = StringUtils.fillParameters(hqlWhere, false, false, true, "utf-8", null, request, null); //替换条件中的属性值
		}
		String pojoClassName = view.getCategoryPojoClassName();
		String mappingName = pojoClassName.substring(pojoClassName.lastIndexOf(".")+1);
		//标题字段
		String titleFieldName = (category.getTitleProperty()==null ? category.getValueProperty() : category.getTitleProperty());
		titleFieldName = (titleFieldName.indexOf(".")==-1 ? mappingName + "." : "") + titleFieldName;
		//值字段
		String valueFieldName = (category.getValueProperty().indexOf(".")==-1 ? mappingName + "." : "") + category.getValueProperty();
		//添加条件,标题和值都不允许为空
		hqlWhere = (hqlWhere==null ? "" : "(" + hqlWhere + ") and ") + titleFieldName + " is not null" + (titleFieldName.equals(valueFieldName) ? "" : " and " + valueFieldName + " is not null");
		//获取数据
		String hqlSelect = titleFieldName + "," + valueFieldName;
		String hqlJoin = generateHqlJoin("", view.getCategoryPojoClassName(), null, hqlSelect, hqlWhere, hqlSelect, hqlSelect);
		List categoryData = databaseService.findRecordsByFilter(pojoClassName, hqlSelect, hqlJoin, hqlWhere, titleFieldName + "," + valueFieldName, category.getOrderBy(), view.getCategoryFilter(), null, 0, limit, sessionInfo);
		if(categoryData!=null && category.getTitleProperty()==null) {
			if(category.getTitleHql()!=null) {
				//根据hql获取标题
				for(Iterator iterator = categoryData.iterator(); iterator.hasNext();) {
					Object[] values = (Object[])iterator.next();
					String hql = category.getTitleHql().replaceAll("\\x7bCATEGORYVALUE\\x7d", values[1].toString());
					Object title = databaseService.findRecordByHql(hql);
					if(title!=null) {
						values[0] = title;
					}
				}
			}
			else {
				BusinessObject businessObject = businessDefineService.getBusinessObject(pojoClassName);
				Field field;
				if(businessObject!=null && (field=businessObject.getField(category.getValueProperty()))!=null) {
					List items = FieldUtils.listSelectItems(field, null, request);
					for(Iterator iterator = items==null ? null : categoryData.iterator(); iterator!=null && iterator.hasNext();) {
						Object[] values = (Object[])iterator.next();
						for(Iterator iteratorItem = items.iterator(); iteratorItem.hasNext();) {
							String[] itemValues = (String[])iteratorItem.next();
							if(values[1].toString().equals(itemValues[1])) {
								values[0] = itemValues[0];
							}
						}
					}
				}
			}
		}
		return categoryData;
	}
	
	/**
	 * 初始化分类
	 * @param viewPackage
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	private void initViewCategory(ViewPackage viewPackage, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		View view = viewPackage.getView();
		if(view.getCategories()!=null && (viewPackage.getCategories()==null || viewPackage.getCategories().isEmpty())) { //初始化分类
			viewPackage.setCategoryCount(view.isCategoryLoop() ? 0 : view.getCategories().size());
			String categories = view.getCategoryRoot() + "|ROOT";
			//起始分类
			if(!"ROOT".equals(view.getBeginCategory())) { //不是从根开始
				//获取第一级分类,从ROOT开始
				int size = view.isCategoryLoop() ? 0 : view.getCategories().size();
				for(int i=0; i<size || size==0; i++) {
					List list = listChildCategories(view, categories, request, sessionInfo, 1);
					if(list==null || list.size()==0) {
						break;
					}
					categories += "," + ((Object[])list.get(0))[0] + "|" + ((Object[])list.get(0))[1];
				}
			}
			viewPackage.setCategories(categories);
		}
		if(viewPackage.getCategories()!=null) { //获取分类标题
			String[] categories = viewPackage.getCategories().split("\\x2C");
			List categoryTitles = new ArrayList();
			for(int i=(categories.length==1 ? 0 : 1); i<categories.length; i++) {
				categoryTitles.add(categories[i].split("\\x7C")[0]);
			}
			viewPackage.setCategoryTitles(categoryTitles);
			viewPackage.setCategoryTitle((String)categoryTitles.get(categoryTitles.size()-1));
		}
	}
	
	/**
	 * 获取视图记录列表及记录数
	 * @param viewPackage
	 * @param beginRow
	 * @param readRecordsOnly
	 * @param countRecordsOnly
	 * @param request
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	protected void retrieveViewData(ViewPackage viewPackage, int beginRow, boolean readRecordsOnly, boolean countRecordsOnly, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		//获取数据
		if(viewPackage.getCurPage()<1) {
    		viewPackage.setCurPage(1); //当前页
    	}
		String searchConditions = viewPackage.isSearchMode() ? viewPackage.getSearchConditions() : null;
		if(viewPackage.getSearchConditionList()==null && searchConditions!=null && !searchConditions.isEmpty()) {
			try {
				viewPackage.setSearchConditionList((List)JsonUtils.generateJavaObject(searchConditions));
			}
			catch (Exception e) {
			
			}
		}
		if(!countRecordsOnly) { //不是仅获取记录数
			List records = null;
			Condition condition = null;
			List searchKeyWords = null;
			boolean isSearchByKey = viewPackage.isSearchMode() &&
									viewPackage.getSearchConditionList()!=null && 
									viewPackage.getSearchConditionList().size()==1 &&
									Condition.CONDITION_EXPRESSION_KEY.equals((condition=(Condition)viewPackage.getSearchConditionList().get(0)).getExpression()) &&
									viewPackage.getView().getQuickFilter()!=null;
			int loop = (isSearchByKey && condition.getValue1().indexOf('+')==-1 ? 5 : 1);
			String key = isSearchByKey ? condition.getValue1() : null;
			for(int i=0; i<loop && (records==null || records.isEmpty()); i++) {
				if(isSearchByKey && i>0) {
					if(i==1 && (searchKeyWords = ((WordService)Environment.getService("wordService")).parseChineseSentence(key, null)).size()==1) { //按所有的词来搜索
						break; //仅一个词
					}
					else if(i==2 && ListUtils.removeObjectsByProperty(searchKeyWords, "type", new Integer(WordService.WORD_TYPE_UNKNOWN))==null) { //剔除不确定类型的词
						continue;
					}
					else if(i==3 && ListUtils.removeObjectsByProperty(searchKeyWords, "type", new Integer(WordService.WORD_TYPE_ADJECTIVE))==null) { //剔除形容词
						continue;
					}
					else if(i==4 && ListUtils.removeObjectsByProperty(searchKeyWords, "type", new Integer(WordService.WORD_TYPE_VERB))==null) { //剔除动词
						continue;
					}
					if(searchKeyWords.isEmpty()) {
						break;
					}
					condition.setValue1(ListUtils.join(searchKeyWords, "word", "+", false)); //按所有的词来搜索
				}
				records = retrieveRecords(viewPackage.getView(), viewPackage.getCategories(), viewPackage.getSearchConditionList(), beginRow, request, sessionInfo);
			}
			//没有找到记录
			if(isSearchByKey && viewPackage.getView().getQuickFilter()!=null && (records==null || records.isEmpty())) {
				records = retrieveRecordsBySpell(key, condition, viewPackage, beginRow, request, sessionInfo); //按拼音搜索
			}
			viewPackage.setRecords(records);
		}
		if(!readRecordsOnly) { //不是仅获取记录列表
			int pageRows = viewPackage.getView().getPageRows();
			//记录数
			int recordCount = viewPackage.getRecordCount();
			if(recordCount==0) {
				recordCount = retrieveRecordCount(viewPackage.getView(), viewPackage.getCategories(), viewPackage.getSearchConditionList(), request, sessionInfo); //获取记录数
			}
			else if(pageRows>0 && !countRecordsOnly) {
				//记录数与总记录数不符,重新计算总记录数
		       	int dataSize = viewPackage.getRecords()==null ? 0 : viewPackage.getRecords().size();
		       	if(dataSize<pageRows && dataSize + (viewPackage.getCurPage() - 1) * pageRows != recordCount) {
		       		recordCount = retrieveRecordCount(viewPackage.getView(), viewPackage.getCategories(), viewPackage.getSearchConditionList(), request, sessionInfo);
		       	}
			}
		    viewPackage.setRecordCount(recordCount); //记录总数
		    //计算页数
	       	if(pageRows>0) {
	       		viewPackage.setPageCount((recordCount-1)/pageRows + 1);
		       	//计算当前页
	       		viewPackage.setCurPage(beginRow / pageRows + 1);
	       		//如果当前页码超出总页数,则重新获取记录
		    	if(viewPackage.getCurPage() > viewPackage.getPageCount() && recordCount>0) {
		    		viewPackage.setCurPage(viewPackage.getPageCount());
		    		if(!countRecordsOnly) { //不是仅获取记录数,重新获取记录
		    			viewPackage.setRecords(retrieveRecords(viewPackage.getView(), viewPackage.getCategories(), viewPackage.getSearchConditionList(), Math.max(0, viewPackage.getCurPage()-1) * pageRows, request, sessionInfo));
		    		}
		    	}
	       	}
		}
	}
	
	/**
	 * 按拼音来搜索
	 * @param key
	 * @param condition
	 * @param viewPackage
	 * @param beginRow
	 * @param request
	 * @param sessionInfo
	 * @return
	 */
	private List retrieveRecordsBySpell(String key, Condition condition, ViewPackage viewPackage, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if(key==null || key.isEmpty()) {
			return null;
		}
		//检查是否全是字母或数字
		int i=0;
		for(; i<key.length() && key.charAt(i)<128; i++);
		if(i<key.length()) {
			return null;
		}
		//检查过滤条件是否为 类名称.字段名称 like '%{KEY}%'
		String pojoClassName = viewPackage.getView().getPojoClassName();
		pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf('.') + 1);
		Pattern pattern = Pattern.compile(pojoClassName + ".([^ ]*) like '%\\{KEY\\}%'", Pattern.CASE_INSENSITIVE);
		Matcher match = pattern.matcher(viewPackage.getView().getQuickFilter());
		if(!match.find() || !viewPackage.getView().getQuickFilter().equals(match.group(0))) {
			return null;
		}
		String fieldName = match.group(1);
		//检查是否有以Spell结尾的字段
		if(FieldUtils.getRecordField(viewPackage.getView().getPojoClassName(), fieldName + "Spell", request)==null) {
			return null;
		}
		//重设条件,并获取记录
		condition.setValue1(key.toLowerCase().trim());
		viewPackage.getView().setQuickFilter(pojoClassName + "." + fieldName + "Spell like '{KEY}%'");
		return retrieveRecords(viewPackage.getView(), viewPackage.getCategories(), viewPackage.getSearchConditionList(), beginRow, request, sessionInfo);
	}

	/**
	 * 获取记录
	 * @param view
	 * @param currentCategories
	 * @param searchConditionList
	 * @param beginRow
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	protected List retrieveRecords(View view, String currentCategories, List searchConditionList, int beginRow, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		String hqlWhere = generateHqlWhere(view, currentCategories, searchConditionList, request, sessionInfo);
		String hqlJoin = generateHqlJoin(view, view.getJoin(), null, hqlWhere, view.getOrderBy(), view.getGroupBy());
		//设置需要延迟加载的对象
		List lazyLoadProperties = listLazyLoadProperties(view);
		//获取记录
		return databaseService.findRecordsByFilter(view.getPojoClassName(), null, hqlJoin, hqlWhere, view.getGroupBy(), view.getOrderBy(), view.getFilter(), lazyLoadProperties, beginRow, view.getPageRows(), sessionInfo);
	}
	
	/**
	 * 获取记录数
	 * @param view
	 * @param currentCategories
	 * @param searchConditionList
	 * @param request
	 * @param sessionInfo
	 * @return
	 * @throws ServiceException
	 */
	protected int retrieveRecordCount(View view, String currentCategories, List searchConditionList, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		String hqlWhere = generateHqlWhere(view, currentCategories, searchConditionList, request, sessionInfo);
		String hqlJoin = generateHqlJoin(view, view.getJoin(), null, hqlWhere, null, view.getGroupBy());
		return databaseService.countRecordsByFilter(view.getPojoClassName(), hqlJoin, hqlWhere, view.getGroupBy(), view.getFilter(), sessionInfo);
	}
	
	/**
	 * 获取需要延迟加载的对象
	 * @param view
	 * @param viewPackage
	 * @return
	 * @throws ServiceException
	 */
	protected List listLazyLoadProperties(View view) throws ServiceException {
		if(view.getColumns()==null) { //没有列
			return null;
		}
		List lazyLoadProperties = new ArrayList();
		BusinessObject businessObject = getBusinessObject(view);
		for(Iterator iterator = view.getColumns().iterator(); iterator.hasNext();) {
			Column column = (Column)iterator.next();
			String columnNames;
			if(Column.COLUMN_TYPE_FORMULA.equals(column.getType())) { //公式
				columnNames = column.getFormulaFields();
			}
			else {
				columnNames = column.getName();
			}
			if(columnNames==null || columnNames.isEmpty()) {
	    		continue;
	    	}
			String[] names = columnNames.split(",");
			for(int i=0; i<names.length; i++) {
				names[i] = names[i].trim();
				int index = names[i].indexOf('.');
				String referenceFields = null;
		    	if(index==-1) {
		    		Field field = (Field)ListUtils.findObjectByProperty(businessObject.getFields(), "name",  names[i]);
		    		if(field!=null) {
		    			referenceFields = (String)field.getParameter("referenceFields"); //获取引用的字段
		    		}
				}
		    	else {
			    	//检查是否组成部分集合
		    		String property = names[i].substring(0, index);
		    		Field field = (Field)ListUtils.findObjectByProperty(businessObject.getFields(), "name", property);
		    		if(field!=null && "components".equals(field.getType()) && field.isPersistence() && lazyLoadProperties.indexOf(property)==-1) {
			   			lazyLoadProperties.add(property);
					}
		    		if(field!=null) {
		    			referenceFields = (String)field.getParameter("referenceFields"); //获取引用的字段
		    		}
		    	}
		    	if(referenceFields==null) {
		    		continue;
		    	}
				String[] referenceFieldNames = referenceFields.split(",");
				for(int j=0; j<referenceFieldNames.length; j++) {
					Field field = (Field)ListUtils.findObjectByProperty(businessObject.getFields(), "name", referenceFieldNames[j]); //获取字段
			    	if("components".equals(field.getType()) && field.isPersistence() && lazyLoadProperties.indexOf(referenceFieldNames[j])==-1) {
			    		lazyLoadProperties.add(referenceFieldNames[j]);
					}
				}
			}
		}
		return (lazyLoadProperties.isEmpty() ? null : lazyLoadProperties);
	}
	
	/**
	 * 获取业务对象
	 * @param view
	 * @return
	 */
	private BusinessObject getBusinessObject(View view) {
		BusinessObject businessObject = (BusinessObject)view.getExtendParameter("businessObject");
		if(businessObject==null) {
			try {
				businessObject = businessDefineService.getBusinessObject(view.getPojoClassName());
			} 
			catch (ServiceException e) {
				Logger.exception(e);
				return null;
			}
			view.setExtendParameter("businessObject", businessObject);
		}
		return businessObject;
	}
	
	/**
	 * 生成连接HQL
	 * @param view
	 * @param hqlJoin
	 * @param hqlSelect
	 * @param hqlWhere
	 * @param hqlOrderBy
	 * @param hqlGroupBy
	 * @return
	 */
	protected String generateHqlJoin(View view, String hqlJoin, String hqlSelect, String hqlWhere, String hqlOrderBy, String hqlGroupBy) {
		return generateHqlJoin("", view.getPojoClassName(), hqlJoin, hqlSelect, hqlWhere, hqlOrderBy, hqlGroupBy); 
	}
	
	/**
	 * 递归函数:生成连接HQL
	 * @param propertyName
	 * @param pojoClassName
	 * @param hqlJoin
	 * @param hqlSelect
	 * @param hqlWhere
	 * @param hqlOrderBy
	 * @param hqlGroupBy
	 * @return
	 */
	private String generateHqlJoin(String propertyName, String pojoClassName, String hqlJoin, String hqlSelect, String hqlWhere, String hqlOrderBy, String hqlGroupBy) {
		if(hqlSelect==null && hqlWhere==null && hqlOrderBy==null) {
			return hqlJoin;
		}
		List fields = FieldUtils.listRecordFields(pojoClassName, "components,component", null, null, null, true, false, false, true, 1);
		pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf(".") + 1);
		for(Iterator iterator = fields==null ? null : fields.iterator(); iterator!=null && iterator.hasNext();) {
			Field field = (Field)iterator.next();
			String fieldName = (propertyName.isEmpty() ? "" : propertyName + "_") + field.getName();
			if((hqlSelect!=null && (hqlSelect.indexOf(fieldName + ".")!=-1 || hqlSelect.indexOf(fieldName + "_")!=-1)) ||
			   (hqlWhere!=null && (hqlWhere.indexOf(fieldName + ".")!=-1 || hqlWhere.indexOf(fieldName + "_")!=-1)) ||
			   (hqlOrderBy!=null && (hqlOrderBy.indexOf(fieldName + ".")!=-1 || hqlOrderBy.indexOf(fieldName + "_")!=-1)) ||
			   (hqlGroupBy!=null && (hqlGroupBy.indexOf(fieldName + ".")!=-1 || hqlGroupBy.indexOf(fieldName + "_")!=-1))) {
				String join = "left join " + (propertyName.isEmpty() ? pojoClassName : propertyName) + "." + field.getName() + " " + fieldName;
				if(hqlJoin==null || hqlJoin.indexOf(join)==-1) {
					//递归调用
					String subJoin = generateHqlJoin(fieldName, "" + field.getParameter("class"), null, hqlSelect, hqlWhere, hqlOrderBy, hqlGroupBy);
					join += subJoin==null ? "" : " " + subJoin;
					hqlJoin = join + (hqlJoin==null ? "" : " " + hqlJoin);
				}
			}
		}
		return hqlJoin; 
	}
	
	/**
	 * 组合hqlWhewe
	 * @param view
	 * @param currentCategories
	 * @param searchConditionList
	 * @param request
	 * @param sessionInfo
	 * @return
	 */
	protected String generateHqlWhere(View view, String currentCategories, List searchConditionList, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		String hqlCategory = generateHqlCategory(view.getCategories(), currentCategories);
	    String hqlWhere = StringUtils.fillParameters(view.getWhere(), false, false, true, "utf-8", null, request, null);
	    if(hqlWhere==null) {
			hqlWhere = hqlCategory;
		}
		else if(hqlCategory!=null) {
			hqlWhere = "(" + hqlWhere + ") and (" + hqlCategory + ")";
		}
		String hqlSearch = generateHqlSearch(view.getPojoClassName(), view.getQuickFilter(), searchConditionList);
		if(hqlSearch!=null) {
			hqlWhere = hqlWhere==null ? hqlSearch : "(" + hqlWhere + ") and (" + hqlSearch + ")";
		}
		if(hqlWhere==null) {
			return null;
		}
		if(!databaseService.getDbServerName().startsWith("oracle")) { // || view.getName().startsWith("admin")) { //不是oracle数据库,或者后台管理视图
			return hqlWhere;
		}
		//数据库是oracle且不是后台管理视图,检查条件只是否包含了CLOB字段,如果有只搜索字段长度小于2000的,提高查询效率
		String pojoName = view.getPojoClassName().substring(view.getPojoClassName().lastIndexOf(".") + 1);
		for(Iterator iterator = listConditionFields(view).iterator(); iterator.hasNext();) {
			Field field = (Field)iterator.next();
			if(field.getLength()!=null || (!"string".equals(field.getType()) && !"html".equals(field.getType()))) { //没有指定字段长度,或者类型不是文本和HTML
				continue;
			}
			//检查字段是否出现在where中
			String fieldName = field.getName().indexOf('.')==-1 ? pojoName + "." + field.getName() : field.getName();
			if(hqlWhere.indexOf(fieldName)==-1) {
				continue;
			}
			String hqlLengthCheck = "length(" + fieldName + ")<=2000";
			if(hqlWhere.indexOf(hqlLengthCheck)==-1) {
				hqlWhere = hqlLengthCheck + " and (" + hqlWhere + ")";
			}
		}
		return hqlWhere;
	}
	
	/**
	 * 生成搜索条件hql
	 * @param pojoClassName
	 * @param quickFilter
	 * @param searchConditionList
	 * @return
	 */
	private String generateHqlSearch(String pojoClassName, String quickFilter, List searchConditionList) {
		if(searchConditionList==null) {
			return null;
		}
		if(searchConditionList.size()==1 && Condition.CONDITION_EXPRESSION_KEY.equals(((Condition)searchConditionList.get(0)).getExpression())) { //快速过滤
			//替换{KEY}
			if(quickFilter==null) {
				return null;
			}
			String hqlSearch = null;
			String[] keys = JdbcUtils.resetQuot(((Condition)searchConditionList.get(0)).getValue1()).split(" "); //空格,用或的关系查询
			for(int i=0; i<keys.length; i++) {
				if(!keys[i].isEmpty()) {
					String subSearch = null;
					String[] andKeys = keys[i].split("\\+"); //加号,用与的关系查询
					for(int j=0; j<andKeys.length; j++) {
						if(!andKeys[j].isEmpty()) {
							subSearch = (subSearch==null ? "" : subSearch + " and ") + "(" + quickFilter.replaceAll("\\x7bKEY\\x7d", andKeys[j]) + ")";
						}
					}
					if(subSearch!=null) {
						hqlSearch = (hqlSearch==null ? "" : hqlSearch + " or ") + "(" + subSearch + ")";
					}
				}
			}
			return hqlSearch;
		}
		String hqlSearch = "";
		pojoClassName = pojoClassName.substring(pojoClassName.lastIndexOf(".") + 1);
		for(Iterator iterator = searchConditionList.iterator(); iterator.hasNext();) {
			Condition condition = (Condition)iterator.next();
			try {
				condition = (Condition)condition.clone();
			} 
			catch (CloneNotSupportedException e) {
				
			}
			if(Condition.CONDITION_EXPRESSION_HQL.equals(condition.getExpression())) { //自定义HQL
				hqlSearch += (hqlSearch.equals("") ? "" : " " + condition.getLinkMode()) + " (" + condition.getValue1() + ")";
				continue;
			}
			if("date".equals(condition.getFieldType())) { //日期
				try {
					condition.setValue1("DATE(" + DateTimeUtils.formatDate(DateTimeUtils.parseDate(condition.getValue1(), null), null) + ")");
				}
				catch(Exception e) {
					condition.setValue1(null);
				}
				try {
					condition.setValue2("DATE(" + DateTimeUtils.formatDate(DateTimeUtils.parseDate(condition.getValue2(), null), null) + ")");
				}
				catch(Exception e) {
					condition.setValue2(null);
				}
			}
			else if("timestamp".equals(condition.getFieldType())) { //时间
				try {
					condition.setValue1("TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.parseTimestamp(condition.getValue1(), null), null) + ")");
				}
				catch(Exception e) {
					try {
						condition.setValue1("TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.parseTimestamp(condition.getValue1(), "yyyy-MM-dd"), null) + ")");
					}
					catch(Exception ex) {
						condition.setValue1(null);
					}
				}
				try {
					condition.setValue2("TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.parseTimestamp(condition.getValue2(), null), null) + ")");
				}
				catch(Exception e) {
					try {
						condition.setValue2("TIMESTAMP(" + DateTimeUtils.formatTimestamp(DateTimeUtils.parseTimestamp(condition.getValue2(), "yyyy-MM-dd"), null) + ")");
					}
					catch(Exception ex) {
						condition.setValue2(null);
					}
				}
			}
			else if("number".equals(condition.getFieldType()) && //数字
					!Condition.CONDITION_EXPRESSION_MEMBER.equals(condition.getExpression()) &&
					!Condition.CONDITION_EXPRESSION_NOT_MEMBER.equals(condition.getExpression())) {
				try {
					Double.parseDouble(condition.getValue1());
				}
				catch(Exception e) {
					condition.setValue1("0");
				}
				if(condition.getValue2()!=null && !condition.getValue2().isEmpty()) {
					try {
						Double.parseDouble(condition.getValue2());
					}
					catch(Exception e) {
						condition.setValue2("0");
					}
				}
			}
			else { //其他
				condition.setValue1(JdbcUtils.resetQuot(condition.getValue1()));
				condition.setValue2(JdbcUtils.resetQuot(condition.getValue2()));
			}
			if(condition.getValue1()==null) {
				continue;
			}
			hqlSearch += hqlSearch.equals("") ? "" : (" " + condition.getLinkMode());
			String fieldName = condition.getFieldName();
			int index = fieldName.lastIndexOf(".");
			if(index==-1) {
				fieldName = pojoClassName + "." + fieldName;
			}
			else {
				fieldName = fieldName.substring(0, index).replaceAll("\\.", "_") + fieldName.substring(index);
			}
			String sep = "";
			if(!"number".equals(condition.getFieldType()) && //不是数字
			   !"date".equals(condition.getFieldType()) && //不是日期
			   !"timestamp".equals(condition.getFieldType())) {  //不是时间
				sep = "'";
			}
			String expression = condition.getExpression();
			if(Condition.CONDITION_EXPRESSION_EQUAL.equals(expression)) { //等于
				hqlSearch += " " + fieldName + "=" + sep + condition.getValue1() + sep;
			}
			else if(Condition.CONDITION_EXPRESSION_NOT_EQUAL.equals(expression)) { //不等于
				hqlSearch += " not " + fieldName + "=" + sep + condition.getValue1() + sep;
			}
			else if(Condition.CONDITION_EXPRESSION_LESS_THAN.equals(expression)) { //小于
				hqlSearch += " " + fieldName + "<" + sep + condition.getValue1() + sep;
			}
			else if(Condition.CONDITION_EXPRESSION_LESS_THAN_OR_EQUAL.equals(expression)) { //小等于
				hqlSearch += " " + fieldName + "<=" + sep + condition.getValue1() + sep;
			}
			else if(Condition.CONDITION_EXPRESSION_GREATER_THAN.equals(expression)) { //大于
				hqlSearch += " " + fieldName + ">" + sep + condition.getValue1() + sep;
			}
			else if(Condition.CONDITION_EXPRESSION_GREATER_THAN_OR_EQUAL.equals(expression)) { //大等于
				hqlSearch += " " + fieldName + ">=" + sep + condition.getValue1() + sep;
			}
			else if(Condition.CONDITION_EXPRESSION_BETWEEN.equals(expression)) { //介于
				hqlSearch += " " + fieldName + " between " + sep + condition.getValue1() + sep + " and " + sep + condition.getValue2() + sep;
			}
			else if(Condition.CONDITION_EXPRESSION_NOT_BETWEEN.equals(expression)) { //不介于
				hqlSearch += " not " + fieldName + " between " + sep + condition.getValue1() + sep + " and " + sep + condition.getValue2() + sep;
			}
			else if(Condition.CONDITION_EXPRESSION_CONTAIN.equals(expression) || Condition.CONDITION_EXPRESSION_NOT_CONTAIN.equals(expression)) { //包含,不包含
				if(Condition.CONDITION_EXPRESSION_NOT_CONTAIN.equals(expression)) {
					hqlSearch += " not ";
				}
				if(databaseService.getDbServerName().startsWith("oracle")) { //oracle数据库
					hqlSearch += " instr(" + fieldName + ", " + sep + condition.getValue1() + sep + ")>0";
				}
				else {
					hqlSearch += " " + fieldName + " like " + sep + "%" + condition.getValue1() + "%" + sep;
				}
			}
			else if(Condition.CONDITION_EXPRESSION_MEMBER.equals(expression)) { //属于
				String values = sep + condition.getValue1().replaceAll(",", sep + "," + sep) + sep;
				if("number".equals(condition.getFieldType())) { //数字
					values = JdbcUtils.validateInClauseNumbers(values);
				}
				hqlSearch += " " + fieldName + " in (" + values + ")";
			}
			else if(Condition.CONDITION_EXPRESSION_NOT_MEMBER.equals(expression)) { //不属于
				String values = sep + condition.getValue1().replaceAll(",", sep + "," + sep) + sep;
				if("number".equals(condition.getFieldType())) { //数字
					values = JdbcUtils.validateInClauseNumbers(values);
				}
				hqlSearch += " not " + fieldName + " in (" + values + ")";
			}
		}
		return hqlSearch.equals("") ? null : hqlSearch;
	}
	
	/**
	 * 根据分类定义和当前分类生成分类hql
	 * @param categooryDefine
	 * @param currentCategories 格式:分类1标题|分类1名称,...,分类n标题|分类n名称
	 * @return
	 * @throws ServiceException
	 */
	private String generateHqlCategory(List categoryDefine, String currentCategories) {
		if(currentCategories==null || currentCategories.equals("")) {
			return null; //无分类
		}
		String[] categories = currentCategories.split(",");
		int level = categories.length-1;
		if(level==0) {
			return null; //根分类
		}
		int size = categoryDefine.size();
		String hql = ((Category)categoryDefine.get(Math.min(size-1,level-1))).getViewDataFilter();
		if(hql==null || hql.equals("")) {
			return null; //不筛选
		}
		//替换{PARENTVALUE}
		if(level>1) {
			hql=hql.replaceAll("\\x7bPARENTVALUE\\x7d", getCategoryValue(categories, ((Category)categoryDefine.get(Math.min(size-1,level-2))).getValueType(), level-1));
		}
		//替换{CATEGORYVALUE}
		hql = hql.replaceAll("\\x7bCATEGORYVALUE\\x7d", getCategoryValue(categories, ((Category)categoryDefine.get(Math.min(size-1,level-1))).getValueType(), level));
		//替换各级分类值{CATEGORY1VALUE},{CATEGORY2VALUE},...
		for(int i=1;i<=level;i++) {
			hql=hql.replaceAll("\\x7bCATEGORY" + i + "VALUE\\x7d", getCategoryValue(categories, ((Category)categoryDefine.get(Math.min(size-1,i-1))).getValueType(), i));
		}
		return hql;
	}
	
	/**
	 * 获取指定分类的值
	 * @param categories 格式:分类标题|分类1名称
	 * @param level
	 * @return
	 */
	private String getCategoryValue(String[] categories, String valueType, int level) {
		String[] values = categories[level].split("\\x7C");
		String value = (values.length==1 ? values[0]:values[1]);
		return valueType==null || valueType.equalsIgnoreCase("STRING") ? "'" + value + "'":value;
	}
	
	/**
	 * 设置排序列
	 * @param viewPackage
	 */
	private void setSortColumn(ViewPackage viewPackage) {
    	//排序选项
		String orderBy = viewPackage.getView().getOrderBy();
    	if(orderBy==null) {
    		viewPackage.setSortColumn(" ");
    		return;
    	}
		int index = orderBy.indexOf(",");
		if(index!=-1) {
			orderBy = orderBy.substring(0,index);
		}
		index = orderBy.indexOf(" ");
		if(index!=-1) {
			viewPackage.setDescendingSort(orderBy.substring(index+1).equals("DESC"));
			orderBy = orderBy.substring(0,index);
		}
		else {
			viewPackage.setDescendingSort(false);
		}
		index = orderBy.indexOf('.');
		String pojoClasssName = viewPackage.getView().getPojoClassName();
		viewPackage.setSortColumn(orderBy.substring(0, index).equals(pojoClasssName.substring(pojoClasssName.lastIndexOf('.') + 1)) ? orderBy.substring(index + 1) : orderBy);
    }
	
	/**
	 * 设置当前位置
	 * @param viewPackage
	 */
	private void setLocation(ViewPackage viewPackage) {
		List location = new ArrayList();
    	String title = null;
		try {
			title = eaiClient.getApplicationTitle(viewPackage.getView().getApplicationName());
		}
		catch (Exception e) {
			
		}
    	if(title!=null) {
    		location.add(title); //应用标题
    	}
    	location.add(viewPackage.getView().getTitle()); //视图标题
    	viewPackage.setLocation(location);
	}
    
    /* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.ViewService#retrieveViewActions(com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public void retrieveViewActions(View view, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		if(view.getActions()==null || view.getActions().isEmpty()) {
			return;
		}
		for(int i=view.getActions().size()-1; i>=0; i--) {
    	    Object action = view.getActions().get(i);
	        if(action instanceof ViewAction) {
	        	ViewAction viewAction = (ViewAction)action;
	        	retrieveViewAction(viewAction, request, sessionInfo);
	        	if(viewAction.getExecute()==null) {
	        		view.getActions().remove(i);
	        	}
	        }
	        else if(action instanceof ViewActionGroup) {
	        	ViewActionGroup actionGroup = (ViewActionGroup)action;
	    		String menuItems = null; //菜单项
	    	    String script = null;; //脚本
	    	    int menuIndex = 0;
	    		for(Iterator iterator=actionGroup.getActions().iterator(); iterator.hasNext();) {
	        		ViewAction viewAction = (ViewAction)iterator.next();
	        		retrieveViewAction(viewAction, request, sessionInfo);
	        		if(viewAction.getExecute()==null) {
	        			iterator.remove();
		        	}
	        		else {
	        			menuItems = (menuItems==null ? "" : menuItems + "\\0") + (viewAction.getGroupTitle()==null ? viewAction.getTitle() : viewAction.getGroupTitle()) + "|" + menuIndex;
	        			script = (script==null ? "window.menuFunctions=new Array();" : script) + "menuFunctions[" + menuIndex + "]=function(){" + viewAction.getExecute() + "};";
	        			menuIndex++;
	        		}
	        	}
	        	if(actionGroup.getActions().isEmpty()) { //没有操作
    	    		view.getActions().remove(i);
    	    	}
    	    	else if(actionGroup.getActions().size()==1) { //只剩一个操作
    	    		view.getActions().set(i, actionGroup.getActions().get(0));
    	    	}
    	    	else { //多个操作,设置分组脚本
    	    		actionGroup.setExecute(script + "PopupMenu.popupMenu('" + menuItems + "', function(menuItemId, menuItemTitle) {window.menuFunctions[menuItemId].call();}, this)");
    	    	}
	        }
    	}
    }
	
	/* (non-Javadoc)
	 * @see com.yuanluesoft.jeaf.view.service.ViewService#checkLoadPrivilege(com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest, com.yuanluesoft.jeaf.sessionmanage.model.SessionInfo)
	 */
	public boolean checkLoadPrivilege(View view, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
		return true;
	}

	/**
	 * 重置单个操作
	 * @param viewAction
	 * @param request
	 * @param sessionInfo
	 * @throws ServiceException
	 */
	private void retrieveViewAction(ViewAction viewAction, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
    	ActionConfig actionConfig = (ActionConfig)request.getAttribute("org.apache.struts.action.mapping.instance");
		//设置命令中的参数值
    	viewAction.setExecute(StringUtils.fillParameters(viewAction.getExecute(), false, true, false, "utf-8", (actionConfig!=null && actionConfig.getName()!=null ? request.getAttribute(actionConfig.getName()) : null), request, null));
		if(viewAction instanceof NewWorkflowInstanceAction) { //新建流程
            NewWorkflowInstanceAction newWorkflowInstanceAction = (NewWorkflowInstanceAction)viewAction;
            List entries = listWorkflowEntries(newWorkflowInstanceAction.getApplicationName(), sessionInfo);
            if(entries==null) {
            	return;
            }
            if(newWorkflowInstanceAction.getExecute()==null || newWorkflowInstanceAction.getExecute().isEmpty()) {
            	newWorkflowInstanceAction.setExecute(Environment.getContextPath() + "/" + newWorkflowInstanceAction.getApplicationName() + "/" + newWorkflowInstanceAction.getForm() + ".shtml?act=create");
            }
            newWorkflowInstanceAction.setExecute("PageUtils.createWorkflowInstnace(" + JsonUtils.generateJSONObject(entries) + ", \"" + viewAction.getExecute() + "\"" + (newWorkflowInstanceAction.getOpenFeatures()==null ? "" : ", \"" + newWorkflowInstanceAction.getOpenFeatures() + "\"") + ")");
        }
	}
    
	/**
	 * 以属性列表方式返回流程入口列表
	 * @param moduleName
	 * @return
	 * @throws WorkflowException
	 */
	protected List listWorkflowEntries(String applicationPath, SessionInfo sessionInfo) throws ServiceException {
		try {
			return workflowExploitService.listWorkflowEntries(applicationPath, null, sessionInfo);
		}
		catch(Exception e) {
			Logger.exception(e);
			throw new ServiceException();
		}
	}
	
    /*
     * (non-Javadoc)
     * @see com.yuanluesoft.jeaf.view.service.ViewService#resetViewColumns(com.yuanluesoft.jeaf.view.model.ViewPackage, com.yuanluesoft.jeaf.view.model.View, javax.servlet.http.HttpServletRequest)
     */
    public void resetViewColumns(View view, String viewMode, HttpServletRequest request, SessionInfo sessionInfo) throws ServiceException {
    	if(View.VIEW_DISPLAY_MODE_CONDITION.equals(viewMode)) { //条件输入模式
    		view.setColumns(listConditionFields(view));
    	    return;
    	}
    	//删除HTML、附件、视频、图片列的链接
    	for(Iterator iterator = view.getColumns().iterator(); iterator.hasNext();) {
    		Column column = (Column)iterator.next();
    		column.setTitle(StringUtils.fillParameters(column.getTitle(), false, false, false, "utf-8", null, request, null));
    		if(column.getName()==null || column.getMaxCharCount()>0) {
    			continue;
    		}
    		Field field = FieldUtils.getRecordField(view.getPojoClassName(), column.getName(), request);
    		if(field!=null && ("html".equals(field.getType()) || "attachment".equals(field.getType()) || "video".equals(field.getType()) || "image".equals(field.getType()))) {
    			column.setLink(null);
    		}
    	}
    	//设置显示列
    	if(View.VIEW_DISPLAY_MODE_NORMAL.equals(viewMode) || View.VIEW_DISPLAY_MODE_CUSTOMIZE.equals(viewMode)) { //普通视图模式、或者视图定制模式
			for(Iterator iterator = view.getColumns().iterator(); iterator.hasNext();) {
    			Column column = (Column)iterator.next();
    			if(!isNormalColumn(column)) {
    				iterator.remove();
    			}
    		}
    	}
    	else if(View.VIEW_DISPLAY_MODE_PRINT.equals(viewMode)) { //打印模式
    		for(Iterator iterator = view.getColumns().iterator(); iterator.hasNext();) {
	    		Column column = (Column)iterator.next();
	    		if(Column.COLUMN_TYPE_SELECT.equals(column.getType()) || !isPrintColumn(column)) {
	    			iterator.remove();
	    			System.out.println("************remove:" + column.getType());
	    		}
	    	}
    	}
    	else if(View.VIEW_DISPLAY_MODE_SELECT.equals(viewMode)) { //选择对话框模式
	    	for(Iterator iterator = view.getColumns().iterator(); iterator.hasNext();) {
	    		Column column = (Column)iterator.next();
	    		column.setLink(null); //清理链接
	    		if(Column.COLUMN_TYPE_SELECT.equals(column.getType()) || !isSelectColumn(column)) {
	    			iterator.remove();
	    		}
	    	}
    	}
    	else if(View.VIEW_DISPLAY_MODE_MULTI_SELECT.equals(viewMode)) { //多选对话框模式
    		boolean containsSelectColumn = false;
	    	for(Iterator iterator = view.getColumns().iterator(); iterator.hasNext();) {
	    		Column column = (Column)iterator.next();
	    		column.setLink(null); //清理链接
	    		if(!Column.COLUMN_TYPE_SELECT.equals(column.getType())) {
	    			containsSelectColumn = true;
	    		}
	    		if(!isSelectColumn(column)) {
	    			iterator.remove();
	    		}
	    	}
	    	if(!containsSelectColumn) { //自动增加选择列
	    		Column column = new Column();
	    		column.setTitle("选择");
	    		column.setType(Column.COLUMN_TYPE_SELECT);
	    		column.setAlign("center");
	    		column.setHideTitle(true);
	    		column.setWidth("32");
	    		view.getColumns().add(0, column);
	    	}
    	}
    }
    
    /**
     * 获取条件输入字段列表
     * @param view
     * @return
     * @throws ServiceException
     */
    private List listConditionFields(View view) throws ServiceException {
    	List fields = FieldUtils.listRecordFields(view.getPojoClassName(), null, "attachment,image,video,imageName,videoName,attachmentName,opinion", null, "none,password", false, true, true, true, 2);
    	FieldUtils.sortFieldsByInputMode(fields, "hidden");
    	return fields;
	}
    
    /**
	 * 是否条件列
	 * @return
	 */
    protected boolean isConditionColumn(Column column) {
		if(column.getDisplay()!=null) {
		    return column.getDisplay().indexOf(View.VIEW_DISPLAY_MODE_CONDITION)!=-1;
		}
		else if(column.getDisplayExcept()!=null) {
		    return column.getDisplayExcept().indexOf(View.VIEW_DISPLAY_MODE_CONDITION)==-1;
		}
		return true;
	}
    
	/**
	 * 是否视图列
	 * @return
	 */
    protected boolean isNormalColumn(Column column) {
		if(column.getDisplay()!=null) {
		    return column.getDisplay().indexOf(View.VIEW_DISPLAY_MODE_NORMAL)!=-1;
		}
		else if(column.getDisplayExcept()!=null) {
		    return column.getDisplayExcept().indexOf(View.VIEW_DISPLAY_MODE_NORMAL)==-1;
		}
		return true;
	}
	
	/**
	 * 是否选择对话框列
	 * @return
	 */
    protected boolean isSelectColumn(Column column) {
		if(column.getDisplay()!=null) {
		    return column.getDisplay().indexOf(View.VIEW_DISPLAY_MODE_SELECT)!=-1;
		}
		else if(column.getDisplayExcept()!=null) {
		    return column.getDisplayExcept().indexOf(View.VIEW_DISPLAY_MODE_SELECT)==-1;
		}
		return true;
	}
	
	/**
	 * 是否选择打印列
	 * @return
	 */
    protected boolean isPrintColumn(Column column) {
		if(column.getDisplay()!=null) {
		    return column.getDisplay().indexOf(View.VIEW_DISPLAY_MODE_PRINT)!=-1;
		}
		else if(column.getDisplayExcept()!=null) {
		    return column.getDisplayExcept().indexOf(View.VIEW_DISPLAY_MODE_PRINT)==-1;
		}
		return true;
	}
	/**
	 * @return the viewCustomizeService
	 */
	public ViewCustomizeService getViewCustomizeService() {
		return viewCustomizeService;
	}
	/**
	 * @param viewCustomizeService the viewCustomizeService to set
	 */
	public void setViewCustomizeService(ViewCustomizeService viewCustomizeService) {
		this.viewCustomizeService = viewCustomizeService;
	}
	/**
	 * @return the workflowExploitService
	 */
	public WorkflowExploitService getWorkflowExploitService() {
		return workflowExploitService;
	}
	/**
	 * @param workflowExploitService the workflowExploitService to set
	 */
	public void setWorkflowExploitService(
			WorkflowExploitService workflowExploitService) {
		this.workflowExploitService = workflowExploitService;
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
	 * @return the eaiClient
	 */
	public EAIClient getEaiClient() {
		return eaiClient;
	}

	/**
	 * @param eaiClient the eaiClient to set
	 */
	public void setEaiClient(EAIClient eaiClient) {
		this.eaiClient = eaiClient;
	}
}