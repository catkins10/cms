<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveDevelopmentAreaCategory">
	<table border="0" width="100%" cellspacing="0" cellpadding="3px">
		<col align="right">
		<col width="100%">
		<tr>
			<td>分类：</td>
			<td><ext:field property="developmentAreaCategory.category"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap">开发区：</td>
			<td><ext:field property="developmentAreaCategory.developmentAreas"/></td>
		</tr>
	</table>
	<input type="hidden" name="allDevelopmentAreas"/>
	<script>
		document.getElementsByName('allDevelopmentAreas')[0].value = DialogUtils.getDialogOpener().document.getElementsByName('developmentArea')[0].value + DialogUtils.getDialogOpener().document.getElementById('spanDevelopmentAreaCategories').innerHTML;
	</script>
</ext:form>