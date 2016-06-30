<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/report/listEnsureUnitCategories">
<div style="height:230px"">
	<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center" class="tdtitle" nowrap="nowrap" width="120px">分类</td>
			<td align="center" class="tdtitle" nowrap="nowrap">单位列表</td>
		</tr>
		<ext:iterate id="ensureUnitCategory" property="ensureUnitCategories">
			<tr style="cursor:pointer" valign="top" onclick="PageUtils.editrecord('cms/siteresource', 'report/ensureUnitCategory', '<ext:write name="ensureUnitCategory" property="id"/>', 'mode=dialog,width=430,height=300')">
				<td class="tdcontent"><ext:write name="ensureUnitCategory" property="category"/></td>
				<td class="tdcontent"><ext:write name="ensureUnitCategory" property="unitNames"/></td>
			</tr>
		</ext:iterate>
	</table>
</div>
</ext:form>