/*
 * Created on 2004-12-21
 *
 */
package com.yuanluesoft.jeaf.view.model;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 
 * @author linchuan
 *
 */
public class Column extends CloneableObject {
	public static final String COLUMN_TYPE_FIELD = "field"; //字段
	public static final String COLUMN_TYPE_SELECT = "select"; //选择
	public static final String COLUMN_TYPE_ROWNUM = "rownum"; //序号
	public static final String COLUMN_TYPE_STATISTIC = "statistic"; //统计
	public static final String COLUMN_TYPE_FORMULA = "formula"; //公式
	
	private String name; //字段名称
	private String title; //标题
	private String type; //类型,field(默认)/rownum(行号)/select(选择框)
	private String width; //显示宽度
	private String align; //对齐方式,left/center/right
	private String format; //显示格式
	private boolean hideTitle; //是否隐藏标题
	private boolean hideZero; //是否隐藏"0"
	private String display; //显示选项
	private String displayExcept; //显示选项,扣除显示方式,当display为空时有效
	private String prefix; //前缀
	private String postfix; //后缀
	private Link link; //链接
	
	//公式相关
	private String formula; //公式
	private String formulaFields; //公式使用到的字段
	
	//集合相关
	private int length; //显示集合中的元素个数,0表示全部
	private String separator; //显示分隔符,默认","
	
	private String ellipsis; //显示省略符号,如"...","等",默认为空
	
	//最多显示字符数
	private int maxCharCount;
	
	public Column() {
		super();
	}
	
	public Column(String name, String title, String type) {
		super();
		this.name = name;
		this.title = title;
		this.type = type;
	}

	/**
	 * @return Returns the align.
	 */
	public String getAlign() {
		return align;
	}
	/**
	 * @param align The align to set.
	 */
	public void setAlign(String align) {
		this.align = align;
	}
	/**
	 * @return Returns the hideTitle.
	 */
	public boolean isHideTitle() {
		return hideTitle;
	}
	/**
	 * @param hideTitle The hideTitle to set.
	 */
	public void setHideTitle(boolean hideTitle) {
		this.hideTitle = hideTitle;
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
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return Returns the format.
	 */
	public String getFormat() {
		return format;
	}
	/**
	 * @param format The format to set.
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	/**
	 * @return Returns the display.
	 */
	public String getDisplay() {
		return display;
	}
	/**
	 * @param display The display to set.
	 */
	public void setDisplay(String display) {
		this.display = display;
	}
    /**
     * @return Returns the displayExcept.
     */
    public String getDisplayExcept() {
        return displayExcept;
    }
    /**
     * @param displayExcept The displayExcept to set.
     */
    public void setDisplayExcept(String displayExcept) {
        this.displayExcept = displayExcept;
    }
    /**
     * @return Returns the ellipsis.
     */
    public String getEllipsis() {
        return ellipsis;
    }
    /**
     * @param ellipsis The ellipsis to set.
     */
    public void setEllipsis(String ellipsis) {
        this.ellipsis = ellipsis;
    }
    /**
     * @return Returns the length.
     */
    public int getLength() {
        return length;
    }
    /**
     * @param length The length to set.
     */
    public void setLength(int length) {
        this.length = length;
    }
    /**
     * @return Returns the separator.
     */
    public String getSeparator() {
        return separator;
    }
    /**
     * @param separator The separator to set.
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }
	/**
	 * @return the maxCharCount
	 */
	public int getMaxCharCount() {
		return maxCharCount;
	}
	/**
	 * @param maxCharCount the maxCharCount to set
	 */
	public void setMaxCharCount(int maxCharCount) {
		this.maxCharCount = maxCharCount;
	}
	/**
	 * @return the link
	 */
	public Link getLink() {
		return link;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(Link link) {
		this.link = link;
	}

	/**
	 * @return the hideZero
	 */
	public boolean isHideZero() {
		return hideZero;
	}

	/**
	 * @param hideZero the hideZero to set
	 */
	public void setHideZero(boolean hideZero) {
		this.hideZero = hideZero;
	}

	/**
	 * @return the formula
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * @param formula the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * @return the formulaFields
	 */
	public String getFormulaFields() {
		return formulaFields;
	}

	/**
	 * @param formulaFields the formulaFields to set
	 */
	public void setFormulaFields(String formulaFields) {
		this.formulaFields = formulaFields;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the postfix
	 */
	public String getPostfix() {
		return postfix;
	}

	/**
	 * @param postfix the postfix to set
	 */
	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}
}