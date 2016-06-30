package com.yuanluesoft.cms.onlineservice.forms.admin;

import com.yuanluesoft.cms.onlineservice.pojo.OnlineServiceItemMaterial;

/**
 * 
 * @author linchuan
 *
 */
public class ServiceItemMaterial extends ServiceItem {
	private OnlineServiceItemMaterial material = new OnlineServiceItemMaterial();

	/**
	 * @return the material
	 */
	public OnlineServiceItemMaterial getMaterial() {
		return material;
	}

	/**
	 * @param material the material to set
	 */
	public void setMaterial(OnlineServiceItemMaterial material) {
		this.material = material;
	}
}