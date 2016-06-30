package com.yuanluesoft.dpc.keyproject.forms;

import com.yuanluesoft.dpc.keyproject.pojo.KeyProjectPhoto;

/**
 * 
 * @author linchuan
 *
 */
public class Photo extends Project {
	private KeyProjectPhoto photo = new KeyProjectPhoto();

	/**
	 * @return the photo
	 */
	public KeyProjectPhoto getPhoto() {
		return photo;
	}

	/**
	 * @param photo the photo to set
	 */
	public void setPhoto(KeyProjectPhoto photo) {
		this.photo = photo;
	}
}