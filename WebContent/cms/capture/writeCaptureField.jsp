<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:instanceof className="String" name="field" property="fieldValue">
	<ext:write name="field" property="fieldValue" filter="false"/>
	<ext:equal value="timestamp" name="field" property="fieldType">
		[<ext:write name="record" property="<%="record." + ((com.yuanluesoft.cms.capture.model.CapturedField)request.getAttribute("field")).getFieldName()%>" format="yyyy-MM-dd HH:mm:ss"/>]
	</ext:equal>
	<ext:equal value="date" name="field" property="fieldType">
		[<ext:write name="record" property="<%="record." + ((com.yuanluesoft.cms.capture.model.CapturedField)request.getAttribute("field")).getFieldName()%>" format="yyyy-MM-dd"/>]
	</ext:equal>
	<ext:equal value="html" name="field" property="fieldType">
		<br/><br/>HTML:<br/>
		<ext:write name="field" property="fieldValue"/>
	</ext:equal>
</ext:instanceof>
<ext:notInstanceof className="String" name="field" property="fieldValue">
	<ext:iterate id="component" name="field" property="fieldValue">
		<table border="0" cellpadding="1" cellspacing="0">
			<ext:iterate id="componentField" name="component" property="fields">
				<tr>
<%					request.setAttribute("field", org.apache.struts.taglib.TagUtils.getInstance().lookup(pageContext, "componentField", null, "page"));
					request.setAttribute("record", org.apache.struts.taglib.TagUtils.getInstance().lookup(pageContext, "component", null, "page")); %>
					<td nowrap="nowrap" valign="top"><ext:write name="field" property="fieldTitle"/>：</td>
					<td><jsp:include page="writeCaptureField.jsp"/></td>
				</tr>
			</ext:iterate>
		</table>
		<br/>
	</ext:iterate>
</ext:notInstanceof>