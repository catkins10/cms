/**
 * 
 */
package com.yuanluesoft.lss.cardquery.page.model;


import com.yuanluesoft.lss.cardquery.pojo.CardQuery;

/**
 * @author kangshiwei
 *
 */
public class CardQueryModel extends CardQuery{
	private String receiveMark; //接收数据备注
	private String removedCardMark; //移出卡片备注
	private String makeCardMark; //制卡备注
	private String sexFront;//性别（前台）
	
	
	
	public String getSexFront() {
		return sexFront;
	}
	public void setSexFront(String sexFront) {
		this.sexFront = sexFront;
	}
	public String getMakeCardMark() {
		return makeCardMark;
	}
	public void setMakeCardMark(String makeCardMark) {
		this.makeCardMark = makeCardMark;
	}
	
	public String getReceiveMark() {
		return receiveMark;
	}
	public void setReceiveMark(String receiveMark) {
		this.receiveMark = receiveMark;
	}
	
	public String getRemovedCardMark() {
		return removedCardMark;
	}
	public void setRemovedCardMark(String removedCardMark) {
		this.removedCardMark = removedCardMark;
	}
	
	
	
}
