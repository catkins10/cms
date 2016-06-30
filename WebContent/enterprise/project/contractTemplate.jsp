<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveContractTemplate">
   	<table  width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col >
		<col valign="middle" width="100%">
		<tr>
			<td nowrap="nowrap" class="tdtitle">模板名称</td>
			<td class="tdcontent"><ext:field property="name"/></td>
		</tr>
		<tr>
			<td nowrap="nowrap" class="tdtitle">适用的项目类型</td>
			<td class="tdcontent"><ext:field property="appliedProjectTypeArray"/></td>
		</tr>
	</table>
</ext:form>