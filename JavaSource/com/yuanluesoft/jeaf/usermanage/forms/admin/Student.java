/*
 * Created on 2007-4-12
 *
 */
package com.yuanluesoft.jeaf.usermanage.forms.admin;

import java.util.Set;

import com.yuanluesoft.jeaf.usermanage.pojo.Genearch;

/**
 * @author Administrator
 *
 *
 */
public class Student extends Person {
	private int seatNumber;
	private Set genearches; //家长列表
	
	private String genearchTitle; //家长称呼
	private char registNewGenearch = '0'; //是否注册新的家长帐号
	private Genearch genearch = new Genearch(); //用于注册新家长
	private long selectedGenearchId; //选中的家长ID
	private String selectedGenearchName; //选中的家长姓名
	
	/**
	 * @return the genearchs
	 */
	public Set getGenearches() {
		return genearches;
	}
	/**
	 * @param genearchs the genearchs to set
	 */
	public void setGenearches(Set genearches) {
		this.genearches = genearches;
	}
	/**
	 * @return the seatNumber
	 */
	public int getSeatNumber() {
		return seatNumber;
	}
	/**
	 * @param seatNumber the seatNumber to set
	 */
	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}
	/**
	 * @return the genearch
	 */
	public Genearch getGenearch() {
		return genearch;
	}
	/**
	 * @param genearch the genearch to set
	 */
	public void setGenearch(Genearch genearch) {
		this.genearch = genearch;
	}
	/**
	 * @return the registNewGenearch
	 */
	public char getRegistNewGenearch() {
		return registNewGenearch;
	}
	/**
	 * @param registNewGenearch the registNewGenearch to set
	 */
	public void setRegistNewGenearch(char registNewGenearch) {
		this.registNewGenearch = registNewGenearch;
	}
	/**
	 * @return the genearchTitle
	 */
	public String getGenearchTitle() {
		return genearchTitle;
	}
	/**
	 * @param genearchTitle the genearchTitle to set
	 */
	public void setGenearchTitle(String genearchTitle) {
		this.genearchTitle = genearchTitle;
	}
	/**
	 * @return the selectedGenearchId
	 */
	public long getSelectedGenearchId() {
		return selectedGenearchId;
	}
	/**
	 * @param selectedGenearchId the selectedGenearchId to set
	 */
	public void setSelectedGenearchId(long selectedGenearchId) {
		this.selectedGenearchId = selectedGenearchId;
	}
	/**
	 * @return the selectedGenearchName
	 */
	public String getSelectedGenearchName() {
		return selectedGenearchName;
	}
	/**
	 * @param selectedGenearchName the selectedGenearchName to set
	 */
	public void setSelectedGenearchName(String selectedGenearchName) {
		this.selectedGenearchName = selectedGenearchName;
	}
}