<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">活动名称</td>
		<td class="tdcontent"><ext:field property="fairName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">届别</td>
		<td class="tdcontent"><ext:field property="fairNumber"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">县别</td>
		<td class="tdcontent"><ext:field property="county"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">总签约目标</td>
		<td class="tdcontent"><ext:field property="signTarget"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">合同项目目标</td>
		<td class="tdcontent"><ext:field property="contractTarget"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">意向项目目标</td>
		<td class="tdcontent"><ext:field property="orderTarget"/></td>
	</tr>
</table>