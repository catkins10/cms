/*
 * Created on 2006-3-24
 *
 */
package com.yuanluesoft.jeaf.view.parser.dom4j;

import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Element;

import com.yuanluesoft.jeaf.exception.ParseException;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.ListUtils;
import com.yuanluesoft.jeaf.view.model.Category;
import com.yuanluesoft.jeaf.view.model.Column;
import com.yuanluesoft.jeaf.view.model.Link;
import com.yuanluesoft.jeaf.view.model.View;
import com.yuanluesoft.jeaf.view.model.viewaction.NewWorkflowInstanceAction;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewAction;
import com.yuanluesoft.jeaf.view.model.viewaction.ViewActionGroup;

/**
 * 
 * @author linchuan
 * 
 */
public class ViewParser {
	public static final String VIEW_ACTION_TYPE_WORKFLOW = "workflow"; //新流程操作
	
	/**
	 * 根据视图XML配置解析视图定义
	 * @param xmlView
	 * @param defaultOpenFeatures
	 * @return
	 * @throws ParseException
	 */
	public View parseView(final String applicationName, final Element xmlView) throws ParseException {
		View view = new View();
		parseView(applicationName, xmlView, view);
		//视图列列表
		Element xmlElement = xmlView.element("columns");
		if(xmlElement!=null) {
			ArrayList columns = new ArrayList();
			for(Iterator iterator = xmlElement.elementIterator(); iterator.hasNext();) {
				columns.add(parseColumn(view, (Element)iterator.next()));
			}
			view.setColumns(columns);
		}
		return view;
	}
	
	/**
	 * 根据视图XML配置解析视图定义
	 * @param xmlView
	 * @param view
	 * @param defaultOpenFeatures
	 * @return
	 * @throws ParseException
	 */
	protected View parseView(final String application, final Element xmlView, View view) throws ParseException {
		view.setApplicationName(application);
		view.setName(xmlView.attributeValue("name")); //名称
		view.setTitle(xmlView.attributeValue("title")); //标题
		view.setHideCondition(xmlView.attributeValue("hide")); //隐藏条件
		String attribute = xmlView.attributeValue("openFeatures");
		view.setOpenFeatures(attribute); //新窗口打开方式
		view.setForm(xmlView.attributeValue("form")); //配置表单名称
		view.setUrl(xmlView.attributeValue("url")); //打开视图的URL
		Element xmlLinks = xmlView.element("links");
		if(xmlLinks!=null) {
			view.setLinks(new ArrayList());
			for(Iterator iterator = xmlLinks.elementIterator(); iterator.hasNext();) {
				Element xmlLink = (Element)iterator.next();
				view.getLinks().add(parseLink(xmlLink)); //解析链接
			}
		}
		else if(view.getForm()!=null) {
			Link link = new Link();
			link.setUrl("javascript:PageUtils.openurl('" + Environment.getWebApplicationUrl() + "/" + view.getApplicationName() + "/" + view.getForm() + ".shtml?act=edit&id={PARAMETER:id}&seq={RANDOMNUMBER}', '" + view.getOpenFeatures() + "', '{PARAMETER:id}')");
			link.setType("recordLink");
			view.setLinks(new ArrayList());
			view.getLinks().add(link);
		}
		//每页行数
		attribute = xmlView.attributeValue("pageRows");
		if(attribute!=null) {
			view.setPageRows(new Integer(attribute).intValue());
		}
		else {
			view.setPageRows(20);
		}
		//宽度
		view.setWidth(xmlView.attributeValue("width"));
		if(view.getWidth()==null) {
			view.setWidth("100%");
		}
		//视图服务
		view.setViewServiceName(xmlView.attributeValue("viewServiceName"));
		
		//orm映射类
		Element xmlData = xmlView.element("data");
		if(xmlData!=null) {
			Element xmlElement = xmlData.element("pojo");
			if(xmlElement!=null) {
				view.setPojoClassName(xmlElement.getText());
			}
			//join子句
			xmlElement = xmlData.element("join");
			if(xmlElement!=null) {
				view.setJoin(xmlElement.getText());
			}
			//where子句
			xmlElement = xmlData.element("where");
			if(xmlElement!=null) {
				view.setWhere(xmlElement.getText());
			}
			//order by子句
			xmlElement = xmlData.element("groupBy");
			if(xmlElement!=null) {
				view.setGroupBy(xmlElement.getText());
			}
			//order by子句
			xmlElement = xmlData.element("orderBy");
			if(xmlElement!=null) {
				view.setOrderBy(xmlElement.getText());
			}
			//数据过滤选项
			xmlElement = xmlData.element("filter");
			if(xmlElement!=null) {
				view.setFilter(xmlElement.getText());
			}
			//对话框数据过滤选项
			xmlElement = xmlData.element("quickFilter");
			if(xmlElement!=null) {
				view.setQuickFilter(xmlElement.getText());
			}
			//newestCheckBy
			xmlElement = xmlData.element("newestCheckBy");
			if(xmlElement!=null) {
				view.setNewestCheckBy(xmlElement.getText());
			}
		}
		//分类属性设置
		Element xmlElement = xmlView.element("categories");
		if(xmlElement!=null) {
			view.setCategoryRoot(xmlElement.attributeValue("root")); //根标题
			view.setBeginCategory(xmlElement.attributeValue("beginCategory")); //起始分类
			attribute = xmlElement.attributeValue("loop");
			view.setCategoryLoop(attribute!=null && attribute.equals("true")); //循环查找下一级分类
			xmlData = xmlElement.element("data");
			view.setCategoryPojoClassName(xmlData.element("pojo").getText()); //分类orm映射
			xmlData = (Element)xmlData.element("filter");
			if(xmlData!=null) {
				view.setCategoryFilter(xmlData.getText()); //分类的数据过滤选项
			}
			//分类列表
			int size = xmlElement.elements().size();
			ArrayList categories = new ArrayList(size);
			for(int i=0; i<size; i++) {
				Element xmlCategory = (Element)xmlElement.elements().get(i);
				if(xmlCategory.getName().equals("category")) {
					categories.add(parseCategory(xmlCategory));
				}
			}
			view.setCategories(categories);
		}
		//脚本列表
		xmlElement = xmlView.element("scripts");
		if(xmlElement!=null) {
			ArrayList scripts = new ArrayList();
			for(Iterator iterator = xmlElement.elementIterator(); iterator.hasNext();) {
				Element scriptElement = (Element)iterator.next();
				scripts.add(scriptElement.getText());
			}
			view.setScripts(scripts);
		}
		//操作列表
		xmlElement = xmlView.element("actions");
		if(xmlElement!=null) {
			ArrayList actions = new ArrayList();
			for(Iterator iterator = xmlElement.elementIterator(); iterator.hasNext();) {
				Element xmlAction = (Element)iterator.next();
				if("group".equals(xmlAction.getName())) { //分组
					ViewActionGroup group = new ViewActionGroup();
					group.setTitle(xmlAction.attributeValue("title")); //标题
					group.setImage(xmlAction.attributeValue("image")); //操作图标
					//解析分组中的操作
					group.setActions(new ArrayList());
					for(Iterator iteratorAction = xmlAction.elementIterator(); iteratorAction.hasNext();) {
						Element element = (Element)iteratorAction.next();
						group.getActions().add(parseViewAction(view, element));
					}
					actions.add(group);
				}
				else {
					actions.add(parseViewAction(view, xmlAction));
				}
			}
			view.setActions(actions);
		}
		//扩展参数列表,用来增加应用程序需要的其他参数
		xmlElement = xmlView.element("extendParameters");
		if(xmlElement!=null) {
			for(Iterator iterator = xmlElement.elementIterator("parameter"); iterator.hasNext();) {
				Element xmlParameter = (Element)iterator.next();
				view.setExtendParameter(xmlParameter.attributeValue("name"), xmlParameter.getText());
			}
		}
		return view;
	}
	/**
	 * 根据分类XML配置解析分类定义
	 * @param xmlModule
	 * @return
	 * @throws ApplicationDefineParseException
	 */
	protected Category parseCategory(final Element xmlCategory) throws ParseException {
		Category category = new Category();
		category.setTitleProperty(xmlCategory.attributeValue("title")); //显示属性
		category.setTitleHql(xmlCategory.attributeValue("titleHql")); //显示属性对应的HQL
		category.setValueProperty(xmlCategory.attributeValue("value")); //值属性
		category.setValueType(xmlCategory.attributeValue("valueType")); //值类型,string(默认)/number/其他
		Element xmlElement = xmlCategory.element("where");
		if(xmlElement!=null) {
			category.setWhere(xmlElement.getText()); //条件
		}
		xmlElement = xmlCategory.element("orderBy");
		if(xmlElement!=null) {
			category.setOrderBy(xmlElement.getText()); //排序
		}
		xmlElement = xmlCategory.element("data");
		if(xmlElement!=null) {
			category.setViewDataFilter(xmlElement.getText()); //视图数据过滤条件
		}
		return category;
	}
	/**
	 * 根据操作XML配置解析操作定义
	 * @param xmlModule
	 * @return
	 * @throws ApplicationDefineParseException
	 */
	protected ViewAction parseViewAction(final View view, final Element xmlAction) throws ParseException {
		ViewAction viewAction = null;
		String type = xmlAction.attributeValue("type");
		if(VIEW_ACTION_TYPE_WORKFLOW.equals(type)) { //新流程实例操作
		    viewAction = new NewWorkflowInstanceAction();
		    parseNewWorkflowInstanceAction((NewWorkflowInstanceAction)viewAction, view, xmlAction);
		}
		else { //普通操作
		    viewAction = new ViewAction();
		    parseBaseViewAction(viewAction, xmlAction);
		}
		return viewAction;
	}
	/**
	 * 解析基本视图操作
	 * @param viewAction
	 * @param xmlAction
	 * @throws ParseException
	 */
	private void parseBaseViewAction(ViewAction viewAction, final Element xmlAction) throws ParseException {
	    viewAction.setTitle(xmlAction.attributeValue("title")); //标题
	    viewAction.setGroupTitle(xmlAction.attributeValue("groupTitle")); //分组时的标题
	    viewAction.setHideCondition(xmlAction.attributeValue("hide")); //隐藏条件
	    viewAction.setImage(xmlAction.attributeValue("image")); //操作图标
	    //解析执行的操作
	    String execute = xmlAction.attributeValue("execute");
	    if(execute!=null) {
		    viewAction.setExecute(execute.replaceAll("\\x7bCONTEXTPATH\\x7d", Environment.getContextPath()).replaceAll("\\x7bWEBAPPLICATIONURL\\x7d", Environment.getWebApplicationUrl()).replaceAll("\\x7bWEBAPPLICATIONSAFEURL\\x7d", Environment.getWebApplicationSafeUrl()));
	    }
	}
	
	/**
	 * 解析新流程操作
	 * @param newWorkflowInstanceAction
	 * @param view
	 * @param xmlAction
	 * @throws ParseException
	 */
	private void parseNewWorkflowInstanceAction(NewWorkflowInstanceAction newWorkflowInstanceAction, View view, final Element xmlAction) throws ParseException {
		parseBaseViewAction(newWorkflowInstanceAction, xmlAction);
	    //设置应用名称
	    newWorkflowInstanceAction.setApplicationName(xmlAction.attributeValue("application"));
	    if(newWorkflowInstanceAction.getApplicationName()==null) {
	        newWorkflowInstanceAction.setApplicationName(view.getApplicationName());
	    }
	    //设置表单
	    newWorkflowInstanceAction.setForm(xmlAction.attributeValue("form"));
	    if(newWorkflowInstanceAction.getForm()==null) {
	        newWorkflowInstanceAction.setForm(view.getForm());
	    }
	    //设置窗口打开方式
	    newWorkflowInstanceAction.setOpenFeatures(xmlAction.attributeValue("openFeatures"));
	    if(newWorkflowInstanceAction.getOpenFeatures()==null) {
	        newWorkflowInstanceAction.setOpenFeatures(view.getOpenFeatures());
	    }
	}
	/**
	 * 根据列XML配置解析列定义
	 * @param view
	 * @param xmlColumn
	 * @return
	 * @throws ParseException
	 */
	protected Column parseColumn(final View view, final Element xmlColumn) throws ParseException {
		return parseColumn(view, new Column(), xmlColumn);
	}
	/**
	 * 根据列XML配置解析列定义
	 * @param xmlColumn
	 * @return
	 * @throws ApplicationDefineParseException
	 */
	protected Column parseColumn(final View view, Column column, final Element xmlColumn) throws ParseException {
		String attribute =xmlColumn.attributeValue("type");
		column.setType(attribute==null || attribute.equals("") ? "field" : attribute); //类型,field(默认)/rownum(行号)/select(选择框)
		column.setName(xmlColumn.attributeValue("name")); //字段名称
		column.setTitle(xmlColumn.attributeValue("title")); //标题
		column.setWidth(xmlColumn.attributeValue("width")); //显示宽度
		column.setAlign(xmlColumn.attributeValue("align")); //对齐方式,left/center/right
		column.setFormat(xmlColumn.attributeValue("format")); //显示格式
		column.setPrefix(xmlColumn.attributeValue("prefix")); //前缀
		column.setPostfix(xmlColumn.attributeValue("postfix")); //后缀
		column.setLink(parseLink(xmlColumn.element("link")));
		if(column.getLink()==null) {
			column.setLink((Link)ListUtils.findObjectByProperty(view.getLinks(), "type", "recordLink"));
		}
		column.setHideTitle("true".equals(xmlColumn.attributeValue("hideTitle"))); //是否隐藏标题
		column.setHideZero("true".equals(xmlColumn.attributeValue("hideZero"))); //是否隐藏"0"
		column.setDisplay(xmlColumn.attributeValue("display")); //显示模式
		column.setDisplayExcept(xmlColumn.attributeValue("displayExcept")); //显示模式
		//公式相关
		column.setFormula(xmlColumn.attributeValue("formula")); //公式
		column.setFormulaFields(xmlColumn.attributeValue("formulaFields")); //公式使用到的字段
		//集合相关
		attribute = xmlColumn.attributeValue("length");
		column.setLength(attribute==null ? 0 : Integer.parseInt(attribute)); //显示集合中的元素个数,0表示全部
		attribute = xmlColumn.attributeValue("separator");
		column.setSeparator(attribute==null ?  "," : attribute); //显示分隔符,默认","
		attribute = xmlColumn.attributeValue("ellipsis");
		column.setEllipsis(attribute==null ? "" : attribute); //显示省略符号,如"...","等",默认为空
		
		//最多显示字符数
		attribute =xmlColumn.attributeValue("maxCharCount");
		column.setMaxCharCount(attribute==null || attribute.equals("") ? 0 : Integer.parseInt(attribute));
		return column;
	}
	/**
	 * 解析链接
	 * @param xmlLink
	 * @return
	 * @throws ParseException
	 */
	protected Link parseLink(Element xmlLink) throws ParseException {
		if(xmlLink==null) {
			return null;
		}
		Link link = new Link();
		link.setTitle(xmlLink.attributeValue("title")); //链接名称
		link.setType(xmlLink.attributeValue("type")); //链接类型,包括:recordLink/记录页面,hostLink/宿主页面,点击“更多...”显示的页面,默认为applicationView.shtml
		if(link.getType()==null) {
			link.setType("recordLink");
		}
		link.setHideCondition(xmlLink.attributeValue("hideCondition")); //隐藏条件
		link.setTarget(xmlLink.attributeValue("target")); //默认的目标窗口
		link.setWidth(xmlLink.attributeValue("width")); //默认的窗口宽度
		link.setHeight(xmlLink.attributeValue("height")); //默认的窗口高度
		link.setFullScreen("true".equals(xmlLink.attributeValue("fullScreen"))); //是否全屏显示
		link.setUrl(xmlLink.getText());
		return link;
	}
	
	/**
	 * 生效视图XML文本
	 * @param view
	 * @return
	 * @throws ParseException
	 */
	public void generateViewXML(View view, Element xmlView) throws ParseException {
		xmlView.addAttribute("name", view.getName()); //视图名称
		xmlView.addAttribute("title", view.getTitle()); //标题
		xmlView.addAttribute("form", view.getForm()); //表单
		xmlView.addAttribute("openFeatures", view.getOpenFeatures()); //新窗口打开方式,未设定则继承上一级的打开方式
		xmlView.addAttribute("hide", view.getHideCondition()); //隐藏条件
		xmlView.addAttribute("pageRows", view.getPageRows()==0 ? null : "" + view.getPageRows()); //每页显示行数
		xmlView.addAttribute("width", view.getWidth()); //表格宽度
		xmlView.addAttribute("viewServiceName", view.getViewServiceName()); //视图服务名称,默认为viewService
		
		//数据相关属性
		Element xmlData = xmlView.addElement("data");
		xmlData.addElement("pojo").setText(view.getPojoClassName()); //orm映射类名称
		if(view.getWhere()!=null) {
			xmlData.addElement("where").setText(view.getWhere()); //hql where 子句
		}
		if(view.getGroupBy()!=null) {
			xmlData.addElement("groupBy").setText(view.getGroupBy()); //hql group by 子句
		}
		if(view.getOrderBy()!=null) {
			xmlData.addElement("orderBy").setText(view.getOrderBy()); //hql order by 子句
		}
		if(view.getFilter()!=null) {
			xmlData.addElement("filter").setText(view.getFilter()); //数据过滤选项, READABLE(允许读取)/EDITABLE(允许编辑)/其他
		}
		if(view.getQuickFilter()!=null) {
			xmlData.addElement("quickFilter").setText(view.getQuickFilter()); //对话框数据过滤选项
		}
		if(view.getNewestCheckBy()!=null) {
			xmlData.addElement("newestCheckBy").setText(view.getNewestCheckBy()); //检查是否新记录时使用的属性
		}
		if(view.getJoin()!=null) {
			xmlData.addElement("join").setText(view.getJoin()); //JOIN子句扩展,默认
		}
		
		//分类相关属性
		if(view.getCategories()!=null && !view.getCategories().isEmpty()) {
			Element xmlCategories = xmlView.addElement("categories");
			xmlCategories.addAttribute("root", view.getCategoryRoot()); //根分类标题
			xmlCategories.addAttribute("beginCategory", view.getBeginCategory()); //初始分类
			xmlCategories.addAttribute("loop", "" + view.isCategoryLoop());
			
			Element xmlCategoryData = xmlCategories.addElement("data");
			xmlCategoryData.addElement("pojo").setText(view.getCategoryPojoClassName()); //分类的orm映射类名称
			if(view.getCategoryFilter()!=null) {
				xmlCategoryData.addElement("filter").setText(view.getCategoryFilter()); //分类数据过滤选项
			}
			for(Iterator iterator = view.getCategories().iterator(); iterator.hasNext();) { //分类定义列表
				Category category = (Category)iterator.next();
				Element xmlCategory = xmlCategories.addElement("category");
				xmlCategory.addAttribute("title", category.getTitleProperty()); //显示属性
				xmlCategory.addAttribute("titleHql", category.getTitleHql()); //显示属性对应的HQL
				xmlCategory.addAttribute("value", category.getValueProperty()); //值属性
				xmlCategory.addAttribute("valueType", category.getValueType()); //值类型,string(默认)/number/其他
				if(category.getWhere()!=null) {
					xmlCategory.addElement("where").setText(category.getWhere()); //条件
				}
				if(category.getWhere()!=null) {
					xmlCategory.addElement("orderBy").setText(category.getOrderBy()); //排序
				}
				if(category.getWhere()!=null) {
					xmlCategory.addElement("data").setText(category.getViewDataFilter()); //视图数据过滤条件
				}
			}
		}
		
		//需要引用的脚本文件
		if(view.getScripts()!=null && !view.getScripts().isEmpty()) {
			Element xmlScripts = xmlView.addElement("scripts");
			for(Iterator iterator = view.getScripts().iterator(); iterator.hasNext();) {
				String script = (String)iterator.next();
				xmlScripts.addElement("script").setText(script);
			}
		}
		
		//视图操作列表
		if(view.getActions()!=null && !view.getActions().isEmpty()) {
			Element xmlActions = xmlView.addElement("actions");
			for(Iterator iterator = view.getActions().iterator(); iterator.hasNext();) {
				Object object = iterator.next();
				if(object instanceof ViewAction) {
					generateViewActionXML((ViewAction)object, xmlActions);
				}
				else if(object instanceof ViewActionGroup) { //分组
					ViewActionGroup group = (ViewActionGroup)object;
					Element xmlGroup = xmlActions.addElement("group");
					xmlGroup.addAttribute("title", group.getTitle()); //标题
					xmlGroup.addAttribute("image", group.getImage()); //操作图标
					for(Iterator iteratorAction = group.getActions().iterator(); iteratorAction.hasNext();) {
						ViewAction action = (ViewAction)iteratorAction.next();
						generateViewActionXML(action, xmlGroup);
					}
				}
			}
		}
		
		//视图列列表
		if(view.getColumns()!=null && !view.getColumns().isEmpty()) {
			Element xmlColumns = xmlView.addElement("columns");
			for(Iterator iterator = view.getColumns().iterator(); iterator.hasNext();) {
				Column column = (Column)iterator.next();
				generateViewColumnXML(column, xmlColumns);
			}
		}
		
		//链接列表
		if(view.getLinks()!=null && !view.getLinks().isEmpty()) {
			Element xmlLinks = xmlView.addElement("links");
			for(Iterator iterator = view.getLinks().iterator(); iterator.hasNext();) {
				Link link = (Link)iterator.next();
				generateViewLinkXML(link, xmlLinks);
			}
		}
		
		//参数列表,为字段类型、输入方式配置所需要的参数
		if(view.getParameters()!=null && !view.getParameters().isEmpty()) {
			Element xmlExtendParameters = xmlView.addElement("extendParameters");
			for(Iterator iterator = view.getParameters().keySet().iterator(); iterator.hasNext();) {
				String name = (String)iterator.next();
				Element xmlParameter = xmlExtendParameters.addElement("parameter");
				xmlParameter.addAttribute("name", name);
				xmlParameter.setText((String)view.getParameters().get(name));
			}
		}
	}
	
	/**
	 * 生成视图操作XML
	 * @param viewAction
	 * @param parentElement
	 * @throws ParseException
	 */
	protected Element generateViewActionXML(ViewAction viewAction, Element parentElement) throws ParseException {
		Element xmlAction = parentElement.addElement("action");
		xmlAction.addAttribute("title", viewAction.getTitle()); //标题
		xmlAction.addAttribute("groupTitle", viewAction.getGroupTitle()); //分组时的标题
		xmlAction.addAttribute("hide", viewAction.getHideCondition()); //隐藏条件
		xmlAction.addAttribute("execute", viewAction.getExecute()); //执行的操作
		xmlAction.addAttribute("image", viewAction.getImage()); //操作按钮图标
		if(viewAction instanceof NewWorkflowInstanceAction) {
			NewWorkflowInstanceAction newWorkflowInstanceAction = (NewWorkflowInstanceAction)viewAction;
			xmlAction.addAttribute("type", VIEW_ACTION_TYPE_WORKFLOW);
			xmlAction.addAttribute("applicationName", newWorkflowInstanceAction.getApplicationName()); //指定应用名称,不设置时继承视图的应用名称
			xmlAction.addAttribute("openFeatures", newWorkflowInstanceAction.getOpenFeatures()); //窗口打开打开方式,不设置时继承视图的打开方式
			xmlAction.addAttribute("form", newWorkflowInstanceAction.getForm()); //表单名称,不设置时继承视图的表单名称
		}
		return xmlAction;
	}
	
	/**
	 * 生成列XML
	 * @param column
	 * @param parentElement
	 * @return
	 * @throws ParseException
	 */
	protected Element generateViewColumnXML(Column column, Element parentElement) throws ParseException {
		Element xmlColumn = parentElement.addElement("column");
		xmlColumn.addAttribute("name", column.getName()); //字段名称
		xmlColumn.addAttribute("title", column.getTitle()); //标题
		if(!"field".equals(column.getType())) {
			xmlColumn.addAttribute("type", column.getType()); //类型,field(默认)/rownum(行号)/select(选择框)
		}
		xmlColumn.addAttribute("width", column.getWidth()); //显示宽度
		xmlColumn.addAttribute("align", column.getAlign()); //对齐方式,left/center/right
		xmlColumn.addAttribute("format", column.getFormat()); //显示格式
		if(column.isHideTitle()) {
			xmlColumn.addAttribute("hideTitle", "true"); //是否隐藏标题
		}
		if(column.isHideZero()) {
			xmlColumn.addAttribute("hideZero", "true"); //是否隐藏"0"
		}
		xmlColumn.addAttribute("display", column.getDisplay()); //显示选项
		xmlColumn.addAttribute("displayExcept", column.getDisplayExcept()); //显示选项,扣除显示方式,当display为空时有效
		xmlColumn.addAttribute("formula", column.getFormula()); //公式
		xmlColumn.addAttribute("formulaFields", column.getFormulaFields()); //公式引用的字段
		if(column.getLength()>0) {
			xmlColumn.addAttribute("length", "" + column.getLength()); //显示集合中的元素个数,0表示全部
		}
		if(!",".equals(column.getSeparator())) {
			xmlColumn.addAttribute("separator", column.getSeparator()); //显示分隔符,默认","
		}
		xmlColumn.addAttribute("ellipsis", column.getEllipsis()); //显示省略符号,如"...","等",默认为空
		if(column.getMaxCharCount()>0) {
			xmlColumn.addAttribute("maxCharCount", "" + column.getMaxCharCount()); //最多显示字符数
		}
		if(column.getLink()!=null) { //链接
			generateViewLinkXML(column.getLink(), xmlColumn);
		}
		return xmlColumn;
	}
	
	/**
	 * 生成视图链接XML
	 * @param link
	 * @param parentElement
	 * @throws ParseException
	 */
	protected Element generateViewLinkXML(Link link, Element parentElement) throws ParseException {
		Element xmlLink = parentElement.addElement("link");
		xmlLink.addAttribute("title", link.getTitle()); //链接名称
		if(!"recordLink".equals(link.getType())) {
			xmlLink.addAttribute("type", link.getType()); //链接类型,包括:recordLink/记录页面,hostLink/宿主页面,点击“更多...”显示的页面,默认为applicationView.shtml
		}
		xmlLink.addAttribute("hideCondition", link.getHideCondition()); //隐藏条件
		xmlLink.addAttribute("target", link.getTarget()); //默认的目标窗口
		xmlLink.addAttribute("width", link.getWidth()); //默认的窗口宽度
		xmlLink.addAttribute("height", link.getHeight()); //默认的窗口高度
		if(link.isFullScreen()) {
			xmlLink.addAttribute("fullScreen", "true"); //是否全屏显示
		}
		xmlLink.setText(link.getUrl());
		return xmlLink;
	}
}