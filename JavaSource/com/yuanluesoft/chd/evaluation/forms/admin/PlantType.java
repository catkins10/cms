package com.yuanluesoft.chd.evaluation.forms.admin;

import java.util.Set;

/**
 * 
 * @author linchuan
 *
 */
public class PlantType extends DirectoryForm {
	private Set indicators; //主要指标
	private Set prerequisites; //必备条件
	private Set generators; //机组综合数据表模板
	
	//扩展属性
	private String generatorTemplate; //机组资料模板
	
	/**
	 * @return the generators
	 */
	public Set getGenerators() {
		return generators;
	}
	/**
	 * @param generators the generators to set
	 */
	public void setGenerators(Set generators) {
		this.generators = generators;
	}
	/**
	 * @return the indicators
	 */
	public Set getIndicators() {
		return indicators;
	}
	/**
	 * @param indicators the indicators to set
	 */
	public void setIndicators(Set indicators) {
		this.indicators = indicators;
	}
	/**
	 * @return the prerequisites
	 */
	public Set getPrerequisites() {
		return prerequisites;
	}
	/**
	 * @param prerequisites the prerequisites to set
	 */
	public void setPrerequisites(Set prerequisites) {
		this.prerequisites = prerequisites;
	}
	/**
	 * @return the generatorTemplate
	 */
	public String getGeneratorTemplate() {
		return generatorTemplate;
	}
	/**
	 * @param generatorTemplate the generatorTemplate to set
	 */
	public void setGeneratorTemplate(String generatorTemplate) {
		this.generatorTemplate = generatorTemplate;
	}
}