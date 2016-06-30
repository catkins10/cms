package com.yuanluesoft.im.pojo;

import com.yuanluesoft.im.service.IMService;
import com.yuanluesoft.jeaf.database.Record;

/**
 * 个人设置(im_personal_setting)
 * @author linchuan
 *
 */
public class IMPersonalSetting extends Record {
	private byte status; //上线后状态,在线、忙碌、隐身、不在电脑旁
	private int playSoundOnReceived; //消息到达是否发出声音
	private int setFocusOnReceived; //消息到达是否获取焦点
	private int openChatDialogOnReceived; //是否主动弹出对话窗口
	private int ctrlSend; //CTRL+Enter发送消息
	private String fontName; //字体
	private String fontSize; //字号
	private String fontColor; //颜色
	
	/**
	 * 获取状态说明
	 * @return
	 */
	public String getStatusText() {
		return IMService.IM_PERSON_STATUS_TEXTS[status - IMService.IM_PERSON_STATUS_OFFLINE];
	}
	
	/**
	 * @return the ctrlSend
	 */
	public int getCtrlSend() {
		return ctrlSend;
	}
	/**
	 * @param ctrlSend the ctrlSend to set
	 */
	public void setCtrlSend(int ctrlSend) {
		this.ctrlSend = ctrlSend;
	}
	/**
	 * @return the fontColor
	 */
	public String getFontColor() {
		return fontColor;
	}
	/**
	 * @param fontColor the fontColor to set
	 */
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	/**
	 * @return the fontName
	 */
	public String getFontName() {
		return fontName;
	}
	/**
	 * @param fontName the fontName to set
	 */
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	/**
	 * @return the fontSize
	 */
	public String getFontSize() {
		return fontSize;
	}
	/**
	 * @param fontSize the fontSize to set
	 */
	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}
	/**
	 * @return the openChatDialogOnReceived
	 */
	public int getOpenChatDialogOnReceived() {
		return openChatDialogOnReceived;
	}
	/**
	 * @param openChatDialogOnReceived the openChatDialogOnReceived to set
	 */
	public void setOpenChatDialogOnReceived(int openChatDialogOnReceived) {
		this.openChatDialogOnReceived = openChatDialogOnReceived;
	}
	/**
	 * @return the playSoundOnReceived
	 */
	public int getPlaySoundOnReceived() {
		return playSoundOnReceived;
	}
	/**
	 * @param playSoundOnReceived the playSoundOnReceived to set
	 */
	public void setPlaySoundOnReceived(int playSoundOnReceived) {
		this.playSoundOnReceived = playSoundOnReceived;
	}
	/**
	 * @return the setFocusOnReceived
	 */
	public int getSetFocusOnReceived() {
		return setFocusOnReceived;
	}
	/**
	 * @param setFocusOnReceived the setFocusOnReceived to set
	 */
	public void setSetFocusOnReceived(int setFocusOnReceived) {
		this.setFocusOnReceived = setFocusOnReceived;
	}
	/**
	 * @return the status
	 */
	public byte getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(byte status) {
		this.status = status;
	}
}