package com.yuanluesoft.jeaf.taglib;

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

import com.yuanluesoft.jeaf.image.model.Image;

/**
 * 
 * @author linchuan
 *
 */
public class ImgTag extends org.apache.struts.taglib.html.ImgTag {
	protected Image imageModel = null; //图片模型
	
	//动态图片模型
	private String nameImageModel = null;
	private String propertyImageModel = null;
	private String scopeImageModel = null;
	
	private boolean autoWidth = false; //当图片宽度小于设定宽度时，按图片宽度显示, 超出时按设定宽度显示
	private boolean autoHeight = false; //当图片高度小于设定高度时，按图片高度显示, 超出时按设定高度显示
	
	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.html.ImgTag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		if(imageModel==null) {
			if(nameImageModel!=null || propertyImageModel!=null) {
				if(nameImageModel==null) {
					nameImageModel = Constants.BEAN_KEY;
				}
				imageModel = (Image)TagUtils.getInstance().lookup(pageContext, nameImageModel, propertyImageModel, scopeImageModel);
			}
		}
		if(imageModel!=null) {
			if(this.src==null) {
				this.src = imageModel.getUrl();
			}
			int imgWidth = this.width==null ? -1 : Integer.parseInt(this.width);
			int imgHeight = this.height==null ? -1 : Integer.parseInt(this.height);
			int imageModelWidth = imageModel.getWidth();
			int imageModelHeight = imageModel.getHeight();
			
			if(autoWidth && imageModelWidth<imgWidth) { //当图片宽度小于设定宽度时，按图片宽度显示, 超出时按设定宽度显示
				this.width = imageModelWidth + "";
			}
			if(autoHeight && imageModelHeight<imgHeight) { //当图片高度小于设定高度时，按图片高度显示, 超出时按设定高度显示
				this.height = imageModelHeight + "";
			}
			if(autoWidth && autoHeight && (imageModelWidth>imgWidth || imageModelHeight>imgHeight)) { //同时自动调整宽度和高度
				float scale = Math.min((float)imgWidth/(float)imageModelWidth, (float)imgHeight/(float)imageModelHeight);
				this.width = (int)(imageModelWidth * scale) + "";
				this.height = (int)(imageModelHeight * scale) + "";
			}
		}
		else if(src==null || srcKey==null) {
			return SKIP_BODY;
		}
		return super.doStartTag();
	}
	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.html.ImgTag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		if(src==null) {
			return SKIP_BODY;
		}
		int ret = super.doEndTag();
		release();
		return ret;
	}
	/* (non-Javadoc)
	 * @see org.apache.struts.taglib.html.ImgTag#release()
	 */
	public void release() {
		imageModel = null; //图片模型
		nameImageModel = null; //动态图片模型
		propertyImageModel = null;
		scopeImageModel = null;
		autoWidth = false;
		autoHeight = false;
		super.release();
	}
	/**
	 * @return the autoHeight
	 */
	public boolean isAutoHeight() {
		return autoHeight;
	}
	/**
	 * @param autoHeight the autoHeight to set
	 */
	public void setAutoHeight(boolean autoHeight) {
		this.autoHeight = autoHeight;
	}
	/**
	 * @return the autoWidth
	 */
	public boolean isAutoWidth() {
		return autoWidth;
	}
	/**
	 * @param autoWidth the autoWidth to set
	 */
	public void setAutoWidth(boolean autoWidth) {
		this.autoWidth = autoWidth;
	}
	/**
	 * @return the imageModelName
	 */
	public String getNameImageModel() {
		return nameImageModel;
	}
	/**
	 * @param imageModelName the imageModelName to set
	 */
	public void setNameImageModel(String imageModelName) {
		this.nameImageModel = imageModelName;
	}
	/**
	 * @return the imageModelProperty
	 */
	public String getPropertyImageModel() {
		return propertyImageModel;
	}
	/**
	 * @param imageModelProperty the imageModelProperty to set
	 */
	public void setPropertyImageModel(String imageModelProperty) {
		this.propertyImageModel = imageModelProperty;
	}
	/**
	 * @return the imageModelScope
	 */
	public String getScopeImageModel() {
		return scopeImageModel;
	}
	/**
	 * @param imageModelScope the imageModelScope to set
	 */
	public void setScopeImageModel(String imageModelScope) {
		this.scopeImageModel = imageModelScope;
	}
}
