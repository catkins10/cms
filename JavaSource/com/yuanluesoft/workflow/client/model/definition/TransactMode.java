/*
 * Created on 2005-1-18
 *
 */
package com.yuanluesoft.workflow.client.model.definition;

import com.yuanluesoft.jeaf.base.model.CloneableObject;

/**
 * 
 * @author linchuan
 *
 */
public class TransactMode extends CloneableObject {
	public final static String TRANSACT_MODE_SINGLE = "single";
	public final static String TRANSACT_MODE_SEQUENCE = "sequence";
	public final static String TRANSACT_MODE_PARALLEL = "parallel";
	public final static String TRANSACT_MODE_VOTE = "vote";
		
	private String name; //办理方式名称
	private int voteNumber = 1; //办理方式为投票时,投票的有效票数
	
	public TransactMode() {
		
	}
	
	public TransactMode(String name) {
		this.name = name;
	}
	
	public String getTitle() {
		if(name.equals(TransactMode.TRANSACT_MODE_SINGLE)) {
			return "单一办理人";
		}
		else if(name.equals(TransactMode.TRANSACT_MODE_SEQUENCE)) {
			return "多人顺序办理";
		}
		else if(name.equals(TransactMode.TRANSACT_MODE_PARALLEL)) {
			return "多人并行办理";
		}
		else if(name.equals(TransactMode.TRANSACT_MODE_VOTE)) {
			if(voteNumber==-1) {
				return "表决方式";
			}
			else {
				return "多人并行," + voteNumber + "人办理完毕即可";
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return name.equals(((TransactMode)obj).getName());
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
	 * @return Returns the voteNumber.
	 */
	public int getVoteNumber() {
		return voteNumber;
	}
	/**
	 * @param voteNumber The voteNumber to set.
	 */
	public void setVoteNumber(int voteNumber) {
		this.voteNumber = (voteNumber<1 ? 1:voteNumber);
	}
}
