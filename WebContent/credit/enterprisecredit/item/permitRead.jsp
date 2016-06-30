<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">许可证号</td>
		<td class="tdcontent" ><ext:write property="permitNum"/></td>
		<td class="tdtitle" nowrap="nowrap">单位名称</td>
		<td class="tdcontent" ><ext:write property="unit"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">许可经营范围</td>
		<td class="tdcontent"><ext:write property="range"/></td>
		<td class="tdtitle" nowrap="nowrap">办结时间</td>
		<td class="tdcontent"><ext:write property="overTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">证书有效期</td>
		<td class="tdcontent"><ext:write property="usefulDate"/></td>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:write property="remark"/></td>
	</tr>
	
	<tr>
		<td class="tdtitle" nowrap="nowrap">登记人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">登记时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>