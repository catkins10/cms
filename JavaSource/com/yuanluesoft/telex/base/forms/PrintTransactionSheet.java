package com.yuanluesoft.telex.base.forms;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * 
 * @author linchuan
 *
 */
public class PrintTransactionSheet extends ActionForm {
	private long telegramId; //电报ID
	private long sheetId; //办理单ID
	private String sheet; //办理单HTML
	/**
	 * @return the sheet
	 */
	public String getSheet() {
		return sheet;
	}
	/**
	 * @param sheet the sheet to set
	 */
	public void setSheet(String sheet) {
		this.sheet = sheet;
	}
	/**
	 * @return the sheetId
	 */
	public long getSheetId() {
		return sheetId;
	}
	/**
	 * @param sheetId the sheetId to set
	 */
	public void setSheetId(long sheetId) {
		this.sheetId = sheetId;
	}
	/**
	 * @return the telegramId
	 */
	public long getTelegramId() {
		return telegramId;
	}
	/**
	 * @param telegramId the telegramId to set
	 */
	public void setTelegramId(long telegramId) {
		this.telegramId = telegramId;
	}
}