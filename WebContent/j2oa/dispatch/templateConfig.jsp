<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveTemplateConfig">
	<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
		<col valign="middle">
		<col valign="middle" width="100%">
		<tr>
			<td class="tdtitle" nowrap="nowrap">适用的文种</td>
			<td class="tdcontent"><ext:field property="docTypes"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">适用的文件字</td>
			<td class="tdcontent"><ext:field property="docWords"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap" valign="top">办理单模板</td>
			<td class="tdcontent"><ext:field property="handlingTemplate" /></td>
		</tr>
	</table>
</ext:form>