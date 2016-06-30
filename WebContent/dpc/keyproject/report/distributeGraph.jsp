<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/writeDistributeGraph" target="graphFrame" method="get">
	<table border="0" cellpadding="3" cellspacing="3" style="color:#000000" width="100%">
		<tr>
			<td nowrap>年度：</td>
			<td width="33%"><ext:field property="year"/></td>
			<td nowrap>项目类别：</td>
			<td width="33%"><ext:field property="classify"/></td>
			<td nowrap>报表类型：</td>
			<td width="33%"><ext:field property="graphByTitle"/></td>
			<td nowrap>&nbsp;<input onclick="FormUtils.submitForm()" type="button" class="button" value="确定"></td>
		</tr>
	</table>
	<iframe onload="frames['graphFrame'].document.body.style.backgroundColor='transparent'" name="graphFrame" width="660" height="390" frameborder="0" allowTransparency="true" style="background-color: transparent" scrolling="no" marginheight="0" marginwidth="0"></iframe>
	<html:hidden property="width" value="660"/>
	<html:hidden property="height" value="390"/>
</ext:form>