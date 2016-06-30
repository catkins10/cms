<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">角色</td>
		<td class="tdcontent" ><ext:write property="role"/></td>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdcontent" ><ext:write property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">职务</td>
		<td class="tdcontent"><ext:write property="job"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:write property="tel"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>