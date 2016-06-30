<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/report/listEnsureColumnConfigs">
<div style="height:230px"">
	<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center" class="tdtitle" nowrap="nowrap">栏目</td>
			<td align="center" class="tdtitle" nowrap="nowrap" width="80px">计分方式</td>
			<td align="center" class="tdtitle" nowrap="nowrap" width="60px">分数</td>
		</tr>
		<ext:iterate id="ensureColumnConfig" property="ensureColumnConfigs">
			<tr style="cursor:pointer" valign="top" onclick="PageUtils.editrecord('cms/siteresource', 'report/ensureColumnConfig', '<ext:write name="ensureColumnConfig" property="id"/>', 'mode=dialog,width=500,height=300')">
				<td class="tdcontent"><ext:write name="ensureColumnConfig" property="columnNames"/></td>
				<td class="tdcontent"><ext:field name="ensureColumnConfig" property="mode" writeonly="true"/></td>
				<td class="tdcontent" align="center"><ext:write name="ensureColumnConfig" property="score"/></td>
			</tr>
		</ext:iterate>
	</table>
</div>
</ext:form>