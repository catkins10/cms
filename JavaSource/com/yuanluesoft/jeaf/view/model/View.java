/*
 * Created on 2004-12-22
 *
 */
package com.yuanluesoft.jeaf.view.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 *
 * @author LinChuan
 *
 */
public class View extends CloneableObject {
	public static final String VIEW_DISPLAY_MODE_NORMAL = "normal";
	public static final String VIEW_DISPLAY_MODE_PRINT = "print";
	public static final String VIEW_DISPLAY_MODE_CONDITION = "condition";
	public static final String VIEW_DISPLAY_MODE_SELECT = "select";
	public static final String VIEW_DISPLAY_MODE_MULTI_SELECT = "multi-select";
	public static final String VIEW_DISPLAY_MODE_CUSTOMIZE = "customize";
	
	public static final String VIEW_FILTER_MODE_READONLY = "READONLY";
	public static final String VIEW_FILTER_MODE_EDITABLE = "EDITABLE";
	public static final String VIEW_FILTER_MODE_TODO = "TODO";
	public static final String VIEW_FILTER_MODE_INPROCESS = "INPROCSS";
    
	private String name; //名称
	private String applicationName; //系统名称
	private String title; //标题
	private String form; //表单
	private String openFeatures; //新窗口打开方式,未设定则继承上一级的打开方式
	private String hideCondition; //隐藏条件
	private int pageRows; //每页显示行数
	private String width; //表格宽度
	private String url; //打开视图的URL
	
	//数据相关属性
	private String pojoClassName; //orm映射类名称
	private String where; //hql where 子句
	private String groupBy; //hql group by 子句
	private String orderBy; //hql order by 子句
	private String filter; //数据过滤选项, READABLE(允许读取)/EDITABLE(允许编辑)/其他
	private String quickFilter; //对话框数据过滤选项
	private String newestCheckBy; //检查是否新记录时使用的属性
	private String join; //JOIN子句扩展,默认
	
	//分类相关属性
	private String categoryRoot; //根分类标题
	private String beginCategory; //初始分类
	private String categoryPojoClassName; //分类的orm映射类名称
	private String categoryFilter; //分类数据过滤选项
	private boolean categoryLoop; //循环查找下一级分类
	private List categories; //分类定义列表

	private String viewServiceName; //视图服务名称,默认为viewService

	private List scripts; //需要引用的脚本文件
	private List actions; //视图操作列表
	private List columns; //视图列列表
	private List links; //链接列表
	
	private Map parameters; //参数列表,为字段类型、输入方式配置所需要的参数
	
	/**
	 * 获取参数值
	 * @param parameterName
	 * @return
	 */
	public Object getExtendParameter(String parameterName) {
		if(parameters==null) {
			return null;
		}
		return parameters.get(parameterName); 
	}
	
	/**
	 * 设置参数值
	 * @param parameterName
	 * @param parameterValue
	 */
	public void setExtendParameter(String parameterName, Object parameterValue) {
		if(parameters==null) {
			parameters = new HashMap();
		}
		if(parameterValue==null) {
			parameters.remove(parameterName);
		}
		else {
			parameters.put(parameterName, parameterValue);
		}
	}

	/**
	 * 是否待办视图
	 * @return
	 */
	public boolean isTodoView() {
		return "TODO".equals(filter);
	}

	/**
	 * 追加条件
	 * @param hqlWhere
	 */
	public void addWhere(String where) {
		if(where==null) {
			return;
		}
		if(this.where==null) {
			this.where = where;
		}
		else {
			this.where = "(" + this.where + ") and (" +  where + ")";
		}
	}
	
	/**
	 * 追加条件
	 * @param hqlWhere
	 */
	public void addJoin(String join) {
		if(join==null) {
			return;
		}
		if(this.join==null) {
			this.join = join;
		}
		else {
			this.join = this.join +  join;
		}
	}
	/**
	 * @return Returns the actions.
	 */
	public List getActions() {
		return actions;
	}
	/**
	 * @param actions The actions to set.
	 */
	public void setActions(List actions) {
		this.actions = actions;
	}
	/**
	 * @return Returns the beginCategory.
	 */
	public String getBeginCategory() {
		return beginCategory;
	}
	/**
	 * @param beginCategory The beginCategory to set.
	 */
	public void setBeginCategory(String beginCategory) {
		this.beginCategory = beginCategory;
	}
	/**
	 * @return Returns the categories.
	 */
	public List getCategories() {
		return categories;
	}
	/**
	 * @param categories The categories to set.
	 */
	public void setCategories(List categories) {
		this.categories = categories;
	}
	/**
	 * @return Returns the categoryFilter.
	 */
	public String getCategoryFilter() {
		return categoryFilter;
	}
	/**
	 * @param categoryFilter The categoryFilter to set.
	 */
	public void setCategoryFilter(String categoryFilter) {
		this.categoryFilter = categoryFilter;
	}
	/**
	 * @return Returns the categoryLoop.
	 */
	public boolean isCategoryLoop() {
		return categoryLoop;
	}
	/**
	 * @param categoryLoop The categoryLoop to set.
	 */
	public void setCategoryLoop(boolean categoryLoop) {
		this.categoryLoop = categoryLoop;
	}
	/**
	 * @return Returns the categoryPojoClassName.
	 */
	public String getCategoryPojoClassName() {
		return categoryPojoClassName;
	}
	/**
	 * @param categoryPojoClassName The categoryPojoClassName to set.
	 */
	public void setCategoryPojoClassName(String categoryPojoClassName) {
		this.categoryPojoClassName = categoryPojoClassName;
	}
	/**
	 * @return Returns the categoryRoot.
	 */
	public String getCategoryRoot() {
		return categoryRoot;
	}
	/**
	 * @param categoryRoot The categoryRoot to set.
	 */
	public void setCategoryRoot(String categoryRoot) {
		this.categoryRoot = categoryRoot;
	}
	/**
	 * @return Returns the columns.
	 */
	public List getColumns() {
		return columns;
	}
	/**
	 * @param columns The columns to set.
	 */
	public void setColumns(List columns) {
		this.columns = columns;
	}
	/**
	 * @return Returns the filter.
	 */
	public String getFilter() {
		return filter;
	}
	/**
	 * @param filter The filter to set.
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}
	/**
	 * @return Returns the form.
	 */
	public String getForm() {
		return form;
	}
	/**
	 * @param form The form to set.
	 */
	public void setForm(String form) {
		this.form = form;
	}
	/**
	 * @return Returns the hideCondition.
	 */
	public String getHideCondition() {
		return hideCondition;
	}
	/**
	 * @param hideCondition The hideCondition to set.
	 */
	public void setHideCondition(String hideCondition) {
		this.hideCondition = hideCondition;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the openFeatures.
	 */
	public String getOpenFeatures() {
		return openFeatures;
	}
	/**
	 * @param openFeatures The openFeatures to set.
	 */
	public void setOpenFeatures(String openFeatures) {
		this.openFeatures = openFeatures;
	}
	/**
	 * @return Returns the pageRows.
	 */
	public int getPageRows() {
		return pageRows<0 ? 20 : pageRows;
	}
	/**
	 * @param pageRows The pageRows to set.
	 */
	public void setPageRows(int pageRows) {
		this.pageRows = pageRows;
	}
	/**
	 * @return Returns the pojoClassName.
	 */
	public String getPojoClassName() {
		return pojoClassName;
	}
	/**
	 * @param pojoClassName The pojoClassName to set.
	 */
	public void setPojoClassName(String pojoClassName) {
		this.pojoClassName = pojoClassName;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return Returns the orderBy.
	 */
	public String getOrderBy() {
		return orderBy;
	}
	/**
	 * @param orderBy The orderBy to set.
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	/**
	 * @return Returns the where.
	 */
	public String getWhere() {
		return where;
	}
	/**
	 * @param where The where to set.
	 */
	public void setWhere(String where) {
		this.where = where;
	}
	/**
	 * @return Returns the quickFilter.
	 */
	public String getQuickFilter() {
		return quickFilter;
	}
	/**
	 * @param quickFilter The quickFilter to set.
	 */
	public void setQuickFilter(String quickFilter) {
		this.quickFilter = quickFilter;
	}
	/**
	 * @return Returns the groupBy.
	 */
	public String getGroupBy() {
		return groupBy;
	}
	/**
	 * @param groupBy The groupBy to set.
	 */
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	/**
	 * @return Returns the width.
	 */
	public String getWidth() {
		return width;
	}
	/**
	 * @param width The width to set.
	 */
	public void setWidth(String width) {
		this.width = width;
	}
	/**
	 * @return Returns the application.
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param application The application to set.
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the links
	 */
	public List getLinks() {
		return links;
	}
	/**
	 * @param links the links to set
	 */
	public void setLinks(List links) {
		this.links = links;
	}
	/**
	 * @return the viewServiceName
	 */
	public String getViewServiceName() {
		return viewServiceName;
	}
	/**
	 * @param viewServiceName the viewServiceName to set
	 */
	public void setViewServiceName(String viewServiceName) {
		this.viewServiceName = viewServiceName;
	}

	/**
	 * @return the newestCheckBy
	 */
	public String getNewestCheckBy() {
		return newestCheckBy;
	}

	/**
	 * @param newestCheckBy the newestCheckBy to set
	 */
	public void setNewestCheckBy(String newestCheckBy) {
		this.newestCheckBy = newestCheckBy;
	}

	/**
	 * @return the scripts
	 */
	public List getScripts() {
		return scripts;
	}

	/**
	 * @param scripts the scripts to set
	 */
	public void setScripts(List scripts) {
		this.scripts = scripts;
	}

	/**
	 * @return the join
	 */
	public String getJoin() {
		return join;
	}

	/**
	 * @param join the join to set
	 */
	public void setJoin(String join) {
		this.join = join;
	}

	/**
	 * @return the parameters
	 */
	public Map getParameters() {
		return parameters;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}