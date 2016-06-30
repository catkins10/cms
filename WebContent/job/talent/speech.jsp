<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveSpeech">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<tr>
			<td nowrap="nowrap" align="right">语言类别：</td>
			<td><ext:field property="language"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">掌握程度：</td>
			<td><ext:field property="level"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">读写能力：</td>
			<td><ext:field property="literacy"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" align="right">听说能力：</td>
			<td><ext:field property="spoken"/></td>
		</tr>
	</table>
</ext:form>