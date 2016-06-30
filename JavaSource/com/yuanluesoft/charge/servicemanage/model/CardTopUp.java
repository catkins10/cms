package com.yuanluesoft.charge.servicemanage.model;

import java.io.Serializable;

/**
 * 
 * @author linchuan
 *
 */
public class CardTopUp implements Serializable, Cloneable {
	private String cardNumber;
	private double cardMoney;
	private String useBegin;
	private String useEnd;
	
	/**
	 * @return the cardMoney
	 */
	public double getCardMoney() {
		return cardMoney;
	}
	/**
	 * @param cardMoney the cardMoney to set
	 */
	public void setCardMoney(double cardMoney) {
		this.cardMoney = cardMoney;
	}
	/**
	 * @return the cardNumber
	 */
	public String getCardNumber() {
		return cardNumber;
	}
	/**
	 * @param cardNumber the cardNumber to set
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	/**
	 * @return the useBegin
	 */
	public String getUseBegin() {
		return useBegin;
	}
	/**
	 * @param useBegin the useBegin to set
	 */
	public void setUseBegin(String useBegin) {
		this.useBegin = useBegin;
	}
	/**
	 * @return the useEnd
	 */
	public String getUseEnd() {
		return useEnd;
	}
	/**
	 * @param useEnd the useEnd to set
	 */
	public void setUseEnd(String useEnd) {
		this.useEnd = useEnd;
	}
}
