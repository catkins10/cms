<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveOpinionType">
	<table border="0" cellpadding="3" cellspacing="0" style="color:#000000" width="100%">
		<col align="right">
		<col width="100%">
		<tr>
			<td nowrap>意见类型：</td>
			<td><ext:field property="opinionType"/></td>
		</tr>
		<tr>
			<td>是否必填：</td>
			<td><ext:field property="required"/></td>
		</tr>
		<tr>
			<td nowrap>未填写时提示：</td>
			<td><ext:field property="inputPrompt"/></td>
		</tr>
	</table>
</ext:form>