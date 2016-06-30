/**
 * 
 */
package com.yuanluesoft.lss.cardquery.forms.admin;

import java.sql.Timestamp;

import com.yuanluesoft.jeaf.form.ActionForm;

/**
 * @author kangshiwei
 *制卡查询参数配置
 */
public class CardQueryConfig extends ActionForm {
	private String cardType; //制卡类型
	private String receiveMark; //接收数据备注
	private String removedCardMark; //移出卡片备注
	private String makeCardMark; //制卡备注
	private Timestamp created; //创建时间
	
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
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
