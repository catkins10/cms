<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newPutPage() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/advert/admin/advertPutPage.shtml?id=<ext:write property="id"/>', 600, 300);
}
function openPutPage(putPageId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/advert/admin/advertPutPage.shtml?id=<ext:write property="id"/>&putPage.id=' + putPageId, 600, 300);
}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加投放页面" onclick="newPutPage()">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">站点/栏目</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">页面</td>
		<td class="tdtitle" nowrap="nowrap" width="150">投放方式</td>
		<td class="tdtitle" nowrap="nowrap" width="150">客户端类型</td>
	</tr>
	<ext:iterate id="putPage" indexId="putPageIndex" property="putPages">
		<tr style="cursor:pointer" align="center" onclick="openPutPage('<ext:write name="putPage" property="id"/>')">
			<td class="tdcontent"><ext:writeNumber name="putPageIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:write name="putPage" property="siteNames"/></td>
			<td class="tdcontent" align="left"><ext:field writeonly="true" name="putPage" property="pageTitles"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="putPage" property="mode"/></td>
			<td class="tdcontent"><ext:field writeonly="true" name="putPage" property="clientTypeArray"/></td>
		</tr>
	</ext:iterate>
</table>