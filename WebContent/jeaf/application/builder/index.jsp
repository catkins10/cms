<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveIndex">
	<script type="text/javascript" src="<%=request.getContextPath()%>/jeaf/application/builder/js/builder.js"></script>
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap="nowrap" valign="top" style="padding-top: 8px">字段列表：</td>
			<td>
				<span id="spanFields"></span>
				<input type="button" class="button" onclick="setFields('index.field', '<ext:write property="id"/>', 'spanFields')" value="选择"/>
			</td>
		</tr>
	</table>
	<script>writeFields('index.field', 'spanFields');</script>
</ext:form>