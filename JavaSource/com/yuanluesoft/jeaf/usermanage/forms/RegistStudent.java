package com.yuanluesoft.jeaf.usermanage.forms;


/**
 * 
 * @author linchuan
 *
 */
public class RegistStudent extends RegistPerson {
	private int seatNumber;
    private String genearchName;
    private String genearchTitle;
    private String genearchMobile;
    private String genearchMail;
        
	/**
	 * @return the genearchMail
	 */
	public String getGenearchMail() {
		return genearchMail;
	}
	/**
	 * @param genearchMail the genearchMail to set
	 */
	public void setGenearchMail(String genearchMail) {
		this.genearchMail = genearchMail;
	}
	/**
	 * @return the genearchMobile
	 */
	public String getGenearchMobile() {
		return genearchMobile;
	}
	/**
	 * @param genearchMobile the genearchMobile to set
	 */
	public void setGenearchMobile(String genearchMobile) {
		this.genearchMobile = genearchMobile;
	}
	/**
	 * @return the genearchName
	 */
	public String getGenearchName() {
		return genearchName;
	}
	/**
	 * @param genearchName the genearchName to set
	 */
	public void setGenearchName(String genearchName) {
		this.genearchName = genearchName;
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
}
