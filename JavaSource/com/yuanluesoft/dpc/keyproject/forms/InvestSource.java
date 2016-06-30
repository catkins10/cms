package com.yuanluesoft.dpc.keyproject.forms;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectInvestSource;

/**
 * 资金来源配置
 * @author linchuan
 *
 */
public class InvestSource extends Parameter {
	private KeyProjectInvestSource investSource = new KeyProjectInvestSource();

	/**
	 * @return the investSource
	 */
	public KeyProjectInvestSource getInvestSource() {
		return investSource;
	}

	/**
	 * @param investSource the investSource to set
	 */
	public void setInvestSource(KeyProjectInvestSource investSource) {
		this.investSource = investSource;
	}
}