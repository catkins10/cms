package com.yuanluesoft.cms.pagebuilder.util;

import org.apache.struts.taglib.TagUtils;
import org.w3c.dom.html.HTMLElement;

import com.yuanluesoft.cms.pagebuilder.model.recordlist.RecordList;
import com.yuanluesoft.jeaf.util.Environment;
import com.yuanluesoft.jeaf.util.StringUtils;

/**
 * 
 * @author linchuan
 *
 */
public class RecordListUtils {
	
	/**
	 * 生成记录列表元素的HTML,<a id="recordList" urn="..."></a>
	 * @param recordList
	 * @param forTotal
	 */
	public static String gernerateRecordListElement(RecordList recordList, boolean forTotal) {
		String urn = gernerateRecordListProperties(recordList);
		String html = "<a id=\"recordList\"" + (forTotal ? " target=\"total\"" : "");
		for(int i=0; i<urn.length(); i+=4096) {
			String value = urn.substring(i, Math.min(i + 4096, urn.length()));
			html += " urn" + (i==0 ? "" : "" + (i/4096)) + "=\"" + TagUtils.getInstance().filter(value) + "\"";
		}
		return html + "></a>";
	}
	
	/**
	 * 设置记录列表属性
	 * @param element
	 * @param recordList
	 */
	public static void setRecordListProperties(HTMLElement element, RecordList recordList) {
		String urn = gernerateRecordListProperties(recordList);
		for(int i=0; i<urn.length(); i+=4096) {
			String value = urn.substring(i, Math.min(i + 4096, urn.length()));
			element.setAttribute("urn" + (i==0 ? "" : "" + (i/4096)), value);
		}
	}
	
	/**
	 * 生成记录列表元素的属性
	 * @param recordList
	 * @return
	 */
	public static String gernerateRecordListProperties(RecordList recordList) {
		String urn = "recordListName=" + recordList.getRecordListName() +
					 "&privateRecordList=" + recordList.isPrivateRecordList() +
					 (recordList.getRecordClassName()!=null && !recordList.getRecordClassName().isEmpty() ? "&recordClassName=" + recordList.getRecordClassName() : "") + //记录类名称,privateRecordList=true时有效 
					 "&applicationName=" + recordList.getApplicationName() +
					 (recordList.isSimpleMode() ? "&simpleMode=true" : "") +
					 "&recordCount=" + recordList.getRecordCount() +
					 "&recordFormat=" + (recordList.getRecordFormat()==null ? "" : StringUtils.encodePropertyValue(recordList.getRecordFormat())) +
					 "&separatorMode=" + recordList.getSeparatorMode() +
					 (recordList.getSeparatorCustom()!=null && !recordList.getSeparatorCustom().isEmpty() ? "&separatorCustom=" + recordList.getSeparatorCustom() : "") + //记录分隔
					 (recordList.isSeparatorImageOfLastRecord() ? "&separatorImageOfLastRecord=true" : "") + //最后一行是否显示图片
					 (recordList.isSeparatorOfLastRecord() ? "&separatorOfLastRecord=true" : "") + //最后一个记录是否也添加分隔符
					 (recordList.getLinkTitle()==null ? "" : "&linkTitle=" + recordList.getLinkTitle()) +
					 (recordList.getSeparatorHeight()==null || recordList.getSeparatorHeight().isEmpty() ? "" : "&separatorHeight=" + recordList.getSeparatorHeight()) + 
					 (recordList.getSeparatorImage()==null || recordList.getSeparatorImage().isEmpty() ? "" : "&separatorImage=" + recordList.getSeparatorImage()) +
					 (recordList.getAreaWidth()==null || recordList.getAreaWidth().isEmpty() || recordList.getAreaWidth().equals("自动") ? "" : "&areaWidth=" + recordList.getAreaWidth()) +
					 (recordList.getAreaHeight()==null || recordList.getAreaHeight().isEmpty() || recordList.getAreaHeight().equals("自动") ? "" : "&areaHeight=" + recordList.getAreaHeight()) +
					 (recordList.getRecordWidth()==null || recordList.getRecordWidth().isEmpty() || recordList.getRecordWidth().equals("自动") ? "" : "&recordWidth=" + recordList.getRecordWidth()) +
					 (recordList.getRecordHeight()==null || recordList.getRecordHeight().isEmpty() || recordList.getRecordHeight().equals("自动") ? "" : "&recordHeight=" + recordList.getRecordHeight()) +
					 (recordList.getRecordIndent()==null || recordList.getRecordIndent().isEmpty() ? "" : "&recordIndent=" + recordList.getRecordIndent()) +
					 (recordList.getScrollMode()==null || recordList.getScrollMode().equals("none") ? "" : "&scrollMode=" + recordList.getScrollMode()) + 
					 (recordList.getSwitchMode()==null || recordList.getSwitchMode().isEmpty() ? "" : "&switchMode=" + recordList.getSwitchMode()) + 
					 (recordList.isManualSwitch() ? "&manualSwitch=true" : "") + //是否手动切换
					 (recordList.isAutoScaling() ? "&autoScaling=true" : "") + //是否大小自适应
					 (recordList.getControlBarPosition()==null || recordList.getControlBarPosition().isEmpty() ? "" : "&controlBarPosition=" + recordList.getControlBarPosition()) + //显示位置
					 (recordList.getControlBarXMargin()==0 ? "" : "&controlBarXMargin=" + recordList.getControlBarXMargin()) + //水平边距
					 (recordList.getControlBarYMargin()==0 ? "" : "&controlBarYMargin=" + recordList.getControlBarYMargin()) + //垂直边距
					 (recordList.getScrollDirection()==null || recordList.getScrollDirection().isEmpty() ? "" : "&scrollDirection=" + recordList.getScrollDirection()) + //滚动方向
					 (recordList.getControlBarRecordSpacing()==0 ? "" : "&controlBarRecordSpacing=" + recordList.getControlBarRecordSpacing()) + //控制栏记录分隔距离
					 (recordList.getMouseOverControlBarRecord()==null || recordList.getMouseOverControlBarRecord().isEmpty() ? "" : "&mouseOverControlBarRecord=" + recordList.getMouseOverControlBarRecord()) + //鼠标经过控制记录时的动作
					 (recordList.getClickControlBarRecord()==null || recordList.getClickControlBarRecord().isEmpty() ? "" : "&clickControlBarRecord=" + recordList.getClickControlBarRecord()) + //点击控制记录时的动作
					 (recordList.getControlBarFormat()==null || recordList.getControlBarFormat().isEmpty() ? "" : "&controlBarFormat=" + StringUtils.encodePropertyValue(recordList.getControlBarFormat())) + //控制栏格式
					 (recordList.getImageClickAction()==null || recordList.getImageClickAction().isEmpty() ? "" : "&imageClickAction=" + recordList.getImageClickAction()) + //点击图片操作
					 (recordList.isSwitchByKey() ? "&switchByKey=true" : "") + //左右方向键翻阅
					 (recordList.getDisplayNextPreviousButton()==null || recordList.getDisplayNextPreviousButton().isEmpty() ?  "" : "&displayNextPreviousButton=" + recordList.getDisplayNextPreviousButton()) + //是否显示前一页、后一页按钮
					 (recordList.getNextPreviousButtonFormat()==null || recordList.getNextPreviousButtonFormat().isEmpty() ? "" : "&nextPreviousButtonFormat=" + StringUtils.encodePropertyValue(recordList.getNextPreviousButtonFormat())) + //前一页、后一页按钮格式
					 (!recordList.isScrollJoin() ? "" : "&scrollJoin=true") +
					 (recordList.getScrollSpeed()>0  ? "&scrollSpeed=" + recordList.getScrollSpeed() : "") +
					 (recordList.getScrollAmount()>0  ? "&scrollAmount=" + recordList.getScrollAmount() : "") +
					 "&linkOpenMode=" + recordList.getLinkOpenMode() +
					 (recordList.isTableMode() ? "&tableMode=true" : "") +
					 (recordList.isSearchResults() ? "&searchResults=true" : "") +
					 (recordList.getEmptyPrompt()!=null && !recordList.getEmptyPrompt().isEmpty() ? "&emptyPrompt=" + StringUtils.encodePropertyValue(recordList.getEmptyPrompt()) : "") + //记录列表为空时的提示
					 "&extendProperties=" + (recordList.getExtendProperties()==null ? "" : StringUtils.encodePropertyValue(recordList.getExtendProperties()));
		return urn;
	}
	
	/**
	 * 获取预置的记录列表控制栏格式
	 * @param switchMode
	 * @return
	 */
	public static String getDefaultControlBarFormat(String switchMode) {
		if("number".equals(switchMode)) { //数字
			String fieldHTML = "<a id=\"field\" urn=\"name=recordNumber";
			for(int i=1; i<=20; i++) {
				fieldHTML += "&amp;imageInstead_recordNumber_" + i + "=" + Environment.getContextPath() + "/cms/image/" + i + ".gif";
			}
			fieldHTML += "&amp;insteadImageWidth=&amp;insteadImageHeight=&amp;insteadImageAlign=absmiddle\">&lt;序号&gt;</a>";
			return "<span class=\"numberBar\">" +
				   "<span class=\"number\" id=\"unselectedRecord\" onmouseover=\"this.className='number numberOver';\" onmouseout=\"this.className='number';\">" + fieldHTML + "</span>" +
				   "<span class=\"number numberCurrent\" id=\"selectedRecord\">" + fieldHTML + "</span>" +
				   "</span>";
		}
		else if("bottomSmallImage".equals(switchMode)) { //底部小图
			return "<span class=\"smallImage\" id=\"unselectedRecord\">" +
				   "<a id=\"field\" urn=\"name=firstImageName&amp;imageWidth=0&amp;imageHeight=0&amp;imageAlign=absmiddle\">&lt;记录图片&gt;</a>" +
				   "</span>" +
				   "<span class=\"smallImage smallImageCurrent\" id=\"selectedRecord\">" +
				   "<span class=\"smallImageTopArrowBox\">" +
				   "<span class=\"smallImageTopArrowOuter\">◆</span>" +
				   "<span class=\"smallImageTopArrowInner\">◆</span>" +
				   "</span>" +
				   "<br>" +
				   "<a id=\"field\" style=\"position: relative;\" urn=\"name=firstImageName&amp;imageWidth=0&amp;imageHeight=0&amp;imageAlign=absmiddle\">&lt;记录图片&gt;</a>" +
				   "</span>";
		}
		else if("rightSmallImage".equals(switchMode)) { //右侧小图
			return "<span class=\"smallImage\" id=\"unselectedRecord\">" +
				   "<a id=\"field\" urn=\"name=firstImageName&amp;imageWidth=0&amp;imageHeight=0&amp;imageAlign=absmiddle\">&lt;记录图片&gt;</a>" +
				   "</span>" +
				   "<span class=\"smallImage smallImageCurrent\" id=\"selectedRecord\">" +
				   "<span class=\"smallImageLeftArrowBox\">" +
				   "<span class=\"smallImageLeftArrowOuter\">◆</span>" +
				   "<span class=\"smallImageLeftArrowInner\">◆</span>" +
				   "</span>" +
				   "<a id=\"field\" style=\"position: relative;\" urn=\"name=firstImageName&amp;imageWidth=0&amp;imageHeight=0&amp;imageAlign=absmiddle\">&lt;记录图片&gt;</a>" +
				   "</span>";
		}
		else if("roundDot".equals(switchMode)) { //圆点
			return "<span class=\"roundDot\" id=\"unselectedRecord\"></span>" +
				   "<span class=\"roundDot roundDotCurrent\" id=\"selectedRecord\"></span>";
		}
		else if("squareDot".equals(switchMode)) { //方点
			return "<span id=\"unselectedRecord\" class=\"squareDot\"></span>" +
				   "<span id=\"selectedRecord\" class=\"squareDot squareDotCurrent\"></span>";
		}
		return null;
	}
}