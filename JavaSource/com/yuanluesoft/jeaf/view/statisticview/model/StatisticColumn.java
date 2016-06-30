/*
 * Created on 2005-3-1
 *
 */
package com.yuanluesoft.jeaf.view.statisticview.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class StatisticColumn implements Serializable {
	private String name; //列名称
	private String function; //执行的统计函数
	private String whereExtend; //列统计扩展条件,当where不为空时whereExtend失效
	
	/**
	 * @return Returns the function.
	 */
	public String getFunction() {
		return function;
	}
	/**
	 * @param function The function to set.
	 */
	public void setFunction(String function) {
		this.function = function;
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
	 * @return Returns the whereExtend.
	 */
	public String getWhereExtend() {
		return whereExtend;
	}
	/**
	 * @param whereExtend The whereExtend to set.
	 */
	public void setWhereExtend(String whereExtend) {
		this.whereExtend = whereExtend;
	}
}