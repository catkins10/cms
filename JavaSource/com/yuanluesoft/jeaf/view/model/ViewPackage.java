/*
 * Created on 2005-5-17
 *
 */
package com.yuanluesoft.jeaf.view.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * @author root
 *
 *
 */
public class ViewPackage implements Serializable {
	public static String VIEW_PACKAGE_NAME = "VIEW_PACKAGE_NAME";
	
	private View view; //视图定义
	private int rowNum; //记录行号
	private String sortColumn; //当前排序列
	private boolean descendingSort; //是否降序排列
	
	private String currentViewAction;
	
	private List records; //记录集
	private int recordCount; //记录总数
	private int curPage; //当前页
	private int pageCount; //总页数
	private String page; //页跳转
	
	private int categoryCount; //分类级数,0:无限级,其他:级数
	private String categories; //当前分类
	private List categoryTitles; //分类标题,包括所有层次
	private String categoryTitle; //当前分类标题
	
	private String viewMode = View.VIEW_DISPLAY_MODE_NORMAL; //视图显示模式
	private String selectedIds; //选中的ID列表
	
	private String searchConditions; //搜索条件
	private String searchConditionsDescribe; //搜索条件描述
	private List searchConditionList; //搜索条件列表,如果为空,则从searchConditions解析
	private String quickFilter; //快速过虑关键字
	private boolean searchMode; //是否搜索模式
	
	private String calendarMode; //日历类型:month/week/day
	private Date beginCalendarDate;	 //日历开始时间
	
	private List location; //当前视图在整个系统中的位置描述

	/**
	 * @return Returns the descendingSort.
	 */
	public boolean isDescendingSort() {
		return descendingSort;
	}
	/**
	 * @param descendingSort The descendingSort to set.
	 */
	public void setDescendingSort(boolean descendingSort) {
		this.descendingSort = descendingSort;
	}
	/**
	 * @return Returns the rowNum.
	 */
	public int getRowNum() {
		return rowNum++;
	}
	/**
	 * @param rowNum The rowNum to set.
	 */
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	/**
	 * @return Returns the sortColumn.
	 */
	public String getSortColumn() {
		return sortColumn;
	}
	/**
	 * @param sortColumn The sortColumn to set.
	 */
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	/**
	 * @return Returns the viewDefine.
	 */
	public View getView() {
		return view;
	}
	/**
	 * @param viewDefine The viewDefine to set.
	 */
	public void setView(View view) {
		this.view = view;
	}
	/**
	 * @return Returns the categories.
	 */
	public String getCategories() {
		return categories;
	}
	/**
	 * @param categories The categories to set.
	 */
	public void setCategories(String categories) {
		this.categories = categories;
	}
	/**
	 * @return Returns the categoryCount.
	 */
	public int getCategoryCount() {
		return categoryCount;
	}
	/**
	 * @param categoryCount The categoryCount to set.
	 */
	public void setCategoryCount(int categoryCount) {
		this.categoryCount = categoryCount;
	}
	/**
	 * @return Returns the categoryTitle.
	 */
	public String getCategoryTitle() {
		return categoryTitle;
	}
	/**
	 * @param categoryTitle The categoryTitle to set.
	 */
	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}
	/**
	 * @return Returns the curPage.
	 */
	public int getCurPage() {
		return curPage;
	}
	/**
	 * @param curPage The curPage to set.
	 */
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	/**
	 * @return Returns the pageCount.
	 */
	public int getPageCount() {
		return pageCount;
	}
	/**
	 * @param pageCount The pageCount to set.
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	/**
	 * @return Returns the recordCount.
	 */
	public int getRecordCount() {
		return recordCount;
	}
	/**
	 * @param recordCount The recordCount to set.
	 */
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	/**
	 * @return Returns the page.
	 */
	public String getPage() {
		return page;
	}
	/**
	 * @param page The page to set.
	 */
	public void setPage(String page) {
		this.page = page;
	}
	/**
	 * @return Returns the searchConditions.
	 */
	public String getSearchConditions() {
		return searchConditions;
	}
	/**
	 * @param searchConditions The searchConditions to set.
	 */
	public void setSearchConditions(String searchConditions) {
		this.searchConditions = searchConditions;
	}
	/**
	 * @return Returns the beginCalendarDate.
	 */
	public Date getBeginCalendarDate() {
		return beginCalendarDate;
	}
	/**
	 * @param beginCalendarDate The beginCalendarDate to set.
	 */
	public void setBeginCalendarDate(Date beginCalendarDate) {
		this.beginCalendarDate = beginCalendarDate;
	}
	/**
	 * @return Returns the calendarMode.
	 */
	public String getCalendarMode() {
		return calendarMode;
	}
	/**
	 * @param calendarMode The calendarMode to set.
	 */
	public void setCalendarMode(String calendarMode) {
		this.calendarMode = calendarMode;
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
     * @return Returns the selectedIds.
     */
    public String getSelectedIds() {
        return selectedIds;
    }
    /**
     * @param selectedIds The selectedIds to set.
     */
    public void setSelectedIds(String selectedIds) {
        this.selectedIds = selectedIds;
    }
    /**
     * @return Returns the searchConditionsDescribe.
     */
    public String getSearchConditionsDescribe() {
        return searchConditionsDescribe;
    }
    /**
     * @param searchConditionsDescribe The searchConditionsDescribe to set.
     */
    public void setSearchConditionsDescribe(String searchConditionsDescribe) {
        this.searchConditionsDescribe = searchConditionsDescribe;
    }
	/**
	 * @return Returns the currentViewAction.
	 */
	public String getCurrentViewAction() {
		return currentViewAction;
	}
	/**
	 * @param currentViewAction The currentViewAction to set.
	 */
	public void setCurrentViewAction(String currentViewAction) {
		this.currentViewAction = currentViewAction;
	}
	/**
	 * @return the location
	 */
	public List getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(List location) {
		this.location = location;
	}
	/**
	 * @return the categoryTitles
	 */
	public List getCategoryTitles() {
		return categoryTitles;
	}
	/**
	 * @param categoryTitles the categoryTitles to set
	 */
	public void setCategoryTitles(List categoryTitles) {
		this.categoryTitles = categoryTitles;
	}
	/**
	 * @return the records
	 */
	public List getRecords() {
		return records;
	}
	/**
	 * @param records the records to set
	 */
	public void setRecords(List records) {
		this.records = records;
	}
	/**
	 * @return the searchMode
	 */
	public boolean isSearchMode() {
		return searchMode;
	}
	/**
	 * @param searchMode the searchMode to set
	 */
	public void setSearchMode(boolean searchMode) {
		this.searchMode = searchMode;
	}
	/**
	 * @return the viewMode
	 */
	public String getViewMode() {
		return viewMode;
	}
	/**
	 * @param viewMode the viewMode to set
	 */
	public void setViewMode(String viewMode) {
		this.viewMode = viewMode;
	}
	/**
	 * @return the searchConditionList
	 */
	public List getSearchConditionList() {
		return searchConditionList;
	}
	/**
	 * @param searchConditionList the searchConditionList to set
	 */
	public void setSearchConditionList(List searchConditionList) {
		this.searchConditionList = searchConditionList;
	}
}