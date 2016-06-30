<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
	<tr valign="top">
		<td>
			<div style="font-weight:bold; padding-bottom:3px">页面：</div>
			<iframe id="contentPageFrame" name="contentPageFrame" width="100%" height="200" src="about:blank" style="border: #999 1px solid; background-color:#fff;"></iframe>
			<div style="font-weight:bold; padding-top:5px; padding-bottom:3px">源文件：</div>
			<iframe id="contentPageHtmlFrame" name="contentPageHtmlFrame" width="100%" height="200" style="border: #999 1px solid; background-color:#fff;"></iframe>
		</td>
		<td style="padding-left:10px" width="300px">
			<div style="clear:both; font-weight:bold; padding-top:4px;">抓取位置配置：
				<input type="button" class="button" value="上移" style="width:40px" onclick="contentCaptureRuleConfigure.moveField(true)">
				<input type="button" class="button" value="下移" style="width:40px" onclick="contentCaptureRuleConfigure.moveField(false)">
			</div>
			<div style="width:100%;">
				<div style="width:100%; height: 220px; border: #e0e0e0 1px solid; background-color:white; margin-top:3px; overflow:auto">
					<table id="contentFieldTable" border="0" width="100%" cellspacing="0" cellpadding="2px" onselectstart="return false;">
						<ext:iterate id="field" property="contentPageFields">
							<tr onclick="contentCaptureRuleConfigure.onSelectField(this)" ondblclick="contentCaptureRuleConfigure.onDblclickField(this)" id="contentField_<ext:write name="field" property="fieldName"/>" title="双击，自动移动到有效字段后">
								<td style="border-bottom: #f0f0f0 1 solid;" nowrap="true" width="32px" align="center">
									<input type="checkbox" class="checkbox">
								</td>
								<td style="border-bottom: #f0f0f0 1 solid;" nowrap="true">
									<ext:write name="field" property="fieldTitle"/>
									<input type="hidden" name="contentFieldName" value="<ext:write name="field" property="fieldName"/>"/>
									<input type="hidden" name="contentField_<ext:write name="field" property="fieldName"/>_fieldType" value="<ext:write name="field" property="fieldType"/>"/>
									<input type="hidden" name="contentField_<ext:write name="field" property="fieldName"/>_begin" value="<ext:write name="field" property="fieldBegin"/>"/>
									<input type="hidden" name="contentField_<ext:write name="field" property="fieldName"/>_end" value="<ext:write name="field" property="fieldEnd"/>"/>
									<input type="hidden" name="contentField_<ext:write name="field" property="fieldName"/>_arraySeparator" value="<ext:write name="field" property="arraySeparator"/>"/>
									<input type="hidden" name="contentField_<ext:write name="field" property="fieldName"/>_value" value="<ext:write name="field" property="value"/>"/>
									<input type="hidden" name="contentField_<ext:write name="field" property="fieldName"/>_format" value="<ext:write name="field" property="fieldFormat"/>"/>
								</td>
							</tr>
						</ext:iterate>
					</table>
				</div>
			</div>
			<div style="font-weight:bold; padding-top:4px; padding-bottom:2px">开始位置：</div>
			<ext:field property="contentFieldBegin" onblur="contentCaptureRuleConfigure.saveFieldConfig()"/>
			<div style="font-weight:bold; padding-top:4px; padding-bottom:2px">结束位置：</div>
			<ext:field property="contentFieldEnd" onblur="contentCaptureRuleConfigure.saveFieldConfig()"/>
			<div id="contentArraySeparatorDiv" style="display1:none">
				<div style="font-weight:bold; padding-top:4px; padding-bottom:2px">分隔符：</div>
				<ext:field property="contentArraySeparator" onblur="contentCaptureRuleConfigure.saveFieldConfig()"/>
			</div>
			<div style="font-weight:bold; padding-top:4px; padding-bottom:2px">默认值：</div>
			<ext:field property="contentFieldValue" onblur="contentCaptureRuleConfigure.saveFieldConfig()"/>
			<div id="contentFieldFormatDiv" style="display:none">
				<div style="font-weight:bold; padding-top:4px; padding-bottom:2px">日期格式：</div>
				<ext:field property="contentFieldFormat" onblur="contentCaptureRuleConfigure.saveFieldConfig()"/>
			</div>
		</td>
	</tr>
</table>
<script>
	var contentCaptureRuleConfigure = new CaptureRuleConfigure('content', '<ext:write property="contentPageURL"/>');
</script>