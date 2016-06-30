<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">类型</td>
		<td class="tdcontent"><ext:field writeonly="true" property="type"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">地区</td>
		<td class="tdcontent"><ext:field writeonly="true" property="city"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">座位数</td>
		<td class="tdcontent"><ext:field writeonly="true" property="seat"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>