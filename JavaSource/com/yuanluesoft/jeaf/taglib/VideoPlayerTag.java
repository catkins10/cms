package com.yuanluesoft.jeaf.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;

import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.RequestUtils;
import com.yuanluesoft.jeaf.util.UUIDLongGenerator;

/**
 * 视频播放器
 * @author linchuan
 *
 */
public class VideoPlayerTag extends BodyTagSupport {
	private int width; //宽度
	private int height; //高度
	private String videoUrl; //视频URL
	private String previewImageUrl; //预览图URL
	private boolean autoStart; //是否自动开始播放
	private boolean hideControlBar; //是否隐藏控制条
	
	//动态视频URL
	private String nameVideoUrl;
	private String propertyVideoUrl;
	private String scopeVideoUrl;
	
	//动态预览图URL
	private String namePreviewImageUrl;
	private String propertyPreviewImageUrl;
	private String scopePreviewImageUrl;
		
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		String videoUrl = this.videoUrl;
		if(videoUrl==null) {
			if(nameVideoUrl==null) {
				nameVideoUrl = Constants.BEAN_KEY;
			}
			videoUrl = (String)TagUtils.getInstance().lookup(pageContext, nameVideoUrl, propertyVideoUrl, scopeVideoUrl);
		}
		if(previewImageUrl==null && (propertyPreviewImageUrl!=null || namePreviewImageUrl!=null)) {
			if(namePreviewImageUrl==null) {
				namePreviewImageUrl = Constants.BEAN_KEY;
			}
			previewImageUrl = (String)TagUtils.getInstance().lookup(pageContext, namePreviewImageUrl, propertyPreviewImageUrl, scopePreviewImageUrl);
		}
		long id = UUIDLongGenerator.generateId();
		String html = "";
		if(pageContext.getRequest().getAttribute("VideoPlayerSupport")==null) {
			html = "<script type=\"text/javascript\" src=\"" + Environment.getContextPath() + "/jeaf/video/player/js/videoplayer.js\"></script>\r\n" +
				   "<script type=\"text/javascript\" src=\"" + Environment.getContextPath() + "/jeaf/common/js/progressbar.js\"></script>\r\n" +
				   "<link href=\"" + Environment.getContextPath() + "/jeaf/video/player/css/videocontroller.css\" rel=\"stylesheet\" type=\"text/css\"/>\r\n";
			pageContext.getRequest().setAttribute("VideoPlayerSupport", Boolean.TRUE);
		}
		html = "<span id=\"" + id + "\" style=\"display:inline-block;\"></span>\r\n" +
			   "<script>new VideoPlayer(document.getElementById('" + id + "'), '" + videoUrl + "', '" + previewImageUrl + "', null, " + width + ", " + height + ", " + autoStart + ", " + hideControlBar + ", null, '" + RequestUtils.getRequestInfo((HttpServletRequest)pageContext.getRequest()).getTerminalType() + "')</script>";
		TagUtils.getInstance().write(this.pageContext, html);
		return (EVAL_BODY_AGAIN );
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		super.release();
		width = 0; //宽度
		height = 0; //高度
		videoUrl = null; //视频URL
		previewImageUrl = null; //预览图URL
		autoStart = false; //是否自动开始播放
		hideControlBar = false; //是否隐藏控制条
		
		//动态视频URL
		nameVideoUrl = null;
		propertyVideoUrl = null;
		scopeVideoUrl = null;
		
		//动态预览图URL
		namePreviewImageUrl = null;
		propertyPreviewImageUrl = null;
		scopePreviewImageUrl = null;
	}
	
	/**
	 * @return the autoStart
	 */
	public boolean isAutoStart() {
		return autoStart;
	}
	/**
	 * @param autoStart the autoStart to set
	 */
	public void setAutoStart(boolean autoStart) {
		this.autoStart = autoStart;
	}
	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	/**
	 * @return the hideControlBar
	 */
	public boolean isHideControlBar() {
		return hideControlBar;
	}
	/**
	 * @param hideControlBar the hideControlBar to set
	 */
	public void setHideControlBar(boolean hideControlBar) {
		this.hideControlBar = hideControlBar;
	}
	/**
	 * @return the previewImageUrl
	 */
	public String getPreviewImageUrl() {
		return previewImageUrl;
	}
	/**
	 * @param previewImageUrl the previewImageUrl to set
	 */
	public void setPreviewImageUrl(String previewImageUrl) {
		this.previewImageUrl = previewImageUrl;
	}
	/**
	 * @return the videoUrl
	 */
	public String getVideoUrl() {
		return videoUrl;
	}
	/**
	 * @param videoUrl the videoUrl to set
	 */
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the namePreviewImageUrl
	 */
	public String getNamePreviewImageUrl() {
		return namePreviewImageUrl;
	}

	/**
	 * @param namePreviewImageUrl the namePreviewImageUrl to set
	 */
	public void setNamePreviewImageUrl(String namePreviewImageUrl) {
		this.namePreviewImageUrl = namePreviewImageUrl;
	}

	/**
	 * @return the nameVideoUrl
	 */
	public String getNameVideoUrl() {
		return nameVideoUrl;
	}

	/**
	 * @param nameVideoUrl the nameVideoUrl to set
	 */
	public void setNameVideoUrl(String nameVideoUrl) {
		this.nameVideoUrl = nameVideoUrl;
	}

	/**
	 * @return the propertyPreviewImageUrl
	 */
	public String getPropertyPreviewImageUrl() {
		return propertyPreviewImageUrl;
	}

	/**
	 * @param propertyPreviewImageUrl the propertyPreviewImageUrl to set
	 */
	public void setPropertyPreviewImageUrl(String propertyPreviewImageUrl) {
		this.propertyPreviewImageUrl = propertyPreviewImageUrl;
	}

	/**
	 * @return the propertyVideoUrl
	 */
	public String getPropertyVideoUrl() {
		return propertyVideoUrl;
	}

	/**
	 * @param propertyVideoUrl the propertyVideoUrl to set
	 */
	public void setPropertyVideoUrl(String propertyVideoUrl) {
		this.propertyVideoUrl = propertyVideoUrl;
	}

	/**
	 * @return the scopePreviewImageUrl
	 */
	public String getScopePreviewImageUrl() {
		return scopePreviewImageUrl;
	}

	/**
	 * @param scopePreviewImageUrl the scopePreviewImageUrl to set
	 */
	public void setScopePreviewImageUrl(String scopePreviewImageUrl) {
		this.scopePreviewImageUrl = scopePreviewImageUrl;
	}

	/**
	 * @return the scopeVideoUrl
	 */
	public String getScopeVideoUrl() {
		return scopeVideoUrl;
	}

	/**
	 * @param scopeVideoUrl the scopeVideoUrl to set
	 */
	public void setScopeVideoUrl(String scopeVideoUrl) {
		this.scopeVideoUrl = scopeVideoUrl;
	}
}
