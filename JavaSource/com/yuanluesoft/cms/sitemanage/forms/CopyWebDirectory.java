package com.yuanluesoft.cms.sitemanage.forms;

import com.yuanluesoft.jeaf.directorymanage.forms.CopyDirectory;

/**
 * 站点目录拷贝
 * @author linchuan
 *
 */
public class CopyWebDirectory extends CopyDirectory {
	private char copyTemplate = '1'; //是否需要拷贝模板

	/**
	 * @return the copyTemplate
	 */
	public char getCopyTemplate() {
		return copyTemplate;
	}

	/**
	 * @param copyTemplate the copyTemplate to set
	 */
	public void setCopyTemplate(char copyTemplate) {
		this.copyTemplate = copyTemplate;
	}
}