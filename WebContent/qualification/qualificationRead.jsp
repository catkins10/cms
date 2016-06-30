<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col align="left">
	<col width="50%">
	<col align="left">
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">学生</td>
		<td class="tdcontent"><ext:field writeonly="true" property="stuName"/></td>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">级别</td>
		<td class="tdcontent"><ext:field writeonly="true" property="level"/></td>
		<td class="tdtitle" nowrap="nowrap">颁发机构</td>
		<td class="tdcontent"><ext:field writeonly="true" property="authority"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">获得时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="obtainTime"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>
