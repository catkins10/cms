package com.yuanluesoft.bidding.enterprise.pojo;

import com.yuanluesoft.jeaf.database.Record;

/**
 * 企业:代理名录库配置(bidding_agent_lib)
 * @author linchuan
 *
 */
public class BiddingAgentLib extends Record {
	private String lib; //代理名录库
	
	/**
	 * @return the lib
	 */
	public String getLib() {
		return lib;
	}
	/**
	 * @param lib the lib to set
	 */
	public void setLib(String lib) {
		this.lib = lib;
	}
}
