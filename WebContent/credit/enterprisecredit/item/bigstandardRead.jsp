<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="50%">
	<col>
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">单位</td>
		<td class="tdcontent" ><ext:write property="unit"/></td>
		<td class="tdtitle" nowrap="nowrap">企业名称</td>
		<td class="tdcontent" ><ext:write property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">行业</td>
		<td class="tdcontent"><ext:write property="industry"/></td>
		<td class="tdtitle" nowrap="nowrap">拟达标等级</td>
		<td class="tdcontent"><ext:write property="level"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">进展情况</td>
		<td class="tdcontent"><ext:write property="situation"/></td>
		<td class="tdtitle" nowrap="nowrap">证书编号</td>
		<td class="tdcontent"><ext:write property="bookNum"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">有效期</td>
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