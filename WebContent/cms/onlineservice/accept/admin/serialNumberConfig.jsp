<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/serialNumberConfigSave">
<table width="100%" border="0" cellpadding="3" cellspacing="3" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td nowrap="nowrap" valign="top">流水号格式</td>
		<td >
			<input type="button" class="button" value="插入办事事项" style="width:80px" onclick="FormUtils.pasteText('content','<办事事项>')">
			<input type="button" class="button" value="插入年" style="width:72px" onclick="FormUtils.pasteText('content','<年>')">
			<input type="button" class="button" value="插入月" style="width:72px" onclick="FormUtils.pasteText('content','<月>')">
			<input type="button" class="button" value="插入日" style="width:72px" onclick="FormUtils.pasteText('content','<日>')">
			<input type="button" class="button" value="插入序号" style="width:72px" onclick="FormUtils.pasteText('content','<序号>')">
			<br><br>
			<ext:field property="content"/>
		</td>
	</tr>
</table>
</ext:form>