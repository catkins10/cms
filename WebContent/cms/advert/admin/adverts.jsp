<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
function newAdvert() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/advert/admin/advert.shtml?siteId=<ext:write property="siteId"/>&opener=<ext:notEmpty name="customerId">customer&customerId=<ext:write name="customerId"/></ext:notEmpty><ext:notEmpty name="spaceId">space&spaceId=<ext:write name="spaceId"/></ext:notEmpty>', '80%', 300);
}
function openAdvert(advertId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/advert/admin/advert.shtml?opener=<ext:notEmpty name="customerId">customer</ext:notEmpty><ext:notEmpty name="spaceId">space</ext:notEmpty>&id=' + advertId, '80%', 500);
}
function putAdvert(advertId) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/cms/advert/admin/advertPut.shtml?siteId=<ext:write property="siteId"/>&advertId=' + advertId, 500, 500);
}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加广告" onclick="newAdvert()">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="23px" valign="bottom" align="center">
		<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
		<td class="tdtitle" nowrap="nowrap" width="30%">名称</td>
		<td class="tdtitle" nowrap="nowrap" width="30%">广告位</td>
		<td class="tdtitle" nowrap="nowrap" width="40%">客户名称</td>
		<ext:equal value="true" name="editabled">
			<td class="tdtitle" nowrap="nowrap" width="60px">广告投放</td>
		</ext:equal>
	</tr>
	<ext:iterate id="advert" indexId="advertIndex" property="adverts">
		<tr style="cursor:pointer" align="center">
			<td class="tdcontent" onclick="openAdvert('<ext:write name="advert" property="id"/>')"><ext:writeNumber name="advertIndex" plus="1"/></td>
			<td class="tdcontent" align="left" onclick="openAdvert('<ext:write name="advert" property="id"/>')"><ext:field writeonly="true" name="advert" property="name"/></td>
			<td class="tdcontent" align="left" onclick="openAdvert('<ext:write name="advert" property="id"/>')"><ext:field writeonly="true" name="advert" property="spaceName"/></td>
			<td class="tdcontent" align="left" onclick="openAdvert('<ext:write name="advert" property="id"/>')"><ext:field writeonly="true" name="advert" property="customerName"/></td>
			<ext:equal value="true" name="editabled">
				<td class="tdcontent"><input type="button" class="button" onclick="putAdvert('<ext:write name="advert" property="id"/>')" value="投放"/></td>
			</ext:equal>
		</tr>
	</ext:iterate>
</table>