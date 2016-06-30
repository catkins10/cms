package com.yuanluesoft.dpc.keyproject.forms;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectDevelopmentAreaCategory;

/**
 * 
 * @author linchuan
 *
 */
public class DevelopmentAreaCategory extends Parameter {
	private KeyProjectDevelopmentAreaCategory developmentAreaCategory = new KeyProjectDevelopmentAreaCategory();

	/**
	 * @return the developmentAreaCategory
	 */
	public KeyProjectDevelopmentAreaCategory getDevelopmentAreaCategory() {
		return developmentAreaCategory;
	}

	/**
	 * @param developmentAreaCategory the developmentAreaCategory to set
	 */
	public void setDevelopmentAreaCategory(
			KeyProjectDevelopmentAreaCategory developmentAreaCategory) {
		this.developmentAreaCategory = developmentAreaCategory;
	}
}