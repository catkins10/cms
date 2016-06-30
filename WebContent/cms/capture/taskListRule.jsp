<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
	<tr valign="top">
		<td>
			<div style="font-weight:bold; padding-bottom:3px">页面：</div>
			<iframe id="listPageFrame" name="listPageFrame" width="100%" height="200"  style="border: #999 1px solid; background-color:#fff" src="about:blank"></iframe>
			<div style="font-weight:bold; padding-top:4px; padding-bottom:3px">源文件：</div>
			<iframe id="listPageHtmlFrame" name="listPageHtmlFrame" width="100%" height="200" style="border: #999 1px solid; background-color:#fff"></iframe>
		</td>
		<td style="padding-left:10px" width="300px">
			<div style="padding-top:4px; padding-bottom:2px">
				<b>记录URL：</b><ext:field property="recordPageURLDirection" onclick="onRecordPageURLDirectionChanged()"/>
			</div>
			<div id="recordPageURLDirectionByURL" style="<ext:notEqual value="1" property="recordPageURLDirection">display:none</ext:notEqual>">
				<div style="font-weight:bold; padding-top:6px; padding-bottom:3px">记录页面URL格式：<input type="button" value="插入记录ID" class="button" onmousedown="FormUtils.pasteText('recordPageURL', '&lt;记录ID&gt;')"/></div>
				<ext:field property="recordPageURL"/>
			</div>
			<div style="padding-top:4px; padding-bottom:2px">
				<b>下一页URL：</b><ext:field property="nextPageDirection" onclick="onNextPageDirectionChanged()"/>
			</div>
			<div id="nextPageDirectionByURL" style="<ext:notEqual value="1" property="nextPageDirection">display:none</ext:notEqual>">
				<div style="font-weight:bold; padding-top:4px; padding-bottom:2px">
					<div style="float:left">
						下一页URL格式：<input type="button" value="插入页码" class="button" onclick="FormUtils.pasteText('nextPageURL', '&lt;页码&gt;')"/>
						起始页码：
					</div>
					<div style="float:left"><ext:field property="beginPage" style="width: 30px"/></div>
				</div>
				<div style="clear:both; padding-top: 1px;">
					<ext:field property="nextPageURL"/>
				</div>
			</div>
			<div style="clear:both; font-weight:bold; padding-top:4px; padding-bottom:2px">抓取位置配置：
				<input type="button" class="button" value="上移" style="width:40px" onclick="listCaptureRuleConfigure.moveField(true)">
				<input type="button" class="button" value="下移" style="width:40px" onclick="listCaptureRuleConfigure.moveField(false)">
			</div>
			<div style="width:100%;">
				<div style="width:100%; height: 180px; border: #e0e0e0 1px solid; background-color:white; margin-top:0px; overflow:auto">
					<table id="listFieldTable" border="0" width="100%" cellspacing="0" cellpadding="2px" onselectstart="return false;">
						<ext:iterate id="field" property="listPageFields">
							<tr id="listField_<ext:write name="field" property="fieldName"/>" onclick="listCaptureRuleConfigure.onSelectField(this)" ondblclick="listCaptureRuleConfigure.onDblclickField(this)" title="双击，自动移动到有效字段后">
								<td style="border-bottom: #f0f0f0 1 solid;" nowrap="true" width="32px" align="center">
									<input type="checkbox" class="checkbox">
								</td>
								<td style="border-bottom: #f0f0f0 1 solid;" nowrap="true">
									<ext:write name="field" property="fieldTitle"/>
									<input type="hidden" name="listFieldName" value="<ext:write name="field" property="fieldName"/>"/>
									<input type="hidden" name="listField_<ext:write name="field" property="fieldName"/>_fieldType" value="<ext:write name="field" property="fieldType"/>"/>
									<input type="hidden" name="listField_<ext:write name="field" property="fieldName"/>_begin" value="<ext:write name="field" property="fieldBegin"/>"/>
									<input type="hidden" name="listField_<ext:write name="field" property="fieldName"/>_end" value="<ext:write name="field" property="fieldEnd"/>"/>
									<input type="hidden" name="listField_<ext:write name="field" property="fieldName"/>_arraySeparator" value="<ext:write name="field" property="arraySeparator"/>"/>
									<input type="hidden" name="listField_<ext:write name="field" property="fieldName"/>_value" value="<ext:write name="field" property="value"/>"/>
									<input type="hidden" name="listField_<ext:write name="field" property="fieldName"/>_format" value="<ext:write name="field" property="fieldFormat"/>"/>
								</td>
							</tr>
						</ext:iterate>
					</table>
				</div>
			</div>
			<div style="font-weight:bold; padding-top:4px; padding-bottom:2px">开始位置：</div>
			<ext:field property="listFieldBegin" onblur="listCaptureRuleConfigure.saveFieldConfig()"/>
			<div style="font-weight:bold; padding-top:4px; padding-bottom:2px">结束位置：</div>
			<ext:field property="listFieldEnd" onblur="listCaptureRuleConfigure.saveFieldConfig()"/>
			<div id="listArraySeparatorDiv" style="display1:none">
				<div style="font-weight:bold; padding-top:4px; padding-bottom:2px">分隔符：</div>
				<ext:field property="listArraySeparator" onblur="listCaptureRuleConfigure.saveFieldConfig()"/>
			</div>
			<div style="font-weight:bold; padding-top:4px; padding-bottom:2px">默认值：</div>
			<ext:field property="listFieldValue" onblur="listCaptureRuleConfigure.saveFieldConfig()"/>
			<div id="listFieldFormatDiv" style="display:none">
				<div style="font-weight:bold; padding-top:4px; padding-bottom:2px">日期格式：</div>
				<ext:field property="listFieldFormat" onblur="listCaptureRuleConfigure.saveFieldConfig()"/>
			</div>
		</td>
	</tr>
</table>
<script>
	function onRecordPageURLDirectionChanged() {
		var recordPageURLDirections = document.getElementsByName("recordPageURLDirection");
		document.getElementById("recordPageURLDirectionByURL").style.display = recordPageURLDirections[1].checked ? "" : "none";
	}
	function onNextPageDirectionChanged() {
		var nextPageDirections = document.getElementsByName("nextPageDirection");
		document.getElementById("nextPageDirectionByURL").style.display = nextPageDirections[1].checked ? "" : "none";
	}
	var listCaptureRuleConfigure = new CaptureRuleConfigure('list', '<ext:write property="listPageURL"/>');
</script>