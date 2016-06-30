package com.yuanluesoft.cms.scene.pojo;

import com.yuanluesoft.cms.templatemanage.pojo.Template;

/**
 * 场景服务:模板配置(scene_template)
 * @author linchuan
 *
 */
public class SceneTemplate extends Template {
	private long directoryId; //模板作用范围ID,不指定时，则为通用模板，当场景没有配置自己的模板时使用
	private String directoryName; //模板作用范围,可以是站点、场景服务、场景目录
	
	/**
	 * @return the directoryId
	 */
	public long getDirectoryId() {
		return directoryId;
	}
	/**
	 * @param directoryId the directoryId to set
	 */
	public void setDirectoryId(long directoryId) {
		this.directoryId = directoryId;
	}
	/**
	 * @return the directoryName
	 */
	public String getDirectoryName() {
		return directoryName;
	}
	/**
	 * @param directoryName the directoryName to set
	 */
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
}