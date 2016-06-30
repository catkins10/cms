<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/recordCaptureTest">
	<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
		<tr align="center">
			<td width="36px" nowrap="nowrap" class="tdtitle">序号</td>
			<td width="60px" nowrap="nowrap" class="tdtitle">字段名称</td>
			<td width="100%" class="tdtitle">字段值</td>
		</tr>
		<ext:notEmpty property="capturedRecord">
			<ext:iterate id="field" indexId="fieldIndex" property="capturedRecord.fields">
				<tr valign="top">
					<td class="tdcontent" align="center"><ext:writeNumber name="fieldIndex" plus="1"/></td>
					<td class="tdcontent" nowrap="nowrap"><ext:write name="field" property="fieldTitle"/></td>
					<td class="tdcontent" style="word-break: break-all;">
<%						request.setAttribute("field", org.apache.struts.taglib.TagUtils.getInstance().lookup(pageContext, "field", null, "page"));
						request.setAttribute("record", org.apache.struts.taglib.TagUtils.getInstance().lookup(pageContext, "recordCaptureTest", "capturedRecord", "request")); %>
						<jsp:include page="writeCaptureField.jsp"/>
					</td>
				</tr>
			</ext:iterate>
		</ext:notEmpty>
	</table>
</ext:form>