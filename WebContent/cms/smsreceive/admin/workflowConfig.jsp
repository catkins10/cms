<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/admin/saveWorkflowConfig">
   	<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col>
		<col width="100%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">机构名称</td>
			<td class="tdcontent"><ext:field property="orgName"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">流程名称</td>
			<td class="tdcontent"><ext:field property="workflowName"/></td>
		</tr>
	</table>
</ext:form>