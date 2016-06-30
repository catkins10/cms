<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">单位名称</td>
		<td class="tdcontent" ><ext:field property="unit"/></td>
		<td class="tdtitle" nowrap="nowrap">行业</td>
		<td class="tdcontent" ><ext:field property="industry"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">所属乡镇</td>
		<td class="tdcontent"><ext:field property="level"/></td>
		<td class="tdtitle" nowrap="nowrap">证书编号</td>
		<td class="tdcontent"><ext:field property="bookNum"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">达标日期</td>
		<td class="tdcontent"><ext:field property="startDate"/></td>
		<td class="tdtitle" nowrap="nowrap">有效期</td>
		<td class="tdcontent"><ext:field property="usefulDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">行业主管单位</td>
		<td class="tdcontent" colspan="3"><ext:field property="mainUnit"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>