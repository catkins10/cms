package com.yuanluesoft.cms.pagebuilder.model.recordlist;

/**
 * 
 * @author yuanluesoft
 *
 */
public class RecordList {
	private String recordListName; //名称
	private String applicationName; //应用名称
	private boolean privateRecordList; //是否页面私有的记录列表
	private String recordClassName; //记录类名称,privateRecordList=true时有效
	private int recordCount; //显示记录数
	private String recordFormat; //记录显示格式
	private String separatorMode;  //记录分隔方式,空格/换行/图片/自定义
	private String separatorImage; //记录分隔图片
	private boolean separatorImageOfLastRecord = true; //最后一行是否显示图片
	private String separatorCustom; //记录分隔
	private boolean separatorOfLastRecord; //最后一个记录是否也添加分隔符
	private String separatorHeight; //记录分隔高度
	private String areaWidth; //区域宽度
	private String areaHeight; //区域高度
	private String recordWidth; //记录宽度
	private String recordHeight; //记录高度
	private String recordIndent; //记录缩进
	private String scrollMode; //滚动方式
	private String switchMode; //滚动切换方式
	private boolean manualSwitch; //是否手动切换
	private boolean autoScaling; //是否大小自适应
	private String controlBarPosition; //显示位置
	private int controlBarXMargin; //水平边距
	private int controlBarYMargin; //垂直边距
	private String scrollDirection; //滚动方向,horizontal/横向,vertical/纵向
	private int controlBarRecordSpacing; //控制栏记录分隔距离
	private String mouseOverControlBarRecord; //鼠标经过控制记录时的动作
	private String clickControlBarRecord; //点击控制记录时的动作
	private String controlBarFormat; //控制栏格式
	private String imageClickAction; //点击图片操作,none/不切换图片,nextImage/切换到下一图片,leftPreviousImageAndRightNextImage/左侧切换到上一图片、右侧切换到下一图片
	private boolean switchByKey; //左右方向键翻阅
	private String displayNextPreviousButton; //是否显示前一页、后一页按钮,none/不显示,always/一直显示,mouseOver/鼠标经过时显示
	private String nextPreviousButtonFormat; //前一页、后一页按钮格式
	private boolean scrollJoin; //滚动时是否首尾衔接
	private int scrollSpeed = 10; //滚动速度
	private int scrollAmount = 1; //滚动距离
	private String linkTitle; //链接类型
	private String linkOpenMode; //链接打开方式
	private boolean simpleMode; //精简方式显示记录
	private boolean tableMode; //表格方式显示记录
	private boolean searchResults; //是否搜索结果
	private String extendProperties; //扩展属性
	private String emptyPrompt; //记录列表为空时的提示
	
	/**
	 * @return the recordCount
	 */
	public int getRecordCount() {
		return recordCount;
	}
	/**
	 * @param recordCount the recordCount to set
	 */
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	/**
	 * @return the recordFormat
	 */
	public String getRecordFormat() {
		return recordFormat;
	}
	/**
	 * @param recordFormat the recordFormat to set
	 */
	public void setRecordFormat(String recordFormat) {
		this.recordFormat = recordFormat;
	}
	/**
	 * @return the separatorHeight
	 */
	public String getSeparatorHeight() {
		return separatorHeight;
	}
	/**
	 * @param separatorHeight the separatorHeight to set
	 */
	public void setSeparatorHeight(String separatorHeight) {
		this.separatorHeight = separatorHeight;
	}
	/**
	 * @return the separatorImage
	 */
	public String getSeparatorImage() {
		return separatorImage;
	}
	/**
	 * @param separatorImage the separatorImage to set
	 */
	public void setSeparatorImage(String separatorImage) {
		this.separatorImage = separatorImage;
	}
	/**
	 * @return the separatorMode
	 */
	public String getSeparatorMode() {
		return separatorMode;
	}
	/**
	 * @param separatorMode the separatorMode to set
	 */
	public void setSeparatorMode(String separatorMode) {
		this.separatorMode = separatorMode;
	}
	/**
	 * @return the height
	 */
	public String getAreaHeight() {
		return areaHeight;
	}
	/**
	 * @param height the height to set
	 */
	public void setAreaHeight(String height) {
		this.areaHeight = height;
	}
	/**
	 * @return the recordHeight
	 */
	public String getRecordHeight() {
		return recordHeight;
	}
	/**
	 * @param recordHeight the recordHeight to set
	 */
	public void setRecordHeight(String recordHeight) {
		this.recordHeight = recordHeight;
	}
	/**
	 * @return the recordWidth
	 */
	public String getRecordWidth() {
		return recordWidth;
	}
	/**
	 * @param recordWidth the recordWidth to set
	 */
	public void setRecordWidth(String recordWidth) {
		this.recordWidth = recordWidth;
	}
	/**
	 * @return the scrollJoin
	 */
	public boolean isScrollJoin() {
		return scrollJoin;
	}
	/**
	 * @param scrollJoin the scrollJoin to set
	 */
	public void setScrollJoin(boolean scrollJoin) {
		this.scrollJoin = scrollJoin;
	}
	/**
	 * @return the scrollMode
	 */
	public String getScrollMode() {
		return scrollMode;
	}
	/**
	 * @param scrollMode the scrollMode to set
	 */
	public void setScrollMode(String scrollMode) {
		this.scrollMode = scrollMode;
	}
	/**
	 * @return the scrollSpeed
	 */
	public int getScrollSpeed() {
		return scrollSpeed;
	}
	/**
	 * @param scrollSpeed the scrollSpeed to set
	 */
	public void setScrollSpeed(int scrollSpeed) {
		this.scrollSpeed = scrollSpeed;
	}
	/**
	 * @return the width
	 */
	public String getAreaWidth() {
		return areaWidth;
	}
	/**
	 * @param width the width to set
	 */
	public void setAreaWidth(String width) {
		this.areaWidth = width;
	}
	/**
	 * @return the recordIndent
	 */
	public String getRecordIndent() {
		return recordIndent;
	}
	/**
	 * @param recordIndent the recordIndent to set
	 */
	public void setRecordIndent(String recordIndent) {
		this.recordIndent = recordIndent;
	}
	/**
	 * @return the tableMode
	 */
	public boolean isTableMode() {
		return tableMode;
	}
	/**
	 * @param tableMode the tableMode to set
	 */
	public void setTableMode(boolean tableMode) {
		this.tableMode = tableMode;
	}
	/**
	 * @return the searchResult
	 */
	public boolean isSearchResults() {
		return searchResults;
	}
	/**
	 * @param searchResult the searchResult to set
	 */
	public void setSearchResults(boolean searchResult) {
		this.searchResults = searchResult;
	}
	/**
	 * @return the applicatonName
	 */
	public String getApplicationName() {
		return applicationName;
	}
	/**
	 * @param applicatonName the applicatonName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	/**
	 * @return the scrollAmount
	 */
	public int getScrollAmount() {
		return scrollAmount;
	}
	/**
	 * @param scrollAmount the scrollAmount to set
	 */
	public void setScrollAmount(int scrollAmount) {
		this.scrollAmount = scrollAmount;
	}
	/**
	 * @return the linkOpenMode
	 */
	public String getLinkOpenMode() {
		return linkOpenMode;
	}
	/**
	 * @param linkOpenMode the linkOpenMode to set
	 */
	public void setLinkOpenMode(String linkOpenMode) {
		this.linkOpenMode = linkOpenMode;
	}
	/**
	 * @return the name
	 */
	public String getRecordListName() {
		return recordListName;
	}
	/**
	 * @param name the name to set
	 */
	public void setRecordListName(String name) {
		this.recordListName = name;
	}
	/**
	 * @return the privateRecordList
	 */
	public boolean isPrivateRecordList() {
		return privateRecordList;
	}
	/**
	 * @param privateRecordList the privateRecordList to set
	 */
	public void setPrivateRecordList(boolean privateRecordList) {
		this.privateRecordList = privateRecordList;
	}
	/**
	 * @return the extendProperties
	 */
	public String getExtendProperties() {
		return extendProperties;
	}
	/**
	 * @param extendProperties the extendProperties to set
	 */
	public void setExtendProperties(String extendProperties) {
		this.extendProperties = extendProperties;
	}
	/**
	 * @return the linkTitle
	 */
	public String getLinkTitle() {
		return linkTitle;
	}
	/**
	 * @param linkTitle the linkTitle to set
	 */
	public void setLinkTitle(String linkTitle) {
		this.linkTitle = linkTitle;
	}
	/**
	 * @return the emptyPrompt
	 */
	public String getEmptyPrompt() {
		return emptyPrompt;
	}
	/**
	 * @param emptyPrompt the emptyPrompt to set
	 */
	public void setEmptyPrompt(String emptyPrompt) {
		this.emptyPrompt = emptyPrompt;
	}
	/**
	 * @return the recordClassName
	 */
	public String getRecordClassName() {
		return recordClassName;
	}
	/**
	 * @param recordClassName the recordClassName to set
	 */
	public void setRecordClassName(String recordClassName) {
		this.recordClassName = recordClassName;
	}
	/**
	 * @return the separatorCustom
	 */
	public String getSeparatorCustom() {
		return separatorCustom;
	}
	/**
	 * @param separatorCustom the separatorCustom to set
	 */
	public void setSeparatorCustom(String separatorCustom) {
		this.separatorCustom = separatorCustom;
	}
	/**
	 * @return the separatorImageOfLastRecord
	 */
	public boolean isSeparatorImageOfLastRecord() {
		return separatorImageOfLastRecord;
	}
	/**
	 * @param separatorImageOfLastRecord the separatorImageOfLastRecord to set
	 */
	public void setSeparatorImageOfLastRecord(boolean separatorImageOfLastRecord) {
		this.separatorImageOfLastRecord = separatorImageOfLastRecord;
	}
	/**
	 * @return the separatorOfLastRecord
	 */
	public boolean isSeparatorOfLastRecord() {
		return separatorOfLastRecord;
	}
	/**
	 * @param separatorOfLastRecord the separatorOfLastRecord to set
	 */
	public void setSeparatorOfLastRecord(boolean separatorOfLastRecord) {
		this.separatorOfLastRecord = separatorOfLastRecord;
	}
	/**
	 * @return the simpleMode
	 */
	public boolean isSimpleMode() {
		return simpleMode;
	}
	/**
	 * @param simpleMode the simpleMode to set
	 */
	public void setSimpleMode(boolean simpleMode) {
		this.simpleMode = simpleMode;
	}
	/**
	 * @return the switchMode
	 */
	public String getSwitchMode() {
		return switchMode;
	}
	/**
	 * @param switchMode the switchMode to set
	 */
	public void setSwitchMode(String switchMode) {
		this.switchMode = switchMode;
	}
	/**
	 * @return the manualSwitch
	 */
	public boolean isManualSwitch() {
		return manualSwitch;
	}
	/**
	 * @param manualSwitch the manualSwitch to set
	 */
	public void setManualSwitch(boolean manualSwitch) {
		this.manualSwitch = manualSwitch;
	}
	/**
	 * @return the autoScaling
	 */
	public boolean isAutoScaling() {
		return autoScaling;
	}
	/**
	 * @param autoScaling the autoScaling to set
	 */
	public void setAutoScaling(boolean autoScaling) {
		this.autoScaling = autoScaling;
	}
	/**
	 * @return the controlBarFormat
	 */
	public String getControlBarFormat() {
		return controlBarFormat;
	}
	/**
	 * @param controlBarFormat the controlBarFormat to set
	 */
	public void setControlBarFormat(String controlBarFormat) {
		this.controlBarFormat = controlBarFormat;
	}
	/**
	 * @return the controlBarPosition
	 */
	public String getControlBarPosition() {
		return controlBarPosition;
	}
	/**
	 * @param controlBarPosition the controlBarPosition to set
	 */
	public void setControlBarPosition(String controlBarPosition) {
		this.controlBarPosition = controlBarPosition;
	}
	/**
	 * @return the controlBarXMargin
	 */
	public int getControlBarXMargin() {
		return controlBarXMargin;
	}
	/**
	 * @param controlBarXMargin the controlBarXMargin to set
	 */
	public void setControlBarXMargin(int controlBarXMargin) {
		this.controlBarXMargin = controlBarXMargin;
	}
	/**
	 * @return the controlBarYMargin
	 */
	public int getControlBarYMargin() {
		return controlBarYMargin;
	}
	/**
	 * @param controlBarYMargin the controlBarYMargin to set
	 */
	public void setControlBarYMargin(int controlBarYMargin) {
		this.controlBarYMargin = controlBarYMargin;
	}
	/**
	 * @return the imageClickAction
	 */
	public String getImageClickAction() {
		return imageClickAction;
	}
	/**
	 * @param imageClickAction the imageClickAction to set
	 */
	public void setImageClickAction(String imageClickAction) {
		this.imageClickAction = imageClickAction;
	}
	/**
	 * @return the nextPreviousButtonFormat
	 */
	public String getNextPreviousButtonFormat() {
		return nextPreviousButtonFormat;
	}
	/**
	 * @param nextPreviousButtonFormat the nextPreviousButtonFormat to set
	 */
	public void setNextPreviousButtonFormat(String nextPreviousButtonFormat) {
		this.nextPreviousButtonFormat = nextPreviousButtonFormat;
	}
	/**
	 * @return the scrollDirection
	 */
	public String getScrollDirection() {
		return scrollDirection;
	}
	/**
	 * @param scrollDirection the scrollDirection to set
	 */
	public void setScrollDirection(String scrollDirection) {
		this.scrollDirection = scrollDirection;
	}
	/**
	 * @return the controlBarRecordSpacing
	 */
	public int getControlBarRecordSpacing() {
		return controlBarRecordSpacing;
	}
	/**
	 * @param controlBarRecordSpacing the controlBarRecordSpacing to set
	 */
	public void setControlBarRecordSpacing(int controlBarRecordSpacing) {
		this.controlBarRecordSpacing = controlBarRecordSpacing;
	}
	/**
	 * @return the clickControlBarRecord
	 */
	public String getClickControlBarRecord() {
		return clickControlBarRecord;
	}
	/**
	 * @param clickControlBarRecord the clickControlBarRecord to set
	 */
	public void setClickControlBarRecord(String clickControlBarRecord) {
		this.clickControlBarRecord = clickControlBarRecord;
	}
	/**
	 * @return the mouseOverControlBarRecord
	 */
	public String getMouseOverControlBarRecord() {
		return mouseOverControlBarRecord;
	}
	/**
	 * @param mouseOverControlBarRecord the mouseOverControlBarRecord to set
	 */
	public void setMouseOverControlBarRecord(String mouseOverControlBarRecord) {
		this.mouseOverControlBarRecord = mouseOverControlBarRecord;
	}
	/**
	 * @return the displayNextPreviousButton
	 */
	public String getDisplayNextPreviousButton() {
		return displayNextPreviousButton;
	}
	/**
	 * @param displayNextPreviousButton the displayNextPreviousButton to set
	 */
	public void setDisplayNextPreviousButton(String displayNextPreviousButton) {
		this.displayNextPreviousButton = displayNextPreviousButton;
	}
	/**
	 * @return the switchByKey
	 */
	public boolean isSwitchByKey() {
		return switchByKey;
	}
	/**
	 * @param switchByKey the switchByKey to set
	 */
	public void setSwitchByKey(boolean switchByKey) {
		this.switchByKey = switchByKey;
	}
}