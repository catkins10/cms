<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
		<tr>
		<td class="tdtitle" nowrap="nowrap">领证单位全称</td>
		<td class="tdcontent" ><ext:write property="name"/></td>
		<td class="tdtitle" nowrap="nowrap">企业地址</td>
		<td class="tdcontent" ><ext:write property="addr"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">证书编号</td>
		<td class="tdcontent"><ext:write property="licenseNum"/></td>
		<td class="tdtitle" nowrap="nowrap">发证机关</td>
		<td class="tdcontent"><ext:write property="deparment"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">签发日期</td>
		<td class="tdcontent"><ext:write property="startDate"/></td>
		<td class="tdtitle" nowrap="nowrap">有效期限（截止日）</td>
		<td class="tdcontent"><ext:write property="endDate"/></td>
	</tr>

	
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>